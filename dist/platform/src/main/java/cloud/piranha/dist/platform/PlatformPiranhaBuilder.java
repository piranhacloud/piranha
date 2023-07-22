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
package cloud.piranha.dist.platform;

import java.io.File;
import java.lang.System.Logger.Level;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import cloud.piranha.core.api.WebApplicationExtension;
import static java.lang.System.Logger.Level.WARNING;

/**
 * The builder so you can easily build instances of
 * {@link cloud.piranha.dist.platform.PlatformPiranha}.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.dist.platform.PlatformPiranha
 */
public class PlatformPiranhaBuilder {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(PlatformPiranhaBuilder.class.getName());

    /**
     * Stores the Platform Piranha instance.
     */
    private final PlatformPiranha piranha = new PlatformPiranha();

    /**
     * Stores the InitialContext factory.
     */
    private String initialContextFactory = "cloud.piranha.naming.thread.ThreadInitialContextFactory";

    /**
     * Stores the verbose flag.
     */
    private boolean verbose = false;

    /**
     * Build the server.
     *
     * @return the server.
     */
    public PlatformPiranha build() {
        if (verbose) {
            showArguments();
        }
        System.setProperty(INITIAL_CONTEXT_FACTORY, initialContextFactory);
        return piranha;
    }

    /**
     * Set the exit on stop flag.
     *
     * @param exitOnStop the exit on stop flag.
     * @return the builder.
     */
    public PlatformPiranhaBuilder exitOnStop(boolean exitOnStop) {
        piranha.getConfiguration().setBoolean("exitOnStop", exitOnStop);
        return this;
    }

    /**
     * Set the extension class.
     *
     * @param extensionClass the extension class.
     * @return the builder.
     */
    public PlatformPiranhaBuilder extensionClass(Class<? extends WebApplicationExtension> extensionClass) {
        piranha.getConfiguration().setClass("extensionClass", extensionClass);
        return this;
    }

    /**
     * Set the extension class.
     *
     * @param extensionClassName the extension class name.
     * @return the builder.
     */
    public PlatformPiranhaBuilder extensionClass(String extensionClassName) {
        try {
            extensionClass(Class.forName(extensionClassName)
                    .asSubclass(WebApplicationExtension.class));
        } catch (ClassNotFoundException cnfe) {
            LOGGER.log(WARNING, "Unable to load default extension class", cnfe);
        }
        return this;
    }

    /**
     * Set the HTTP server port.
     *
     * @param httpPort the HTTP server port.
     * @return the builder.
     */
    public PlatformPiranhaBuilder httpPort(int httpPort) {
        piranha.getConfiguration().setInteger("httpPort", httpPort);
        return this;
    }

    /**
     * Set the HTTP server class.
     *
     * @param httpServerClass the HTTP server class.
     * @return the builder.
     */
    public PlatformPiranhaBuilder httpServerClass(String httpServerClass) {
        piranha.getConfiguration().setString("httpServerClass", httpServerClass);
        return this;
    }

    /**
     * Set the HTTPS keystore file.
     *
     * @param httpsKeystoreFile the HTTPS keystore file.
     * @return the builder.
     */
    public PlatformPiranhaBuilder httpsKeystoreFile(String httpsKeystoreFile) {
        piranha.getConfiguration().setString("httpsKeystoreFile", httpsKeystoreFile);
        return this;
    }

    /**
     * Set the HTTPS keystore password.
     *
     * @param httpsKeystorePassword the HTTPS keystore password.
     * @return the builder.
     */
    public PlatformPiranhaBuilder httpsKeystorePassword(String httpsKeystorePassword) {
        piranha.getConfiguration().setString("httpsKeystorePassword", httpsKeystorePassword);
        return this;
    }

    /**
     * Set the HTTPS server port.
     *
     * @param httpsPort the HTTPS server port.
     * @return the builder.
     */
    public PlatformPiranhaBuilder httpsPort(int httpsPort) {
        piranha.getConfiguration().setInteger("httpsPort", httpsPort);
        return this;
    }

    /**
     * Set the HTTPS server class.
     *
     * @param httpsServerClass the HTTPS server class.
     * @return the builder.
     */
    public PlatformPiranhaBuilder httpsServerClass(String httpsServerClass) {
        piranha.getConfiguration().setString("httpsServerClass", httpsServerClass);
        return this;
    }

    /**
     * Set the HTTPS truststore file.
     *
     * @param httpsTruststoreFile the HTTPS truststore file.
     * @return the builder.
     */
    public PlatformPiranhaBuilder httpsTruststoreFile(String httpsTruststoreFile) {
        piranha.getConfiguration().setString("httpsTruststoreFile", httpsTruststoreFile);
        return this;
    }

    /**
     * Set the HTTPS truststore password.
     *
     * @param httpsTruststorePassword the HTTPS truststore password.
     * @return the builder.
     */
    public PlatformPiranhaBuilder httpsTruststorePassword(String httpsTruststorePassword) {
        piranha.getConfiguration().setString("httpsTruststorePassword", httpsTruststorePassword);
        return this;
    }

    /**
     * Enable/disable JPMS.
     *
     * @param jpms the JPMS flag.
     * @return the builder.
     */
    public PlatformPiranhaBuilder jpms(boolean jpms) {
        piranha.getConfiguration().setBoolean("jpmsEnabled", jpms);
        return this;
    }

    /**
     * Show the arguments used.
     */
    private void showArguments() {
        LOGGER.log(Level.INFO, """
                
                PIRANHA
                
                Arguments
                =========
                
                Extension class           : %s
                Exit on stop              : %s
                HTTP port                 : %s
                HTTP server class         : %s
                HTTPS keystore file       : %s
                HTTPS keystore password   : ****
                HTTPS port                : %s
                HTTPS server class        : %s
                HTTPS truststore file     : %s
                HTTPS truststore password : ****
                JPMS enabled              : %s
                Web applications dir      : %s
                
                """.formatted(
                piranha.getConfiguration().getClass("extensionClass"),
                piranha.getConfiguration().getBoolean("exitOnStop", false),
                piranha.getConfiguration().getInteger("httpPort"),
                piranha.getConfiguration().getString("httpServerClass"),
                piranha.getConfiguration().getString("httpsKeystoreFile"),
                piranha.getConfiguration().getInteger("httpsPort"),
                piranha.getConfiguration().getString("httpsServerClass"),
                piranha.getConfiguration().getString("httpsTruststoreFile"),
                piranha.getConfiguration().getBoolean("jpmsEnabled", false),
                piranha.getConfiguration().getFile("webAppsDir")
        ));
    }

    /**
     * Set the verbose flag.
     *
     * @param verbose the verbose flag.
     * @return the builder.
     */
    public PlatformPiranhaBuilder verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    /**
     * Set the web applications directory.
     *
     * @param webAppsDir the web applications directory.
     * @return the builder.
     */
    public PlatformPiranhaBuilder webAppsDir(String webAppsDir) {
        if (webAppsDir != null) {
            piranha.getConfiguration().setFile("webAppsDir", new File(webAppsDir));
        }
        return this;
    }
}
