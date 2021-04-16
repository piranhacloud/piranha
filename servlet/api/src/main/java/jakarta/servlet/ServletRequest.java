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
 * The ServletRequest API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletRequest {

    /**
     * {@return the async context}
     */
    public AsyncContext getAsyncContext();

    /**
     * Get the attribute.
     *
     * @param name the name.
     * @return the value, or null if not found.
     */
    public Object getAttribute(String name);

    /**
     * {@return the attribute names}
     */
    public Enumeration<String> getAttributeNames();

    /**
     * {@return the character encoding}
     */
    public String getCharacterEncoding();

    /**
     * Get the content length.
     *
     * @return the content length, or -1 if not known.
     */
    public int getContentLength();

    /**
     * Get the content length.
     *
     * @return the content length, or -1 if not known.
     */
    public long getContentLengthLong();

    /**
     * {@return the content type}
     */
    public String getContentType();

    /**
     * {@return the dispatcher type}
     */
    public DispatcherType getDispatcherType();

    /**
     * Get the input stream.
     *
     * @return the input stream.
     * @throws IOException when an I/O error occurs.
     */
    public ServletInputStream getInputStream() throws IOException;

    /**
     * {@return the local address}
     */
    public String getLocalAddr();

    /**
     * {@return the local name}
     */
    public String getLocalName();

    /**
     * {@return the local port}
     */
    public int getLocalPort();

    /**
     * {@return the locale}
     */
    public Locale getLocale();

    /**
     * {@return the locales}
     */
    public Enumeration<Locale> getLocales();

    /**
     * Get the parameter.
     *
     * @param name the name.
     * @return the value or null if not found.
     */
    public String getParameter(String name);

    /**
     * {@return the parameter map}
     */
    public Map<String, String[]> getParameterMap();

    /**
     * {@return the parameter names}
     */
    public Enumeration<String> getParameterNames();

    /**
     * Get the parameter values.
     *
     * @param name the name.
     * @return the values.
     */
    public String[] getParameterValues(String name);

    /**
     * {@return the protocol}
     */
    public String getProtocol();

    /**
     * Get the reader.
     *
     * @return the reader.
     * @throws IOException when an I/O error occurs.
     */
    public BufferedReader getReader() throws IOException;

    /**
     * {@return the real path}
     * @param path the path.
     * @deprecated
     */
    @Deprecated
    public String getRealPath(String path);

    /**
     * {@return the remote address}
     */
    public String getRemoteAddr();

    /**
     * {@return the remote host}
     */
    public String getRemoteHost();

    /**
     * {@return the remote port}
     */
    public int getRemotePort();

    /**
     * {@return the request dispatcher}
     * @param path the path.
     */
    public RequestDispatcher getRequestDispatcher(String path);

    /**
     * {@return the scheme}
     */
    public String getScheme();

    /**
     * {@return the server name}
     */
    public String getServerName();

    /**
     * {@return the server port}
     */
    public int getServerPort();

    /**
     * {@return the servlet context}
     */
    public ServletContext getServletContext();

    /**
     * Is async started.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isAsyncStarted();

    /**
     * Is async supported.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isAsyncSupported();

    /**
     * Is secure.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isSecure();

    /**
     * Remove the attribute.
     *
     * @param name the name.
     */
    public void removeAttribute(String name);

    /**
     * Set the attribute.
     *
     * @param name the name.
     * @param object the object value.
     */
    public void setAttribute(String name, Object object);

    /**
     * Set the character encoding.
     *
     * @param characterEncoding the character encoding.
     * @throws UnsupportedEncodingException when the encoding is NOT supported.
     */
    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException;

    /**
     * Start async processing.
     *
     * @return the async context.
     * @throws IllegalStateException when not able to start async processing.
     */
    public AsyncContext startAsync() throws IllegalStateException;

    /**
     * Start async processing.
     *
     * @param servletRequest the servlet request.
     * @param servletResponse the servlet response.
     * @return the async context.
     * @throws IllegalStateException when not able to start async processing.
     */
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException;
}
