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
package cloud.piranha.resource;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultResourceManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultResourceManagerTest {

    /**
     * Test getResource method to not find a missing resource.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetResource() throws Exception {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DirectoryResource(new File("")));
        assertNull(manager.getResource("/doesnotexist"));
    }

    /**
     * Test getResource method to find the /src directory resource.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetResource2() throws Exception {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DirectoryResource(new File(".")));
        assertNotNull(manager.getResource("src"));
    }

    /**
     * Test getResourceAsStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetResource3() throws Exception {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DirectoryResource(new File(".")));
        manager.addResource(new DirectoryResource(new File("")));
        assertNotNull(manager.getResource("/src/main/java/cloud/piranha/resource/DefaultResourceManager.java"));
    }

    /**
     * Test getResourceAsStream method to not find a missing resource.
     */
    @Test
    void testGetResourceAsStream() {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DirectoryResource(new File("")));
        assertNull(manager.getResourceAsStream("/doesnotexist"));
    }

    /**
     * Test getResourceAsStream method to not find a missing resource.
     */
    @Test
    void testGetResourceAsStream2() {
        DefaultResourceManager manager = new DefaultResourceManager();
        manager.addResource(new DirectoryResource(new File(".")));
        manager.addResource(new DirectoryResource(new File("")));
        assertNotNull(manager.getResourceAsStream("/src/main/java/cloud/piranha/resource/DefaultResourceManager.java"));
    }
}
