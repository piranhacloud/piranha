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
package cloud.piranha.dist.server;

import java.lang.System.Logger.Level;

import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.extension.servlet.ServletExtension;
import static java.lang.System.Logger.Level.WARNING;

/**
 * The Main for Piranha Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServerPiranhaMain {

    /**
     * Stores the logger
     */
    private static final System.Logger LOGGER = System.getLogger(ServerPiranhaMain.class.getName());

    /**
     * Get the default extension.
     *
     * @return the default extension.
     */
    protected Class<? extends WebApplicationExtension> getDefaultExtension() {
        return ServletExtension.class;
    }

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        ServerPiranhaBuilder builder = new ServerPiranhaMain().processArguments(arguments);
        if (builder != null) {
            builder.build().start();
        } else {
            showHelp();
        }
    }

    /**
     * Process the arguments.
     *
     * @param arguments the arguments.
     * @return the builder.
     */
    protected ServerPiranhaBuilder processArguments(String[] arguments) {
        ServerPiranhaBuilder builder = new ServerPiranhaBuilder()
                .defaultExtensionClass(getDefaultExtension())
                .exitOnStop(true);

        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i].equals("--default-extension")) {
                    builder = builder.defaultExtensionClass(arguments[i + 1]);
                }
                if (arguments[i].equals("--help")) {
                    return null;
                }
                if (arguments[i].equals("--http-port")) {
                    builder = builder.httpPort(Integer.parseInt(arguments[i + 1]));
                }
                if (arguments[i].equals("--http-server-class")) {
                    builder = builder.httpServerClass(arguments[i + 1]);
                }
                if (arguments[i].equals("--https-keystore-file")) {
                    builder = builder.httpsKeystoreFile(arguments[i + 1]);
                }
                if (arguments[i].equals("--https-keystore-password")) {
                    builder = builder.httpsKeystorePassword(arguments[i + 1]);
                }
                if (arguments[i].equals("--https-port")) {
                    builder = builder.httpsPort(Integer.parseInt(arguments[i + 1]));
                }
                if (arguments[i].equals("--https-server-class")) {
                    builder = builder.httpsServerClass(arguments[i + 1]);
                }
                if (arguments[i].equals("--https-truststore-file")) {
                    builder = builder.httpsTruststoreFile(arguments[i + 1]);
                }
                if (arguments[i].equals("--https-truststore-password")) {
                    builder = builder.httpsTruststorePassword(arguments[i + 1]);
                }
                if (arguments[i].equals("--jpms")) {
                    builder = builder.jpms(true);
                }
                if (arguments[i].equals("--ssl-truststore-file")) {
                    LOGGER.log(WARNING, "The --ssl-truststore-file has been replaced by --https-truststore-file [DEPRECATED]");
                    builder = builder.sslTruststoreFile(arguments[i + 1]);
                }
                if (arguments[i].equals("--ssl-truststore-password")) {
                    LOGGER.log(WARNING, "The --ssl-truststore-password has been replaced by --https-truststore-password [DEPRECATED]");
                    builder = builder.sslTruststorePassword(arguments[i + 1]);
                }
                if (arguments[i].equals("--verbose")) {
                    builder = builder.verbose(true);
                }
                if (arguments[i].equals("--webapps-dir")) {
                    builder = builder.webAppsDir(arguments[i + 1]);
                }
            }
        }
        return builder;
    }

    /**
     * Show help.
     */
    protected static void showHelp() {
        LOGGER.log(Level.INFO, "");
        LOGGER.log(Level.INFO,
                """
 --default-extension <className>      - Set the default extension
 --help                               - Show this help
 --http-port <integer>                - Set the HTTP port (use -1 to disable)
 --http-server-class                  - Set the HTTP server class
 --https-keystore-file <file>         - Set the HTTPS keystore file 
 --https-keystore-password <string>   - Set the HTTPS keystore password
 --https-port <integer>               - Set the HTTPS port (disabled by default)
 --https-server-class                 - Set the HTTPS server class
 --https-truststore-file <file>       - Set the SSL keystore file
 --https-truststore-password <string> - Set the SSL keystore password
 --jpms                               - Enable Java Platform Module System
 --webapps-dir <directory>            - Set the web applications directory
                """);
    }
}
