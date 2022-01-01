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
package cloud.piranha.http.jdk;

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.api.HttpServerProcessor;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The JDK implementation of HTTP Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class JdkHttpServer implements HttpServer {

    /**
     * Stores the server port number.
     */
    private int serverPort;

    /**
     * Stores the HTTP server processor.
     */
    private HttpServerProcessor httpServerProcessor;

    /**
     * Stores the SSL enabled flag.
     */
    private boolean ssl;

    /**
     * Stores the underlying server.
     */
    private com.sun.net.httpserver.HttpServer server;

    /**
     * Constructor
     */
    public JdkHttpServer() {
        this(8080, null, false);
    }

    /**
     * Constructor.
     *
     * @param serverPort the port number.
     * @param processor the HTTP server processor.
     * @param ssl the SSL enabled flag.
     */
    public JdkHttpServer(int serverPort, HttpServerProcessor processor, boolean ssl) {
        this.serverPort = serverPort;
        this.httpServerProcessor = processor;
        this.ssl = ssl;
    }

    @Override
    public boolean isRunning() {
        return server != null;
    }

    @Override
    public void start() {
        try {
            if (!ssl) {
                server = com.sun.net.httpserver.HttpServer.create();
            } else {
                server = com.sun.net.httpserver.HttpsServer.create();
            }
            server.bind(new InetSocketAddress(serverPort), 0);
            server.createContext("/", new JdkHttpHandler(httpServerProcessor));
            server.start();
        } catch (IOException ioe) {
            server = null;
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
        }
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public boolean getSSL() {
        return ssl;
    }

    @Override
    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }

    @Override
    public HttpServerProcessor getHttpServerProcessor() {
        return httpServerProcessor;
    }

    @Override
    public void setHttpServerProcessor(HttpServerProcessor httpServerProcessor) {
        this.httpServerProcessor = httpServerProcessor;
    }
}
