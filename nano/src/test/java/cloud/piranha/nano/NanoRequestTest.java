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
package cloud.piranha.nano;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.WebConnection;

import org.junit.jupiter.api.Test;

import cloud.piranha.core.impl.DefaultWebApplication;
import java.io.IOException;

/**
 * The JUnit tests for the NanoRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class NanoRequestTest {

    /**
     * Test authenticate method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAuthenticate() throws Exception {
        NanoRequest request = new NanoRequest();
        assertThrows(NullPointerException.class, () -> request.authenticate(null));
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        NanoResponse response = new NanoResponse();
        response.setWebApplication(webApplication);
        NanoRequest request = new NanoRequest();
        request.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        String sessionId1 = request.getSession(true).getId();
        String sessionId2 = request.changeSessionId();
        webApplication.unlinkRequestAndResponse(request, response);
        assertNotEquals(sessionId1, sessionId2);
    }

    /**
     * Test getAuthType method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetAuthType() throws Exception {
        NanoRequest request = new NanoRequest();
        assertNull(request.getAuthType());
    }

    /**
     * Test getContextPath method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContextPath() throws Exception {
        NanoRequest request = new NanoRequest();
        assertEquals("", request.getContextPath());
    }

    /**
     * Test getCookies method.
     */
    @Test
    void testGetCookies() {
        NanoRequest request = new NanoRequest();
        assertNull(request.getCookies());
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    void testGetDateHeader() {
        NanoRequest request = new NanoRequest();
        assertEquals(-1, request.getDateHeader("header"));
    }

    /**
     * Test getHeader method.
     */
    @Test
    void testGetHeader() {
        NanoRequest request = new NanoRequest();
        assertNull(request.getHeader("header"));
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    void testGetHeaderNames() {
        NanoRequest request = new NanoRequest();
        assertTrue(request.getHeaderNames().hasMoreElements());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    void testGetHeaders() {
        NanoRequest request = new NanoRequest();
        assertFalse(request.getHeaders("myheader").hasMoreElements());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream() throws Exception {
        NanoRequest request = new NanoRequest();
        assertNotNull(request.getInputStream());
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    void testGetIntHeader() {
        NanoRequest request = new NanoRequest();
        assertEquals(-1, request.getIntHeader("header"));
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart() throws Exception {
        NanoPiranha piranha = new NanoPiranha();
        NanoRequest request = new NanoRequest();
        request.setWebApplication(piranha.getWebApplication());
        assertNull(request.getPart("part"));
    }

    /**
     * Test getParts method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetParts() throws Exception {
        NanoPiranha piranha = new NanoPiranha();
        NanoRequest request = new NanoRequest();
        request.setWebApplication(piranha.getWebApplication());
        assertNotNull(request.getParts());
        assertTrue(request.getParts().isEmpty());
    }

    /**
     * Test getPathInfo method.
     */
    @Test
    void testGetPathInfo() {
        NanoRequest request = new NanoRequest();
        assertNull(request.getPathInfo());
    }

    /**
     * Test getPathTranslated method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPathTranslated() throws Exception {
        NanoRequest request = new NanoRequest();
        assertNull(request.getPathTranslated());
    }

    /**
     * Test getProtocol method.
     */
    @Test
    void testGetProtocol() {
        NanoRequest request = new NanoRequest();
        assertEquals("HTTP/1.1", request.getProtocol());
    }

    /**
     * Test getRemoteUser method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRemoteUser() throws Exception {
        NanoRequest request = new NanoRequest();
        assertNull(request.getRemoteUser());
    }

    /**
     * Test getRequestURI method.
     */
    @Test
    void testGetRequestURI() {
        NanoRequest request = new NanoRequest();
        assertNotNull(request.getRequestURI());
    }

    /**
     * Test getRequestURL method.
     */
    @Test
    void testGetRequestURL() {
        NanoRequest request = new NanoRequest();
        assertNotNull(request.getRequestURL());
    }

    /**
     * Test getRequestedSessionId method.
     */
    @Test
    void testGetRequestedSessionId() {
        NanoRequest request = new NanoRequest();
        assertNull(request.getRequestedSessionId());
    }

    /**
     * Test getScheme method.
     */
    @Test
    void testGetScheme() {
        NanoRequest request = new NanoRequest();
        assertEquals("http", request.getScheme());
    }

    /**
     * Test getServletPath method.
     */
    @Test
    void testGetServletPath() {
        NanoRequest request = new NanoRequest();
        assertEquals("", request.getServletPath());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        NanoResponse response = new NanoResponse();
        response.setWebApplication(webApplication);
        NanoRequest request = new NanoRequest();
        request.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNull(request.getSession(false));
        webApplication.unlinkRequestAndResponse(request, response);
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        NanoResponse response = new NanoResponse();
        response.setWebApplication(webApplication);
        NanoRequest request = new NanoRequest();
        request.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(request.getSession());
        webApplication.unlinkRequestAndResponse(request, response);
    }

    /**
     * Test getUserPrincipal method.
     */
    @Test
    void testGetUserPrincipal() {
        NanoRequest request = new NanoRequest();
        assertNull(request.getUserPrincipal());
    }

    /**
     * Test isRequestedSessionIdFromCookie method.
     */
    @Test
    void testIsRequestedSessionIdFromCookie() {
        NanoRequest request = new NanoRequest();
        assertFalse(request.isRequestedSessionIdFromCookie());
    }

    /**
     * Test isRequestedSessionIdFromURL method.
     */
    @Test
    void testIsRequestedSessionIdFromURL() {
        NanoRequest request = new NanoRequest();
        assertFalse(request.isRequestedSessionIdFromURL());
    }

    /**
     * Test isRequestedSessionIdFromUrl method.
     */
    @Test
    void testIsRequestedSessionIdFromUrl() {
        NanoRequest request = new NanoRequest();
        assertFalse(request.isRequestedSessionIdFromUrl());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test
    void testIsRequestedSessionIdValid() {
        NanoRequest request = new NanoRequest();
        assertFalse(request.isRequestedSessionIdValid());
    }

    /**
     * Test isUserInRole method.
     */
    @Test
    void testIsUserInRole() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        NanoResponse response = new NanoResponse();
        response.setWebApplication(webApplication);
        NanoRequest request = new NanoRequest();
        request.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertFalse(request.isUserInRole("role"));
        webApplication.unlinkRequestAndResponse(request, response);
    }

    /**
     * Test login method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testLogin() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        NanoResponse response = new NanoResponse();
        response.setWebApplication(webApplication);
        NanoRequest request = new NanoRequest();
        request.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        request.login("username", "password");
    }

    /**
     * Test logout method.
     */
    @Test
    void testLogout() {
        try {
            DefaultWebApplication webApplication = new DefaultWebApplication();
            NanoResponse response = new NanoResponse();
            response.setWebApplication(webApplication);
            NanoRequest request = new NanoRequest();
            request.setWebApplication(webApplication);
            webApplication.linkRequestAndResponse(request, response);
            request.logout();
        } catch (ServletException ex) {
            fail();
        }
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testUpgrade() {
        try {
            DefaultWebApplication webApplication = new DefaultWebApplication();
            NanoResponse response = new NanoResponse();
            response.setWebApplication(webApplication);
            NanoRequest request = new NanoRequest();
            request.setWebApplication(webApplication);
            webApplication.linkRequestAndResponse(request, response);
            request.upgrade(TestHttpUpgradeHandler.class);
            webApplication.unlinkRequestAndResponse(request, response);
        } catch (IOException | ServletException ex) {
            fail();
        }
    }

    public static class TestHttpUpgradeHandler implements HttpUpgradeHandler {

        @Override
        public void init(WebConnection webConnection) {
        }

        @Override
        public void destroy() {
        }
    }
}
