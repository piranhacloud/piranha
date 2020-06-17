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

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cloud.piranha.http.api.HttpServerRequest;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

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
     * Stores the query parameters.
     */
    private Map<String, List<String>> queryParameters;

    /**
     * Stores the HTTP request.
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

    /**
     * @see HttpServerRequest#getHeader(java.lang.String)
     */
    @Override
    public String getHeader(String name) {
        return request.headers().get(name);
    }

    /**
     * @see HttpServerRequest#getHeaderNames()
     */
    @Override
    public Iterator<String> getHeaderNames() {
        return request.headers().names().iterator();
    }

    /**
     * @see HttpServerRequest#getInputStream()
     */
    @Override
    public InputStream getInputStream() {
        synchronized (request) {
            if (inputStream == null) {
                inputStream = new ByteBufInputStream(request.content());
            }
        }
        return inputStream;
    }

    /**
     * @see HttpServerRequest#getLocalAddress()
     */
    @Override
    public String getLocalAddress() {
        InetSocketAddress localAddress = (InetSocketAddress) context.channel().localAddress();
        return localAddress.getAddress().getHostAddress();
    }

    /**
     * @see HttpServerRequest#getLocalHostname()
     */
    @Override
    public String getLocalHostname() {
        InetSocketAddress localAddress = (InetSocketAddress) context.channel().localAddress();
        return localAddress.getAddress().getHostName();
    }

    /**
     * @see HttpServerRequest#getLocalPort()
     */
    @Override
    public int getLocalPort() {
        InetSocketAddress localAddress = (InetSocketAddress) context.channel().localAddress();
        return localAddress.getPort();
    }

    /**
     * @see HttpServerRequest#getMethod()
     */
    @Override
    public String getMethod() {
        return request.method().name();
    }

    /**
     * @see HttpServerRequest#getQueryParameter(java.lang.String)
     */
    @Override
    public String getQueryParameter(String name) {
        synchronized (request) {
            if (queryParameters == null) {
                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
                queryParameters = queryStringDecoder.parameters();
            }
        }
        return queryParameters.get(name).get(0);
    }

    /**
     * @see HttpServerRequest#getQueryString()
     */
    @Override
    public String getQueryString() {
        String result = null;
        if (request.uri().contains("?")) {
            result = request.uri().substring(request.uri().indexOf("?") + 1);
        }
        return result;
    }

    /**
     * @see HttpServerRequest#getRemoteAddress()
     */
    @Override
    public String getRemoteAddress() {
        InetSocketAddress remoteAddress = (InetSocketAddress) context.channel().remoteAddress();
        return remoteAddress.getAddress().getHostAddress();
    }

    /**
     * @see HttpServerRequest#getRemoteHostname()
     */
    @Override
    public String getRemoteHostname() {
        InetSocketAddress remoteAddress = (InetSocketAddress) context.channel().remoteAddress();
        return remoteAddress.getAddress().getHostName();
    }

    /**
     * @see HttpServerRequest#getRemotePort()
     */
    @Override
    public int getRemotePort() {
        InetSocketAddress remoteAddress = (InetSocketAddress) context.channel().remoteAddress();
        return remoteAddress.getPort();
    }

    /**
     * @see HttpServerRequest#getRequestTarget()
     */
    @Override
    public String getRequestTarget() {
        return request.uri();
    }
}
