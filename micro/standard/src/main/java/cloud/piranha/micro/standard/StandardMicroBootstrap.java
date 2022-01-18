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
package cloud.piranha.micro.standard;

import cloud.piranha.extension.standard.StandardExtension;
import cloud.piranha.micro.core.MicroPiranhaBuilder;

/**
 * The bootstrapper for the Standard version of Piranha Micro.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class StandardMicroBootstrap {

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        MicroPiranhaBuilder builder = new StandardMicroBootstrap().processArguments(arguments);
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
    private MicroPiranhaBuilder processArguments(String[] arguments) {
        
        MicroPiranhaBuilder builder = new MicroPiranhaBuilder()
                .defaultExtensionClass(StandardExtension.class)
                .exitOnStop(true);
        
        if (arguments != null) {
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i].equals("--help")) {
                    return null;
                }
                if (arguments[i].equals("--http-port")) {
                    builder = builder.httpsPort(Integer.parseInt(arguments[i + 1]));
                }
                if (arguments[i].equals("--https-port")) {
                    builder = builder.httpsPort(Integer.parseInt(arguments[i + 1]));
                }
                if (arguments[i].equals("--jpms")) {
                    builder = builder.jpms(true);
                }
                if (arguments[i].equals("--ssl-keystore-file")) {
                    builder = builder.sslKeystoreFile(arguments[i + 1]);
                }
                if (arguments[i].equals("--ssl-keystore-password")) {
                    builder = builder.sslKeystorePassword(arguments[i + 1]);
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
        }
        return builder;
    }

    /**
     * Show help.
     */
    private static void showHelp() {
        System.out.println();
        System.out.println(
                """
                  --help                           - Show this help
                  --http-port <integer>            - Set the HTTP port (use -1 to disable)
                  --https-port <integer>           - Set the HTTPS port (disabled by default)
                  --jpms                           - Enable Java Platform Module System
                  --ssl-keystore-file <file>       - Set the SSL keystore file (applies to the
                                                     whole JVM)
                  --ssl-keystore-password <string> - Set the SSL keystore password (applies to
                                                     the whole JVM
                  --verbose                        - Shows the runtime parameters
                  --war-file <file>                - The WAR file to deploy
                  --webapp-dir <directory>         - The directory to use for the web
                                                     application (auto creates when it does not
                                                     exist, if omitted runtime will use the 
                                                     filename portion of --war-file)
                  --write-pid                      - Write out a PID file
                """);
    }
}
