/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.grizzly;

import com.manorrock.piranha.api.HttpServerRequest;
import java.io.InputStream;
import java.util.Iterator;
import org.glassfish.grizzly.http.server.Request;

/**
 * The Grizzly HTTP server request.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
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
    
    /**
     * Get the header.
     * 
     * @param name the name.
     * @return the value.
     */
    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }

    /**
     * Get the header names.
     * 
     * @return the header names.
     */
    @Override
    public Iterator<String> getHeaderNames() {
        return request.getHeaderNames().iterator();
    }

    /**
     * Get the input stream.
     * 
     * @return the input stream.
     */
    @Override
    public InputStream getInputStream() {
        return request.getInputStream();
    }

    /**
     * Get the local address.
     * 
     * @return the local address.
     */
    @Override
    public String getLocalAddress() {
        return request.getLocalAddr();
    }

    /**
     * Get the local hostname.
     * 
     * @return the local hostname.
     */
    @Override
    public String getLocalHostname() {
        return request.getLocalName();
    }

    /**
     * Get the local port.
     * 
     * @return the local port.
     */
    @Override
    public int getLocalPort() {
        return request.getLocalPort();
    }

    /**
     * Get the method.
     * 
     * @return the method.
     */
    @Override
    public String getMethod() {
        return request.getMethod().getMethodString();
    }

    /**
     * Get the query parameter.
     * 
     * @param name the name.
     * @return the value.
     */
    @Override
    public String getQueryParameter(String name) {
        return request.getParameter(name);
    }

    /**
     * Get the query string.
     * 
     * @return the query string.
     */
    @Override
    public String getQueryString() {
        return request.getQueryString();
    }

    /**
     * Get the remote address.
     * 
     * @return the remote address.
     */
    @Override
    public String getRemoteAddress() {
        return request.getRemoteAddr();
    }

    /**
     * Get the remote hostname.
     * 
     * @return the remote hostname.
     */
    @Override
    public String getRemoteHostname() {
        return request.getRemoteHost();
    }

    /**
     * Get the remote port.
     * 
     * @return the remote port.
     */
    @Override
    public int getRemotePort() {
        return request.getRemotePort();
    }

    /**
     * Get the request target.
     * 
     * @return the request target.
     */
    @Override
    public String getRequestTarget() {
        return request.getRequestURI();
    }   
}
