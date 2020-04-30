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
package cloud.piranha.webapp.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The SecurityManager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms
 */
public interface SecurityManager {

    enum AuthenticateSource {
        /**
         * The container / runtime calls authenticate before a request
         */
        PRE_REQUEST_CONTAINER,

        /**
         * The user (code) has programmatically called authenticate
         */
        MID_REQUEST_USER
    }

    /**
     * Method that bypasses the authentication mechanism installed by the authentication manager and directly invokes an
     * identity store.
     */
    interface UsernamePasswordLoginHandler {
        AuthenticatedIdentity login(HttpServletRequest request, String username, String password);
    }

    /**
     * Get if we are denying uncovered HTTP methods.
     * 
     * @return true if we are, false otherwise.
     */
    boolean getDenyUncoveredHttpMethods();
    
    /**
     * Check if the current request adheres to the user data constraint, if any.
     * 
     * <p>
     * In practice this means checking if HTTPS is used when so required by the application.
     *
     * @param request the request.
     * @param response the response.
     * @return true if request adheres to constraints, false otherwise
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    default boolean isRequestSecurityAsRequired(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return true;
    }

    /**
     * Check if the requested resource, represented by the request, is public or not.
     * 
     * @param request the request.
     * @return true if the requested resource can be accessed by public (unauthenticated) callers, otherwise false
     */
    default boolean isRequestedResourcePublic(HttpServletRequest request) {
        return true;
    }

    /**
     * Check if the current caller (which can be the anonymous caller) is authorized to access the requested resource.
     * 
     * <p>
     * If the unauthenticated caller is authorized, then this means the resource is public (aka unconstrained, aka
     * unchecked), and the outcome of this method MUST be consistend with
     * {@link #isRequestedResourcePublic(HttpServletRequest)}.
     * 
     * 
     * @param request the request.
     * @return true if the current caller is allowed to access the requested resource, false otherwise
     */
    default boolean isCallerAuthorizedForResource(HttpServletRequest request) {
        return true;
    }

    /**
     * Authenticate the request.
     *
     * @param request the request.
     * @param response the response.
     * @return true if authenticated.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    /**
     * Gets the request object the security system wants to put in place.
     * 
     * <p>
     * This method allows the security system (or authentication module being delegated to) a custom or, more likely,
     * wrapped request.
     * 
     * @param request the request.
     * @param response the response.
     * @return a request object that the runtime should put into service
     */
    default HttpServletRequest getAuthenticatedRequest(HttpServletRequest request, HttpServletResponse response) {
        return request;
    }

    /**
     * Gets the response object the security system wants to put in place.
     * 
     * <p>
     * This method allows the security system (or authentication module being delegated to) a custom or, more likely,
     * wrapped response.
     * 
     * @param request the request.
     * @param response the response.
     * @return a response object that the runtime should put into service
     */
    default HttpServletResponse getAuthenticatedResponse(HttpServletRequest request, HttpServletResponse response) {
        return response;
    }

    /**
     * Authenticate the request.
     *
     * @param request the request.
     * @param response the response.
     * @param source the source or moment from where this authenticate method is called
     * @return true if authenticated.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    default boolean authenticate(HttpServletRequest request, HttpServletResponse response, AuthenticateSource source) throws IOException, ServletException {
        // By default, source and mandatory directive are ignored, and semantics for the 2 parameter
        // version hold.
        // The 2 parameter version is expected to be essentially source = MID_REQUEST_USER
        return authenticate(request, response);
    }

    /**
     * Gives the security system the opportunity to process the response after the request (after the target resource has
     * been invoked).
     * 
     * <p>
     * Although this may be rare to used in practice, it allows for encryption of the response, inserting security tokens,
     * signing the response, etc.
     * 
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    default void postRequestProcess(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    /**
     * Declare roles.
     *
     * @param roles the roles.
     */
    void declareRoles(String[] roles);

    /**
     * Get the web application.
     *
     * @return the web application.
     */
    WebApplication getWebApplication();

    /**
     * Is the user in the specific role.
     *
     * @param request the request.
     * @param role the role.
     * @return true if in the role, false otherwise.
     */
    boolean isUserInRole(HttpServletRequest request, String role);

    /**
     * Login.
     *
     * @param request the request.
     * @param username the username.
     * @param password the password.
     * @throws ServletException when unable to login.
     */
    void login(HttpServletRequest request, String username, String password) throws ServletException;

    /**
     * Logout.
     *
     * @param request the request.
     * @param response the response.
     * @throws ServletException when a servlet error occurs.
     */
    void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    /**
     * Set if we are denying uncovered HTTP methods.
     * 
     * @param denyUncoveredHttpMethods the boolean value.
     */
    void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods);
    
    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    void setWebApplication(WebApplication webApplication);

    /**
     * Set the handler that may be used by the login method to contact an identity store.
     * 
     * @param usernamePasswordLoginHandler the handler
     */
    default void setUsernamePasswordLoginHandler(UsernamePasswordLoginHandler usernamePasswordLoginHandler) {
        // do nothing, optional method
    }
}
