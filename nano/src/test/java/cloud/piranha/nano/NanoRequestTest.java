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
package cloud.piranha.nano;

import javax.servlet.http.HttpUpgradeHandler;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * The JUnit tests for the NanoRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoRequestTest {

    /**
     * Test authenticate method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAuthenticate() throws Exception {
        NanoRequest request = new NanoRequest(null);
        request.authenticate(null);
    }

    /**
     * Test changeSessionId method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testChangeSessionId() throws Exception {
        NanoRequest request = new NanoRequest(null);
        request.changeSessionId();
    }

    /**
     * Test getAuthType method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetAuthType() throws Exception {
        NanoRequest request = new NanoRequest(null);
        request.getAuthType();
    }

    /**
     * Test getContextPath method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetContextPath() throws Exception {
        NanoRequest request = new NanoRequest(null);
        assertEquals("", request.getContextPath());
    }

    /**
     * Test getCookies method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetCookies() {
        NanoRequest request = new NanoRequest(null);
        request.getCookies();
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    public void testGetDateHeader() {
        NanoRequest request = new NanoRequest(null);
        assertEquals(-1, request.getDateHeader("header"));
    }

    /**
     * Test getHeader method.
     */
    @Test
    public void testGetHeader() {
        NanoRequest request = new NanoRequest(null);
        assertNull(request.getHeader("header"));
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    public void testGetHeaderNames() {
        NanoRequest request = new NanoRequest(null);
        assertFalse(request.getHeaderNames().hasMoreElements());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    public void testGetHeaders() {
        NanoRequest request = new NanoRequest(null);
        assertFalse(request.getHeaders("myheader").hasMoreElements());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetInputStream() throws Exception {
        NanoRequest request = new NanoRequest(null);
        assertNotNull(request.getInputStream());
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    public void testGetIntHeader() {
        NanoRequest request = new NanoRequest(null);
        assertEquals(-1, request.getIntHeader("header"));
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetPart() throws Exception {
        NanoRequest request = new NanoRequest(null);
        assertNotNull(request.getPart("part"));
    }

    /**
     * Test getParts method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetParts() throws Exception {
        NanoRequest request = new NanoRequest(null);
        assertNotNull(request.getParts());
    }

    /**
     * Test getPathInfo method.
     */
    @Test
    public void testGetPathInfo() {
        NanoRequest request = new NanoRequest(null);
        assertNull(request.getPathInfo());
    }

    /**
     * Test getPathTranslated method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetPathTranslated() throws Exception {
        NanoRequest request = new NanoRequest(null);
        assertNotNull(request.getPathTranslated());
    }

    /**
     * Test getProtocol method.
     */
    @Test
    public void testGetProtocol() {
        NanoRequest request = new NanoRequest(null);
        assertEquals("HTTP/1.1", request.getProtocol());
    }

    /**
     * Test getRemoteUser method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRemoteUser() throws Exception {
        NanoRequest request = new NanoRequest(null);
        assertNotNull(request.getRemoteUser());
    }

    /**
     * Test getRequestURI method.
     */
    @Test
    public void testGetRequestURI() {
        NanoRequest request = new NanoRequest(null);
        assertNotNull(request.getRequestURI());
    }

    /**
     * Test getRequestURL method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestURL() {
        NanoRequest request = new NanoRequest(null);
        assertNotNull(request.getRequestURL());
    }

    /**
     * Test getRequestedSessionId method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRequestedSessionId() {
        NanoRequest request = new NanoRequest(null);
        assertNotNull(request.getRequestedSessionId());
    }

    /**
     * Test getScheme method.
     */
    @Test
    public void testGetScheme() {
        NanoRequest request = new NanoRequest(null);
        assertEquals("http", request.getScheme());
    }

    /**
     * Test getServletPath method.
     */
    @Test
    public void testGetServletPath() {
        NanoRequest request = new NanoRequest(null);
        assertEquals("", request.getServletPath());
    }

    /**
     * Test getSession method.
     */
    @Test
    public void testGetSession() {
        NanoRequest request = new NanoRequest(null);
        assertNull(request.getSession(false));
    }

    /**
     * Test getSession method.
     */
    @Test
    public void testGetSession2() {
        NanoRequest request = new NanoRequest(null);
        assertNull(request.getSession());
    }

    /**
     * Test getUserPrincipal method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetUserPrincipal() {
        NanoRequest request = new NanoRequest(null);
        assertNotNull(request.getUserPrincipal());
    }

    /**
     * Test isRequestedSessionIdFromCookie method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdFromCookie() {
        NanoRequest request = new NanoRequest(null);
        assertFalse(request.isRequestedSessionIdFromCookie());
    }

    /**
     * Test isRequestedSessionIdFromURL method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdFromURL() {
        NanoRequest request = new NanoRequest(null);
        assertFalse(request.isRequestedSessionIdFromURL());
    }

    /**
     * Test isRequestedSessionIdFromUrl method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdFromUrl() {
        NanoRequest request = new NanoRequest(null);
        assertFalse(request.isRequestedSessionIdFromUrl());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsRequestedSessionIdValid() {
        NanoRequest request = new NanoRequest(null);
        assertFalse(request.isRequestedSessionIdValid());
    }

    /**
     * Test isUserInRole method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testIsUserInRole() {
        NanoRequest request = new NanoRequest(null);
        assertFalse(request.isUserInRole("role"));
    }

    /**
     * Test login method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testLogin() throws Exception {
        NanoRequest request = new NanoRequest(null);
        request.login("username", "password");
    }

    /**
     * Test logout method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testLogout() throws Exception {
        NanoRequest request = new NanoRequest(null);
        request.logout();
    }

    /**
     * Test upgrade method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUpgrade() throws Exception {
        NanoRequest request = new NanoRequest(null);
        request.upgrade(HttpUpgradeHandler.class);
    }
}
