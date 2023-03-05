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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cloud.piranha.core.api.WebApplication;

/**
 * The JUnit tests for testing everything related to the Filter API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class FilterTest {

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
     * Test doFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testDoFilter() throws Exception {
        webApp.addFilter("Filter 1", new TestMultiple1Filter());
        webApp.addFilterMapping("Filter 1", "/*");
        webApp.addServlet("End Servlet", new TestEndServlet());
        webApp.addServletMapping("End Servlet", "/doFilter");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/doFilter");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.service(request, response);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testAddFilterGetClassName() throws Exception{
        Filter filter = new TestMultiple1Filter();
        FilterRegistration.Dynamic registration = webApp.addFilter("filter", filter);
        assertEquals(TestMultiple1Filter.class.getName(), registration.getClassName());
    }

    /**
     * Test filter used in a test with multiple filters.
     */
    class TestMultiple1Filter implements Filter {

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
            request.setAttribute("TestMultiple1Filter", "true");
            chain.doFilter(request, response);
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

    /**
     * Test filter used in a test with multiple filters.
     */
    class TestMultiple2Filter implements Filter {

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
            request.setAttribute("TestMultiple2Filter", "true");
            chain.doFilter(request, response);
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

    /**
     * Test servlet to end a filter chain with a 200 response code.
     */
    class TestEndServlet extends HttpServlet {

        private static final long serialVersionUID = 1L;

        /**
         * Handles the GET request.
         *
         * @param request the servlet request.
         * @param response the servlet response.
         * @throws IOException when an I/O error occurs
         * @throws ServletException when a servlet error occurs
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setStatus(200);
        }
    }
}
