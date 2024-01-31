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
package cloud.piranha.extension.annotationscan;

import cloud.piranha.core.api.AnnotationManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.extension.annotationscan.AnnotationScanExtension;
import cloud.piranha.resource.impl.DefaultResourceManager;
import cloud.piranha.resource.impl.DefaultResourceManagerClassLoader;
import jakarta.servlet.annotation.WebServlet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the AnnotationScanExtension class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class AnnotationScanExtensionTest {

    /**
     * Test configure method.
     */
    @Test
    void testConfigure() {
        WebApplication webApplication = new DefaultWebApplication();
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultResourceManagerClassLoader classLoader
                = new DefaultResourceManagerClassLoader(resourceManager);
        classLoader.setDelegateClassLoader(getClass().getClassLoader());
        webApplication.setClassLoader(classLoader);
        AnnotationScanExtension extension = new AnnotationScanExtension();
        extension.configure(webApplication);
        webApplication.initialize();
        assertEquals(classLoader, webApplication.getClassLoader());
    }

    /**
     * Test configure method.
     */
    @Test
    void testConfigure2() {
        WebApplication webApplication = new DefaultWebApplication();
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultResourceManagerClassLoader classLoader
                = new DefaultResourceManagerClassLoader(resourceManager);
        classLoader.setDelegateClassLoader(getClass().getClassLoader());
        webApplication.setClassLoader(classLoader);
        webApplication.setInitParameter(
                "cloud.piranha.extension.annotationscan.AnnotatedClasses", 
                "cloud.piranha.extension.annotationscan.TestServlet");
        AnnotationScanExtension extension = new AnnotationScanExtension();
        extension.configure(webApplication);
        webApplication.initialize();
        assertEquals(classLoader, webApplication.getClassLoader());
        AnnotationManager annotationManager = webApplication.getManager().getAnnotationManager();
        assertFalse(annotationManager.getAnnotations(WebServlet.class).isEmpty());
    }
}
