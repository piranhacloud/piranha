/*
 * Copyright (c) 2002-2025 Manorrock.com. All Rights Reserved.
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

import java.util.Set;

/**
 * The manager that delivers support for the HandlesTypes annotation.
 * 
 * <p>
 *  Whenever the onStartup method of a ServletContainerInitializer is called it
 *  gets passed a set of classes that it expressed interest in. This manager
 *  delivers the way a web application can vend that set of classes. See the
 *  JavaDoc of the ServletContainerInitializer for more information about the
 *  onStartup method.
 * </p>
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HandlesTypesManager {
    
    /**
     * Add the annotated class.
     * 
     * @param annotationClass the annotation class.
     * @param annotatedClass the annotated class.
     */
    void addAnnotatedClass(Class<?> annotationClass, Class<?> annotatedClass);
    
    /**
     * Add the extending class.
     * 
     * @param baseClass the based class.
     * @param extendingClass the extending class.
     */
    void addExtendingClass(Class<?> baseClass, Class<?> extendingClass);
    
    /**
     * Add the implementing class.
     * 
     * @param interfaceClass the interface.
     * @param implementingClass the implementing class.
     */
    void addImplementingClass(Class<?> interfaceClass, Class<?> implementingClass);
    
    /**
     * Get the annotated classes.
     * 
     * @param annotationClass the annotation classes.
     * @return the annotated classes.
     */
    Set<Class<?>> getAnnotatedClasses(Class<?> annotationClass);
    
    /**
     * Get the extending classes.
     * 
     * @param baseClass the base class.
     * @return the set of extending classes.
     */
    Set<Class<?>> getExtendingClasses(Class<?> baseClass);
    
    /**
     * Get the implementing classes.
     * 
     * @param interfaceClass the interface class.
     * @return the set of implementing classes.
     */
    Set<Class<?>> getImplementingClasses(Class<?> interfaceClass);
    
    /**
     * Get the set of classes that either are annotated with the given classes,
     * implement any of the given classes, or extend any of the given classes.
     * 
     * @param classes the set of given classes.
     * @return the set of classes or null if none found.
     */
    Set<Class<?>> getClasses(Set<Class<?>> classes);
}
