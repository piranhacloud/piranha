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

import cloud.piranha.core.api.FilterEnvironment;
import cloud.piranha.core.api.WebApplication;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultFilterEnvironment class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultFilterEnvironmentTest {

    /**
     * Create a filter that just calls the next in the chain.
     * 
     * @return the filter.
     */
    private Filter createNoopFilter() {
        return new Filter() {
            @Override
            public void doFilter(
                    ServletRequest request, 
                    ServletResponse response, 
                    FilterChain chain) 
                    throws IOException, ServletException {
                
                chain.doFilter(request, response);
            }
        };
    }
    
    /**
     * Create the web application.
     * 
     * @return the web application.
     */
    private WebApplication createWebApplication() {
        return new DefaultWebApplication();
    }
    
    /**
     * Test getName method.
     */
    @Test
    void testGetName() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetNameFilter", createNoopFilter());
        FilterRegistration registration = webApplication.getFilterRegistration("testGetNameFilter");
        assertTrue(registration instanceof DefaultFilterEnvironment);
        assertEquals("testGetNameFilter", registration.getName());
    }
    
    /**
     * Test getClassName method.
     */
    @Test
    void testGetClassName() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetClassNameFilter", TestGetClassNameFilter.class);
        FilterRegistration registration = webApplication.getFilterRegistration("testGetClassNameFilter");
        assertTrue(registration instanceof DefaultFilterEnvironment);
        assertEquals(TestGetClassNameFilter.class.getName(),
                registration.getClassName());
    }
    
    /**
     * Test getInitParameterNames method.
     */
    @Test
    void testGetInitParameterNames() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetInitParametersFilter", createNoopFilter());
        FilterRegistration registration = webApplication.getFilterRegistration("testGetInitParametersFilter");
        registration.setInitParameter("key", "value");
        assertTrue(registration instanceof DefaultFilterEnvironment);
        FilterEnvironment environment = (FilterEnvironment) registration;
        assertNotNull(environment.getInitParameterNames());
        assertEquals("key", environment.getInitParameterNames().nextElement());
    }
    
    /**
     * Test getInitParameters method.
     */
    @Test
    void testGetInitParameters() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetInitParametersFilter", createNoopFilter());
        FilterRegistration registration = webApplication.getFilterRegistration("testGetInitParametersFilter");
        assertTrue(registration instanceof DefaultFilterEnvironment);
        assertNotNull(registration.getInitParameters());
    }
    
    /**
     * Test getUrlPatternMappings method.
     */
    @Test
    void testGetUrlPatternMappings() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetUrlPatternMappingsFilter", createNoopFilter());
        FilterRegistration registration = webApplication.getFilterRegistration("testGetUrlPatternMappingsFilter");
        assertTrue(registration instanceof DefaultFilterEnvironment);
        assertTrue(registration.getUrlPatternMappings().isEmpty());
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testSetInitParametersFilter", createNoopFilter());
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParametersFilter");
        registration.setInitParameter("name", "value");
        assertTrue(registration instanceof DefaultFilterEnvironment);
        assertTrue(registration.setInitParameters(new HashMap<>()).isEmpty());
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters2() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testSetInitParameters2Filter", createNoopFilter());
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParameters2Filter");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(null, null);
        assertTrue(registration instanceof DefaultFilterEnvironment);
        assertThrows(IllegalArgumentException.class, () -> registration.setInitParameters(parameters));
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters3() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testSetInitParameters3Filter", createNoopFilter());
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParameters3Filter");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", null);
        assertTrue(registration instanceof DefaultFilterEnvironment);
        assertThrows(IllegalArgumentException.class, () -> registration.setInitParameters(parameters));
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters4() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testSetInitParameters4Filter", createNoopFilter());
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParameters4Filter");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        assertTrue(registration instanceof DefaultFilterEnvironment);
        assertTrue(registration.setInitParameters(parameters).isEmpty());
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters5() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testSetInitParameters5Filter", createNoopFilter());
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParameters5Filter");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        assertTrue(registration instanceof DefaultFilterEnvironment);
        assertTrue(registration.setInitParameters(parameters).isEmpty());
        assertFalse(registration.setInitParameters(parameters).isEmpty());
    }

    /**
     * Test filter for getClassName method.
     */
    public class TestGetClassNameFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                FilterChain chain) throws IOException, ServletException {
        }
    }
}
