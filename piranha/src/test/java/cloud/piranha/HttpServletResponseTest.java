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
package cloud.piranha;

import cloud.piranha.api.WebApplication;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * The JUnit tests for testing everything related to the HttpServletResponse
 * API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpServletResponseTest {

    /**
     * Stores the HTTP servlet request.
     */
    private TestWebApplicationRequest request;

    /**
     * Stores the HTTP servlet response.
     */
    private TestWebApplicationResponse response;

    /**
     * Stores the web application.
     */
    private WebApplication webApplication;

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @Before
    public void setUp() throws Exception {
        request = new TestWebApplicationRequest();
        response = new TestWebApplicationResponse();
        webApplication = new DefaultWebApplication();
        request.setWebApplication(webApplication);
        response.setWebApplication(webApplication);
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testRedirect() throws Exception {
        webApplication.addServlet("Servlet", TestRedirectAfterFlushServlet.class);
        webApplication.addServletMapping("Servlet", "/servlet");
        request.setServletPath("/servlet");
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testRedirect2() throws Exception {
        webApplication.addServlet("Servlet2a", TestRedirect2aServlet.class);
        webApplication.addServlet("Servlet2b", TestRedirect2bServlet.class);
        webApplication.addServletMapping("Servlet2a", "/servlet2a");
        webApplication.addServletMapping("Servlet2b", "/servlet2a/servlet2b");
        request.setServletPath("/servlet2a");
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
        assertEquals("http://localhost:80/servlet2a/servlet2b", response.getHeader("Location"));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testRedirect3() throws Exception {
        webApplication.addServlet("Servlet3", TestRedirect3Servlet.class);
        webApplication.addServletMapping("Servlet3", "/servlet3");
        request.setServletPath("/servlet3");
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
        assertEquals("http://localhost:80/relative_to_root", response.getHeader("Location"));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testRedirect4() throws Exception {
        webApplication.addServlet("Servlet4", TestRedirect4Servlet.class);
        webApplication.addServletMapping("Servlet4", "/servlet4");
        request.setServletPath("/servlet4");
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
        assertEquals("http://this.is.outside/and_absolute", response.getHeader("Location"));
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    public void testSetDateHeader() {
        response.setDateHeader("header", 1);
        response.setDateHeader("header", 2);
        assertEquals("2", response.getHeader("header"));
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    public void testSetDateHeader2() {
        response.setDateHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    public void testSetHeader() {
        response.setHeader("header", "value1");
        response.addHeader("header", "value2");
        response.setHeader("header", "value3");
        assertEquals("value3", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    public void testSetHeader2() {
        response.setHeader("header", "value1");
        response.setHeader("header", "value2");
        assertEquals("value2", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    public void testSetHeader3() {
        response.setHeader("header", "value1");
        assertEquals("value1", response.getHeader("header"));
    }

    /**
     * Test setIntHeader method.
     */
    @Test
    public void testSetIntHeader() {
        response.setIntHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
    }

    /**
     * Test setStatus method.
     */
    @Test
    public void testSetStatus() {
        response.setStatus(500);
        assertEquals(500, response.getStatus());
    }

    /**
     * Test setStatus method.
     */
    @Test
    public void testSetStatus2() {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND, "Not found");
        assertEquals(404, response.getStatus());
    }

    /**
     * Test servlet that sends a redirect.
     */
    public static class TestRedirect2aServlet extends HttpServlet {

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("servlet2b");
        }
    }

    /**
     * Test servlet that receives the redirect.
     */
    public static class TestRedirect2bServlet extends HttpServlet {

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.print("SUCCESS");
            writer.flush();
        }
    }

    /**
     * Test servlet that sends a redirect.
     */
    public static class TestRedirect3Servlet extends HttpServlet {

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("/relative_to_root");
        }
    }

    /**
     * Test servlet that sends a redirect.
     */
    public static class TestRedirect4Servlet extends HttpServlet {

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("http://this.is.outside/and_absolute");
        }
    }

    /**
     * Test servlet that tries to send a redirect after flushing the buffer.
     */
    public static class TestRedirectAfterFlushServlet extends HttpServlet {

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.flushBuffer();
            response.sendRedirect("/this_should_throw_an_illegal_state_exception");
        }
    }
}
