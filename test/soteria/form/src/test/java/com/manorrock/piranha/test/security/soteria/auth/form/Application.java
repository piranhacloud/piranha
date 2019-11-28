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
package com.manorrock.piranha.test.security.soteria.auth.form;

import static cloud.piranha.security.exousia.AuthorizationPreInitializer.AUTHZ_FACTORY_CLASS;
import static cloud.piranha.security.exousia.AuthorizationPreInitializer.AUTHZ_POLICY_CLASS;
import static cloud.piranha.security.exousia.AuthorizationPreInitializer.CONSTRAINTS;
import static cloud.piranha.builder.WebApplicationBuilder.newWebApplication;
import static java.util.Arrays.asList;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;

import javax.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;

import org.omnifaces.exousia.constraints.SecurityConstraint;
import org.omnifaces.exousia.modules.def.DefaultPolicy;
import org.omnifaces.exousia.modules.def.DefaultPolicyConfigurationFactory;

import com.manorrock.piranha.test.utils.TestWebApp;

import cloud.piranha.security.elios.AuthenticationInitializer;
import cloud.piranha.security.exousia.AuthorizationInitializer;
import cloud.piranha.security.exousia.AuthorizationPreInitializer;
import cloud.piranha.security.jakarta.JakartaSecurityInitializer;
import cloud.piranha.security.soteria.SoteriaInitializer;

import cloud.piranha.cdi.weld.WeldInitializer;

/**
 * @author Arjan Tijms
 */
@FormAuthenticationMechanismDefinition(
    loginToContinue = @LoginToContinue(
        loginPage="/login-page",
        errorPage="/error-page"))
public class Application {
    
    public static TestWebApp get() {
        System.getProperties().put(INITIAL_CONTEXT_FACTORY, DynamicInitialContextFactory.class.getName());
        
        return 
            new TestWebApp(newWebApplication()
                .addInitializer(WeldInitializer.class)
                    
                .addAttribute(AUTHZ_FACTORY_CLASS, DefaultPolicyConfigurationFactory.class)
                .addAttribute(AUTHZ_POLICY_CLASS, DefaultPolicy.class)
                .addAttribute(CONSTRAINTS, asList(
                    new SecurityConstraint("/protected/servlet", "architect")))
                
                .addInitializer(AuthorizationPreInitializer.class)
                .addInitializer(AuthenticationInitializer.class)
                .addInitializer(AuthorizationInitializer.class)
                .addInitializer(JakartaSecurityInitializer.class)
                
                .addInitializer(SoteriaInitializer.class)
                
                .addServlet(ProtectedServlet.class, "/protected/servlet")
                .addServlet(ErrorPageServlet.class, "/error-page")
                .addServlet(LoginPageServlet.class, "/login-page")
                .start());
    }

}
