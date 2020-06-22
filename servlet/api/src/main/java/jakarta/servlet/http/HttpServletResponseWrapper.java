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
package jakarta.servlet.http;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletResponseWrapper;

/**
 * The HttpServletResponseWrapper API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpServletResponseWrapper extends ServletResponseWrapper implements HttpServletResponse {

    /**
     * Constructor.
     *
     * @param response the HTTP servlet response.
     */
    public HttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    /**
     * Add the cookie.
     *
     * @param cookie the cookie.
     */
    @Override
    public void addCookie(Cookie cookie) {
        getWrapped().addCookie(cookie);
    }

    /**
     * Add the date header.
     *
     * @param name the name.
     * @param date the date.
     */
    @Override
    public void addDateHeader(String name, long date) {
        getWrapped().addDateHeader(name, date);
    }

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void addHeader(String name, String value) {
        getWrapped().addHeader(name, value);
    }

    /**
     * Add the int header.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void addIntHeader(String name, int value) {
        getWrapped().addIntHeader(name, value);
    }

    /**
     * Contains the header.
     *
     * @param name the name.
     * @return true if it contains the given header, false otherwise.
     */
    @Override
    public boolean containsHeader(String name) {
        return getWrapped().containsHeader(name);
    }

    /**
     * Encode the redirect URL.
     *
     * @param url the redirect URL.
     * @return the encoded redirect URL.
     */
    @Override
    public String encodeRedirectURL(String url) {
        return getWrapped().encodeRedirectURL(url);
    }

    /**
     * Encode the redirect URL.
     *
     * @param url the redirect URL.
     * @return the encoded redirect URL.
     * @deprecated
     */
    @Deprecated
    @Override
    public String encodeRedirectUrl(String url) {
        throw new UnsupportedOperationException();
    }

    /**
     * Encode the URL.
     *
     * @param url the URL.
     * @return the encoded URL.
     */
    @Override
    public String encodeURL(String url) {
        return getWrapped().encodeURL(url);
    }

    /**
     * Encode the URL.
     *
     * @param url the URL.
     * @return the encoded URL.
     * @deprecated
     */
    @Deprecated
    @Override
    public String encodeUrl(String url) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the header.
     *
     * @param name the name.
     * @return the header, or null if not found.
     */
    @Override
    public String getHeader(String name) {
        return getWrapped().getHeader(name);
    }

    /**
     * Get the headers.
     *
     * @param name the name.
     * @return the headers.
     */
    @Override
    public Collection<String> getHeaders(String name) {
        return getWrapped().getHeaders(name);
    }

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    @Override
    public Collection<String> getHeaderNames() {
        return getWrapped().getHeaderNames();
    }

    /**
     * Get the status.
     *
     * @return the status.
     */
    @Override
    public int getStatus() {
        return getWrapped().getStatus();
    }

    /**
     * Get the wrapped response.
     *
     * @return the wrapped response.
     */
    private HttpServletResponse getWrapped() {
        return (HttpServletResponse) super.getResponse();
    }

    /**
     * Send an error.
     *
     * @param status the status code.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendError(int status) throws IOException {
        getWrapped().sendError(status);
    }

    /**
     * Send an error.
     *
     * @param status the status code.
     * @param message the message.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendError(int status, String message) throws IOException {
        getWrapped().sendError(status, message);
    }

    /**
     * Send a redirect.
     *
     * @param location the location.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void sendRedirect(String location) throws IOException {
        getWrapped().sendRedirect(location);
    }

    /**
     * Set the date header.
     *
     * @param name the name.
     * @param date the date.
     */
    @Override
    public void setDateHeader(String name, long date) {
        getWrapped().setDateHeader(name, date);
    }

    /**
     * Set the header.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setHeader(String name, String value) {
        getWrapped().setHeader(name, value);
    }

    /**
     * Set the int header.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setIntHeader(String name, int value) {
        getWrapped().setIntHeader(name, value);
    }

    /**
     * Set the status.
     *
     * @param status the status.
     */
    @Override
    public void setStatus(int status) {
        getWrapped().setStatus(status);
    }

    /**
     * Set the status.
     *
     * @param status the status code.
     * @param message the status message.
     * @deprecated
     */
    @Deprecated
    @Override
    public void setStatus(int status, String message) {
        throw new UnsupportedOperationException();
    }
}
