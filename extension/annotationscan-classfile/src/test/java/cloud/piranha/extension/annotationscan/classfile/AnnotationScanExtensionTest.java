/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.resource.impl.DefaultResourceManager;
import cloud.piranha.resource.impl.DefaultResourceManagerClassLoader;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.annotation.WebServlet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.JRE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        ClassfileAnnotationScanExtension extension = new ClassfileAnnotationScanExtension();
        extension.configure(webApplication);
        webApplication.initialize();
        assertEquals(classLoader, webApplication.getClassLoader());
    }

    /**
     * Test configure method.
     */
    @Test
    @EnabledOnJre(value = JRE.JAVA_21, disabledReason = "Only JDK 21 includes the Classfile API")
    void testConfigure2() {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        resourceManager.addResource(new DirectoryResource("target/test-classes"));

        DefaultResourceManagerClassLoader classLoader = new DefaultResourceManagerClassLoader(getClass().getClassLoader(), resourceManager);
        WebApplication webApplication = new DefaultWebApplication();
        webApplication.setClassLoader(classLoader);

        ClassfileAnnotationScanExtension extension = new ClassfileAnnotationScanExtension();
        extension.configure(webApplication);
        webApplication.initialize();

        AnnotationManager annotationManager = webApplication.getManager().getAnnotationManager();
        assertFalse(annotationManager.getAnnotations(WebServlet.class).isEmpty());
    }
}
