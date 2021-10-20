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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * The ServletResponse API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletResponse {

    /**
     * Flush the buffer.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void flushBuffer() throws IOException;

    /**
     * {@return the buffer size}
     */
    public int getBufferSize();

    /**
     * {@return the character encoding}
     */
    public String getCharacterEncoding();

    /**
     * {@return the content type}
     */
    public String getContentType();

    /**
     * {@return the locale}
     */
    public Locale getLocale();

    /**
     * Get the output stream.
     *
     * @return the output stream.
     * @throws IOException when an I/O error occurs.
     */
    public ServletOutputStream getOutputStream() throws IOException;

    /**
     * Get the writer.
     *
     * @return the writer.
     * @throws IOException when an I/O error occurs.
     */
    public PrintWriter getWriter() throws IOException;

    /**
     * Is committed.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isCommitted();

    /**
     * Reset.
     */
    public void reset();

    /**
     * Reset the buffer.
     */
    public void resetBuffer();

    /**
     * Set the buffer size.
     *
     * @param bufferSize the buffer size.
     */
    public void setBufferSize(int bufferSize);

    /**
     * Set the character encoding.
     *
     * @param characterEncoding the character encoding.
     */
    public void setCharacterEncoding(String characterEncoding);

    /**
     * Set the content length.
     *
     * @param contentLength the content length.
     */
    public void setContentLength(int contentLength);

    /**
     * Set the content length.
     *
     * @param contentLength the content length.
     */
    public void setContentLengthLong(long contentLength);

    /**
     * Set the content type.
     *
     * @param contentType the content type.
     */
    public void setContentType(String contentType);

    /**
     * Set the locale.
     *
     * @param locale the locale.
     */
    public void setLocale(Locale locale);
}
