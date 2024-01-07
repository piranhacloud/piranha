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
package cloud.piranha.resource.impl;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for the ByteArrayResource class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ByteArrayResourceTest {
    
    /**
     * Test getAllLocations method.
     */
    @Test
    void testGetAllLocations() {
        ByteArrayResource resource = new ByteArrayResource("mylocation", null);
        assertEquals(1, resource.getAllLocations().count());
    }
    
    /**
     * Test getBytes method.
     */
    @Test
    void testGetBytes() {
        ByteArrayResource resource = new ByteArrayResource("", null);
        assertNull(resource.getBytes());
    }

    /**
     * Test getResource method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetResource() throws Exception {
        ByteArrayResource resource = new ByteArrayResource("mylocation", new byte[0]);
        assertNotNull(resource.getResource("mylocation"));
        assertNull(resource.getResource("notmylocation"));
        assertTrue(resource.getResource("mylocation").openStream() instanceof ByteArrayInputStream);
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    void testGetResourceAsStream() {
        ByteArrayResource resource = new ByteArrayResource("mylocation", new byte[0]);
        assertTrue(resource.getResourceAsStream("mylocation") instanceof ByteArrayInputStream);
        assertNull(resource.getResourceAsStream("notmylocation"));
    }
}
