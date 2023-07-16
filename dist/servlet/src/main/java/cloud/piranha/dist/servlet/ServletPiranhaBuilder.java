/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.dist.servlet;

import cloud.piranha.core.api.WebApplicationExtension;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import static java.lang.System.Logger.Level.WARNING;

/**
 * The Builder for Piranha Servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletPiranhaBuilder {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ServletPiranhaBuilder.class.getName());

    /**
     * Stores the Piranha Servlet instance.
     */
    private final ServletPiranha piranha = new ServletPiranha();

    /**
     * Stores the verbose flag.
     */
    private boolean verbose = false;

    /**
     * Stores the WAR file(name).
     */
    private String warFile;

    /**
     * Stores the web application directory.
     */
    private String webAppDir;

    /**
     * Build the Piranha instance.
     *
     * @return the Piranha instance.
     */
    public ServletPiranha build() {
        if (verbose) {
            showArguments();
        }
        if (warFile != null) {
            piranha.setWarFile(warFile);
        }
        if (webAppDir != null) {
            piranha.setWebAppDir(webAppDir);
        }
        return piranha;
    }

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     * @return the builder.
     */
    public ServletPiranhaBuilder contextPath(String contextPath) {
        piranha.getConfiguration().setString("contextPath", contextPath);
        return this;
    }

    /**
     * Set the CRaC enabled flag.
     *
     * @param crac the CRaC enabled flag.
     * @return the builder.
     */
    public ServletPiranhaBuilder crac(boolean crac) {
        piranha.getConfiguration().setBoolean("cracEnabled", crac);
        return this;
    }

    /**
     * Set the exit on stop flag.
     *
     * @param exitOnStop the exit on stop flag.
     * @return the builder.
     */
    public ServletPiranhaBuilder exitOnStop(boolean exitOnStop) {
        piranha.getConfiguration().setBoolean("exitOnStop", exitOnStop);
        return this;
    }

    /**
     * Set the extension class.
     *
     * @param extensionClass the extension class.
     * @return the builder.
     */
    public ServletPiranhaBuilder extensionClass(
            Class<? extends WebApplicationExtension> extensionClass) {
        piranha.getConfiguration().setClass("extensionClass", extensionClass);
        return this;
    }

    /**
     * Set the extension class.
     *
     * @param extensionClassName the default extension class name.
     * @return the builder.
     */
    public ServletPiranhaBuilder extensionClass(String extensionClassName) {
        try {
            extensionClass(Class.forName(extensionClassName)
                    .asSubclass(WebApplicationExtension.class));
        } catch (ClassNotFoundException cnfe) {
            LOGGER.log(WARNING, "Unable to load extension class", cnfe);
        }
        return this;
    }

    /**
     * Set the HTTP server port.
     *
     * @param httpPort the HTTP server port.
     * @return the builder.
     */
    public ServletPiranhaBuilder httpPort(int httpPort) {
        piranha.getConfiguration().setInteger("httpPort", httpPort);
        return this;
    }

    /**
     * Set the HTTP server class.
     *
     * @param httpServerClass the HTTP server class.
     * @return the builder.
     */
    public ServletPiranhaBuilder httpServerClass(String httpServerClass) {
        piranha.getConfiguration().setString("httpServerClass", httpServerClass);
        return this;
    }

    /**
     * Set the HTTPS keystore file.
     *
     * @param httpsKeystoreFile the HTTPS keystore file.
     * @return the builder.
     */
    public ServletPiranhaBuilder httpsKeystoreFile(String httpsKeystoreFile) {
        piranha.getConfiguration().setString("httpsKeystoreFile", httpsKeystoreFile);
        return this;
    }

    /**
     * Set the HTTPS keystore password.
     *
     * @param httpsKeystorePassword the HTTPS keystore password.
     * @return the builder.
     */
    public ServletPiranhaBuilder httpsKeystorePassword(String httpsKeystorePassword) {
        piranha.getConfiguration().setString("httpsKeystorePassword", httpsKeystorePassword);
        return this;
    }

    /**
     * Set the HTTPS server port.
     *
     * @param httpsPort the HTTPS server port.
     * @return the builder.
     */
    public ServletPiranhaBuilder httpsPort(int httpsPort) {
        piranha.getConfiguration().setInteger("httpsPort", httpsPort);
        return this;
    }

    /**
     * Set the HTTPS server class.
     *
     * @param httpsServerClass the HTTPS server class.
     * @return the builder.
     */
    public ServletPiranhaBuilder httpsServerClass(String httpsServerClass) {
        piranha.getConfiguration().setString("httpsServerClass", httpsServerClass);
        return this;
    }

    /**
     * Enable/disable JPMS.
     *
     * @param jpms the JPMS flag.
     * @return the builder.
     */
    public ServletPiranhaBuilder jpms(boolean jpms) {
        piranha.getConfiguration().setBoolean("jpmsEnabled", jpms);
        return this;
    }

    /**
     * Set the PID.
     *
     * @param pid the PID.
     * @return the builder.
     */
    public ServletPiranhaBuilder pid(Long pid) {
        piranha.getConfiguration().setLong("pid", pid);
        return this;
    }

    /**
     * Show the arguments used.
     */
    private void showArguments() {
        LOGGER.log(Level.INFO,
                """
                
                PIRANHA
                
                Arguments
                =========
                
                Context path            : %s
                Extension class         : %s
                Exit on stop            : %s
                HTTP port               : %s
                HTTP server class       : %s
                HTTPS keystore file     : %s
                HTTPS keystore password : ****
                HTTPS port              : %s
                HTTPS server class      : %s
                JPMS enabled            : %s
                PID                     : %s
                WAR filename            : %s
                Web application dir     : %s                
                """.formatted(
                        piranha.getConfiguration().getString("contextPath"),
                        piranha.getConfiguration().getClass("extensionClass"),
                        piranha.getConfiguration().getBoolean("exitOnStop", false),
                        piranha.getConfiguration().getInteger("httpPort"),
                        piranha.getConfiguration().getString("httpServerClass"),
                        piranha.getConfiguration().getString("httpsKeystoreFile"),
                        piranha.getConfiguration().getInteger("httpsPort"),
                        piranha.getConfiguration().getString("httpsServerClass"),
                        piranha.getConfiguration().getBoolean("jpmsEnabled", false),
                        piranha.getConfiguration().getLong("pid"),
                        warFile,
                        webAppDir));
    }

    /**
     * Set the verbose flag.
     *
     * @param verbose the verbose flag.
     * @return the builder.
     */
    public ServletPiranhaBuilder verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    /**
     * Set the WAR file.
     *
     * @param warFile the WAR file.
     * @return the builder.
     */
    public ServletPiranhaBuilder warFile(String warFile) {
        this.warFile = warFile;
        return this;
    }

    /**
     * Set the web application directory.
     *
     * @param webAppDir the web application directory.
     * @return the builder.
     */
    public ServletPiranhaBuilder webAppDir(String webAppDir) {
        this.webAppDir = webAppDir;
        return this;
    }
}
