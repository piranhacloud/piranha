/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.http.jdk;

import cloud.piranha.http.api.HttpServerRequest;
import com.sun.net.httpserver.HttpExchange;
import java.io.InputStream;
import java.util.Iterator;

/**
 * The JDK HttpServer version of a HttpServerRequest.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class JdkHttpRequest implements HttpServerRequest {

    /**
     * Stores the HTTP exchange.
     */
    private final HttpExchange exchange;

    /**
     * Constructor.
     *
     * @param exchange the HTTP exchange.
     */
    public JdkHttpRequest(HttpExchange exchange) {
        this.exchange = exchange;
    }

    @Override
    public String getHeader(String name) {
        return exchange.getRequestHeaders().getFirst(name);
    }

    @Override
    public Iterator<String> getHeaderNames() {
        return exchange.getRequestHeaders().keySet().iterator();
    }

    @Override
    public Iterator<String> getHeaders(String name) {
        return exchange.getRequestHeaders().get(name).iterator();
    }

    @Override
    public InputStream getInputStream() {
        return exchange.getRequestBody();
    }

    @Override
    public String getLocalAddress() {
        return exchange.getLocalAddress().getHostString();
    }

    @Override
    public String getLocalHostname() {
        return exchange.getLocalAddress().getHostName();
    }

    @Override
    public int getLocalPort() {
        return exchange.getLocalAddress().getPort();
    }

    @Override
    public String getMethod() {
        return exchange.getRequestMethod();
    }

    @Override
    public String getRemoteAddress() {
        return exchange.getRemoteAddress().getHostString();
    }

    @Override
    public String getRemoteHostname() {
        return exchange.getRemoteAddress().getHostName();
    }

    @Override
    public int getRemotePort() {
        return exchange.getRemoteAddress().getPort();
    }

    @Override
    public String getRequestTarget() {
        return exchange.getRequestURI().toString();
    }

    @Override
    public String getProtocol() {
        return exchange.getProtocol();
    }

    @Override
    public boolean isSecure() {
        return exchange.getRequestURI().getRawSchemeSpecificPart().equalsIgnoreCase("https");
    }
}
