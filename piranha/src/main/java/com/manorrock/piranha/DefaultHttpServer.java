/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha;

import com.manorrock.piranha.api.HttpServerProcessor;
import com.manorrock.piranha.api.HttpServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The default HttpServer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServer implements HttpServer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getName());

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
     * Stores the thread factory.
     */
    protected ThreadFactory threadFactory;

    /**
     * Constructor
     */
    public DefaultHttpServer() {
        threadFactory = new DefaultHttpServerThreadFactory();
        processor = new DefaultHttpServerProcessor();
        serverPort = 8080;
        serverStopRequest = false;
    }

    /**
     * Constructor
     *
     * @param serverPort the server port.
     * @param processor the HTTP server processor.
     */
    public DefaultHttpServer(int serverPort, HttpServerProcessor processor) {
        this.threadFactory = new DefaultHttpServerThreadFactory();
        this.processor = processor;
        this.serverPort = serverPort;
        this.serverStopRequest = false;
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
     * Is the server running.
     *
     * @return true if it is, false otherwise.
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
     * Start the server.
     */
    @Override
    public void start() {
        try {
            executorService = Executors.newCachedThreadPool(threadFactory);
            serverStopRequest = false;
            serverSocket = new ServerSocket(serverPort);
            serverSocket.setReuseAddress(false);
            serverSocket.setSoTimeout(soTimeout);
            serverAcceptorThread = new Thread(new DefaultHttpServerAcceptorThread(this), "DefaultHttpServer-AcceptorThread");
            serverAcceptorThread.start();
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "An I/O error occurred while starting the HTTP server", exception);
        }
    }

    /**
     * Stop the server.
     */
    @Override
    public void stop() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException exception) {
                LOGGER.log(Level.WARNING, "An I/O error occurred while stopping the HTTP server", exception);
            }
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(120, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            LOGGER.log(Level.WARNING, "Termination of the executor service was interrupted", exception);
        }
    }
}
