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
package cloud.piranha.dist.webprofile;

import cloud.piranha.extension.webprofile.WebProfileExtension;
import cloud.piranha.single.SinglePiranhaBuilder;
import static java.lang.System.Logger.Level.WARNING;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

/**
 * The Main for Piranha Web Profile.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebProfilePiranhaMain {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(WebProfilePiranhaMain.class.getName());

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        SinglePiranhaBuilder builder = new WebProfilePiranhaMain().processArguments(arguments);
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
     */
    private SinglePiranhaBuilder processArguments(String[] arguments) {
        
        SinglePiranhaBuilder builder = new SinglePiranhaBuilder()
                .extensionClass(WebProfileExtension.class)
                .exitOnStop(true);
        int httpPort = 0;
        int httpsPort = 0;
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i].equals("--extension-class")) {
                    builder = builder.extensionClass(arguments[i + 1]);
                }
                if (arguments[i].equals("--context-path")) {
                    builder = builder.contextPath(arguments[i + 1]);
                }
                if (arguments[i].equals("--help")) {
                    return null;
                }
                if (arguments[i].equals("--http-port")) {
                    int arg = Integer.parseInt(arguments[i + 1]);
                    builder = builder.httpPort(arg);
                    httpPort = arg;
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
                    int arg = Integer.parseInt(arguments[i + 1]);
                    builder = builder.httpsPort(arg);
                    httpsPort = arg;
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
                if (arguments[i].equals("--war-file")) {
                    builder = builder.warFile(arguments[i + 1]);
                }
                if (arguments[i].equals("--webapp-dir")) {
                    builder = builder.webAppDir(arguments[i + 1]);
                }
                if (arguments[i].equals("--write-pid")) {
                    builder = builder.pid(ProcessHandle.current().pid());
                }
            }
            checkPorts(httpPort, httpsPort);
        }
        return builder;
    }

    private void checkPorts(int httpPort, int httpsPort) {
        if(httpsPort != 0 && httpPort == httpsPort) {
            LOGGER.log(WARNING, "The http and the https ports are the same. Please use different ports");
            System.exit(-1);
        }
    }

    /**
     * Show help.
     */
    private static void showHelp() {
        LOGGER.log(Level.INFO, "");
        LOGGER.log(Level.INFO,
                """
  --extension-class <className>        - Set the extension to use
  --help                               - Show this help
  --context-path <string>              - Set the Servlet context path
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
  --war-file <file>                    - The WAR file to deploy
  --webapp-dir <directory>             - The directory to use for the web
                                         application (auto creates when it does
                                         not exist, if omitted runtime will use
                                         the filename portion of --war-file)
  --write-pid                          - Write out a PID file
                """);
    }
}
