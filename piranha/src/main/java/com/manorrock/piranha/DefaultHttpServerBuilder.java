/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
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

/**
 * The default HTTP server builder.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Deprecated
public class DefaultHttpServerBuilder implements HttpServerBuilder {

    /**
     * Stores the server port.
     */
    private int port;

    /**
     * Stores the processor.
     */
    private HttpServerProcessor processor;

    /**
     * Build the HTTP server.
     *
     * @return the HTTP server.
     */
    @Override
    public HttpServer build() {
        int serverPort = port <= 0 ? 8080 : port;
        HttpServer result;
        if (processor != null) {
            result = new DefaultHttpServer(serverPort, processor);
        } else {
            result = new DefaultHttpServer();
        }
        return result;
    }

    /**
     * Set the server port number.
     *
     * @param port the port number
     * @return the HTTP server builder.
     */
    @Override
    public HttpServerBuilder port(int port) {
        this.port = port;
        return this;
    }

    /**
     * Set the processor.
     *
     * @param processor the processor.
     * @return the HTTP server builder.
     */
    @Override
    public HttpServerBuilder processor(HttpServerProcessor processor) {
        this.processor = processor;
        return this;
    }
}
