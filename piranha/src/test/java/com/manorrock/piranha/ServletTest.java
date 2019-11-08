/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha;

import com.manorrock.piranha.api.WebApplication;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * The JUnit tests for testing everything related to the addServlet method and
 * the Servlet API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletTest {

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
     * Test addServlet method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAddServlet() throws Exception {
        webApp.addServlet("Broken Servlet", new TestBrokenServlet());
        webApp.addServletMapping("Broken Servlet", "/echo");
        webApp.initialize();
        assertNotNull(webApp.getAttribute("Broken Servlet"));
        webApp.start();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/echo");
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        TestServletOutputStream outputStream = new TestServletOutputStream();
        outputStream.setResponse(response);
        response.setOutputStream(outputStream);
        webApp.service(request, response);
        assertEquals(500, response.getStatus());
    }

    /**
     * Test addServlet method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAddServlet2() throws Exception {
        assertNotNull(webApp.addServlet("Echo", TestServlet.class));
        webApp.addServletMapping("Echo", "/echo");
        webApp.initialize();
        webApp.start();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/echo");
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        TestServletOutputStream outputStream = new TestServletOutputStream();
        outputStream.setResponse(response);
        response.setOutputStream(outputStream);
        webApp.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("SUCCESS"));
    }

    /**
     * Test addServlet method.
     */
    @Test
    public void testAddServlet3() {
        ServletRegistration.Dynamic dynamic = webApp.addServlet("bogus", Servlet.class);
        assertNotNull(dynamic);
    }

    /**
     * Test addServlet method.
     */
    @Test
    public void testAddServlet4() {
        ServletRegistration.Dynamic dynamic = webApp.addServlet("echo", "servlet.EchoServlet");
        assertNotNull(dynamic);
    }

    /**
     * Test addServlet method.
     */
    @Test
    public void testAddServlet5() {
        ServletRegistration.Dynamic dynamic = webApp.addServlet("bogus", "servlet.BogusServlet");
        assertNotNull(dynamic);
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testService() throws Exception {
        assertNotNull(webApp.addServlet("Echo", TestServlet.class));
        webApp.addServletMapping("Echo", "/echo");
        webApp.initialize();
        webApp.start();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/echo");
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        TestServletOutputStream outputStream = new TestServletOutputStream();
        outputStream.setResponse(response);
        response.setOutputStream(outputStream);
        webApp.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("SUCCESS"));
    }

    /**
     * Test to verify instantiating a broken servlet fails.
     */
    public class TestBrokenServlet extends HttpServlet {

        /**
         * Initialize the servlet.
         *
         * @param servletConfig the servlet config.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        public void init(ServletConfig servletConfig) throws ServletException {
            servletConfig.getServletContext().setAttribute("Broken Servlet", true);
            throw new ServletException("Broken Servlet");
        }
    }

    /**
     * Test to verify instantiating a servlet succeeds.
     */
    public static class TestServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestServlet() {
        }

        /**
         * Initialize the servlet.
         *
         * @param servletConfig the servlet config.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        public void init(ServletConfig servletConfig) throws ServletException {
        }

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.getWriter().println("SUCCESS");
        }
    }
}
