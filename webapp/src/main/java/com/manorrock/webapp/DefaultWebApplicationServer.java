/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import com.manorrock.httpserver.HttpServerProcessor;
import com.manorrock.httpserver.HttpServerRequest;
import com.manorrock.httpserver.HttpServerResponse;
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
        WebApplicationServer<DefaultHttpServletRequest, DefaultHttpServletResponse> {

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
        for (WebApplication webApp : webApplications.values()) {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.initialize();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        }
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
    public void service(DefaultHttpServletRequest request, DefaultHttpServletResponse response)
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
        for (WebApplication webApp : webApplications.values()) {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.start();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        }
    }

    /**
     * Stop the server.
     */
    @Override
    public void stop() {
        for (WebApplication webApp : webApplications.values()) {
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(webApp.getClassLoader());
                webApp.stop();
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        }
    }
}
