/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

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
