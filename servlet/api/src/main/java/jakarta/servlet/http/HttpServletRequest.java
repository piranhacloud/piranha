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
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;

/**
 * The HttpServletRequest API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpServletRequest extends ServletRequest {

    /**
     * Defines the BASIC_AUTH constant.
     */
    public static final String BASIC_AUTH = "BASIC";

    /**
     * Defines the CLIENT_CERT constant.
     */
    public static final String CLIENT_CERT_AUTH = "CLIENT_CERT";

    /**
     * Defines the DIGEST constant.
     */
    public static final String DIGEST_AUTH = "DIGEST";

    /**
     * Defines the FORM constant.
     */
    public static final String FORM_AUTH = "FORM";

    /**
     * Authenticate the request.
     *
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     * @return true if authenticated, false otherwise.
     */
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException;

    /**
     * Change the session id.
     *
     * @return the new session id.
     */
    public String changeSessionId();

    /**
     * Get the auth type.
     *
     * @return the auth type.
     */
    public String getAuthType();

    /**
     * Get the context path.
     *
     * @return the context path.
     */
    public String getContextPath();

    /**
     * Get the cookies.
     *
     * @return the cookies, or null if none.
     */
    public Cookie[] getCookies();

    /**
     * Get the date header.
     *
     * @param name the name.
     * @return the date, or -1 if not found.
     */
    public long getDateHeader(String name);

    /**
     * Get the header.
     *
     * @param name the name.
     * @return the header, or null if not found.
     */
    public String getHeader(String name);

    /**
     * Get the headers.
     *
     * @param name the name.
     * @return the headers.
     */
    public Enumeration<String> getHeaders(String name);

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    public Enumeration<String> getHeaderNames();

    /**
     * Get the HTTP servlet mapping.
     *
     * @return the HTTP servlet mapping.
     */
    default HttpServletMapping getHttpServletMapping() {
        return new HttpServletMapping() {
            @Override
            public String getMatchValue() {
                return "";
            }

            @Override
            public String getPattern() {
                return "";
            }

            @Override
            public String getServletName() {
                return "";
            }

            @Override
            public MappingMatch getMappingMatch() {
                return null;
            }
        };
    }

    /**
     * Get the int header.
     *
     * @param name the name.
     * @return the header, or -1 if not found.
     */
    public int getIntHeader(String name);

    /**
     * Get the HTTP method.
     *
     * @return the HTTP method.
     */
    public String getMethod();

    /**
     * Get the part.
     *
     * @param name the name.
     * @return the part, or null if not found.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    public Part getPart(String name) throws IOException, ServletException;

    /**
     * Get the parts.
     *
     * @return the parts.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    public Collection<Part> getParts() throws IOException, ServletException;

    /**
     * Get the path info.
     *
     * @return the path info.
     */
    public String getPathInfo();

    /**
     * Get the path translated.
     *
     * @return the path translated.
     */
    public String getPathTranslated();

    /**
     * Get the query string.
     *
     * @return the query string, or null if not found.
     */
    public String getQueryString();

    /**
     * Get the remote user.
     *
     * @return the remote user, or null if not found.
     */
    public String getRemoteUser();

    /**
     * Get the request URI.
     *
     * @return the request URI.
     */
    public String getRequestURI();

    /**
     * Get the request URL.
     *
     * @return the request URL.
     */
    public StringBuffer getRequestURL();

    /**
     * Get the requested session id.
     *
     * @return the requested session id.
     */
    public String getRequestedSessionId();

    /**
     * Get the servlet path.
     *
     * @return the servlet path.
     */
    public String getServletPath();

    /**
     * Get the HTTP session.
     *
     * @param create the create flag.
     * @return the HTTP session, or null if not found and create flag is false.
     */
    public HttpSession getSession(boolean create);

    /**
     * Get the HTTP session.
     *
     * @return the HTTP session.
     */
    public HttpSession getSession();

    /**
     * Get the trailer fields.
     *
     * @return the trailer fields.
     */
    default Map<String, String> getTrailerFields() {
        return Collections.emptyMap();
    }

    /**
     * Get the user principal.
     *
     * @return the user principal.
     */
    public Principal getUserPrincipal();

    /**
     * Is the requested session id from a cookie.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isRequestedSessionIdFromCookie();

    /**
     * Is the requested session id from a URL.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isRequestedSessionIdFromURL();

    /**
     * Is the requested session id from a URL.
     *
     * @return true if it is, false otherwise.
     * @deprecated
     */
    @Deprecated
    public boolean isRequestedSessionIdFromUrl();

    /**
     * Is the requested session id valid.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isRequestedSessionIdValid();

    /**
     * Is trailer fields ready.
     *
     * @return true if fields are ready, false otherwise.
     */
    default boolean isTrailerFieldsReady() {
        return true;
    }

    /**
     * Is the user in the specified role.
     *
     * @param role the role.
     * @return true if the user is in the specified role, false otherwise.
     */
    public boolean isUserInRole(String role);

    /**
     * Login.
     *
     * @param username the username.
     * @param password the password.
     * @throws ServletException when a Servlet error occurs.
     */
    public void login(String username, String password) throws ServletException;

    /**
     * Logout.
     *
     * @throws ServletException when a Servlet error occurs.
     */
    public void logout() throws ServletException;

    /**
     * Create a new push builder.
     * 
     * @return the push builder.
     */
    default PushBuilder newPushBuilder() {
        return null;
    }

    /**
     * Upgrade the request.
     *
     * @param <T> the type of HTTP upgrade handler.
     * @param handlerClass the handler class.
     * @return the HTTP upgrade handler.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     *
     */
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException;
}
