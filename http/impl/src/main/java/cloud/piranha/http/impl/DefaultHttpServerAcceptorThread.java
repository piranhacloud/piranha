/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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

import static java.lang.System.Logger.Level.WARNING;

import java.io.IOException;
import java.net.Socket;
import java.lang.System.Logger;

/**
 * The acceptor thread used by the default implementation of HTTP server.
 *
 * <p>
 * This thread is waiting for socket connections, and once it gets a socket
 * connection it hands it off for processing to a processing thread.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultHttpServerAcceptorThread implements Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(
            DefaultHttpServerAcceptorThread.class.getName());

    /**
     * Stores the HTTP server.
     */
    private final DefaultHttpServer server;

    /**
     * Constructor.
     *
     * @param server the server we are working for.
     */
    public DefaultHttpServerAcceptorThread(DefaultHttpServer server) {
        this.server = server;
    }

    /**
     * @see Runnable#run()
     */
    @Override
    public void run() {
        while (!server.serverStopRequest) {
            try {
                Socket socket = server.serverSocket.accept();
                server.executorService.execute(new DefaultHttpServerProcessingThread(server, socket));
            } catch (IOException exception) {
                // not interesting to do anything with this here as the client probably just hung up.
            } catch (Throwable throwable) {
                LOGGER.log(WARNING, "An error occurred while accepting a socket connection", throwable);
            }
        }
    }
}
