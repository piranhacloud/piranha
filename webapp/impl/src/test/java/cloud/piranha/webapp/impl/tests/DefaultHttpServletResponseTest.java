/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.webapp.impl.tests;

import cloud.piranha.webapp.api.LocaleEncodingManager;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplicationResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultWebApplicationResponse class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultHttpServletResponseTest {

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
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testFlushBuffer() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.flushBuffer();
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
     * Test getCharacterEncoding method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetCharacterEncoding2() throws Exception {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        response.setWebApplication(webApp);
        LocaleEncodingManager localeEncodingManager = webApp.getLocaleEncodingManager();
        localeEncodingManager.addCharacterEncoding(Locale.JAPAN.toString(), "euc-jp");
        localeEncodingManager.addCharacterEncoding(Locale.CHINA.toString(), "gb18030");

        response.setContentType("text/html");
        assertEquals("iso-8859-1", response.getCharacterEncoding().toLowerCase());

        /*
         * setLocale should change character encoding based on
         * locale-encoding-mapping-list
         */
        response.setLocale(Locale.JAPAN);
        assertEquals("euc-jp", response.getCharacterEncoding().toLowerCase());

        /*
         * setLocale should change character encoding based on
         * locale-encoding-mapping-list
         */
        response.setLocale(Locale.CHINA);
        assertEquals("gb18030", response.getCharacterEncoding().toLowerCase());

        /*
         * setContentType here doesn't define character encoding (so character
         * encoding should stay as it is)
         */
        response.setContentType("text/html");
        assertEquals("gb18030", response.getCharacterEncoding().toLowerCase());

        /*
         * setCharacterEncoding should still be able to change encoding
         */
        response.setCharacterEncoding("utf-8");
        assertEquals("utf-8", response.getCharacterEncoding().toLowerCase());

        /*
         * setLocale should not override explicit character encoding request
         */
        response.setLocale(Locale.JAPAN);
        assertEquals("utf-8", response.getCharacterEncoding().toLowerCase());

        /*
         * setContentType should still be able to change encoding
         */
        response.setContentType("text/html;charset=gb18030");
        assertEquals("gb18030", response.getCharacterEncoding().toLowerCase());

        /*
         * setCharacterEncoding should still be able to change encoding
         */
        response.setCharacterEncoding("utf-8");
        assertEquals("utf-8", response.getCharacterEncoding());

        /*
         * getWriter should freeze the character encoding
         */
        response.getWriter();
        assertEquals("utf-8", response.getCharacterEncoding());

        /*
         * setCharacterEncoding should no longer be able to change the encoding
         */
        response.setCharacterEncoding("iso-8859-1");
        assertEquals("utf-8", response.getCharacterEncoding());

        /*
         * setLocale should not override explicit character encoding request
         */
        response.setLocale(Locale.JAPAN);
        assertEquals("utf-8", response.getCharacterEncoding());
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
        assertEquals("text/html", response.getContentType());
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
        assertThrows(IllegalStateException.class, () -> response.getOutputStream());
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
        assertThrows(IllegalStateException.class, () -> response.getWriter());
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
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error");
        response.flushBuffer();
        assertThrows(IllegalStateException.class, () -> response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
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

    @Test
    void testSetContentLength3() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentLength(12);
        assertEquals("12", response.getHeader("Content-Length"));
    }

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

    @Test
    void testContentTypeHeader() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentType("text/html");
        assertEquals("text/html", response.getContentType());
        assertEquals("ISO-8859-1", response.getCharacterEncoding());

        response.setBodyOnly(false);
        response.flushBuffer();

        assertTrue(new String(response.getResponseBytes()).contains("Content-Type: text/html;charset=ISO-8859-1"));
    }

    @Test
    void testContentTypeHeader2() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setContentType("text/html;charset=UTF-8");
        assertEquals("text/html", response.getContentType());
        assertEquals("UTF-8", response.getCharacterEncoding());

        response.setBodyOnly(false);
        response.flushBuffer();

        assertTrue(new String(response.getResponseBytes()).contains("Content-Type: text/html;charset=UTF-8\n"));
    }
}
