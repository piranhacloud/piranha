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
package cloud.piranha.extension.annotationscan.classfile;

import cloud.piranha.core.api.AnnotationManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.extension.annotationscan.classfile.internal.InternalAnnotationScanAnnotationManager;
import cloud.piranha.extension.annotationscan.classfile.internal.InternalAnnotationScanAnnotationInfo;
import cloud.piranha.resource.api.ResourceManager;
import cloud.piranha.resource.api.ResourceManagerClassLoader;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import sun.reflect.ReflectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.System.Logger;
import java.lang.annotation.Annotation;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.WARNING;
import static java.lang.invoke.MethodType.methodType;
import static java.util.Arrays.stream;

/**
 * This ServletContainerInitializer deep scans for annotations and adds them to
 * the StandardAnnotationScanAnnotationManager.
 *
 * @author Arjan Tijms
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Thiago Henrique Hupner
 */
public class ClassfileAnnotationScanInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ClassfileAnnotationScanInitializer.class.getName());

    /**
     * Default constructor
     */
    public ClassfileAnnotationScanInitializer() {
    }

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

        ClassLoader classLoader = webApp.getClassLoader();
        if (!(classLoader instanceof ResourceManagerClassLoader resourceManagerClassLoader)) {
            LOGGER.log(WARNING, "ResourceManagerClassLoader not installed. This scanner does not work");
            return;
        }
        ResourceManager resourceManager = resourceManagerClassLoader.getResourceManager();
        resourceManager
                .getAllLocations()
                .filter(e -> e.endsWith(".class") && !e.endsWith("module-info.class") && !e.startsWith("/META-INF/versions"))
                .filter(resource -> classFileHasJakartaAnnotations(readResource(resource, resourceManager)))
                .map(e -> loadClass(classLoader, e))
                .flatMap(this::getJakartaAnnotations)
                .map(annotationInstance -> new InternalAnnotationScanAnnotationInfo<>(annotationInstance, annotationInstance.annotationType()))
                .forEach(annotationManager::addAnnotation);


    }

    /**
     * Load the class using the given class loader.
     *
     * @param classLoader the class loader.
     * @param className   the class name.
     * @return the class.
     */
    private Class<?> loadClass(ClassLoader classLoader, String className) {
        try {
            return classLoader.loadClass(
                    className.replace("/", ".")
                            .substring(1, className.length() - ".class".length()));
        } catch (ClassNotFoundException e) {
            return Object.class;
        }
    }

    /**
     * Get the web annotations for the given class.
     *
     * @param clazz the class.
     * @return the stream of web annotations.
     */
    private Stream<Annotation> getJakartaAnnotations(Class<?> clazz) {
        return stream(clazz.getAnnotations())
                .filter(this::isJakartaAnnotation);
    }

    private boolean isJakartaAnnotation(Annotation clazz) {
        return isJakartaAnnotation(clazz.annotationType().describeConstable().orElse(ConstantDescs.CD_Object));
    }

    private boolean isJakartaAnnotation(ClassDesc annotation) {
        return annotation.packageName().startsWith("jakarta");
    }

    private byte[] readResource(String resourceName, ResourceManager resourceManager) {
        try (InputStream resourceAsStream = resourceManager.getResourceAsStream(resourceName)) {
            return resourceAsStream.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean classFileHasJakartaAnnotations(byte[] classFileBytes) {
        /*
         *   Without reflection the code would be similar to:
         *   {@snippet lang="java"
         *   ClassModel classModel = Classfile.parse(classFileBytes);
         *   Optional<Attribute<?>> attribute = classModel.findAttribute(Attributes.RUNTIME_VISIBLE_ANNOTATIONS);
         *   return optionalAttribute
         *       .stream()
         *       .flatMap(attribute -> attribute.annotations().stream())
         *       .map(annotation -> annotation.classSymbol())
         *       .anyMatch(this::isJakartaAnnotation)
         *   }
         */
        try {
            Object classModel = Holder.PARSE.invokeExact(classFileBytes);

            Optional<?> optionalAttribute = (Optional<?>) Holder.FIND_ATTRIBUTE.invokeExact(classModel, Holder.RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE_MAPPER);
            if (optionalAttribute.isEmpty()) {
                return false;
            }

            List<?> annotations = (List<?>) Holder.ANNOTATIONS.invokeExact(optionalAttribute.get());
            for (Object annotation : annotations) {
                ClassDesc annotationDesc = (ClassDesc) Holder.CLASS_SYMBOL.invokeExact(annotation);
                if (isJakartaAnnotation(annotationDesc)) {
                    return true;
                }
            }
        } catch (Throwable ignored) {
            // invokeExact throws Throwable...
        }
        return false;
    }
}

class Holder {
    /**
     * Stores the handle to Classfile.parse(byte[])
     */
    static final MethodHandle PARSE;
    /**
     * Stores the handle to ClassModel.findAttribute(AttributeMapper)
     */
    static final MethodHandle FIND_ATTRIBUTE;
    /**
     * Stores the handle to RuntimeVisibleAnnotationsAttribute.annotations()
     */
    static final MethodHandle ANNOTATIONS;
    /**
     * Stores the handle to Annotation.classSymbol()
     */
    static final MethodHandle CLASS_SYMBOL;
    /**
     * Stores the RuntimeVisibleAnnotationsAttributeMapper
     */
    static final Object RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE_MAPPER;

    /**
     * Stores the value of trusted lookup
     */
    private static final int TRUSTED = -1;

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ClassfileAnnotationScanInitializer.class.getName());

    private Holder() {
    }

    static {
        try {
            MethodHandles.Lookup lookup = getLookup();

            Class<?> classFileClass = lookup.findClass("jdk.internal.classfile.Classfile");
            Class<?> classModelClass = lookup.findClass("jdk.internal.classfile.ClassModel");
            Class<?> attributeMapperClass = lookup.findClass("jdk.internal.classfile.AttributeMapper");
            Class<?> attributesClass = lookup.findClass("jdk.internal.classfile.Attributes");
            Class<?> annotationClass = lookup.findClass("jdk.internal.classfile.Annotation");
            Class<?> classFileOptionsArrayClass = lookup.findClass("[Ljdk.internal.classfile.Classfile$Option;");
            Class<?> runtimeVisibleAnnotationsAttributeClass = lookup.findClass("jdk.internal.classfile.attribute.RuntimeVisibleAnnotationsAttribute");

            MethodHandle parse = lookup.findStatic(classFileClass, "parse", methodType(classModelClass, byte[].class, classFileOptionsArrayClass));
            Object classFileOptionsEmptyArray = MethodHandles.arrayConstructor(classFileOptionsArrayClass).invoke(0);
            PARSE = MethodHandles.insertArguments(parse, 1, classFileOptionsEmptyArray)
                    .asType(methodType(Object.class, byte[].class));
            FIND_ATTRIBUTE = lookup.findVirtual(classModelClass, "findAttribute", methodType(Optional.class, attributeMapperClass))
                    .asType(methodType(Optional.class, Object.class, Object.class));
            ANNOTATIONS = lookup.findVirtual(runtimeVisibleAnnotationsAttributeClass, "annotations", methodType(List.class))
                    .asType(methodType(List.class, Object.class));
            CLASS_SYMBOL = lookup.findVirtual(annotationClass, "classSymbol", methodType(ClassDesc.class))
                    .asType(methodType(ClassDesc.class, Object.class));
            RUNTIME_VISIBLE_ANNOTATIONS_ATTRIBUTE_MAPPER = lookup.findStaticGetter(attributesClass, "RUNTIME_VISIBLE_ANNOTATIONS", attributeMapperClass).invoke();
        } catch (Throwable e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static MethodHandles.Lookup getLookup() {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            lookup.findClass("jdk.internal.classfile.Classfile");
            lookup.findClass("jdk.internal.classfile.attribute.RuntimeVisibleAnnotationsAttribute");
            return lookup;
        } catch (ClassNotFoundException | IllegalAccessException e) {
            Module module = lookup.lookupClass().getModule();
            String moduleName = module.isNamed() ? module.getName() : "ALL-UNNAMED";
            LOGGER.log(WARNING, "The classfile annotation scan extension requires the following flags to work: " +
                    "--add-exports=java.base/jdk.internal.classfile={0} --add-exports=java.base/jdk.internal.classfile.attribute={0}. " +
                    "Using non-standard APIs as fallback for now.", moduleName);
        }
        try {
            ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
            Class<MethodHandles.Lookup> lookupClass = MethodHandles.Lookup.class;
            Constructor<MethodHandles.Lookup> constructor = lookupClass.getDeclaredConstructor(Class.class, Class.class, int.class);
            Constructor<?> lookupConstructor = reflectionFactory.newConstructorForSerialization(lookupClass, constructor);
            return (MethodHandles.Lookup) lookupConstructor.newInstance(Object.class, null, TRUSTED);
        } catch (Throwable e) {
            LOGGER.log(ERROR, "Could not use the non-standard APIs, this extension will fail. Please add the flags to the command line.");
            throw new ExceptionInInitializerError(e);
        }
    }
}
