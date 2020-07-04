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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the Cookie class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class CookieTest {

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        Cookie cookie = new Cookie("name", "value");
        Cookie clone = (Cookie) cookie.clone();
        assertEquals(cookie.getName(), clone.getName());
        assertEquals(cookie.getValue(), clone.getValue());
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        Cookie cookie = new Cookie("name", "value");
        assertNull(cookie.getComment());
        cookie.setComment("comment");
        assertEquals("comment", cookie.getComment());
    }

    /**
     * Test getDomain method.
     */
    @Test
    public void testGetDomain() {
        Cookie cookie = new Cookie("name", "value");
        assertNull(cookie.getDomain());
        cookie.setDomain("domain");
        assertEquals("domain", cookie.getDomain());
    }

    /**
     * Test getMaxAge method.
     */
    @Test
    public void testGetMaxAge() {
        Cookie cookie = new Cookie("name", "value");
        assertEquals(0, cookie.getMaxAge());
        cookie.setMaxAge(10);
        assertEquals(10, cookie.getMaxAge());
    }

    /**
     * Test getPath method.
     */
    @Test
    public void testGetPath() {
        Cookie cookie = new Cookie("name", "value");
        assertNull(cookie.getPath());
        cookie.setPath("path");
        assertEquals("path", cookie.getPath());
    }

    /**
     * Test getSecure method.
     */
    @Test
    public void testGetSecure() {
        Cookie cookie = new Cookie("name", "value");
        assertFalse(cookie.getSecure());
        cookie.setSecure(true);
        assertTrue(cookie.getSecure());
    }

    /**
     * Test getVersion method.
     */
    @Test
    public void testGetVersion() {
        Cookie cookie = new Cookie("name", "value");
        assertEquals(0, cookie.getVersion());
        cookie.setVersion(10);
        assertEquals(10, cookie.getVersion());
    }

    /**
     * Test isHttpOnly method.
     */
    @Test
    public void testIsHttpOnly() {
        Cookie cookie = new Cookie("name", "value");
        assertFalse(cookie.isHttpOnly());
        cookie.setHttpOnly(true);
        assertTrue(cookie.isHttpOnly());
    }

    /**
     * Test setValue method.
     */
    @Test
    public void testSetValue() {
        Cookie cookie = new Cookie("name", "value");
        assertEquals("value", cookie.getValue());
        cookie.setValue("newvalue");
        assertEquals("newvalue", cookie.getValue());
    }
}
