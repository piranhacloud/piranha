/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
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
package javax.servlet;

import java.util.Locale;
import javax.servlet.http.TestHttpServletRequest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * The JUnit tests for the ServletRequestWrapper class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletRequestWrapperTest {

    /**
     * Test getAsyncContext method.
     */
    @Test
    public void testGetAsyncContext() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertNull(wrapper.getAsyncContext());
    }

    /**
     * Test geAttribute method.
     */
    @Test
    public void testGetAttribute() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertNull(wrapper.getAttribute("null"));
    }
    
    /**
     * Test getAttributeNames method.
     */
    @Test
    public void testGetAttributeNames() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertNotNull(wrapper.getAttributeNames());
    }
    
    /**
     * Test getCharacterEncoding method.
     */
    @Test
    public void testCharacterEncoding() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertEquals("UTF-8", wrapper.getCharacterEncoding());
    }
    
    /**
     * Test getContentLength method.
     */
    @Test
    public void testGetContentLength() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertEquals(-1, wrapper.getContentLength());
    }
    
    /**
     * Test getContentLengthLong method.
     */
    @Test
    public void testGetContentLengthLong() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertEquals(-1L, wrapper.getContentLengthLong());
    }
    
    /**
     * Test getContentType method.
     */
    @Test
    public void testGetContentType() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertEquals("text/html", wrapper.getContentType());
    }
    
    /**
     * Test getDispatcherType method.
     */
    @Test
    public void testGetDispatcherType() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertEquals(DispatcherType.ERROR, wrapper.getDispatcherType());
    }
    
    /**
     * Test getInputStream method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetInputStream() throws Exception {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertNotNull(wrapper.getInputStream());
    }
    
    /**
     * Test getLocalAddr method.
     */
    @Test
    public void testGetLocalAddr() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertEquals("127.0.0.1", wrapper.getLocalAddr());
    }
    
    /**
     * Test getLocalName method.
     */
    @Test
    public void testGetLocalName() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertEquals("localhost", wrapper.getLocalName());
    }
    
    /**
     * Test getLocalPort method.
     */
    @Test
    public void testGetLocalPort() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertEquals(8180, wrapper.getLocalPort());
    }
    
    /**
     * Test getLocale method.
     */
    @Test
    public void testGetLocale() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertEquals(Locale.getDefault(), wrapper.getLocale());
    }
    
    /**
     * Test getLocales method.
     */
    @Test
    public void testGetLocales() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertNotNull(wrapper.getLocales());
    }
    
    /**
     * Test getParameter method.
     */
    @Test
    public void testGetParameter() {
        ServletRequestWrapper wrapper = new ServletRequestWrapper(new TestServletRequest(null));
        assertNull(wrapper.getParameter("null"));
    }
}
