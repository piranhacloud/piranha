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
package cloud.piranha.micro.builder;

import static java.util.Map.entry;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.impl.DefaultWebApplication;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * A Piranha Micro web application.
 *
 * @author Arjan Tijms
 */
public class MicroWebApplication extends DefaultWebApplication {

    /**
     * Runnable to do nothing
     */
    private final Runnable doNothing = new Runnable() {
        @Override
        public void run() {
            // do nothing ;)
        }
    };

    /**
     * Stores the deployed application.
     */
    private Consumer<Map<String, Object>> deployedApplication;

    /**
     * {@return the deployed application}
     */
    public Consumer<Map<String, Object>> getDeployedApplication() {
        return deployedApplication;
    }

    /**
     * Set the deployed application.
     *
     * @param deployedApplication the deployed application.
     */
    public void setDeployedApplication(Consumer<Map<String, Object>> deployedApplication) {
        this.deployedApplication = deployedApplication;
    }

    /**
     * Service the request.
     *
     * @param request the request.
     * @param response the resposne.
     */
    @Override
    public void service(ServletRequest request, ServletResponse response) {
        deployedApplication.accept(copyApplicationRequestToMap((WebApplicationRequest) request, (WebApplicationResponse) response));
    }

    /**
     * Copy the request and response to a map.
     *
     * @param applicationRequest the web application request.
     * @param applicationResponse the web application response.
     * @return the map.
     */
    private Map<String, Object> copyApplicationRequestToMap(WebApplicationRequest applicationRequest, WebApplicationResponse applicationResponse) {
        Map<String, Object> requestValues = new HashMap<>();

        requestValues.putAll(requestToMap(applicationRequest));
        requestValues.putAll(responseToMap(applicationResponse));

        return requestValues;
    }

    /**
     * Get a map of request.
     *
     * @param request the web application request.
     * @return the map.
     */
    private Map<String, Object> requestToMap(WebApplicationRequest request) {
        return Map.ofEntries(
                entry("LocalAddr", request.getLocalAddr()),
                entry("LocalName", request.getLocalName()),
                entry("LocalPort", request.getLocalPort()),
                entry("RemoteAddr", request.getRemoteAddr()),
                entry("RemoteHost", request.getRemoteHost()),
                entry("RemotePort", request.getRemotePort()),
                entry("ServerName", request.getServerName()),
                entry("ServerPort", request.getServerPort()),
                entry("Method", request.getMethod()),
                entry("ContextPath", request.getContextPath()),
                entry("ServletPath", request.getServletPath()),
                entry("QueryString", request.getQueryString() == null ? "" : request.getQueryString()),
                entry("InputStream", getInputStreamUnchecked(request)),
                entry("Headers", getHeadersAsMap(request)));
    }

    /**
     * Get the input stream unchecked.
     * 
     * @param request the web application request.
     * @retunr the unchecked input stream.
     */
    private InputStream getInputStreamUnchecked(WebApplicationRequest request) {
        try {
            return request.getInputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Get the headers as a map.
     *
     * @param request the web application request.
     * @return the map.
     */
    private Map<String, List<String>> getHeadersAsMap(WebApplicationRequest request) {
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            headers.computeIfAbsent(name, e -> new ArrayList<>()).add(value);
        }
        return headers;
    }

    /**
     * Get a map of underlying output stream and response closer.
     *
     * @param response the web application response.
     * @return the map.
     */
    private Map<String, Object> responseToMap(WebApplicationResponse response) {
        return Map.of(
                "WebApplicationOutputStream", response.getWebApplicationOutputStream(),
                "ResponseCloser", response.getResponseCloser() == null ? doNothing : response.getResponseCloser());
    }
}
