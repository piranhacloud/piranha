/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
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

import com.manorrock.piranha.api.WebApplicationServerRequestMapper;
import com.manorrock.piranha.api.WebApplicationServer;
import com.manorrock.piranha.api.WebApplication;
import com.manorrock.piranha.api.HttpServerResponse;
import com.manorrock.piranha.api.HttpServerRequest;
import com.manorrock.piranha.api.HttpServerProcessor;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletException;

/**
 * The default WebApplicationServer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationServer implements
        HttpServerProcessor,
        WebApplicationServer<DefaultWebApplicationRequest, DefaultWebApplicationResponse> {

    /**
     * Stores the request mapper.
     */
    protected WebApplicationServerRequestMapper requestMapper;

    /**
     * Stores the web applications.
     */
    protected final ConcurrentHashMap<String, WebApplication> webApplications;

    /**
     * Constructor.
     */
    public DefaultWebApplicationServer() {
        this.requestMapper = new DefaultWebApplicationServerRequestMapper();
        this.webApplications = new ConcurrentHashMap<>();
    }

    /**
     * Add a context path mapping.
     *
     * @param servletContextName the servlet context name.
     * @param contextPath the context path.
     */
    public void addMapping(String servletContextName, String contextPath) {
        Iterator<WebApplication> webApps = webApplications.values().iterator();
        while (webApps.hasNext()) {
            WebApplication webApp = webApps.next();
            if (webApp.getServletContextName().equals(servletContextName)) {
                requestMapper.addMapping(webApp, contextPath);
                break;
            }
        }
    }

    /**
     * Add the web application.
     *
     * @param webApplication the web application.
     */
    @Override
    public void addWebApplication(WebApplication webApplication) {
        webApplications.put(webApplication.getContextPath(), webApplication);
        requestMapper.addMapping(webApplication, webApplication.getContextPath());
    }

    /**
     * Get the request mapper.
     *
     * @return the request mapper.
     */
    @Override
    public WebApplicationServerRequestMapper getRequestMapper() {
        return requestMapper;
    }

    /**
     * Initialize the server.
     */
    @Override
    public void initialize() {
        webApplications.values().forEach((webApp) -> {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.initialize();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        });
    }

    /**
     * Process the request.
     *
     * @param request the request.
     * @param response the response.
     */
    @Override
    public void process(HttpServerRequest request, HttpServerResponse response) {
        try {
            DefaultWebApplicationServerRequest servletRequest = new DefaultWebApplicationServerRequest(request);
            DefaultWebApplicationServerResponse servletResponse = new DefaultWebApplicationServerResponse(response);
            DefaultWebApplicationServerInputStream inputStream = new DefaultWebApplicationServerInputStream(
                    request.getInputStream(), servletRequest);
            servletRequest.setInputStream(inputStream);
            try (DefaultWebApplicationServerOutputStream outputStream = new DefaultWebApplicationServerOutputStream()) {
                outputStream.setOutputStream(response.getOutputStream());
                servletResponse.setOutputStream(outputStream);
                outputStream.setResponse(servletResponse);
                if (request.getRequestTarget().contains("?")) {
                    servletRequest.setQueryString(request.getRequestTarget().substring(request.getRequestTarget().indexOf("?") + 1));
                }
                service(servletRequest, servletResponse);
                if (!servletResponse.isCommitted()) {
                    servletResponse.flushBuffer();
                }
                outputStream.flush();
            }
        } catch (IOException | ServletException exception) {
            exception.printStackTrace(System.err);
        }
    }

    /**
     * Service method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void service(DefaultWebApplicationRequest request, DefaultWebApplicationResponse response)
            throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        if (requestUri != null) {
            WebApplication webApplication = requestMapper.findMapping(requestUri);

            if (webApplication != null) {
                ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
                try {
                    Thread.currentThread().setContextClassLoader(webApplication.getClassLoader());
                    String contextPath = webApplication.getContextPath();
                    request.setContextPath(contextPath);
                    request.setServletPath(requestUri.substring(contextPath.length()));
                    request.setWebApplication(webApplication);
                    response.setWebApplication(webApplication);
                    webApplication.service(request, response);
                } finally {
                    Thread.currentThread().setContextClassLoader(oldClassLoader);
                }

            } else {
                response.sendError(404);
            }
        } else {
            response.sendError(500);
        }
    }

    /**
     * Set the request mapper.
     *
     * @param requestMapper the request mapper.
     */
    @Override
    public void setRequestMapper(WebApplicationServerRequestMapper requestMapper) {
        this.requestMapper = requestMapper;
    }

    /**
     * Start the server.
     */
    @Override
    public void start() {
        webApplications.values().forEach((webApp) -> {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.start();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        });
    }

    /**
     * Stop the server.
     */
    @Override
    public void stop() {
        webApplications.values().forEach((webApp) -> {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.stop();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        });
    }
}
