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

import javax.servlet.http.HttpUpgradeHandler;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * The JUnit tests for the NanoHttpServletRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoHttpServletRequestTest {

    /**
     * Test authenticate method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAuthenticate() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        request.authenticate(null);
    }

    /**
     * Test changeSessionId method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testChangeSessionId() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        request.changeSessionId();
    }

    /**
     * Test getAuthType method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetAuthType() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        request.getAuthType();
    }

    /**
     * Test getContextPath method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetContextPath() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertEquals("", request.getContextPath());
    }

    /**
     * Test getCookies method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetCookies() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        request.getCookies();
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    public void testGetDateHeader() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertEquals(-1, request.getDateHeader("header"));
    }

    /**
     * Test getHeader method.
     */
    @Test
    public void testGetHeader() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNull(request.getHeader("header"));
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    public void testGetHeaderNames() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertFalse(request.getHeaderNames().hasMoreElements());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    public void testGetHeaders() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertFalse(request.getHeaders("myheader").hasMoreElements());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetInputStream() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getInputStream());
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    public void testGetIntHeader() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertEquals(-1, request.getIntHeader("header"));
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetPart() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getPart("part"));
    }

    /**
     * Test getParts method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetParts() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getParts());
    }

    /**
     * Test getPathInfo method.
     */
    @Test
    public void testGetPathInfo() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNull(request.getPathInfo());
    }

    /**
     * Test getPathTranslated method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetPathTranslated() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getPathTranslated());
    }

    /**
     * Test getProtocol method.
     */
    @Test
    public void testGetProtocol() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertEquals("HTTP/1.1", request.getProtocol());
    }

    /**
     * Test getRemoteUser method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRemoteUser() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getRemoteUser());
    }

    /**
     * Test getRequestURI method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestURI() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getRequestURI());
    }

    /**
     * Test getRequestURL method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestURL() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getRequestURL());
    }

    /**
     * Test getRequestedSessionId method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestedSessionId() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getRequestedSessionId());
    }

    /**
     * Test getScheme method.
     */
    @Test
    public void testGetScheme() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertEquals("http", request.getScheme());
    }

    /**
     * Test getServletPath method.
     */
    @Test
    public void testGetServletPath() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertEquals("", request.getServletPath());
    }

    /**
     * Test getSession method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetSession() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getSession(false));
    }

    /**
     * Test getSession method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetSession2() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getSession());
    }

    /**
     * Test getUserPrincipal method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetUserPrincipal() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertNotNull(request.getUserPrincipal());
    }

    /**
     * Test isRequestedSessionIdFromCookie method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdFromCookie() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertFalse(request.isRequestedSessionIdFromCookie());
    }

    /**
     * Test isRequestedSessionIdFromURL method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdFromURL() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertFalse(request.isRequestedSessionIdFromURL());
    }

    /**
     * Test isRequestedSessionIdFromUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdFromUrl() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertFalse(request.isRequestedSessionIdFromUrl());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdValid() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertFalse(request.isRequestedSessionIdValid());
    }

    /**
     * Test isUserInRole method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsUserInRole() {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        assertFalse(request.isUserInRole("role"));
    }

    /**
     * Test login method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testLogin() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        request.login("username", "password");
    }

    /**
     * Test logout method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testLogout() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        request.logout();
    }

    /**
     * Test upgrade method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUpgrade() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(null);
        request.upgrade(HttpUpgradeHandler.class);
    }
}
