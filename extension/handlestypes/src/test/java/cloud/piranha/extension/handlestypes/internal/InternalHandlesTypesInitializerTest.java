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
package cloud.piranha.extension.handlestypes.internal;

import cloud.piranha.core.api.HandlesTypesManager;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.extension.handlestypes.HandlesTypesExtension;
import cloud.piranha.resource.impl.AliasedDirectoryResource;
import cloud.piranha.resource.impl.DefaultResourceManager;
import java.io.File;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The JUnit tests for the InternalHandlesTypesInitializer class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class InternalHandlesTypesInitializerTest {

    /**
     * Test onStartup method.
     */
    @Test
    public void testOnStartup() throws Exception {
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader();
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        resourceManager.addResource(new AliasedDirectoryResource(
                new File("target/test-classes"), "/WEB-INF/classes"));
        classLoader.setResourceManager(resourceManager);
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setClassLoader(classLoader);
        HandlesTypesExtension extension = new HandlesTypesExtension();
        extension.configure(webApplication);
        webApplication.addInitializer(new TestServletContainerInitializer());

        webApplication.initialize();

        /*
         * Verify a HandlesTypesManager was installed.
         */
        HandlesTypesManager manager = webApplication.getManager().getHandlesTypesManager();
        assertNotNull(manager);

        /*
         * Verify that for annotation TestB we have A listed as a class of interest.
         */
        assertFalse(manager.getAnnotatedClasses(TestB.class).isEmpty());

        /*
         * Verify that for super class TestC we have A listed as a class of interest.
         */
        assertFalse(manager.getExtendingClasses(TestC.class).isEmpty());

        /*
         * Verify that for interface TestD we have A listed as a class of interest.
         */
        assertFalse(manager.getImplementingClasses(TestD.class).isEmpty());
        
        /*
         * Verify that we can get a combined set and it should be not empty.
         */
        HashSet<Class<?>> classes = new HashSet<>();
        classes.add(TestB.class);
        classes.add(TestC.class);
        classes.add(TestD.class);
        assertFalse(manager.getClasses(classes).isEmpty());
    }
}
