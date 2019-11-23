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
package com.manorrock.piranha.nano;

import com.manorrock.piranha.DefaultHttpHeaderManager;
import com.manorrock.piranha.api.HttpHeaderManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.ReadListener;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

/**
 * The Nano version of HttpServletRequest and ServletInputStream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoHttpServletRequest extends ServletInputStream implements HttpServletRequest {

    /**
     * Stores the header manager.
     */
    private final HttpHeaderManager headerManager;

    /**
     * Stores the input stream.
     */
    private final InputStream inputStream;

    /**
     * Stores the method.
     */
    private String method;

    /**
     * Stores the path info.
     */
    private String pathInfo;

    /**
     * Stores the query string.
     */
    private String queryString;

    /**
     * Stores the servlet context.
     */
    ServletContext servletContext;

    /**
     * Stores the servlet path.
     */
    private String servletPath;

    /**
     * Constructor.
     *
     * @param inputStream the input stream.
     */
    public NanoHttpServletRequest(InputStream inputStream) {
        this.headerManager = new DefaultHttpHeaderManager();
        this.inputStream = inputStream;
        this.method = "GET";
        this.pathInfo = null;
        this.queryString = null;
        this.servletPath = "";
    }

    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public HttpSession getSession(boolean create) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isUserInRole(String role) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void login(String username, String password) throws ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void logout() throws ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public AsyncContext getAsyncContext() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Object getAttribute(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public long getContentLengthLong() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public DispatcherType getDispatcherType() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getLocalAddr() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getLocalName() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getLocalPort() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Enumeration<Locale> getLocales() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getParameter(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Enumeration<String> getParameterNames() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String[] getParameterValues(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getRemotePort() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isAsyncStarted() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isAsyncSupported() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void removeAttribute(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setAttribute(String name, Object object) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isFinished() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isReady() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int read() throws IOException {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     */
    public void addHeader(String name, String value) {
        headerManager.addHeader(name, value);
    }

    /**
     * Not supported.
     *
     * @param response the response.
     * @return true if authenticated, false otherwise.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @return the session id.
     */
    @Override
    public String changeSessionId() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @return the auth type.
     */
    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Get the context path.
     *
     * @return the context path.
     */
    @Override
    public String getContextPath() {
        return "";
    }

    /**
     * Not supported.
     *
     * @return the list of cookies.
     */
    @Override
    public Cookie[] getCookies() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Get the date header.
     *
     * @param name the name.
     * @return the date header as a long.
     */
    @Override
    public long getDateHeader(String name) {
        return headerManager.getDateHeader(name);
    }

    /**
     * Get the header.
     *
     * @param name the name.
     * @return the value, or null.
     */
    @Override
    public String getHeader(String name) {
        return headerManager.getHeader(name);
    }

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        return headerManager.getHeaderNames();
    }

    /**
     * Get the values for the given header.
     *
     * @param name the name.
     * @return the values.
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        return headerManager.getHeaders(name);
    }

    /**
     * Get the input stream.
     *
     * @return the input stream.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return this;
    }

    /**
     * Get the int header.
     *
     * @param name the name.
     * @return the value.
     */
    @Override
    public int getIntHeader(String name) {
        return headerManager.getIntHeader(name);
    }

    /**
     * Get the method.
     *
     * @return the method.
     */
    @Override
    public String getMethod() {
        return method;
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @return the part.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public Part getPart(String name) throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @return the parts.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Get the path info.
     *
     * @return the path info.
     */
    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    /**
     * Not supported.
     * 
     * @return the translated path.
     */
    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Get the protocol.
     *
     * @return the protocol.
     */
    @Override
    public String getProtocol() {
        return "HTTP/1.1";
    }

    /**
     * Not supported.
     * 
     * @return the remote user.
     */
    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @return the request URI.
     */
    @Override
    public String getRequestURI() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @return the request URL.
     */
    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException("Not supported");
    }
    
    /**
     * Get the query string.
     *
     * @return the query string.
     */
    @Override
    public String getQueryString() {
        return queryString;
    }

    /**
     * Get the scheme.
     *
     * @return the scheme.
     */
    @Override
    public String getScheme() {
        return "http";
    }

    /**
     * Get the servlet context.
     *
     * @return the servlet context.
     */
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * Get the servlet path.
     *
     * @return the servlet path.
     */
    @Override
    public String getServletPath() {
        return servletPath;
    }

    /**
     * Get the underlying input stream.
     *
     * @return the underlying input stream.
     */
    public InputStream getUnderlyingInputStream() {
        return inputStream;
    }

    /**
     * Set the method.
     *
     * @param method the method.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Set the path info.
     *
     * @param pathInfo the path info.
     */
    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    /**
     * Set the query string.
     *
     * @param queryString the query string.
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * Set the servlet context.
     *
     * @param servletContext the servlet context.
     */
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Set the servlet path.
     *
     * @param servletPath the servlet path.
     */
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }
}
