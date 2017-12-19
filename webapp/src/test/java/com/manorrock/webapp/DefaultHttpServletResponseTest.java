/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultHttpServletResponse class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServletResponseTest {

    /**
     * Test addDateHeader method.
     */
    @Test
    public void testAddDateHeader() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.addDateHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
    }

    /**
     * Test addIntHeader method.
     */
    @Test
    public void testAddIntHeader() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.addIntHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
    }

    /**
     * Test containsHeader method.
     */
    @Test
    public void testContainsHeader() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        response.encodeRedirectUrl("/encodeMe");
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeRedirectUrl2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        response.encodeRedirectUrl("/encodeMe");
    }

    /**
     * Test encodeUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeUrl() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        response.encodeUrl("/encodeMe");
    }

    /**
     * Test encodeUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeUrl2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test getContentType method.
     */
    @Test
    public void testGetContentType() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        assertNull(response.getContentType());
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    public void testGetHeaderNames() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.getWriter();
        response.getOutputStream();
    }

    /**
     * Test getWebApplication method.
     */
    @Test
    public void testGetWebApplication() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        assertNull(response.getWebApplication());
    }

    /**
     * Test getWriter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetWriter() throws Exception {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.getOutputStream();
        response.getWriter();
    }

    /**
     * Test reset method.
     */
    @Test
    public void testReset() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.setContentLength(12);
        assertEquals(12, response.getContentLength());
    }

    /**
     * Test setContentLength method.
     */
    @Test
    public void testSetContentLength2() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.setContentLengthLong(12L);
        assertEquals(12, response.getContentLength());
    }

    /**
     * Test setContentType.
     */
    @Test
    public void testSetContentType() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.setContentType(null);
        assertNull(response.getContentType());
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    public void testSetDateHeader() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.setDateHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
    }

    /**
     * Test setIntHeader method.
     */
    @Test
    public void testSetIntHeader() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.setIntHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
    }

    /**
     * Test setStatus method.
     */
    @Test
    public void testSetStatus() {
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        response.setStatus(HttpServletResponse.SC_OK, "OK");
    }
}
