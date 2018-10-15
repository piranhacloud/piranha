/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha.grizzly;

import com.manorrock.piranha.api.HttpServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

/**
 * The Grizzly-based HttpServer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class GrizzlyHttpServer implements HttpServer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getName());

    /**
     * Stores the Grizzly HttpServer.
     */
    private final  org.glassfish.grizzly.http.server.HttpServer httpServer;

    /**
     * Constructor.
     */
    public GrizzlyHttpServer() {
        httpServer = org.glassfish.grizzly.http.server.HttpServer.createSimpleServer();
        addHttpHandler();
    }

    /**
     * Constructor
     *
     * @param serverPort the server port.
     */
    public GrizzlyHttpServer(int serverPort) {
        httpServer = org.glassfish.grizzly.http.server.HttpServer.createSimpleServer("", serverPort);
        addHttpHandler();
    }

    /**
     * Constructor
     *
     * @param httpServer the Grizzly HTTP server.
     */
    public GrizzlyHttpServer(org.glassfish.grizzly.http.server.HttpServer httpServer) {
        this.httpServer = httpServer;
        addHttpHandler();
    }
    
    /**
     * Add the HTTP handler.
     */
    private void addHttpHandler() {
        httpServer.getServerConfiguration().addHttpHandler(new HttpHandler() {
            @Override
            public void service(Request request, Response response) throws Exception {
                
            }
        });
    }

    /**
     * Is the server running.
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isRunning() {
        return httpServer.isStarted();
    }

    /**
     * Start the server.
     */
    @Override
    public void start() {
        try {
            httpServer.start();
        } catch (IOException ioe) {
            LOGGER.log(Level.WARNING, "An I/O error occurred while starting the HTTP server", ioe);
        }
    }

    /**
     * Stop the server.
     */
    @Override
    public void stop() {
        httpServer.shutdownNow();
    }
}
