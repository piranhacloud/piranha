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
package cloud.piranha.micro;

import static java.util.Arrays.stream;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static javax.xml.xpath.XPathConstants.NODESET;
import static org.jboss.jandex.AnnotationTarget.Kind.CLASS;
import static org.jboss.jandex.DotName.createSimple;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.Index;
import org.jboss.jandex.IndexReader;
import org.jboss.shrinkwrap.api.Archive;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cloud.piranha.DefaultAnnotationManager;
import cloud.piranha.DefaultAnnotationManager.DefaultAnnotationInfo;
import cloud.piranha.http.impl.DefaultHttpServer;
import cloud.piranha.DefaultWebApplication;
import cloud.piranha.appserver.impl.DefaultWebApplicationServer;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.faces.mojarra.MojarraInitializer;
import cloud.piranha.resource.shrinkwrap.GlobalArchiveStreamHandler;
import cloud.piranha.resource.shrinkwrap.ShrinkWrapResource;
import cloud.piranha.rest.jersey.JerseyInitializer;
import cloud.piranha.security.jakarta.JakartaSecurityAllInitializer;
import cloud.piranha.servlet.webservlet.WebServletInitializer;
import cloud.piranha.servlet.webxml.WebXmlInitializer;

/**
 * Deploys a shrinkwrap application archive to a newly started embedded Piranha instance.
 * 
 * @author arjan
 *
 */
public class PiranhaServerDeployer {
    
    Class<?>[] webAnnotations = new Class<?>[] {
       // Servlet
       WebServlet.class, 
       WebListener.class,
       WebInitParam.class,
       WebFilter.class,
       ServletSecurity.class,
       MultipartConfig.class,
       
       // REST
       Path.class, 
       Provider.class, 
       ApplicationPath.class,
    };
    
    Class<?>[] instances = new Class<?>[] {
        // REST
        Application.class,
    };
    
    private HttpServer httpServer;
    
    public Set<String> start(Archive<?> applicationArchive, ClassLoader classLoader, Map<String, Function<URL, URLConnection>> handlers) {
        try {
            System.getProperties().put(INITIAL_CONTEXT_FACTORY, DynamicInitialContextFactory.class.getName());
            
            WebApplication webApplication = getWebApplication(applicationArchive, classLoader);
            
            // TODO: UGLY HACK
            GlobalArchiveStreamHandler streamHandler = new GlobalArchiveStreamHandler(webApplication);
            
            handlers.put("shrinkwrap", e -> streamHandler.connect(e));
            
            // Source of annotations
            Index index = getIndex();
            
            // Target of annotations
            DefaultAnnotationManager annotationManager = (DefaultAnnotationManager) webApplication.getAnnotationManager();
            
            // Copy from source index to target manager
            forEachWebAnnotation(webAnnotation ->
                // Read the web annotations (@WebServlet.class etc) from the source index
                getAnnotations(index, webAnnotation)
                    // Get the annotation target and annotation instance corresponding to the
                    // (raw/abstract) indexed annotation
                    .map(indexedAnnotation -> getTarget(indexedAnnotation))
                    .forEach(annotationTarget -> 
                        getAnnotationInstances(annotationTarget, webAnnotation)
                            .forEach(annotationInstance ->  
                                // Store the matching annotation instance (@WebServlet(name=...)
                                // and annotation target (@WebServlet public class Target) in the manager
                                annotationManager.addAnnotation(
                                    new DefaultAnnotationInfo<>(annotationInstance,  annotationTarget)))));
            
            
            forEachInstance(instanceClass ->
                getInstances(index, instanceClass)
                    .map(indexedInstance -> getTarget(indexedInstance))
                    .forEach(implementingClass -> 
                        annotationManager.addInstance(instanceClass, implementingClass)));
                        
            
            getCallerCredentials(System.getProperty("io.piranha.identitystore.callers"));
            
            DefaultWebApplicationServer webApplicationServer = new cloud.piranha.appserver.impl.DefaultWebApplicationServer();
            webApplicationServer.addWebApplication(webApplication);
            
            webApplication.addInitializer(new WebXmlInitializer());
            webApplication.addInitializer(new WebServletInitializer());
            
            webApplication.addInitializer(JakartaSecurityAllInitializer.class.getName());
            webApplication.addInitializer(JerseyInitializer.class.getName());
            webApplication.addInitializer(MojarraInitializer.class.getName());
            
            webApplicationServer.initialize();
            webApplicationServer.start();
            
            httpServer = new DefaultHttpServer(9090, webApplicationServer);
            httpServer.start();
            
            return webApplication.getServletRegistrations().keySet();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    WebApplication getWebApplication(Archive<?> archive, ClassLoader newClassLoader) {
        WebApplication webApplication = new DefaultWebApplication();
        webApplication.setClassLoader(newClassLoader);
        webApplication.addResource(new ShrinkWrapResource(archive));
        
        return webApplication;
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop();
        }
    }
    
    Index getIndex() {
        ClassLoader classLoader= Thread.currentThread().getContextClassLoader();
        
        try (InputStream indexStream = classLoader.getResourceAsStream("META-INF/piranha.idx")) {
            return new IndexReader(indexStream).read();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    
    void forEachWebAnnotation(Consumer<? super Class<?>> consumer) {
        stream(webAnnotations).forEach(consumer);
    }
    
    Stream<AnnotationInstance> getAnnotations(Index index, Class<?> webAnnotation) {
        return 
            index.getAnnotations(
                    createSimple(webAnnotation.getName()))
                 .stream();
    }
    
    Stream<ClassInfo> getInstances(Index index, Class<?> instanceClass) {
        return
            Stream.concat(
                index.getAllKnownSubclasses(
                        createSimple(instanceClass.getName()))
                     .stream(),
                 index.getAllKnownImplementors(
                         createSimple(instanceClass.getName()))
                      .stream());
    }
    
    Class<?> getTarget (AnnotationInstance annotationInstance) {
        return getTarget(annotationInstance.target());
    }
    
    Class<?> getTarget (AnnotationTarget target) {
        try {
            if (target.kind() == CLASS) {
                return Class.forName(
                    target.asClass().toString(), true, 
                    Thread.currentThread().getContextClassLoader());
            }
            
            return Class.forName(
                target.asMethod().declaringClass().toString(), true,
                Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    Stream<Annotation> getAnnotationInstances(Class<?> target, Class<?> annotationType) {
        return stream(target.getAnnotations())
                .filter(e -> e.annotationType().isAssignableFrom(annotationType));
    }
    
    void forEachInstance(Consumer<? super Class<?>> consumer) {
        stream(instances).forEach(consumer);
    }
    
    void getCallerCredentials(String callersAsXml) {
        if (callersAsXml == null || callersAsXml.isEmpty()) {
            return;
        }
        
        try {
            
            XPath xPath = XPathFactory
            .newInstance()
            .newXPath();
            
            NodeList nodes = (NodeList) 
                xPath
                    .evaluate(
                        "//caller", 
                        DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .parse(new ByteArrayInputStream(callersAsXml.getBytes())), 
                        NODESET);
            
            for (int i = 0; i < nodes.getLength(); i++) {
                NamedNodeMap callerAttributes = nodes.item(i).getAttributes();
                
                String caller = callerAttributes.getNamedItem("callername").getNodeValue();
                String password = callerAttributes.getNamedItem("password").getNodeValue(); 
                String groups = callerAttributes.getNamedItem("groups").getNodeValue();
                
                InMemmoryIdentityStore.addCredential(caller, password, Arrays.asList(groups.split(",")));
            }
        
        } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
