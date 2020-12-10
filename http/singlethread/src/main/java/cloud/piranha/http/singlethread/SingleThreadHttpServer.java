/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.http.singlethread;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.impl.DefaultHttpServerRequest;
import cloud.piranha.http.impl.DefaultHttpServerResponse;

/**
 * The single threaded implementation of HTTP Server.
 *
 * <p>
 * This class has been deprecated in favor of the JDK HttpServer implementation
 * and it will be removed the next cycle.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @deprecated
 * @see cloud.piranha.http.api.HttpServer
 */
@Deprecated
public class SingleThreadHttpServer implements HttpServer, Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getPackageName());

    /**
     * Stores the processor.
     */
    protected HttpServerProcessor processor;

    /**
     * Stores the running flag.
     */
    protected boolean running;

    /**
     * Stores the server acceptor thread.
     */
    protected Thread serverProcessingThread;

    /**
     * Stores the port we are listening on.
     */
    protected int serverPort;

    /**
     * Stores the server socket.
     */
    protected ServerSocket serverSocket;

    /**
     * Stores the server stop request.
     */
    protected boolean serverStopRequest;

    /**
     * Stores the SO_TIMEOUT.
     */
    protected int soTimeout;

    /**
     * Stores the SSL flag
     */
    private boolean ssl;

    /**
     * Constructor.
     */
    public SingleThreadHttpServer() {
        serverPort = 8080;
        serverStopRequest = false;
        soTimeout = 2000;
    }

    /**
     * Constructor.
     *
     * @param serverPort the server port.
     */
    public SingleThreadHttpServer(int serverPort) {
        this.serverPort = serverPort;
        serverStopRequest = false;
        soTimeout = 2000;

    }

    /**
     * Constructor
     *
     * @param serverPort the server port.
     * @param processor the HTTP server processor.
     */
    public SingleThreadHttpServer(int serverPort, HttpServerProcessor processor) {
        this.processor = processor;
        this.serverPort = serverPort;
        this.serverStopRequest = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        while (!serverStopRequest) {
            try {
                try (Socket socket = serverSocket.accept()) {
                    DefaultHttpServerRequest request = new DefaultHttpServerRequest(socket);
                    DefaultHttpServerResponse response = new DefaultHttpServerResponse(socket);
                    processor.process(request, response);
                }
            } catch (SocketException se) {
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "An I/O error occurred during processing", ioe);
            } catch (Throwable throwable) {
                LOGGER.log(SEVERE, "A severe error occurred during processing", throwable);
            }
        }
    }

    @Override
    public void start() {
        LOGGER.log(FINE, "Start HTTP server");
        try {
            serverStopRequest = false;
            if (ssl) {
                SSLContext context = SSLContext.getDefault();
                SSLEngine engine = context.createSSLEngine();
                SSLServerSocketFactory factory = context.getServerSocketFactory();
                SSLServerSocket socket = (SSLServerSocket) factory.createServerSocket(serverPort);
                SSLParameters parameters = new SSLParameters();
                parameters.setCipherSuites(engine.getSupportedCipherSuites());
                parameters.setProtocols(engine.getSupportedProtocols());
                parameters.setNeedClientAuth(false);
                parameters.setWantClientAuth(true);
                socket.setSSLParameters(parameters);
                serverSocket = socket;
            } else {
                serverSocket = new ServerSocket(serverPort);
            }
            serverSocket.setReuseAddress(true);
            serverSocket.setSoTimeout(soTimeout);
            serverProcessingThread = new Thread(this, "SingleThreadHttpServer");
            serverProcessingThread.start();
            running = true;
        } catch (IOException exception) {
            LOGGER.log(WARNING, "An I/O error occurred while starting the HTTP server", exception);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(WARNING, "Unable to match SSL algorithm", ex);
        }
    }

    @Override
    public void stop() {
        LOGGER.log(FINE, "Stopping HTTP server");
        serverStopRequest = true;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException exception) {
                LOGGER.log(WARNING, "An I/O error occurred while stopping the HTTP server", exception);
            }
        }
        running = false;
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
    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }

    @Override
    public boolean getSSL() {
        return ssl;
    }

    @Override
    public void setHttpServerProcessor(HttpServerProcessor httpServerProcessor) {
        processor = httpServerProcessor;
    }

    @Override
    public HttpServerProcessor getHttpServerProcessor() {
        return processor;
    }
}
