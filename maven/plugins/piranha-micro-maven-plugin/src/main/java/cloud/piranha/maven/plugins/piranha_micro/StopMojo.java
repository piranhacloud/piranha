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
package cloud.piranha.maven.plugins.piranha_micro;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/*
 * -----------------------------------------------------------------------------
 * Documentation for this plugin is at src/main/javadoc/resources/micro-maven-plugin.html
 * at the top-level project. If you introduce anything new / change anything 
 * that needs documentation please make sure this HTML page is updated.
 * -----------------------------------------------------------------------------
 */

/**
 * This goal will stop a Piranha Micro that was started with the <code>start</code> goal.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Mojo(name = "stop", defaultPhase = LifecyclePhase.NONE)
public class StopMojo extends AbstractMojo {

    /**
     * Stores the project build directory.
     */
    @Parameter(defaultValue = "${project.build.directory}", required = true, readonly = true)
    private String buildDir;
    
    @Override
    public void execute() throws MojoExecutionException {
        try {
            if (!Files.deleteIfExists(new File(
                    buildDir, "piranha-micro/tmp/piranha-micro.pid").toPath())) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                if (Files.deleteIfExists(new File(
                    buildDir, "piranha-micro/tmp/piranha-micro.pid").toPath())) {
                    System.err.println("Unable to delete PID file");
                }
            }
        } catch (IOException ioe) {
            throw new MojoExecutionException(ioe);
        }
    }
}
