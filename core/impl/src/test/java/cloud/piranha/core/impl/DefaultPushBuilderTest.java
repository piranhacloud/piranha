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

import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultPushBuilder class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultPushBuilderTest {
    
    /**
     * Test addHeader method.
     */
    @Test
    void testAddHeader() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultPushBuilder builder = new DefaultPushBuilder(request);
        builder.addHeader("name", "value");
        assertEquals("value", builder.getHeader("name"));
        builder.removeHeader("name");
        assertNull(builder.getHeader("name"));
    }
    
    /**
     * Test getHeaderNames method.
     */
    @Test
    void testGetHeaderNames() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultPushBuilder builder = new DefaultPushBuilder(request);
        builder.addHeader("name", "value");
        Set<String> names = builder.getHeaderNames();
        assertFalse(names.isEmpty());
        assertTrue(names.contains("name"));
    }

    /**
     * Test method method.
     */
    @Test
    void testMethod() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultPushBuilder builder = new DefaultPushBuilder(request);
        builder.method("GET");
        assertEquals("GET", builder.getMethod());
    }
    
    /**
     * Test path method.
     */
    @Test
    void testPath() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultPushBuilder builder = new DefaultPushBuilder(request);
        builder.path("index.html");
        assertEquals("index.html", builder.getPath());
    }
    
    /**
     * Test push method.
     */
    @Test
    void testPush() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultPushBuilder builder = new DefaultPushBuilder(request);
        builder.path("myimage.png");
        assertEquals("myimage.png", builder.getPath());
        builder.push();
        assertNotEquals("myimage.png", builder.getPath());
    }
    
    /**
     * Test queryString method.
     */
    @Test
    void testQueryString() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultPushBuilder builder = new DefaultPushBuilder(request);
        builder.queryString("foo=bar");
        assertEquals("foo=bar", builder.getQueryString());
    }
    
    /**
     * Test sessionId method.
     */
    @Test
    void testSessionId() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultPushBuilder builder = new DefaultPushBuilder(request);
        builder.sessionId("mysessionid");
        assertEquals("mysessionid", builder.getSessionId());
    }
    
    /**
     * Test setHeader method.
     */
    @Test
    void testSetHeader() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultPushBuilder builder = new DefaultPushBuilder(request);
        builder.setHeader("name", "value");
        assertEquals("value", builder.getHeader("name"));
        builder.removeHeader("name");
        assertNull(builder.getHeader("name"));
    }
}
