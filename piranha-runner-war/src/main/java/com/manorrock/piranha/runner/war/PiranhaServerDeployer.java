/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha.runner.war;

import static com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.AUTHZ_FACTORY_CLASS;
import static com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.AUTHZ_POLICY_CLASS;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;

import java.util.Set;

import org.jboss.shrinkwrap.api.Archive;
import org.omnifaces.exousia.modules.def.DefaultPolicy;
import org.omnifaces.exousia.modules.def.DefaultPolicyConfigurationFactory;

import com.manorrock.piranha.DefaultHttpServer;
import com.manorrock.piranha.DefaultWebApplication;
import com.manorrock.piranha.DefaultWebApplicationServer;
import com.manorrock.piranha.api.HttpServer;
import com.manorrock.piranha.api.WebApplication;
import com.manorrock.piranha.authentication.elios.AuthenticationInitializer;
import com.manorrock.piranha.authorization.exousia.AuthorizationInitializer;
import com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer;
import com.manorrock.piranha.security.jakarta.JakartaSecurityInitializer;
import com.manorrock.piranha.security.soteria.SoteriaInitializer;
import com.manorrock.piranha.servlet.ServletFeature;
import com.manorrock.piranha.shrinkwrap.ShrinkWrapResource;
import com.manorrock.piranha.weld.WeldInitializer;

/**
 * Deploys a shrinkwrap application archive to a newly started embedded Piranha instance.
 * 
 * @author arjan
 *
 */
public class PiranhaServerDeployer {
    
    private HttpServer httpServer;
    
    public Set<String> start(Archive<?> applicationArchive, ClassLoader classLoader) {
        
        System.getProperties().put(INITIAL_CONTEXT_FACTORY, DynamicInitialContextFactory.class.getName());
        
        WebApplication webApplication = getWebApplication(applicationArchive, classLoader);
        
        DefaultWebApplicationServer webApplicationServer = new DefaultWebApplicationServer();
        webApplication.addFeature(new ServletFeature());
        webApplicationServer.addWebApplication(webApplication);
        
        webApplication.addInitializer(WeldInitializer.class.getName());
        
        webApplication.setAttribute(AUTHZ_FACTORY_CLASS, DefaultPolicyConfigurationFactory.class);
        webApplication.setAttribute(AUTHZ_POLICY_CLASS, DefaultPolicy.class);
        
        webApplication.addInitializer(AuthorizationPreInitializer.class.getName());
        webApplication.addInitializer(AuthenticationInitializer.class.getName());
        webApplication.addInitializer(AuthorizationInitializer.class.getName());
        webApplication.addInitializer(JakartaSecurityInitializer.class.getName());
        
        webApplication.addInitializer(SoteriaInitializer.class.getName());
        
        webApplicationServer.initialize();
        webApplicationServer.start();
        
        httpServer = new DefaultHttpServer(8080, webApplicationServer);
        httpServer.start();
        
        return webApplication.getServletRegistrations().keySet();
    }
    
    WebApplication getWebApplication(Archive<?> archive, ClassLoader newClassLoader) {
        WebApplication webApplication = new DefaultWebApplication();
        webApplication.setClassLoader(newClassLoader);
        webApplication.addResource(new ShrinkWrapResource(archive));
        
        return webApplication;
    }

    public void stop() {
        httpServer.stop();
    }
    
}
