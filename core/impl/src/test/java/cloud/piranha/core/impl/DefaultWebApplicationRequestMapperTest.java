/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultWebApplicationRequestMapper class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationRequestMapperTest {

    /**
     * Test addServletMaping method.
     * 
     * <p>
     *  Validate passing null pattern throws an IllegalArgumentException.
     * </p>
     */
    @Test
    void testAddServletMapping2() {
        DefaultWebApplicationRequestMapper mapper = new DefaultWebApplicationRequestMapper();
        assertThrows(IllegalArgumentException.class, 
                () -> {mapper.addServletMapping("kaboom", (String[]) null);});
    }

    /**
     * Test addServletMapping method.
     * 
     * <p>
     *  Validate that we return the Servlet name(s) that already has / have been 
     *  mapped to the given URL pattern(s).
     * </p>
     */
    @Test
    void testAddServletMapping3() {
        DefaultWebApplicationRequestMapper mapper = new DefaultWebApplicationRequestMapper();
        mapper.addServletMapping("first", "/mapped");
        Set<String> already = mapper.addServletMapping("second", "/mapped");
        assertFalse(already.isEmpty());
    }
    
    /**
     * Test addServletMapping method.
     * 
     * <p>
     *  Validate that we prepend a slash when it is missing.
     * </p>
     */
    @Test
    void testAddServletMapping4() {
        DefaultWebApplicationRequestMapper mapper = new DefaultWebApplicationRequestMapper();
        mapper.addServletMapping("prepend", "mapped");
        assertEquals("/mapped", mapper.findServletMapping("/mapped").getPattern());
    }
    
    /**
     * Test addServletMapping method.
     * 
     * <p>
     *  Validate we set the default servlet for a "/" URL pattern.
     * </p>
     */
    @Test
    void testAddServletMapping5() {
        DefaultWebApplicationRequestMapper mapper = new DefaultWebApplicationRequestMapper();
        mapper.addServletMapping("theDefaultServlet", "/");
        assertEquals("theDefaultServlet", mapper.getDefaultServlet());
    }

    /**
     * Test findServletMapping method.
     * 
     * <p>
     *  Test finding an exact match.
     * </p>
     */
    @Test
    void testFindServletMapping() {
        DefaultWebApplicationRequestMapper mapper = new DefaultWebApplicationRequestMapper();
        mapper.addServletMapping("echo", "/echo");
        DefaultWebApplicationRequestMapping mapping = mapper.findServletMapping("/echo");
        assertTrue(mapping.isExact());
    }

    /**
     * Test findExactServletMatch method.
     *
     * @throws Exception
     */
    @Test
    void testFindExactServletMatch2() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(webAppRequestMapper);
        webApplication.addServlet("echo", new TestEcho1Servlet());
        webApplication.addServletMapping("echo", "/echo");
        webApplication.initialize();
        webApplication.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo");
        request.setWebApplication(webApplication);
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        response.setBodyOnly(true);
        webApplication.service(request, response);
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(webAppRequestMapper);
        webApplication.addServlet("echo", new TestEcho1Servlet());
        webApplication.addServletMapping("echo", "*.echo");
        webApplication.initialize();
        webApplication.start();
        
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo.echo");
        request.setWebApplication(webApplication);
        
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        response.setWebApplication(webApplication);
        
        webApplication.service(request, response);
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(webAppRequestMapper);
        webApplication.addServlet("echo", new TestEcho1Servlet());
        webApplication.addServletMapping("echo", "/echo/*");
        webApplication.initialize();
        webApplication.start();
        
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo2/test.echo");
        request.setWebApplication(webApplication);
        
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        response.setWebApplication(webApplication);
        
        webApplication.service(request, response);
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(webAppRequestMapper);
        webApplication.addServlet("echo", new TestEcho1Servlet());
        webApplication.addServletMapping("echo", "/echo/*");
        webApplication.initialize();
        webApplication.start();
        
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo");
        request.setWebApplication(webApplication);
        
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        response.setWebApplication(webApplication);
        
        webApplication.service(request, response);
        assertEquals("ECHO", new String(response.getResponseBytes()));
    }

    @Test
    void testFindPrefixServletMatch5() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(webAppRequestMapper);
        webApplication.addServlet("echo", new TestEcho1Servlet());
        webApplication.addServletMapping("echo", "/echo/*");
        webApplication.initialize();
        webApplication.start();
        
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo2");
        request.setWebApplication(webApplication);
        
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        response.setWebApplication(webApplication);
        
        webApplication.service(request, response);
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(webAppRequestMapper);
        webApplication.addServlet("echo2", new TestEcho1Servlet());
        webApplication.addServletMapping("echo2", "*.echo2");
        webApplication.initialize();
        webApplication.start();

        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo.echo");
        request.setWebApplication(new DefaultWebApplication());

        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        
        webApplication.service(request, response);

        assertNotEquals(200, response.getStatus());
    }

    /**
     * Test findPrefixServletMatch method.
     *
     * @throws Exception
     */
    @Test
    void testFindPrefixServletMatch() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(webAppRequestMapper);
        webApplication.addServlet("echo", new TestEcho1Servlet());
        webApplication.addServletMapping("echo", "/echo/*");
        webApplication.initialize();
        webApplication.start();
        
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo/test.echo");
        request.setWebApplication(webApplication);
        
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        response.setWebApplication(webApplication);

        webApplication.service(request, response);

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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(webAppRequestMapper);
        webApplication.addServlet("echo", new TestEcho1Servlet());
        webApplication.addServletMapping("echo", "/*");
        webApplication.initialize();
        webApplication.start();
        
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo/test.echo");
        request.setWebApplication(webApplication);
        
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(true);
        response.setWebApplication(webApplication);
        
        webApplication.service(request, response);
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
