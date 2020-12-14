/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;

/**
 * A test Servlet request.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestServletRequest extends ServletRequestWrapper {
    
    /**
     * Constructor.
     * 
     * @param wrapped the wrapped Servlet request.
     */
    public TestServletRequest(ServletRequest wrapped) {
        super(null);
    }

    /**
     * Get the async context.
     * 
     * @return the async context.
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
     * Get the attribute names.
     * 
     * @return the attribute names.
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(new ArrayList<>());
    }

    /**
     * Get the character encoding.
     * 
     * @return the character encoding.
     */
    @Override
    public String getCharacterEncoding() {
        return "UTF-8";
    }

    /**
     * Get the content length.
     * 
     * @return the content length.
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
     * Get the content type.
     * 
     * @return the content type.
     */
    @Override
    public String getContentType() {
        return "text/html";
    }

    /**
     * Get the dispatcher type.
     * 
     * @return the dispatcher type.
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
     * Get the local address.
     * 
     * @return the local address.
     */
    @Override
    public String getLocalAddr() {
        return "127.0.0.1";
    }

    /**
     * Get the local name.
     * 
     * @return the local name.
     */
    @Override
    public String getLocalName() {
        return "localhost";
    }

    /**
     * Get the local port.
     * 
     * @return the local port.
     */
    @Override
    public int getLocalPort() {
        return 8180;
    }

    /**
     * Get the locale.
     * 
     * @return the locale.
     */
    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * Get the locales.
     * 
     * @return the locales.
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
}
