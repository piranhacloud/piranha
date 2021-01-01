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
package cloud.piranha.http.undertow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import cloud.piranha.http.api.HttpServerRequest;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

/**
 * The Undertow implementation of HTTP Server Request.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class UndertowHttpRequest implements HttpServerRequest {

    /**
     * Stores the HTTP server exchange.
     */
    private final HttpServerExchange exchange;
    
    /**
     * Stores the header names.
     */
    private ArrayList<String> headerNames;

    /**
     * Constructor.
     *
     * @param exchange the HTTP server exchange.
     */
    public UndertowHttpRequest(HttpServerExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public String getHeader(String name) {
        return exchange.getRequestHeaders().getFirst(name);
    }

    @Override
    public Iterator<String> getHeaderNames() {
        if (headerNames == null) {
            headerNames = new ArrayList<>();
            Iterator<HttpString> names = exchange.getRequestHeaders().getHeaderNames().iterator();
            while(names.hasNext()) {
                headerNames.add(names.next().toString());
            }
        }
        return headerNames.iterator();
    }

    @Override
    public Iterator<String> getHeaders(String name) {
        return exchange.getRequestHeaders().get(name).iterator();
    }

    @Override
    public InputStream getInputStream() {
        if (!exchange.isBlocking()) {
            exchange.startBlocking();
        }
        return exchange.getInputStream();
    }

    @Override
    public String getLocalAddress() {
        return exchange.getDestinationAddress().getAddress().getHostAddress();
    }

    @Override
    public String getLocalHostname() {
        return exchange.getHostName();
    }

    @Override
    public int getLocalPort() {
        return exchange.getHostPort();
    }

    @Override
    public String getMethod() {
        return exchange.getRequestMethod().toString();
    }

    @Override
    public String getQueryParameter(String name) {
        return exchange.getQueryParameters().get(name).getFirst();
    }

    @Override
    public String getQueryString() {
        return exchange.getQueryString();
    }

    @Override
    public String getRemoteAddress() {
        return exchange.getSourceAddress().getAddress().getHostAddress();
    }

    @Override
    public String getRemoteHostname() {
        return exchange.getSourceAddress().getAddress().getHostName();
    }

    @Override
    public int getRemotePort() {
        return exchange.getSourceAddress().getPort();
    }

    @Override
    public String getRequestTarget() {
        return exchange.getRequestURI();
    }

    @Override
    public String getProtocol() {
        return exchange.getProtocol().toString();
    }
}
