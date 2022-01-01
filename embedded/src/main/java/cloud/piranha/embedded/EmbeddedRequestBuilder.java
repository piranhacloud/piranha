/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.embedded;

import jakarta.servlet.http.Cookie;

import cloud.piranha.core.api.WebApplication;

/**
 * The builder so you can easily build instances of
 * {@link cloud.piranha.embedded.EmbeddedRequest}.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.embedded.EmbeddedRequest
 */
public class EmbeddedRequestBuilder {

    /**
     * Stores the request.
     */
    private final EmbeddedRequest request;

    /**
     * Constructor.
     */
    public EmbeddedRequestBuilder() {
        request = new EmbeddedRequest();
    }

    /**
     * Add an attribute.
     *
     * @param name the name.
     * @param value the value.
     * @return the builder.
     */
    public EmbeddedRequestBuilder attribute(String name, Object value) {
        request.setAttribute(name, value);
        return this;
    }

    /**
     * Build the request.
     *
     * @return the request.
     */
    public EmbeddedRequest build() {
        if (request.getContextPath() == null) {
            request.setContextPath("");
        }
        return request;
    }

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     * @return the builder.
     */
    public EmbeddedRequestBuilder contextPath(String contextPath) {
        request.setContextPath(contextPath);
        return this;
    }

    /**
     * Add a cookie.
     *
     * @param cookie the cookie.
     * @return the builder.
     */
    public EmbeddedRequestBuilder cookie(Cookie cookie) {
        Cookie[] cookies;
        if (request.getCookies() != null) {
            cookies = new Cookie[request.getCookies().length + 1];
            for (int i = 0; i < cookies.length - 1; i++) {
                cookies[i] = (Cookie) cookies[i].clone();
            }
            cookies[cookies.length - 1] = cookie;
        } else {
            cookies = new Cookie[1];
            cookies[0] = cookie;
        }
        request.setCookies(cookies);
        return this;
    }
    
    /**
     * Adds a single valued header
     * 
     * @param name the name of the header
     * @param value the value of the header
     * @return the builder.
     */
    public EmbeddedRequestBuilder header(String name, String value) {
        request.setHeader(name, value);
        return this;
    }

    /**
     * Set the method.
     *
     * @param method the method.
     * @return the builder.
     */
    public EmbeddedRequestBuilder method(String method) {
        request.setMethod(method);
        return this;
    }

    /**
     * Add a parameter.
     *
     * @param name the name.
     * @param values the values.
     * @return the builder.
     */
    public EmbeddedRequestBuilder parameter(String name, String... values) {
        request.setParameter(name, values);
        return this;
    }

    /**
     * Set the path info.
     *
     * @param pathInfo the path info.
     * @return the builder.
     */
    public EmbeddedRequestBuilder pathInfo(String pathInfo) {
        request.setPathInfo(pathInfo);
        return this;
    }

    /**
     * Set the requested session id.
     * 
     * @param requestedSessionId the requested session id.
     * @return the builder.
     */
    public EmbeddedRequestBuilder requestedSessionId(String requestedSessionId) {
        request.setRequestedSessionId(requestedSessionId);
        return this;
    }

    /**
     * Set the requested session id from cookie flag.
     * 
     * @param requestedSessionIdFromCookie the requested session id from cookie flag.
     * @return the builder.
     */
    public EmbeddedRequestBuilder requestedSessionIdFromCookie(boolean requestedSessionIdFromCookie) {
        request.setRequestedSessionIdFromCookie(requestedSessionIdFromCookie);
        return this;
    }

    /**
     * Set the scheme.
     *
     * @param scheme the scheme.
     * @return the builder.
     */
    public EmbeddedRequestBuilder scheme(String scheme) {
        request.setScheme(scheme);
        return this;
    }

    /**
     * Set the servlet path.
     *
     * @param servletPath the servlet path.
     * @return the builder.
     */
    public EmbeddedRequestBuilder servletPath(String servletPath) {
        request.setServletPath(servletPath);
        return this;
    }

    /**
     * Set the web application.
     *
     * @param webApp the web application.
     * @return return the builder.
     */
    public EmbeddedRequestBuilder webApplication(WebApplication webApp) {
        request.setWebApplication(webApp);
        return this;
    }
}
