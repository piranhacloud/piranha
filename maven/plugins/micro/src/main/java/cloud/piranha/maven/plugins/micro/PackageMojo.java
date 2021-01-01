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
package cloud.piranha.maven.plugins.micro;

import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * The Mojo that will package your application with Piranha Micro.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Mojo(name = "package", defaultPhase = LifecyclePhase.PACKAGE)
public class PackageMojo extends MicroMojo {

    /**
     * Stores the web application extensions.
     *
     * <p>
     * Each extension will be added as a dependency to be resolved by Shrinkwrap
     * and during initialization of MicroPiranha they will be used in order to
     * bootstrap MicroPiranha with the given extensions.
     * </p>
     *
     * </p>
     * Note each extensions should be in groupId:artifactId:version format, if
     * version is omitted the same version as this plugin is assumed.
     * </p>
     */
    @Parameter(required = false)
    private String[] extensions;

    /**
     * Stores the WAR dependency.
     *
     * <p>
     * If you already have an existing WAR file on a Maven repository you can
     * use this parameter to refer to it instead of copying it.
     * </p>
     *
     * <p>
     * Note this should be in groupId:artifactId:version format, if version is
     * omitted the same version as this plugin is assumed.
     * </p>
     */
    @Parameter(required = false)
    private String warDependency;

    /**
     * Stores the WAR file.
     *
     * <p>
     * If you have an existing WAR file you can use this to refer to it.
     * </p>
     *
     * <p>
     * Note if the WAR file is not specified, but copied to
     * <pre>src/main/piranha-micro/ROOT.war</pre> it will automatically pick it
     * up without the need to configure this parameter in the POM file.
     * </p>
     */
    @Parameter(defaultValue = "src/main/piranha-micro/ROOT.war", required = false)
    private File warFile;

    /**
     * Execute the mojo.
     *
     * @throws MojoExecutionException when an execution error occurs.
     * @throws MojoFailureException when a failure occurs.
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        showBanner();
    }
}
