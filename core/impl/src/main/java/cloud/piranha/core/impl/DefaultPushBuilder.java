/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.WebApplicationRequest;
import jakarta.servlet.http.PushBuilder;
import java.util.HashSet;
import java.util.Set;

/**
 * The default PushBuilder.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultPushBuilder implements PushBuilder {

    /**
     * Stores the header manager.
     */
    private final DefaultHttpHeaderManager headerManager = new DefaultHttpHeaderManager();
    
    /**
     * Stores the method.
     */
    private String method;
    
    /**
     * Stores the path.
     */
    private String path;
    
    /**
     * Stores the query string.
     */
    private String queryString;
    
    /**
     * Stores the web application request.
     */
    private final WebApplicationRequest request;
    
    /**
     * Stores the session id.
     */
    private String sessionId;
    
    /**
     * Constructor.
     * 
     * @param request the web application request.
     */
    public DefaultPushBuilder(WebApplicationRequest request) {
        this.request = request;
    }

    @Override
    public PushBuilder method(String method) {
        this.method = method;
        return this;
    }

    @Override
    public PushBuilder queryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    @Override
    public PushBuilder sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    public PushBuilder setHeader(String name, String value) {
        headerManager.setHeader(name, value);
        return this;
    }

    @Override
    public PushBuilder addHeader(String name, String value) {
        headerManager.addHeader(name, value);
        return this;
    }

    @Override
    public PushBuilder removeHeader(String name) {
        headerManager.removeHeader(name);
        return this;
    }

    @Override
    public PushBuilder path(String path) {
        this.path = path;
        return this;
    }

    @Override
    public void push() {
        path = null;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Set<String> getHeaderNames() {
        HashSet<String> names = new HashSet<>();
        headerManager.getHeaderNames()
                .asIterator()
                .forEachRemaining(name -> names.add(name));
        return names;
    }

    @Override
    public String getHeader(String name) {
        return headerManager.getHeader(name);
    }

    @Override
    public String getPath() {
        return path;
    }
}
