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
package cloud.piranha.spring.starter.embedded;

import cloud.piranha.http.api.HttpServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;

/**
 * The Piranha Embedded ServletWebServerFactory.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EmbeddedPiranhaServletWebServerFactory implements ServletWebServerFactory {

    /**
     * Stores the context path.
     */
    private String contextPath = "";
    
    /**
     * Stores the port.
     */
    private int port = 8080;

    /**
     * Stores the HTTP server implementation
     */
    private HttpServer httpServer;

    @Override
    public WebServer getWebServer(ServletContextInitializer... initializers) {
        EmbeddedPiranhaWebServer webServer = new EmbeddedPiranhaWebServer();
        webServer.setContextPath(contextPath);
        webServer.setPort(port);
        webServer.setInitializers(initializers);
        webServer.setHttpServer(httpServer);
        webServer.init();
        return webServer;
    }
    
    /**
     * Set the context path.
     * 
     * @param contextPath the context path.
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * Set the port.
     * 
     * @param port the port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Set the HTTP server implementation
     * @param httpServer the HTTP server implementation
     */
    public void setHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }


}
