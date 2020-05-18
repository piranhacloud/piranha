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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the HttpServletResponseWrapper class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpServletResponseWrapperTest {

    /**
     * Test addCookie method.
     */
    @Test
    public void testAddCookie() {
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
    public void testAddDateHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addDateHeader("date", 0);
        assertEquals("0", wrapped.getHeader("date"));
    }

    /**
     * Test addHeader method.
     */
    @Test
    public void testAddHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addHeader("header", "value");
        assertEquals("value", wrapped.getHeader("header"));
    }

    /**
     * Test addIntHeader method.
     */
    @Test
    public void testAddIntHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addIntHeader("int", 1);
        assertEquals("1", wrapper.getHeader("int"));
    }

    /**
     * Test containsHeader method.
     */
    @Test
    public void testContainsHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.addIntHeader("int", 1);
        assertTrue(wrapper.containsHeader("int"));
    }

    /**
     * Test encodeRedirectURL method.
     */
    @Test
    public void testEncodeRedirectURL() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        assertEquals("url", wrapper.encodeRedirectURL("url"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeRedirectUrl() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.encodeRedirectUrl("url");
    }

    /**
     * Test encodeURL method.
     */
    @Test
    public void testEncodeURL() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        assertEquals("url", wrapper.encodeURL("url"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeUrl() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.encodeUrl("url");
    }

    /**
     * Test getHeaders method.
     */
    @Test
    public void testGetHeaders() {
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
    public void testGetHeaderNames() {
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
    public void testGetStatus() {
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
    public void testSendError() throws Exception {
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
    public void testSendError2() throws Exception {
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
    public void testSendRedirect() throws Exception {
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
    public void testSetDateHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.setDateHeader("date", 0);
        assertEquals("0", wrapped.getHeader("date"));
    }

    /**
     * Test setHeader method.
     */
    @Test
    public void testSetHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.setHeader("header", "value");
        assertEquals("value", wrapped.getHeader("header"));
    }

    /**
     * Test setIntHeader method.
     */
    @Test
    public void testSetIntHeader() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.setIntHeader("date", 0);
        assertEquals("0", wrapped.getHeader("date"));
    }

    /**
     * Test setStatus method.
     */
    @Test
    public void testSetStatus() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.setStatus(301);
        assertEquals(301, wrapped.getStatus());
    }

    /**
     * Test setStatus method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetStatus2() {
        TestHttpServletResponse wrapped = new TestHttpServletResponse(null);
        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(wrapped);
        wrapper.setStatus(301, "Moved");
    }
}
