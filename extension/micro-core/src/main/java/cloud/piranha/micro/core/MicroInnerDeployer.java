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
package cloud.piranha.micro.core;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
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
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Priority;
import jakarta.annotation.Resource;
import jakarta.annotation.Resources;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.RunAs;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
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

import cloud.piranha.api.Piranha;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.naming.thread.ThreadInitialContextFactory;
import cloud.piranha.resource.shrinkwrap.GlobalArchiveStreamHandler;
import cloud.piranha.resource.shrinkwrap.ShrinkWrapResource;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationExtension;
import cloud.piranha.webapp.impl.DefaultAnnotationManager;
import cloud.piranha.webapp.impl.DefaultAnnotationManager.DefaultAnnotationInfo;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplicationExtensionContext;

/**
 * Deploys a shrinkwrap application archive to a newly started embedded Piranha
 * instance.
 *
 * <p>
 * This class is expected to be run within in its own inner (isolated) class
 * loader
 *
 * @author arjan
 *
 */
public class MicroInnerDeployer {

    /**
     * Defines the attribute name for the MicroPiranha reference.
     */
    static final String MICRO_PIRANHA = "cloud.piranha.micro.MicroPiranha";

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(MicroInnerDeployer.class.getName());

    /**
     * Stores the web annotations.
     */
    String[] webAnnotations = new String[]{
        // Servlet
        WebServlet.class.getName(),
        WebListener.class.getName(),
        WebInitParam.class.getName(),
        WebFilter.class.getName(),
        ServletSecurity.class.getName(),
        MultipartConfig.class.getName(),
        // REST
        "jakarta.ws.rs.Path", //Path.class,
        "jakarta.ws.rs.ext.Provider", // Provider.class,
        "jakarta.ws.rs.ApplicationPath", //  ApplicationPath.class,

        // General
        DeclareRoles.class.getName(), // Not Servlet, but often used on Servlets
        DenyAll.class.getName(),
        PermitAll.class.getName(),
        RolesAllowed.class.getName(),
        RunAs.class.getName(),
        PostConstruct.class.getName(),
        PreDestroy.class.getName(),
        Priority.class.getName(),
        Resource.class.getName(),
        Resources.class.getName(),};

    /**
     * Stores the instances.
     */
    String[] instances = new String[]{
        // REST
        "jakarta.ws.rs.core.Application",};

    /**
     * Stores the HTTP server.
     */
    private HttpServer httpServer;

