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
package com.manorrock.piranha.authentication.elios;

import static com.manorrock.piranha.authentication.elios.AuthenticationInitializer.AUTH_SERVICE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.eleos.config.helper.Caller;
import org.omnifaces.eleos.services.DefaultAuthenticationService;

import com.manorrock.piranha.DefaultAuthenticatedIdentity;
import com.manorrock.piranha.DefaultWebApplicationRequest;
import com.manorrock.piranha.api.SecurityManager;
import com.manorrock.piranha.api.WebApplication;

/**
 * SecurityManager implementation that uses Jakarta Security semantics.
 * 
 * WIP!
 * 
 * @author Arjan Tijms
 *
 */
public class JakartaSecurityManager implements SecurityManager {
    
    @Override
    public void declareRoles(String[] roles) {
        
    }

    @Override
    public boolean authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        DefaultAuthenticationService authenticationService = (DefaultAuthenticationService) request.getServletContext().getAttribute(AUTH_SERVICE);
        
        Caller caller = authenticationService.validateRequest(request, response, true, e -> true);
        if (caller == null) {
            return false;
        }
        
        DefaultWebApplicationRequest defaultWebApplicationRequest = (DefaultWebApplicationRequest) request;
        defaultWebApplicationRequest.setUserPrincipal(caller.getCallerPrincipal());
        
        DefaultAuthenticatedIdentity.setCurrentIdentity(caller.getCallerPrincipal(), caller.getGroups());
        
        // TODO: handle the "in progress" (send_continue) case
        return true;
    }

    @Override
    public boolean isUserInRole(HttpServletRequest request, String role) {
        
        // TMP delegate to authorization manager later
        
        return DefaultAuthenticatedIdentity
            .getCurrentIdentity()
            .getGroups().stream()
            .anyMatch(e -> e.equals(role));
    }

    @Override
    public void login(HttpServletRequest request, String username, String password) throws ServletException {
    }

    @Override
    public void logout(HttpServletRequest request) throws ServletException {
        
    }
    
    @Override
    public WebApplication getWebApplication() {
        return null;
    }

    @Override
    public void setWebApplication(WebApplication webApplication) {
        
    }

}
