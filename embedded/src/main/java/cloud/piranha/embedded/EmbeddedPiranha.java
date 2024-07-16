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
package cloud.piranha.embedded;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.core.api.PiranhaConfiguration;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.impl.DefaultPiranhaConfiguration;
import cloud.piranha.core.impl.DefaultWebApplication;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The embeddable version of Piranha.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EmbeddedPiranha implements Piranha {

    /**
     * Stores the configuration.
     */
    private PiranhaConfiguration configuration;
    
    /**
     * Stores the web application.
     */
    private final WebApplication webApplication;

    public EmbeddedPiranha() {
        configuration = new DefaultPiranhaConfiguration();
        webApplication = new DefaultWebApplication();
    }

    /**
     * Constructor.
     *
     * @param webApplication the web application to use.
     */
    public EmbeddedPiranha(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    /**
     * Destroy the web application.
     *
     * @return the instance.
     */
    public EmbeddedPiranha destroy() {
        webApplication.destroy();
        return this;
    }

    @Override
    public PiranhaConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * {
     *
     * @return the web application}
     */
    public WebApplication getWebApplication() {
        return webApplication;
    }

    /**
     * Initialize the web application.
     *
     * @return the instance.
     */
    public EmbeddedPiranha initialize() {
        webApplication.initialize();
        return this;
    }

    /**
     * This method services a request by dispatching it to the configured
     * Servlet and/or Filters.
     *
     * @param servletPath the request path, e.g. <code>/foo/bar</code>
     * @param parameters the request parameters, with each even parameter the
     * name, and odd parameter the value. e.g. <code>/foo, 1, bar, 2</code>
     * @return the response generated by the Servlet and/or Filters
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    public EmbeddedResponse service(String servletPath, String... parameters) throws IOException, ServletException {
        EmbeddedResponse response = new EmbeddedResponse();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath(servletPath)
                .build();

        if (parameters != null && parameters.length > 0) {
            if (parameters.length % 2 != 0) {
                throw new IllegalStateException("Parameters must be provided in pairs of two");
            }
            Map<String, ArrayList<String>> parameterMap = new HashMap<>();
            for (int i = 0; i <= parameters.length - 2; i += 2) {
                parameterMap.computeIfAbsent(parameters[i], e -> new ArrayList<>()).add(parameters[i + 1]);
            }
            for (Map.Entry<String, ArrayList<String>> parameterEntry : parameterMap.entrySet()) {
                request.setParameter(parameterEntry.getKey(), parameterEntry.getValue().toArray(String[]::new));
            }
        }

        service(request, response);
        return response;
    }

    /**
     * This method services a request by dispatching it to the configured
     * Servlet and/or Filters.
     *
     * @param request the request.
     * @return the response generated by the Servlet and/or Filters
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    public EmbeddedResponse service(WebApplicationRequest request) throws IOException, ServletException {
        EmbeddedResponse response = new EmbeddedResponse();
        service(request, response);
        return response;
    }

    /**
     * Service the request.
     * 
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    public void service(WebApplicationRequest request, WebApplicationResponse response) throws IOException, ServletException {
        if (request instanceof EmbeddedRequest embeddedRequest) {
            embeddedRequest.setWebApplication(webApplication);
        }
        if (response instanceof EmbeddedResponse embeddedResponse) {
            embeddedResponse.setWebApplication(webApplication);
        }

        webApplication.linkRequestAndResponse(request, response);
        webApplication.service(request, response);
        webApplication.unlinkRequestAndResponse(request, response);
    }

    /**
     * Start the web application.
     *
     * @return the instance.
     */
    public EmbeddedPiranha start() {
        webApplication.start();
        return this;
    }

    /**
     * Stop the web application.
     *
     * @return the instance.
     */
    public EmbeddedPiranha stop() {
        webApplication.stop();
        return this;
    }
}
