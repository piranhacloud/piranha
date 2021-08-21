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

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * A test Servlet request.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestServletRequest implements ServletRequest {
    /**
     * {@return the async context}
     */
    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    /**
     * Get the attribute.
     * 
     * @param name the name.
     * @return the value.
     */
    @Override
    public Object getAttribute(String name) {
        return null;
    }

    /**
     * {@return the attribute names}
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(new ArrayList<>());
    }

    /**
     * {@return the character encoding}
     */
    @Override
    public String getCharacterEncoding() {
        return "UTF-8";
    }

    /**
     * {@return the content length}
     */
    @Override
    public int getContentLength() {
        return -1;
    }

    /**
     * Get the content length (long).
     * 
     * @return the content length.
     */
    @Override
    public long getContentLengthLong() {
        return -1L;
    }

    /**
     * {@return the content type}
     */
    @Override
    public String getContentType() {
        return "text/html";
    }

    /**
     * {@return the dispatcher type}
     */
    @Override
    public DispatcherType getDispatcherType() {
        return DispatcherType.ERROR;
    }

    /**
     * Get the servlet input stream.
     * 
     * @return the servlet input stream.
     * @throws IOException when I/O error occurs.
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new TestServletInputStream();
    }

    /**
     * {@return the local address}
     */
    @Override
    public String getLocalAddr() {
        return "127.0.0.1";
    }

    /**
     * {@return the local name}
     */
    @Override
    public String getLocalName() {
        return "localhost";
    }

    /**
     * {@return the local port}
     */
    @Override
    public int getLocalPort() {
        return 8180;
    }

    /**
     * {@return the locale}
     */
    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * {@return the locales}
     */
    @Override
    public Enumeration<Locale> getLocales() {
        return Collections.enumeration(new ArrayList<>());
    }

    /**
     * Get the parameter.
     * 
     * @param name the name.
     * @return the value.
     */
    @Override
    public String getParameter(String name) {
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public void setAttribute(String name, Object object) {

    }

    @Override
    public void setCharacterEncoding(String characterEncoding) throws UnsupportedEncodingException {

    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }
}
