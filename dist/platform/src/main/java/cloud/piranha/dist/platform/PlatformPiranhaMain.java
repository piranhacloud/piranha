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
package cloud.piranha.dist.platform;

import java.lang.System.Logger.Level;

import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.extension.platform.PlatformExtension;

/**
 * The Main for Piranha Platform.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class PlatformPiranhaMain {

    /**
     * Stores the logger
     */
    private static final System.Logger LOGGER = System.getLogger(PlatformPiranhaMain.class.getName());

    /**
     * Get the default extension.
     *
     * @return the default extension.
     */
    protected Class<? extends WebApplicationExtension> getDefaultExtension() {
        return PlatformExtension.class;
    }

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        PlatformPiranhaBuilder builder = new PlatformPiranhaMain().processArguments(arguments);
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
    protected PlatformPiranhaBuilder processArguments(String[] arguments) {
        PlatformPiranhaBuilder builder = new PlatformPiranhaBuilder()
                .extensionClass(getDefaultExtension())
                .exitOnStop(true);

        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i].equals("--extension-class")) {
                    builder = builder.extensionClass(arguments[i + 1]);
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
                if (arguments[i].equals("--logging-level")) {
                    builder = builder.loggingLevel(arguments[i + 1]);
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
  --extension-class <className>        - Set the extension to use
  --help                               - Show this help
  --http-port <integer>                - Set the HTTP port (use -1 to disable)
  --http-server-class <className>      - Set the HTTP server class to use
  --https-keystore-file <file>         - Set the HTTPS keystore file (applies to
                                         the whole JVM)
  --https-keystore-password <string>   - Set the HTTPS keystore password 
                                         (applies to the whole JVM)
  --https-port <integer>               - Set the HTTPS port (disabled by 
                                         default)
  --https-server-class <className>     - Set the HTTPS server class to use
  --https-truststore-file <file>       - Set the HTTPS keystore file (applies to
                                         the whole JVM)
  --https-truststore-password <string> - Set the HTTPS keystore password 
                                         (applies to the whole JVM)
  --jpms                               - Enable Java Platform Module System
  --logging-level <string>             - Set the logging level
  --verbose                            - Shows the runtime parameters
  --webapps-dir <directory>            - Set the web applications directory
                """);
    }
}
