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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * The nano HttpServletResponse.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoHttpServletResponse extends ServletOutputStream implements HttpServletResponse {

    /**
     * Stores the buffer.
     */
    private byte[] buffer;
    
    /**
     * Stores the content type.
     */
    private String contentType;
    
    /**
     * Stores the gotWriter flag.
     */
    private boolean gotWriter;
    
    /**
     * Stores the output stream.
     */
    private OutputStream outputStream;

    /**
     * Stores the status.
     */
    private int status;

    /**
     * Stores the writer.
     */
    private PrintWriter writer;

    /**
     * Constructor.
     *
     * @param outputStream the output stream.
     */
    public NanoHttpServletResponse(OutputStream outputStream) {
        this.buffer = new byte[8192];
        this.contentType = null;
        this.gotWriter = false;
        this.outputStream = outputStream;
        this.status = 200;
    }

    /**
     * Not supported.
     *
     * @param cookie the cookie.
     */
    @Override
    public void addCookie(Cookie cookie) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @param date the date.
     */
    @Override
    public void addDateHeader(String name, long date) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void addHeader(String name, String value) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void addIntHeader(String name, int value) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @return true if it contains the header, false otherwise.
     */
    @Override
    public boolean containsHeader(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param url the url.
     * @return the encoded redirect url.
     */
    @Override
    public String encodeRedirectURL(String url) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param url the url.
     * @return the encoded redirect url.
     */
    @Override
    public String encodeRedirectUrl(String url) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param url the url.
     * @return the encoded url.
     */
    @Override
    public String encodeURL(String url) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param url the url.
     * @return the encoded url.
     */
    @Override
    public String encodeUrl(String url) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Flush the buffer.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void flushBuffer() throws IOException {
        if (gotWriter) {
            writer.flush();
        }
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @return the value.
     */
    @Override
    public String getHeader(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @return the header names.
     */
    @Override
    public Collection<String> getHeaderNames() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @return the values.
     */
    @Override
    public Collection<String> getHeaders(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Get the status.
     *
     * @return the status.
     */
    @Override
    public int getStatus() {
        return status;
    }

    /**
     * Get the writer.
     *
     * @return the writer.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(outputStream));
            gotWriter = true;
        }
        return writer;
    }

    /**
     * Not supported.
     *
     * @param status the status.
     * @param message the message.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendError(int status, String message) throws IOException {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param status the status.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendError(int status) throws IOException {
        this.status = status;
    }

    /**
     * Not supported.
     *
     * @param location the location.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendRedirect(String location) throws IOException {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @param date the date.
     */
    @Override
    public void setDateHeader(String name, long date) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setHeader(String name, String value) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setIntHeader(String name, int value) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setStatus(int status) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setStatus(int status, String message) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getBufferSize() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void resetBuffer() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Set the buffer size.
     * 
     * @param bufferSize the buffer size.
     */
    @Override
    public void setBufferSize(int bufferSize) {
        this.buffer = new byte[bufferSize];
    }

    @Override
    public void setCharacterEncoding(String characterEncoding) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setContentLength(int contentLength) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setContentLengthLong(long contentLength) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Set the content type.
     * 
     * @param contentType the content type.
     */
    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setLocale(Locale locale) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean isReady() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void write(int b) throws IOException {
        throw new UnsupportedOperationException("Not supported");
    }
}
