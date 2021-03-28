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
package cloud.piranha.http.netty;

import cloud.piranha.http.api.HttpServerRequest;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;

/**
 * The Netty implementation of HTTP Server Request.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NettyHttpServerRequest implements HttpServerRequest {

    /**
     * Stores the context.
     */
    private final ChannelHandlerContext context;

    /**
     * Stores the input stream.
     */
    private InputStream inputStream;
    
    /**
     * Stores the underlying HTTP request.
     */
    private final FullHttpRequest request;

    /**
     * Constructor.
     *
     * @param request the HTTP request.
     * @param context the context.
     */
    public NettyHttpServerRequest(ChannelHandlerContext context, FullHttpRequest request) {
        super();
        this.context = context;
        this.request = request;
    }

    @Override
    public String getHeader(String name) {
        return request.headers().get(name);
    }

    @Override
    public Iterator<String> getHeaderNames() {
        return request.headers().names().iterator();
    }

    @Override
    public Iterator<String> getHeaders(String name) {
        return request.headers().getAll(name).iterator();
    }

    @Override
    public String getHttpVersion() {
        return request.protocolVersion().text();
    }

    @Override
    public String getLocalAddress() {
        InetSocketAddress localAddress = (InetSocketAddress) context.channel().localAddress();
        return localAddress.getAddress().getHostAddress();
    }

    @Override
    public String getLocalHostname() {
        InetSocketAddress localAddress = (InetSocketAddress) context.channel().localAddress();
        return localAddress.getAddress().getHostName();
    }

    @Override
    public int getLocalPort() {
        InetSocketAddress localAddress = (InetSocketAddress) context.channel().localAddress();
        return localAddress.getPort();
    }

    @Override
    public InputStream getMessageBody() {
        synchronized (request) {
            if (inputStream == null) {
                inputStream = new ByteBufInputStream(request.content());
            }
        }
        return inputStream;
    }

    @Override
    public String getMethod() {
        return request.method().name();
    }

    @Override
    public String getRemoteAddress() {
        InetSocketAddress remoteAddress = (InetSocketAddress) context.channel().remoteAddress();
        return remoteAddress.getAddress().getHostAddress();
    }

    @Override
    public String getRemoteHostname() {
        InetSocketAddress remoteAddress = (InetSocketAddress) context.channel().remoteAddress();
        return remoteAddress.getAddress().getHostName();
    }

    @Override
    public int getRemotePort() {
        InetSocketAddress remoteAddress = (InetSocketAddress) context.channel().remoteAddress();
        return remoteAddress.getPort();
    }

    @Override
    public String getRequestTarget() {
        return request.uri();
    }
}
