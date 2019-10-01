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
package com.manorrock.piranha.authorization.exousia;

import static com.manorrock.piranha.authorization.exousia.AuthorizationPreFilter.localServletRequest;

import java.security.Permission;
import java.security.Policy;
import java.util.List;
import java.util.Set;

import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyContextException;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.omnifaces.exousia.AuthorizationService;

import com.manorrock.piranha.DefaultAuthenticatedIdentity;
import com.manorrock.piranha.api.AuthenticatedIdentity;
import com.manorrock.piranha.api.WebApplication;

/**
 * The Exousia initializer.
 * 
 * @author Arjan Tijms
 */
public class AuthorizationPreInitializer implements ServletContainerInitializer {
    
    public final static String AUTHZ_SERVICE = AuthorizationPreInitializer.class.getName() + ".authz.service";
    
    public final static String AUTHZ_FACTORY_CLASS = AuthorizationPreInitializer.class.getName() + ".authz.factory.class";
    public final static String AUTHZ_POLICY_CLASS = AuthorizationPreInitializer.class.getName() + ".authz.module.class";
    
    public final static String UNCHECKED_PERMISSIONS = AuthorizationPreInitializer.class.getName() + ".unchecked.permissions";

    
    /**
     * Initialize Exousia
     * 
     * @param classes the classes.
     * @param servletContext the Servlet context.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        
        WebApplication context = (WebApplication) servletContext;
        
        // Gets the authorization module classes that were configured externally
        Class<?> factoryClass = getAttribute(servletContext, AUTHZ_FACTORY_CLASS);
        Class<? extends Policy> policyClass = getAttribute(servletContext, AUTHZ_POLICY_CLASS);
        
        AuthorizationService authorizationService = new AuthorizationService(
            factoryClass, 
            policyClass, context.getServletContextId(), 
            () -> localServletRequest.get(),
            () -> DefaultAuthenticatedIdentity.getCurrentSubject());
        
        PolicyConfiguration policyConfiguration = authorizationService.getPolicyConfiguration();
        
        try {
            List<Permission> unchecked = getOptionalAttribute(servletContext, UNCHECKED_PERMISSIONS);
            if (unchecked != null) {
                for (Permission permission : unchecked) {
                    policyConfiguration.addToUncheckedPolicy(permission);
                }
            }
    
            policyConfiguration.commit();
        } catch (PolicyContextException e) {
            throw new IllegalStateException(e);
        }
        
        servletContext.setAttribute(AUTHZ_SERVICE, authorizationService);
        servletContext.addFilter(AuthorizationPreFilter.class.getSimpleName(), AuthorizationPreFilter.class);
        
        // TMP - should use Dynamic
        context.addFilterMapping(AuthorizationPreFilter.class.getSimpleName(), "/*");
    }
    
    public <T> T getAttribute(ServletContext servletContext, String name) throws ServletException {
        T t = getOptionalAttribute(servletContext, name);
        if (t == null) {
            throw new ServletException("Attribute " + t + " not specified");
        }
        
        return t;
    }
    
    public <T> T getOptionalAttribute(ServletContext servletContext, String name) throws ServletException {
        @SuppressWarnings("unchecked")
        T t = (T) servletContext.getAttribute(name);
                
        return t;
    }

}
