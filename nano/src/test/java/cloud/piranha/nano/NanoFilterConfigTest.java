/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.nano;

import cloud.piranha.core.impl.DefaultWebApplication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 * THe JUnit tests for the NanoFilterConfig class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class NanoFilterConfigTest {

    /**
     * Test getInitParameter method.
     */
    @Test
    void testGetInitParameter() {
        NanoFilterConfig config = new NanoFilterConfig(null);
        assertNull(config.getInitParameter("name"));
    }

    /**
     * Test getInitParameterNames method.
     */
    @Test
    void testGetInitParameterNames() {
        NanoFilterConfig config = new NanoFilterConfig(null);
        assertNotNull(config.getInitParameterNames());
    }
    
    /**
     * Test getServletContext method.
     */
    @Test
    void testGetServletContext() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        NanoFilterConfig config = new NanoFilterConfig(webApplication);
        assertEquals(webApplication, config.getServletContext());
    }
    
    /**
     * Test getFilterName method.
     */
    @Test
    void testGetFilterName() {
        NanoFilterConfig config = new NanoFilterConfig(null);
        assertNotNull(config.getFilterName());
    }
    
    /**
     * Test setFilterName method.
     */
    @Test
    void testSetFilterName() {
        NanoFilterConfig config = new NanoFilterConfig(null);
        config.setFilterName("name");
        assertEquals("name", config.getFilterName());
    }
    
    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter() {
        NanoFilterConfig config = new NanoFilterConfig(null);
        config.setInitParameter("name", "value");
        assertEquals("value", config.getInitParameter("name"));
    }
}
