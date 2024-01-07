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

import java.lang.annotation.Annotation;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultAnnotationManager class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultAnnotationManagerTest {
    

    /**
     * Test addAnnotatedClass method.
     */
    @Test
    void testAddAnnotatedClass() {
        try {
            DefaultAnnotationManager manager = new DefaultAnnotationManager();
            manager.addAnnotatedClass(null, null);
            fail();
        } catch(IllegalArgumentException iae) {            
            // expecting this so swallowing it up
        }
    }

    /**
     * Test addAnnotatedClass method.
     */
    @Test
    void testAddAnnotatedClass2() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        manager.addAnnotatedClass(annotation.getClass(), Object.class);
        manager.addAnnotatedClass(annotation.getClass(), String.class);
        assertEquals(2, manager.getAnnotatedClass(annotation.getClass()).size());
    }

    /**
     * Test addAnnotation method.
     */
    @Test
    void testAddAnnotation() {
        try {
            DefaultAnnotationManager manager = new DefaultAnnotationManager();
            manager.addAnnotation(null);
            fail();
        } catch(IllegalArgumentException iae) {
            // expecting this so swallowing it up
        }
    }

    /**
     * Test addInstance method.
     */
    @Test
    void testAddInstance() {
        try {
            DefaultAnnotationManager manager = new DefaultAnnotationManager();
            manager.addInstance(null, null);
            fail();
        } catch(IllegalArgumentException iae) {
            // expecting this so swallowing it up
        }
    }

    /**
     * Test getAnnotations method.
     */
    @Test
    void testGetAnnotations() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        assertNotNull(manager.getAnnotations(Object.class));
    }

    /**
     * Test getAnnotations method.
     */
    @Test
    void testGetAnnotations2() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        assertNotNull(manager.getAnnotations(new Class[0]));
    }

    /**
     * Test getInstances method.
     */
    @Test
    void testGetInstances() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        assertNotNull(manager.getInstances(Object.class));
    }

    /**
     * Test getInstances method.
     */
    @Test
    void testGetInstances2() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        assertNotNull(manager.getInstances(new Class[0]));
    }

    /**
     * Test getAnnotationsByTarget method.
     */
    @Test
    void testGetAnnotationsByTarget() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        assertNotNull(manager.getAnnotationsByTarget(null, null));
    }

    /**
     * Test getAnnotatedClass method.
     */
    @Test
    void testGetAnnotatedClass() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        assertNotNull(manager.getAnnotatedClass(null));
    }

    /**
     * Test getAnnotatedClasses method.
     */
    @Test
    void testGetAnnotatedClasses() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        assertNotNull(manager.getAnnotatedClasses(null));
    }

    /**
     * Test getAnnotatedClasses method.
     */
    @Test
    void testGetAnnotatedClasses2() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        manager.addAnnotatedClass(annotation.getClass(), Object.class);
        manager.addAnnotatedClass(annotation.getClass(), String.class);
        assertEquals(2, manager.getAnnotatedClasses(new Class[] {annotation.getClass()}).size());
    }
}
