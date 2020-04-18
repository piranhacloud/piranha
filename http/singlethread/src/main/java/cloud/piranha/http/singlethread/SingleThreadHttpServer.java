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

import cloud.piranha.DefaultHttpServerProcessor;
import cloud.piranha.DefaultHttpServerRequest;
import cloud.piranha.DefaultHttpServerResponse;
import cloud.piranha.api.HttpServer;
import cloud.piranha.api.HttpServerProcessor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A single thread HttpServer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SingleThreadHttpServer implements HttpServer, Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getName());

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
     * Constructor.
     */
    public SingleThreadHttpServer() {
        processor = new DefaultHttpServerProcessor();
        serverPort = 8080;
        serverStopRequest = false;
    }

    /**
     * Constructor.
     * 
     * @param serverPort the server port.
     */
    public SingleThreadHttpServer(int serverPort) {
        this.serverPort = serverPort;
        processor = new DefaultHttpServerProcessor();
        serverStopRequest = false;
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

    /**
     * Is the server running.
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isRunning() {
        return running;
    }

    /**
     * Handle the socket request.
     */
    @Override
    public void run() {
        while (!serverStopRequest) {
            try {
                try (Socket socket = serverSocket.accept()) {
                    DefaultHttpServerRequest request = new DefaultHttpServerRequest(socket);
                    DefaultHttpServerResponse response = new DefaultHttpServerResponse(socket);
                    processor.process(request, response);
                }
            } catch (IOException ioe) {
            } catch (Throwable throwable) {
            }
        }
    }

    /**
     * Start the server.
     */
    @Override
    public void start() {
        try {
            serverStopRequest = false;
            serverSocket = new ServerSocket(serverPort);
            serverSocket.setReuseAddress(false);
            serverSocket.setSoTimeout(soTimeout);
            serverProcessingThread = new Thread(this, "SingleThreadHttpServer");
            serverProcessingThread.start();
            running = true;
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
            running = false;
        }
        serverStopRequest = true;
        serverProcessingThread.interrupt();
    }
}
