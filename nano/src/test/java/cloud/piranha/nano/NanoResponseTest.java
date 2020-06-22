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
package cloud.piranha.nano;

import cloud.piranha.webapp.impl.DefaultWebApplication;
import java.io.ByteArrayOutputStream;
import jakarta.servlet.http.Cookie;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * The JUnit tests for the NanoResponse class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoResponseTest {
    
    /**
     * Test addCookie method.
     */
    @Test
    public void testAddCookie() {
        NanoResponse response = new NanoResponse();
        response.addCookie(new Cookie("name", "value"));
    }

    /**
     * Test addDateHeader method.
     */
    @Test
    public void testAddDateHeader() {
        NanoResponse response = new NanoResponse();
        response.addDateHeader("name", 0);
        assertEquals("0", response.getHeader("name"));
    }

    /**
     * Test addHeader method.
     */
    @Test
    public void testAddHeader() {
        NanoResponse response = new NanoResponse();
        response.addHeader("name", "value");
        assertEquals("value", response.getHeader("name"));
    }

    /**
     * Test addIntHeader method.
     */
    @Test
    public void testAddIntHeader() {
        NanoResponse response = new NanoResponse();
        response.addIntHeader("name", 1);
        assertEquals("1", response.getHeader("name"));
    }

    /**
     * Test containsHeader method.
     */
    @Test
    public void testContainsHeader() {
        NanoResponse response = new NanoResponse();
        assertFalse(response.containsHeader("name"));
    }

    /**
     * Test encodeRedirectURL method.
     */
    @Test
    public void testEncodeRedirectURL() {
        NanoResponse response = new NanoResponse();
        response.setWebApplication(new DefaultWebApplication());
        assertEquals("url", response.encodeRedirectURL("url"));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    public void testEncodeRedirectUrl() {
        NanoResponse response = new NanoResponse();
        response.setWebApplication(new DefaultWebApplication());
        assertEquals("url", response.encodeRedirectUrl("url"));
    }

    /**
     * Test encodeURL method.
     */
    @Test
    public void testEncodeURL() {
        NanoResponse response = new NanoResponse();
        response.setWebApplication(new DefaultWebApplication());
        assertNotNull(response.encodeURL("url"));
        assertEquals("url", response.encodeURL("url"));
    }

    /**
     * Test encodeUrl method.
     */
    @Test
    public void testEncodeUrl() {
        NanoResponse response = new NanoResponse();
        response.setWebApplication(new DefaultWebApplication());
        assertEquals("url", response.encodeUrl("url"));
    }

    /**
     * Test getHeader method.
     */
    @Test
    public void testGetHeader() {
        NanoResponse response = new NanoResponse();
        assertNull(response.getHeader("header"));
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    public void testGetHeaderNames() {
        NanoResponse response = new NanoResponse();
        assertNotNull(response.getHeaderNames());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    public void testGetHeaders() {
        NanoResponse response = new NanoResponse();
        assertNotNull(response.getHeaders("header"));
    }

    /**
     * Test getStatus method.
     */
    @Test
    public void testGetStatus() {
        NanoResponse response = new NanoResponse();
        assertEquals(200, response.getStatus());
    }
    
    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testSendError() throws Exception {
        NanoResponse response = new NanoResponse();
        response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        response.sendError(500, "Errror");
        assertEquals(500, response.getStatus());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testSendError2() throws Exception {
        NanoResponse response = new NanoResponse();
        response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        response.sendError(500);
        assertEquals(500, response.getStatus());
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testSendRedirect() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        NanoResponse response = new NanoResponse();
        response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        response.setWebApplication(webApplication);
        NanoRequest request = new NanoRequest();
        request.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        response.sendRedirect("/redirect");
        webApplication.unlinkRequestAndResponse(request, response);
    }

    /**
     * Test setDateHeader method.
     */
    @Test
    public void testSetDateHeader() {
        NanoResponse response = new NanoResponse();
        response.setDateHeader("header", 0);
    }

    /**
     * Test setHeader method.
     */
    @Test
    public void testSetHeader() {
        NanoResponse response = new NanoResponse();
        response.setHeader("header", "value");
        assertEquals("value", response.getHeader("header"));
    }

    /**
     * Test setIntHeader method.
     */
    @Test
    public void testSetIntHeader() {
        NanoResponse response = new NanoResponse();
        response.setIntHeader("header", 1);
        assertEquals("1", response.getHeader("header"));
    }
}
