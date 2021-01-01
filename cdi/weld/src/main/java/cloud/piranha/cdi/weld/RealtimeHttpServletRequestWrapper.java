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
package cloud.piranha.cdi.weld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;

/**
 * An HttpServletRequest wrapper that consistently consults the getWrapped() method for every operation.
 *
 * @author Arjan Tijms
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class RealtimeHttpServletRequestWrapper implements HttpServletRequest {

    /**
     * Get the async context.
     *
     * @return the async context.
     */
    @Override
    public AsyncContext getAsyncContext() {
        return getWrapped().getAsyncContext();
    }

    /**
     * Get the attribute.
     *
     * @param name the name.
     * @return the value.
     */
    @Override
    public Object getAttribute(String name) {
        return getWrapped().getAttribute(name);
    }

    /**
     * Get the attribute names.
     *
     * @return the attribute names.
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return getWrapped().getAttributeNames();
    }

    /**
     * Get the character encoding.
     *
     * @return the character encoding.
     */
    @Override
    public String getCharacterEncoding() {
        return getWrapped().getCharacterEncoding();
    }

    /**
     * Get the content length.
     *
     * @return the content length.
     */
    @Override
    public int getContentLength() {
        return getWrapped().getContentLength();
    }

    /**
     * Get the content length.
     *
     * @return the content length.
     */
    @Override
    public long getContentLengthLong() {
        return getWrapped().getContentLengthLong();
    }

    /**
     * Get the content type.
     *
     * @return the content type.
     */
    @Override
    public String getContentType() {
        return getWrapped().getContentType();
    }

    /**
     * Get the dispatcher type.
     */
    @Override
    public DispatcherType getDispatcherType() {
        return getWrapped().getDispatcherType();
    }

    /**
     * Get the input stream.
     *
     * @return the input stream.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return getWrapped().getInputStream();
    }

    /**
     * Get the local address.
     *
     * @return the local address.
     */
    @Override
    public String getLocalAddr() {
        return getWrapped().getLocalAddr();
    }

    /**
     * Get the local name.
     *
     * @return the local name.
     */
    @Override
    public String getLocalName() {
        return getWrapped().getLocalName();
    }

    /**
     * Get the local port.
     *
     * @return the local port.
     */
    @Override
    public int getLocalPort() {
        return getWrapped().getLocalPort();
    }

    /**
     * Get the locale.
     *
     * @return the locale.
     */
    @Override
    public Locale getLocale() {
        return getWrapped().getLocale();
    }

    /**
     * Get the locales.
     *
     * @return the locales.
     */
    @Override
    public Enumeration<Locale> getLocales() {
        return getWrapped().getLocales();
    }

    /**
     * Get the parameter.
     *
     * @param name the name.
     * @return the value, or null if not found.
     */
    @Override
    public String getParameter(String name) {
        return getWrapped().getParameter(name);
    }

    /**
     * Get the parameter map.
     *
     * @return the parameter map.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return getWrapped().getParameterMap();
    }

    /**
     * Get the parameter names.
     *
     * @return the parameter names.
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return getWrapped().getParameterNames();
    }

    /**
     * Get the parameter values.
     *
     * @param name the name.
     * @return the values.
     */
    @Override
    public String[] getParameterValues(String name) {
        return getWrapped().getParameterValues(name);
    }

    /**
     * Get the protocol.
     *
     * @return the protocol.
     */
    @Override
    public String getProtocol() {
        return getWrapped().getProtocol();
    }

    /**
     * Get the reader.
     *
     * @return the reader.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return getWrapped().getReader();
    }

    /**
     * Get the real path.
     *
     * @param path the path.
     * @return the real path.
     * @deprecated
     */
    @Deprecated
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
        return getWrapped().getRemoteAddr();
    }

    /**
     * Get the remote host.
     *
     * @return the remote host.
     */
    @Override
    public String getRemoteHost() {
        return getWrapped().getRemoteHost();
    }

    /**
     * Get the remote port.
     *
     * @return the remote port.
     */
    @Override
    public int getRemotePort() {
        return getWrapped().getRemotePort();
    }

    /**
     * Get the wrapped request.
     *
     * @return the wrapped request.
     */
    public ServletRequest getRequest() {
        return getWrapped();
    }

    /**
     * Get the request dispatcher.
     *
     * @param path the path.
     * @return the request dispatcher.
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return getWrapped().getRequestDispatcher(path);
    }

    /**
     * Get the scheme.
     *
     * @return the scheme.
     */
    @Override
    public String getScheme() {
        return getWrapped().getScheme();
    }

    /**
     * Get the server name.
     *
     * @return the server name.
     */
    @Override
    public String getServerName() {
        return getWrapped().getServerName();
    }

    /**
     * Get the server port.
     *
     * @return the server port.
     */
    @Override
    public int getServerPort() {
        return getWrapped().getServerPort();
    }

    /**
     * Get the servlet context.
     *
     * @return the servlet context.
     */
    @Override
    public ServletContext getServletContext() {
        return getWrapped().getServletContext();
    }

    /**
     * Is async started.
     *
     * @return true if async started, false otherwise.
     */
    @Override
    public boolean isAsyncStarted() {
        return getWrapped().isAsyncStarted();
    }

    /**
     * Is async supported.
     *
     * @return true if async is supported, false otherwise.
     */
    @Override
    public boolean isAsyncSupported() {
        return getWrapped().isAsyncSupported();
    }

    /**
     * Is secure.
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isSecure() {
        return getWrapped().isSecure();
    }

    /**
     * Is this a wrapper for the given request.
     *
     * @param wrapped the wrapped request.
     * @return true if it is, false otherwise.
     */
    public boolean isWrapperFor(ServletRequest wrapped) {
        if (this.getWrapped() == wrapped) {
            return true;
        } else if (this.getWrapped() instanceof RealtimeHttpServletRequestWrapper) {
            return ((RealtimeHttpServletRequestWrapper) this.getWrapped()).isWrapperFor(wrapped);
        } else {
            return false;
        }
    }

    /**
     * Are we a wrapper for the given type.
     *
     * @param wrappedType the wrapped type.
     * @return true if we are, false otherwise.
     */
    public boolean isWrapperFor(Class<?> wrappedType) {
        if (!ServletRequest.class.isAssignableFrom(wrappedType)) {
            throw new IllegalArgumentException("Given class "
                    + wrappedType.getName() + " not a subinterface of "
                    + ServletRequest.class.getName());
        }
        if (wrappedType.isAssignableFrom(getWrapped().getClass())) {
            return true;
        } else if (getWrapped() instanceof RealtimeHttpServletRequestWrapper) {
            return ((RealtimeHttpServletRequestWrapper) getWrapped()).isWrapperFor(wrappedType);
        } else {
            return false;
        }
    }

    /**
     * Remove the attribute.
     *
     * @param name the name.
     */
    @Override
    public void removeAttribute(String name) {
        getWrapped().removeAttribute(name);
    }

    /**
     * Set the attribute.
     *
     * @param name the name.
     * @param object the object value.
     */
    @Override
    public void setAttribute(String name, Object object) {
        getWrapped().setAttribute(name, object);
    }

    /**
     * Set the character encoding.
     *
     * @param characterEncoding the character encoding.
     * @throws UnsupportedEncodingException when trying to set an unsupported
     * character encoding.
     */
    @Override
    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException {
        getWrapped().setCharacterEncoding(characterEncoding);
    }

    /**
     * Start async processing.
     *
     * @throws IllegalStateException when not allowed.
     */
    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return getWrapped().startAsync();
    }

    /**
     * Start async processing.
     *
     * @param servletRequest the servlet request.
     * @param servletResponse the servlet response.
     */
    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return getWrapped().startAsync(servletRequest, servletResponse);
    }
    
    /**
     * Authenticate the request.
     *
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return getWrapped().authenticate(response);
    }

    /**
     * Change the session id.
     *
     * @return the new session id.
     */
    @Override
    public String changeSessionId() {
        return getWrapped().changeSessionId();
    }

    /**
     * Get the auth type.
     *
     * @return the auth type.
     */
    @Override
    public String getAuthType() {
        return getWrapped().getAuthType();
    }

    /**
     * Get the context path.
     *
     * @return the context path.
     */
    @Override
    public String getContextPath() {
        return getWrapped().getContextPath();
    }

    /**
     * Get the cookies.
     *
     * @return the cookies, or null if none.
     */
    @Override
    public Cookie[] getCookies() {
        return getWrapped().getCookies();
    }

    /**
     * Get the date header.
     *
     * @param name the name.
     * @return the date, or -1 if not found.
     */
    @Override
    public long getDateHeader(String name) {
        return getWrapped().getDateHeader(name);
    }

    /**
     * Get the header.
     *
     * @param name the name.
     * @return the value, or null if not found.
     */
    @Override
    public String getHeader(String name) {
        return getWrapped().getHeader(name);
    }

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        return getWrapped().getHeaderNames();
    }

    /**
     * Get the headers.
     *
     * @param name the name.
     * @return the values.
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        return getWrapped().getHeaders(name);
    }

    /**
     * Get the int header.
     *
     * @param name the name.
     * @return the int, or -1 if not found.
     */
    @Override
    public int getIntHeader(String name) {
        return getWrapped().getIntHeader(name);
    }

    /**
     * Get the method.
     *
     * @return the method.
     */
    @Override
    public String getMethod() {
        return getWrapped().getMethod();
    }

    /**
     * Get the part.
     *
     * @param name the name.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return getWrapped().getPart(name);
    }

    /**
     * Get the parts.
     *
     * @return the parts.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return getWrapped().getParts();
    }

    /**
     * Get the path info.
     *
     * @return the path info.
     */
    @Override
    public String getPathInfo() {
        return getWrapped().getPathInfo();
    }

    /**
     * Get the path translated.
     *
     * @return the path translated.
     */
    @Override
    public String getPathTranslated() {
        return getWrapped().getPathTranslated();
    }

    /**
     * Get the query string.
     *
     * @return the query string.
     */
    @Override
    public String getQueryString() {
        return getWrapped().getQueryString();
    }

    /**
     * Get the remote user.
     *
     * @return the remote user.
     */
    @Override
    public String getRemoteUser() {
        return getWrapped().getRemoteUser();
    }

    /**
     * Get the request URI.
     *
     * @return the request URI.
     */
    @Override
    public String getRequestURI() {
        return getWrapped().getRequestURI();
    }

    /**
     * Get the request URL.
     *
     * @return the request URL.
     */
    @Override
    public StringBuffer getRequestURL() {
        return getWrapped().getRequestURL();
    }

    /**
     * Get the requested session id.
     *
     * @return the requested session id.
     */
    @Override
    public String getRequestedSessionId() {
        return getWrapped().getRequestedSessionId();
    }

    /**
     * Get the servlet path.
     *
     * @return the servlet path.
     */
    @Override
    public String getServletPath() {
        return getWrapped().getServletPath();
    }

    /**
     * Get the HTTP session.
     *
     * @return the HTTP session.
     */
    @Override
    public HttpSession getSession() {
        return getWrapped().getSession();
    }

    /**
     * Get the HTTP session.
     *
     * @param create the create flag.
     * @return the HTTP session, or null if not able to create.
     */
    @Override
    public HttpSession getSession(boolean create) {
        return getWrapped().getSession(create);
    }

    /**
     * Get the user principal.
     *
     * @return the user principal.
     */
    @Override
    public Principal getUserPrincipal() {
        return getWrapped().getUserPrincipal();
    }

    /**
     * Get the wrapped request.
     *
     * @return the wrapped request.
     */
    protected abstract HttpServletRequest getWrapped();

    /**
     * Is the requested session id from a cookie.
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return getWrapped().isRequestedSessionIdFromCookie();
    }

    /**
     * Is the requested session id from a URL.
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isRequestedSessionIdFromURL() {
        return getWrapped().isRequestedSessionIdFromURL();
    }

    /**
     * Is the requested session if from a URL.
     *
     * @return true if it is, false otherwise.
     * @deprecated
     */
    @Deprecated
    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException();
    }

    /**
     * Is the requested session id valid.
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isRequestedSessionIdValid() {
        return getWrapped().isRequestedSessionIdValid();
    }

    /**
     * Is the user in the role.
     *
     * @param role the role.
     * @return true if the user is, false otherwise.
     */
    @Override
    public boolean isUserInRole(String role) {
        return getWrapped().isUserInRole(role);
    }

    /**
     * Login.
     *
     * @param username the username.
     * @param password the password.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void login(String username, String password) throws ServletException {
        getWrapped().login(username, password);
    }

    /**
     * Logout.
     *
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void logout() throws ServletException {
        getWrapped().logout();
    }

    /**
     * Upgrade the request.
     *
     * @param <T> the type of HTTP upgrade handler class.
     * @param handlerClass the handler class.
     * @return the HTTP upgrade handler.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return getWrapped().upgrade(handlerClass);
    }
}
