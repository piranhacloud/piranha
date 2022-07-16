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

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
class DefaultWebApplicationResponseTest {

    /**
     * Test addDateHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddDateHeader() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.addDateHeader("name", 1234);
        assertEquals("Thu, 1 Jan 1970 00:00:01 GMT", response.getHeader("name"));
        response.close();
    }

    /**
     * Test addIntHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddIntHeader() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.addIntHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
        response.close();
    }

    /**
     * Test containsHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testContainsHeader() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertFalse(response.containsHeader("name"));
        response.addHeader("name", "value");
        assertTrue(response.containsHeader("name"));
        response.close();
    }

    /**
     * Test encodeRedirectUrl method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testEncodeRedirectUrl() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeRedirectURL("/encodeMe"));
        response.close();
    }

    /**
     * Test encodeUrl method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testEncodeUrl() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeURL("/encodeMe"));
        response.close();
    }

    /**
     * Test flushBuffer method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testFlushBuffer() {
        try {
            DefaultWebApplicationResponse response = new TestWebApplicationResponse();
            response.flushBuffer();
            response.close();
        } catch (IOException ex) {
            fail();
        }
    }

    /**
     * Test getCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetCharacterEncoding() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
        response.close();
    }

    /**
     * Test getContentType method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContentType() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNull(response.getContentType());
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
        response.close();
    }

    /**
     * Test getContentType method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContentType2() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNull(response.getContentType());
        response.setContentType("text/html;charset=UTF-8");
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
        response.close();
    }

    /**
     * Test getHeaderNames method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetHeaderNames() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNotNull(response.getHeaderNames());
        assertTrue(response.getHeaderNames().isEmpty());
        response.addHeader("name", "value");
        assertFalse(response.getHeaderNames().isEmpty());
        response.close();
    }

    /**
     * Test getHeaders method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetHeaders() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNotNull(response.getHeaders("name"));
        assertTrue(response.getHeaders("name").isEmpty());
        response.addHeader("name", "value");
        assertFalse(response.getHeaders("name").isEmpty());
        response.close();
    }

    /**
     * Test getLocale method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetLocale() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertEquals(Locale.getDefault(), response.getLocale());
        response.setLocale(Locale.ITALIAN);
        assertEquals(Locale.ITALIAN, response.getLocale());
        response.close();
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
        response.close();
    }

    /**
     * Test getWebApplication method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetWebApplication() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNull(response.getWebApplication());
        response.close();
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
        response.close();
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
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testReset() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        response.reset();
        assertEquals(200, response.getStatus());
        response.close();
    }

    /**
     * Test reset method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testReset2() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
        response.reset();
        assertNotEquals("UTF-8", response.getCharacterEncoding());
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSendError() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.sendError(SC_INTERNAL_SERVER_ERROR, "error");
        response.flushBuffer();
        assertEquals(SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("error", response.getStatusMessage());
        assertTrue(response.isCommitted());
        response.close();
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
        response.close();
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        String defaultEncoding = response.getCharacterEncoding();
        response.setCharacterEncoding("UTF-8");
        assertTrue("UTF-8".equalsIgnoreCase(response.getCharacterEncoding()));
        response.setCharacterEncoding(null);
        assertTrue((defaultEncoding == null && response.getCharacterEncoding() == null)
                || (defaultEncoding != null && defaultEncoding.equalsIgnoreCase(response.getCharacterEncoding())));
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding2() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        String defaultEncoding = response.getCharacterEncoding();
        response.setCharacterEncoding("UTF-8");
        assertTrue("UTF-8".equalsIgnoreCase(response.getCharacterEncoding()));
        response.setCharacterEncoding(null);
        assertTrue((defaultEncoding == null && response.getCharacterEncoding() == null)
                || (defaultEncoding != null && defaultEncoding.equalsIgnoreCase(response.getCharacterEncoding())));
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding3() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setCharacterEncoding("does-not-exist");
        assertTrue("does-not-exist".equalsIgnoreCase(response.getCharacterEncoding()));
        assertThrows(UnsupportedEncodingException.class, () -> {
            response.getWriter();
        });
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding4() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setContentType("text/html");
        response.setCharacterEncoding("ISO-8859-7");
        String contentType = response.getContentType();
        assertTrue(contentType.toLowerCase().contains("text/html"));
        assertTrue(contentType.toLowerCase().contains("charset"));
        assertTrue(contentType.toLowerCase().contains("iso-8859-7"));
    }

    /**
     * Test setContentLength method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetContentLength() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLength(12);
        assertEquals(12, response.getContentLength());
        response.close();
    }

    /**
     * Test setContentLength method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetContentLength2() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLengthLong(12L);
        assertEquals(12, response.getContentLength());
        response.close();
    }

    /**
     * Test setContentLength method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetContentLength3() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLength(12);
        assertEquals("12", response.getHeader("Content-Length"));
        response.close();
    }

    /**
     * Test setContentLength method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetContentLength4() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLengthLong(12L);
        assertEquals("12", response.getHeader("Content-Length"));
        response.close();
    }

    /**
     * Test setContentType.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetContentType() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentType(null);
        assertNull(response.getContentType());
        response.close();
    }

    /**
     * Test setContentType method.
     *
     * @throws Exception when a serious error occurs.
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
        response.close();
    }

    /**
     * Test setContentType method.
     *
     * @throws Exception when a serious error occurs.
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
        response.close();
    }

    /**
     * Test setDateHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetDateHeader() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setDateHeader("header", 1000);
        response.setDateHeader("header", 2000);
        assertEquals("Thu, 1 Jan 1970 00:00:02 GMT", response.getHeader("header"));
        response.close();
    }

    /**
     * Test setDateHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetDateHeader2() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setDateHeader("header", 1000);
        assertEquals("Thu, 1 Jan 1970 00:00:01 GMT", response.getHeader("header"));
        response.close();
    }

    /**
     * Test setHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetHeader() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setHeader("header", "value1");
        response.addHeader("header", "value2");
        response.setHeader("header", "value3");
        assertEquals("value3", response.getHeader("header"));
        response.close();
    }

    /**
     * Test setHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetHeader2() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setHeader("header", "value1");
        response.setHeader("header", "value2");
        assertEquals("value2", response.getHeader("header"));
        response.close();
    }

    /**
     * Test setHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetHeader3() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setHeader("header", "value1");
        assertEquals("value1", response.getHeader("header"));
        response.close();
    }

    /**
     * Test setIntHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetIntHeader() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setIntHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
        response.close();
    }

    /**
     * Test setStatus method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetStatus() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setStatus(500);
        assertEquals(500, response.getStatus());
        response.close();
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
