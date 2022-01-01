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

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import cloud.piranha.core.impl.DefaultWebApplicationResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * All JUnit tests related to WebApplicationResponse functionality.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class WebApplicationResponseTest {

    /**
     * Test addDateHeader method.
     */
    @Test
    void testAddDateHeader() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.addDateHeader("name", 1234);
        assertEquals("Thu, 1 Jan 1970 00:00:01 GMT", response.getHeader("name"));
    }

    /**
     * Test addIntHeader method.
     */
    @Test
    void testAddIntHeader() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.addIntHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
    }

    /**
     * Test containsHeader method.
     */
    @Test
    void testContainsHeader() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertFalse(response.containsHeader("name"));
        response.addHeader("name", "value");
        assertTrue(response.containsHeader("name"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    void testEncodeRedirectUrl() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeRedirectURL("/encodeMe"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    void testEncodeRedirectUrl2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeRedirectUrl("/encodeMe"));
    }

    /**
     * Test encodeUrl method.
     */
    @Test
    void testEncodeUrl() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeURL("/encodeMe"));
    }

    /**
     * Test encodeUrl method.
     */
    @Test
    void testEncodeUrl2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeUrl("/encodeMe"));
    }

    /**
     * Test flushBuffer method.
     */
    @Test
    void testFlushBuffer() {
        try {
            DefaultWebApplicationResponse response = new TestWebApplicationResponse();
            response.flushBuffer();
        } catch (IOException ex) {
            fail();
        }
    }

    /**
     * Test getCharacterEncoding method.
     */
    @Test
    void testGetCharacterEncoding() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNull(response.getContentType());
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType2() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNull(response.getContentType());
        response.setContentType("text/html;charset=UTF-8");
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    void testGetHeaderNames() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNotNull(response.getHeaderNames());
        assertTrue(response.getHeaderNames().isEmpty());
        response.addHeader("name", "value");
        assertFalse(response.getHeaderNames().isEmpty());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    void testGetHeaders() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNotNull(response.getHeaders("name"));
        assertTrue(response.getHeaders("name").isEmpty());
        response.addHeader("name", "value");
        assertFalse(response.getHeaders("name").isEmpty());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocale() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertEquals(Locale.getDefault(), response.getLocale());
        response.setLocale(Locale.ITALIAN);
        assertEquals(Locale.ITALIAN, response.getLocale());
    }

    /**
     * Test getOutputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetOutputStream() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.getWriter();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> response.getOutputStream()));
    }

    /**
     * Test getWebApplication method.
     */
    @Test
    void testGetWebApplication() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNull(response.getWebApplication());
    }

    /**
     * Test getWriter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetWriter() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.getOutputStream();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> response.getWriter()));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testRedirect() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet", TestRedirectServlet.class);
        webApplication.addServletMapping("Servlet", "/servlet");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/servlet");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> webApplication.service(request, response)));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testRedirect2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet2a", TestRedirect2aServlet.class);
        webApplication.addServlet("Servlet2b", TestRedirect2bServlet.class);
        webApplication.addServletMapping("Servlet2a", "/servlet2a");
        webApplication.addServletMapping("Servlet2b", "/servlet2a/servlet2b");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/servlet2a");
        request.setWebApplication(webApplication);
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
        assertEquals("http://localhost:80/servlet2b", response.getHeader("Location"));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testRedirect3() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet3", TestRedirect3Servlet.class);
        webApplication.addServletMapping("Servlet3", "/servlet3");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/servlet3");
        request.setWebApplication(webApplication);
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
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
    void testRedirect4() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet4", TestRedirect4Servlet.class);
        webApplication.addServletMapping("Servlet4", "/servlet4");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/servlet4");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
        assertEquals("http://this.is.outside/and_absolute", response.getHeader("Location"));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testRedirect5() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet5a", TestRedirect5aServlet.class);
        webApplication.addServlet("Servlet5b", TestRedirect5bServlet.class);
        webApplication.setContextPath("/app");
        webApplication.addServletMapping("Servlet5a", "/servlet5a");
        webApplication.addServletMapping("Servlet5b", "/servlet5a/servlet5b");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContextPath("/app");
        request.setServletPath("/servlet5a");
        request.setWebApplication(webApplication);
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
        assertEquals("http://localhost:80/app/servlet5b", response.getHeader("Location"));
    }

    /**
     * Test reset method.
     */
    @Test
    void testReset() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.reset();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSendError() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error");
        response.flushBuffer();
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("error", response.getStatusMessage());
        assertTrue(response.isCommitted());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSendError2() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.sendError(SC_INTERNAL_SERVER_ERROR, "error");
        response.flushBuffer();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> response.sendError(SC_INTERNAL_SERVER_ERROR)));
    }

    /**
     * Test setContentLength method.
     */
    @Test
    void testSetContentLength() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLength(12);
        assertEquals(12, response.getContentLength());
    }

    /**
     * Test setContentLength method.
     */
    @Test
    void testSetContentLength2() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLengthLong(12L);
        assertEquals(12, response.getContentLength());
    }

    /**
     * Test setContentLength method.
     */
    @Test
    void testSetContentLength3() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLength(12);
        assertEquals("12", response.getHeader("Content-Length"));
    }

    /**
     * Test setContentLength method.
     */
    @Test
    void testSetContentLength4() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLengthLong(12L);
        assertEquals("12", response.getHeader("Content-Length"));
    }

    /**
     * Test setContentType.
     */
    @Test
    void testSetContentType() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentType(null);
        assertNull(response.getContentType());
    }

    /**
     * Test setContentType method.
     */
    @Test
    void testSetContentType2() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
        response.setBodyOnly(false);
        response.flushBuffer();
        assertTrue(new String(response.getResponseBytes()).contains(
                "Content-Type: text/html;charset=ISO-8859-1"));
    }

    /**
     * Test setContentType method.
     */
    @Test
    void testContentType3() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentType("text/html;charset=UTF-8");
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
        response.setBodyOnly(false);
        response.flushBuffer();
        assertTrue(new String(response.getResponseBytes()).contains(
                "Content-Type: text/html;charset=UTF-8\n"));
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    void testSetDateHeader() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setDateHeader("header", 1000);
        response.setDateHeader("header", 2000);
        assertEquals("Thu, 1 Jan 1970 00:00:02 GMT", response.getHeader("header"));
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    void testSetDateHeader2() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setDateHeader("header", 1000);
        assertEquals("Thu, 1 Jan 1970 00:00:01 GMT", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    void testSetHeader() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setHeader("header", "value1");
        response.addHeader("header", "value2");
        response.setHeader("header", "value3");
        assertEquals("value3", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    void testSetHeader2() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setHeader("header", "value1");
        response.setHeader("header", "value2");
        assertEquals("value2", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    void testSetHeader3() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setHeader("header", "value1");
        assertEquals("value1", response.getHeader("header"));
    }

    /**
     * Test setIntHeader method.
     */
    @Test
    void testSetIntHeader() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setIntHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
    }

    /**
     * Test setStatus method.
     */
    @Test
    void testSetStatus() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setStatus(500);
        assertEquals(500, response.getStatus());
    }

    /**
     * Test setStatus method.
     */
    @Test
    void testSetStatus2() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setStatus(HttpServletResponse.SC_NOT_FOUND, "Not found");
        assertEquals(404, response.getStatus());
    }

    /**
     * Test servlet that is used by testRedirect2.
     */
    public static class TestRedirect2aServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestRedirect2aServlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("servlet2b");
        }
    }

    /**
     * Test servlet that is used by testRedirect2.
     */
    public static class TestRedirect2bServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestRedirect2bServlet() {
        }

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
     * Test servlet that is used by testRedirect3.
     */
    public static class TestRedirect3Servlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestRedirect3Servlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("/relative_to_root");
        }
    }

    /**
     * Test servlet that is used by testRedirect4.
     */
    public static class TestRedirect4Servlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestRedirect4Servlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("http://this.is.outside/and_absolute");
        }
    }
    
    /**
     * Test servlet that is used by testRedirect5.
     */
    public static class TestRedirect5aServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestRedirect5aServlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("servlet5b");
        }
    }

    /**
     * Test servlet that is used by testRedirect5.
     */
    public static class TestRedirect5bServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestRedirect5bServlet() {
        }

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
     * Test servlet that is used by testRedirect.
     */
    public static class TestRedirectServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestRedirectServlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.flushBuffer();
            response.sendRedirect("/this_should_throw_an_illegal_state_exception");
        }
    }
}
