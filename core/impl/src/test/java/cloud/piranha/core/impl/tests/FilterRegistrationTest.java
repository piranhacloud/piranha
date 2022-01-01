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
package cloud.piranha.core.impl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.impl.DefaultWebApplication;

/**
 * The JUnit tests for testing everything related to the FilterRegistration API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class FilterRegistrationTest {

    /**
     * Stores the web application.
     */
    protected WebApplication webApp;

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @BeforeEach
    void setUp() throws Exception {
        webApp = new DefaultWebApplication();
    }

    /**
     * Test getFilterRegistration method.
     */
    @Test
    void testGetFilterRegistration() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        assertNotNull(webApp.getFilterRegistration("filter"));
    }

    /**
     * Test getFilterRegistration method.
     */
    @Test
    void testGetFilterRegistration2() {
        webApp.addFilter("filter", "doesnotexist");
        assertNotNull(webApp.getFilterRegistration("filter"));
    }

    /**
     * Test getFilterRegistrations method.
     */
    @Test
    void testGetFilterRegistrations() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        assertFalse(webApp.getFilterRegistrations().isEmpty());
    }

    /**
     * Test getName method.
     */
    @Test
    void testGetName() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        FilterRegistration registration = webApp.getFilterRegistration("filter");
        assertEquals("filter", registration.getName());
    }

    /**
     * Test getClassName method.
     */
    @Test
    void testGetClassName() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        FilterRegistration registration = webApp.getFilterRegistration("filter");
        assertEquals(TestFilterRegistrationFilter.class.getCanonicalName(),
                registration.getClassName());
    }

    /**
     * Test getInitParameters method.
     */
    @Test
    void testGetInitParameters() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        FilterRegistration registration = webApp.getFilterRegistration("filter");
        assertNotNull(registration.getInitParameters());
    }

    /**
     * Test getUrlPatternMappings method.
     */
    @Test
    void testGetUrlPatternMappings() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        FilterRegistration registration = webApp.getFilterRegistration("filter");
        assertTrue(registration.getUrlPatternMappings().isEmpty());
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        FilterRegistration registration = webApp.getFilterRegistration("filter");
        registration.setInitParameter("name", "value");
        assertTrue(registration.setInitParameters(new HashMap<>()).isEmpty());
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters2() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        FilterRegistration registration = webApp.getFilterRegistration("filter");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(null, null);
        assertThrows(IllegalArgumentException.class, () -> registration.setInitParameters(parameters));
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters3() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        FilterRegistration registration = webApp.getFilterRegistration("filter");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", null);
        assertThrows(IllegalArgumentException.class, () -> registration.setInitParameters(parameters));
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters4() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        FilterRegistration registration = webApp.getFilterRegistration("filter");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        assertTrue(registration.setInitParameters(parameters).isEmpty());
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters5() {
        webApp.addFilter("filter", TestFilterRegistrationFilter.class);
        FilterRegistration registration = webApp.getFilterRegistration("filter");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        assertTrue(registration.setInitParameters(parameters).isEmpty());
        assertFalse(registration.setInitParameters(parameters).isEmpty());
    }

    /**
     * Test filter used in a test which tests adding a filter registration.
     */
    class TestFilterRegistrationFilter implements Filter {

        /**
         * Destroy the filter.
         */
        @Override
        public void destroy() {
        }

        /**
         * Process the filter.
         *
         * @param request the request.
         * @param response the response.
         * @param chain the chain.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        }

        /**
         * Initialize the filter.
         *
         * @param filterConfig the filter configuration.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
        }
    }
}
