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
package cloud.piranha.http.webapp;

import cloud.piranha.core.impl.CookieParser;
import cloud.piranha.core.impl.DefaultWebApplicationInputStream;
import cloud.piranha.http.api.HttpServerRequest;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * The HttpServerRequest variant of WebApplicationRequest.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpWebApplicationRequest extends DefaultWebApplicationRequest {

    /**
     * Stores the wrapped HttpServerRequest.
     */
    private final HttpServerRequest wrapped;

    /**
     * Constructor.
     *
     * @param wrapped the wrapped HttpServerRequest.
     */
    public HttpWebApplicationRequest(HttpServerRequest wrapped) {
        this.wrapped = wrapped;
        populateRequest(wrapped);
    }

    /**
     * Populate the request.
     * 
     * @param serverRequest the HTTP server request.
     */
    private void populateRequest(HttpServerRequest serverRequest) {
        
        if (serverRequest.getRequestTarget() != null && serverRequest.getRequestTarget().contains("?")) {
            String requestTarget = serverRequest.getRequestTarget();
            contextPath = requestTarget.substring(0, requestTarget.indexOf("?"));
            queryString = requestTarget.substring(requestTarget.indexOf("?") + 1);
        } else {
            contextPath = serverRequest.getRequestTarget();
        }
        
        localAddress = serverRequest.getLocalAddress();
        localName = serverRequest.getLocalHostname();
        localPort = serverRequest.getLocalPort();
        method = serverRequest.getMethod();
        protocol = serverRequest.getProtocol();
        remoteAddr = serverRequest.getRemoteAddress();
        remoteHost = serverRequest.getRemoteHostname();
        remotePort = serverRequest.getRemotePort();
        serverName = serverRequest.getLocalHostname();
        serverPort = serverRequest.getLocalPort();
        webApplicationInputStream = new DefaultWebApplicationInputStream();
        webApplicationInputStream.setWebApplicationRequest(this);
        webApplicationInputStream.setInputStream(wrapped.getInputStream());

        Iterator<String> headerNames = serverRequest.getHeaderNames();
        while (headerNames.hasNext()) {
            String name = headerNames.next();
            String value = serverRequest.getHeader(name);
            serverRequest.getHeaders(name).forEachRemaining(x -> addHeader(name, x));
            if (name.equalsIgnoreCase("Content-Type")) {
                setContentType(value);
            }
            if (name.equalsIgnoreCase("Content-Length")) {
                setContentLength(Integer.parseInt(value));
            }
            if (name.equalsIgnoreCase("COOKIE")) {
                cookies = CookieParser.parse(value);
                Stream.of(cookies)
                    .filter(x -> "JSESSIONID".equals(x.getName()))
                    .findAny()
                    .ifPresent(cookie -> {
                        setRequestedSessionIdFromCookie(true);
                        setRequestedSessionId(cookie.getValue());
                    });
            }
        }

        if (contextPath != null) {
            String jsessionid = ";jsessionid=";
            int indexJsessionid = contextPath.indexOf(jsessionid);
            if (indexJsessionid > -1) {
                if (!isRequestedSessionIdFromCookie()) {
                    setRequestedSessionIdFromURL(true);
                    setRequestedSessionId(contextPath.substring(indexJsessionid + jsessionid.length()));
                }
                contextPath = contextPath.substring(0, indexJsessionid);
            }
        }
        
        if (serverRequest.isSecure()) {
            scheme = "https";
            if (wrapped.getSslCipherSuite() != null) {
                setAttribute("jakarta.servlet.request.cipher_suite", wrapped.getSslCipherSuite());
            }
            setAttribute("jakarta.servlet.request.key_size", wrapped.getSslKeySize());
            if (wrapped.getSslCertificates() != null) {
                setAttribute("jakarta.servlet.request.X509Certificate", wrapped.getSslCertificates());
                if (wrapped.getSslPrincipal() != null) {
                    /*
                     * setUserPrincipal(wrapped.getSslPrincipal());
                     * setAuthType(CLIENT_CERT_AUTH);
                     */
                }
            }
        }
    }
}
