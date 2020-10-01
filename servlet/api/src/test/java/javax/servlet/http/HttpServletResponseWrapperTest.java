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
package javax.servlet.http;

import javax.servlet.TestServletOutputStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The JUnit tests for the HttpServletResponseWrapper class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpServletResponseWrapperTest {

    /**
     * Test addCookie method.
     */
    @Test
    void testAddCookie() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addCookie(new Cookie("name", "value"));
        assertNotNull(wrapped.getCookies());
        assertFalse(wrapped.getCookies().isEmpty());
    }

    /**
     * Test addDateHeader method.
     */
    @Test
    void testAddDateHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addDateHeader("date", 0);
        assertEquals("0", wrapped.getHeader("date"));
    }

    /**
     * Test addHeader method.
     */
    @Test
    void testAddHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addHeader("header", "value");
        assertEquals("value", wrapped.getHeader("header"));
    }

    /**
     * Test addIntHeader method.
     */
    @Test
    void testAddIntHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addIntHeader("int", 1);
        assertEquals("1", wrapper.getHeader("int"));
    }

    /**
     * Test containsHeader method.
     */
    @Test
    void testContainsHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addIntHeader("int", 1);
        assertTrue(wrapper.containsHeader("int"));
    }

    /**
     * Test encodeRedirectURL method.
     */
    @Test
    void testEncodeRedirectURL() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        assertEquals("url", wrapper.encodeRedirectURL("url"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    void testEncodeRedirectUrl() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        assertThrows(UnsupportedOperationException.class, () -> wrapper.encodeRedirectUrl("url"));
    }

    /**
     * Test encodeURL method.
     */
    @Test
    void testEncodeURL() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        assertEquals("url", wrapper.encodeURL("url"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    void testEncodeUrl() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        assertThrows(UnsupportedOperationException.class, () -> wrapper.encodeUrl("url"));
    }

    /**
     * Test getHeaders method.
     */
    @Test
    void testGetHeaders() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addIntHeader("int", 1);
        assertNotNull(wrapper.getHeaders("int"));
        assertEquals(1, wrapper.getHeaders("int").size());
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    void testGetHeaderNames() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addIntHeader("int", 1);
        assertNotNull(wrapper.getHeaderNames());
        assertEquals(1, wrapper.getHeaderNames().size());
    }

    /**
     * Test getStatus method.
     */
    @Test
    void testGetStatus() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        assertEquals(200, wrapper.getStatus());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testSendError() throws Exception {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        TestServletOutputStream outputStream = new TestServletOutputStream();
        wrapped.setOutputStream(outputStream);
        outputStream.setResponse(wrapped);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.sendError(400);
        assertEquals(400, wrapper.getStatus());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testSendError2() throws Exception {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        TestServletOutputStream outputStream = new TestServletOutputStream();
        wrapped.setOutputStream(outputStream);
        outputStream.setResponse(wrapped);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.sendError(404, "Not found");
        assertEquals(404, wrapper.getStatus());
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testSendRedirect() throws Exception {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        TestServletOutputStream outputStream = new TestServletOutputStream();
        wrapped.setOutputStream(outputStream);
        outputStream.setResponse(wrapped);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.sendRedirect("redirect_urL");
        assertEquals(302, wrapper.getStatus());
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    void testSetDateHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.setDateHeader("date", 0);
        assertEquals("0", wrapped.getHeader("date"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    void testSetHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.setHeader("header", "value");
        assertEquals("value", wrapped.getHeader("header"));
    }

    /**
     * Test setIntHeader method.
     */
    @Test
    void testSetIntHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.setIntHeader("date", 0);
        assertEquals("0", wrapped.getHeader("date"));
    }

    /**
     * Test setStatus method.
     */
    @Test
    void testSetStatus() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.setStatus(301);
        assertEquals(301, wrapped.getStatus());
    }

    /**
     * Test setStatus method.
     */
    @Test
    void testSetStatus2() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        assertThrows(UnsupportedOperationException.class, () -> wrapper.setStatus(301, "Moved"));
    }

    /**
     * Test set response method
     */
    @Test
    void testSetResponse() {
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(null);
        assertThrows(IllegalArgumentException.class, () -> wrapper.setResponse(null));
    }
}
