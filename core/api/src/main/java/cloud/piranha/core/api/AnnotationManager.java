/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.api;

import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Set;

/**
 * The AnnotationManager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms
 */
public interface AnnotationManager {
   
    /**
     * Add the annotation.
     *
     * @param annotationInfo the annotation info.
     */
    void addAnnotation(AnnotationInfo<?> annotationInfo);
    
    /**
     * Add an instance.
     * 
     * @param instanceClass the instance class.
     * @param implementingClass the implementing class.
     */
    void addInstance(Class<?> instanceClass, Class<?> implementingClass);
    
    /**
     * Get the annotations.
     * 
     * @param <T> the type.
     * @param annotationClass the annotation class.
     * @return the typed list of annotations.
     */
    <T> List<AnnotationInfo<T>> getAnnotations(Class<T> annotationClass);
    
    /**
     * Get the annotations.
     * 
     * @param annotationClasses the annotation classes.
     * @return the list of annotations.
     */
    List<AnnotationInfo> getAnnotations(Class<?>... annotationClasses);

    /**
     * Get the instances.
     * 
     * @param <T> the type.
     * @param instanceClass the instance class.
     * @return the typed list of instances.
     */
    <T> List<Class<T>> getInstances(Class<T> instanceClass);
    
    /**
     * Get the instances.
     * 
     * @param instanceClasses the instance classes.
     * @return the list of instances.
     */
    List<Class<?>> getInstances(Class<?>... instanceClasses);

    /**
     * {@return the set of all annotated classes}
     */
    Set<Class<?>> getAnnotatedClasses();
    
    /**
     * Get the annotation for the annotation class and annotated element type.
     * 
     * @param <T> the type.
     * @param annotationClass the annotation class.
     * @param type the annotated element type.
     * @return the list of annotations.
     */
    <T> List<AnnotationInfo<T>> getAnnotationsByTarget(Class<T> annotationClass, AnnotatedElement type);
}
