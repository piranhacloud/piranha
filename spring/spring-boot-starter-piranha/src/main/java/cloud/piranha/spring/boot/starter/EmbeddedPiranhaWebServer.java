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
package cloud.piranha.spring.boot.starter;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.impl.DefaultHttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.boot.web.servlet.ServletContextInitializer;

/**
 * The Piranha Embedded WebServer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EmbeddedPiranhaWebServer implements WebServer {

    /**
     * Stores the HttpServer instance.
     */
    private final HttpServer httpServer;

    /**
     * Constructor.
     *
     * @param initializers the initializers.
     */
    public EmbeddedPiranhaWebServer(ServletContextInitializer[] initializers) {
        EmbeddedPiranha piranha = new EmbeddedPiranha();
        if (initializers != null) {
            for (int i = 0; i < initializers.length; i++) {
                try {
                    initializers[i].onStartup((ServletContext) piranha.getWebApplication());
                } catch(ServletException se) {
                    throw new RuntimeException(se);
                }
            }
        }
        piranha.initialize();
        piranha.start();
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        server.start();
        server.addWebApplication(piranha.getWebApplication());
        httpServer = new DefaultHttpServer(8080);
        httpServer.setHttpServerProcessor(server);
    }

    @Override
    public void start() throws WebServerException {
        httpServer.start();
    }

    @Override
    public void stop() throws WebServerException {
        httpServer.stop();
    }

    @Override
    public int getPort() {
        return 8080;
    }
}
