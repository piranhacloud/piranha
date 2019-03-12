/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha;

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
