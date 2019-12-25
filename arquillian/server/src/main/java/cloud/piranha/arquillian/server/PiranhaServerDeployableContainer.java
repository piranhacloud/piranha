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
package cloud.piranha.arquillian.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.client.protocol.metadata.Servlet;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexWriter;
import org.jboss.jandex.Indexer;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import cloud.piranha.DefaultResourceManager;
import cloud.piranha.resource.shrinkwrap.IsolatingResourceManagerClassLoader;
import cloud.piranha.resource.shrinkwrap.ShrinkWrapResource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Piranha Arquillian connector.
 * 
 * <p>
 * This connector will start up and embedded Piranha runtime in an isolated class loader for every application
 * that is deployed.
 * 
 * @author Arjan Tijms
 *
 */
public class PiranhaServerDeployableContainer implements DeployableContainer<PiranhaServerContainerConfiguration> {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(PiranhaServerDeployableContainer.class.getName());
    
    private PiranhaServerContainerConfiguration configuration;
    private Object piranhaServerDeployer;
    

    @Override
    public Class<PiranhaServerContainerConfiguration> getConfigurationClass() {
        return PiranhaServerContainerConfiguration.class;
    }
    
    public ProtocolDescription getDefaultProtocol() {
        return new ProtocolDescription("Servlet 3.0");
    }

    @Override
    public void setup(PiranhaServerContainerConfiguration configuration) {
        this.configuration = configuration;
        configuration.validate();
    }

    @Override
    public void start() throws LifecycleException {
        // We don't start Piranha separately. Start and Deploy is one step.
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public ProtocolMetaData deploy(Archive<?> archive) throws DeploymentException {
        
        Set<String> servletNames = new HashSet<>();
        
        if (!archive.contains("WEB-INF/beans.xml")) {
            archive.add(EmptyAsset.INSTANCE, "WEB-INF/beans.xml");
        }
        
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            
            // Resolve all the dependencies that make up a Piranha runtime configuration
            
            ConfigurableMavenResolverSystem mavenResolver = Maven.configureResolver();
            
            configuration.getRepositoriesList().stream().forEach(repo ->
                mavenResolver.withRemoteRepo(UUID.randomUUID().toString(), repo, "default"));
            
            JavaArchive[] piranhaArchives = 
                mavenResolver
                     .workOffline(configuration.isOffline())
                     .resolve(configuration.getMergedDependencies())
                     .withTransitivity()
                     .as(JavaArchive.class);

            // Make all those dependencies available to the Piranha class loader
            ClassLoader piranhaClassLoader = getPiranhaClassLoader(piranhaArchives);
            
            // Make the web application archive (the .war) available to a separate classloader
            // The webInfClassLoader delegates to the Piranha class loader.
            
            // The class loading hierarchy looks as follows:
            
            // Web-inf class loader (application classes)
            //        |
            //        |--- System class loader (Pass-through for Shrinkwrap classes only)
            //        |--- java.lang.ClassLoader (super class, Weld, Javasist etc hack-in their classes here) 
            //        |
            // Piranha class loader (Piranha classes)
            //        |
            //        |
            // Platform class loader (JDK classes)
            
            ClassLoader webInfClassLoader = getWebInfClassLoader(archive, piranhaClassLoader);
            
            Thread.currentThread().setContextClassLoader(webInfClassLoader);
        
            piranhaServerDeployer = 
                Class.forName(
                        "cloud.piranha.micro.PiranhaServerDeployer", 
                        true,
                        webInfClassLoader)
                    .newInstance();
            
            servletNames.addAll((Set<String>) 
                piranhaServerDeployer
                    .getClass()
                    .getMethod("start", Archive.class, ClassLoader.class)
                    .invoke(piranhaServerDeployer, archive, webInfClassLoader));
        
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new DeploymentException("", e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
        
        HTTPContext httpContext = new HTTPContext("localhost", 9090);
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
        
        IsolatingResourceManagerClassLoader classLoader = new IsolatingResourceManagerClassLoader("Piranha Loader");
        classLoader.setResourceManager(manager);
        
        return classLoader;
    }
    
    ClassLoader getWebInfClassLoader(Archive<?> applicationArchive, ClassLoader piranhaClassloader) {
        // Add the main application archive to this class loader, and set the classes to be loaded from the
        // /WEB-INF/classes folder
        ShrinkWrapResource applicationResource = new ShrinkWrapResource("/WEB-INF/classes", applicationArchive);
        
        // Create a separate archive that contains an index of the application archive. This index
        // can be obtained from the class loader by getting the "META-INF/piranha.idx" resource.
        ShrinkWrapResource indexResource = new ShrinkWrapResource(
            ShrinkWrap.create(JavaArchive.class)
                      .add(new ByteArrayAsset(createIndex(applicationResource)), "META-INF/piranha.idx"));
        
        
        IsolatingResourceManagerClassLoader classLoader = new IsolatingResourceManagerClassLoader(piranhaClassloader, "WebInf Loader");
        
        // Add the resources representing the application archive and index archive to the resource manager
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(applicationResource);
        manager.addResource(indexResource);
        
        // Make the application classes and the index available to the class loader by setting the resource manager
        // that contains these.
        classLoader.setResourceManager(manager);
        
        return classLoader;
    }
    
    private byte[] createIndex(ShrinkWrapResource applicationResource) {
        Indexer indexer = new Indexer();
        
        // Add all classes from the application resource to the indexer
        applicationResource
            .getAllLocations()
            .filter(e -> e.endsWith(".class"))
            .forEach(className -> addToIndex(className, applicationResource, indexer));
        
        
        Index index = indexer.complete();
        
        // Write the index out to a byte array
        
        ByteArrayOutputStream indexBytes = new ByteArrayOutputStream();
        
        IndexWriter writer = new IndexWriter(indexBytes);
        
        try {
            writer.write(index);
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Unable to write out index", ioe);
            }
        }
        
        return indexBytes.toByteArray();
    }
    
    private void addToIndex(String className, ShrinkWrapResource resource, Indexer indexer) {
        try (InputStream classAsStream = resource.getResourceAsStream(className)) {
            indexer.index(classAsStream);
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Unable to add to index", ioe);
            }
        }
    }
    
    @Override
    public void deploy(Descriptor descriptor) throws DeploymentException {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    @Override
    public void undeploy(Archive<?> archive) throws DeploymentException {
        try {
            piranhaServerDeployer
                .getClass()
                .getMethod("stop")
                .invoke(piranhaServerDeployer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error occurred during undeploy", e);
            }
        }
    }
    
    @Override
    public void undeploy(Descriptor descriptor) throws DeploymentException {
        
    }

    @Override
    public void stop() throws LifecycleException {
     // We don't stop Piranha separately. Stop and Undeploy is one step.
    }

    
}
