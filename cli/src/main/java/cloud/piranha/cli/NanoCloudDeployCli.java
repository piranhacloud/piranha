/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha.cli;

import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;

import static java.net.http.HttpClient.Redirect.ALWAYS;

/**
 * The Piranha Nano Cloud Deploy CLI.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@CommandLine.Command(name = "deploy", description = "Deploy Piranha Nano application to the Cloud")
public class NanoCloudDeployCli implements Runnable {

    /**
     * Stores the (application) name.
     */
    @CommandLine.Option(names = "--name", required = true, description = "The name of the application")
    private String name;

    /**
     * Stores the password.
     */
    @CommandLine.Option(names = "--password", required = true, arity = "0..1", interactive = true, description = "The password to deploy with")
    private char[] password;

    /**
     * Stores the username.
     */
    @CommandLine.Option(names = "--username", required = true, description = "The username to deploy with")
    private String username;

    /**
     * Execute 'pi nano cloud deploy'.
     */
    @Override
    public void run() {
        File file = new File("target/azure.zip");
        if (!file.exists()) {
            System.out.println("The target/azure.zip file does not exist, please run 'pi nano cloud build' first.");
            System.exit(1);
        }
        try {
            System.out.println("Executing - POST https://" + name + ".scm.azurewebsites.net/api/zipdeploy");
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(ALWAYS)
                    .authenticator(new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }
                    })
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(BodyPublishers.ofFile(Path.of("target/azure.zip")))
                    .uri(URI.create("https://" + name + ".scm.azurewebsites.net/api/zipdeploy"))
                    .build();
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            System.out.println("Response status: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
