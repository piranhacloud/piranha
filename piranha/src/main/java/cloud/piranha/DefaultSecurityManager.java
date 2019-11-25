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
package cloud.piranha;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import cloud.piranha.api.SecurityManager;
import cloud.piranha.api.WebApplication;

/**
 * The default SecurityManager.
 *
 * <p>
 This security manager implies the use of DefaultWebApplicationRequest, if your
 server / web application does not want to use DefaultWebApplicationRequest or
 subclass DefaultWebApplicationRequest you have to implement your own security
 manager.
 </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultSecurityManager implements SecurityManager {

    /**
     * Stores the logins.
     */
    protected final HashMap<String, String> logins = new HashMap<>();

    /**
     * Stores the roles.
     */
    protected ArrayList<String> roles = new ArrayList<>();

    /**
     * Stores the user roles.
     */
    protected final HashMap<String, String[]> userRoles = new HashMap<>();

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;

    /**
     * Add user.
     *
     * @param username the username.
     * @param password the password.
     * @param roles the roles.
     */
    public void addUser(String username, String password, String... roles) {
        logins.put(username, password);
        userRoles.put(username, roles);
    }

    /**
     * Authenticate the request.
     *
     * @param request the request.
     * @param response the response.
     * @return true if authenticated, false otherwise.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public boolean authenticate(
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        boolean result = false;

        if (request.getAuthType() != null) {
            if (request.getAuthType().equals(HttpServletRequest.BASIC_AUTH)) {
                throw new ServletException("Basic auth is not supported");
            }

            if (request.getAuthType().equals(HttpServletRequest.CLIENT_CERT_AUTH)) {
                throw new ServletException("Client cert auth is not supported");
            }

            if (request.getAuthType().equals(HttpServletRequest.DIGEST_AUTH)) {
                throw new ServletException("Digest auth is not supported");
            }

            if (request.getAuthType().equals(HttpServletRequest.FORM_AUTH)) {
                String username = request.getParameter("j_username");
                String password = request.getParameter("j_password");
                login(request, username, password);
                if (request.getUserPrincipal() != null) {
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * Declare roles.
     *
     * @param roles the roles.
     */
    @Override
    public void declareRoles(String[] roles) {
        this.roles.addAll(Arrays.asList(roles));
    }

    /**
     * Get the web application.
     *
     * @return the web application.
     */
    @Override
    public WebApplication getWebApplication() {
        return webApplication;
    }

    /**
     * Is the user in the given role.
     *
     * @param request the request.
     * @param role the role.
     * @return true if in the role, false otherwise.
     */
    @Override
    public boolean isUserInRole(HttpServletRequest request, String role) {
        boolean result = false;

        if (request.getRemoteUser() != null) {
            String foundRoles[] = userRoles.get(request.getRemoteUser());
            if (foundRoles != null) {
                for (String foundRole : foundRoles) {
                    if (foundRole.equals(role)) {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Login with the given username and password.
     *
     * @param request the servlet request.
     * @param username the username.
     * @param password the password.
     * @throws ServletException when a serious error occurs.
     */
    @Override
    public void login(HttpServletRequest request, String username, String password) throws ServletException {

        if (logins.containsKey(username) && password != null && password.equals(logins.get(username))) {
            DefaultWebApplicationRequest abstractRequest;

            while (request instanceof HttpServletRequestWrapper) {
                HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper) request;
                request = (HttpServletRequest) wrapper.getRequest();
            }
            abstractRequest = (DefaultWebApplicationRequest) request;
            abstractRequest.setUserPrincipal(new DefaultSecurityPrincipal(username));
        } else {
            throw new ServletException("Unable to login using the given username and password");
        }
    }

    /**
     * Logout.
     *
     * @param request the request.
     * @throws ServletException when a serious error occurs.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    }

    /**
     * Remove the given user.
     *
     * @param username the username.
     */
    public void removeUser(String username) {
        logins.remove(username);
        userRoles.remove(username);
    }

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    @Override
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }
}
