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
package cloud.piranha.security.jakarta;

import static cloud.piranha.security.eleos.AuthenticationInitializer.AUTH_SERVICE;
import static cloud.piranha.security.exousia.AuthorizationPreInitializer.AUTHZ_SERVICE;
import static cloud.piranha.webapp.api.SecurityManager.AuthenticateSource.MID_REQUEST_USER;
import static cloud.piranha.webapp.impl.DefaultAuthenticatedIdentity.getCurrentSubject;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omnifaces.eleos.config.helper.Caller;
import org.omnifaces.eleos.services.DefaultAuthenticationService;
import org.omnifaces.exousia.AuthorizationService;

import cloud.piranha.webapp.api.AuthenticatedIdentity;
import cloud.piranha.webapp.api.SecurityManager;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.impl.DefaultAuthenticatedIdentity;
import cloud.piranha.webapp.impl.DefaultServletEnvironment;
import cloud.piranha.webapp.impl.DefaultWebApplicationRequest;

/**
 * SecurityManager implementation that uses Jakarta Security semantics.
 *
 * WIP!
 *
 * @author Arjan Tijms
 *
 */
public class JakartaSecurityManager implements SecurityManager {

    /**
     * Stores the username password login handler.
     */
    private UsernamePasswordLoginHandler usernamePasswordLoginHandler;

    /**
     * Stores the roles.
     */
    private final Set<String> roles = ConcurrentHashMap.newKeySet();

    @Override
    public void declareRoles(String[] roles) {
        this.roles.addAll(asList(roles));
    }

    @Override
    public void declareRoles(Collection<String> roles) {
        this.roles.addAll(roles);
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    @Override
    public boolean isRequestSecurityAsRequired(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return getAuthorizationService(request).checkWebUserDataPermission(request);
    }

    @Override
    public boolean isRequestedResourcePublic(HttpServletRequest request) {
        return getAuthorizationService(request).checkPublicWebResourcePermission(request);
    }

    @Override
    public boolean isCallerAuthorizedForResource(HttpServletRequest request) {
        return getAuthorizationService(request).checkWebResourcePermission(request);
    }

    @Override
    public boolean isUserInRole(HttpServletRequest request, String role) {
        return getAuthorizationService(request).checkWebRoleRefPermission(getServletName(request), role);
    }

    @Override
    public boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return authenticate(request, response, MID_REQUEST_USER);
    }

    @Override
    public boolean authenticate(HttpServletRequest request, HttpServletResponse response, AuthenticateSource source) throws IOException, ServletException {
        DefaultAuthenticationService authenticationService = (DefaultAuthenticationService) request.getServletContext().getAttribute(AUTH_SERVICE);

        Caller storedCaller = null;

        HttpSession session = request.getSession(false);
        if (session != null) {
            storedCaller = (Caller) session.getAttribute(".caller");
            if (storedCaller != null) {
                DefaultWebApplicationRequest piranhaRequest = (DefaultWebApplicationRequest) request;
                piranhaRequest.setUserPrincipal(new MarkerPrincipal(storedCaller.getName()));
            }
        }

        Caller caller = authenticationService.validateRequest(
                request,
                response,
                source == MID_REQUEST_USER,
                source == MID_REQUEST_USER ? true : !isRequestedResourcePublic(request));

        // Caller is null means authentication failed. If authentication did not happen (auth module decided to do nothing)
        // we have a caller instance with a null caller principal
        if (caller == null) {
            return false;
        }

        if (caller.getCallerPrincipal() instanceof MarkerPrincipal) {
            caller = storedCaller;
        }

        if (authenticationService.mustRegisterSession(request, response)) {
            request.getSession().setAttribute(".caller", caller);
        }

        if (caller != null) {
            setIdentityForCurrentRequest(request, caller.getCallerPrincipal(), caller.getGroups());
        }

        if (caller == null) {
            return false;
        }

        return true;
    }

    @Override
    public void login(HttpServletRequest request, String username, String password) throws ServletException {
        AuthenticatedIdentity resultIdentity = usernamePasswordLoginHandler.login(request, username, password);

        if (resultIdentity == null) {
            throw new ServletException();
        }
        setIdentityForCurrentRequest(request, resultIdentity.getCallerPrincipal(), resultIdentity.getGroups());
    }

    @Override
    public HttpServletRequest getAuthenticatedRequest(HttpServletRequest request, HttpServletResponse response) {
        return getAuthenticationService(request).getWrappedRequestIfSet(request, response);
    }

    @Override
    public HttpServletResponse getAuthenticatedResponse(HttpServletRequest request, HttpServletResponse response) {
        return getAuthenticationService(request).getWrappedResponseIfSet(request, response);
    }

    @Override
    public void postRequestProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        getAuthenticationService(request).secureResponse(request, response);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        getAuthenticationService(request).clearSubject(request, response, getCurrentSubject());

        DefaultAuthenticatedIdentity.clear();
    }

    @Override
    public WebApplication getWebApplication() {
        return null;
    }

    @Override
    public void setWebApplication(WebApplication webApplication) {

    }

    @Override
    public void setUsernamePasswordLoginHandler(UsernamePasswordLoginHandler usernamePasswordLoginHandler) {
        this.usernamePasswordLoginHandler = usernamePasswordLoginHandler;
    }

    protected DefaultAuthenticationService getAuthenticationService(HttpServletRequest request) {
        return (DefaultAuthenticationService) request.getServletContext().getAttribute(AUTH_SERVICE);
    }

    protected AuthorizationService getAuthorizationService(HttpServletRequest request) {
        return (AuthorizationService) request.getServletContext().getAttribute(AUTHZ_SERVICE);
    }

    @Override
    public boolean getDenyUncoveredHttpMethods() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void setIdentityForCurrentRequest(HttpServletRequest request, Principal callerPrincipal, Set<String> groups) {
        Principal currentPrincipal = callerPrincipal == null ? null : callerPrincipal.getName() == null ? null : callerPrincipal;

        DefaultWebApplicationRequest defaultWebApplicationRequest = (DefaultWebApplicationRequest) request;
        defaultWebApplicationRequest.setUserPrincipal(currentPrincipal);

        DefaultAuthenticatedIdentity.setCurrentIdentity(currentPrincipal, groups);
    }

    private String getServletName(HttpServletRequest request) {
        ServletConfig servletConfig = (ServletConfig) request.getAttribute(DefaultServletEnvironment.class.getName());
        if (servletConfig != null && servletConfig.getServletName() != null) {
            return servletConfig.getServletName();
        }

        return "";
    }

    class MarkerPrincipal implements Principal {

        /**
         * Stores the name.
         */
        private final String name;

        /**
         * Constructor.
         *
         * @param name the name.
         */
        public MarkerPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
