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
package cloud.piranha.servlet4.impl;

import cloud.piranha.servlet4.impl.DefaultWebApplicationRequestMapper;
import cloud.piranha.servlet4.impl.DefaultWebApplicationRequestMapping;
import cloud.piranha.servlet4.impl.DefaultWebApplication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * The tests for the ServletRequestMapper class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationRequestMapperTest {

    /**
     * Test findExactServletMatch method.
     *
     * @throws Exception
     */
    @Test
    void testFindExactServletMatch() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "/echo");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        webApp.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBytes()));
    }

    /**
     * Test findExtensionServletMatch method.
     *
     * @throws Exception
     */
    @Test
    void testFindExtensionServletMatch() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "*.echo");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo.echo");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        webApp.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBytes()));
    }

    /**
     * Test findServletPrefixMatch method.
     *
     * @throws Exception
     */
    @Test
    void testFindPrefixServletMatch3() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "/echo/*");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo2/test.echo");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        webApp.service(request, response);
        assertEquals(404, response.getStatus());
    }

    /**
     * Test findServletPrefixMatch method.
     *
     * @throws Exception
     */
    @Test
    void testFindPrefixServletMatch4() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "/echo/*");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        webApp.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBytes()));
    }

    @Test
    void testFindPrefixServletMatch5() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "/echo/*");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo2");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        webApp.service(request, response);
        assertEquals(404, response.getStatus());
    }

    /**
     * Test findExtensionServletMatch method.
     *
     * @throws Exception
     */
    @Test
    void testFindExtensionServletMatch2() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo2", new TestEcho1Servlet());
        webApp.addServletMapping("echo2", "*.echo2");
        webApp.initialize();
        webApp.start();

        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo.echo");
        request.setWebApplication(new DefaultWebApplication());

        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.service(request, response);

        assertTrue(response.getStatus() != 200);
    }

    /**
     * Test findPrefixServletMatch method.
     *
     * @throws Exception
     */
    @Test
    void testFindPrefixServletMatch() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "/echo/*");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo/test.echo");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        webApp.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBytes()));
    }

    /**
     * Test findPrefixServletMatch method.
     *
     * @throws Exception
     */
    @Test
    void testFindPrefixServletMatch2() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("echo", new TestEcho1Servlet());
        webApp.addServletMapping("echo", "/*");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo/test.echo");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        webApp.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBytes()));
    }

    /**
     * Test addFilterMapping method.
     */
    @Test
    void testAddFilterMapping() {
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
    void testAddFilterMapping2() {
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
    void testAddFilterMapping3() {
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
    void testAddFilterMapping4() {
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
    void testAddFilterMapping5() {
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
    void testAddServletMapping() {
        DefaultWebApplicationRequestMapper requestMapper = new DefaultWebApplicationRequestMapper();
        Set<String> result = requestMapper.addServletMapping("servlet", "*.html");
        assertTrue(result.isEmpty());
        result = requestMapper.addServletMapping("servlet", "*.html");
        assertTrue(result.isEmpty());
        DefaultWebApplicationRequestMapping mapping = requestMapper.findServletMapping("/index.html?q=keyword");
        assertNotNull(mapping);
    }
}
