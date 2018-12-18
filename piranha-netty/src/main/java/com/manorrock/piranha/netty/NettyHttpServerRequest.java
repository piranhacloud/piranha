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
package com.manorrock.piranha.netty;

import com.manorrock.piranha.api.HttpServerRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Netty HTTP server request.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NettyHttpServerRequest implements HttpServerRequest {

    /**
     * Stores the input stream.
     */
    private InputStream inputStream;

    /**
     * Stores the local address.
     */
    private final String localAddress;

    /**
     * Stores the local hostname.
     */
    private final String localHostname;

    /**
     * Stores the local port.
     */
    private final int localPort;
    
    /**
     * Stores the query parameters.
     */
    private Map<String, List<String>> queryParameters;

    /**
     * Stores the HTTP request.
     */
    private final HttpRequest request;

    /**
     * Constructor.
     *
     * @param request the HTTP request.
     * @param inputStream the input stream.
     * @param localAddress the local address.
     * @param localHostname the local hostname.
     * @param localPort the local port.
     */
    public NettyHttpServerRequest(HttpRequest request, InputStream inputStream,
            String localAddress, String localHostname, int localPort) {
        this.request = request;
        this.localAddress = localAddress;
        this.localHostname = localHostname;
        this.localPort = localPort;
    }

    /**
     * Get the header.
     *
     * @param name the name.
     * @return the value.
     */
    @Override
    public String getHeader(String name) {
        return request.headers().get(name);
    }

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    @Override
    public Iterator<String> getHeaderNames() {
        return request.headers().names().iterator();
    }

    /**
     * Get the input stream.
     *
     * @return the input stream.
     */
    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Get the local address.
     *
     * @return the local address.
     */
    @Override
    public String getLocalAddress() {
        return localAddress;
    }

    /**
     * Get the local hostname.
     *
     * @return the local hostname.
     */
    @Override
    public String getLocalHostname() {
        return localHostname;
    }

    /**
     * Get the local port.
     *
     * @return the local port.
     */
    @Override
    public int getLocalPort() {
        return localPort;
    }

    /**
     * Get the method.
     *
     * @return the method.
     */
    @Override
    public String getMethod() {
        return request.method().name();
    }

    /**
     * Get the query parameter.
     *
     * @param name the name.
     * @return the value.
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
     * Get the query string.
     * 
     * @return the query string.
     */
    @Override
    public String getQueryString() {
        String result = null;
        if (request.uri().contains("?")) {
            result = request.uri().substring(request.uri().indexOf("?") + 1);
        }
        return result;
    }

    @Override
    public String getRemoteAddress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRemoteHostname() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the request target.
     *
     * @return the request target.
     */
    @Override
    public String getRequestTarget() {
        return request.getUri();
    }
}
