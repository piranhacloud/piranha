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
package cloud.piranha.server.core;

import cloud.piranha.core.api.WebApplicationExtension;

/**
 * The bootstrapper for Piranha Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class ServerBootstrap {
    
    /**
     * Get the default extension.
     * 
     * @return the default extension.
     */
    protected abstract Class<? extends WebApplicationExtension> getDefaultExtension();

    /**
     * Show help.
     */
    protected static void showHelp() {
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
                                                     the whole JVM)
                  --webapps-dir <directory>        - Set the web applications directory
                """);
    }  

    /**
     * Process the arguments.
     *
     * @param arguments the arguments.
     * @return the builder.
     */
    protected ServerPiranhaBuilder processArguments(String[] arguments) {
        ServerPiranhaBuilder builder = new ServerPiranhaBuilder().defaultExtensionClass(getDefaultExtension()).exitOnStop(true);
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
                if (arguments[i].equals("--webapps-dir")) {
                    builder = builder.webAppsDir(arguments[i + 1]);
                }
            }
        }
        return builder;
    }
}
