/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.security.eleos;

import static jakarta.security.auth.message.config.AuthConfigFactory.DEFAULT_FACTORY_SECURITY_PROPERTY;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omnifaces.eleos.config.factory.ConfigParser;
import org.omnifaces.eleos.config.factory.DefaultConfigFactory;
import org.omnifaces.eleos.config.factory.DefaultConfigParser;
import org.omnifaces.eleos.services.DefaultAuthenticationService;

import cloud.piranha.webapp.api.WebApplication;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * The Eleos initializer.
 *
 * @author Arjan Tijms
 */
public class AuthenticationInitializer implements ServletContainerInitializer {

    /**
     * Stores the auth module class name.
     */
    public static final String AUTH_MODULE_CLASS = AuthenticationInitializer.class.getName() + ".auth.module.class";

    /**
     * Stores the auth service name.
     */
    public static final String AUTH_SERVICE = AuthenticationInitializer.class.getName() + ".auth.service";

    /**
     * Initialize Eleos
     *
     * @param classes the classes.
     * @param servletContext the Servlet context.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        
        System.out.println("*** AuthenticationInitializer ***");

        // Gets the authentication module class that was configured externally
        Class<?> authModuleClass = (Class<?>) servletContext.getAttribute(AUTH_MODULE_CLASS);
        if (authModuleClass == null) {
            authModuleClass = DoNothingServerAuthModule.class;
        }

        String appContextId = servletContext.getVirtualServerName() + " " + servletContext.getContextPath();

        // This sets the authentication factory to the default factory. This factory stores and retrieves
        // the authentication artifacts.
        Security.setProperty(DEFAULT_FACTORY_SECURITY_PROPERTY, DefaultConfigFactory.class.getName());

        // Defines the modules that we have available. Here it's only a single fixed module.
        ConfigParser configParser = new DefaultConfigParser(authModuleClass);

        // Indicates the module we want to use
        Map<String, Object> options = new HashMap<>();
        options.put("authModuleId", authModuleClass.getSimpleName());

        // This authentication service installs an authentication config provider in the default factory, which
        // is the one we setup above. This authentication config provider uses the passed-in configParser to
        // retrieve configuration for authentication modules from.
        DefaultAuthenticationService authenticationService = new DefaultAuthenticationService(appContextId, options, configParser, null);

        servletContext.setAttribute(AUTH_SERVICE, authenticationService);

        FilterRegistration.Dynamic dynamic = servletContext.addFilter(AuthenticationFilter.class.getSimpleName(), AuthenticationFilter.class);

        dynamic.setAsyncSupported(true);

        ((WebApplication) servletContext).addFilterMapping(AuthenticationFilter.class.getSimpleName(), "/*");
    }

}
