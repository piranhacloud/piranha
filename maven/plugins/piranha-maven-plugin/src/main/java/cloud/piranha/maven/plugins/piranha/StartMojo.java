/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.maven.plugin.MojoExecutionException;
import static org.apache.maven.plugins.annotations.LifecyclePhase.NONE;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * This goal will deploy the Maven WAR module and start Piranha Core Profile in
 * a separate process.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Mojo(name = "start", defaultPhase = NONE)
public class StartMojo extends BaseMojo {

    /**
     * Copy the WAR file to Piranha Core Profile.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void copyWarFileToPiranhaCoreProfile() throws IOException {
        File warFile = new File(buildDirectory, warName + ".war");
        File outputFile = new File(runtimeDirectory, warName + ".war");
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        Files.copy(warFile.toPath(), outputFile.toPath(), REPLACE_EXISTING);
    }

    @Override
    public void execute() throws MojoExecutionException {
        try {
            determineVersionToUse();
            getPiranhaJarFile();
            copyWarFileToPiranhaCoreProfile();
            startPiranhaCoreProfile();
        } catch (IOException ioe) {
            throw new MojoExecutionException(ioe);
        }
    }

    /**
     * Start and wait for Piranha Core Profile.
     */
    private void startPiranhaCoreProfile() throws IOException {

        ArrayList<String> commands = new ArrayList<>();
        commands.add("java");
        if (jvmArguments != null && !jvmArguments.equals("")) {
            commands.addAll(Arrays.asList(jvmArguments.split(" ")));
        }
        commands.add("-jar");
        commands.add(piranhaJarFile.getAbsolutePath());
        commands.add("--http-port");
        commands.add(httpPort.toString());
        commands.add("--war-file");
        commands.add(warName + ".war");
        if (contextPath != null) {
            commands.add("--context-path");
            if (contextPath.startsWith("/")) {
                contextPath = contextPath.substring(1);
            }
            commands.add(contextPath);
        }
        commands.add("--write-pid");

        new ProcessBuilder()
                .directory(new File(runtimeDirectory))
                .command(commands)
                .start();

        File pidFile = new File(runtimeDirectory + "/tmp/piranha.pid");
        int count = 0;
        System.out.print("Waiting for Piranha to be ready ");
        while (!pidFile.exists()) {
            try {
                Thread.sleep(500);
                count++;
                System.out.print(".");
            } catch (InterruptedException ie) {
            }
            if (count == 80) {
                System.out.println();
                System.out.println("Warning, PID file not seen!");
                break;
            }
        }
        System.out.println();

        System.out.println("Application is available at: http://localhost:" + httpPort + "/"
                + (contextPath != null ? contextPath : warName));
    }
}
