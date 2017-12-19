/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletResponseWrapper;

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
    @Override
    public void setStatus(int status, String message) {
        throw new UnsupportedOperationException();
    }
}
