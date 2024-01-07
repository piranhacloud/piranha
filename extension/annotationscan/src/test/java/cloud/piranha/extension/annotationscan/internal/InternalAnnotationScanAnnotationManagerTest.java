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
package cloud.piranha.extension.annotationscan.internal;

import cloud.piranha.extension.annotationscan.TestWithHandlesTypesInitializer;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.extension.annotationscan.TestAnnotation;
import java.util.Collections;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the StandardAnnotationScanAnnotationManager class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class InternalAnnotationScanAnnotationManagerTest {
        
    @Test
    void testInitializerWithHandlesTypes () {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setAnnotationManager(new InternalAnnotationScanAnnotationManager());
        webApp.addInitializer(TestWithHandlesTypesInitializer.class.getName());
        webApp.initialize();
        assertTrue(webApp.getAttribute("object_class") instanceof Boolean);
        assertFalse((Boolean) webApp.getAttribute("object_class"));
        assertTrue(webApp.getAttribute("someannotation_class") instanceof Boolean);
        assertFalse((Boolean) webApp.getAttribute("someannotation_class"));
    }

    @Test
    void testInitializerWithHandlesTypes2 () {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setAnnotationManager(new InternalAnnotationScanAnnotationManager());
        webApp.addInitializer(TestWithHandlesTypesInitializer.class.getName());
        InternalAnnotationScanAnnotationManager annotationManager = 
                (InternalAnnotationScanAnnotationManager) webApp.getManager().getAnnotationManager();
        annotationManager.addInstance(Set.class, Collections.emptySet().getClass());
        annotationManager.addAnnotation(new InternalAnnotationScanAnnotationInfo<>(
                ClassAnnotated.class.getAnnotation(TestAnnotation.class),
                ClassAnnotated.class
            )
        );
        webApp.initialize();
        assertTrue(webApp.getAttribute("object_class") instanceof Boolean);
        assertTrue((Boolean) webApp.getAttribute("object_class"));
        assertTrue(webApp.getAttribute("someannotation_class") instanceof Boolean);
        assertTrue((Boolean) webApp.getAttribute("someannotation_class"));
    }
    
    @TestAnnotation
    static class ClassAnnotated {
    }
}
