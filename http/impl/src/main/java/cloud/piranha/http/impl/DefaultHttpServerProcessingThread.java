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

import static java.util.logging.Level.WARNING;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * A processing thread used by the default implementation of HTTP server.
 *
 * <p>
 * This thread is used to process a HTTP Server Request and generate a HTTP
 * Server Response. It does so by giving it to a HTTP Server Processor.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultHttpServerProcessingThread implements Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(
            DefaultHttpServerProcessingThread.class.getPackageName());

    /**
     * Stores the server.
     */
    private final DefaultHttpServer server;

    /**
     * Stores the socket.
     */
    private final Socket socket;

    /**
     * Constructor.
     *
     * @param server the server we are working for.
     * @param socket the socket we are dealing with.
     */
    public DefaultHttpServerProcessingThread(DefaultHttpServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    /**
     * @see Runnable#run()
     */
    @Override
    public void run() {
        try {
            DefaultHttpServerRequest request = new DefaultHttpServerRequest(socket);
            DefaultHttpServerResponse response = new DefaultHttpServerResponse(socket);
            server.processor.process(request, response);
        } finally {
            if (!server.processor.isAsync()) {
                try {
                    socket.close();
                } catch (IOException exception) {
                    if (LOGGER.isLoggable(WARNING)) {
                        LOGGER.log(WARNING, "An I/O error occurred during processing of the request", exception);
                    }
                }
            }
        }
    }
}
