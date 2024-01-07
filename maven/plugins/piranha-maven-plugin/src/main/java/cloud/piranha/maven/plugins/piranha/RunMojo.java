/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.maven.plugins.piranha;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.maven.plugin.MojoExecutionException;
import static org.apache.maven.plugins.annotations.LifecyclePhase.NONE;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * This goal will deploy your web application, start Piranha and wait for it. It
 * echoes the Piranha console back to you for your convenience.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Mojo(name = "run", defaultPhase = NONE)
public class RunMojo extends BaseMojo {

    @Override
    public void execute() throws MojoExecutionException {
        if (!skip) {
            try {
                determineVersionToUse();
                downloadDistribution();
                extractDistribution();
                copyWarFile();
                startPiranhaAndWait();
            } catch (IOException ioe) {
                throw new MojoExecutionException(ioe);
            }
        }
    }

    /**
     * Start Piranha using a JAR distribution.
     */
    private void startJarPiranha() throws IOException {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("java");
        if (jvmArguments != null && !jvmArguments.equals("")) {
            commands.addAll(Arrays.asList(jvmArguments.split(" ")));
        }
        commands.add("-jar");
        commands.add(piranhaFile.getAbsolutePath());
        if (contextPath != null) {
            commands.add("--context-path");
            if (contextPath.startsWith("/")) {
                contextPath = contextPath.substring(1);
            }
            commands.add(contextPath);
        }
        if (httpPort > 0) {
            commands.add("--http-port");
            commands.add(httpPort.toString());
        }
        if (httpsPort > 0) {
            commands.add("--https-port");
            commands.add(httpsPort.toString());
        }
        commands.add("--war-file");
        commands.add(warName + ".war");
        commands.add("--write-pid");
        Process process = new ProcessBuilder()
                .directory(new File(runtimeDirectory))
                .command(commands)
                .inheritIO()
                .start();
        System.out.println("Application is available at: http://localhost:"
                + httpPort + "/" + (contextPath != null ? contextPath : warName));
        try {
            process.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace(System.err);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Start Piranha using a ZIP distribution.
     */
    private void startZipPiranha() throws IOException {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("/bin/bash");
        commands.add("-c");
        StringBuilder arguments = new StringBuilder();
        arguments.append("./run.sh");
        arguments.append(" --http-port ").append(httpPort.toString());
        if (contextPath != null) {
            arguments.append(" --context-path ");
            if (contextPath.startsWith("/")) {
                contextPath = contextPath.substring(1);
            }
            arguments.append(contextPath);
        }
        arguments.append(" --verbose --write-pid");
        commands.add(arguments.toString());
        Process process = new ProcessBuilder()
                .directory(new File(runtimeDirectory + File.separator + "bin"))
                .command(commands)
                .inheritIO()
                .start();
        System.out.println("Application is available at: http://localhost:"
                + httpPort + "/" + (contextPath != null ? contextPath : warName));
        try {
            process.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace(System.err);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Start Piranha.
     */
    private void startPiranhaAndWait() throws IOException {
        switch (piranhaType) {
            case "jar" ->
                startJarPiranha();
            case "zip" ->
                startZipPiranha();
        }
    }
}
