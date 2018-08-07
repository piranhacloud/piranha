/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;

/**
 * The default HttpServletResponse.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class DefaultHttpServletResponse implements WebApplicationResponse {

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
    protected boolean gotWriter;

    /**
     * Stores the header manager.
     */
    protected DefaultHttpHeaderManager headerManager;

    /**
     * Stores the locale.
     */
    protected Locale locale;

    /**
     * Stores the writer.
     */
    protected PrintWriter writer;

    /**
     * Stores the output stream.
     */
    protected ServletOutputStream outputStream;

    /**
     * Stores the status code.
     */
    protected int status;

    /**
     * Stores the status message.
     */
    protected String statusMessage;

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;

    /**
     * Constructor.
     */
    public DefaultHttpServletResponse() {
        headerManager = new DefaultHttpHeaderManager();
        gotOutput = false;
        gotWriter = false;
        characterEncoding = null;
        characterEncodingSet = false;
        committed = false;
        contentType = null;
        contentTypeSet = false;
        cookies = new ArrayList<>();
        locale = null;
        status = 200;
        statusMessage = null;
        writer = null;
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

        if (webApplication.getHttpSessionManager() != null) {
            result = webApplication.getHttpSessionManager().encodeRedirectURL(this, url);
        }

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

        if (webApplication.getHttpSessionManager() != null) {
            result = webApplication.getHttpSessionManager().encodeURL(this, url);
        }

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
    }

    /**
     * Get the character encoding.
     *
     * @return the character encoding.
     */
    @Override
    public String getCharacterEncoding() {
        String result = "ISO-8859-1";
        if (characterEncoding != null) {
            result = characterEncoding;
        }
        return result;
    }

    @Override
    public abstract int getBufferSize();

    /**
     * Get the content length.
     *
     * @return the content length.
     */
    public long getContentLength() {
        return contentLength;
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
            return outputStream;
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
     * Get the status message.
     *
     * @return the status message.
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Get the web application.
     *
     * @return the web application.
     */
    public WebApplication getWebApplication() {
        return webApplication;
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
    public abstract void resetBuffer();

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
     * Send the redirect.
     *
     * @param location the location.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendRedirect(String location) throws IOException {
        verifyNotCommitted("sendRedirect");
        resetBuffer();
        setStatus(SC_FOUND);
        flushBuffer();
    }

    /**
     * Set the buffer size.
     *
     * @param size the buffer size in bytes.
     */
    @Override
    public abstract void setBufferSize(int size);

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
     * Set the committed.
     *
     * @param committed the committed flag.
     */
    public void setCommitted(boolean committed) {
        this.committed = committed;
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
                    encoding = encoding.substring(encoding.indexOf("=") + 1).trim();
                    setCharacterEncoding(encoding);
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
    public void setOutputStream(ServletOutputStream outputStream) {
        this.outputStream = outputStream;
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
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
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
}
