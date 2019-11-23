/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.nano;

import javax.servlet.http.Cookie;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * The JUnit tests for the NanoHttpServletResponse class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoHttpServletResponseTest {

    /**
     * Test addCookie method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddCookie() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.addCookie(new Cookie("name", "value"));
    }

    /**
     * Test addDateHeader method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddDateHeader() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.addDateHeader("name", 0);
    }

    /**
     * Test addHeader method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddHeader() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.addHeader("name", "value");
    }

    /**
     * Test addIntHeader method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddIntHeader() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.addIntHeader("name", 1);
    }

    /**
     * Test containsHeader method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testContainsHeader() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.containsHeader("name");
    }

    /**
     * Test encodeRedirectURL method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeRedirectURL() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.encodeRedirectURL("url");
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeRedirectUrl() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.encodeRedirectUrl("url");
    }

    /**
     * Test encodeURL method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeURL() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.encodeURL("url");
    }

    /**
     * Test encodeUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testEncodeUrl() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.encodeUrl("url");
    }

    /**
     * Test getHeader method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetHeader() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.getHeader("header");
    }

    /**
     * Test getHeaderNames method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetHeaderNames() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.getHeaderNames();
    }

    /**
     * Test getHeaders method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetHeaders() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.getHeaders("header");
    }

    /**
     * Test getStatus method.
     */
    @Test
    public void testGetStatus() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        assertEquals(200, response.getStatus());
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSendError() throws Exception {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.sendError(500, "Errror");
    }

    /**
     * Test sendError method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSendError2() throws Exception {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.sendError(500);
    }

    /**
     * Test sendRedirect method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSendRedirect() throws Exception {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.sendRedirect("/redirect");
    }

    /**
     * Test setDateHeader method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetDateHeader() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.setDateHeader("header", 0);
    }

    /**
     * Test setHeader method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetHeader() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.setHeader("header", "value");
    }

    /**
     * Test setIntHeader method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSetIntHeader() {
        NanoHttpServletResponse response = new NanoHttpServletResponse(null);
        response.setIntHeader("header", 1);
    }
}
