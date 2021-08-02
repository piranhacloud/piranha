/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.webapp.impl;

import static cloud.piranha.webapp.impl.DefaultServletRequestDispatcher.PREVIOUS_REQUEST;
import static jakarta.servlet.DispatcherType.INCLUDE;
import static jakarta.servlet.RequestDispatcher.INCLUDE_QUERY_STRING;
import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cloud.piranha.webapp.api.AttributeManager;
import cloud.piranha.webapp.api.HttpHeaderManager;
import cloud.piranha.webapp.api.HttpSessionManager;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationRequest;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ReadListener;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestWrapper;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;

/**
 * The default WebApplicationRequest.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationRequest extends ServletInputStream implements WebApplicationRequest {

    /**
     * Defines the 'multipart/form-data' constant.
     */
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    
    /**
     * Defines the 'UTF-8' charset constant.
     */
    private static final String UTF_8 = "UTF-8";
    
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
     * Stores the gotInputStream flag.
     */
    protected boolean gotInputStream;

    /**
     * Stores the gotReader flag.
     */
    protected boolean gotReader;

    /**
     * Stores the header manager.
     */
    protected HttpHeaderManager headerManager;

    /**
     * Stores the httpServletMapping.
     */
    protected HttpServletMapping httpServletMapping;
    
    /**
     * Stores the input stream.
     */
    protected InputStream inputStream;

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
     * Stores the multipartConfig.
     */
    protected MultipartConfigElement multipartConfig;

    /**
     * Stores the parameters.
     */
    protected HashMap<String, String[]> parameters;

    /**
     * Stores the parameters parsed flag.
     */
    protected boolean parametersParsed;

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
     * Stores the reader.
     */
    protected BufferedReader reader;

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
     * Stores the original servlet path.
     */
    protected String originalServletPath;

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
     * Stores the finished flag.
     */
    private boolean finished;

    /**
     * The number of items read from the input stream
     */
    private int index;

    /**
     * Stores the read listener.
     */
    private ReadListener readListener;

    /**
     * Constructor.
     */
    public DefaultWebApplicationRequest() {
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
        this.inputStream = new ByteArrayInputStream(new byte[0]);
        this.method = "GET";
        this.protocol = "HTTP/1.1";
        this.scheme = "http";
        this.serverName = "localhost";
        this.serverPort = 80;
        this.servletPath = "";
        this.parameters = new HashMap<>();
        this.upgraded = false;
    }

    /**
     * Authenticate.
     *
     * @param response the HTTP servlet response.
     * @return true if authenticated, false otherwise.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
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
        currentSessionId = webApplication.getHttpSessionManager().changeSessionId(this);
        return currentSessionId;
    }

    /**
     * {@return the async context}
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
     * {@return the auth type}
     */
    @Override
    public String getAuthType() {
        return authType;
    }

    /**
     * {@return the character encoding}
     */
    @Override
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * {@return the content length}
     */
    @Override
    public int getContentLength() {
        return (int) contentLength;
    }

    /**
     * {@return the content length}
     */
    @Override
    public long getContentLengthLong() {
        return contentLength;
    }

    /**
     * {@return the content type}
     */
    @Override
    public String getContentType() {
        return contentType;
    }
   
    @Override
    public String getContextPath() {
        return contextPath;
    }

    /**
     * {@return the cookies}
     */
    @Override
    public Cookie[] getCookies() {
        Cookie[] result = null;
        if (cookies != null) {
            result = new Cookie[cookies.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = (Cookie) cookies[i].clone();
            }
        }
        return result;
    }

    /**
     * {@return the date header}
     * @param name the header name.
     */
    @Override
    public long getDateHeader(String name) {
        return headerManager.getDateHeader(name);
    }

    /**
     * {@return the dispatcher type}
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
     * {@return the header names}
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
    
    @Override
    public HttpServletMapping getHttpServletMapping() {
        return httpServletMapping;
    }

    /**
     * @param httpServletMapping the httpServletMapping to set
     */
    public void setHttpServletMapping(HttpServletMapping httpServletMapping) {
        this.httpServletMapping = httpServletMapping;
    }

    /**
     * Get the input stream.
     *
     * @return the input stream.
     * @throws IOException when a serious I/O error occurs.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream result;
        if (!gotReader) {
            gotInputStream = true;
            result = this;
        } else {
            throw new IllegalStateException(
                    "Cannot getInputStream because getReader has been previously called");
        }
        return result;
    }

    /**
     * {@return the int header}
     * @param name the name.
     */
    @Override
    public int getIntHeader(String name) {
        return headerManager.getIntHeader(name);
    }

    /**
     * {@return the local address}
     */
    @Override
    public String getLocalAddr() {
        return localAddress;
    }

    /**
     * {@return the local name}
     */
    @Override
    public String getLocalName() {
        return localName;
    }

    /**
     * {@return the local port}
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
        if (languages.hasMoreElements()) {
            String localeString = languages.nextElement();
            String[] localeStrings = localeString.split(",");
            if (localeStrings[0].contains("-")) {
                String[] localeString1 = localeStrings[0].split("-");
                result = new Locale(localeString1[0].trim(), localeString1[1].trim());
            } else {
                result = new Locale(localeStrings[0].trim());
            }
        }
        return result;
    }

    /**
     * {@return the locales}
     */
    @Override
    public Enumeration<Locale> getLocales() {
        ArrayList<Locale> locales = new ArrayList<>();
        Enumeration<String> languages = getHeaders("Accept-Language");
        if (languages.hasMoreElements()) {
            String localeString = languages.nextElement();
            String[] localeStrings = localeString.split(",");
            for (String localeString1 : localeStrings) {
                if (localeString1.contains("-")) {
                    String[] localeString2 = localeString1.split("-");
                    locales.add(new Locale(localeString2[0].trim(), localeString2[1].trim()));
                } else {
                    locales.add(new Locale(localeString1.trim()));
                }
            }
        } else {
            locales = new ArrayList<>();
            locales.add(Locale.getDefault());
        }
        return Collections.enumeration(locales);
    }

    @Override
    public String getMethod() {
        return method;
    }
    
    @Override
    public MultipartConfigElement getMultipartConfig() {
        return multipartConfig;
    }

    /**
     * @param multipartConfig the multipartConfig to set
     */
    public void setMultipartConfig(MultipartConfigElement multipartConfig) {
        this.multipartConfig = multipartConfig;
    }
   
    @Override
    public String getParameter(String name) {
        String result = null;
        getParametersFromRequest();
        if (getParameterValues(name) != null) {
            result = getParameterValues(name)[0];
        }
        return result;
    }
    
    @Override
    public Map<String, String[]> getParameterMap() {
        getParametersFromRequest();
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        getParametersFromRequest();
        return Collections.enumeration(parameters.keySet());
    }

    /**
     * {@return the parameter values}
     * @param name the parameter name.
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
                String mergedQueryString = mergeQueryFromAttributes();
                if (mergedQueryString != null) {
                    for (String param : mergedQueryString.split("&")) {
                        String pair[] = param.split("=");
                        String key = URLDecoder.decode(pair[0], UTF_8);
                        String value = "";
                        if (pair.length > 1) {
                            value = URLDecoder.decode(pair[1], UTF_8);
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
                
                boolean hasMultiPart
                        = // FORM/Multipart submission
                        contentType != null && contentType.startsWith(MULTIPART_FORM_DATA);
                
                if (hasMultiPart) {
                    for (Part part : getParts()) {
                        if (part.getSubmittedFileName() == null) {
                            setParameter(part.getName(), new String[]{new String(part.getInputStream().readAllBytes())});
                        }
                    }
                } else {

                boolean hasBody
                        = // FORM submission
                        contentType != null && contentType.startsWith("application/x-www-form-urlencoded")
                        || // PUT parameters
                        "put".equalsIgnoreCase(getMethod()) && getContentLength() > 0;

                    if (hasBody) {
                        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                        int read = read();
                        while (read != -1) {
                            byteOutput.write(read);
                            read = read();
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
                                    pair[0] = URLDecoder.decode(pair[0], UTF_8);
                                    pair[1] = URLDecoder.decode(pair[1], UTF_8);
                                    setParameter(pair[0], new String[]{pair[1]});
                                } else {
                                    pair[0] = URLDecoder.decode(pair[0], UTF_8);
                                    if (!"".equals(pair[0])) {
                                        setParameter(pair[0], new String[]{""});
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException | ServletException ioe) {
                throw new RuntimeException(ioe);
            }
        }
    }

    /**
     * Merge query string from this request and from the attribute
     * {@link RequestDispatcher.INCLUDE_QUERY_STRING} if the dispatcher type is {@link DispatcherType.INCLUDE}
     * @return the query string merged
     */
    private String mergeQueryFromAttributes() {
        String queryStringFromAttribute = dispatcherType == INCLUDE ? (String) getAttribute(INCLUDE_QUERY_STRING) : null;
        if (queryStringFromAttribute == null) {
            return queryString;
        }

        if (queryString == null) {
            return queryStringFromAttribute;
        }

        return queryStringFromAttribute + "&" + queryString;
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
        return webApplication.getMultiPartManager().getPart(webApplication, this, name);
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
        return webApplication.getMultiPartManager().getParts(webApplication, this);
    }

    /**
     * {@return the path info}
     */
    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    /**
     * {@return the translated path}
     */
    @Override
    public String getPathTranslated() {
        return null;
    }

    /**
     * {@return the protocol}
     */
    @Override
    public String getProtocol() {
        return protocol;
    }

    /**
     * {@return the query string}
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
        if (!gotInputStream) {
            if (reader == null) {

                String charsetName = characterEncoding == null ? StandardCharsets.ISO_8859_1.toString() : characterEncoding;
                if (!isSupported(charsetName)) {
                    throw new UnsupportedEncodingException(charsetName);
                }

                reader = new BufferedReader(new InputStreamReader(this, charsetName));
            }
            gotReader = true;
        } else {
            throw new IllegalStateException("Cannot getReader because getInputStream has been previously called");
        }
        return reader;
    }

    private boolean isSupported(String csn) {
        try {
            return Charset.isSupported(csn);
        } catch (IllegalCharsetNameException x) {
            return false;
        }
    }


    /**
     * {@return the real path}
     * @param path the path.
     * @deprecated
     */
    @Deprecated
    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException("HttpServletRequest.getRealPath is no longer supported");
    }

    /**
     * {@return the remote address}
     */
    @Override
    public String getRemoteAddr() {
        return remoteAddr;
    }

    /**
     * {@return the remote host}
     */
    @Override
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * {@return the remote port}
     */
    @Override
    public int getRemotePort() {
        return remotePort;
    }

    /**
     * {@return the remote user}
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
     * {@return the request dispatcher}
     * @param path the path.
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        Path rootContext = Paths.get(getContextPath());
        Path resolved = rootContext.resolveSibling(Paths.get(path)).normalize();
        if (!resolved.startsWith(rootContext))
            resolved = rootContext.resolveSibling(resolved);
        return webApplication.getRequestDispatcher(resolved.toString());
    }

    /**
     * {@return the request URI}
     */
    @Override
    public String getRequestURI() {
        return addOrRemoveSlashIfNeeded(
            contextPath +
            coalesce(originalServletPath, servletPath) + 
            coalesce(pathInfo, ""));
    }

    /**
     * {@return the request URL}
     */
    @Override
    public StringBuffer getRequestURL() {
        StringBuffer result = new StringBuffer();
        result.append(getScheme());
        result.append("://");
        result.append(getServerName());
        result.append(":");
        result.append(getServerPort());
        result.append(getRequestURI());
        
        return result;
    }

    /**
     * {@return the requested session id}
     */
    @Override
    public String getRequestedSessionId() {
        return requestedSessionId;
    }

    /**
     * {@return the scheme}
     */
    @Override
    public String getScheme() {
        return scheme;
    }

    /**
     * {@return the server name}
     */
    @Override
    public String getServerName() {
        return serverName;
    }

    /**
     * {@return the server port}
     */
    @Override
    public int getServerPort() {
        return serverPort;
    }

    /**
     * {@return the servlet context}
     */
    @Override
    public WebApplication getServletContext() {
        return webApplication;
    }

    /**
     * {@return the servlet path}
     */
    @Override
    public String getServletPath() {
        return servletPath;
    }

    /**
     * {@return the session}
     */
    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        HttpSession session = null;
        HttpSessionManager manager = webApplication.getHttpSessionManager();
        if (currentSessionId == null && requestedSessionId != null) {
            currentSessionId = requestedSessionId;
        }

        if (manager.hasSession(currentSessionId)) {
            session = manager.getSession(this, currentSessionId);
        } else if (create) {
            session = manager.createSession(this);
            currentSessionId = session.getId();
        }

        return session;
    }

    /**
     * {@return the upgrade handler}
     */
    public HttpUpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }

    /**
     * {@return the user principal}
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
    @Deprecated
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
        return "https".equals(scheme);
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
        webApplication.getSecurityManager().logout(this, (HttpServletResponse) this.webApplication.getResponse(this));
    }

    /**
     * Remove the attribute.
     *
     * @param name the name.
     */
    @Override
    public void removeAttribute(String name) {
        Object oldValue = attributeManager.getAttribute(name);
        attributeManager.removeAttribute(name);
        if (webApplication != null && webApplication.getHttpRequestManager() != null) {
            webApplication.getHttpRequestManager().attributeRemoved(this, name, oldValue);
        }
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
        if (value != null) {
            boolean added = true;
            Object oldValue = attributeManager.getAttribute(name);
            if (oldValue == null) {
                added = false;
            }

            attributeManager.setAttribute(name, value);
            if (webApplication != null && webApplication.getHttpRequestManager() != null) {
                if (!added) {
                    webApplication.getHttpRequestManager().attributeAdded(this, name, value);
                } else {
                    webApplication.getHttpRequestManager().attributeReplaced(this, name, oldValue);
                }
            }
        } else {
            Object oldValue = attributeManager.getAttribute(name);
            attributeManager.removeAttribute(name);
            if (webApplication != null && webApplication.getHttpRequestManager() != null) {
                webApplication.getHttpRequestManager().attributeRemoved(this, name, oldValue);
            }
        }
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
     * @throws UnsupportedEncodingException when the encoding is not supported.
     */
    @Override
    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException {
        boolean supported = false;
        try {
            supported = Charset.isSupported(characterEncoding);
        } catch (IllegalArgumentException e) {
        }
        if (!supported) {
            throw new UnsupportedEncodingException("Character encoding '" + characterEncoding + "' is not supported");
        }
        if (!gotReader) {
            this.characterEncoding = characterEncoding;
        }
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
        
        if (contentType.startsWith(MULTIPART_FORM_DATA)) {
            // "multipart/form-data" contains a boundary and no charset
            return;
        }
        
        String[] parts = contentType.split(";");
        if (parts.length == 1) {
            return;
        }
        
        String charset = parts[1].trim();
        String[] pair = charset.split("=");
        if (pair.length == 1) {
            return;
        }
        characterEncoding = pair[1].trim();
    }

    @Override
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * Set the cookies.
     *
     * @param cookies the cookies.
     */
    public void setCookies(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            this.cookies = null;
        } else {
            this.cookies = new Cookie[cookies.length];
            for (int i = 0; i < cookies.length; i++) {
                this.cookies[i] = (Cookie) cookies[i].clone();
            }
        }
    }

    /**
     * Set the dispatcher type.
     *
     * @param dispatcherType the dispatcher type.
     */
    @Override
    public void setDispatcherType(DispatcherType dispatcherType) {
        this.dispatcherType = dispatcherType;
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
    public void setInputStream(InputStream inputStream) {
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

    @Override
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }
    
    /**
     * Gets the original Servlet Path
     *
     * @return the original Servlet Path
     *
     */
    public String getOriginalServletPath() {
        return originalServletPath;
    }

    /**
     * Set the original Servlet Path
     *
     * @param originalServletPath the original Servlet Path
     */
    public void setOriginalServletPath(String originalServletPath) {
        this.originalServletPath = originalServletPath;
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

    @Override
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    /**
     * Start async.
     *
     * @return the async context.
     * @throws IllegalStateException when async is not supported.
     */
    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        if (!isAsyncSupported()) {
            throw new IllegalStateException("Async is not supported");
        }

        return startAsync(this, this.webApplication.getResponse(this));
    }

    /**
     * Unwrap the request.
     *
     * @param <T> the type to unwrap to.
     * @param request the request.
     * @param type the class type of the result
     * @return the unwrapped request.
     */
    public static <T> T unwrap(ServletRequest request, Class<T> type) {
        ServletRequest currentRequest = request;
        while (currentRequest instanceof ServletRequestWrapper wrapper) {
            currentRequest = wrapper.getRequest();
        }
        return type.cast(currentRequest);
    }

    /**
     * Set the async started flag.
     * 
     * @param asyncStarted the async started flag.
     */
    public void setAsyncStarted(boolean asyncStarted) {
        this.asyncStarted = asyncStarted;
    }

    /**
     * Start async.
     *
     * @param request the request.
     * @param response the response.
     * @return the async context.
     * @throws IllegalStateException when async is not supported.
     */
    @Override
    public AsyncContext startAsync(ServletRequest request, ServletResponse response) throws IllegalStateException {
        requireNonNull(request); requireNonNull(response);

        if (!isAsyncSupported()) {
            throw new IllegalStateException("Async is not supported");
        }

        if (request.getAttribute("CALLED_FROM_ASYNC_WRAPPER") != null) {
            return new DefaultAsyncContext(request, response);
        }

        if (asyncContext != null) {
            throw new IllegalStateException("Async cycle has already been started");
        }

        asyncContext = new DefaultAsyncContext(request, response);
        asyncStarted = true;

        Object previousAttribute = request.getAttribute(PREVIOUS_REQUEST);
        while (previousAttribute instanceof HttpServletRequest) {
            HttpServletRequest previousRequest = unwrap((HttpServletRequest) previousAttribute, HttpServletRequest.class);

            if (previousRequest instanceof DefaultWebApplicationRequest defaultRequest) {
                defaultRequest.setAsyncStarted(true);
            }

            previousAttribute = previousRequest.getAttribute(PREVIOUS_REQUEST);
        }


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
    @SuppressWarnings("unchecked")
    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        try {
            upgradeHandler = handlerClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ie) {
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
        if (contentType != null && !contentType.startsWith(MULTIPART_FORM_DATA)) {
            throw new ServletException("Request not of type multipart/form-data");
        }
    }

    // -------------------------------------------------------------------------
    //  ServletInputStream methods
    // -------------------------------------------------------------------------
    /**
     * Is the Servlet input stream finished?
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * Is the Servlet input stream ready?
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * Set the read listener.
     *
     * @param listener the read listener.
     */
    @Override
    public void setReadListener(ReadListener listener) {
        if (listener == null) {
            throw new NullPointerException("Read listener cannot be null");
        }
        if (this.readListener != null) {
            throw new IllegalStateException("Read listener can only be set once");
        }
        if (!isAsyncStarted() && !isUpgraded()) {
            throw new IllegalStateException("Read listener cannot be set as the request is not upgraded nor the async is started");
        }
        this.readListener = listener;
    }

    /**
     * Read from the Servlet input stream.
     *
     * @return the read value.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        if (finished || getContentLength() == 0) {
            return -1;
        }

        int read = inputStream.read();
        index++;
        if (index == getContentLength() || read == -1) {
            finished = true;
        }

        return read;
    }

    @Override
    public String toString() {
        return getRequestURIWithQueryString() + " " + super.toString();
    }

    /**
     * {@return the request URI with query string}
     */
    public String getRequestURIWithQueryString() {
        String requestURI = getRequestURI();
        String queryString = getQueryString();
        return queryString == null ? requestURI : requestURI + "?" + queryString;
    }
    
    @SafeVarargs
    private <T> T coalesce(T... objects) {
        for (T object : objects) {
            if (object != null) {
                return object;
            }
        }

        return null;
    }
    
    private String addOrRemoveSlashIfNeeded(String string) {
        if (string.startsWith("/")) {
            if (string.startsWith("//")) {
                return string.substring(1);
            }
            
            return string;
        }

        return "/" + string;
    }
}