    /**
     * Start the application.
     *
     * @param applicationArchive the application archive.
     * @param classLoader the classloader.
     * @param handlers the handlers.
     * @param config the configuration.
     * @return the map.
     */
    public Map<String, Object> start(Archive<?> applicationArchive, ClassLoader classLoader, Map<String, Function<URL, URLConnection>> handlers, Map<String, Object> config) {
        try {
            System.getProperties().put(INITIAL_CONTEXT_FACTORY, ThreadInitialContextFactory.class.getName());

            WebApplication webApplication = getWebApplication(applicationArchive, classLoader);

            LOGGER.info(
                    "Starting web application " + applicationArchive.getName() + " on Piranha Micro " + webApplication.getAttribute(MICRO_PIRANHA));

            // The global archive stream handler is set to resolve "shrinkwrap://" URLs (created from strings).
            // Such URLs come into being primarily when code takes resolves a class or resource from the class loader by URL
            // and then takes the string form of the URL representing the class or resource.
            GlobalArchiveStreamHandler streamHandler = new GlobalArchiveStreamHandler(webApplication);

            // Life map to the StaticURLStreamHandlerFactory used by the root class loader
            handlers.put("shrinkwrap", streamHandler::connect);

            // Source of annotations
            Index index = getIndex();

            // Target of annotations
            DefaultAnnotationManager annotationManager = (DefaultAnnotationManager) webApplication.getAnnotationManager();

            // Copy from source index to target manager
            forEachWebAnnotation(webAnnotation
                    -> // Read the web annotations (@WebServlet.class etc) from the source index
                    getAnnotations(index, webAnnotation)
                            // Get the annotation target and annotation instance corresponding to the
                            // (raw/abstract) indexed annotation
                            .map(this::getTarget)
                            .forEach(annotationTarget
                                    -> getAnnotationInstances(annotationTarget, webAnnotation)
                                    .forEach(annotationInstance
                                            -> // Store the matching annotation instance (@WebServlet(name=...)
                                            // and annotation target (@WebServlet public class Target) in the manager
                                            annotationManager.addAnnotation(
                                            new DefaultAnnotationInfo<>(annotationInstance, annotationTarget)))));

            // Collect sub-classes of our "instances" collection
            forEachInstance(instanceClass
                    -> getInstances(index, instanceClass)
                            .map(this::getTarget)
                            .forEach(implementingClass
                                    -> annotationManager.addInstance(instanceClass, implementingClass)));

            initIdentityStore(webApplication);

            String contextPath = (String) config.get("micro.root");
            if (contextPath != null) {
                webApplication.setContextPath(contextPath);
            }

            DefaultWebApplicationExtensionContext extensionContext = new DefaultWebApplicationExtensionContext();
            for (WebApplicationExtension extension : ServiceLoader.load(WebApplicationExtension.class)) {
                extensionContext.add(extension);
            }
            extensionContext.configure(webApplication);

            webApplication.initialize();
            webApplication.start();

            if ((boolean) config.get("micro.http.start")) {
                HttpWebApplicationServer webApplicationServer = new HttpWebApplicationServer();
                webApplicationServer.addWebApplication(webApplication);

                ServiceLoader<HttpServer> httpServers = ServiceLoader.load(HttpServer.class);
                httpServer = httpServers.findFirst().orElseThrow();
                httpServer.setServerPort((Integer) config.get("micro.port"));
                httpServer.setSSL(Boolean.getBoolean("piranha.http.ssl"));
                httpServer.setHttpServerProcessor(webApplicationServer);
                httpServer.start();
            }

            return Map.of(
                    "deployedServlets", webApplication.getServletRegistrations().keySet(),
                    "deployedApplication", new MicroInnerApplication(webApplication));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    WebApplication getWebApplication(Archive<?> archive, ClassLoader newClassLoader) {
        WebApplication webApplication = new DefaultWebApplication();
        webApplication.setClassLoader(newClassLoader);
        webApplication.addResource(new ShrinkWrapResource(archive));

        // Set version
        webApplication.setAttribute(MICRO_PIRANHA, new Piranha() {
            @Override
            public String getVersion() {
                return System.getProperty("micro.version");
            }

            @Override
            public String toString() {
                return getVersion();
            }
        });

        return webApplication;
    }

    /**
     * Stop the application.
     */
    public void stop() {
        if (httpServer != null) {
            httpServer.stop();
        }
    }

    Index getIndex() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream indexStream = classLoader.getResourceAsStream("META-INF/piranha.idx")) {
            return new IndexReader(indexStream).read();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    void forEachWebAnnotation(Consumer<? super Class<?>> consumer) {
        stream(webAnnotations)
                .map(this::toClass)
                .flatMap(Optional::stream)
                .map(e -> (Class<?>) e)
                .forEach(consumer);
    }

    Optional<? super Class<?>> toClass(String className) {
        try {
            return Optional.of(Class.forName(className, true, Thread.currentThread().getContextClassLoader()));
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    Stream<AnnotationInstance> getAnnotations(Index index, Class<?> webAnnotation) {
        return index.getAnnotations(
                createSimple(webAnnotation.getName()))
                .stream();
    }

    Stream<ClassInfo> getInstances(Index index, Class<?> instanceClass) {
        return Stream.concat(
                index.getAllKnownSubclasses(
                        createSimple(instanceClass.getName()))
                        .stream(),
                index.getAllKnownImplementors(
                        createSimple(instanceClass.getName()))
                        .stream());
    }

    Class<?> getTarget(AnnotationInstance annotationInstance) {
        return getTarget(annotationInstance.target());
    }

    Class<?> getTarget(AnnotationTarget target) {
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
        stream(instances)
                .map(this::toClass)
                .flatMap(Optional::stream)
                .map(e -> (Class<?>) e)
                .forEach(consumer);
    }

    void getCallerCredentials(String callersAsXml) {
        if (isEmpty(callersAsXml)) {
            return;
        }

        try {

            XPath xPath = XPathFactory
                    .newInstance()
                    .newXPath();

            NodeList nodes = (NodeList) xPath
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

                InMemoryIdentityStore.addCredential(caller, password, asList(groups.split(",")));
            }

        } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException e) {
            e.printStackTrace();
        }

    }

    void initIdentityStore(WebApplication webApplication) throws IOException {
        String callers = System.getProperty("io.piranha.identitystore.callers");
        if (callers == null) {
            InputStream xmlStream = webApplication.getResourceAsStream("WEB-INF/piranha-callers.xml");
            if (xmlStream != null) {
                callers = new String(xmlStream.readAllBytes(), UTF_8);
            }
        }

        getCallerCredentials(callers);
    }

    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

}
