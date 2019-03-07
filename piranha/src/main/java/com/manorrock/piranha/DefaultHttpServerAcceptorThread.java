/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha;

import java.io.IOException;
import java.net.Socket;

/**
 * The default HttpServerAcceptorThread.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultHttpServerAcceptorThread implements Runnable {

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
     * Handle the socket request.
     */
    @Override
    public void run() {
        while (!server.serverStopRequest) {
            try {
                Socket socket = server.serverSocket.accept();
                server.executorService.execute(new DefaultHttpServerProcessingThread(server, socket));
            } catch (IOException exception) {
            } catch (Throwable throwable) {
            }
        }
    }
}
