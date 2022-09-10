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
import org.apache.maven.plugin.MojoExecutionException;
import static org.apache.maven.plugins.annotations.LifecyclePhase.NONE;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * This goal will deploy the Maven WAR module, start Piranha Core Profile and
 * wait for it. It echoes the Piranha Core Profile console back to you for your
 * convenience.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Mojo(name = "run", defaultPhase = NONE)
public class RunMojo extends BaseMojo {
    
    /**
     * Copy the WAR file to Piranha Micro.
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
            startAndWaitForPiranhaCoreProfile();
        } catch (IOException ioe) {
            throw new MojoExecutionException(ioe);
        }
    }

    /**
     * Start and wait for Piranha Core Profile.
     */
    private void startAndWaitForPiranhaCoreProfile() throws IOException {

        System.out.println("Application is available at: http://localhost:8080/" + warName);

        Process process = new ProcessBuilder()
                .directory(new File(runtimeDirectory))
                .command("java",
                        "-jar",
                        piranhaJarFile.getAbsolutePath(),
                        "--war-file",
                        warName + ".war")
                .inheritIO()
                .start();

        try {
            process.waitFor();
        } catch (InterruptedException ie) {
            ie.printStackTrace(System.err);
            Thread.currentThread().interrupt();
        }
    }
}
