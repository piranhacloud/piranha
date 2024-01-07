/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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

import cloud.piranha.core.api.WebApplicationOutputStream;
import cloud.piranha.core.api.WebApplicationResponse;
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
import java.util.Collection;
import java.util.Locale;
import static java.util.Locale.GERMAN;
import static java.util.Locale.ITALIAN;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The JUnit tests for the DefaultWebApplicationResponse class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationResponseTest {

    /**
     * Test addCookie method.
     */
    @Test
    void testAddCookie() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.addCookie(new Cookie("name", "value"));
        assertFalse(response.getCookies().isEmpty());
    }

    /**
     * Test addDateHeader method.
     */
    @Test
    void testAddDateHeader() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.addIntHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
    }

    /**
     * Test addIntHeader method.
     */
    @Test
    void testAddIntHeader2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.addIntHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
        response.addIntHeader("name", 2345);
        Collection<String> values = response.getHeaders("name");
        assertFalse(values.isEmpty());
        assertTrue(values.contains("1234"));
        assertTrue(values.contains("2345"));
    }

    /**
     * Test closeAsyncResponse method.
     */
    @Test
    void testCloseAsyncResponse() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setResponseCloser(() -> {
        });
        response.closeAsyncResponse();
    }

    /**
     * Test containsHeader method.
     */
    @Test
    void testContainsHeader() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        assertNotNull(response.encodeRedirectURL("/encodeMe"));
    }

    /**
     * Test encodeUrl method.
     */
    @Test
    void testEncodeUrl() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        assertNotNull(response.encodeURL("/encodeMe"));
    }

    /**
     * Test flushBuffer method.
     */
    @Test
    void testFlushBuffer() {
        try {
            DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
            response.flushBuffer();
        } catch (IOException ex) {
            fail();
        }
    }
    
    /**
     * Test flushBuffer method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testFlushBuffer2() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setContentLength(20);
        PrintWriter writer = response.getWriter();
        writer.print("0123456789");
        writer.print("0123456789");
        writer.print("0123456789");
        writer.print("0123456789");
        writer.println("And we flushed!");
        response.addIntHeader("header1", 12345);
        assertNull(response.getHeader("header1"));
    }

    /**
     * Test getBufferSize method.
     */
    @Test
    void testGetBufferSize() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertTrue(response.getBufferSize() >= 0);
    }

    /**
     * Test getCharacterEncoding method.
     */
    @Test
    void testGetCharacterEncoding() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test getContentLanguage method.
     */
    @Test
    void testGetContentLanguage() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertNull(response.getContentLanguage());
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.addCookie(new Cookie("name", "value"));
        assertFalse(response.getCookies().isEmpty());
    }

    /**
     * Test getHeader method.
     */
    @Test
    void testGetHeader() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertNotNull(response.getOutputStream());
    }

    /**
     * Test getOutputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetOutputStream2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.getWriter();
        assertNotNull(assertThrows(IllegalStateException.class,
                response::getOutputStream));
    }

    /**
     * Test getResponseCloser method.
     */
    @Test
    void testGetResponseCloser() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertNotNull(response.getResponseCloser());
    }

    /**
     * Test getStatus method.
     */
    @Test
    void testGetStatus() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test getStatusMessage method.
     */
    @Test
    void testGetStatusMessage() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertNull(response.getStatusMessage());
    }

    /**
     * Test getTrailerFields method.
     */
    @Test
    void testGetTrailerFields() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertNull(response.getTrailerFields());
    }

    /**
     * Test getWebApplication method.
     */
    @Test
    void testGetWebApplication() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertNull(response.getWebApplication());
    }

    /**
     * Test getWebApplicationOutputStream method.
     */
    @Test
    void testGetWebApplicationOutputStream() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertNotNull(response.getWebApplicationOutputStream());
    }

    /**
     * Test getWriter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetWriter() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.getOutputStream();
        assertNotNull(assertThrows(IllegalStateException.class,
                response::getWriter));
    }

    /**
     * Test isBodyOnly method.
     */
    @Test
    void testIsBodyOnly() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertFalse(response.isBodyOnly());
    }

    /**
     * Test isBufferResetting method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsBufferResetting() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertFalse(response.isBufferResetting());
    }

    /**
     * Test isCommitted method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsCommitted() throws IOException {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertFalse(response.isCommitted());
        response.flushBuffer();
        assertTrue(response.isCommitted());
    }

    /**
     * Test reset method.
     */
    @Test
    void testReset() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.reset();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test reset method.
     *
     */
    @Test
    void testReset2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
     */
    @Test
    void testSendRedirect() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet", new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.flushBuffer();
                response.sendRedirect("/this_should_throw_an_illegal_state_exception");
            }

        });
        webApplication.addServletMapping("Servlet", "/servlet");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/servlet");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet2a", new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.sendRedirect("servlet2b");
            }
        });
        webApplication.addServlet("Servlet2b", new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.print("SUCCESS");
                writer.flush();
            }
        });
        webApplication.addServletMapping("Servlet2a", "/servlet2a");
        webApplication.addServletMapping("Servlet2b", "/servlet2a/servlet2b");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/servlet2a");
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet3", new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.sendRedirect("/relative_to_root");
            }
        });
        webApplication.addServletMapping("Servlet3", "/servlet3");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/servlet3");
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet4", new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.sendRedirect("http://this.is.outside/and_absolute");
            }
        });
        webApplication.addServletMapping("Servlet4", "/servlet4");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/servlet4");
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Servlet5a", new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.sendRedirect("servlet5b");
            }
        });
        webApplication.addServlet("Servlet5b", new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.print("SUCCESS");
                writer.flush();
            }
        }
        );
        webApplication.setContextPath("/app");
        webApplication.addServletMapping("Servlet5a", "/servlet5a");
        webApplication.addServletMapping("Servlet5b", "/servlet5a/servlet5b");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setContextPath("/app");
        request.setServletPath("/servlet5a");
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(302, response.getStatus());
        assertNotNull(response.getHeader("Location"));
    }

    /**
     * Test setBodyOnly method.
     */
    @Test
    void testSetBodyOnly() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertFalse(response.isBodyOnly());
        response.setBodyOnly(true);
        assertTrue(response.isBodyOnly());
    }

    /**
     * Test setBufferSize method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetBufferSize() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setCharacterEncoding("does-not-exist");
        assertTrue("does-not-exist".equalsIgnoreCase(response.getCharacterEncoding()));
        assertThrows(UnsupportedEncodingException.class, response::getWriter);
    }

    /**
     * Test setCharacterEncoding method.
     */
    @Test
    void testSetCharacterEncoding4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
     * Test setCharacterEncoding method.
     */
    @Test
    void testSetCharacterEncoding5() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        String defaultCharacterEncoding = response.getCharacterEncoding();
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
        response.setCharacterEncoding(null);
        assertEquals(defaultCharacterEncoding, response.getCharacterEncoding());
        response.reset();
        response.setContentType("text/plain; charset=UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
        response.setCharacterEncoding(null);
        assertEquals(defaultCharacterEncoding, response.getCharacterEncoding());
        response.reset();
        response.setCharacterEncoding("does-not-exist");
        assertEquals("does-not-exist", response.getCharacterEncoding());
        assertThrows(UnsupportedEncodingException.class, response::getWriter);
        response.reset();
        response.setContentType("text/html");
        response.setCharacterEncoding("ISO-8859-7");
        assertEquals("text/html;charset=ISO-8859-7", response.getContentType());
    }
    
    /**
     * Test setCharacterEncoding method.
     */
    @Test
    void testSetCharacterEncoding6() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setLocale(new Locale("ja"));
        assertEquals("shift_jis", response.getCharacterEncoding().toLowerCase());
    }

    /**
     * Test setCommitted method.
     */
    @Test
    void testSetCommitted() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertFalse(response.isCommitted());
        response.setCommitted(true);
        assertTrue(response.isCommitted());
    }

    /**
     * Test setContentLength method.
     */
    @Test
    void testSetContentLength() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setContentLength(12);
        assertEquals("12", response.getHeader("Content-Length"));
    }

    /**
     * Test setContentLengthLong method.
     */
    @Test
    void testSetContentLengthLong() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setDateHeader("header", 1000);
        assertEquals("Thu, 1 Jan 1970 00:00:01 GMT", response.getHeader("header"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    void testSetHeader() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
    void testSetHeader2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
    void testSetHeader3() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.setHeader("header", "value1");
        assertEquals("value1", response.getHeader("header"));
    }

    /**
     * Test setIntHeader method.
     */
    @Test
    void testSetIntHeader() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setLocale(GERMAN);
        assertEquals(GERMAN, response.getLocale());
    }

    /**
     * Test setResponseCloser method.
     */
    @Test
    void testSetResponseCloser() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setResponseCloser(() -> {
        });
        assertNotNull(response.getResponseCloser());
    }

    /**
     * Test setStatus method.
     */
    @Test
    void testSetStatus() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        assertThrows(IllegalStateException.class, () -> {
            response.setTrailerFields(() -> {
                return null;
            });
        });
    }

    /**
     * Test setWebApplication method.
     */
    @Test
    void testSetWebApplication() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        assertEquals(webApplication, response.getWebApplication());
    }

    /**
     * Test setWebApplicationOutputStream method.
     */
    @Test
    void testSetWebAplicationOutputStream() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplicationOutputStream(new WebApplicationOutputStream() {
            @Override
            public void flushBuffer() {
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
            public WriteListener getWriteListener() {
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
            public void write(int b) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        assertNotNull(response.getWebApplicationOutputStream());
    }

    /**
     * Test verifyNotCommitted method.
     */
    @Test
    void testVerifyNotCommitted() {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setCommitted(true);
        assertThrows(IllegalStateException.class, () -> {
            response.verifyNotCommitted("bogus");
        });
    }
}
