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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.AnnotationInfo;
import cloud.piranha.core.api.AnnotationManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The default AnnotationManager.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAnnotationManager implements AnnotationManager {
    
    /**
     * Stores the annotated classes.
     */
    private HashMap<Class<? extends Annotation>, Set<Class<?>>> annotatedClasses = new HashMap<>();

    @Override
    public void addAnnotatedClass(Class<? extends Annotation> annotationClass, Class<?> clazz) {
        if (annotationClass == null) {
            throw new IllegalArgumentException("annotationClass cannot be null");
        }
        if (!annotatedClasses.containsKey(annotationClass)) {
            HashSet<Class<?>> classes = new HashSet<>();
            classes.add(clazz);
            annotatedClasses.put(annotationClass, classes);
        } else {
            HashSet<Class<?>> classes = (HashSet) annotatedClasses.get(annotationClass);
            classes.add(clazz);
        }
    }

    @Override
    public void addAnnotation(AnnotationInfo<?> annotationInfo) {
        if (annotationInfo == null) {
            throw new IllegalArgumentException("annotationInfo cannot be null");
        }
    }

    @Override
    public void addInstance(Class<?> instanceClass, Class<?> implementingClass) {
        if (instanceClass == null) {
            throw new IllegalArgumentException("instanceClass cannot be null");
        }
    }

    @Override
    public <T> List<AnnotationInfo<T>> getAnnotations(Class<T> annotationClass) {
        return Collections.emptyList();
    }

    @Override
    public List<AnnotationInfo<?>> getAnnotations(Class<?>... annotationClasses) {
        return Collections.emptyList();
    }

    @Override
    public <T> List<Class<T>> getInstances(Class<T> instanceClass) {
        return Collections.emptyList();
    }

    @Override
    public List<Class<?>> getInstances(Class<?>... instanceClasses) {
        return Collections.emptyList();
    }

    @Override
    public <T> List<AnnotationInfo<T>> getAnnotationsByTarget(Class<T> annotationClass, AnnotatedElement type) {
        return Collections.emptyList();
    }

    @Override
    public Set<Class<?>> getAnnotatedClass(Class<? extends Annotation> annotationClass) {
        return annotatedClasses.getOrDefault(annotationClass, Collections.emptySet());
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(Class<?>[] annotationClasses) {
        HashSet<Class<?>> result = new HashSet<>();
        if (annotationClasses != null) {
            for (Class<?> annotationClass : annotationClasses) {
                result.addAll(getAnnotatedClass((Class<? extends Annotation>) annotationClass));
            }
        }
        return result;
    }
}
