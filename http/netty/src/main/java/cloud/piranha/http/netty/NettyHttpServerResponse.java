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
package cloud.piranha.http.netty;

import cloud.piranha.http.api.HttpServerResponse;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The Netty implementation of HTTP Server Response.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NettyHttpServerResponse implements HttpServerResponse {

    /**
     * Stores the output stream.
     */
    private OutputStream outputStream;

    /**
     * Stores the response.
     */
    private final FullHttpResponse response;

    /**
     * Constructor.
     *
     * @param response the HTTP response.
     */
    public NettyHttpServerResponse(FullHttpResponse response) {
        this.response = response;
    }

    /**
     * @see HttpServerResponse#getHeader(java.lang.String) 
     */
    @Override
    public String getHeader(String name) {
        return response.headers().get(name);
    }

    /**
     * @see HttpServerResponse#getOutputStream() 
     */
    @Override
    public OutputStream getOutputStream() {
        synchronized (response) {
            if (outputStream == null) {
                outputStream = new ByteBufOutputStream(response.content());
            }
        }
        return outputStream;
    }

    /**
     * @see HttpServerResponse#setHeader(java.lang.String, java.lang.String) 
     */
    @Override
    public void setHeader(String name, String value) {
        response.headers().set(name, value);
    }

    /**
     * @see HttpServerResponse#setStatus(int) 
     */
    @Override
    public void setStatus(int status) {
        response.setStatus(HttpResponseStatus.valueOf(status));
    }

    /**
     * @see HttpServerResponse#writeHeaders() 
     */
    @Override
    public void writeHeaders() throws IOException {
    }

    /**
     * @see HttpServerResponse#writeStatusLine() 
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void writeStatusLine() throws IOException {
    }
}
