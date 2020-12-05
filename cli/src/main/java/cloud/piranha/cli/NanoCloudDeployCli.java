/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import static java.net.http.HttpClient.Redirect.ALWAYS;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;
import java.util.List;

/**
 * The Piranha Nano Cloud Deploy CLI.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoCloudDeployCli {

    /**
     * Stores the pattern.
     */
    private static final String PATTERN = "  %-38s: %s\n";

    /**
     * Stores the (application) name.
     */
    private String name;

    /**
     * Stores the password.
     */
    private String password;

    /**
     * Stores the username.
     */
    private String username;

    /**
     * Execute 'pi nano cloud deploy'.
     *
     * @param arguments the arguments.
     */
    public void execute(List<String> arguments) {
        parse(arguments);
        if (name == null) {
            usage();
        } else {
            File file = new File("target/azure.zip");
            if (!file.exists()) {
                System.out.println("The target/azure.zip file does not exist, please run 'pi nano cloud build' first.");
                System.exit(1);
            }
        }
        try {
            System.out.println("Executing - POST https://" + name + ".scm.azurewebsites.net/api/zipdeploy");
            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(ALWAYS)
                    .authenticator(new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password.toCharArray());
                        }
                    })
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(BodyPublishers.ofFile(Path.of("target/azure.zip")))
                    .uri(URI.create("https://" + name + ".scm.azurewebsites.net/api/zipdeploy"))
                    .build();
            HttpResponse response = client.send(request, BodyHandlers.ofString());
            System.out.println("Response status: " + response.statusCode());
            System.out.println("Response body: " + response.body().toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse out the arguments.
     *
     * @param arguments the arguments.
     */
    private void parse(List<String> arguments) {
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i).equals("--name")) {
                name = arguments.get(i + 1);
            }
            if (arguments.get(i).equals("--password")) {
                password = arguments.get(i + 1);
            }
            if (arguments.get(i).equals("--username")) {
                username = arguments.get(i + 1);
            }
        }
    }

    /**
     * Shows the usage.
     */
    private void usage() {
        System.out.println("usage: pi nano cloud deploy <arguments>");
        System.out.println();
        System.out.println("Required arguments");
        System.out.printf(PATTERN, "--name <name>", "The name of the application");
        System.out.printf(PATTERN, "--password <password>", "The password to deploy with");
        System.out.printf(PATTERN, "--username <username>", "The username to deploy with");
    }
}
