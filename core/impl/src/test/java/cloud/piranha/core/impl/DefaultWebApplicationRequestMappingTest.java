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
package cloud.piranha.core.impl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The JUnit tests for the DefaultWebApplicationRequestMapping class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationRequestMappingTest {
    
    /**
     * Test getMatchValue method.
     */
    @Test
    public void testGetMatchValue() {
        DefaultWebApplicationRequestMapping mapping = new DefaultWebApplicationRequestMapping(null);
        assertNull(mapping.getMatchValue());
        mapping.setMatchValue("/match");
        assertEquals("/match", mapping.getMatchValue());
    }

    /**
     * Test getPattern method.
     */
    @Test
    public void testGetPattern() {
        DefaultWebApplicationRequestMapping mapping = new DefaultWebApplicationRequestMapping("/*");
        assertEquals("/*", mapping.getPattern());
    }

    /**
     * Test isExact method.
     */
    @Test
    public void testIsExact() {
        DefaultWebApplicationRequestMapping mapping = new DefaultWebApplicationRequestMapping("/exact");
        assertFalse(mapping.isExact());
        mapping.setExact(true);
        assertTrue(mapping.isExact());
    }

    /**
     * Test isExtension method.
     */
    @Test
    public void testIsExtension() {
        DefaultWebApplicationRequestMapping mapping = new DefaultWebApplicationRequestMapping("*.html");
        assertFalse(mapping.isExtension());
        mapping.setExtension(true);
        assertTrue(mapping.isExtension());
    }

    /**
     * Test setPattern method.
     */
    @Test
    public void testSetPattern() {
        DefaultWebApplicationRequestMapping mapping = new DefaultWebApplicationRequestMapping("/pattern1");
        assertEquals("/pattern1", mapping.getPattern());
        mapping.setPattern("/pattern2");
        assertEquals("/pattern2", mapping.getPattern());
    }
}
