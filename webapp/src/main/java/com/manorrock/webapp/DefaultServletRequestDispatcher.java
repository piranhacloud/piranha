/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The default ServletRequestDispatcher.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultServletRequestDispatcher implements RequestDispatcher {

    /**
     * Stores the servlet environment.
     */
    private final DefaultServletEnvironment servletEnvironment;
    /**
     * Stores the path.
     */
    private final String path;

    /**
     * Constructor.
     *
     * @param servletEnvironment the servlet environment.
     * @param path the path.
     */
    public DefaultServletRequestDispatcher(DefaultServletEnvironment servletEnvironment, String path) {
        this.servletEnvironment = servletEnvironment;
        this.path = path;
    }

    /**
     * Set forward attribute.
     *
     * @param originalRequest the original request
     * @param forwardedRequest the forward request.
     * @param dispatcherKey the dispatcher key.
     */
    private void setForwardAttribute(
            HttpServletRequest originalRequest,
            HttpServletRequest forwardedRequest,
            String dispatcherKey) {

        String value = null;

        if (originalRequest.getAttribute(dispatcherKey) != null) {
            value = (String) originalRequest.getAttribute(dispatcherKey);
        } else {
            if (dispatcherKey.equals(RequestDispatcher.FORWARD_CONTEXT_PATH)) {
                value = originalRequest.getContextPath();
            }
            if (dispatcherKey.equals(RequestDispatcher.FORWARD_PATH_INFO)) {
                value = originalRequest.getPathInfo();
            }
            if (dispatcherKey.equals(RequestDispatcher.FORWARD_QUERY_STRING)) {
                value = originalRequest.getQueryString();
            }
            if (dispatcherKey.equals(RequestDispatcher.FORWARD_REQUEST_URI)) {
                value = originalRequest.getRequestURI();
            }
            if (dispatcherKey.equals(RequestDispatcher.FORWARD_SERVLET_PATH)) {
                value = originalRequest.getServletPath();
            }
        }

        forwardedRequest.setAttribute(dispatcherKey, value);
    }

    /**
     * Forward the request and response.
     *
     * @param servletRequest the request.
     * @param servletResponse the response.
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.resetBuffer();

        DefaultServletRequestDispatcherRequest forwardedRequest = new DefaultServletRequestDispatcherRequest();
        forwardedRequest.setWebApplication(servletEnvironment.getWebApplication());
        forwardedRequest.setContextPath(request.getContextPath());

        if (path != null) {
            setForwardAttribute(request, forwardedRequest, RequestDispatcher.FORWARD_CONTEXT_PATH);
            setForwardAttribute(request, forwardedRequest, RequestDispatcher.FORWARD_PATH_INFO);
            setForwardAttribute(request, forwardedRequest, RequestDispatcher.FORWARD_QUERY_STRING);
            setForwardAttribute(request, forwardedRequest, RequestDispatcher.FORWARD_REQUEST_URI);
            setForwardAttribute(request, forwardedRequest, RequestDispatcher.FORWARD_SERVLET_PATH);

            String servletPath = !path.contains("?") ? path : path.substring(0, path.indexOf("?"));
            forwardedRequest.setServletPath(servletPath);

            String queryString = !path.contains("?") ? null : path.substring(path.indexOf("?") + 1);
            forwardedRequest.setQueryString(queryString);

        } else {
            forwardedRequest.setServletPath("/" + servletEnvironment.getServletName());
        }

        try {
            servletEnvironment.getWebApplication().linkRequestAndResponse(forwardedRequest, servletResponse);
            servletEnvironment.getServlet().service(forwardedRequest, servletResponse);
            servletEnvironment.getWebApplication().unlinkRequestAndResponse(forwardedRequest, servletResponse);
        } catch (IOException | ServletException exception) {
            throw exception;
        }

        response.flushBuffer();
    }

    /**
     * Include the request and response.
     *
     * @param servletRequest the request.
     * @param servletResponse the response.
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        DefaultServletRequestDispatcherRequest req = new DefaultServletRequestDispatcherRequest();
        DefaultServletRequestDispatcherResponse res = new DefaultServletRequestDispatcherResponse();

        HttpServletRequest originalRequest = (HttpServletRequest) servletRequest;

        req.setAttribute(RequestDispatcher.INCLUDE_REQUEST_URI, originalRequest.getRequestURI());
        req.setAttribute(RequestDispatcher.INCLUDE_CONTEXT_PATH, originalRequest.getContextPath());
        req.setAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH, originalRequest.getServletPath());
        req.setAttribute(RequestDispatcher.INCLUDE_PATH_INFO, originalRequest.getPathInfo());
        req.setAttribute(RequestDispatcher.INCLUDE_QUERY_STRING, originalRequest.getQueryString());
        req.setContextPath(originalRequest.getContextPath());
        req.setServletPath(path);
        req.setPathInfo(null);
        req.setQueryString(null);
        req.setWebApplication(servletEnvironment.getWebApplication());

        servletEnvironment.getServlet().service(servletRequest, servletResponse);

        try {
            ServletOutputStream outputStream = servletResponse.getOutputStream();
            outputStream.write(res.getResponseBody());
            outputStream.flush();
        } catch (IllegalStateException exception) {
            PrintWriter writer = servletResponse.getWriter();
            writer.print(new String(res.getResponseBody()));
            writer.flush();
        }
    }
}
