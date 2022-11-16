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
package cloud.piranha.core.tests;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.api.WebApplicationOutputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import static java.util.Locale.GERMAN;
import static java.util.Locale.ITALIAN;
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
 * The JUnit tests for any WebApplicationResponse implementation.
 *
 * <p>
 * Note all these tests only use the public APIs of WebApplication,
 * WebApplicationRequest and WebApplicationResponse.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class WebApplicationResponseTest {

    /**
     * Create the web application.
     *
     * @return the web application.
     */
    protected abstract WebApplication createWebApplication();

    /**
     * Create the web application request.
     *
     * @return the web application request.
     */
    protected abstract WebApplicationRequest createWebApplicationRequest();

    /**
     * Create the web application response.
     *
     * @return the web application response.
     */
    protected abstract WebApplicationResponse createWebApplicationResponse();

    /**
     * Test addCookie method.
     */
    @Test
    void testAddCookie() {
        WebApplicationResponse response = createWebApplicationResponse();
        response.addCookie(new Cookie("name", "value"));
        assertFalse(response.getCookies().isEmpty());
    }

    /**
     * Test addDateHeader method.
     */
    @Test
    void testAddDateHeader() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.addDateHeader("name", 1234);
        assertEquals("Thu, 1 Jan 1970 00:00:01 GMT", response.getHeader("name"));
    }

    /**
     * Test addHeader method.
     */
    @Test
    void testAddHeader() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertFalse(response.containsHeader("name"));
        response.addHeader("name", "value");
        assertTrue(response.containsHeader("name"));
        assertEquals("value", response.getHeader("name"));
    }

    /**
     * Test addIntHeader method.
     */
    @Test
    void testAddIntHeader() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.addIntHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
    }

    /**
     * Test closeAsyncResponse method.
     */
    @Test
    void testCloseAsyncResponse() {
        WebApplicationResponse response = createWebApplicationResponse();
        response.setResponseCloser(() -> {
        });
        response.closeAsyncResponse();
    }

    /**
     * Test containsHeader method.
     */
    @Test
    void testContainsHeader() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertFalse(response.containsHeader("name"));
        response.addHeader("name", "value");
        assertTrue(response.containsHeader("name"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    void testEncodeRedirectUrl() {
        WebApplication webApplication = createWebApplication();
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        assertNotNull(response.encodeRedirectURL("/encodeMe"));
    }

    /**
     * Test encodeUrl method.
     */
    @Test
    void testEncodeUrl() {
        WebApplication webApplication = createWebApplication();
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        assertNotNull(response.encodeURL("/encodeMe"));
    }

    /**
     * Test flushBuffer method.
     */
    @Test
    void testFlushBuffer() {
        try {
            WebApplicationResponse response = createWebApplicationResponse();
            response.flushBuffer();
        } catch (IOException ex) {
            fail();
        }
    }

    /**
     * Test getBufferSize method.
     */
    @Test
    void testGetBufferSize() {
        WebApplicationResponse response = createWebApplicationResponse();
        assertTrue(response.getBufferSize() >= 0);
    }

    /**
     * Test getCharacterEncoding method.
     */
    @Test
    void testGetCharacterEncoding() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNull(response.getContentType());
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType2() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNull(response.getContentType());
        response.setContentType("text/html;charset=UTF-8");
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test getCookies method.
     */
    @Test
    void testGetCookies() {
        WebApplicationResponse response = createWebApplicationResponse();
        response.addCookie(new Cookie("name", "value"));
        assertFalse(response.getCookies().isEmpty());
    }

    /**
     * Test getHeader method.
     */
    @Test
    void testGetHeader() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.addHeader("name", "value");
        assertEquals("value", response.getHeader("name"));
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    void testGetHeaderNames() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
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
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
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
        WebApplicationResponse response = createWebApplicationResponse();
        assertEquals(Locale.getDefault(), response.getLocale());
        response.setLocale(ITALIAN);
        assertEquals(ITALIAN, response.getLocale());
    }

    /**
     * Test getOutputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetOutputStream() throws Exception {
        WebApplicationResponse response = createWebApplicationResponse();
        assertNotNull(response.getOutputStream());
    }

    /**
     * Test getOutputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetOutputStream2() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.getWriter();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> response.getOutputStream()));
    }

    /**
     * Test getResponseCloser method.
     */
    @Test
    void testGetResponseCloser() {
        WebApplicationResponse response = createWebApplicationResponse();
        assertNotNull(response.getResponseCloser());
    }

    /**
     * Test getStatus method.
     */
    @Test
    void testGetStatus() {
        WebApplicationResponse response = createWebApplicationResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test getTrailerFields method.
     */
    @Test
    void testGetTrailerFields() {
        WebApplicationResponse response = createWebApplicationResponse();
        assertNull(response.getTrailerFields());
    }

    /**
     * Test getWebApplicationOutputStream method.
     */
    @Test
    void testGetUnderlyingOutputStream() {
        WebApplicationResponse response = createWebApplicationResponse();
        assertNotNull(response.getWebApplicationOutputStream());
    }

    /**
     * Test getWriter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetWriter() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(response.getWriter());
    }

    /**
     * Test getWriter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetWriter2() throws Exception {
        WebApplicationResponse response = createWebApplicationResponse();
        response.getOutputStream();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> response.getWriter()));
    }

    /**
     * Test isCommitted method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsCommitted() throws IOException {
        WebApplicationResponse response = createWebApplicationResponse();
        assertFalse(response.isCommitted());
        response.flushBuffer();
        assertTrue(response.isCommitted());
    }

    /**
     * Test reset method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testReset() throws Exception {
        WebApplicationResponse response = createWebApplicationResponse();
        response.reset();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test reset method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testReset2() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
        response.reset();
        assertNotEquals("UTF-8", response.getCharacterEncoding());
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
    }

    /**
     * Test resetBuffer method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testResetBuffer() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setCharacterEncoding("UTF-8");
        response.resetBuffer();
        assertEquals("UTF-8", response.getCharacterEncoding());
        response.flushBuffer();
        try {
            response.resetBuffer();
            fail();
        } catch (IllegalStateException ise) {
        }
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSendError() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.sendError(SC_INTERNAL_SERVER_ERROR, "error");
        response.flushBuffer();
        assertEquals(SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertTrue(response.isCommitted());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSendError2() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.sendError(SC_INTERNAL_SERVER_ERROR, "error");
        response.flushBuffer();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> response.sendError(SC_INTERNAL_SERVER_ERROR)));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSendRedirect() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("Servlet", TestSendRedirectServlet.class);
        webApplication.addServletMapping("Servlet", "/servlet");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/servlet");
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> webApplication.service(request, response)));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSendRedirect2() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("Servlet2a", TestSendRedirect2aServlet.class);
        webApplication.addServlet("Servlet2b", TestSendRedirect2bServlet.class);
        webApplication.addServletMapping("Servlet2a", "/servlet2a");
        webApplication.addServletMapping("Servlet2b", "/servlet2a/servlet2b");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/servlet2a");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testRedirect3() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("Servlet3", TestSendRedirect3Servlet.class);
        webApplication.addServletMapping("Servlet3", "/servlet3");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/servlet3");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSendRedirect4() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("Servlet4", TestSendRedirect4Servlet.class);
        webApplication.addServletMapping("Servlet4", "/servlet4");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/servlet4");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
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
    void testSendRedirect5() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("Servlet5a", TestSendRedirect5aServlet.class);
        webApplication.addServlet("Servlet5b", TestSendRedirect5bServlet.class);
        webApplication.setContextPath("/app");
        webApplication.addServletMapping("Servlet5a", "/servlet5a");
        webApplication.addServletMapping("Servlet5b", "/servlet5a/servlet5b");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setContextPath("/app");
        request.setServletPath("/servlet5a");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
    }

    /**
     * Test setBufferSize method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetBufferSize() throws Exception {
        WebApplicationResponse response = createWebApplicationResponse();
        response.setBufferSize(1024);
        assertEquals(1024, response.getBufferSize());
        response.flushBuffer();
        assertThrows(IllegalStateException.class, () -> response.setBufferSize(512));
    }

    /**
     * Test setCharacterEncoding method.
     */
    @Test
    void testSetCharacterEncoding() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        String defaultEncoding = response.getCharacterEncoding();
        response.setCharacterEncoding("UTF-8");
        assertTrue("UTF-8".equalsIgnoreCase(response.getCharacterEncoding()));
        response.setCharacterEncoding(null);
        assertTrue((defaultEncoding == null && response.getCharacterEncoding() == null)
                || (defaultEncoding != null && defaultEncoding.equalsIgnoreCase(response.getCharacterEncoding())));
    }

    /**
     * Test setCharacterEncoding method.
     */
    @Test
    void testSetCharacterEncoding2() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        String defaultEncoding = response.getCharacterEncoding();
        response.setCharacterEncoding("UTF-8");
        assertTrue("UTF-8".equalsIgnoreCase(response.getCharacterEncoding()));
        response.setCharacterEncoding(null);
        assertTrue((defaultEncoding == null && response.getCharacterEncoding() == null)
                || (defaultEncoding != null && defaultEncoding.equalsIgnoreCase(response.getCharacterEncoding())));
    }

    /**
     * Test setCharacterEncoding method.
     */
    @Test
    void testSetCharacterEncoding3() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setCharacterEncoding("does-not-exist");
        assertTrue("does-not-exist".equalsIgnoreCase(response.getCharacterEncoding()));
        assertThrows(UnsupportedEncodingException.class, () -> {
            response.getWriter();
        });
    }

    /**
     * Test setCharacterEncoding method.
     */
    @Test
    void testSetCharacterEncoding4() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setContentType("text/html");
        response.setCharacterEncoding("ISO-8859-7");
        String contentType = response.getContentType();
        assertTrue(contentType.toLowerCase().contains("text/html"));
        assertTrue(contentType.toLowerCase().contains("charset"));
        assertTrue(contentType.toLowerCase().contains("iso-8859-7"));
    }

    /**
     * Test setContentLength method.
     */
    @Test
    void testSetContentLength() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setContentLength(12);
        assertEquals("12", response.getHeader("Content-Length"));
    }

    /**
     * Test setContentLength method.
     */
    @Test
    void testSetContentLength2() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setContentLengthLong(12L);
        assertEquals("12", response.getHeader("Content-Length"));
    }

    /**
     * Test setContentType method.
     */
    @Test
    void testSetContentType() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setContentType(null);
        assertNull(response.getContentType());
    }

    /**
     * Test setContentType method.
     */
    @Test
    void testSetContentType2() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
    }

    /**
     * Test setContentType method.
     */
    @Test
    void testSetContentType3() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setContentType("text/html;charset=UTF-8");
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    void testSetDateHeader() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setDateHeader("header", 1000);
        assertEquals("Thu, 1 Jan 1970 00:00:01 GMT", response.getHeader("header"));
        response.setDateHeader("header", 2000);
        assertEquals("Thu, 1 Jan 1970 00:00:02 GMT", response.getHeader("header"));
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    void testSetDateHeader2() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setDateHeader("header", 1000);
        assertEquals("Thu, 1 Jan 1970 00:00:01 GMT", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    void testSetHeader() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setHeader("header", "value1");
        response.addHeader("header", "value2");
        response.setHeader("header", "value3");
        assertEquals("value3", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    void testSetHeader2() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setHeader("header", "value1");
        response.setHeader("header", "value2");
        assertEquals("value2", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    void testSetHeader3() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setHeader("header", "value1");
        assertEquals("value1", response.getHeader("header"));
    }

    /**
     * Test setIntHeader method.
     */
    @Test
    void testSetIntHeader() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setIntHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
    }

    /**
     * Test setLocale method.
     */
    @Test
    void testSetLocale() {
        WebApplicationResponse response = createWebApplicationResponse();
        response.setLocale(GERMAN);
        assertEquals(GERMAN, response.getLocale());
    }

    /**
     * Test setResponseCloser method.
     */
    @Test
    void testSetResponseCloser() {
        WebApplicationResponse response = createWebApplicationResponse();
        response.setResponseCloser(() -> {
        });
        assertNotNull(response.getResponseCloser());
    }

    /**
     * Test setStatus method.
     */
    @Test
    void testSetStatus() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setStatus(500);
        assertEquals(500, response.getStatus());
    }

    /**
     * Test setTrailerFields method.
     */
    @Test
    void testSetTrailerFields() {
        WebApplication webApplication = createWebApplication();
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        assertThrows(IllegalStateException.class, () -> {
            response.setTrailerFields(() -> { 
                return null;
            });
        });
    }
    
    /**
     * Test setWebApplicationOutputStream method.
     */
    @Test
    void testSetWebAplicationOutputStream() {
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplicationOutputStream(new WebApplicationOutputStream(){
            @Override
            public void flushBuffer() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getBufferSize() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public OutputStream getOutputStream() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public WebApplicationResponse getResponse() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void resetBuffer() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setBufferSize(int bufferSize) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setOutputStream(OutputStream outputStream) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setResponse(WebApplicationResponse response) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isReady() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void write(int b) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        assertNotNull(response.getWebApplicationOutputStream());
    }
    
    /**
     * Test setWebApplication method.
     */
    @Test
    void testGetWebApplication()  {
        WebApplication webApplication = createWebApplication();
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
    }
    
    /**
     * Test servlet that is used by testSendRedirect.
     */
    public static class TestSendRedirectServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestSendRedirectServlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.flushBuffer();
            response.sendRedirect("/this_should_throw_an_illegal_state_exception");
        }
    }

    /**
     * Test servlet that is used by testSendRedirect2.
     */
    public static class TestSendRedirect2aServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestSendRedirect2aServlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("servlet2b");
        }
    }

    /**
     * Test servlet that is used by testSendRedirect2.
     */
    public static class TestSendRedirect2bServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestSendRedirect2bServlet() {
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
     * Test servlet that is used by testSendRedirect3.
     */
    public static class TestSendRedirect3Servlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestSendRedirect3Servlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("/relative_to_root");
        }
    }

    /**
     * Test servlet that is used by testSendRedirect4.
     */
    public static class TestSendRedirect4Servlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestSendRedirect4Servlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("http://this.is.outside/and_absolute");
        }
    }

    /**
     * Test servlet that is used by testSendRedirect5.
     */
    public static class TestSendRedirect5aServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestSendRedirect5aServlet() {
        }

        @Override
        protected void doGet(HttpServletRequest request,
                HttpServletResponse response) throws IOException, ServletException {
            response.sendRedirect("servlet5b");
        }
    }

    /**
     * Test servlet that is used by testSendRedirect5.
     */
    public static class TestSendRedirect5bServlet extends HttpServlet {

        /**
         * Constructor.
         */
        public TestSendRedirect5bServlet() {
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
}
