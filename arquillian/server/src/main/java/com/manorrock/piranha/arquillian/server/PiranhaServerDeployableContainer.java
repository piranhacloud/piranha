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

import java.lang.reflect.InvocationTargetException;
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
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import com.manorrock.piranha.DefaultResourceManager;
import com.manorrock.piranha.shrinkwrap.IsolatingResourceManagerClassLoader;
import com.manorrock.piranha.shrinkwrap.ShrinkWrapResource;

/**
 * 
 * @author Arjan Tijms
 *
 */
public class PiranhaServerDeployableContainer implements DeployableContainer<PiranhaServerContainerConfiguration> {
    
    private PiranhaServerContainerConfiguration configuration;
    

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
            
            // Resolve all the dependencies that make up a Piranha runtime configuration
            JavaArchive[] piranhaArchives = 
                Maven.configureResolver()
                     //.workOffline()
                     .resolve(
                        "org.jboss:jandex:2.1.1.Final",
                        "com.manorrock.piranha:piranha-authentication-eleos:19.11.0-SNAPSHOT",
                        "com.manorrock.piranha:piranha:19.11.0-SNAPSHOT",
                        "com.manorrock.piranha:piranha-servlet:19.11.0-SNAPSHOT",
                        "com.manorrock.piranha:piranha-runner-war:19.11.0-SNAPSHOT",
                        "com.manorrock.piranha:piranha-authorization-exousia:19.11.0-SNAPSHOT",
                        "com.manorrock.piranha:piranha-security-jakarta:19.11.0-SNAPSHOT",
                        "com.manorrock.piranha:piranha-security-soteria:19.11.0-SNAPSHOT",
                        "com.manorrock.piranha:piranha-cdi-weld:19.11.0-SNAPSHOT")
                     .withTransitivity()
                     .as(JavaArchive.class);

            // Make all those dependencies available to the Piranha class loader
            ClassLoader piranhaClassLoader = getPiranhaClassLoader(piranhaArchives);
            
            // Make the web application archive (the .war) available to a separate classloader
            ClassLoader newClassLoader = getWebInfClassLoader(archive, piranhaClassLoader);
            
            Thread.currentThread().setContextClassLoader(newClassLoader);
        
            Object foo = 
                Class.forName(
                        "com.manorrock.piranha.runner.war.PiranhaServerDeployer", 
                        true,
                        newClassLoader)
                    .newInstance();
            
            foo.getClass().getMethod("start", Archive.class, ClassLoader.class)
                          .invoke(foo,archive, piranhaClassLoader);
            
        
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new DeploymentException("", e);
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
    
    ClassLoader getPiranhaClassLoader(Archive<?>[] piranhaArchives) {
        DefaultResourceManager manager = new DefaultResourceManager();
        
        for (Archive<?> archive : piranhaArchives) {
            manager.addResource(new ShrinkWrapResource(archive));
        }
        
        IsolatingResourceManagerClassLoader classLoader = new IsolatingResourceManagerClassLoader();
        classLoader.setResourceManager(manager);
        
        return classLoader;
        
    }
    
    ClassLoader getWebInfClassLoader(Archive<?> applicationArchive, ClassLoader piranhaClassloader) {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new ShrinkWrapResource("/WEB-INF/classes", applicationArchive));
        IsolatingResourceManagerClassLoader classLoader = new IsolatingResourceManagerClassLoader(piranhaClassloader);
        classLoader.setResourceManager(manager);
        
        return classLoader;
    }
    
    @Override
    public void deploy(Descriptor descriptor) throws DeploymentException {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public void undeploy(Archive<?> archive) throws DeploymentException {
        // httpServer.stop();
    }
    
    @Override
    public void undeploy(Descriptor descriptor) throws DeploymentException {
        
    }

    @Override
    public void stop() throws LifecycleException {
    }

    
}
