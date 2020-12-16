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
package cloud.piranha.servlet4.impl;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import cloud.piranha.servlet4.webapp.AnnotationManager;

/**
 * The default AnnotationManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAnnotationManager implements AnnotationManager {

    /**
     * The default AnnotationInfo.
     * 
     * @param <T> the type.
     */
    public static class DefaultAnnotationInfo<T> implements AnnotationInfo<T> {

        /**
         * Stores the instance.
         */
        private final T instance;
        
        /**
         * Stores the target.
         */
        private final AnnotatedElement target;

        /**
         * Constructor.
         * 
         * @param instance the instance.
         * @param target the target annotated element.
         */
        public DefaultAnnotationInfo(T instance, AnnotatedElement target) {
            this.instance = instance;
            this.target = target;
        }

        @Override
        public T getInstance() {
            return instance;
        }

        @Override
        public AnnotatedElement getTarget() {
            return target;
        }
    }

    /**
     * Stores the annotations.
     */
    private final Map<Class<?>, List<AnnotationInfo<?>>> annotations = new ConcurrentHashMap<>();
    
    /**
     * Stores the instances.
     */
    private final Map<Class<?>, List<Class<?>>> instances = new ConcurrentHashMap<>();


    @Override
    public List<AnnotationInfo<?>> getAnnotations(Class<?>... annotationClasses) {
        return
            Arrays.stream(annotationClasses)
                  .flatMap(this::getAnnotationStream)
                  .collect(toList());
    }

    @Override
    public <T> List<AnnotationInfo<T>> getAnnotations(Class<T> annotationClass) {
        return
            getAnnotationStream(annotationClass)
                .collect(toList());
    }

    @Override
    public List<Class<?>> getInstances(Class<?>... instanceClasses) {
        return
            Arrays.stream(instanceClasses)
                  .flatMap(this::getInstanceStream)
                  .collect(toList());
    }

    @Override
    public <T> List<Class<T>> getInstances(Class<T> instanceClass) {
        return
            getInstanceStream(instanceClass)
                .collect(toList());
    }

    /**
     * Add the annotation.
     * 
     * @param annotationInfo the annotation info.
     * @return the annotation manager.
     */
    public DefaultAnnotationManager addAnnotation(AnnotationInfo<?> annotationInfo) {
        annotations.computeIfAbsent(
            ((Annotation) annotationInfo.getInstance()).annotationType(),
            e -> new ArrayList<>())
                   .add(annotationInfo);

        return this;
    }

    /**
     * Add an instance.
     * 
     * @param instanceClass the instance class.
     * @param implementingClass the implementing class.
     * @return the annotation manager.
     */
    public DefaultAnnotationManager addInstance(Class<?> instanceClass, Class<?> implementingClass) {
        instances.computeIfAbsent(
            instanceClass,
            e -> new ArrayList<>())
                     .add(implementingClass);

        return this;
    }


    // ### Private methods

    @SuppressWarnings("unchecked")
    private <T> Stream<AnnotationInfo<T>> getAnnotationStream(Class<T> annotationClass) {
        return annotations.getOrDefault(annotationClass, emptyList())
                          .stream()
                          .map(e -> (AnnotationInfo<T>) e);
    }

    @SuppressWarnings("unchecked")
    private <T> Stream<Class<T>> getInstanceStream(Class<T> instanceClass) {
        return instances.getOrDefault(instanceClass, emptyList())
                          .stream()
                          .map(e -> (Class<T>) e);
    }

    // ### Not implemented

    /**
     * Get the annotated classes.
     *
     * @return the annotated classes.
     */
    @Override
    public Set<Class<?>> getAnnotatedClasses() {
        return new HashSet<>();
    }


    @Override
    public <T> List<AnnotationInfo<T>> getAnnotationsByTarget(Class<T> annotationClass, AnnotatedElement type) {
        return null;
    }
}
