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

import com.manorrock.piranha.api.HttpServerRequest;
import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Test HttpServerRequest class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpServerRequest implements HttpServerRequest {
    
    /**
     * Stores the headers.
     */
    private ConcurrentHashMap<String, String[]> headers;
    
    /**
     * Stores the query string.
     */
    private String queryString;
    
    /**
     * Stores the request target.
     */
    private String requestTarget;

    /**
     * Constructor.
     */
    public TestHttpServerRequest() {
        this.headers = new ConcurrentHashMap<>();
        this.requestTarget = "";
    }

    @Override
    public String getHeader(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the header names.
     * 
     * @return the header names.
     */
    @Override
    public Iterator<String> getHeaderNames() {
        return headers.keySet().iterator();
    }

    @Override
    public InputStream getInputStream() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLocalAddress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLocalHostname() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the query string.
     * 
     * @return the query string.
     */
    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public String getMethod() {
        throw new UnsupportedOperationException("Not supported yet.");
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
        return requestTarget;
    }
    
    @Override
    public String getQueryParameter(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Set the query string.
     * 
     * @param queryString the query string. 
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
    /**
     * Set the request target.
     * 
     * @param requestTarget the request target.
     */
    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }
}
