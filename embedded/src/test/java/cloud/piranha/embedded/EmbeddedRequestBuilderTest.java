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
package cloud.piranha.embedded;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.impl.DefaultWebApplication;
import jakarta.servlet.http.Cookie;
import java.net.HttpCookie;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for the EmbeddedRequestBuilder class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EmbeddedRequestBuilderTest {
    
    /**
     * Test attribute method.
     */
    @Test
    public void testAttribute() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .attribute("name", "value")
                .build();
        assertEquals("value", request.getAttribute("name"));
    }
    
    /**
     * Test contextPath method.
     */
    @Test
    public void testContextPath() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .contextPath("/mycontext")
                .build();
        assertEquals("/mycontext", request.getContextPath());
    }
    
    /**
     * Test contextPath method.
     */
    @Test
    public void testContextPath2() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .contextPath(null)
                .build();
        assertEquals("", request.getContextPath());
    }
    
    /**
     * Test cookie method.
     */
    @Test
    public void testCookie() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .cookie(new Cookie("name", "value"))
                .cookie(new Cookie("name2", "value2"))
                .build();
        assertEquals("name", request.getCookies()[0].getName());
        assertEquals("value", request.getCookies()[0].getValue());
        assertEquals("name2", request.getCookies()[1].getName());
        assertEquals("value2", request.getCookies()[1].getValue());
    }
    
    /**
     * Test header method.
     */
    @Test
    public void testHeader() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .header("name", "value")
                .build();
        assertEquals("value", request.getHeader("name"));
    }
    
    /**
     * Test method method.
     */
    @Test
    public void testMethod() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .method("POST")
                .build();
        assertEquals("POST", request.getMethod());
    }
    
    /**
     * Test parameter method.
     */
    @Test
    public void testParameter() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .parameter("name", "value")
                .build();
        assertEquals("value", request.getParameter("name"));
    }
    
    /**
     * Test parameter method.
     */
    @Test
    public void testParameter2() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .parameter("name", "value1", "value2")
                .build();
        assertEquals("value2", request.getParameterValues("name")[1]);
    }
    
    /**
     * Test pathInfo method.
     */
    @Test
    public void testPathInfo() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .build();
        assertNull(request.getPathInfo());
    }
    
    /**
     * Test pathInfo method.
     */
    @Test
    public void testPathInfo2() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .pathInfo("/pathInfo")
                .build();
        assertEquals("/pathInfo", request.getPathInfo());
    }
    
    /**
     * Test requestedSessionId method.
     */
    @Test
    public void testRequestedSessionId() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .requestedSessionId("does-not-exist")
                .build();
        assertEquals("does-not-exist", request.getRequestedSessionId());
    }
    
    /**
     * Test requestedSessionIdFromCookie method.
     */
    @Test
    public void testRequestedSessionIdFromCookie() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .requestedSessionIdFromCookie(true)
                .build();
        assertTrue(request.isRequestedSessionIdFromCookie());
        assertFalse(request.isRequestedSessionIdFromURL());
    }
    
    /**
     * Test scheme method.
     */
    @Test
    public void testScheme() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .scheme("https")
                .build();
        assertTrue(request.isSecure());
        assertEquals("https", request.getScheme());
    }
    
    /**
     * Test servletPath method.
     */
    @Test
    public void testServletPath() {
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/servletPath")
                .build();
        assertEquals("/servletPath", request.getServletPath());
    }
    
    /**
     * Test webApplication method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testWebApplication() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .webApplication(webApplication)
                .build();
        assertTrue(request.getWebApplication() instanceof WebApplication);
        assertEquals(webApplication, request.getWebApplication());
    }    
}
