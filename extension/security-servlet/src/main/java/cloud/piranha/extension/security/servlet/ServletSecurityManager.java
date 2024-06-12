/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.security.servlet;

import static cloud.piranha.extension.epicyro.AuthenticationInitializer.AUTH_SERVICE;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.AUTHZ_SERVICE;
import static cloud.piranha.core.api.SecurityManager.AuthenticateSource.MID_REQUEST_USER;
import static cloud.piranha.core.impl.DefaultAuthenticatedIdentity.getCurrentSubject;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.glassfish.exousia.AuthorizationService;

import cloud.piranha.core.api.AuthenticatedIdentity;
import cloud.piranha.core.api.SecurityManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.impl.DefaultAuthenticatedIdentity;
import cloud.piranha.core.impl.DefaultServletEnvironment;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.glassfish.epicyro.config.helper.Caller;
import org.glassfish.epicyro.services.DefaultAuthenticationService;

/**
 * SecurityManager implementation that uses Servlet Security semantics.
 *
 * @author Arjan Tijms
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletSecurityManager implements SecurityManager {

    /**
     * Stores the auth method.
     */
    protected String authMethod;

    /**
     * Stores if we are denying uncovered HTTP methods.
     */
    protected boolean denyUncoveredHttpMethods;
    
    /**
     * Stores the form error page.
     */
    protected String formErrorPage;
    
    /**
     * Stores the form login page.
     */
    protected String formLoginPage;
    
    /**
     * Stores the realm name.
     */
    protected String realmName;

    /**
     * Stores all declared roles in the application
     */
    protected final Set<String> roles = ConcurrentHashMap.newKeySet();

    /**
     * Handler for the specific HttpServletRequest#login method call
     */
    protected UsernamePasswordLoginHandler usernamePasswordLoginHandler;

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;

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
                WebApplicationRequest webApplicationRequest = (WebApplicationRequest) request;
                webApplicationRequest.setUserPrincipal(new ServletSecurityPrincipal(storedCaller.getName()));
            }
        }

        boolean mandatory = true;
        if (source != MID_REQUEST_USER) {
            mandatory = !isRequestedResourcePublic(request);
        }
        Caller caller = authenticationService.validateRequest(
                request,
                response,
                source == MID_REQUEST_USER,
                mandatory);

        // Caller is null means authentication failed. If authentication did not happen (auth module decided to do nothing)
        // we have a caller instance with a null caller principal
        if (caller == null) {
            return false;
        }

        if (caller.getCallerPrincipal() instanceof ServletSecurityPrincipal) {
            caller = storedCaller;
        }

        if (authenticationService.mustRegisterSession(request, response)) {
            request.getSession().setAttribute(".caller", caller);
        }

        if (caller != null && caller.getCallerPrincipal() != null) {
            setIdentityForCurrentRequest(request, caller.getCallerPrincipal(), caller.getGroups(), "authenticate");
        }

        return caller != null;
    }

    @Override
    public void declareRoles(String[] roles) {
        this.roles.addAll(asList(roles));
    }

    @Override
    public void declareRoles(Collection<String> roles) {
        if (roles == null) {
            return;
        }

        this.roles.addAll(roles);
    }

    @Override
    public String getAuthMethod() {
        return authMethod;
    }

    @Override
    public HttpServletRequest getAuthenticatedRequest(HttpServletRequest request, HttpServletResponse response) {
        return getAuthenticationService(request).getWrappedRequestIfSet(request, response);
    }

    @Override
    public HttpServletResponse getAuthenticatedResponse(HttpServletRequest request, HttpServletResponse response) {
        return getAuthenticationService(request).getWrappedResponseIfSet(request, response);
    }

    protected DefaultAuthenticationService getAuthenticationService(HttpServletRequest request) {
        return (DefaultAuthenticationService) request.getServletContext().getAttribute(AUTH_SERVICE);
    }

    protected AuthorizationService getAuthorizationService(HttpServletRequest request) {
        return (AuthorizationService) request.getServletContext().getAttribute(AUTHZ_SERVICE);
    }

    @Override
    public boolean getDenyUncoveredHttpMethods() {
        return denyUncoveredHttpMethods;
    }

    @Override
    public String getFormErrorPage() {
        return formErrorPage;
    }

    @Override
    public String getFormLoginPage() {
        return formLoginPage;
    }
    
    @Override
    public String getRealmName() {
        return realmName;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    private String getServletName(HttpServletRequest request) {
        ServletConfig servletConfig = (ServletConfig) request.getAttribute(DefaultServletEnvironment.class.getName());
        if (servletConfig != null && servletConfig.getServletName() != null) {
            return servletConfig.getServletName();
        }

        return "";
    }

    @Override
    public boolean isRequestSecurityAsRequired(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (getAuthorizationService(request) != null) {
            return getAuthorizationService(request).checkWebUserDataPermission(request);
        } else {
            return false;
        }
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
    public void login(HttpServletRequest request, String username, String password) throws ServletException {
        AuthenticatedIdentity resultIdentity = usernamePasswordLoginHandler.login(request, username, password);

        if (resultIdentity == null) {
            throw new ServletException();
        }
        setIdentityForCurrentRequest(request, resultIdentity.getCallerPrincipal(), resultIdentity.getGroups(), "login");
    }

    @Override
    public void postRequestProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        getAuthenticationService(request).secureResponse(request, response);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        getAuthenticationService(request).clearSubject(request, response, getCurrentSubject());

        WebApplicationRequest webApplicationRequest = (WebApplicationRequest) request;
        webApplicationRequest.setUserPrincipal(null);
        webApplicationRequest.setAuthType(null);

        DefaultAuthenticatedIdentity.clear();
    }

    @Override
    public WebApplication getWebApplication() {
        return webApplication;
    }

    @Override
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    @Override
    public void setUsernamePasswordLoginHandler(UsernamePasswordLoginHandler usernamePasswordLoginHandler) {
        this.usernamePasswordLoginHandler = usernamePasswordLoginHandler;
    }

    @Override
    public void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods) {
        this.denyUncoveredHttpMethods = denyUncoveredHttpMethods;
    }

    private void setIdentityForCurrentRequest(HttpServletRequest request, Principal callerPrincipal, Set<String> groups, String authType) {
        Principal currentPrincipal = callerPrincipal == null ? null : callerPrincipal.getName() == null ? null : callerPrincipal;

        WebApplicationRequest webApplicationRequest = (WebApplicationRequest) request;
        webApplicationRequest.setUserPrincipal(currentPrincipal);
        if (currentPrincipal != null) {
            webApplicationRequest.setAuthType(authType);
        }

        DefaultAuthenticatedIdentity.setCurrentIdentity(currentPrincipal, groups);
    }

    @Override
    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    @Override
    public void setFormErrorPage(String formErrorPage) {
        this.formErrorPage = formErrorPage;
    }    
    
    @Override
    public void setFormLoginPage(String formLoginPage) {
        this.formLoginPage = formLoginPage;
    }

    @Override
    public void setRealmName(String realmName) {
        this.realmName = realmName;
    }
}
