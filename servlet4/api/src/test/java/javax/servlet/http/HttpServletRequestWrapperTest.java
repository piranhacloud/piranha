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
package javax.servlet.http;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the HttpServletRequestWrapper class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpServletRequestWrapperTest {

    /**
     * Test authenticate method.
     *
     * @throws Exception when an serious error occurs.
     */
    @Test
    void testAuthenticate() throws Exception {
        HttpServletRequestWrapper wrapper = new TestHttpServletRequest(null);
        HttpServletResponseWrapper response = new HttpServletResponseWrapper(null);
        assertFalse(wrapper.authenticate(response));
    }
    
    /**
     * Test getHttpServletMapping method.
     */
    @Test
    void testGetHttpServletMapping() { 
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(null);
        assertNotNull(wrapper.getHttpServletMapping());
        HttpServletMapping mapping = wrapper.getHttpServletMapping();
        assertEquals("", mapping.getMatchValue());
        assertEquals("", mapping.getPattern());
        assertEquals("", mapping.getServletName());
        assertNull(mapping.getMappingMatch());
    }
    
    /**
     * Test getTrailerFields method.
     */
    @Test
    void testGetTrailerFields() { 
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(null);
        assertNotNull(wrapper.getTrailerFields());
        Map<String, String> trailerFields = wrapper.getTrailerFields();
        assertTrue(trailerFields.isEmpty());
    }
    
    /**
     * Test isTrailerFieldsReady method.
     */
    @Test
    void testIsTrailerFieldsReady() { 
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(null);
        assertTrue(wrapper.isTrailerFieldsReady());
    }
    
    /**
     * Test newPushBuilder method.
     */
    @Test
    void testNewPushBuilder() { 
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(null);
        assertNull(wrapper.newPushBuilder());
    }
}
