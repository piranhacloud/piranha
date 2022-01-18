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
package cloud.piranha.server.core;

import java.io.File;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;

/**
 * The builder so you can easily build instances of
 * {@link cloud.piranha.server.core.ServerPiranha}.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.server.core.ServerPiranha
 */
public class ServerPiranhaBuilder {

    /**
     * Stores the default extension.
     */
    private Class defaultExtensionClass;

    /**
     * Stores the exit on stop flag.
     */
    private boolean exitOnStop = false;

    /**
     * Stores the HTTP port.
     */
    private int httpPort = 8080;

    /**
     * Stores the HTTPS port.
     */
    private int httpsPort = -1;
    
    /**
     * Stores the InitialContext factory.
     */
    private String initialContextFactory = "com.manorrock.herring.thread.ThreadInitialContextFactory";

    /**
     * Stores the JPMS flag.
     */
    private boolean jpms = false;

    /**
     * Stores the SSL keystore file.
     */
    private String sslKeystoreFile;

    /**
     * Stores the SSL keystore password.
     */
    private String sslKeystorePassword;

    /**
     * Stores the SSL truststore file.
     */
    private String sslTruststoreFile;

    /**
     * Stores the SSL truststore password.
     */
    private String sslTruststorePassword;

    /**
     * Stores the verbose flag.
     */
    private boolean verbose = false;

    /**
     * Stores the web applications directory.
     */
    private String webAppsDir = "webapps";

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
        ServerPiranha piranha = new ServerPiranha();
        piranha.setDefaultExtensionClass(defaultExtensionClass);
        piranha.setExitOnStop(exitOnStop);
        piranha.setHttpPort(httpPort);
        piranha.setHttpsPort(httpsPort);
        piranha.setJpmsEnabled(jpms);
        if (sslKeystoreFile != null) {
            piranha.setSslKeystoreFile(sslKeystoreFile);
        }
        if (sslKeystorePassword != null) {
            piranha.setSslKeystorePassword(sslKeystorePassword);
        }
        if (sslTruststoreFile != null) {
            piranha.setSslKeystoreFile(sslTruststoreFile);
        }
        if (sslTruststorePassword != null) {
            piranha.setSslKeystorePassword(sslTruststorePassword);
        }
        piranha.setWebAppsDir(new File(webAppsDir));
        return piranha;
    }

    /**
     * Set the default extension class.
     *
     * @param defaultExtensionClass the default extension class.
     * @return the builder.
     */
    public ServerPiranhaBuilder defaultExtensionClass(Class defaultExtensionClass) {
        this.defaultExtensionClass = defaultExtensionClass;
        return this;
    }

    /**
     * Set the exit on stop flag.
     *
     * @param exitOnStop the exit on stop flag.
     * @return the builder.
     */
    public ServerPiranhaBuilder exitOnStop(boolean exitOnStop) {
        this.exitOnStop = exitOnStop;
        return this;
    }

    /**
     * Set the HTTP server port.
     *
     * @param httpPort the HTTP server port.
     * @return the builder.
     */
    public ServerPiranhaBuilder httpPort(int httpPort) {
        this.httpPort = httpPort;
        return this;
    }

    /**
     * Set the HTTPS server port.
     *
     * @param httpsPort the HTTPS server port.
     * @return the builder.
     */
    public ServerPiranhaBuilder httpsPort(int httpsPort) {
        this.httpsPort = httpsPort;
        return this;
    }

    /**
     * Enable/disable JPMS.
     *
     * @param jpms the JPMS flag.
     * @return the builder.
     */
    public ServerPiranhaBuilder jpms(boolean jpms) {
        this.jpms = jpms;
        return this;
    }

    /**
     * Show the arguments used.
     */
    private void showArguments() {
        System.out.printf(
                """
                
                PIRANHA SERVER
                
                Arguments
                =========
                
                Default extension       : %s
                Exit on stop            : %s
                HTTP port               : %s
                HTTPS port              : %s
                JPMS enabled            : %s
                SSL keystore file       : %s
                SSL keystore password   : ****
                SSL truststore file     : %s
                SSL truststore password : ****
                Web applications dir    : %s
                
                """,
                defaultExtensionClass.getName(),
                exitOnStop,
                httpPort,
                httpsPort,
                jpms,
                sslKeystoreFile,
                sslTruststoreFile,
                webAppsDir
        );
    }

    /**
     * Set the SSL keystore file.
     *
     * @param sslKeystoreFile the SSL keystore file.
     * @return the builder.
     */
    public ServerPiranhaBuilder sslKeystoreFile(String sslKeystoreFile) {
        this.sslKeystoreFile = sslKeystoreFile;
        return this;
    }

    /**
     * Set the SSL keystore password.
     *
     * @param sslKeystorePassword the SSL keystore password.
     * @return the builder.
     */
    public ServerPiranhaBuilder sslKeystorePassword(String sslKeystorePassword) {
        this.sslKeystorePassword = sslKeystorePassword;
        return this;
    }

    /**
     * Set the SSL truststore file.
     *
     * @param sslTruststoreFile the SSL truststore file.
     * @return the builder.
     */
    public ServerPiranhaBuilder sslTruststoreFile(String sslTruststoreFile) {
        this.sslTruststoreFile = sslTruststoreFile;
        return this;
    }

    /**
     * Set the SSL truststore password.
     *
     * @param sslTruststorePassword the SSL truststore password.
     * @return the builder.
     */
    public ServerPiranhaBuilder sslTruststorePassword(String sslTruststorePassword) {
        this.sslTruststorePassword = sslTruststorePassword;
        return this;
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
        this.webAppsDir = webAppsDir;
        return this;
    }
}
