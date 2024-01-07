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
package cloud.piranha.feature.http;

import cloud.piranha.feature.impl.DefaultFeature;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.impl.DefaultHttpServer;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import java.lang.reflect.InvocationTargetException;

/**
 * The HTTP feature that exposes an HTTP endpoint.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpFeature extends DefaultFeature {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(HttpFeature.class.getName());

    /**
     * Stores the HTTP server.
     */
    private HttpServer httpServer;
    
    /**
     * Stores the HTTP server class.
     */
    private String httpServerClass = DefaultHttpServer.class.getName();
    
    /**
     * Stores the port.
     */
    private int port = 8080;
    
    @Override
    public void destroy() {
        httpServer = null;
    }

    /**
     * Get the HTTP server.
     * 
     * @return the HTTP server.
     */
    public HttpServer getHttpServer() {
        return httpServer;
    }
    
    /**
     * Get the HTTP server class.
     * 
     * @return the HTTP server class.
     */
    public String getHttpServerClass() {
        return httpServerClass;
    }

    /**
     * Get the port.
     * 
     * @return the port.
     */
    public int getPort() {
        return port;
    }

    @Override
    public void init() {
        if (port > 0) {
            try {
                httpServer = (HttpServer) Class.forName(httpServerClass)
                        .getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | IllegalAccessException
                    | IllegalArgumentException | InstantiationException
                    | NoSuchMethodException | SecurityException
                    | InvocationTargetException t) {
                LOGGER.log(ERROR, "Unable to construct HTTP server", t);
            }
            if (httpServer != null) {
                httpServer.setServerPort(port);
            }
        }
    }

    /**
     * Set the HTTP server.
     * 
     * @param httpServer the HTTP server.
     */
    public void setHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    /**
     * Set the HTTP server class.
     * 
     * @param httpServerClass the HTTP server class.
     */
    public void setHttpServerClass(String httpServerClass) {
        if (httpServerClass != null) {
            this.httpServerClass = httpServerClass;
        } else {
            this.httpServerClass = DefaultHttpServer.class.getName();
        }
    }

    /**
     * Set the port.
     * 
     * @param port the port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        if (httpServer != null) {
            httpServer.start();
        }
    }

    @Override
    public void stop() {
        if (httpServer != null) {
            httpServer.stop();
        }
    }
}
