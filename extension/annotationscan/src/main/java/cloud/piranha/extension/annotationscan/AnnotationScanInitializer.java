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
package cloud.piranha.extension.annotationscan;

import cloud.piranha.resource.api.ResourceManagerClassLoader;
import cloud.piranha.webapp.api.AnnotationManager;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.impl.DefaultAnnotationInfo;
import cloud.piranha.webapp.impl.DefaultAnnotationManager;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.WARNING;
import java.lang.annotation.Annotation;
import static java.util.Arrays.stream;
import java.util.Set;
import java.util.stream.Stream;

/**
 * This ServletContainerInitializer deep scans for annotations and adds them to
 * the DefaultAnnotationManager.
 *
 * @author Arjan Tijms
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AnnotationScanInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(AnnotationScanInitializer.class.getName());

    /**
     * On startup.
     *
     * @param classes the classes.
     * @param servletContext the servlet context.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        WebApplication webApp = (WebApplication) servletContext;

        AnnotationManager annotationManager = webApp.getAnnotationManager();
        if (annotationManager instanceof DefaultAnnotationManager == false) {
            LOGGER.log(WARNING, "DefaultAnnotationManager not installed. This scanner does not work");
            return;
        }

        DefaultAnnotationManager defaultAnnotationManager = (DefaultAnnotationManager) annotationManager;

        ClassLoader classLoader = webApp.getClassLoader();
        if (classLoader instanceof ResourceManagerClassLoader == false) {
            LOGGER.log(WARNING, "ResourceManagerClassLoader not installed. This scanner does not work");
            return;
        }

        ResourceManagerClassLoader resourceManagerClassLoader = (ResourceManagerClassLoader) classLoader;

        resourceManagerClassLoader
                .getResourceManager()
                .getAllLocations()
                .filter(e -> e.endsWith(".class") && !e.endsWith("module-info.class"))
                .map(e -> loadClass(classLoader, e))
                .filter(this::hasWebAnnotation)
                .forEach(targetClazz -> getWebAnnotations(targetClazz)
                .forEach(annotationInstance
                        -> defaultAnnotationManager.addAnnotation(
                        new DefaultAnnotationInfo<>(annotationInstance, targetClazz))));
    }

    /**
     * Load the class using the given class loader.
     *
     * @param classLoader the class loader.
     * @param className the class name.
     * @return the class.
     */
    public Class<?> loadClass(ClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(
                    className.replace("/", ".")
                            .substring(1, className.length() - ".class".length()));
        } catch (ClassNotFoundException e) {
        }
        return Object.class;
    }

    /**
     * Get the web annotations for the given class.
     *
     * @param clazz the class.
     * @return the stream of web annotations.
     */
    private Stream<Annotation> getWebAnnotations(Class<?> clazz) {
        return stream(clazz.getAnnotations())
                .filter(this::isWebAnnotation);
    }

    /**
     * Does the given class have any web annotations.
     *
     * @param clazz the class.
     * @return true if it does, false otherwise.
     */
    private boolean hasWebAnnotation(Class<?> clazz) {
        return getWebAnnotations(clazz)
                .findAny()
                .isPresent();
    }

    /**
     * Is this a web annotation.
     *
     * @param annotation the annotation.
     * @return true if it is, false otherwise.
     */
    private boolean isWebAnnotation(Annotation annotation) {
        return annotation instanceof WebServlet
                || annotation instanceof WebListener
                || annotation instanceof WebInitParam
                || annotation instanceof WebFilter
                || annotation instanceof ServletSecurity
                || annotation instanceof MultipartConfig;
    }
}
