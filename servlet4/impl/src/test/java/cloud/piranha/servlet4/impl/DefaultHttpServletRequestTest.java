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
package cloud.piranha.servlet4.impl;

import cloud.piranha.servlet4.impl.DefaultWebApplicationRequest;
import cloud.piranha.servlet4.impl.DefaultSecurityManager;
import cloud.piranha.servlet4.impl.DefaultWebApplicationRequestMapper;
import cloud.piranha.servlet4.impl.DefaultWebApplication;
import cloud.piranha.servlet4.impl.DefaultWebApplicationResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;

import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultWebApplicationRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultHttpServletRequestTest {

    /**
     * Test authenticate.
     *
     * @throws Exception
     */
    @Test
    void testAuthenticate() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSecurityManager(new DefaultSecurityManager());
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        HttpServletResponse response = new TestWebApplicationResponse();
        try {
            request.authenticate(response);
        } catch (IOException | ServletException exception) {
        }
    }

    /**
     * Test authenticate.
     *
     * @throws Exception
     */
    @Test
    void testAuthenticate2() throws Exception {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSecurityManager(securityManager);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        HttpServletResponse response = new TestWebApplicationResponse();
        request.authenticate(response);
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        assertThrows(IllegalStateException.class, () -> request.changeSessionId());
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        request.changeSessionId();
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertThrows(IllegalStateException.class, () -> request.getAsyncContext());
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext2() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        request.setWebApplication(webApplication);
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        request.setAsyncSupported(true);
        request.startAsync();
        assertNotNull(request.getAsyncContext());
    }

    /**
     * Test getContentLengthLong method.
     */
    @Test
    void testGetContentLengthLong() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(-1L, request.getContentLengthLong());
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    void testGetDateHeader() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(-1L, request.getDateHeader("notfound"));
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    void testGetIntHeader() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(-1, request.getIntHeader("notfound"));
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocale() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setHeader("Accept-Language", "en");
        assertNotNull(request.getLocale());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocales() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setHeader("Accept-Language", "en, de");
        assertNotNull(request.getLocales());
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setContentType("text/html");
        assertThrows(ServletException.class, () -> request.getPart("not_there"));
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setContentType("multipart/form-data");
        assertNull(request.getPart("not_there"));
    }

    /**
     * Test getRealPath method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRealPath() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertThrows(UnsupportedOperationException.class, () -> request.getRealPath("/path"));
    }

    /**
     * Test getRequestDispatcher method.
     */
    @Test
    void testGetRequestDispatcher() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        request.setWebApplication(webApp);
        request.getRequestDispatcher("/test");
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        request.setWebApplication(webApp);
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession(false));
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        request.setWebApplication(webApp);
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(false);
        assertNull(session);
    }

    /**
     * Test getUpgradeHandler method.
     */
    @Test
    void testGetUpgradeHandler() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest() {
        };
        assertNull(request.getUpgradeHandler());
    }

    /**
     * Test setRequestedSessionIdFromURL method.
     */
    @Test
    void testIsRequestedSessionIdFromUrl() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromUrl());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test
    void testIsRequestedSessionIdValid() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        request.setWebApplication(webApp);
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertTrue(request.isRequestedSessionIdValid());
    }

    /**
     * Test isSecure method.
     */
    @Test
    void testIsSecure() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setScheme("https");
        assertTrue(request.isSecure());
    }

    /**
     * Test isUpgraded method.
     */
    @Test
    void testIsUpgraded() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertFalse(request.isUpgraded());
    }

    /**
     * Test isUserInRole method.
     */
    @Test
    void testIsUserInRole() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        request.setWebApplication(webApplication);
        assertFalse(request.isUserInRole("notmatched"));
    }

    /**
     * Test login method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testLogin() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.addUser("username", "password", new String[]{});
        webApplication.setSecurityManager(securityManager);
        request.login("username", "password");
        assertNotNull(request.getUserPrincipal());
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    void testRemoveAttribute() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        request.setWebApplication(webApplication);
        request.setAttribute("name", "value");
        assertNotNull(request.getAttribute("name"));
        request.removeAttribute("name");
        assertNull(request.getAttribute("name"));
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getCharacterEncoding());
        request.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", request.getCharacterEncoding());
    }

    /**
     * Test setCookies method.
     */
    @Test
    void testSetCookies() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getCookies());
        request.setCookies(new Cookie[0]);
        assertNull(request.getCookies());
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("name", "value");
        request.setCookies(cookies);
        assertNotNull(request.getCookies());
        assertEquals("name", request.getCookies()[0].getName());
        assertEquals("value", request.getCookies()[0].getValue());
    }

    /**
     * Test setLocalAddr method.
     */
    @Test
    void testSetLocalAddr() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getLocalAddr());
        request.setLocalAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getLocalAddr());
    }

    /**
     * Test setLocalName method.
     */
    @Test
    void testSetLocalName() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getLocalName());
        request.setLocalName("localhost");
        assertEquals("localhost", request.getLocalName());
    }

    /**
     * Test setLocalPort method.
     */
    @Test
    void testSetLocalPort() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(0, request.getLocalPort());
        request.setLocalPort(12345);
        assertEquals(12345, request.getLocalPort());
    }

    /**
     * Test setProtocol method.
     */
    @Test
    void testSetProtocol() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals("HTTP/1.1", request.getProtocol());
        request.setProtocol("HTTP/1.0");
        assertEquals("HTTP/1.0", request.getProtocol());
    }

    /**
     * Test setRemoteAddr method.
     */
    @Test
    void testSetRemoteAddr() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getRemoteAddr());
        request.setRemoteAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getRemoteAddr());
    }

    /**
     * Test setRemoteHost method.
     */
    @Test
    void testSetRemoteHost() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getRemoteHost());
        request.setRemoteHost("localhost");
        assertEquals("localhost", request.getRemoteHost());
    }

    /**
     * Test setRemotePort method.
     */
    @Test
    void testSetRemotePort() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(0, request.getRemotePort());
        request.setRemotePort(12345);
        assertEquals(12345, request.getRemotePort());
    }

    /**
     * Test setRequestedSessionIdFromCookie method.
     */
    @Test
    void testSetRequestedSessionIdFromCookie() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromCookie());
        request.setRequestedSessionIdFromCookie(true);
        assertTrue(request.isRequestedSessionIdFromCookie());
    }

    /**
     * Test setRequestedSessionIdFromURL method.
     */
    @Test
    void testSetRequestedSessionIdFromURL() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromURL());
        request.setRequestedSessionIdFromURL(true);
        assertTrue(request.isRequestedSessionIdFromURL());
    }

    /**
     * Test setServerName method.
     */
    @Test
    void testSetServerName() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals("localhost", request.getServerName());
        request.setServerName("my.host.com");
        assertEquals("my.host.com", request.getServerName());
    }

    /**
     * Test setServerPort method.
     */
    @Test
    void testSetServerPort() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(80, request.getServerPort());
        request.setServerPort(8080);
        assertEquals(8080, request.getServerPort());
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync() {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        request.setAttribute("piranha.response", response);
        request.setAsyncSupported(false);
        assertThrows(IllegalStateException.class, () -> request.startAsync(request, response));
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync2() {
        assertThrows(IllegalStateException.class, () -> {
            try {
                DefaultWebApplicationRequest request = new TestWebApplicationRequest();
                request.setAttribute("piranha.response", new TestWebApplicationResponse());
                request.setAsyncSupported(false);
                request.startAsync();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        });
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testUpgrade() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(request.upgrade(TestHandler.class));
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testUpgrade2() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertThrows(ServletException.class, () -> request.upgrade(TestThrowingHandler.class));
    }

    static class TestHandler implements HttpUpgradeHandler {

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }

    static class TestThrowingHandler implements HttpUpgradeHandler {

        TestThrowingHandler() throws IllegalAccessException {
            throw new IllegalAccessException();
        }

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }
}
