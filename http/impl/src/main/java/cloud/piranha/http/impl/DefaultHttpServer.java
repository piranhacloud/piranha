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
package cloud.piranha.http.impl;

import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.api.HttpServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * The default implementation of HTTP Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServer implements HttpServer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(
            DefaultHttpServer.class.getPackageName());

    /**
     * Stores the executor service.
     */
    protected ExecutorService executorService;

    /**
     * Stores the processor.
     */
    protected HttpServerProcessor processor;

    /**
     * Stores the port we are listening on.
     */
    protected int serverPort;

    /**
     * Stores the server acceptor thread.
     */
    protected Thread serverAcceptorThread;

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
     * Stores the SSL flag.
     */
    protected boolean ssl;

    /**
     * Stores the thread factory.
     */
    protected ThreadFactory threadFactory;

    /**
     * Constructor
     */
    public DefaultHttpServer() {
        threadFactory = new DefaultHttpServerThreadFactory();
        processor = new DefaultHttpServerProcessor();
        serverPort = 8765;
        serverStopRequest = false;
    }

    /**
     * Constructor.
     *
     * @param serverPort the server port.
     */
    public DefaultHttpServer(int serverPort) {
        this.serverPort = serverPort;
        threadFactory = new DefaultHttpServerThreadFactory();
        processor = new DefaultHttpServerProcessor();
        serverStopRequest = false;
    }

    /**
     * Constructor
     *
     * @param serverPort the server port.
     * @param processor the HTTP server processor.
     * @param ssl the SSL flag.
     */
    public DefaultHttpServer(int serverPort, HttpServerProcessor processor, boolean ssl) {
        this.threadFactory = new DefaultHttpServerThreadFactory();
        this.processor = processor;
        this.serverPort = serverPort;
        this.serverStopRequest = false;
        this.ssl = ssl;
    }


    /**
     * Constructor
     *
     * @param serverPort the server port.
     * @param processor the HTTP server processor.
     * @param soTimeout the SO_TIMEOUT.
     */
    public DefaultHttpServer(int serverPort, HttpServerProcessor processor, int soTimeout) {
        this.threadFactory = new DefaultHttpServerThreadFactory();
        this.processor = processor;
        this.serverPort = serverPort;
        this.serverStopRequest = false;
        this.soTimeout = soTimeout;
    }

    /**
     * Get the SO_TIMEOUT.
     *
     * @return the SO_TIMEOUT.
     */
    public int getSoTimeout() {
        return soTimeout;
    }

    /**
     * @see HttpServer#isRunning()
     */
    @Override
    public boolean isRunning() {
        boolean result = false;
        if (executorService != null) {
            result = !executorService.isShutdown();
        }
        return result;
    }

    /**
     * @see HttpServer#start()
     */
    @Override
    public void start() {
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "Starting HTTP server on port {0}", serverPort);
        }
        try {
            executorService = Executors.newCachedThreadPool(threadFactory);
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
            serverAcceptorThread = new Thread(new DefaultHttpServerAcceptorThread(this),
                    "DefaultHttpServer-AcceptorThread");
            serverAcceptorThread.start();
            if (LOGGER.isLoggable(FINE)) {
                LOGGER.log(FINE, "Started HTTP server on port {0}", serverPort);
            }
        } catch (IOException exception) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "An I/O error occurred while starting the HTTP server", exception);
            }
        } catch (NoSuchAlgorithmException ex) {
            if (LOGGER.isLoggable(SEVERE)) {
                LOGGER.log(WARNING, "Unable to match SSL algorithm", ex);
            }
        }
    }

    /**
     * @see HttpServer#stop()
     */
    @Override
    public void stop() {
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "Stopping HTTP server on port {0}", serverPort);
        }
        serverStopRequest = true;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException exception) {
                if (LOGGER.isLoggable(WARNING)) {
                    LOGGER.log(WARNING, "An I/O error occurred while stopping the HTTP server", exception);
                }
            }
        }
        if (executorService != null) {
            executorService.shutdown();
            try {
                executorService.awaitTermination(120, TimeUnit.SECONDS);
            } catch (InterruptedException exception) {
                if (LOGGER.isLoggable(WARNING)) {
                    LOGGER.log(WARNING, "Termination of the executor service was interrupted", exception);
                }
                Thread.currentThread().interrupt();
            }
        }
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "Stopped HTTP server on port {0}", serverPort);
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
