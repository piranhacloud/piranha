/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * The JUnit test for AliasedDirectoryResource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AliasedDirectoryResourceTest {

    /**
     * Test getResource.
     */
    @Test
    public void testGetResource() {
        AliasedDirectoryResource resource = new AliasedDirectoryResource(null, "/alias");
        assertNull(resource.getResource("resource"));
    }

    /**
     * Test getResource.
     */
    @Test
    public void testGetResource2() {
        AliasedDirectoryResource resource = new AliasedDirectoryResource();
        resource.setRootDirectory(new File("src/main/java"));
        resource.setAlias("/alias");
        assertNotNull(resource.getResource("/alias/cloud/piranha/resource/DirectoryResource.java"));
    }

    /**
     * Test getResource method.
     */
    @Test
    public void testGetResource3() {
        AliasedDirectoryResource resource = new AliasedDirectoryResource(new File("src/test/java/org"), "/org");
        assertNull(resource.getResource("/cloud/piranha/resource/AliasedDirectoryResourceTest2.java"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    public void testGetResourceAsStream() {
        AliasedDirectoryResource resource = new AliasedDirectoryResource(null, "/alias");
        assertNull(resource.getResourceAsStream("/resource"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    public void testGetResourceAsStream2() {
        AliasedDirectoryResource resource = new AliasedDirectoryResource(new File("src/main/java"), "/alias");
        assertNotNull(resource.getResourceAsStream("/alias/cloud/piranha/resource/DirectoryResource.java"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    public void testGetResourceAsStream3() {
        AliasedDirectoryResource resource = new AliasedDirectoryResource(new File("src/main/java"), "/alias");
        assertNull(resource.getResourceAsStream("/alias/cloud/piranha/DirectoryResource.class"));
    }

    /**
     * Test getAlias method.
     */
    @Test
    public void testGetAlias() {
        AliasedDirectoryResource resource = new AliasedDirectoryResource(null, "/alias");
        assertEquals(resource.getAlias(), "/alias");
    }

    /**
     * Test getRootDirectory method.
     */
    @Test
    public void testGetRootDirectory() {
        AliasedDirectoryResource resource = new AliasedDirectoryResource();
        resource.setRootDirectory(new File("src/main/java"));
        resource.setAlias("/alias");
        assertNotNull(resource.getRootDirectory());
    }
}
