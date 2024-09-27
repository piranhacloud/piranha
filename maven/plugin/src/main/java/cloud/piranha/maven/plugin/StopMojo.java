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
package cloud.piranha.maven.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import static org.apache.maven.plugins.annotations.LifecyclePhase.NONE;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * This goal will stop the Piranha runtime that was started with the
 * <code>start</code> goal.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Mojo(name = "stop", defaultPhase = NONE)
public class StopMojo extends AbstractMojo {

    /**
     * Stores the runtime directory.
     */
    @Parameter(defaultValue = "${project.build.directory}/piranha", required = true)
    private String runtimeDirectory;

    /**
     * Stores the skip property.
     */
    @Parameter(defaultValue = "false", property = "piranha.skip")
    private boolean skip;

    /**
     * Default constructor.
     */
    public StopMojo() {
    }

    @Override
    public void execute() throws MojoExecutionException {
        if (!skip) {
            try {
                /*
                 * Get the PID from the PID file.
                 */
                String pid = Files.readString((new File(
                        runtimeDirectory, "tmp/piranha.pid").toPath()));

                /*
                 * Delete the PID file.
                 */
                if (!Files.deleteIfExists(new File(
                        runtimeDirectory, "tmp/piranha.pid").toPath())) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    if (Files.deleteIfExists(new File(
                            runtimeDirectory, "tmp/piranha.pid").toPath())) {
                        System.err.println("Unable to delete PID file");
                    }
                }

                if (!pid.trim().equals("")) {
                    /*
                     * If the process is still active destroy it forcibly.
                     */
                    ProcessHandle.of(Long.parseLong(pid.trim())).ifPresent(p -> {
                        if (p.isAlive()) {
                            System.err.println("Process still alive, destroying forcibly");
                            p.destroyForcibly();
                        }
                    });
                }
            } catch (IOException ioe) {
                throw new MojoExecutionException(ioe);
            }
        }
    }
}
