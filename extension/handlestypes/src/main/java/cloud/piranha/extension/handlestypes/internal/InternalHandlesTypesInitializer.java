/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.handlestypes.internal;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationClassLoader;
import cloud.piranha.resource.api.ResourceManager;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The ServletContainerInitializer that delivers HandlesTypes processing.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class InternalHandlesTypesInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER
            = System.getLogger(InternalHandlesTypesInitializer.class.getName());

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext context)
            throws ServletException {

        WebApplication webApplication = (WebApplication) context;
        if (webApplication != null) {
            /*
             * Determine the set of classes for all the ServletContainerInitializers.
             */
            HashSet<Class<?>> collectedClasses = new HashSet<>();
            List<ServletContainerInitializer> initializers = webApplication.getInitializers();
            for (ServletContainerInitializer initializer : initializers) {
                if (initializer.getClass().isAnnotationPresent(HandlesTypes.class)) {
                    HandlesTypes handlesTypes = initializer.getClass().getAnnotation(HandlesTypes.class);
                    if (handlesTypes.value() != null) {
                        collectedClasses.addAll(
                                Arrays.stream(handlesTypes.value())
                                        .collect(Collectors.toSet()));
                    }
                }
            }

            /*
             * Walk the resource hierarchy and determine if the collected
             * classes are used (as annotation, interface or extends) and if so
             * add them to the HandlesTypes manager.
             */
            WebApplicationClassLoader classLoader
                    = (WebApplicationClassLoader) webApplication.getClassLoader();
            if (classLoader != null) {
                ResourceManager resourceManager = classLoader.getResourceManager();
                resourceManager.getAllLocations().parallel()
                    .forEach(location -> {
                        Class clazz = loadClass(webApplication, location);
                        if (clazz != null) {
                            processClass(webApplication, collectedClasses, clazz);
                        }
                    });
            } else {
                LOGGER.log(ERROR, "Unable to process HandlesTypes because the classloader is incompatible");
            }
        }
    }

    /**
     * Load a class from the given location.
     *
     * @param webApplication the web application.
     * @param location the location of the class file.
     * @return the loaded class, or null if it could not be loaded.
     */
    private Class loadClass(WebApplication webApplication, String location) {
        /*
         * We do not look at module-info.java.
         */
        if (location.toLowerCase().endsWith("module-info.class")) {
            return null;
        }
        /*
         * We do not look at any location that is not referring to a class.
         */
        if (!location.toLowerCase().endsWith(".class")) {
            return null;
        }
        String className = location.substring(1).replace('/', '.');
        className = className.substring(0, className.lastIndexOf('.'));
        try {
            return webApplication.getClassLoader().loadClass(className);
        } catch (Throwable t) {
            /*
             * If we could not load it we cannot determine if we are a match.
             */
            return null;
        }
    }

    /**
     * Process the class to check if it matches any collected classes.
     *
     * @param webApplication the web application.
     * @param collectedClasses the collected classes.
     * @param clazz the class under consideration.
     */
    private void processClass(WebApplication webApplication, HashSet<Class<?>> collectedClasses, Class clazz) {
        for (Class collectedClass : collectedClasses) {
            if (isClassAnnotatedWith(clazz, collectedClass)) {
                webApplication.getManager().getHandlesTypesManager()
                        .addAnnotatedClass(collectedClass, clazz);
            }
            if (isClassExtending(clazz, collectedClass)) {
                webApplication.getManager().getHandlesTypesManager()
                        .addExtendingClass(collectedClass, clazz);
            }
            if (isClassImplementing(clazz, collectedClass)) {
                webApplication.getManager().getHandlesTypesManager()
                        .addImplementingClass(collectedClass, clazz);
            }
        }
    }

    /**
     * Check if the class is annotated with the given annotation.
     *
     * @param clazz the class under consideration.
     * @param annotationClass the annotation class.
     * @return true if it is, false otherwise.
     */
    private boolean isClassAnnotatedWith(Class clazz, Class annotationClass) {
        return annotationClass.isAnnotation() && clazz.getAnnotation(annotationClass) != null;
    }

    /**
     * Check if the class extends the given superclass.
     *
     * @param clazz the class.
     * @param superClass the class being extended (aka the super class).
     * @return true if it is, false otherwise.
     */
    private boolean isClassExtending(Class<?> clazz, Class<?> superClass) {
        Class<?> currentClass = clazz.getSuperclass();
        while (currentClass != null) {
            if (superClass.equals(currentClass)) {
                return true;
            }
            currentClass = currentClass.getSuperclass();
        }
        return false;
    }

    /**
     * Check if the class implements the given interface.
     *
     * @param clazz the class.
     * @param interfaceClass the interface class.
     * @return true if it is, false otherwise.
     */
    private boolean isClassImplementing(Class<?> clazz, Class<?> interfaceClass) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            for (Class<?> iface : currentClass.getInterfaces()) {
                if (interfaceClass.isAssignableFrom(iface)) {
                    return true;
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return false;
    }
}
