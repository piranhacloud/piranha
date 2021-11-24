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
package cloud.piranha.micro.core;

import cloud.piranha.core.api.WebApplicationExtension;

/**
 * The builder to create a version of Piranha Micro.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class MicroPiranhaBuilder {

    /**
     * Stores the default extension class.
     */
    private Class<? extends WebApplicationExtension> defaultExtensionClass;

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
     * Stores the PID.
     */
    private Long pid;

    /**
     * Build the Piranha instance.
     *
     * @return the Piranha instance.
     */
    public MicroPiranha build() {
        if (verbose) {
            showArguments();
        }
        MicroPiranha piranha = new MicroPiranha();
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
        if (warFile != null) {
            piranha.setWarFile(warFile);
        }
        if (webAppDir != null) {
            piranha.setWebAppDir(webAppDir);
        }
        piranha.setPid(pid);
        return piranha;
    }

    /**
     * Set the default extension class.
     *
     * @param defaultExtensionClass the default extension class.
     * @return the builder.
     */
    public MicroPiranhaBuilder defaultExtensionClass(
            Class<? extends WebApplicationExtension> defaultExtensionClass) {
        this.defaultExtensionClass = defaultExtensionClass;
        return this;
    }

    /**
     * Set the exit on stop flag.
     *
     * @param exitOnStop the exit on stop flag.
     * @return the builder.
     */
    public MicroPiranhaBuilder exitOnStop(boolean exitOnStop) {
        this.exitOnStop = exitOnStop;
        return this;
    }

    /**
     * Set the HTTP server port.
     *
     * @param httpPort the HTTP server port.
     * @return the builder.
     */
    public MicroPiranhaBuilder httpPort(int httpPort) {
        this.httpPort = httpPort;
        return this;
    }

    /**
     * Set the HTTPS server port.
     *
     * @param httpsPort the HTTPS server port.
     * @return the builder.
     */
    public MicroPiranhaBuilder httpsPort(int httpsPort) {
        this.httpsPort = httpsPort;
        return this;
    }

    /**
     * Enable/disable JPMS.
     *
     * @param jpms the JPMS flag.
     * @return the builder.
     */
    public MicroPiranhaBuilder jpms(boolean jpms) {
        this.jpms = jpms;
        return this;
    }

    /**
     * Show the arguments used.
     */
    private void showArguments() {
        System.out.printf(
                """
                
                PIRANHA MICRO
                
                Arguments
                =========
                
                Default Extension     : %s
                Exit on stop          : %s
                HTTP port             : %s
                HTTPS port            : %s
                JPMS enabled          : %s
                PID                   : %s
                SSL keystore file     : %s
                SSK keystore password : ****
                WAR filename          : %s
                Webapp dir            : %s
                
                """,
                defaultExtensionClass.getName(),
                exitOnStop,
                httpPort,
                httpsPort,
                jpms,
                pid,
                sslKeystoreFile,
                warFile,
                webAppDir);
    }

    /**
     * Set the SSL keystore file.
     *
     * @param sslKeystoreFile the SSL keystore file.
     * @return the builder.
     */
    public MicroPiranhaBuilder sslKeystoreFile(String sslKeystoreFile) {
        this.sslKeystoreFile = sslKeystoreFile;
        return this;
    }

    /**
     * Set the SSL keystore password.
     *
     * @param sslKeystorePassword the SSL keystore password.
     * @return the builder.
     */
    public MicroPiranhaBuilder sslKeystorePassword(String sslKeystorePassword) {
        this.sslKeystorePassword = sslKeystorePassword;
        return this;
    }

    /**
     * Set the verbose flag.
     *
     * @param verbose the verbose flag.
     * @return the builder.
     */
    public MicroPiranhaBuilder verbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    /**
     * Set the WAR file.
     *
     * @param warFile the WAR file.
     * @return the builder.
     */
    public MicroPiranhaBuilder warFile(String warFile) {
        this.warFile = warFile;
        return this;
    }

    /**
     * Set the web application directory.
     *
     * @param webAppDir the web application directory.
     * @return the builder.
     */
    public MicroPiranhaBuilder webAppDir(String webAppDir) {
        this.webAppDir = webAppDir;
        return this;
    }

    /**
     * Set the PID.
     *
     * @param pid the PID.
     * @return the builder.
     */
    public MicroPiranhaBuilder pid(Long pid) {
        this.pid = pid;
        return this;
    }
}
