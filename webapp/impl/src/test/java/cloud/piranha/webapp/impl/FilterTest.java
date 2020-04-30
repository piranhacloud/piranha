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
package cloud.piranha.webapp.impl;

import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.api.WebApplication;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * The JUnit tests for testing everything related to the addFilter method and
 * the Filter API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class FilterTest {

    /**
     * Stores the web application.
     */
    protected WebApplication webApp;

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @Before
    public void setUp() throws Exception {
        webApp = new DefaultWebApplication();
    }

    /**
     * Test addFilter method.
     */
    @Test
    public void testAddFilter() {
        webApp.addFilter("Broken Filter", new TestBrokenFilter());
        webApp.initialize();
        assertNotNull(webApp.getAttribute("Broken Filter"));
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAddFilter2() throws Exception {
        webApp.addFilter("Filter 1", new TestMultiple1Filter());
        webApp.addFilterMapping("Filter 1", "/*");
        webApp.addFilter("Filter 2", new TestMultiple2Filter());
        webApp.addFilterMapping("Filter 2", "/*");
        webApp.addServlet("End Servlet", new TestEndServlet());
        webApp.addServletMapping("End Servlet", "/multipleFilters");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/multipleFilters");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.service(request, response);
        assertEquals(200, response.getStatus());
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddFilter4() throws Exception {
        webApp.initialize();
        webApp.start();
        webApp.addFilter("filter", new TestMultiple1Filter());
    }

    /**
     * Test addFilter method.
     */
    @Test
    public void testAddFilter5() {
        assertNotNull(webApp.addFilter("filter", TestMultiple1Filter.class));
        assertNotNull(webApp.getFilterRegistration("filter"));
        assertEquals(TestMultiple1Filter.class.getCanonicalName(), webApp.getFilterRegistration("filter").getClassName());
    }

    /**
     * Test addFilter method.
     */
    @Test
    public void testAddFilter6() {
        assertNotNull(webApp.addFilter("filter", "doesnotexit"));
    }

    /**
     * Test addFilter methd.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddFilter7() throws Exception {
        webApp.initialize();
        webApp.start();
        webApp.addFilter("filter", "should throw IllegalStateException");
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddFilter8() throws Exception {
        webApp.initialize();
        webApp.addFilter(null, "filter name is null so throw IllegalArgumentException");
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddFilter9() throws Exception {
        webApp.initialize();
        webApp.addFilter(null, Filter.class);
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAddFilter10() throws Exception {
        webApp.initialize();
        assertNotNull(webApp.addFilter("filter", Filter.class));
        assertNotNull(webApp.addFilter("filter", Filter.class));
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAddFilter11() throws Exception {
        webApp.initialize();
        webApp.addFilter("filter", "InCompleteRegistrationFilter");
        webApp.addFilter("filter", "InCompleteRegistrationFilter");
    }

    /**
     * Test addFilter method.
     */
    @Test
    public void testAddFilter12() {
        assertNotNull(webApp.addFilter("filter", "InCompleteRegistrationFilter"));
        assertNotNull(webApp.getFilterRegistration("filter"));
    }

    /**
     * Test doFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testDoFilter() throws Exception {
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

    /**
     * Test broken filter.
     */
    public class TestBrokenFilter implements Filter {

        /**
         * Initialize the filter.
         *
         * @param filterConfig the filter config.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            ServletContext servletContext = filterConfig.getServletContext();
            servletContext.setAttribute("Broken Filter", true);
            throw new ServletException("Broken Filter");
        }

        /**
         * Do the filter processing.
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
         * Destroy the filter.
         */
        @Override
        public void destroy() {
        }
    }

    /**
     * Test filter used in a test with multiple filters.
     */
    public class TestMultiple1Filter implements Filter {

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
    public class TestMultiple2Filter implements Filter {

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
    public class TestEndServlet extends HttpServlet {

        /**
         * Handles the GET request.
         *
         * @param request the servlet request.
         * @param response the servlet response.
         * @throws IOException when an I/O error occurs
         * @throws ServletException when a servlet error occurs
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            response.setStatus(200);
        }
    }
}
