/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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

import cloud.piranha.extension.annotationscan.internal.InternalAnnotationScanAnnotationManager;
import cloud.piranha.extension.annotationscan.internal.InternalAnnotationScanAnnotationInfo;
import cloud.piranha.resource.api.ResourceManager;
import cloud.piranha.resource.api.ResourceManagerClassLoader;
import cloud.piranha.core.api.AnnotationManager;
import cloud.piranha.core.api.WebApplication;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import sun.reflect.ReflectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.System.Logger;

import static java.lang.System.Logger.Level.WARNING;

import java.lang.annotation.Annotation;

import static java.util.Arrays.stream;

import java.lang.constant.ClassDesc;
import java.lang.constant.DirectMethodHandleDesc;
import java.lang.constant.MethodHandleDesc;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This ServletContainerInitializer deep scans for annotations and adds them to
 * the StandardAnnotationScanAnnotationManager.
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
     * @param classes        the classes.
     * @param servletContext the servlet context.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        WebApplication webApp = (WebApplication) servletContext;

        AnnotationManager annotationManager = webApp.getManager().getAnnotationManager();
        if (annotationManager == null) {
            annotationManager = new InternalAnnotationScanAnnotationManager();
            webApp.getManager().setAnnotationManager(annotationManager);
        }

        final AnnotationManager annotationMgr = annotationManager;

        ClassLoader classLoader = webApp.getClassLoader();
        if (!(classLoader instanceof ResourceManagerClassLoader resourceManagerClassLoader)) {
            LOGGER.log(WARNING, "ResourceManagerClassLoader not installed. This scanner does not work");
            return;
        }

        if (servletContext.getInitParameter("cloud.piranha.extension.annotationscan.AnnotatedClasses") != null) {
            String[] classNames = servletContext.getInitParameter("cloud.piranha.extension.annotationscan.AnnotatedClasses").split(",");
            if (classNames.length > 0) {
                for (String className : classNames) {
                    Class clazz = null;
                    try {
                        clazz = classLoader.loadClass(className);
                    } catch (ClassNotFoundException cnfe) {
                        LOGGER.log(WARNING, "Unable to load class: " + className);
                    }
                    if (clazz != null && hasWebAnnotation(clazz)) {
                        final Class targetClazz = clazz;
                        getWebAnnotations(clazz).forEach(
                                annotationInstance ->
                                        annotationMgr.addAnnotation(new InternalAnnotationScanAnnotationInfo<>(annotationInstance, targetClazz)));
                    }
                }
            }
        } else {

            if (System.getProperty("cloud.piranha.extension.annotationscan.experimental.classfile") != null) {
                ResourceManager resourceManager = resourceManagerClassLoader
                        .getResourceManager();
                resourceManager
                        .getAllLocations()
                        .filter(e -> e.endsWith(".class") && !e.endsWith("module-info.class") && !e.startsWith("/META-INF/versions"))
                        .filter(resource -> classFileHasWebAnnotations(readResource(resource, resourceManager)))
                        .map(e -> loadClass(classLoader, e))
                        .forEach(targetClazz -> getWebAnnotations(targetClazz)
                                .forEach(annotationInstance
                                        -> annotationMgr.addAnnotation(new InternalAnnotationScanAnnotationInfo<>(annotationInstance, targetClazz))));
                return;

            }

            resourceManagerClassLoader
                    .getResourceManager()
                    .getAllLocations()
                    .filter(e -> e.endsWith(".class") && !e.endsWith("module-info.class") && !e.startsWith("/META-INF/versions"))
                    .map(e -> loadClass(classLoader, e))
                    .filter(this::hasWebAnnotation)
                    .forEach(targetClazz -> getWebAnnotations(targetClazz)
                            .forEach(annotationInstance
                                    -> annotationMgr.addAnnotation(new InternalAnnotationScanAnnotationInfo<>(annotationInstance, targetClazz))));
        }
    }

    /**
     * Load the class using the given class loader.
     *
     * @param classLoader the class loader.
     * @param className   the class name.
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
                || annotation instanceof MultipartConfig
                || (annotation != null && annotation.toString().contains("jakarta.ws.rs.")
                || (annotation != null && annotation.toString().contains("jakarta.websocket.")));
    }

    private boolean isWebAnnotation(ClassDesc annotation) {

        List<ClassDesc> webAnnotations = Stream.of(WebServlet.class.describeConstable(),
                        WebListener.class.describeConstable(),
                        WebFilter.class.describeConstable(),
                        ServletSecurity.class.describeConstable(),
                        MultipartConfig.class.describeConstable())
                .<ClassDesc>mapMulti(Optional::ifPresent)
                .toList();

        if (webAnnotations.stream()
                .anyMatch(annotation::equals)) {
            return true;
        }

        List<String> packages = List.of("jakarta.ws.rs", "jakarta.websocket");
        return packages.stream().anyMatch(thePackage -> annotation.packageName().equals(thePackage));
    }

    private byte[] readResource(String resourceName, ResourceManager resourceManager) {
        try (InputStream resourceAsStream = resourceManager.getResourceAsStream(resourceName)) {
            return resourceAsStream.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean classFileHasWebAnnotations(byte[] classFileBytes) {

        class Holder {
            static final Class<?> CLASS_FILE_CLASS;
            static final Class<?> CLASS_MODEL_CLASS;
            static final Class<?> ATTRIBUTE_MAPPER_CLASS;
            static final Class<?> ATTRIBUTES_CLASS;
            static final Class<?> ANNOTATION_CLASS;
            static final Class<?> CLASS_FILE_OPTIONS_ARRAY_CLASS;
            static final Class<?> RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE_CLASS;

            static final MethodHandle PARSE;
            static final MethodHandle FIND_ATTRIBUTE;
            static final MethodHandle ANNOTATIONS;
            static final MethodHandle CLASS_SYMBOL;
            static final Object RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE_MAPPER;

            static {
                try {
                    final MethodHandles.Lookup trustedLookup = getLookup();

                    CLASS_FILE_CLASS = trustedLookup.findClass("jdk.internal.classfile.Classfile");
                    CLASS_MODEL_CLASS = trustedLookup.findClass("jdk.internal.classfile.ClassModel");
                    ATTRIBUTE_MAPPER_CLASS = trustedLookup.findClass("jdk.internal.classfile.AttributeMapper");
                    ATTRIBUTES_CLASS = trustedLookup.findClass("jdk.internal.classfile.Attributes");
                    ANNOTATION_CLASS = trustedLookup.findClass("jdk.internal.classfile.Annotation");
                    CLASS_FILE_OPTIONS_ARRAY_CLASS = trustedLookup.findClass("[Ljdk.internal.classfile.Classfile$Option;");
                    RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE_CLASS = trustedLookup.findClass("jdk.internal.classfile.attribute.RuntimeVisibleAnnotationsAttribute");
                    RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE_MAPPER = trustedLookup.findStaticGetter(Holder.ATTRIBUTES_CLASS, "RUNTIME_VISIBLE_ANNOTATIONS", Holder.ATTRIBUTE_MAPPER_CLASS).invoke();

                    PARSE = trustedLookup.findStatic(Holder.CLASS_FILE_CLASS, "parse", MethodType.methodType(Holder.CLASS_MODEL_CLASS, byte[].class, Holder.CLASS_FILE_OPTIONS_ARRAY_CLASS));
                    FIND_ATTRIBUTE = trustedLookup.findVirtual(CLASS_MODEL_CLASS, "findAttribute", MethodType.methodType(Optional.class, ATTRIBUTE_MAPPER_CLASS));
                    ANNOTATIONS = trustedLookup.findVirtual(Holder.RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE_CLASS, "annotations", MethodType.methodType(List.class));
                    CLASS_SYMBOL = trustedLookup.findVirtual(ANNOTATION_CLASS, "classSymbol", MethodType.methodType(ClassDesc.class));
                } catch (Throwable e) {
                    throw new ExceptionInInitializerError(e);
                }
            }

            private static MethodHandles.Lookup getLookup() {
                try {
                    ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
                    Class<MethodHandles.Lookup> lookupClass = MethodHandles.Lookup.class;
                    Constructor<MethodHandles.Lookup> constructor = lookupClass.getDeclaredConstructor(Class.class, Class.class, int.class);
                    Constructor<?> lookupConstructor = reflectionFactory.newConstructorForSerialization(lookupClass, constructor);
                    return (MethodHandles.Lookup) lookupConstructor.newInstance(Object.class, null, -1);
                } catch (Throwable e) {
                    throw new ExceptionInInitializerError(e);
                }
            }
            private static <T, R> Function<T, R> unchecked(ThrowingFunction<T, R> f) {
                return t -> {
                    try {
                        return f.apply(t);
                    } catch (Throwable thr) {
                        return ThrowingFunction.sneakyThrow(thr);
                    }
                };
            }

            private interface ThrowingFunction<T, R> {
                R apply(T t) throws Throwable;

                @SuppressWarnings("unchecked")
                static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
                    throw (T) t;
                }
            }
        }
        /*
         *   Without reflection the code would be similar to:
         *   {@snippet lang="java"
         *   ClassModel classModel = Classfile.parse(classFileBytes);
         *   Optional<Attribute<?>> attribute = classModel.findAttribute(Attributes.RUNTIME_VISIBLE_ANNOTATIONS);
         *   return optionalAttribute
         *       .stream()
         *       .flatMap(attribute -> attribute.annotations().stream())
         *       .map(annotation -> annotation.classSymbol())
         *       .anyMatch(this::isWebAnnotation)
         *   }
         */
        Object classModel = Holder.unchecked(ignored -> Holder.PARSE.invoke(classFileBytes)).apply(null);

        Optional<?> optionalAttribute = Holder.unchecked(ignored -> (Optional<?>) Holder.FIND_ATTRIBUTE.invoke(classModel, Holder.RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE_MAPPER)).apply(null);
        return optionalAttribute
                .stream()
                .flatMap(Holder.unchecked(attribute -> ((List<?>) Holder.ANNOTATIONS.invoke(attribute)).stream()))
                .map(Holder.unchecked(annotation -> ((ClassDesc) Holder.CLASS_SYMBOL.invoke(annotation))))
                .anyMatch(this::isWebAnnotation);

    }

}
