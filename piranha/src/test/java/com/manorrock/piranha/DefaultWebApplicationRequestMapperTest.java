/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.util.Collection;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The tests for the ServletRequestMapper class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationRequestMapperTest {

    /**
     * Test findExactServletMatch method.
     *
     * @throws Exception
     */
    @Test
    public void testFindExactServletMatch() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "/echo");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setServletPath("/echo");

        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        webApp.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBody()));
    }

    /**
     * Test findExtensionServletMatch method.
     *
     * @throws Exception
     */
    @Test
    public void testFindExtensionServletMatch() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "*.echo");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setServletPath("/echo.echo");

        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        webApp.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBody()));
    }

    /**
     * Test findExtensionServletMatch method.
     *
     * @throws Exception
     */
    @Test
    public void testFindExtensionServletMatch2() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo2", new TestEcho1Servlet());
        webApp.addServletMapping("echo2", "*.echo2");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setServletPath("/echo.echo");

        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        webApp.service(request, response);

        assertTrue(response.getStatus() != 200);
    }

    /**
     * Test findPrefixServletMatch method.
     *
     * @throws Exception
     */
    @Test
    public void testFindPrefixServletMatch() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "/echo/*");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setServletPath("/echo/test.echo");

        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        webApp.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBody()));
    }

    /**
     * Test findPrefixServletMatch method.
     *
     * @throws Exception
     */
    @Test
    public void testFindPrefixServletMatch2() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "/*");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setServletPath("/echo/test.echo");

        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        webApp.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBody()));
    }

    /**
     * Test addFilterMapping method.
     */
    @Test
    public void testAddFilterMapping() {
        DefaultWebApplicationRequestMapper requestMapper = new DefaultWebApplicationRequestMapper();
        Set<String> result = requestMapper.addFilterMapping("filter", "/*");
        assertTrue(result.isEmpty());
        Collection<String> filters = requestMapper.findFilterMappings("/one_two");
        assertFalse(filters.isEmpty());
    }

    /**
     * Test addFilterMapping method.
     */
    @Test
    public void testAddFilterMapping2() {
        DefaultWebApplicationRequestMapper requestMapper = new DefaultWebApplicationRequestMapper();
        Set<String> result = requestMapper.addFilterMapping("filter", "/exactly_this");
        assertTrue(result.isEmpty());
        Collection<String> filters = requestMapper.findFilterMappings("/exactly_this");
        assertFalse(filters.isEmpty());
    }

    /**
     * Test addFilterMapping method.
     */
    @Test
    public void testAddFilterMapping3() {
        DefaultWebApplicationRequestMapper requestMapper = new DefaultWebApplicationRequestMapper();
        Set<String> result = requestMapper.addFilterMapping("filter", "*.html");
        assertTrue(result.isEmpty());
        Collection<String> filters = requestMapper.findFilterMappings("/index.html");
        assertFalse(filters.isEmpty());
    }

    /**
     * Test addFilterMapping method.
     */
    @Test
    public void testAddFilterMapping4() {
        DefaultWebApplicationRequestMapper requestMapper = new DefaultWebApplicationRequestMapper();
        Set<String> result = requestMapper.addFilterMapping("filter", "*.html");
        assertTrue(result.isEmpty());
        result = requestMapper.addFilterMapping("filter", "*.html");
        assertFalse(result.isEmpty());
        Collection<String> filters = requestMapper.findFilterMappings("/index.html");
        assertFalse(filters.isEmpty());
    }

    /**
     * Test addFilterMapping method.
     */
    @Test
    public void testAddFilterMapping5() {
        DefaultWebApplicationRequestMapper requestMapper = new DefaultWebApplicationRequestMapper();
        Set<String> result = requestMapper.addFilterMapping("filter", "*.html");
        assertTrue(result.isEmpty());
        result = requestMapper.addFilterMapping("filter", "*.html");
        assertFalse(result.isEmpty());
        Collection<String> filters = requestMapper.findFilterMappings("/index.html?q=keyword");
        assertFalse(filters.isEmpty());
    }
    
    /**
     * Test addServletMapping method.
     */
    @Test
    public void testAddServletMapping() {
        DefaultWebApplicationRequestMapper requestMapper = new DefaultWebApplicationRequestMapper();
        Set<String> result = requestMapper.addServletMapping("servlet", "*.html");
        assertTrue(result.isEmpty());
        result = requestMapper.addServletMapping("servlet", "*.html");
        assertFalse(result.isEmpty());
        DefaultWebApplicationRequestMapping mapping = requestMapper.findServletMapping("/index.html?q=keyword");
        assertNotNull(mapping);
    }
}
