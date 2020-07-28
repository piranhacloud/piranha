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
package cloud.piranha.maven.plugins.micro;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * The abstract Mojo used as our own base class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class MicroMojo extends AbstractMojo {

    /*
     * Jotting down some thoughts, remove when done with the implementation.
     *
     * 1. Create the list of dependencies (Piranha Micro and extensions).
     * 2. Resolve dependencies and get the file URLs.
     * 3. Create the class loader for Piranha Micro.
     * 4. Create the instance of Piranha Micro
     * 5. Determine war file URL.
     *    a. Using warFile parameter
     *    b. Using warDependency parameter
     *    c. From the project dependency itself
     */
    /**
     * Stores the banner flag (defaults to true).
     */
    @Parameter(defaultValue = "true", required = false)
    protected boolean banner;

    /**
     * Stores the port (defaults to 8080).
     *
     * <p>
     * If you override the port, the resulting JAR file will use that as its
     * default, unless you override it using the --port command line parameter.
     * </p>
     */
    @Parameter(defaultValue = "8080", required = false)
    private Integer port;
    
    /**
     * Stores the web application directory.
     * 
     * <p>
     * The temporary directory used by your Piranha Micro application to extract
     * the web application into.
     * </p>
     */
    @Parameter(defaultValue = "webapp", required = false)
    private String webappDirectory;

    /**
     * Shows the banner.
     */
    protected void showBanner() {
        if (banner) {
            getLog().info("--- Piranha Micro ---");
        }
    }
}
