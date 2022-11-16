/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.LocaleEncodingManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.api.WebApplicationOutputStream;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.list;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The default WebApplicationResponse.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationResponse implements WebApplicationResponse {

    /**
     * Defines the 'ISO-8859-1' constant.
     */
    private static final String ISO_8859_1 = "ISO-8859-1";
    
    /**
     * Stores the body only flag.
     */
    protected boolean bodyOnly;

    /**
     * Stores the buffer resetting flag.
     */
    protected boolean bufferResetting;

    /**
     * Stores the character encoding.
     */
    protected String characterEncoding;
    
    /**
     * Stores if the character encoding was set using setLocale.
     */
    protected boolean characterEncodingLocaleSet;

    /**
     * Stores if the character encoding has been set manually.
     */
    protected boolean characterEncodingSet;

    /**
     * Stores the committed flag.
     */
    protected boolean committed;

    /**
     * Stores the content language
     */
    protected String contentLanguage;

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
     * Stores the response closer.
     */
    protected Runnable responseCloser;

    /**
     * Stores the status code.
     */
    protected int status;

    /**
     * Stores the status message.
     */
    protected String statusMessage;
    
    /**
     * Stores the trailer fields supplier.
     */
    protected Supplier<Map<String,String>> trailerFields;

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;

    /**
     * Stores the web application output stream.
     */
    protected WebApplicationOutputStream webApplicationOutputStream;

    /**
     * Stores the writer.
     */
    protected PrintWriter writer;

    /**
     * Constructor.
     */
    public DefaultWebApplicationResponse() {
        characterEncoding = ISO_8859_1;
        characterEncodingLocaleSet = false;
        characterEncodingSet = false;
        committed = false;
        contentType = null;
        contentTypeSet = false;
        cookies = new ArrayList<>();
        gotOutput = false;
        gotWriter = false;
        headerManager = new DefaultHttpHeaderManager();
        locale = Locale.getDefault();
        responseCloser = new Runnable() {
            @Override
            public void run() {
            }
        };
        status = 200;
        statusMessage = null;
        webApplication = null;
        webApplicationOutputStream = new DefaultWebApplicationOutputStream();
        webApplicationOutputStream.setResponse(this);
        writer = null;
    }

    @Override
    public void addCookie(Cookie cookie) {
        /*
         * Servlet:SPEC:192.4
         */
        verifyNotCommitted("addCookie");
        this.cookies.add(cookie);
    }

    @Override
    public void addDateHeader(String name, long date) {
        addHeader(name, formatDateToGMT(date));
    }

    @Override
    public void addHeader(String name, String value) {
        if (isCommitted()) {
            return;
        }
        /*
         * Servlet:SPEC:192.4
         */
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
    public void closeAsyncResponse() {
        responseCloser.run();
    }

    @Override
    public boolean containsHeader(String name) {
        return headerManager.containsHeader(name);
    }

    @Override
    public String encodeRedirectURL(String url) {
        String result = url;
        if (webApplication.getManager().getHttpSessionManager() != null) {
            result = webApplication.getManager().getHttpSessionManager().encodeRedirectURL(this, url);
        }
        return result;
    }

    @Override
    public String encodeURL(String url) {
        String result = url;
        if (webApplication.getManager().getHttpSessionManager() != null) {
            result = webApplication.getManager().getHttpSessionManager().encodeURL(this, url);
        }
        return result;
    }
    
    @Override
    public void flushBuffer() throws IOException {
        /*
         * Servlet:SPEC:192.3
         */
        if (!isCommitted()) {
            webApplicationOutputStream.flushBuffer();
            setCommitted(true);
        }
        if (gotWriter) {
            writer.flush();
        }
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
    public int getBufferSize() {
        return webApplicationOutputStream.getBufferSize();
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
    public String getContentLanguage() {
        return contentLanguage;
    }

    @Override
    public String getContentType() {
        if (contentType == null) {
            return null;
        }
        String encoding = (characterEncodingLocaleSet || characterEncodingSet) ? ";charset=" + characterEncoding : "";
        return contentType + encoding;
    }

    @Override
    public Collection<Cookie> getCookies() {
        return cookies;
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
            return webApplicationOutputStream;
        }
        throw new IllegalStateException("Cannot get output stream as the writer was already acquired");
    }

    @Override
    public Runnable getResponseCloser() {
        return responseCloser;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }
    
    @Override
    public Supplier<Map<String,String>> getTrailerFields() {
        return trailerFields;
    }

    @Override
    public WebApplication getWebApplication() {
        return webApplication;
    }
    
    @Override
    public WebApplicationOutputStream getWebApplicationOutputStream() {
        return webApplicationOutputStream;
    }

    @Override
    public synchronized PrintWriter getWriter() throws IOException {
        PrintWriter result = null;
        if (!gotOutput) {
            if (!gotWriter) {
                if (characterEncoding == null || !(characterEncodingSet || characterEncodingLocaleSet)) {
                    setCharacterEncoding(ISO_8859_1);
                }
                gotWriter = true;
                writer = new PrintWriter(new OutputStreamWriter(webApplicationOutputStream, characterEncoding), false);
            }
            result = writer;
        } else {
            throw new IllegalStateException("Cannot get writer as the output stream was already acquired");
        }
        return result;
    }

    @Override
    public boolean isBodyOnly() {
        return bodyOnly;
    }

    @Override
    public boolean isBufferResetting() {
        return bufferResetting;
    }

    @Override
    public boolean isCommitted() {
        return committed;
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
    public void reset() {
        verifyNotCommitted("reset");
        characterEncoding = ISO_8859_1;
        characterEncodingSet = false;
        characterEncodingLocaleSet = false;
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
        resetBuffer();
        setErrorMessageAttribute();
    }

    @Override
    public void resetBuffer() {
        if (!committed) {
            bufferResetting = true;
            try {
                if (gotWriter) {
                    /*
                     * Flush the writer to get rid of any pending output in the
                     * writer which will be discarded as the response is in 
                     * buffer resetting mode.
                     */
                    writer.flush(); 
                }
                webApplicationOutputStream.resetBuffer();
            } finally {
                bufferResetting = false;
            }
        } else {
            throw new IllegalStateException("Buffer already committed, not resetting");
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
        if (gotWriter) {
            writer.close();
        }
    }

    @Override
    public void setBodyOnly(boolean bodyOnly) {
        this.bodyOnly = bodyOnly;
    }

    @Override
    public void setBufferSize(int bufferSize) {
        verifyNotCommitted("setBufferSize");
        webApplicationOutputStream.setBufferSize(bufferSize);
    }

    @Override
    public void setCharacterEncoding(String characterEncoding) {
        /*
         * Servlet:SPEC:192.4
         */
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
        /*
         * Servlet:SPEC:192.4
         */
        if (isInclude()) {
            return;
        }
        this.contentLength = contentLength;
        headerManager.addHeader("Content-Length", String.valueOf(contentLength));
    }

    @Override
    public void setContentType(String type) {
        /*
         * Servlet:SPEC:192.4
         */
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

    /**
     * Set the error message attribute on the request.
     */
    private void setErrorMessageAttribute() {
        if (webApplication != null) {
            ServletRequest request = webApplication.getRequest(this);
            if (request != null) {
                request.setAttribute(RequestDispatcher.ERROR_MESSAGE, statusMessage);
            }
        }
    }

    @Override
    public void setHeader(String name, String value) {
        /*
         * Servlet:SPEC:192.4
         */
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
                LocaleEncodingManager localeEncodingManager = webApplication.getManager().getLocaleEncodingManager();
                if (localeEncodingManager != null) {
                    String encoding = localeEncodingManager.getCharacterEncoding(locale.toString());
                    if (encoding == null) { 
                        encoding = localeEncodingManager.getCharacterEncoding(locale.getLanguage());
                    }
                    if (encoding != null && !characterEncodingSet) {
                        characterEncoding = encoding;
                        characterEncodingLocaleSet = true;
                    }
                }
            }
        }
    }

    @Override
    public void setResponseCloser(Runnable responseCloser) {
        this.responseCloser = responseCloser;
    }

    @Override
    public void setStatus(int status) {
        /*
         * Servlet:SPEC:192.4
         */
        if (isInclude()) {
            return;
        }
        if (!isCommitted()) {
            this.status = status;
        }
    }

    @Override
    public void setTrailerFields(Supplier<Map<String,String>> trailerFields) {
        if (isCommitted()) {
            throw new IllegalStateException("Response is already committed");
        }
        HttpServletRequest httpServletRequest = (HttpServletRequest) webApplication.getRequest(this);
        if (httpServletRequest == null) {
            throw new IllegalStateException("Not supported on this type of ServletRequest");
        }
        if (httpServletRequest.getProtocol().equalsIgnoreCase("HTTP/1.0")) {
            throw new IllegalStateException("Not supported with HTTP/1.0 protocol");
        }
        this.trailerFields = trailerFields;
    }

    @Override
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    @Override
    public void setWebApplicationOutputStream(WebApplicationOutputStream outputStream) {
        this.webApplicationOutputStream = outputStream;
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
