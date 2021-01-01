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
package jakarta.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * The ServletRequestWrapper API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletRequestWrapper implements ServletRequest {

    /**
     * Stores the wrapped request.
     */
    private ServletRequest wrapped;

    /**
     * Constructor.
     *
     * @param wrapped the wrapped request.
     */
    public ServletRequestWrapper(ServletRequest wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Get the async context.
     *
     * @return the async context.
     */
    @Override
    public AsyncContext getAsyncContext() {
        return wrapped.getAsyncContext();
    }

    /**
     * Get the attribute.
     *
     * @param name the name.
     * @return the value.
     */
    @Override
    public Object getAttribute(String name) {
        return wrapped.getAttribute(name);
    }

    /**
     * Get the attribute names.
     *
     * @return the attribute names.
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return wrapped.getAttributeNames();
    }

    /**
     * Get the character encoding.
     *
     * @return the character encoding.
     */
    @Override
    public String getCharacterEncoding() {
        return wrapped.getCharacterEncoding();
    }

    /**
     * Get the content length.
     *
     * @return the content length.
     */
    @Override
    public int getContentLength() {
        return wrapped.getContentLength();
    }

    /**
     * Get the content length.
     *
     * @return the content length.
     */
    @Override
    public long getContentLengthLong() {
        return wrapped.getContentLengthLong();
    }

    /**
     * Get the content type.
     *
     * @return the content type.
     */
    @Override
    public String getContentType() {
        return wrapped.getContentType();
    }

    /**
     * Get the dispatcher type.
     */
    @Override
    public DispatcherType getDispatcherType() {
        return wrapped.getDispatcherType();
    }

    /**
     * Get the input stream.
     *
     * @return the input stream.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return wrapped.getInputStream();
    }

    /**
     * Get the local address.
     *
     * @return the local address.
     */
    @Override
    public String getLocalAddr() {
        return wrapped.getLocalAddr();
    }

    /**
     * Get the local name.
     *
     * @return the local name.
     */
    @Override
    public String getLocalName() {
        return wrapped.getLocalName();
    }

    /**
     * Get the local port.
     *
     * @return the local port.
     */
    @Override
    public int getLocalPort() {
        return wrapped.getLocalPort();
    }

    /**
     * Get the locale.
     *
     * @return the locale.
     */
    @Override
    public Locale getLocale() {
        return wrapped.getLocale();
    }

    /**
     * Get the locales.
     *
     * @return the locales.
     */
    @Override
    public Enumeration<Locale> getLocales() {
        return wrapped.getLocales();
    }

    /**
     * Get the parameter.
     *
     * @param name the name.
     * @return the value, or null if not found.
     */
    @Override
    public String getParameter(String name) {
        return wrapped.getParameter(name);
    }

    /**
     * Get the parameter map.
     *
     * @return the parameter map.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return wrapped.getParameterMap();
    }

    /**
     * Get the parameter names.
     *
     * @return the parameter names.
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return wrapped.getParameterNames();
    }

    /**
     * Get the parameter values.
     *
     * @param name the name.
     * @return the values.
     */
    @Override
    public String[] getParameterValues(String name) {
        return wrapped.getParameterValues(name);
    }

    /**
     * Get the protocol.
     *
     * @return the protocol.
     */
    @Override
    public String getProtocol() {
        return wrapped.getProtocol();
    }

    /**
     * Get the reader.
     *
     * @return the reader.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return wrapped.getReader();
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
        return wrapped.getRealPath(path);
    }

    /**
     * Get the remote address.
     *
     * @return the remote address.
     */
    @Override
    public String getRemoteAddr() {
        return wrapped.getRemoteAddr();
    }

    /**
     * Get the remote host.
     *
     * @return the remote host.
     */
    @Override
    public String getRemoteHost() {
        return wrapped.getRemoteHost();
    }

    /**
     * Get the remote port.
     *
     * @return the remote port.
     */
    @Override
    public int getRemotePort() {
        return wrapped.getRemotePort();
    }

    /**
     * Get the wrapped request.
     *
     * @return the wrapped request.
     */
    public ServletRequest getRequest() {
        return wrapped;
    }

    /**
     * Get the request dispatcher.
     *
     * @param path the path.
     * @return the request dispatcher.
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return wrapped.getRequestDispatcher(path);
    }

    /**
     * Get the scheme.
     *
     * @return the scheme.
     */
    @Override
    public String getScheme() {
        return wrapped.getScheme();
    }

    /**
     * Get the server name.
     *
     * @return the server name.
     */
    @Override
    public String getServerName() {
        return wrapped.getServerName();
    }

    /**
     * Get the server port.
     *
     * @return the server port.
     */
    @Override
    public int getServerPort() {
        return wrapped.getServerPort();
    }

    /**
     * Get the servlet context.
     *
     * @return the servlet context.
     */
    @Override
    public ServletContext getServletContext() {
        return wrapped.getServletContext();
    }

    /**
     * Is async started.
     *
     * @return true if async started, false otherwise.
     */
    @Override
    public boolean isAsyncStarted() {
        return wrapped.isAsyncStarted();
    }

    /**
     * Is async supported.
     *
     * @return true if async is supported, false otherwise.
     */
    @Override
    public boolean isAsyncSupported() {
        return wrapped.isAsyncSupported();
    }

    /**
     * Is secure.
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isSecure() {
        return wrapped.isSecure();
    }

    /**
     * Is this a wrapper for the given request.
     *
     * @param wrapped the wrapped request.
     * @return true if it is, false otherwise.
     */
    public boolean isWrapperFor(ServletRequest wrapped) {
        if (this.wrapped == wrapped) {
            return true;
        } else if (this.wrapped instanceof ServletRequestWrapper) {
            return ((ServletRequestWrapper) this.wrapped).isWrapperFor(wrapped);
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
        if (wrappedType.isAssignableFrom(wrapped.getClass())) {
            return true;
        } else if (wrapped instanceof ServletRequestWrapper) {
            return ((ServletRequestWrapper) wrapped).isWrapperFor(wrappedType);
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
        wrapped.removeAttribute(name);
    }

    /**
     * Set the attribute.
     *
     * @param name the name.
     * @param object the object value.
     */
    @Override
    public void setAttribute(String name, Object object) {
        wrapped.setAttribute(name, object);
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
        wrapped.setCharacterEncoding(characterEncoding);
    }

    /**
     * Set the wrapped request.
     *
     * @param wrapped the wrapped request.
     */
    public void setRequest(ServletRequest wrapped) {
        if (wrapped == null)
            throw new IllegalArgumentException("Request cannot be null");
        this.wrapped = wrapped;
    }

    /**
     * Start async processing.
     *
     * @throws IllegalStateException when not allowed.
     */
    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return wrapped.startAsync();
    }

    /**
     * Start async processing.
     *
     * @param servletRequest the servlet request.
     * @param servletResponse the servlet response.
     */
    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return wrapped.startAsync(servletRequest, servletResponse);
    }
}
