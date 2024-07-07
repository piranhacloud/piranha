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
package cloud.piranha.dist.server;

import cloud.piranha.core.api.PiranhaBuilder;
import java.lang.System.Logger.Level;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import cloud.piranha.core.api.WebApplicationExtension;
import java.io.File;
import static java.lang.System.Logger.Level.WARNING;

/**
 * The builder so you can easily build instances of
 * {@link cloud.piranha.dist.server.ServerPiranha}.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.dist.server.ServerPiranha
 */
public class ServerPiranhaBuilder implements PiranhaBuilder<ServerPiranha> {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(ServerPiranhaBuilder.class.getName());

    /**
     * Stores the Piranha Server instance.
     */
    private final ServerPiranha piranha = new ServerPiranha();

    /**
     * Stores the InitialContext factory.
     */
    private String initialContextFactory = "com.manorrock.herring.thread.ThreadInitialContextFactory";

    /**
     * Stores the verbose flag.
     */
    private boolean verbose = false;

    /**
     * Build the server.
     *
     * @return the server.
     */
    public ServerPiranha build() {
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
    public ServerPiranhaBuilder exitOnStop(boolean exitOnStop) {
        piranha.getConfiguration().setBoolean("exitOnStop", exitOnStop);
        return this;
    }

    /**
     * Set the extension class.
     *
     * @param extensionClass the extension class.
     * @return the builder.
     */
    public ServerPiranhaBuilder extensionClass(Class<? extends WebApplicationExtension> extensionClass) {
        piranha.getConfiguration().setClass("extensionClass", extensionClass);
        return this;
    }

    /**
     * Set the extension class.
     *
     * @param extensionClassName the extension class name.
     * @return the builder.
     */
    public ServerPiranhaBuilder extensionClass(String extensionClassName) {
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
    public ServerPiranhaBuilder httpPort(int httpPort) {
        piranha.getConfiguration().setInteger("httpPort", httpPort);
        return this;
    }

    /**
     * Set the HTTP server class.
     *
     * @param httpServerClass the HTTP server class.
     * @return the builder.
     */
    public ServerPiranhaBuilder httpServerClass(String httpServerClass) {
        piranha.getConfiguration().setString("httpServerClass", httpServerClass);
        return this;
    }

    /**
     * Set the HTTPS keystore file.
     *
     * @param httpsKeystoreFile the HTTPS keystore file.
     * @return the builder.
     */
    public ServerPiranhaBuilder httpsKeystoreFile(String httpsKeystoreFile) {
        piranha.getConfiguration().setString("httpsKeystoreFile", httpsKeystoreFile);
        return this;
    }

    /**
     * Set the HTTPS keystore password.
     *
     * @param httpsKeystorePassword the HTTPS keystore password.
     * @return the builder.
     */
    public ServerPiranhaBuilder httpsKeystorePassword(String httpsKeystorePassword) {
        piranha.getConfiguration().setString("httpsKeystorePassword", httpsKeystorePassword);
        return this;
    }

    /**
     * Set the HTTPS server port.
     *
     * @param httpsPort the HTTPS server port.
     * @return the builder.
     */
    public ServerPiranhaBuilder httpsPort(int httpsPort) {
        piranha.getConfiguration().setInteger("httpsPort", httpsPort);
        return this;
    }

    /**
     * Set the HTTPS server class.
     *
     * @param httpsServerClass the HTTPS server class.
     * @return the builder.
     */
    public ServerPiranhaBuilder httpsServerClass(String httpsServerClass) {
        piranha.getConfiguration().setString("httpsServerClass", httpsServerClass);
        return this;
    }

    /**
     * Set the HTTPS truststore file.
     *
     * @param httpsTruststoreFile the HTTPS truststore file.
     * @return the builder.
     */
    public ServerPiranhaBuilder httpsTruststoreFile(String httpsTruststoreFile) {
        piranha.getConfiguration().setString("httpsTruststoreFile", httpsTruststoreFile);
        return this;
    }

    /**
     * Set the HTTPS truststore password.
     *
     * @param httpsTruststorePassword the HTTPS truststore password.
     * @return the builder.
     */
    public ServerPiranhaBuilder httpsTruststorePassword(String httpsTruststorePassword) {
        piranha.getConfiguration().setString("httpsTruststorePassword", httpsTruststorePassword);
        return this;
    }

    /**
     * Enable/disable JPMS.
     *
     * @param jpms the JPMS flag.
     * @return the builder.
     */
    public ServerPiranhaBuilder jpms(boolean jpms) {
        piranha.getConfiguration().setBoolean("jpmsEnabled", jpms);
        return this;
    }

    /**
     * Set the logging level.
     * 
     * @param loggingLevel the logging level.
     * @return the builder.
     */
    public ServerPiranhaBuilder loggingLevel(String loggingLevel) {
        piranha.getConfiguration().setString("loggingLevel", loggingLevel);
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

            Default extension class   : %s
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
            Logging level             : %s
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
                        piranha.getConfiguration().getString("loggingLevel"),
                        piranha.getConfiguration().getFile("webAppsDir")
                ));
    }

    /**
     * Set the verbose flag.
     *
     * @param verbose the verbose flag.
     * @return the builder.
     */
    public ServerPiranhaBuilder verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    /**
     * Set the web applications directory.
     *
     * @param webAppsDir the web applications directory.
     * @return the builder.
     */
    public ServerPiranhaBuilder webAppsDir(String webAppsDir) {
        if (webAppsDir != null) {
            piranha.getConfiguration().setFile("webAppsDir", new File(webAppsDir));
        }
        return this;
    }
}
