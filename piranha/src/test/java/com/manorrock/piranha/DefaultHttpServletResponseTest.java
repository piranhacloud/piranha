/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultWebApplicationResponse class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServletResponseTest {

    /**
     * Test addDateHeader method.
     */
    @Test
    public void testAddDateHeader() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.addDateHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
    }

    /**
     * Test addIntHeader method.
     */
    @Test
    public void testAddIntHeader() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.addIntHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
    }

    /**
     * Test containsHeader method.
     */
    @Test
    public void testContainsHeader() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        assertFalse(response.containsHeader("name"));
        response.addHeader("name", "value");
        assertTrue(response.containsHeader("name"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeRedirectUrl() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        response.encodeRedirectUrl("/encodeMe");
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeRedirectUrl2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        response.encodeRedirectUrl("/encodeMe");
    }

    /**
     * Test encodeUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeUrl() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        response.encodeUrl("/encodeMe");
    }

    /**
     * Test encodeUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeUrl2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        response.encodeUrl("/encodeMe");
    }

    /**
     * Test flushBuffer method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testFlushBuffer() throws Exception {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        outputStream.setResponse(response);
        response.setOutputStream(outputStream);
        response.flushBuffer();
    }

    /**
     * Test getCharacterEncoding method.
     */
    @Test
    public void testGetCharacterEncoding() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test getContentType method.
     */
    @Test
    public void testGetContentType() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        assertNull(response.getContentType());
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    public void testGetHeaderNames() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        assertNotNull(response.getHeaderNames());
        assertTrue(response.getHeaderNames().isEmpty());
        response.addHeader("name", "value");
        assertFalse(response.getHeaderNames().isEmpty());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    public void testGetHeaders() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        assertNotNull(response.getHeaders("name"));
        assertTrue(response.getHeaders("name").isEmpty());
        response.addHeader("name", "value");
        assertFalse(response.getHeaders("name").isEmpty());
    }

    /**
     * Test getLocale method.
     */
    @Test
    public void testGetLocale() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        assertNull(response.getLocale());
        response.setLocale(Locale.ITALIAN);
        assertEquals(Locale.ITALIAN, response.getLocale());
    }

    /**
     * Test getOutputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetOutputStream() throws Exception {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.getWriter();
        response.getOutputStream();
    }

    /**
     * Test getWebApplication method.
     */
    @Test
    public void testGetWebApplication() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        assertNull(response.getWebApplication());
    }

    /**
     * Test getWriter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetWriter() throws Exception {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.getOutputStream();
        response.getWriter();
    }

    /**
     * Test reset method.
     */
    @Test
    public void testReset() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.reset();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testSendError() throws Exception {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        DefaultServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error");
        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals("error", response.getStatusMessage());
        assertTrue(response.isCommitted());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testSendError2() throws Exception {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        DefaultServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error");
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testSendRedirect() throws Exception {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        DefaultServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        response.sendRedirect("/redirect.html");
        assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, response.getStatus());
        assertTrue(response.isCommitted());
    }

    /**
     * Test setContentLength method.
     */
    @Test
    public void testSetContentLength() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setContentLength(12);
        assertEquals(12, response.getContentLength());
    }

    /**
     * Test setContentLength method.
     */
    @Test
    public void testSetContentLength2() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setContentLengthLong(12L);
        assertEquals(12, response.getContentLength());
    }

    /**
     * Test setContentType.
     */
    @Test
    public void testSetContentType() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setContentType(null);
        assertNull(response.getContentType());
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    public void testSetDateHeader() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setDateHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
    }

    /**
     * Test setIntHeader method.
     */
    @Test
    public void testSetIntHeader() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setIntHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
    }

    /**
     * Test setStatus method.
     */
    @Test
    public void testSetStatus() {
        DefaultWebApplicationResponse response = new TestHttpServletResponse();
        response.setStatus(HttpServletResponse.SC_OK, "OK");
    }
}
