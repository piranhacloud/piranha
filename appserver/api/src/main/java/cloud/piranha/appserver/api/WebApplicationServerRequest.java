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
package cloud.piranha.appserver.api;

import static java.util.Map.entry;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationRequest;

/**
 * The WebApplicationServerRequest API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationServerRequest extends WebApplicationRequest {

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     */
    void setContextPath(String contextPath);

    /**
     * Set the servlet path.
     *
     * @param servletPath the servlet path.
     */
    void setServletPath(String servletPath);

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    void setWebApplication(WebApplication webApplication);

    default Map<String, Object> toMap() {
        return Map.ofEntries(
            entry("LocalAddr", getLocalAddr()),
            entry("LocalName", getLocalName()),
            entry("LocalPort", getLocalPort()),
            entry("RemoteAddr", getRemoteAddr()),
            entry("RemoteHost", getRemoteHost()),
            entry("RemotePort", getRemotePort()),
            entry("ServerName", getServerName()),
            entry("ServerPort", getServerPort()),
            entry("Method", getMethod()),
            entry("ContextPath", getContextPath()),
            entry("ServletPath", getServletPath()),
            entry("QueryString", getQueryString()),
            entry("InputStream", getInputStreamUnchecked()),
            entry("Headers", getHeadersAsMap()));
    }

    private InputStream getInputStreamUnchecked() {
        try {
            return getInputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Map<String, List<String>> getHeadersAsMap() {
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> headerNames = getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = getHeader(name);
            headers.computeIfAbsent(name, e -> new ArrayList<>()).add(value);
        }

        return headers;
    }


}
