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
package jakarta.servlet.http;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import jakarta.servlet.ServletResponse;

/**
 * The HttpServletResponse API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpServletResponse extends ServletResponse {

    /**
     * Defines the 100 status code.
     */
    public static final int SC_CONTINUE = 100;

    /**
     * Defines the 101 status code.
     */
    public static final int SC_SWITCHING_PROTOCOLS = 101;

    /**
     * Defines the 200 status code.
     */
    public static final int SC_OK = 200;

    /**
     * Defines the 201 status code.
     */
    public static final int SC_CREATED = 201;

    /**
     * Defines the 202 status code.
     */
    public static final int SC_ACCEPTED = 202;

    /**
     * Defines the 203 status code.
     */
    public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;

    /**
     * Defines the 204 status code.
     */
    public static final int SC_NO_CONTENT = 204;

    /**
     * Defines the 205 status code.
     */
    public static final int SC_RESET_CONTENT = 205;

    /**
     * Defines the 206 status code.
     */
    public static final int SC_PARTIAL_CONTENT = 206;

    /**
     * Defines the 300 status code.
     */
    public static final int SC_MULTIPLE_CHOICES = 300;

    /**
     * Defines the 301 status code.
     */
    public static final int SC_MOVED_PERMANENTLY = 301;

    /**
     * Defines the 302 status code.
     */
    public static final int SC_MOVED_TEMPORARILY = 302;

    /**
     * Defines the 302 status code (recommended variant).
     */
    public static final int SC_FOUND = 302;

    /**
     * Defines the 303 status code.
     */
    public static final int SC_SEE_OTHER = 303;

    /**
     * Defines the 305 status code.
     */
    public static final int SC_NOT_MODIFIED = 304;

    /**
     * Defines the 305 status code.
     */
    public static final int SC_USE_PROXY = 305;

    /**
     * Defines the 307 status code.
     */
    public static final int SC_TEMPORARY_REDIRECT = 307;

    /**
     * Defines the 400 status code.
     */
    public static final int SC_BAD_REQUEST = 400;

    /**
     * Defines the 401 status code.
     */
    public static final int SC_UNAUTHORIZED = 401;

    /**
     * Defines the 402 status code.
     */
    public static final int SC_PAYMENT_REQUIRED = 402;

    /**
     * Defines the 403 status code.
     */
    public static final int SC_FORBIDDEN = 403;

    /**
     * Defines the 404 status code.
     */
    public static final int SC_NOT_FOUND = 404;

    /**
     * Defines the 405 status code.
     */
    public static final int SC_METHOD_NOT_ALLOWED = 405;

    /**
     * Defines the 406 status code.
     */
    public static final int SC_NOT_ACCEPTABLE = 406;

    /**
     * Defines the 407 status code.
     */
    public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;

    /**
     * Defines the 408 status code.
     */
    public static final int SC_REQUEST_TIMEOUT = 408;

    /**
     * Defines the 409 status code.
     */
    public static final int SC_CONFLICT = 409;

    /**
     * Defines the 410 status code.
     */
    public static final int SC_GONE = 410;

    /**
     * Defines the 411 status code.
     */
    public static final int SC_LENGTH_REQUIRED = 411;

    /**
     * Defines the 412 status code.
     */
    public static final int SC_PRECONDITION_FAILED = 412;

    /**
     * Defines the 413 status code.
     */
    public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;

    /**
     * Defines the 414 status code.
     */
    public static final int SC_REQUEST_URI_TOO_LONG = 414;

    /**
     * Defines the 415 status code.
     */
    public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;

    /**
     * Defines the 416 status code.
     */
    public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

    /**
     * Defines the 417 status code.
     */
    public static final int SC_EXPECTATION_FAILED = 417;

    /**
     * Defines the 500 status code.
     */
    public static final int SC_INTERNAL_SERVER_ERROR = 500;

    /**
     * Defines the 501 status code.
     */
    public static final int SC_NOT_IMPLEMENTED = 501;

    /**
     * Defines the 502 status code.
     */
    public static final int SC_BAD_GATEWAY = 502;

    /**
     * Defines the 503 status code.
     */
    public static final int SC_SERVICE_UNAVAILABLE = 503;

    /**
     * Defines the 504 status code.
     */
    public static final int SC_GATEWAY_TIMEOUT = 504;

    /**
     * Defines the 505 status code.
     */
    public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * Adds the cookie.
     *
     * @param cookie the cookie.
     */
    public void addCookie(Cookie cookie);

    /**
     * Add the date header.
     *
     * @param name the name.
     * @param date the date.
     */
    public void addDateHeader(String name, long date);

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     */
    public void addHeader(String name, String value);

    /**
     * Add the intheader.
     *
     * @param name the name.
     * @param value the value.
     */
    public void addIntHeader(String name, int value);

    /**
     * Contains the header.
     *
     * @param name the name.
     * @return true if it contains the given header, false otherwise.
     */
    public boolean containsHeader(String name);

    /**
     * Encode the redirect URL.
     *
     * @param url the redirect URL.
     * @return the encoded redirect URL.
     */
    public String encodeRedirectURL(String url);

    /**
     * Encode the redirect URL.
     *
     * @param url the redirect URL.
     * @return the encoded redirect URL.
     * @deprecated
     */
    @Deprecated
    public String encodeRedirectUrl(String url);

    /**
     * Encode the URL.
     *
     * @param url the URL.
     * @return the encoded URL.
     */
    public String encodeURL(String url);

    /**
     * Encode the URL.
     *
     * @param url the URL.
     * @return the encoded URL.
     * @deprecated
     */
    @Deprecated
    public String encodeUrl(String url);

    /**
     * Get the header.
     *
     * @param name the name.
     * @return the value, or null if not found.
     */
    public String getHeader(String name);

    /**
     * {@return the header names}
     */
    public Collection<String> getHeaderNames();

    /**
     * {@return the headers}
     * @param name the name.
     */
    public Collection<String> getHeaders(String name);

    /**
     * {@return the status}
     */
    public int getStatus();
    
    /**
     * {@return the supplier of trailer fields}
     */
    default Supplier<Map<String, String>> getTrailerFields() {
        return null;
    }

    /**
     * Send the error.
     *
     * @param status the status code.
     * @param message the message.
     * @throws IOException when an I/O error occurs.
     */
    public void sendError(int status, String message) throws IOException;

    /**
     * Sends the error.
     *
     * @param status the status code.
     * @throws IOException when an I/O error occurs.
     */
    public void sendError(int status) throws IOException;

    /**
     * Send a redirect.
     *
     * @param location the location.
     * @throws IOException when an I/O error occurs.
     */
    public void sendRedirect(String location) throws IOException;

    /**
     * Set the date header.
     *
     * @param name the name.
     * @param date the date.
     */
    public void setDateHeader(String name, long date);

    /**
     * Set the header.
     *
     * @param name the name.
     * @param value the value.
     */
    public void setHeader(String name, String value);

    /**
     * Set the int header.
     *
     * @param name the name.
     * @param value the value.
     */
    public void setIntHeader(String name, int value);

    /**
     * Set the status.
     *
     * @param status the status.
     */
    public void setStatus(int status);

    /**
     * Set the status.
     *
     * @param status the status.
     * @param message the message.
     * @deprecated 
     */
    @Deprecated
    public void setStatus(int status, String message);
   
    /**
     * Set the trailer fields supplier.
     * 
     * @param supplier the trailer fields supplier.
     */
    default void setTrailerFields(Supplier<Map<String, String>> supplier) { 
    } 
}
