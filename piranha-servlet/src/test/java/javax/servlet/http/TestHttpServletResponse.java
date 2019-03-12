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
package javax.servlet.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletOutputStream;

/**
 * A Test HttpServletResponse.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpServletResponse extends HttpServletResponseWrapper {
    
    /**
     * Stores the cookies.
     */
    private ArrayList<Cookie> cookies = new ArrayList<>();
    
    /**
     * Stores the headers.
     */
    private HashMap<String, List<String>> headers = new HashMap<>();
    
    /**
     * Stores the Servlet output stream.
     */
    private ServletOutputStream outputStream;
    
    /**
     * Stores the status.
     */
    private int status = 200;
    
    /**
     * Stores the status message.
     */
    private String statusMessage;
    
    /**
     * Constructor.
     * 
     * @param response the response.
     */
    public TestHttpServletResponse(HttpServletResponse response) {
        super(response);
    }
    
    /**
     * Add a a cookie.
     * 
     * @param cookie the cookie.
     */
    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    /**
     * Add a date header.
     * 
     * @param name the name.
     * @param date the date.
     */
    @Override
    public void addDateHeader(String name, long date) {
        addHeader(name, String.valueOf(date));
    }

    /**
     * Add a header.
     * 
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void addHeader(String name, String value) {
        if (headers.containsKey(name)) {
            List<String> values = headers.get(name);
            values.add(value);
        } else {
            List<String> values = new ArrayList<>();
            values.add(value);
            headers.put(name, values);
        }
    }

    /**
     * Add the int header.
     * 
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void addIntHeader(String name, int value) {
        addHeader(name, String.valueOf(value));
    }

    /**
     * Contains the given header.
     * 
     * @param name the name.
     * @return true if it does, false otherwise.
     */
    @Override
    public boolean containsHeader(String name) {
        return headers.containsKey(name);
    }

    /**
     * Encode the url.
     * 
     * @param url the url.
     * @return the url.
     */
    @Override
    public String encodeURL(String url) {
        return url;
    }

    /**
     * Encode the redirect url.
     * 
     * @param url the url.
     * @return the url.
     */
    @Override
    public String encodeRedirectURL(String url) {
        return url;
    }

    /**
     * Get the cookies.
     * 
     * @return the cookies.
     */
    public List<Cookie> getCookies() {
        return cookies;
    }

    /**
     * Get the header.
     * 
     * @param name the name.
     * @return the value.
     */
    @Override
    public String getHeader(String name) {
        return headers.get(name).get(0);
    }

    /**
     * Get the header names.
     * 
     * @return the header names.
     */
    @Override
    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    /**
     * Get the headers.
     * 
     * @param name the name.
     * @return 
     */
    @Override
    public Collection<String> getHeaders(String name) {
        return headers.get(name);
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
     * Send the error.
     * 
     * @param status the status.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendError(int status) throws IOException {
        this.status = status;
    }

    /**
     * Send the error.
     * 
     * @param status the status.
     * @param message the message.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendError(int status, String message) throws IOException {
        this.status = status;
        this.statusMessage = message;
    }

    /**
     * Send a redirect.
     * 
     * @param location the location.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendRedirect(String location) throws IOException {
        this.status = 302;
    }
    
    /**
     * Set the date header.
     * 
     * @param name the name.
     * @param date the date.
     */
    @Override
    public void setDateHeader(String name, long date) {
        setHeader(name, String.valueOf(date));
    }

    /**
     * Set the header.
     * 
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setHeader(String name, String value) {
        List<String> values = new ArrayList<>();
        values.add(String.valueOf(value));
        headers.put(name, values); 
    }

    /**
     * Set the int header.
     * 
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setIntHeader(String name, int value) {
        setHeader(name, String.valueOf(value));
    }

    /**
     * Set the Servlet output stream.
     * 
     * @param outputStream the output stream.
     */
    public void setOutputStream(ServletOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Set the status.
     * 
     * @param status the status.
     */
    @Override
    public void setStatus(int status) {
        this.status = status;
    }
}
