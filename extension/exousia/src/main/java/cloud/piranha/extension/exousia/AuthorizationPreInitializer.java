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
package cloud.piranha.extension.exousia;

import java.security.Policy;
import java.util.Set;

import org.glassfish.exousia.AuthorizationService;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.impl.DefaultAuthenticatedIdentity;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * The Exousia initializer.
 *
 * @author Arjan Tijms
 */
public class AuthorizationPreInitializer implements ServletContainerInitializer {

    /**
     * Defines the constant for the auth service attribute.
     */
    public static final String AUTHZ_SERVICE = AuthorizationPreInitializer.class.getName() + ".authz.service";

    /**
     * Defines the constant for the auth factory class attribute.
     */
    public static final String AUTHZ_FACTORY_CLASS = AuthorizationPreInitializer.class.getName() + ".authz.factory.class";

    /**
     * Defines the constant fore the auth policy class attribute.
     */
    public static final String AUTHZ_POLICY_CLASS = AuthorizationPreInitializer.class.getName() + ".authz.module.class";

    /**
     * Defines the constant for the unchecked permissions attribute.
     */
    public static final String UNCHECKED_PERMISSIONS = AuthorizationPreInitializer.class.getName() + ".unchecked.permissions";

    /**
     * Defines the constants for the per-role permissions attribute.
     */
    public static final String PERROLE_PERMISSIONS = AuthorizationPreInitializer.class.getName() + ".perrole.permissions";

    /**
     * Defines the constant for the security constraints attribute.
     */
    public static final String CONSTRAINTS = AuthorizationPreInitializer.class.getName() + ".constraints";

    /**
     * Defines the constant for the security elements attribute.
     */
    public static final String SECURITY_ELEMENTS = AuthorizationPreInitializer.class.getName() + ".security.elements";

    /**
     * Defines the constant for the security annotations attribute.
     */
    public static final String SECURITY_ANNOTATIONS = "cloud.piranha.authorization.exousia.AuthorizationPreInitializer.security.annotations";

    /**
     * Stores the Piranha to Exousia converter.
     */
    PiranhaToExousiaConverter piranhaToExousiaConverter = new PiranhaToExousiaConverter();

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

        // Create the main Exousia authorization service, which implements the various entry points (an SPI)
        // for a runtime to make use of Jakarta Authorization
        createAuthorizationService(context);
    }




    // ### Private methods


    private void createAuthorizationService(WebApplication context) throws ServletException {
        // Gets the authorization module classes that were configured externally
        Class<?> factoryClass = getAttribute(context, AUTHZ_FACTORY_CLASS);
        Class<? extends Policy> policyClass = getAttribute(context, AUTHZ_POLICY_CLASS);

        // No need for the previous policy (likely the Java SE "JavaPolicy") to be consulted.
        Policy.setPolicy(null);

        // Create the main Exousia authorization service, which implements the various entry points (an SPI)
        // for a runtime to make use of Jakarta Authorization
        AuthorizationService authorizationService = new AuthorizationService(
                factoryClass, policyClass,
                context.getServletContextId(),
                DefaultAuthenticatedIdentity::getCurrentSubject,
                new PiranhaPrincipalMapper());

        authorizationService.setRequestSupplier(
            () -> AuthorizationPreFilter.getLocalServletRequest().get());

        context.setAttribute(AUTHZ_SERVICE, authorizationService);
    }






    // ### Utility methods

    private static <T> T getAttribute(ServletContext servletContext, String name) throws ServletException {
        T t = getOptionalAttribute(servletContext, name);
        if (t == null) {
            throw new ServletException("Attribute " + name + " not specified");
        }

        return t;
    }

    private static  <T> T getOptionalAttribute(ServletContext servletContext, String name) {
        @SuppressWarnings("unchecked")
        T t = (T) servletContext.getAttribute(name);

        return t;
    }

}