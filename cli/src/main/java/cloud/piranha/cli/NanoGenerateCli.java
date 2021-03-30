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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Piranha Nano Generate CLI.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@CommandLine.Command(name = "generate", description = "Generate a Piranha Nano application")
public class NanoGenerateCli implements Runnable {
    /**
     * Stores the output directory.
     */
    @CommandLine.Option(names = "--outputDirectory", description = "The output directory")
    private String outputDirectory;

    /**
     * Construct the filename.
     *
     * @param entryName the entry name.
     * @return the filename.
     */
    public String constructFilename(String entryName) {
        if (entryName.startsWith("piranha-nano-servlet-helloworld-main/")) {
            entryName = entryName.substring("piranha-nano-servlet-helloworld-main/".length());
        }
        return entryName;
    }

    /**
     * Generate the project.
     */
    @Override
    public void run() {
        try {
            HttpClient client = HttpClient
                    .newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();
            
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create("https://github.com/piranhacloud/piranha-nano-servlet-helloworld/archive/main.zip"))
                    .build();

            HttpResponse<InputStream> response = client.send(request, BodyHandlers.ofInputStream());
            InputStream inputStream = response.body();

            try (ZipInputStream zipInput = new ZipInputStream(inputStream)) {
                ZipEntry zipEntry;
                while ((zipEntry = zipInput.getNextEntry()) != null) {
                    if (zipEntry.isDirectory()) {
                        File directory = new File(constructFilename(zipEntry.getName()));
                        directory.mkdirs();
                    } else {
                        try (FileOutputStream fileOutput = new FileOutputStream(
                                constructFilename(zipEntry.getName()))) {
                            byte[] buffer = new byte[8192];
                            int length;
                            while ((length = zipInput.read(buffer)) != -1) {
                                fileOutput.write(buffer, 0, length);
                            }
                        } catch (IOException ioe) {
                            System.out.println("An error occurred writing out: " + zipEntry.getName());
                        }
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
