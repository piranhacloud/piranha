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
package com.manorrock.piranha.arquillian.server;

import static com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.AUTHZ_FACTORY_CLASS;
import static com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.AUTHZ_POLICY_CLASS;

import java.util.HashSet;
import java.util.Set;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.client.protocol.metadata.Servlet;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;
import org.omnifaces.exousia.modules.def.DefaultPolicy;
import org.omnifaces.exousia.modules.def.DefaultPolicyConfigurationFactory;

import com.manorrock.piranha.DefaultHttpServer;
import com.manorrock.piranha.DefaultResourceManager;
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
import com.manorrock.piranha.shrinkwrap.IsolatingResourceManagerClassLoader;
import com.manorrock.piranha.shrinkwrap.ShrinkWrapResource;
import com.manorrock.piranha.weld.WeldInitializer;

/**
 * 
 * @author Arjan Tijms
 *
 */
public class PiranhaServerDeployableContainer implements DeployableContainer<PiranhaServerContainerConfiguration> {
    
    private PiranhaServerContainerConfiguration configuration;
    
    
    private HttpServer httpServer;

    @Override
    public Class<PiranhaServerContainerConfiguration> getConfigurationClass() {
        return PiranhaServerContainerConfiguration.class;
    }
    
    public ProtocolDescription getDefaultProtocol() {
        return new ProtocolDescription("Servlet 4.0");
    }

    @Override
    public void setup(PiranhaServerContainerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void start() throws LifecycleException {
    }
    
    @Override
    public ProtocolMetaData deploy(Archive<?> archive) throws DeploymentException {
        
        Set<String> servletNames = new HashSet<>();
        
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            
            ClassLoader newClassLoader = getWebInfClassLoader(archive);
            
            Thread.currentThread().setContextClassLoader(newClassLoader);
        
            WebApplication webApplication = getWebApplication(archive, newClassLoader);
            
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
            
            servletNames.addAll(webApplication.getServletRegistrations().keySet());
        
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
        
        HTTPContext httpContext = new HTTPContext("localhost", 8080);
        for (String servletName : servletNames) {
            httpContext.add(new Servlet(servletName, "/"));
        }
        
        ProtocolMetaData protocolMetaData = new ProtocolMetaData();
        protocolMetaData.addContext(httpContext);
        
        return protocolMetaData;
    }
    
    WebApplication getWebApplication(Archive<?> archive, ClassLoader newClassLoader) {
        WebApplication webApplication = new DefaultWebApplication();
        webApplication.setClassLoader(newClassLoader);
        webApplication.addResource(new ShrinkWrapResource(archive));
        
        return webApplication;
    }
    
    
    ClassLoader getWebInfClassLoader(Archive<?> archive) {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new ShrinkWrapResource("/WEB-INF/classes", archive));
//        manager.addResource(new ShrinkWrapResource(
//            ShrinkWrap.create(JavaArchive.class)
//                      .addPackages(
//                          true, 
//                          e -> !e.get().contains("arquillian"), 
//                          DefaultWebApplication.class.getPackage())
//                ));
        
        IsolatingResourceManagerClassLoader classLoader = new IsolatingResourceManagerClassLoader();
        classLoader.setResourceManager(manager);
        
        return classLoader;
    }
    
    @Override
    public void deploy(Descriptor descriptor) throws DeploymentException {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public void undeploy(Archive<?> archive) throws DeploymentException {
        httpServer.stop();
    }
    
    @Override
    public void undeploy(Descriptor descriptor) throws DeploymentException {
        
    }

    @Override
    public void stop() throws LifecycleException {
    }

    
}
