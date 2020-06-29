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

import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

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
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.addDateHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
    }

    /**
     * Test addIntHeader method.
     */
    @Test
    public void testAddIntHeader() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.addIntHeader("name", 1234);
        assertEquals("1234", response.getHeader("name"));
    }

    /**
     * Test containsHeader method.
     */
    @Test
    public void testContainsHeader() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertFalse(response.containsHeader("name"));
        response.addHeader("name", "value");
        assertTrue(response.containsHeader("name"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    public void testEncodeRedirectUrl() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeRedirectURL("/encodeMe"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    public void testEncodeRedirectUrl2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeRedirectUrl("/encodeMe"));
    }

    /**
     * Test encodeUrl method.
     */
    @Test
    public void testEncodeUrl() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeURL("/encodeMe"));
    }

    /**
     * Test encodeUrl method.
     */
    @Test
    public void testEncodeUrl2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        assertNotNull(response.encodeUrl("/encodeMe"));
    }

    /**
     * Test flushBuffer method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testFlushBuffer() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.flushBuffer();
    }

    /**
     * Test getCharacterEncoding method.
     */
    @Test
    public void testGetCharacterEncoding() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertEquals("ISO-8859-1", response.getCharacterEncoding());
        response.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test getContentType method.
     */
    @Test
    public void testGetContentType() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNull(response.getContentType());
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
    }

    /**
     * Test getContentType method.
     */
    @Test
    public void testGetContentType2() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNull(response.getContentType());
        response.setContentType("text/html;charset=UTF-8");
        assertEquals("text/html", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    public void testGetHeaderNames() {
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
    public void testGetHeaders() {
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
    public void testGetLocale() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
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
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.getWriter();
        response.getOutputStream();
    }

    /**
     * Test getWebApplication method.
     */
    @Test
    public void testGetWebApplication() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        assertNull(response.getWebApplication());
    }

    /**
     * Test getWriter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetWriter() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.getOutputStream();
        response.getWriter();
    }

    /**
     * Test reset method.
     */
    @Test
    public void testReset() {
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
    public void testSendError() throws Exception {
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
    @Test(expected = IllegalStateException.class)
    public void testSendError2() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error");
        response.flushBuffer();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    /**
     * Test setContentLength method.
     */
    @Test
    public void testSetContentLength() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLength(12);
        assertEquals(12, response.getContentLength());
    }

    /**
     * Test setContentLength method.
     */
    @Test
    public void testSetContentLength2() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLengthLong(12L);
        assertEquals(12, response.getContentLength());
    }

    /**
     * Test setContentType.
     */
    @Test
    public void testSetContentType() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentType(null);
        assertNull(response.getContentType());
    }

    @Test
    public void testContentTypeHeader() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
        assertEquals("ISO-8859-1", response.getCharacterEncoding());

        response.setBodyOnly(false);
        response.flushBuffer();

        assertTrue(new String(response.getResponseBytes()).contains("Content-Type: text/html\n"));
        assertFalse(new String(response.getResponseBytes()).contains("charset=iso-8859-1"));
    }

    @Test
    public void testContentTypeHeader2() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentType("text/html;charset=UTF-8");
        assertEquals("text/html", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());

        response.setBodyOnly(false);
        response.flushBuffer();

        assertTrue(new String(response.getResponseBytes()).contains("Content-Type: text/html;charset=utf-8\n"));
    }
}
