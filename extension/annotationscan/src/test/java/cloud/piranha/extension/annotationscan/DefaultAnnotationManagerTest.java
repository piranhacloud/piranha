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

import cloud.piranha.core.impl.DefaultWebApplication;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultAnnotationManager class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultAnnotationManagerTest {
    
    /**
     * Test getClasses method.
     */
    @Test
    void testGetClasses() {
        DefaultAnnotationManager manager = new DefaultAnnotationManager();
        assertNotNull(manager.getAnnotatedClasses());
        assertTrue(manager.getAnnotatedClasses().isEmpty());
    }
    
    @Test
    void testInitializerWithHandlesTypes () {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setAnnotationManager(new DefaultAnnotationManager());
        webApp.addInitializer(InitializerWithHandlesTypes.class.getName());
        webApp.initialize();
        assertTrue(webApp.getAttribute("object_class") instanceof Boolean);
        assertFalse((Boolean) webApp.getAttribute("object_class"));

        assertTrue(webApp.getAttribute("someannotation_class") instanceof Boolean);
        assertFalse((Boolean) webApp.getAttribute("someannotation_class"));
    }

    @Test
    void testInitializerWithHandlesTypes2 () {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setAnnotationManager(new DefaultAnnotationManager());
        webApp.addInitializer(InitializerWithHandlesTypes.class.getName());
        DefaultAnnotationManager annotationManager = (DefaultAnnotationManager) webApp.getAnnotationManager();
        annotationManager.addInstance(Set.class, Collections.emptySet().getClass());
        annotationManager.addAnnotation(
            new DefaultAnnotationInfo<>(
                ClassAnnotated.class.getAnnotation(SomeAnnotation.class),
                ClassAnnotated.class
            )
        );

        webApp.initialize();
        assertTrue(webApp.getAttribute("object_class") instanceof Boolean);
        assertTrue((Boolean) webApp.getAttribute("object_class"));

        assertTrue(webApp.getAttribute("someannotation_class") instanceof Boolean);
        assertTrue((Boolean) webApp.getAttribute("someannotation_class"));
    }

    @HandlesTypes({Set.class, SomeAnnotation.class})
    public static class InitializerWithHandlesTypes implements ServletContainerInitializer{

        public InitializerWithHandlesTypes() {
        }
        
        @Override
        public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
            Optional<Class<?>> classInstance = classes.stream().filter(Set.class::isAssignableFrom).findFirst();
            servletContext.setAttribute("object_class", classInstance.isPresent());

            Optional<Class<?>> classWithAnnotation = classes.stream().filter(x -> x.getAnnotation(SomeAnnotation.class) != null).findFirst();
            servletContext.setAttribute("someannotation_class", classWithAnnotation.isPresent());
        }
    }
    
    @SomeAnnotation
    static class ClassAnnotated {
    }

    @Retention(RUNTIME)
    @interface SomeAnnotation {
    }    
}
