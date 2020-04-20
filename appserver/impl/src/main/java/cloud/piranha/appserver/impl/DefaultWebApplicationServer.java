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
package cloud.piranha.appserver.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.api.HttpServerRequest;
import cloud.piranha.http.api.HttpServerResponse;
import cloud.piranha.api.WebApplication;
import cloud.piranha.appserver.api.WebApplicationServer;
import cloud.piranha.appserver.api.WebApplicationServerRequest;
import cloud.piranha.appserver.api.WebApplicationServerRequestMapper;
import cloud.piranha.appserver.api.WebApplicationServerResponse;

/**
 * The default WebApplicationServer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationServer
        implements HttpServerProcessor, WebApplicationServer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultWebApplicationServer.class.getName());

    /**
     * Stores the async boolean.
     */
    protected boolean async = false;
    
    /**
     * Stores the request mapper.
     */
    protected WebApplicationServerRequestMapper requestMapper;

    /**
     * Stores the web applications.
     */
    protected final HashMap<String, WebApplication> webApplications;

    /**
     * Constructor.
     */
    public DefaultWebApplicationServer() {
        this.requestMapper = new DefaultWebApplicationServerRequestMapper();
        this.webApplications = new HashMap<>();
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
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Adding web application with context path: {0}", webApplication.getContextPath());
        }
        webApplications.put(webApplication.getContextPath(), webApplication);
        requestMapper.addMapping(webApplication, webApplication.getContextPath());
    }

    /**
     * Create the web application server request.
     *
     * @param request the HTTP server request.
     * @return the web application server request.
     */
    private WebApplicationServerRequest createRequest(HttpServerRequest request) {
        DefaultWebApplicationServerRequest result = new DefaultWebApplicationServerRequest();
        result.setLocalAddr(request.getLocalAddress());
        result.setLocalName(request.getLocalHostname());
        result.setLocalPort(request.getLocalPort());
        result.setRemoteAddr(request.getRemoteAddress());
        result.setRemoteHost(request.getRemoteHostname());
        result.setRemotePort(request.getRemotePort());
        result.setServerName(request.getLocalHostname());
        result.setServerPort(request.getLocalPort());
        result.setMethod(request.getMethod());
        result.setContextPath(request.getRequestTarget());
        result.setServletPath("");
        result.setQueryString(request.getQueryString());
        Iterator<String> headerNames = request.getHeaderNames();
        while (headerNames.hasNext()) {
            String name = headerNames.next();
            String value = request.getHeader(name);
            result.setHeader(name, value);
            if (name.equalsIgnoreCase("Content-Type")) {
                result.setContentType(value);
            }
            if (name.equalsIgnoreCase("Content-Length")) {
                result.setContentLength(Integer.parseInt(value));
            }
            if (name.equalsIgnoreCase("COOKIE")) {
                ArrayList<Cookie> cookieList = new ArrayList<>();
                String[] cookieCandidates = value.split(";");
                if (cookieCandidates.length > 0) {
                    for (String cookieCandidate : cookieCandidates) {
                        String[] cookieString = cookieCandidate.split("=");
                        Cookie cookie = new Cookie(cookieString[0].trim(), cookieString[1].trim());
                        if (cookie.getName().equals("JSESSIONID")) {
                            result.setRequestedSessionIdFromCookie(true);
                            result.setRequestedSessionId(cookie.getValue());
                        } else {
                            cookieList.add(cookie);
                        }
                    }
                }
                result.setCookies(cookieList.toArray(new Cookie[0]));
            }
        }
        result.setInputStream(request.getInputStream());
        return result;
    }

    /**
     * Create the web application server response.
     *
     * @param response the HTTP server response.
     * @return the web application server response.
     */
    public WebApplicationServerResponse createResponse(HttpServerResponse response) {
        DefaultWebApplicationServerResponse result = new DefaultWebApplicationServerResponse();
        result.setUnderlyingOutputStream(response.getOutputStream());
        return result;
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
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Starting initialization of {0} web application(s)", webApplications.size());
        }
        webApplications.values().forEach((webApp) -> {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.initialize();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        });
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Finished initialization of {0} web application(s)", webApplications.size());
        }
    }

    /**
     * Are we processing the request asynchronously.
     * 
     * @return true if we are, false otherwise.
     */
    @Override
    public boolean isAsync() {
        return async;
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
            DefaultWebApplicationServerRequest serverRequest = (DefaultWebApplicationServerRequest) createRequest(request);
            DefaultWebApplicationServerResponse serverResponse = (DefaultWebApplicationServerResponse) createResponse(response);
            service(serverRequest, serverResponse);
        } catch (Exception exception) {
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
    public void service(WebApplicationServerRequest request, WebApplicationServerResponse response) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        if (requestUri == null) {
            response.sendError(500);
            return;
        }

        WebApplication webApplication = requestMapper.findMapping(requestUri);
        if (webApplication == null) {
            response.sendError(404);
            return;
        }

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(webApplication.getClassLoader());
            String contextPath = webApplication.getContextPath();
            request.setContextPath(contextPath);
            request.setServletPath(requestUri.substring(contextPath.length()));
            request.setWebApplication(webApplication);
            response.setWebApplication(webApplication);

            webApplication.service(request, response);
            
            // Make sure the request is fully read wrt parameters (if any still)
            request.getParameterMap();
            
            if (request.isAsyncStarted()) {
                async = true;
            }            
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
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
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.info("Starting WebApplication server engine");
        }
        webApplications.values().forEach((webApp) -> {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.start();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        });
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.info("Started WebApplication server engine");
        }
    }

    /**
     * Stop the server.
     */
    @Override
    public void stop() {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.info("Stopping WebApplication server engine");
        }
        webApplications.values().forEach((webApp) -> {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.stop();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        });
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.info("Stopped WebApplication server engine");
        }
    }
}
