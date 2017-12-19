/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import javax.servlet.MultipartConfigElement;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultServletEnvironment class.
 *
 * @author Manfred Riem (mriem@manorrock.com).
 */
public class DefaultServletEnvironmentTest {

    /**
     * Test getClassName method.
     */
    @Test
    public void testGetClassName() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, null, servlet);
        assertEquals(TestSnoopServlet.class.getName(), environment.getClassName());
    }

    /**
     * Test getInitParameter method.
     */
    @Test
    public void testGetInitParameter() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, null, servlet);
        environment.setInitParameter("name", "value");
        assertEquals("value", environment.getInitParameter("name"));
    }

    /**
     * Test getLoadOnStartup method.
     */
    @Test
    public void testGetLoadOnStartup() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, null, servlet);
        environment.setLoadOnStartup(1);
        assertEquals(1, environment.getLoadOnStartup());
    }

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, "Snoop", servlet);
        assertNotNull(environment.getName());
        assertEquals("Snoop", environment.getName());
    }

    /**
     * Test getRunAsRole method.
     */
    @Test
    public void testGetRunAsRole() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, null, servlet);
        environment.setRunAsRole("role");
        assertNotNull(environment.getRunAsRole());
        assertEquals("role", environment.getRunAsRole());
    }

    /**
     * Test setAsyncSupported method.
     */
    @Test
    public void testSetAsyncSupported() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, null, servlet);
        assertFalse(environment.isAsyncSupported());
        environment.setAsyncSupported(true);
        assertTrue(environment.isAsyncSupported());
    }

    /**
     * Test setMultipartConfig method.
     */
    @Test
    public void testMultipartConfig() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, null, servlet);
        assertNull(environment.getMultipartConfig());
        environment.setMultipartConfig(new MultipartConfigElement("/location"));
        assertNotNull(environment.getMultipartConfig());
    }
}
