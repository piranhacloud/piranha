/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

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
