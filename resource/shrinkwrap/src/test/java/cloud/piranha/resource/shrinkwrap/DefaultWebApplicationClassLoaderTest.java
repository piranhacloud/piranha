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
package cloud.piranha.resource.shrinkwrap;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;

import cloud.piranha.resource.DefaultResourceManager;
import cloud.piranha.resource.api.Resource;

/**
 * The JUnit tests for the IsolatingResourceManagerClassLoader.
 *
 * @author Arjan Tijms
 */
class DefaultWebApplicationClassLoaderTest {


    /**
     * Test loadClass method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testLoadClass() throws Exception {
        IsolatingResourceManagerClassLoader classLoader = new IsolatingResourceManagerClassLoader("test");
        
        Resource resource = new ShrinkWrapResource("/WEB-INF/classes", create(WebArchive.class)
                .addClass(DefaultResourceManagerTest.class));

        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(resource);
        
        classLoader.setResourceManager(manager);
        
        Class<?> clazz = classLoader.loadClass(DefaultResourceManagerTest.class.getName());
        
        assertNotNull(clazz);
        
        //assertEquals(clazz.getClassLoader(), classLoader);
        
    }

}
