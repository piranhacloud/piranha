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
package javax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * The ServletResponseWrapper API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletResponseWrapper implements ServletResponse {

    /**
     * Stores the wrapped response.
     */
    private ServletResponse wrapped;

    /**
     * Constructor.
     *
     * @param wrapped the wrapped response.
     */
    public ServletResponseWrapper(ServletResponse wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Flush the buffer.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void flushBuffer() throws IOException {
        wrapped.flushBuffer();
    }

    /**
     * Get the buffer size.
     *
     * @return the buffer size.
     */
    @Override
    public int getBufferSize() {
        return wrapped.getBufferSize();
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
     * Get the content type.
     *
     * @return the content type.
     */
    @Override
    public String getContentType() {
        return this.wrapped.getContentType();
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
     * Get the output stream.
     *
     * @return the output stream.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return wrapped.getOutputStream();
    }

    /**
     * Get the wrapped response.
     *
     * @return the wrapped response.
     */
    public ServletResponse getResponse() {
        return wrapped;
    }

    /**
     * Get the writer.
     *
     * @return the writer.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        return wrapped.getWriter();
    }

    /**
     * Is committed.
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isCommitted() {
        return wrapped.isCommitted();
    }

    /**
     * Is wrapper for.
     *
     * @param wrapped the wrapped response.
     * @return true if we wrap it, false otherwise.
     */
    public boolean isWrapperFor(ServletResponse wrapped) {
        if (this.wrapped == wrapped) {
            return true;
        } else if (this.wrapped instanceof ServletResponseWrapper) {
            return ((ServletResponseWrapper) this.wrapped).isWrapperFor(wrapped);
        } else {
            return false;
        }
    }

    /**
     * Is wrapper for.
     *
     * @param wrappedType the wrapped type.
     * @return true if we wrap it, false otherwise.
     */
    public boolean isWrapperFor(Class<?> wrappedType) {
        if (wrappedType.isAssignableFrom(wrapped.getClass())) {
            return true;
        } else if (wrapped instanceof ServletResponseWrapper) {
            return ((ServletResponseWrapper) wrapped).isWrapperFor(wrappedType);
        } else {
            return false;
        }
    }

    /**
     * Reset.
     */
    @Override
    public void reset() {
        wrapped.reset();
    }

    /**
     * Reset the buffer.
     */
    @Override
    public void resetBuffer() {
        wrapped.resetBuffer();
    }

    /**
     * Set the buffer size.
     *
     * @param bufferSize the buffer size.
     */
    @Override
    public void setBufferSize(int bufferSize) {
        wrapped.setBufferSize(bufferSize);
    }

    /**
     * Set the character encoding.
     *
     * @param characterEncoding the character encoding.
     */
    @Override
    public void setCharacterEncoding(String characterEncoding) {
        wrapped.setCharacterEncoding(characterEncoding);
    }

    /**
     * Set the content length.
     *
     * @param contentLength the content length.
     */
    @Override
    public void setContentLength(int contentLength) {
        this.wrapped.setContentLength(contentLength);
    }

    /**
     * Set the content length.
     *
     * @param contentLength the content length.
     */
    @Override
    public void setContentLengthLong(long contentLength) {
        wrapped.setContentLengthLong(contentLength);
    }

    /**
     * Set the content type.
     *
     * @param contentType the content type.
     */
    @Override
    public void setContentType(String contentType) {
        wrapped.setContentType(contentType);
    }

    /**
     * Set the locale.
     *
     * @param locale the locale.
     */
    @Override
    public void setLocale(Locale locale) {
        wrapped.setLocale(locale);
    }

    /**
     * Set the wrapped response.
     *
     * @param wrapped the wrapped response.
     */
    public void setResponse(ServletResponse wrapped) {
        this.wrapped = wrapped;
    }
}
