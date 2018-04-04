/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpHeaderManagerTest {

    /**
     * Test addHeader method.
     */
    @Test
    public void testAddHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "VALUE");
        assertEquals("VALUE", manager.getHeader("NAME"));
    }

    /**
     * Test addHeader method.
     */
    @Test
    public void testAddHeader2() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "VALUE");
        manager.addHeader("NAME", "VALUE2");
        assertEquals("VALUE", manager.getHeader("NAME"));
        Enumeration<String> values = manager.getHeaders("NAME");
        assertTrue(values.hasMoreElements());
        assertNotNull(values.nextElement());
        assertTrue(values.hasMoreElements());
        assertNotNull(values.nextElement());
        assertFalse(values.hasMoreElements());
    }

    /**
     * Test containsHeader method.
     */
    @Test
    public void testContainsHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "VALUE");
        assertEquals("VALUE", manager.getHeader("NAME"));
        assertTrue(manager.containsHeader("NAME"));
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    public void testGetDateHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        manager.addHeader("NAME", format.format(new Date()));
        assertTrue(manager.getDateHeader("NAME") > 0);
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    public void testGetDateHeader2() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        assertEquals(-1, manager.getDateHeader("NAME"));
    }

    /**
     * Test getDateHeader method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDateHeader3() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "KABOOM");
        manager.getDateHeader("NAME");
    }

    /**
     * Test getHeader method.
     */
    @Test
    public void testGetHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        assertNull(manager.getHeader("NAME"));
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    public void testGetHeaderNames() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "VALUE");
        Enumeration<String> names = manager.getHeaderNames();
        assertTrue(names.hasMoreElements());
        assertEquals("NAME", names.nextElement());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    public void testGetHeaders() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        assertNull(manager.getHeaders("NAME"));
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    public void testGetIntHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "1");
        assertEquals(1, manager.getIntHeader("NAME"));
    }

    /**
     * Test getIntHeader method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetIntHeader2() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.setHeader("NAME", "abcd");
        assertEquals(1, manager.getIntHeader("NAME"));
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    public void testGetIntHeader3() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        assertEquals(-1, manager.getIntHeader("NAME"));
    }
}
