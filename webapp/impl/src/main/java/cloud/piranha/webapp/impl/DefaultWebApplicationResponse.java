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
package cloud.piranha.webapp.impl;

import static java.util.Collections.list;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import cloud.piranha.core.api.LocaleEncodingManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationResponse;

/**
 * The default WebApplicationResponse.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationResponse extends ServletOutputStream implements WebApplicationResponse {

    /**
     * Defines the 'ISO-8859-1' constant.
     */
    private static final String ISO_8859_1 = "ISO-8859-1";
    
    /**
     * Stores the body only flag.
     */
    protected boolean bodyOnly;

    /**
     * Stores the buffer.
     */
    protected byte[] buffer;
    
    /**
     * Boolean indicating the buffer is resetting. When true, 
     * all output written will be ignored (thrown away).
     */
    protected boolean bufferResetting;

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
     * Stores the index.
     */
    protected int index;

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
    protected OutputStream outputStream;

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
     * Stores the content language
     */
    protected String contentLanguage;

    /**
     * Stores the response closer.
     */
    protected Runnable responseCloser;

    /**
     * Constructor.
     */
    public DefaultWebApplicationResponse() {
        buffer = new byte[8192];
        characterEncoding = ISO_8859_1;
        characterEncodingSet = false;
        committed = false;
        contentType = null;
        contentTypeSet = false;
        cookies = new ArrayList<>();
        gotOutput = false;
        gotWriter = false;
        headerManager = new DefaultHttpHeaderManager();
        locale = Locale.getDefault();
        status = 200;
        statusMessage = null;
        writer = null;
    }

    @Override
    public void addCookie(Cookie cookie) {
        if (isInclude()) {
            return;
        }
        this.cookies.add(cookie);
    }

    @Override
    public void addDateHeader(String name, long date) {
        addHeader(name, formatDateToGMT(date));
    }

    @Override
    public void addHeader(String name, String value) {
        if (isInclude()) {
            return;
        }
        headerManager.addHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        addHeader(name, Integer.toString(value));
    }

    @Override
    public boolean containsHeader(String name) {
        return headerManager.containsHeader(name);
    }

    @Override
    public String encodeRedirectURL(String url) {
        String result = url;

        if (webApplication.getHttpSessionManager() != null) {
            result = webApplication.getHttpSessionManager().encodeRedirectURL(this, url);
        }

        return result;
    }

    @Deprecated
    @Override
    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }

    @Override
    public String encodeURL(String url) {
        String result = url;
        if (webApplication.getHttpSessionManager() != null) {
            result = webApplication.getHttpSessionManager().encodeURL(this, url);
        }
        return result;
    }

    @Deprecated
    @Override
    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    @Override
    public void flushBuffer() throws IOException {
        if (!isCommitted()) {
            writeOut();
        }
        if (gotWriter) {
            writer.flush();
        }
    }

    @Override
    public String getCharacterEncoding() {
        String result = ISO_8859_1;
        if (characterEncoding != null) {
            result = characterEncoding;
        }
        return result;
    }

    @Override
    public int getBufferSize() {
        return buffer.length;
    }

    /**
     * {@return the content length}
     */
    public long getContentLength() {
        return contentLength;
    }

    @Override
    public String getContentType() {
        if (contentType == null) {
            return null;
        }
        String encoding = characterEncodingSet ? ";charset=" + characterEncoding : "";
        return contentType + encoding;
    }

    @Override
    public String getHeader(String name) {
        return headerManager.getHeader(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        List<String> headerNames = new ArrayList<>();
        Enumeration<String> enumeration = headerManager.getHeaderNames();
        if (enumeration != null) {
            headerNames = list(enumeration);
        }
        return headerNames;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        ArrayList<String> result = new ArrayList<>();
        Enumeration<String> enumeration = headerManager.getHeaders(name);
        if (enumeration != null) {
            result = list(enumeration);
        }
        return result;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (!gotWriter) {
            gotOutput = true;
            return this;
        }
        throw new IllegalStateException("Cannot get output stream as the writer was already acquired");
    }

    @Override
    public int getStatus() {
        return status;
    }

    /**
     * {@return the status message}
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * {@return the web application}
     */
    public WebApplication getWebApplication() {
        return webApplication;
    }

    @Override
    public synchronized PrintWriter getWriter() throws IOException {
        PrintWriter result = null;
        if (!gotOutput) {
            if (gotWriter == false) {
                if (characterEncoding == null || !characterEncodingSet) {
                    setCharacterEncoding(ISO_8859_1);
                }
                gotWriter = true;
                writer = new PrintWriter(new OutputStreamWriter(this, characterEncoding), false);
            }
            result = writer;
        } else {
            throw new IllegalStateException("Cannot get writer as the output stream was already acquired");
        }
        return result;
    }

    /**
     * Is body only.
     *
     * @return true if we are only sending the body, false otherwise.
     */
    public boolean isBodyOnly() {
        return bodyOnly;
    }

    @Override
    public boolean isCommitted() {
        return committed;
    }

    @Override
    public void reset() {
        verifyNotCommitted("reset");
        this.status = 200;
        this.statusMessage = null;
        resetBuffer();
        setErrorMessageAttribute();
    }

    @Override
    public void resetBuffer() {
        bufferResetting = true;
        try {
            if (gotWriter) {
                writer.flush(); // output will be written and ignored.
            }
            this.buffer = new byte[buffer.length];
        } finally {
            bufferResetting = false;
        }
    }

    @Override
    public void sendError(int status) throws IOException {
        verifyNotCommitted("sendError");
        resetBuffer();
        setStatus(status);
    }

    @Override
    public void sendError(int status, String statusMessage) throws IOException {
        verifyNotCommitted("sendError");
        resetBuffer();
        gotWriter = false;
        gotOutput = false;
        setStatus(status);
        this.statusMessage = statusMessage;
        setErrorMessageAttribute();
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        verifyNotCommitted("sendRedirect");
        resetBuffer();
        setStatus(SC_FOUND);
        URL url;
        try {
            url = new URL(location);
        } catch (MalformedURLException mue) {
            HttpServletRequest request = (HttpServletRequest) webApplication.getRequest(this);
            if (location.startsWith("/")) {
                url = new URL(request.getScheme(), request.getServerName(),
                        request.getServerPort(), location);
            } else {
                url = new URL(request.getScheme(), request.getServerName(),
                        request.getServerPort(), request.getContextPath()
                        + "/" + location);
            }
        }
        setHeader("Location", url.toExternalForm());
        flushBuffer();
    }

    /**
     * Set the body only flag.
     *
     * @param bodyOnly flag.
     */
    public void setBodyOnly(boolean bodyOnly) {
        this.bodyOnly = bodyOnly;
    }

    @Override
    public void setBufferSize(int bufferSize) {
        verifyNotCommitted("setBufferSize");
        this.buffer = new byte[bufferSize];
    }

    @Override
    public void setCharacterEncoding(String characterEncoding) {
        if (isInclude()) {
            return;
        }
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

    @Override
    public void setContentLength(int contentLength) {
        setContentLengthLong(contentLength);
    }

    @Override
    public void setContentLengthLong(long contentLength) {
        if (isInclude()) {
            return;
        }
        this.contentLength = contentLength;
        headerManager.addHeader("Content-Length", String.valueOf(contentLength));
    }

    @Override
    public void setContentType(String type) {
        if (isInclude()) {
            return;
        }
        if (!committed) {
            if (type != null) {
                if (type.contains(";")) {
                    contentType = type.substring(0, type.indexOf(";")).trim();
                    if (!gotWriter) {
                        String encoding = type.substring(type.indexOf(";") + 1).trim();
                        if (encoding.contains("=")) {
                            encoding = encoding.substring(encoding.indexOf("=") + 1).trim();
                            setCharacterEncoding(encoding);
                        }
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

    @Override
    public void setDateHeader(String name, long date) {
        setHeader(name, formatDateToGMT(date));
    }

    @Override
    public void setHeader(String name, String value) {
        if (isInclude()) {
            return;
        }
        headerManager.setHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        setHeader(name, Integer.toString(value));
    }

    @Override
    public void setLocale(Locale locale) {
        if (!committed) {
            this.locale = locale;
            this.contentLanguage = locale.toLanguageTag();
            if (webApplication == null) {
                return;
            }
            if (!gotWriter && !characterEncodingSet) {
                LocaleEncodingManager localeEncodingManager = webApplication.getManager(LocaleEncodingManager.class);
                if (localeEncodingManager != null) {
                    String encoding = localeEncodingManager.getCharacterEncoding(locale.toString());
                    if (encoding == null) { 
                        encoding = localeEncodingManager.getCharacterEncoding(locale.getLanguage());
                    }
                    if (encoding != null && !characterEncodingSet) {
                        characterEncoding = encoding;
                    }
                }
            }
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

    @Override
    public void setStatus(int status) {
        if (isInclude()) {
            return;
        }
        if (!isCommitted()) {
            this.status = status;
        }
    }

    @Deprecated
    @Override
    public void setStatus(int status, String statusMessage) {
        setStatus(status);
    }

    @Override
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

    private void setErrorMessageAttribute() {
        if (webApplication != null) {
            ServletRequest request = webApplication.getRequest(this);
            if (request != null) {
                request.setAttribute(RequestDispatcher.ERROR_MESSAGE, statusMessage);
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (!isCommitted()) {
            flush();
        }
    }

    @Override
    public void closeAsyncResponse() {
        responseCloser.run();
    }

    @Override
    public void flush() throws IOException {
        if (bufferResetting) {
            return;
        }
        
        if (!isCommitted()) {
            writeOut();
        }

        outputStream.flush();
    }

    /**
     * Format the timestamp to a GMT string.
     *
     * @param timestamp the timestamp.
     * @return the GMT string.
     */
    private String formatDateToGMT(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("GMT"))
                .format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    @Override
    public Collection<Cookie> getCookies() {
        return cookies;
    }

    @Override
    public OutputStream getUnderlyingOutputStream() {
        return outputStream;
    }

    /**
     * Is this an include dispatch.
     *
     * @return true if it is, false otherwise.
     */
    private boolean isInclude() {
        if (webApplication == null) {
            return false;
        }
        ServletRequest request = webApplication.getRequest(this);
        return request != null && request.getDispatcherType() == DispatcherType.INCLUDE;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public final void setUnderlyingOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void setWriteListener(WriteListener listener) {
    }

    @Override
    public void write(int integer) throws IOException {
        if (bufferResetting) {
            return;
        }
        
        if (index == buffer.length - 1) {
            writeOut();
            outputStream.write(integer);
        } else if (index == buffer.length) {
            outputStream.write(integer);
        } else {
            this.buffer[index] = (byte) integer;
            this.index++;
        }
    }

    /**
     * Write the content language.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeContentLanguage() throws IOException {
        if (contentLanguage == null) {
            return;
        }
        outputStream.write("Content-Language: ".getBytes());
        outputStream.write(contentLanguage.getBytes());
        outputStream.write("\n".getBytes());
    }

    /**
     * Write the content type.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeContentType() throws IOException {
        if (contentType != null) {
            outputStream.write("Content-Type: ".getBytes());
            outputStream.write(contentType.getBytes());
            if (characterEncoding != null) {
                outputStream.write(";charset=".getBytes());
                outputStream.write(characterEncoding.getBytes());
            }
            outputStream.write("\n".getBytes());
        }
    }

    /**
     * Write out a cookie.
     *
     * @param cookie the cookie.
     * @throws IOException when an I/O error occurs.
     */
    private void writeCookie(Cookie cookie) throws IOException {
        outputStream.write("Set-Cookie: ".getBytes());
        outputStream.write(cookie.getName().getBytes());
        outputStream.write("=".getBytes());
        if (cookie.getValue() != null) {
            outputStream.write(cookie.getValue().getBytes());
        }
        if (cookie.getMaxAge() > -1) {
            outputStream.write(("; Max-Age=" + cookie.getMaxAge()).getBytes());
            String expireDate = formatDateToGMT(Instant.now().plusSeconds(cookie.getMaxAge()).toEpochMilli());
            outputStream.write(("; Expires=" + expireDate).getBytes());
        }
        if (cookie.getSecure()) {
            outputStream.write("; Secure".getBytes());
        }
        if (cookie.isHttpOnly()) {
            outputStream.write("; HttpOnly".getBytes());
        }
        if (cookie.getPath() != null) {
            outputStream.write(("; Path=" + cookie.getPath()).getBytes());
        }
        if (cookie.getVersion() > 0) {
            outputStream.write(("; Version=" + cookie.getVersion()).getBytes());
        }
        outputStream.write("\n".getBytes());
    }

    /**
     * Write the cookies.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeCookies() throws IOException {
        for (Cookie cookie : cookies) {
            writeCookie(cookie);
        }
    }

    /**
     * Write out a response header.
     *
     * @param name the name of the header.
     * @throws IOException when an I/O error occurs.
     */
    private void writeHeader(String name) throws IOException {
        Iterator<String> values = getHeaders(name).iterator();
        outputStream.write(name.getBytes());
        outputStream.write(": ".getBytes());
        while (values.hasNext()) {
            String value = values.next();
            if (value != null) {
                outputStream.write(value.getBytes());
                if (values.hasNext()) {
                    outputStream.write(",".getBytes());
                }
            }
        }
        outputStream.write("\n".getBytes());
    }

    @Override
    public void writeHeaders() throws IOException {
        writeContentType();
        writeContentLanguage();
        writeCookies();
        for (String name : getHeaderNames()) {
            writeHeader(name);
        }
        outputStream.write("\n".getBytes());
    }

    /**
     * Write out the status-line, headers and the buffer.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeOut() throws IOException {
        if (!isBodyOnly()) {
            writeStatusLine();
            writeHeaders();
        }
        if (!isCommitted()) {
            outputStream.write(buffer, 0, index);
            index = buffer.length;
        }
        setCommitted(true);
    }

    @Override
    public void writeStatusLine() throws IOException {
        outputStream.write("HTTP/1.1".getBytes());
        outputStream.write(" ".getBytes());
        outputStream.write(Integer.toString(getStatus()).getBytes());
        if (getStatusMessage() != null) {
            outputStream.write(" ".getBytes());
            outputStream.write(getStatusMessage().getBytes());
        }
        outputStream.write("\n".getBytes());
    }

    @Override
    public Runnable getResponseCloser() {
        return responseCloser;
    }

    @Override
    public void setResponseCloser(Runnable responseCloser) {
        this.responseCloser = responseCloser;
    }
}
