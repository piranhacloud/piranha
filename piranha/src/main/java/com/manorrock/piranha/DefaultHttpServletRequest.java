/*
 *  Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

/**
 * The default HttpServletRequest.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class DefaultHttpServletRequest implements WebApplicationRequest {

    /**
     * Stores the auth type.
     */
    protected String authType;

    /**
     * Stores the async context.
     */
    protected AsyncContext asyncContext;

    /**
     * Stores if async is started.
     */
    protected boolean asyncStarted;

    /**
     * Stores if async is supported.
     */
    protected boolean asyncSupported;

    /**
     * Stores the attribute manager.
     */
    protected AttributeManager attributeManager;

    /**
     * Stores the character encoding.
     */
    protected String characterEncoding;

    /**
     * Stores the content length.
     */
    protected long contentLength;

    /**
     * Stores the content type.
     */
    protected String contentType;

    /**
     * Stores the context path.
     */
    protected String contextPath;

    /**
     * Stores the cookies.
     */
    protected Cookie[] cookies;

    /**
     * Stores the current session id.
     */
    protected String currentSessionId;

    /**
     * Stores the dispatcher type.
     */
    protected DispatcherType dispatcherType;

    /**
     * Stores the header manager.
     */
    protected HttpHeaderManager headerManager;

    /**
     * Stores the input stream.
     */
    protected ServletInputStream inputStream;

    /**
     * Stores the local address.
     */
    protected String localAddress;

    /**
     * Stores the local name.
     */
    protected String localName;

    /**
     * Stores the local port.
     */
    protected int localPort;

    /**
     * Stores the method.
     */
    protected String method;

    /**
     * Stores the parameters.
     */
    protected HashMap<String, String[]> parameters;

    /**
     * Stores the parameters parsed flag.
     */
    protected boolean parametersParsed;

    /**
     * Stores the parts.
     */
    protected HashMap<String, Part> parts;

    /**
     * Stores the path info.
     */
    protected String pathInfo;

    /**
     * Stores the protocol.
     */
    protected String protocol;

    /**
     * Stores the query string.
     */
    protected String queryString;

    /**
     * Stores the remote address.
     */
    protected String remoteAddr;

    /**
     * Stores the remote host.
     */
    protected String remoteHost;

    /**
     * Stores the remote port.
     */
    protected int remotePort;

    /**
     * Stores the requested session id.
     */
    protected String requestedSessionId;

    /**
     * Stores the requested session id from cookie flag.
     */
    protected boolean requestedSessionIdFromCookie;

    /**
     * Stores the requested session id from url flag.
     */
    protected boolean requestedSessionIdFromURL;

    /**
     * Stores the scheme.
     */
    protected String scheme;

    /**
     * Stores the server name.
     */
    protected String serverName;

    /**
     * Stores the server port.
     */
    protected int serverPort;

    /**
     * Stores the servlet path.
     */
    protected String servletPath;

    /**
     * Stores the upgraded flag.
     */
    protected boolean upgraded;

    /**
     * Stores the upgrade handler.
     */
    protected HttpUpgradeHandler upgradeHandler;

    /**
     * Stores the user principal.
     */
    protected Principal userPrincipal;

    /**
     * Stores the web application
     */
    protected WebApplication webApplication;

    /**
     * Constructor.
     */
    public DefaultHttpServletRequest() {
        this.authType = null;
        this.asyncStarted = false;
        this.asyncSupported = false;
        this.attributeManager = new DefaultAttributeManager();
        this.characterEncoding = null;
        this.contentLength = -1;
        this.contentType = null;
        this.contextPath = "";
        this.cookies = null;
        this.dispatcherType = DispatcherType.REQUEST;
        this.headerManager = new DefaultHttpHeaderManager();
        this.headerManager.setHeader("Accept", "*/*");
        this.method = "GET";
        this.protocol = "HTTP/1.1";
        this.scheme = "http";
        this.serverName = "localhost";
        this.serverPort = 80;
        this.servletPath = "";
        this.parameters = new HashMap<>();
        this.parts = new HashMap<>();
        this.upgraded = false;
    }

    /**
     * Authenticate.
     *
     * @param response the HTTP servlet response.
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return webApplication.getSecurityManager().authenticate(this, response);
    }

    /**
     * Change the session id.
     *
     * @return the changed session id.
     */
    @Override
    public String changeSessionId() {
        return webApplication.getHttpSessionManager().changeSessionId(this);
    }

    /**
     * Get the async context.
     *
     * @return the async context.
     */
    @Override
    public AsyncContext getAsyncContext() {
        if (asyncContext == null) {
            throw new IllegalStateException("Async was not started");
        }
        return asyncContext;
    }

    /**
     * Get the attribute.
     *
     * @param name the attribute name.
     * @return the value.
     */
    @Override
    public Object getAttribute(String name) {
        return attributeManager.getAttribute(name);
    }

    /**
     * Get attribute names.
     *
     * @return the attribute names.
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return attributeManager.getAttributeNames();
    }

    /**
     * Get the auth type.
     *
     * @return the auth type.
     */
    @Override
    public String getAuthType() {
        return authType;
    }

    /**
     * Get the character encoding.
     *
     * @return the character encoding.
     */
    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * Get the content length.
     *
     * @return the content length.
     */
    @Override
    public int getContentLength() {
        return (int) contentLength;
    }

    /**
     * Get the content length.
     *
     * @return the content length.
     */
    @Override
    public long getContentLengthLong() {
        return contentLength;
    }

    /**
     * Get the content type.
     *
     * @return the content type.
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /**
     * Get the context path.
     *
     * @return the context path.
     */
    @Override
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Get the cookies.
     *
     * @return the cookies.
     */
    @Override
    public Cookie[] getCookies() {
        return cookies;
    }

    /**
     * Get the date header.
     *
     * @param name the header name.
     * @return the date header.
     */
    @Override
    public long getDateHeader(String name) {
        return headerManager.getDateHeader(name);
    }

    /**
     * Get the dispatcher type.
     *
     * @return the dispatcher type.
     */
    @Override
    public DispatcherType getDispatcherType() {
        return dispatcherType;
    }

    /**
     * Get the header.
     *
     * @param name the header name.
     * @return the header value.
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
     * Get the headers.
     *
     * @param name the header name.
     * @return the header values.
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        return headerManager.getHeaders(name);
    }

    /**
     * Get the input stream.
     *
     * @return the input stream.
     * @throws IOException when a serious I/O error occurs.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return inputStream;
    }

    /**
     * Get the int header.
     *
     * @param name the name.
     * @return the int header.
     */
    @Override
    public int getIntHeader(String name) {
        return headerManager.getIntHeader(name);
    }

    /**
     * Get the local address.
     *
     * @return the local address.
     */
    @Override
    public String getLocalAddr() {
        return localAddress;
    }

    /**
     * Get the local name.
     *
     * @return the local name.
     */
    @Override
    public String getLocalName() {
        return localName;
    }

    /**
     * Get the local port.
     *
     * @return the local port.
     */
    @Override
    public int getLocalPort() {
        return localPort;
    }

    /**
     * Get locale.
     *
     * @return the locale.
     */
    @Override
    public Locale getLocale() {
        Locale result = Locale.getDefault();
        Enumeration<String> languages = getHeaders("Accept-Language");
        if (languages != null) {
            String localeString = languages.nextElement();
            String[] localeStrings = localeString.split(",");
            result = new Locale(localeStrings[0]);
        }
        return result;
    }

    /**
     * Get the locales.
     *
     * @return the locales.
     */
    @Override
    public Enumeration<Locale> getLocales() {
        ArrayList<Locale> locales = new ArrayList<>();
        Enumeration<String> languages = getHeaders("Accept-Language");
        if (languages != null) {
            String localeString = languages.nextElement();
            String[] localeStrings = localeString.split(",");
            for (String localeString1 : localeStrings) {
                locales.add(new Locale(localeString1.trim()));
            }
        } else {
            locales = new ArrayList<>();
            locales.add(Locale.getDefault());
        }
        return Collections.enumeration(locales);
    }

    /**
     * Get the request method.
     *
     * @return the request method.
     */
    @Override
    public String getMethod() {
        return method;
    }

    /**
     * Get the parameter.
     *
     * @param name the name.
     * @return the value.
     */
    @Override
    public String getParameter(String name) {
        String result = null;
        getParametersFromRequest();
        if (getParameterValues(name) != null) {
            result = getParameterValues(name)[0];
        }
        return result;
    }

    /**
     * Get the parameter map.
     *
     * @return the parameter map.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        getParametersFromRequest();
        return Collections.unmodifiableMap(parameters);
    }

    /**
     * Get the parameter names.
     *
     * @return the parameter names.
     */
    @Override
    public Enumeration<String> getParameterNames() {
        getParametersFromRequest();
        return Collections.enumeration(parameters.keySet());
    }

    /**
     * Get the parameter values.
     *
     * @param name
     * @return the parameter values.
     */
    @Override
    public String[] getParameterValues(String name) {
        getParametersFromRequest();
        return parameters.get(name);
    }

    /**
     * Get the parameters from the request.
     */
    protected void getParametersFromRequest() {
        if (!parametersParsed) {
            parametersParsed = true;
            try {
                if (queryString != null) {
                    for (String param : queryString.split("&")) {
                        String pair[] = param.split("=");
                        String key = URLDecoder.decode(pair[0], "UTF-8");
                        String value = "";
                        if (pair.length > 1) {
                            value = URLDecoder.decode(pair[1], "UTF-8");
                        }
                        String[] values = parameters.get(key);
                        if (values == null) {
                            values = new String[]{value};
                            parameters.put(key, values);
                        } else {
                            String[] newValues = new String[values.length + 1];
                            System.arraycopy(values, 0, newValues, 0, values.length);
                            newValues[values.length] = value;
                            parameters.put(key, newValues);
                        }
                    }
                }
                if (contentType != null && contentType.equals("application/x-www-form-urlencoded")) {
                    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                    InputStream inputStream = getInputStream();
                    int read = inputStream.read();
                    int index = 0;
                    while (read != -1) {
                        byteOutput.write(read);
                        read = inputStream.read();
                        index++;
                        if (index + 1 == getContentLength()) {
                            break;
                        }
                    }
                    if (read != -1) {
                        byteOutput.write(read);
                    }
                    String parameterString = new String(byteOutput.toByteArray());
                    String[] pairs = parameterString.trim().split("&");
                    if (pairs != null) {
                        for (int i = 0; i < pairs.length; i++) {
                            String[] pair = pairs[i].trim().split("=");
                            if (pair.length == 2) {
                                pair[0] = URLDecoder.decode(pair[0], "UTF-8");
                                pair[1] = URLDecoder.decode(pair[1], "UTF-8");
                                setParameter(pair[0], new String[]{pair[1]});
                            } else {
                                pair[0] = URLDecoder.decode(pair[0], "UTF-8");
                                if (!"".equals(pair[0])) {
                                    setParameter(pair[0], new String[]{""});
                                }
                            }
                        }
                    }
                }
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    /**
     * Get the part.
     *
     * @param name the name.
     * @return the part, or null.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public Part getPart(String name) throws IOException, ServletException {
        verifyMultipartFormData();
        return parts.get(name);
    }

    /**
     * Get the parts.
     *
     * @return the parts.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        verifyMultipartFormData();
        return parts.values();
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
     * Get the translated path.
     *
     * @return the translated path.
     */
    @Override
    public String getPathTranslated() {
        return null;
    }

    /**
     * Get the protocol.
     *
     * @return the protocol.
     */
    @Override
    public String getProtocol() {
        return protocol;
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
     * Get the reader.
     *
     * @return the reader.
     * @throws IOException when a serious I/O error occurs.
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    /**
     * Get the real path.
     *
     * @param path the path.
     * @return the real path.
     * @deprecated
     */
    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the remote address.
     *
     * @return the remote address.
     */
    @Override
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * Get the remote host.
     *
     * @return the remote host.
     */
    @Override
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * Get the remote port.
     *
     * @return the remote port.
     */
    @Override
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * Get the remote user.
     *
     * @return the remote user.
     */
    @Override
    public String getRemoteUser() {
        String result = null;
        if (getUserPrincipal() != null) {
            result = getUserPrincipal().getName();
        }
        return result;
    }

    /**
     * Get the request dispatcher.
     *
     * @param path the path.
     * @return the request dispatcher.
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return webApplication.getRequestDispatcher(path);
    }

    /**
     * Get the request URI.
     *
     * @return the request URI.
     */
    @Override
    public String getRequestURI() {
        String result = contextPath + servletPath;
        if (pathInfo != null) {
            result = contextPath + servletPath + pathInfo;
        }
        return result;
    }

    /**
     * Get the request URL.
     *
     * @return the request URL.
     */
    @Override
    public StringBuffer getRequestURL() {
        StringBuffer result = new StringBuffer();
        result.append(getScheme());
        result.append("://");
        result.append(getServerName());
        result.append(":");
        result.append(getServerPort());
        result.append(getContextPath());
        result.append(getServletPath());
        if (getPathInfo() != null) {
            result.append(getPathInfo());
        }
        return result;
    }

    /**
     * Get the requested session id.
     *
     * @return the requested session id.
     */
    @Override
    public String getRequestedSessionId() {
        return requestedSessionId;
    }

    /**
     * Get the scheme.
     *
     * @return the scheme.
     */
    @Override
    public String getScheme() {
        return scheme;
    }

    /**
     * Get the server name.
     *
     * @return the server name.
     */
    @Override
    public String getServerName() {
        return serverName;
    }

    /**
     * Get the server port.
     *
     * @return the server port.
     */
    @Override
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Get the servlet context.
     *
     * @return the servlet context.
     */
    @Override
    public ServletContext getServletContext() {
        return webApplication;
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
     * Get the session.
     *
     * @return the session.
     */
    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    /**
     * Get the session.
     *
     * @param create to create it or not.
     * @return the session.
     */
    @Override
    public HttpSession getSession(boolean create) {
        HttpSession result = null;
        HttpSessionManager manager = webApplication.getHttpSessionManager();
        if (currentSessionId == null && requestedSessionId != null) {
            currentSessionId = requestedSessionId;
        }
        if (manager.hasSession(currentSessionId)) {
            result = manager.getSession(webApplication, this, currentSessionId);
        } else if (create) {
            result = manager.createSession(webApplication, this);
            currentSessionId = result.getId();
        }
        return result;
    }

    /**
     * Get the upgrade handler.
     *
     * @return the upgrade handler.
     */
    public HttpUpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }

    /**
     * Get the user principal.
     *
     * @return the user principal.
     */
    @Override
    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    /**
     * Is async started.
     *
     * @return is async started.
     */
    @Override
    public boolean isAsyncStarted() {
        return asyncStarted;
    }

    /**
     * Is async supported.
     *
     * @return true if is async is supported.
     */
    @Override
    public boolean isAsyncSupported() {
        return asyncSupported;
    }

    /**
     * Is the requested session id from cookie.
     *
     * @return if the requested session id from cookie.
     */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return requestedSessionIdFromCookie;
    }

    /**
     * Is the requested session id from the request url.
     *
     * @return if the requested session id from the request url.
     */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return requestedSessionIdFromURL;
    }

    /**
     * Is the requested session id from the request url.
     *
     * @return if the requested session id from the request url.
     * @deprecated
     */
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }

    /**
     * Is the requested session id valid?
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isRequestedSessionIdValid() {
        boolean result = false;
        if (requestedSessionId != null) {
            HttpSessionManager manager = webApplication.getHttpSessionManager();
            result = manager.hasSession(requestedSessionId);
        }
        return result;
    }

    /**
     * Is the request secure.
     *
     * @return is the request secure.
     */
    @Override
    public boolean isSecure() {
        boolean result = false;
        if ("https".equals(scheme)) {
            result = true;
        }
        return result;
    }

    /**
     * Is the request upgraded.
     *
     * @return false
     */
    public boolean isUpgraded() {
        return upgraded;
    }

    /**
     * Is the user in the specified role.
     *
     * @param role the role.
     * @return if the user in the specified role.
     */
    @Override
    public boolean isUserInRole(String role) {
        return webApplication.getSecurityManager().isUserInRole(this, role);
    }

    /**
     * Login.
     *
     * @param username the username.
     * @param password the password.
     * @throws ServletException when a serious error occurs.
     */
    @Override
    public void login(String username, String password) throws ServletException {
        webApplication.getSecurityManager().login(this, username, password);
    }

    /**
     * Logout.
     *
     * @throws ServletException when a serious error occurs.
     */
    @Override
    public void logout() throws ServletException {
        webApplication.getSecurityManager().logout(this);
    }

    /**
     * Remove the attribute.
     *
     * @param name the name.
     */
    @Override
    public void removeAttribute(String name) {
        attributeManager.removeAttribute(name);
    }

    /**
     * Set the async supported flag.
     *
     * @param asyncSupported the async supported flag.
     */
    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    /**
     * Set the attribute.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setAttribute(String name, Object value) {
        attributeManager.setAttribute(name, value);
    }

    /**
     * Set the auth type.
     *
     * @param authType the auth type.
     */
    public void setAuthType(String authType) {
        this.authType = authType;
    }

    /**
     * Set the character encoding.
     *
     * @param characterEncoding the character encoding.
     * @throws UnsupportedEncodingException
     */
    @Override
    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException {
        this.characterEncoding = characterEncoding;
    }

    /**
     * Set the content length.
     *
     * @param contentLength the content length.
     */
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * Set the content type.
     *
     * @param contentType the content type.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * Set the cookies.
     *
     * @param cookies the cookies.
     */
    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
        if (this.cookies != null && this.cookies.length == 0) {
            this.cookies = null;
        }
    }

    /**
     * Set the header.
     *
     * @param name the name.
     * @param value the value (string).
     */
    public void setHeader(String name, String value) {
        headerManager.setHeader(name, value);
    }

    /**
     * Set the input stream.
     *
     * @param inputStream the input stream.
     */
    public void setInputStream(ServletInputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Set the local address.
     *
     * @param localAddress the local address.
     */
    public void setLocalAddr(String localAddress) {
        this.localAddress = localAddress;
    }

    /**
     * Set the local name.
     *
     * @param localName the local name.
     */
    public void setLocalName(String localName) {
        this.localName = localName;
    }

    /**
     * Set the local port.
     *
     * @param localPort the local port.
     */
    public void setLocalPort(int localPort) {
        this.localPort = localPort;
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
     * Set the parameter values.
     *
     * @param name the parameter name.
     * @param values the values.
     */
    public void setParameter(String name, String[] values) {
        parameters.put(name, values);
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
     * Set the protocol.
     *
     * @param protocol the protocol.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
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
     * Set the remote address.
     *
     * @param remoteAddr the remote address.
     */
    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    /**
     * Set the remote host.
     *
     * @param remoteHost the remote host.
     */
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    /**
     * Set the remote port.
     *
     * @param remotePort the remote port.
     */
    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    /**
     * Set the requested session id.
     *
     * @param requestedSessionId the requested session id.
     */
    public void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    /**
     * Set the requested session id from cookie.
     *
     * @param requestedSessionIdFromCookie the requested session id from cookie.
     */
    public void setRequestedSessionIdFromCookie(boolean requestedSessionIdFromCookie) {
        this.requestedSessionIdFromCookie = requestedSessionIdFromCookie;
    }

    /**
     * Set the request session id from URL flag.
     *
     * @param requestedSessionIdFromURL the requested session if from URL flag.
     */
    public void setRequestedSessionIdFromURL(boolean requestedSessionIdFromURL) {
        this.requestedSessionIdFromURL = requestedSessionIdFromURL;
    }

    /**
     * Set the scheme.
     *
     * @param scheme the scheme.
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * Set the server name.
     *
     * @param serverName the server name.
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Set the server port.
     *
     * @param serverPort the server port.
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Set the servlet path.
     *
     * @param servletPath the servlet path.
     */
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    /**
     * Set the upgraded flag.
     *
     * @param upgraded the upgraded flag.
     */
    public void setUpgraded(boolean upgraded) {
        this.upgraded = upgraded;
    }

    /**
     * Set the user principal.
     *
     * @param userPrincipal the user principal.
     */
    public void setUserPrincipal(Principal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    /**
     * Start async.
     *
     * @return the async context.
     * @throws IllegalStateException
     */
    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        if (!isAsyncSupported()) {
            throw new IllegalStateException("Async is not supported");
        }
        asyncContext = new DefaultAsyncContext(this, this.webApplication.getResponse(this));
        asyncStarted = true;
        return asyncContext;
    }

    /**
     * Start async.
     *
     * @param request the request.
     * @param response the response.
     * @return the async context.
     * @throws IllegalStateException
     */
    @Override
    public AsyncContext startAsync(ServletRequest request, ServletResponse response) throws IllegalStateException {
        if (!isAsyncSupported()) {
            throw new IllegalStateException("Async is not supported");
        }
        asyncContext = new DefaultAsyncContext(request, response);
        asyncStarted = true;
        return asyncContext;
    }

    /**
     * Upgrade the request.
     *
     * @param <T> the type.
     * @param handlerClass the handler class.
     * @return the upgrade handler.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a serious error occurs.
     */
    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        try {
            upgradeHandler = handlerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ie) {
            throw new ServletException(ie);
        }
        setUpgraded(true);
        return (T) upgradeHandler;
    }

    /**
     * Verify the method is of type "multipart/form-data"
     *
     * @throws ServletException the exception thrown when it is not.
     */
    protected void verifyMultipartFormData() throws ServletException {
        if (contentType != null && !contentType.equals("multipart/form-data")) {
            throw new ServletException("Request not of type multipart/form-data");
        }
    }
}
