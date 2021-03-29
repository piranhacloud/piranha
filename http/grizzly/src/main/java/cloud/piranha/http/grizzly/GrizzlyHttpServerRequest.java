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
package cloud.piranha.http.grizzly;

import java.io.InputStream;

import org.glassfish.grizzly.http.server.Request;

import cloud.piranha.http.api.HttpServerRequest;
import java.util.Iterator;

/**
 * The Grizzly implementation of HTTP Server Request.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see HttpServerRequest
 */
public class GrizzlyHttpServerRequest implements HttpServerRequest {

    /**
     * Stores the Grizzly request.
     */
    private final Request request;

    /**
     * Constructor.
     *
     * @param request the Grizzly request.
     */
    public GrizzlyHttpServerRequest(Request request) {
        this.request = request;
    }

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    @Override
    public Iterator<String> getHeaderNames() {
        return request.getHeaderNames().iterator();
    }

    @Override
    public Iterator<String> getHeaders(String name) {
        return request.getHeaders(name).iterator();
    }

    @Override
    public String getHttpVersion() {
        return request.getProtocol().getProtocolString();
    }

    @Override
    public String getLocalAddress() {
        return request.getLocalAddr();
    }

    @Override
    public String getLocalHostname() {
        return request.getLocalName();
    }

    @Override
    public int getLocalPort() {
        return request.getLocalPort();
    }

    @Override
    public InputStream getMessageBody() {
        return request.getInputStream();
    }

    @Override
    public String getMethod() {
        return request.getMethod().getMethodString();
    }

    @Override
    public String getRemoteAddress() {
        return request.getRemoteAddr();
    }

    @Override
    public String getRemoteHostname() {
        return request.getRemoteHost();
    }

    @Override
    public int getRemotePort() {
        return request.getRemotePort();
    }

    @Override
    public String getRequestTarget() {
        return request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }
}
