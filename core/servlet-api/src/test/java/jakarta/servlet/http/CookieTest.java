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
package jakarta.servlet.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the Cookie class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class CookieTest {

    /**
     * Test constructor.
     */
    @Test
    void testConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cookie("$illegal", "value");
        });
    }
    
    /**
     * Test constructor.
     */
    @Test
    void testConstructor2() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cookie(",illegal", "value");
        });
    }
    
    /**
     * Test constructor.
     */
    @Test
    void testConstructor3() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cookie(";illegal", "value");
        });
    }
    
    /**
     * Test constructor.
     */
    @Test
    void testConstructor4() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cookie(" illegal", "value");
        });
    }
    
    /**
     * Test constructor.
     */
    @Test
    void testConstructor5() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cookie("\tillegal", "value");
        });
    }
    
    /**
     * Test constructor.
     */
    @Test
    void testConstructor6() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Cookie("\nillegal", "value");
        });
    }

    /**
     * Test clone method.
     */
    @Test
    void testClone() {
        Cookie cookie = new Cookie("name", "value");
        Cookie clone = (Cookie) cookie.clone();
        assertEquals(cookie.getName(), clone.getName());
        assertEquals(cookie.getValue(), clone.getValue());
    }

    /**
     * Test getComment method.
     */
    @Test
    void testGetComment() {
        Cookie cookie = new Cookie("name", "value");
        assertNull(cookie.getComment());
        cookie.setComment("comment");
        assertEquals("comment", cookie.getComment());
    }

    /**
     * Test getDomain method.
     */
    @Test
    void testGetDomain() {
        Cookie cookie = new Cookie("name", "value");
        assertNull(cookie.getDomain());
        cookie.setDomain("domain");
        assertEquals("domain", cookie.getDomain());
    }

    /**
     * Test getMaxAge method.
     */
    @Test
    void testGetMaxAge() {
        Cookie cookie = new Cookie("name", "value");
        assertEquals(-1, cookie.getMaxAge());
        cookie.setMaxAge(10);
        assertEquals(10, cookie.getMaxAge());
    }

    /**
     * Test getPath method.
     */
    @Test
    void testGetPath() {
        Cookie cookie = new Cookie("name", "value");
        assertNull(cookie.getPath());
        cookie.setPath("path");
        assertEquals("path", cookie.getPath());
    }

    /**
     * Test getSecure method.
     */
    @Test
    void testGetSecure() {
        Cookie cookie = new Cookie("name", "value");
        assertFalse(cookie.getSecure());
        cookie.setSecure(true);
        assertTrue(cookie.getSecure());
    }

    /**
     * Test getVersion method.
     */
    @Test
    void testGetVersion() {
        Cookie cookie = new Cookie("name", "value");
        assertEquals(0, cookie.getVersion());
        cookie.setVersion(10);
        assertEquals(10, cookie.getVersion());
    }

    /**
     * Test isHttpOnly method.
     */
    @Test
    void testIsHttpOnly() {
        Cookie cookie = new Cookie("name", "value");
        assertFalse(cookie.isHttpOnly());
        cookie.setHttpOnly(true);
        assertTrue(cookie.isHttpOnly());
    }

    /**
     * Test setValue method.
     */
    @Test
    void testSetValue() {
        Cookie cookie = new Cookie("name", "value");
        assertEquals("value", cookie.getValue());
        cookie.setValue("newvalue");
        assertEquals("newvalue", cookie.getValue());
    }
}
