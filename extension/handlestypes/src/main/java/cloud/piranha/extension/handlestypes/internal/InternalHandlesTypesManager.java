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
package cloud.piranha.extension.handlestypes.internal;

import cloud.piranha.core.api.HandlesTypesManager;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The HandlesTypes manager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class InternalHandlesTypesManager implements HandlesTypesManager {

    /**
     * Stores the annotated classes.
     */
    private final ConcurrentHashMap<Class<?>, Set<Class<?>>> annotatedClasses = new ConcurrentHashMap<>();

    /**
     * Stores the extending classes.
     */
    private final ConcurrentHashMap<Class<?>, Set<Class<?>>> extendingClasses = new ConcurrentHashMap<>();

    /**
     * Stores the implementing classes.
     */
    private final ConcurrentHashMap<Class<?>, Set<Class<?>>> implementingClasses = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    public InternalHandlesTypesManager() {
    }

    @Override
    public void addAnnotatedClass(Class<?> annotationClass, Class<?> annotatedClass) {
        synchronized (annotatedClasses) {
            if (!annotatedClasses.containsKey(annotationClass)) {
                HashSet<Class<?>> hashSet = new HashSet<>();
                hashSet.add(annotatedClass);
                annotatedClasses.put(annotationClass, hashSet);
            } else {
                annotatedClasses.get(annotationClass).add(annotatedClass);
            }
        }
    }

    @Override
    public void addExtendingClass(Class<?> baseClass, Class<?> extendingClass) {
        synchronized (extendingClasses) {
            if (!extendingClasses.containsKey(baseClass)) {
                HashSet<Class<?>> hashSet = new HashSet<>();
                hashSet.add(extendingClass);
                extendingClasses.put(baseClass, hashSet);
            } else {
                extendingClasses.get(baseClass).add(extendingClass);
            }
        }
    }

    @Override
    public void addImplementingClass(Class<?> interfaceClass, Class<?> implementingClass) {
        synchronized (implementingClasses) {
            if (!implementingClasses.containsKey(interfaceClass)) {
                HashSet<Class<?>> hashSet = new HashSet<>();
                hashSet.add(implementingClass);
                implementingClasses.put(interfaceClass, hashSet);
            } else {
                implementingClasses.get(interfaceClass).add(implementingClass);
            }
        }
    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(Class<?> annotationClass) {
        return annotatedClasses.getOrDefault(annotationClass, new HashSet<>());
    }

    @Override
    public Set<Class<?>> getExtendingClasses(Class<?> baseClass) {
        return extendingClasses.getOrDefault(baseClass, new HashSet<>());
    }

    @Override
    public Set<Class<?>> getImplementingClasses(Class<?> interfaceClass) {
        return implementingClasses.getOrDefault(interfaceClass, new HashSet<>());
    }

    @Override
    public Set<Class<?>> getClasses(Set<Class<?>> classes) {
        HashSet<Class<?>> result = new HashSet<>();
        if (classes != null) {
            for (Class<?> clazz : classes) {
                result.addAll(getAnnotatedClasses(clazz));
                result.addAll(getExtendingClasses(clazz));
                result.addAll(getImplementingClasses(clazz));
            }
        }
        return result.isEmpty() ? null : result;
    }
}
