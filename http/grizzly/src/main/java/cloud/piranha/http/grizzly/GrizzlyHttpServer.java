/*
 * Copyright (c) 2002-2025 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.http.grizzly;

import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.api.HttpServerProcessorEndState;
import static cloud.piranha.http.api.HttpServerProcessorEndState.ASYNCED;
import java.io.IOException;
import java.util.concurrent.Semaphore;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.lang.System.Logger.Level.TRACE;
import static java.lang.System.Logger.Level.WARNING;
import java.lang.System.Logger;
import org.glassfish.grizzly.CompletionHandler;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http2.Http2AddOn;

/**
 * The Grizzly implementation of HTTP Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.http.api.HttpServer
 */
public class GrizzlyHttpServer implements cloud.piranha.http.api.HttpServer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(GrizzlyHttpServer.class.getName());

    /**
     * Stores the Grizzly HttpServer.
     */
    private HttpServer httpServer;

    /**
     * Stores the HTTP server processor.
     */
    private HttpServerProcessor httpServerProcessor;

    /**
     * *
     * Stores the SSL flag
     */
    private boolean ssl;

    /**
     * *
     * Stores the server port
     */
    private int port;

    /**
     * Constructor.
     */
    public GrizzlyHttpServer() {
    }

    /**
     * Constructor
     *
     * @param serverPort the server port.
     */
    public GrizzlyHttpServer(int serverPort) {
        port = serverPort;
    }

    /**
     * Constructor
     *
     * @param serverPort the server port.
     * @param httpServerProcessor the HTTP server processor;
     */
    public GrizzlyHttpServer(int serverPort, HttpServerProcessor httpServerProcessor) {
        port = serverPort;
        this.httpServerProcessor = httpServerProcessor;
    }

    /**
     * Constructor
     *
     * @param httpServer the Grizzly HTTP server.
     */
    public GrizzlyHttpServer(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    @Override
    public HttpServerProcessor getHttpServerProcessor() {
        LOGGER.log(TRACE, "getHttpServerProcessor");
        return httpServerProcessor;
    }

    @Override
    public int getServerPort() {
        LOGGER.log(TRACE, "getServerPort -> {0}", port);
        return port;
    }

    @Override
    public boolean getSSL() {
        LOGGER.log(TRACE, "getSSL -> {0}", ssl);
        return ssl;
    }

    @Override
    public boolean isRunning() {
        boolean running = httpServer != null;
        LOGGER.log(TRACE, "isRunning -> {0}", running);
        return running;
    }

    @Override
    public void setHttpServerProcessor(HttpServerProcessor httpServerProcessor) {
        LOGGER.log(TRACE, "setHttpServerProcessor");
        this.httpServerProcessor = httpServerProcessor;
    }

    @Override
    public void setServerPort(int serverPort) {
        LOGGER.log(TRACE, "setServerPort({0})", serverPort);
        this.port = serverPort;
    }

    @Override
    public void setSSL(boolean ssl) {
        LOGGER.log(TRACE, "setSSL({0})", ssl);
        this.ssl = ssl;
    }

    @Override
    public void start() {
        if (httpServer == null) {
            httpServer = HttpServer.createSimpleServer(null, port);
            NetworkListener networkListener = httpServer.getListener("grizzly");
            networkListener.setSecure(ssl);
            networkListener.registerAddOn(new Http2AddOn());
        }
        httpServer.getServerConfiguration().setPassTraceRequest(true);
        httpServer.getServerConfiguration().addHttpHandler(new HttpHandler() {
            @Override
            public void service(Request request, Response response) throws Exception {
                LOGGER.log(TRACE, "service enter uri={0} thread={1} grizzly.isSuspended={2} grizzly.isCommitted={3}",
                        request.getRequestURI(), Thread.currentThread().getName(),
                        response.isSuspended(), response.isCommitted());
                GrizzlyHttpServerRequest gRequest = new GrizzlyHttpServerRequest(request);
                GrizzlyHttpServerResponse gResponse = new GrizzlyHttpServerResponse(response);
                HttpServerProcessorEndState state = httpServerProcessor.process(gRequest, gResponse);
                LOGGER.log(TRACE, "service processor returned state={0} grizzly.isSuspended={1} grizzly.isCommitted={2}",
                        state, response.isSuspended(), response.isCommitted());
                if (state == ASYNCED && !response.isCommitted()) {
                    LOGGER.log(TRACE, "service state=ASYNCED calling response.suspend(60s) grizzly.isSuspended={0}",
                            response.isSuspended());
                    response.suspend(60, SECONDS, new CompletionHandler<Response>() {
                        @Override
                        public void cancelled() {
                            LOGGER.log(TRACE, "grizzly CompletionHandler.cancelled thread={0}",
                                    Thread.currentThread().getName());
                        }

                        @Override
                        public void failed(Throwable thrwbl) {
                            LOGGER.log(TRACE, "grizzly CompletionHandler.failed thread={0}",
                                    Thread.currentThread().getName());
                        }

                        @Override
                        public void completed(Response e) {
                            LOGGER.log(TRACE, "grizzly CompletionHandler.completed thread={0}",
                                    Thread.currentThread().getName());
                        }

                        @Override
                        public void updated(Response e) {
                            LOGGER.log(TRACE, "grizzly CompletionHandler.updated thread={0}",
                                    Thread.currentThread().getName());
                        }
                    });
                    LOGGER.log(TRACE, "service response.suspend() returned grizzly.isSuspended={0}",
                            response.isSuspended());
                }
                LOGGER.log(TRACE, "service exit thread={0}", Thread.currentThread().getName());
            }
        });
        try {
            httpServer.start();
        } catch (IOException ioe) {
            LOGGER.log(WARNING, "An I/O error occurred while starting the HTTP server", ioe);
        }
    }

    @Override
    public void stop() {
        LOGGER.log(TRACE, "stop");
        Semaphore lock = new Semaphore(1);
        lock.acquireUninterruptibly();
        httpServer.shutdown(5, SECONDS).addCompletionHandler(
                new CompletionHandler<HttpServer>() {
            @Override
            public void cancelled() {
                lock.release();
            }

            @Override
            public void failed(Throwable thrwbl) {
                lock.release();
            }

            @Override
            public void completed(HttpServer e) {
                lock.release();
            }

            @Override
            public void updated(HttpServer e) {
                lock.release();
            }
        });
        try {
            lock.acquire();
        } catch (InterruptedException ie) {
            LOGGER.log(WARNING, "Interrupted while waiting for the HTTP server to shut down", ie);
        }
        httpServer = null;
    }
}
