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
package cloud.piranha.core.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cloud.piranha.core.api.WebApplication;

/**
 * The JUnit tests for the Servlet API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ServletTest {

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @BeforeEach
    void setUp() throws Exception {
        webApplication = new DefaultWebApplication();
    }

    /**
     * Test addServlet method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddServlet() throws Exception {
        webApplication.addServlet("Broken Servlet", new TestBrokenServlet());
        webApplication.addServletMapping("Broken Servlet", "/echo");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo");
        webApplication.initialize();
        webApplication.start();
        try {
            webApplication.service(request, response);
            fail();
        } catch(RuntimeException ue) {
        }
        assertNotNull(webApplication.getAttribute("Broken Servlet"));
        webApplication.stop();
    }

    /**
     * Test addServlet method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddServlet2() throws Exception {
        try {
            assertNotNull(webApplication.addServlet("Echo", TestServlet.class));
            webApplication.addServletMapping("Echo", "/echo");
            TestWebApplicationResponse response = new TestWebApplicationResponse();
            response.setBodyOnly(false);
            TestWebApplicationRequest request = new TestWebApplicationRequest();
            request.setServletPath("/echo");
            webApplication.initialize();
            webApplication.start();
            webApplication.service(request, response);
            assertTrue(new String(response.getResponseBytes()).contains("200"));
            assertTrue(new String(response.getResponseBytes()).contains("SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Test addServlet method.
     */
    @Test
    void testAddServlet3() {
        ServletRegistration.Dynamic dynamic = webApplication.addServlet("bogus", Servlet.class);
        assertNotNull(dynamic);
    }

    /**
     * Test addServlet method.
     */
    @Test
    void testAddServlet4() {
        ServletRegistration.Dynamic dynamic = webApplication.addServlet("echo", "servlet.EchoServlet");
        assertNotNull(dynamic);
    }

    /**
     * Test addServlet method.
     */
    @Test
    void testAddServlet5() {
        ServletRegistration.Dynamic dynamic = webApplication.addServlet("bogus", "servlet.BogusServlet");
        assertNotNull(dynamic);
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService() throws Exception {
        assertNotNull(webApplication.addServlet("Echo", TestServlet.class));
        webApplication.addServletMapping("Echo", "/echo");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setBodyOnly(false);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/echo");
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertTrue(new String(response.getResponseBytes()).contains("200"));
        assertTrue(new String(response.getResponseBytes()).contains("SUCCESS"));
    }

    /**
     * Test to verify instantiating a broken servlet fails.
     */
    public static class TestBrokenServlet extends HttpServlet {

        private static final long serialVersionUID = 1L;

        /**
         * Initialize the servlet.
         *
         * @param servletConfig the servlet config.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        public void init(ServletConfig servletConfig) throws ServletException {
            servletConfig.getServletContext().setAttribute("Broken Servlet", true);
            throw new RuntimeException("Broken Servlet");
        }
    }

    /**
     * Test to verify instantiating a Servlet succeeds.
     */
    public static class TestServlet extends HttpServlet {

        private static final long serialVersionUID = 1L;

        public TestServlet() {
        }
        
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.getWriter().println("SUCCESS");
        }
    }
}
