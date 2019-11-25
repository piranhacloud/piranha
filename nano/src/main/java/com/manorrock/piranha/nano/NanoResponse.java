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

import cloud.piranha.DefaultHttpHeaderManager;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * The Nano version of the HttpServletResponse and the ServletOutputStream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoResponse extends ServletOutputStream implements HttpServletResponse {

    /**
     * Stores the body only flag.
     */
    protected boolean bodyOnly;

    /**
     * Stores the buffer.
     */
    protected byte[] buffer;

    /**
     * Stores the character encoding.
     */
    protected String characterEncoding;

    /**
     * Stores if the character encoding has been set manually.
     */
    protected boolean characterEncodingSet;

    /**
     * Stores the committed flag.
     */
    protected boolean committed;

    /**
     * Stores the content length.
     */
    protected long contentLength;

    /**
     * Stores the content type.
     */
    protected String contentType;

    /**
     * Stores if the content type has been set manually.
     */
    protected boolean contentTypeSet;

    /**
     * Stores the cookies.
     */
    protected List<Cookie> cookies;

    /**
     * Stores if we acquired the output stream.
     */
    protected boolean gotOutput;

    /**
     * Stores if we acquired the writer.
     */
    private boolean gotWriter;

    /**
     * Stores the header manager.
     */
    protected DefaultHttpHeaderManager headerManager;

    /**
     * Stores the index.
     */
    protected int index;

    /**
     * Stores the locale.
     */
    protected Locale locale;

    /**
     * Stores the output stream.
     */
    protected OutputStream outputStream;

    /**
     * Stores the status.
     */
    protected int status;

    /**
     * Stores the status message.
     */
    protected String statusMessage;

    /**
     * Stores the writer.
     */
    protected PrintWriter writer;

    /**
     * Constructor.
     */
    public NanoResponse() {
        this.bodyOnly = true;
        this.buffer = new byte[8192];
        this.contentType = null;
        this.cookies = new ArrayList<>();
        this.headerManager = new DefaultHttpHeaderManager();
        this.gotWriter = false;
        this.status = 200;
    }

    /**
     * Constructor.
     *
     * @param outputStream the output stream.
     */
    public NanoResponse(OutputStream outputStream) {
        this();
        this.outputStream = outputStream;
    }

    /**
     * Add the cookie.
     *
     * @param cookie the cookie.
     */
    @Override
    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
    }

    /**
     * Add the date header.
     *
     * @param name the header name.
     * @param date the header date value.
     */
    @Override
    public void addDateHeader(String name, long date) {
        addHeader(name, Long.toString(date));
    }

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void addHeader(String name, String value) {
        headerManager.addHeader(name, value);
    }

    /**
     * Add the integer header.
     *
     * @param name the name of the header.
     * @param value the value of the header.
     */
    @Override
    public void addIntHeader(String name, int value) {
        addHeader(name, Integer.toString(value));
    }

    /**
     * Contains the given header.
     *
     * @param name the header name.
     * @return true if there, false otherwise.
     */
    @Override
    public boolean containsHeader(String name) {
        return headerManager.containsHeader(name);
    }

    /**
     * Encode the redirect URL.
     *
     * @param url the url.
     * @return the encoded redirect url.
     */
    @Override
    public String encodeRedirectURL(String url) {
        String result = url;
        return result;
    }

    /**
     * Encode the redirect url.
     *
     * @param url the url.
     * @return the encoded redirect url.
     * @deprecated
     */
    @Override
    public String encodeRedirectUrl(String url) {
        throw new UnsupportedOperationException("HttpServletResponse.encodeRedirectUrl is no longer supported");
    }

    /**
     * Encode the URL.
     *
     * @param url the URL.
     * @return the encoded url.
     */
    @Override
    public String encodeURL(String url) {
        String result = url;
        return result;
    }

    /**
     * Encode the url.
     *
     * @param url the url.
     * @return the encoded url.
     * @deprecated
     */
    @Override
    public String encodeUrl(String url) {
        throw new UnsupportedOperationException("HttpServletResponse.encodeUrl is no longer supported");
    }

    /**
     * Flush the output stream.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        if (!isCommitted()) {
            writeOut();
        }
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
        outputStream.flush();
        committed = true;
    }

    /**
     * Get the buffer size.
     *
     * @return the buffer size.
     */
    @Override
    public int getBufferSize() {
        return buffer.length;
    }

    /**
     * Get the character encoding.
     *
     * @return the character encoding.
     */
    @Override
    public String getCharacterEncoding() {
        String result = null;
        if (characterEncoding != null) {
            result = characterEncoding;
        }
        return result;
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
     * Get the header.
     *
     * @param name the header name.
     * @return the value.
     */
    @Override
    public String getHeader(String name) {
        return headerManager.getHeader(name);
    }

    /**
     * Get the header names.
     *
     * @return the collection.
     */
    @Override
    public Collection<String> getHeaderNames() {
        ArrayList<String> result = new ArrayList<>();
        Enumeration<String> enumeration = headerManager.getHeaderNames();
        if (enumeration != null) {
            result = Collections.list(enumeration);
        }
        return result;
    }

    /**
     * Get the headers.
     *
     * @param name the name of the header.
     * @return the collection.
     */
    @Override
    public Collection<String> getHeaders(String name) {
        ArrayList<String> result = new ArrayList<>();
        Enumeration<String> enumeration = headerManager.getHeaders(name);
        if (enumeration != null) {
            result = Collections.list(enumeration);
        }
        return result;
    }

    /**
     * Get the locale.
     *
     * @return the locale.
     */
    @Override
    public Locale getLocale() {
        return locale;
    }

    /**
     * Get the output stream.
     *
     * @return the output stream.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (!gotWriter) {
            gotOutput = true;
            return this;
        } else {
            throw new IllegalStateException("Cannot get output stream as the writer was already acquired");
        }
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
    public synchronized PrintWriter getWriter() throws IOException {
        PrintWriter result = null;
        if (!gotOutput) {
            if (gotWriter == false) {
                gotWriter = true;
                if (characterEncoding == null) {
                    characterEncoding = "ISO-8859-1";
                }
                writer = new PrintWriter(new OutputStreamWriter(outputStream, characterEncoding));
            }
            result = writer;
        } else {
            throw new IllegalStateException("Cannot get writer as the output stream was already acquired");
        }
        return result;
    }

    /**
     * Is committed.
     *
     * @return the committed flag.
     */
    @Override
    public boolean isCommitted() {
        return committed;
    }

    /**
     * Is the output stream ready?
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * Reset the response.
     */
    @Override
    public void reset() {
        verifyNotCommitted("reset");
        this.status = 200;
        this.statusMessage = null;
        resetBuffer();
    }

    /**
     * Reset the buffer.
     */
    @Override
    public void resetBuffer() {
        buffer = new byte[buffer.length];
    }

    /**
     * Send the error.
     *
     * @param status the status code.
     * @param statusMessage the message.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendError(int status, String statusMessage) throws IOException {
        verifyNotCommitted("sendError");
        setStatus(status);
        this.statusMessage = statusMessage;
        flushBuffer();
    }

    /**
     * Send an error.
     *
     * @param status the error code.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendError(int status) throws IOException {
        verifyNotCommitted("sendError");
        setStatus(status);
        flushBuffer();
    }

    /**
     * Not supported.
     *
     * @param location the location.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendRedirect(String location) throws IOException {
        throw new UnsupportedOperationException("sendRedirect is not supported");
    }

    /**
     * Set the body only flag.
     *
     * @param bodyOnly if true the response will only output the body, if false
     * the response will contain the status line and response headers.
     */
    public void setBodyOnly(boolean bodyOnly) {
        this.bodyOnly = bodyOnly;
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

    /**
     * Set the character encoding.
     *
     * @param characterEncoding the character encoding.
     */
    @Override
    public void setCharacterEncoding(String characterEncoding) {
        if (!gotWriter && !committed) {
            this.characterEncoding = characterEncoding;
            characterEncodingSet = true;
        }
    }

    /**
     * Set the content length.
     *
     * @param contentLength the content length.
     */
    @Override
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * Set the content length.
     *
     * @param contentLength the content length.
     */
    @Override
    public void setContentLengthLong(long contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * Set the content type.
     *
     * @param type the content type.
     */
    @Override
    public void setContentType(String type) {
        if (!gotWriter && !committed) {
            if (type != null) {
                if (type.contains(";")) {
                    contentType = type.substring(0, type.indexOf(";")).trim();
                    String encoding = type.substring(type.indexOf(";") + 1).trim();
                    if (encoding.contains("=")) {
                        encoding = encoding.substring(encoding.indexOf("=") + 1).trim();
                        setCharacterEncoding(encoding);
                    }
                } else {
                    contentType = type;
                }
            } else {
                contentType = type;
            }
            contentTypeSet = true;
        }
    }

    /**
     * Set the date header.
     *
     * @param name the header name.
     * @param date the date.
     */
    @Override
    public void setDateHeader(String name, long date) {
        setHeader(name, Long.toString(date));
    }

    /**
     * Set the header.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setHeader(String name, String value) {
        headerManager.setHeader(name, value);
    }

    /**
     * Set the int header.
     *
     * @param name the header name.
     * @param value the header value.
     */
    @Override
    public void setIntHeader(String name, int value) {
        setHeader(name, Integer.toString(value));
    }

    /**
     * Set the locale.
     *
     * @param locale the locale.
     */
    @Override
    public void setLocale(Locale locale) {
        if (!gotWriter && !committed) {
            this.locale = locale;
        }
    }

    /**
     * Set the output stream.
     *
     * @param outputStream the output stream.
     */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Set the status.
     *
     * @param status the status.
     * @param statusMessage the message
     * @deprecated
     */
    @Override
    public void setStatus(int status, String statusMessage) {
        setStatus(status);
    }

    /**
     * Set the status.
     *
     * @param status the status code.
     */
    @Override
    public void setStatus(int status) {
        verifyNotCommitted("setStatus");
        this.status = status;
    }

    /**
     * Set the write listener.
     *
     * @param listener the write listener.
     */
    @Override
    public void setWriteListener(WriteListener listener) {
    }

    /**
     * Verify we are not committed.
     *
     * @param methodName the method we are checking for.
     */
    protected void verifyNotCommitted(String methodName) {
        if (isCommitted()) {
            throw new IllegalStateException("Response already committed in " + methodName);
        }
    }

    /**
     * Write the integer.
     *
     * @param integer the integer.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void write(int integer) throws IOException {
        if (index == buffer.length - 1) {
            outputStream.write(integer);
        } else if (index == buffer.length) {
            outputStream.write(integer);
        } else {
            this.buffer[index] = (byte) integer;
            this.index++;
        }
    }

    /**
     * Write out the status-line, headers and the buffer.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeOut() throws IOException {
        if (!bodyOnly) {
//        writeStatusLine();
//        writeContentType();
//        writeCookies();
//        writeHeaders();
        }
        outputStream.write(buffer, 0, index);
        index = buffer.length;
        committed = true;
    }
}
