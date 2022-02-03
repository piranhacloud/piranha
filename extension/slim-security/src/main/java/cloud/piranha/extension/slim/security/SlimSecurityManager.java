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
package cloud.piranha.extension.slim.security;

import cloud.piranha.core.api.SecurityManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The SlimSecurityManager. 
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SlimSecurityManager implements SecurityManager {
    
    /**
     * Stores the denyUncoveredHttpMethods flag.
     */
    protected boolean denyUncoveredHttpMethods;

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
        if (roles != null) {
            userRoles.put(username, roles);
        }
    }

    /**
     * Add the user roles.
     *
     * @param username the username.
     * @param roles the roles.
     */
    public void addUserRole(String username, String... roles) {
        if (roles != null) {
            userRoles.put(username, roles);
        }
    }

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

    @Override
    public void declareRoles(String[] roles) {
        this.roles.addAll(Arrays.asList(roles));
    }

    @Override
    public boolean getDenyUncoveredHttpMethods() {
        return denyUncoveredHttpMethods;
    }

    @Override
    public Set<String> getRoles() {
        return new HashSet<>(roles);
    }

    @Override
    public WebApplication getWebApplication() {
        return webApplication;
    }

    @Override
    public boolean isUserInRole(HttpServletRequest request, String role) {
        boolean result = false;

        if (request.getRemoteUser() != null) {
            String[] foundRoles= userRoles.get(request.getRemoteUser());
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

    @Override
    public void login(HttpServletRequest request, String username, String password) throws ServletException {

        if (logins.containsKey(username) && password != null && password.equals(logins.get(username))) {
            while (request instanceof HttpServletRequestWrapper wrapper) {
                request = (HttpServletRequest) wrapper.getRequest();
            }
            if (request instanceof WebApplicationRequest webAppRequest) {
                webAppRequest.setUserPrincipal(new SlimSecurityManagerPrincipal(username));
            }
        } else {
            throw new ServletException("Unable to login using the given username and password");
        }
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (request.getUserPrincipal() != null) {
            while (request instanceof HttpServletRequestWrapper wrapper) {
                request = (HttpServletRequest) wrapper.getRequest();
            }
            if (request instanceof WebApplicationRequest webAppRequest) {
                webAppRequest.setUserPrincipal(null);
            }
        } else {
            throw new ServletException("Unable to logout as user is not logged in");
        }
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
    
    @Override
    public void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods) {
        this.denyUncoveredHttpMethods = denyUncoveredHttpMethods;
    }

    @Override
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }
}
