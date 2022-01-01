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
package jakarta.servlet;

import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the ServletRequestWrapper class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ServletRequestWrapperTest {

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertNull(wrapper.getAsyncContext());
    }

    /**
     * Test geAttribute method.
     */
    @Test
    void testGetAttribute() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertNull(wrapper.getAttribute("null"));
    }

    /**
     * Test getAttributeNames method.
     */
    @Test
    void testGetAttributeNames() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertNotNull(wrapper.getAttributeNames());
    }

    /**
     * Test getCharacterEncoding method.
     */
    @Test
    void testCharacterEncoding() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertEquals("UTF-8", wrapper.getCharacterEncoding());
    }

    /**
     * Test getContentLength method.
     */
    @Test
    void testGetContentLength() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertEquals(-1, wrapper.getContentLength());
    }

    /**
     * Test getContentLengthLong method.
     */
    @Test
    void testGetContentLengthLong() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertEquals(-1L, wrapper.getContentLengthLong());
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertEquals("text/html", wrapper.getContentType());
    }

    /**
     * Test getDispatcherType method.
     */
    @Test
    void testGetDispatcherType() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertEquals(DispatcherType.ERROR, wrapper.getDispatcherType());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream() throws Exception {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertNotNull(wrapper.getInputStream());
    }

    /**
     * Test getLocalAddr method.
     */
    @Test
    void testGetLocalAddr() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertEquals("127.0.0.1", wrapper.getLocalAddr());
    }

    /**
     * Test getLocalName method.
     */
    @Test
    void testGetLocalName() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertEquals("localhost", wrapper.getLocalName());
    }

    /**
     * Test getLocalPort method.
     */
    @Test
    void testGetLocalPort() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertEquals(8180, wrapper.getLocalPort());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocale() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertEquals(Locale.getDefault(), wrapper.getLocale());
    }

    /**
     * Test getLocales method.
     */
    @Test
    void testGetLocales() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertNotNull(wrapper.getLocales());
    }

    /**
     * Test getParameter method.
     */
    @Test
    void testGetParameter() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertNull(wrapper.getParameter("null"));
    }

    /**
     * Test set request method
     */
    @Test
    void testSetRequest() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest());
        assertThrows(IllegalArgumentException.class, () -> wrapper.setRequest(null));
    }
}
