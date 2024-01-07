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
package cloud.piranha.http.virtual;

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.impl.DefaultHttpServerRequest;
import cloud.piranha.http.impl.DefaultHttpServerResponse;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import jdk.incubator.concurrent.StructuredTaskScope;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import static java.lang.System.Logger.Level.WARNING;

/**
 * Implementation of HttpServer that uses virtual threads
 */
public class VirtualHttpServer implements HttpServer {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(VirtualHttpServer.class.getName());
    /**
     * Stores the executor service.
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Stores the running flag
     */
    private boolean isRunning;
    /**
     * Stores the HTTP server processor
     */
    private HttpServerProcessor httpServerProcessor;
    /**
     * Stores the server port
     */
    private int serverPort;

    /**
     * Stores the SSL flag
     */
    private boolean ssl;

    /**
     * Constructor
     */
    public VirtualHttpServer() {
        this(8080);
    }

    /**
     * Constructor
     *
     * @param serverPort the server port
     */
    public VirtualHttpServer(int serverPort) {
        this(serverPort, null, false);
    }

    /**
     * Constructor
     *
     * @param serverPort server port
     * @param httpServerProcessor the HTTP server processor
     * @param ssl true if SSL is enabled
     */
    public VirtualHttpServer(int serverPort, HttpServerProcessor httpServerProcessor, boolean ssl) {
        this.httpServerProcessor = httpServerProcessor;
        this.serverPort = serverPort;
        this.ssl = ssl;
    }


    @Override
    public void start() {
        isRunning = true;
        CountDownLatch barrier = new CountDownLatch(1);
        executorService.execute(() -> {
            try {
                ServerSocket serverSocket = getServerSocket();
                barrier.countDown();
                serve(serverSocket);
            } catch (Exception e) {
                isRunning = false;
                LOGGER.log(WARNING, e);
                throw new RuntimeException(e);
            }
        });
        try {
            barrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a server socket
     * @return the server socket
     * @throws Exception if an error occurs
     */
    private ServerSocket getServerSocket() throws Exception {
        try {
            ServerSocket serverSocket;
            if (ssl) {
                SSLContext context = SSLContext.getDefault();
                SSLEngine engine = context.createSSLEngine();
                SSLServerSocketFactory factory = context.getServerSocketFactory();
                SSLServerSocket socket = (SSLServerSocket) factory.createServerSocket(getServerPort());
                SSLParameters parameters = new SSLParameters();
                parameters.setCipherSuites(engine.getSupportedCipherSuites());
                parameters.setProtocols(engine.getSupportedProtocols());
                parameters.setNeedClientAuth(false);
                parameters.setWantClientAuth(true);
                socket.setSSLParameters(parameters);
                serverSocket = socket;
            } else {
                serverSocket = new ServerSocket(getServerPort());
            }
            serverSocket.setReuseAddress(true);
            return serverSocket;
        } catch (IOException exception) {
            throw new IOException("An I/O error occurred while starting the HTTP server", exception);
        } catch (NoSuchAlgorithmException ex) {
            throw new NoSuchAlgorithmException("Unable to match SSL algorithm", ex);
        }
    }

    @Override
    public void stop() {
        isRunning = false;
        executorService.shutdown();
    }

    /**
     * Handle requests
     * @param serverSocket the server socket
     * @throws IOException if an error occurs
     * @throws InterruptedException if an error occurs
     */
    private void serve(ServerSocket serverSocket) throws IOException, InterruptedException {
        try (StructuredTaskScope<Void> scope = new StructuredTaskScope<>()) {
            try {
                while (isRunning()) {
                    Socket socket = serverSocket.accept();
                    scope.fork(() -> handle(socket));
                }
            } finally {
                scope.join().shutdown();
            }
        }
    }

    /**
     * Handle a request
     * @param socket the socket
     * @return null
     * @throws IOException if an error occurs
     */
    private Void handle(Socket socket) throws IOException {
        DefaultHttpServerRequest defaultHttpServerRequest = new DefaultHttpServerRequest(socket);
        DefaultHttpServerResponse defaultHttpServerResponse = new DefaultHttpServerResponse(socket);
        getHttpServerProcessor().process(defaultHttpServerRequest, defaultHttpServerResponse);
        defaultHttpServerResponse.closeResponse();
        return null;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public HttpServerProcessor getHttpServerProcessor() {
        return httpServerProcessor;
    }

    @Override
    public void setHttpServerProcessor(HttpServerProcessor httpServerProcessor) {
        this.httpServerProcessor = httpServerProcessor;
    }

    @Override
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }

    @Override
    public boolean getSSL() {
        return ssl;
    }

}
