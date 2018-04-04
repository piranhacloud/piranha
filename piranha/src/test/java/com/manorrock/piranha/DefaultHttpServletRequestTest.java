/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultHttpServletRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServletRequestTest {

    /**
     * Test authenticate.
     *
     * @throws Exception
     */
    @Test
    public void testAuthenticate() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSecurityManager(new DefaultSecurityManager());
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        HttpServletResponse response = new TestHttpServletResponse();
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
    public void testAuthenticate2() throws Exception {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSecurityManager(securityManager);
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        HttpServletResponse response = new TestHttpServletResponse();
        request.authenticate(response);
    }

    /**
     * Test changeSessionId method.
     */
    @Test(expected = IllegalStateException.class)
    public void testChangeSessionId() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        request.changeSessionId();
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    public void testChangeSessionId2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        request.changeSessionId();
    }

    /**
     * Test getAsyncContext method.
     */
    @Test(expected = IllegalStateException.class)
    public void testGetAsyncContext() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.getAsyncContext();
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    public void testGetAsyncContext2() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
    public void testGetContentLengthLong() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertEquals(-1L, request.getContentLengthLong());
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    public void testGetDateHeader() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertEquals(-1L, request.getDateHeader("notfound"));
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    public void testGetIntHeader() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertEquals(-1, request.getIntHeader("notfound"));
    }

    /**
     * Test getLocale method.
     */
    @Test
    public void testGetLocale() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setHeader("Accept-Language", "en");
        assertNotNull(request.getLocale());
    }

    /**
     * Test getLocale method.
     */
    @Test
    public void testGetLocales() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setHeader("Accept-Language", "en, de");
        assertNotNull(request.getLocales());
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = ServletException.class)
    public void testGetPart() throws Exception {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setContentType("text/html");
        request.getPart("not_there");
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetPart2() throws Exception {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setContentType("multipart/form-data");
        assertNull(request.getPart("not_there"));
    }

    /**
     * Test getReader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetReader() throws Exception {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertNotNull(request.getReader());
    }

    /**
     * Test getRealPath method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRealPath() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApplication);
        request.getRealPath("/path");
    }

    /**
     * Test getRequestDispatcher method.
     */
    @Test
    public void testGetRequestDispatcher() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
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
    public void testGetSession() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
    public void testGetSession2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
    public void testGetSession3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
    public void testGetUpgradeHandler() {
        DefaultHttpServletRequest request = new DefaultHttpServletRequest() {
        };
        assertNull(request.getUpgradeHandler());
    }

    /**
     * Test setRequestedSessionIdFromURL method.
     */
    @Test
    public void testIsRequestedSessionIdFromUrl() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertFalse(request.isRequestedSessionIdFromUrl());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test
    public void testIsRequestedSessionIdValid() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
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
    public void testIsSecure() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setScheme("https");
        assertTrue(request.isSecure());
    }

    /**
     * Test isUpgraded method.
     */
    @Test
    public void testIsUpgraded() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertFalse(request.isUpgraded());
    }

    /**
     * Test isUserInRole method.
     */
    @Test
    public void testIsUserInRole() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
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
    public void testLogin() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpServletRequest request = new TestHttpServletRequest();
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
    public void testRemoveAttribute() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
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
    public void testSetCharacterEncoding() throws Exception {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertNull(request.getCharacterEncoding());
        request.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", request.getCharacterEncoding());
    }

    /**
     * Test setCookies method.
     */
    @Test
    public void testSetCookies() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
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
    public void testSetLocalAddr() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertNull(request.getLocalAddr());
        request.setLocalAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getLocalAddr());
    }

    /**
     * Test setLocalName method.
     */
    @Test
    public void testSetLocalName() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertNull(request.getLocalName());
        request.setLocalName("localhost");
        assertEquals("localhost", request.getLocalName());
    }

    /**
     * Test setLocalPort method.
     */
    @Test
    public void testSetLocalPort() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertEquals(0, request.getLocalPort());
        request.setLocalPort(12345);
        assertEquals(12345, request.getLocalPort());
    }

    /**
     * Test setProtocol method.
     */
    @Test
    public void testSetProtocol() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertEquals("HTTP/1.1", request.getProtocol());
        request.setProtocol("HTTP/1.0");
        assertEquals("HTTP/1.0", request.getProtocol());
    }

    /**
     * Test setRemoteAddr method.
     */
    @Test
    public void testSetRemoteAddr() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertNull(request.getRemoteAddr());
        request.setRemoteAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getRemoteAddr());
    }

    /**
     * Test setRemoteHost method.
     */
    @Test
    public void testSetRemoteHost() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertNull(request.getRemoteHost());
        request.setRemoteHost("localhost");
        assertEquals("localhost", request.getRemoteHost());
    }

    /**
     * Test setRemotePort method.
     */
    @Test
    public void testSetRemotePort() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertEquals(0, request.getRemotePort());
        request.setRemotePort(12345);
        assertEquals(12345, request.getRemotePort());
    }

    /**
     * Test setRequestedSessionIdFromCookie method.
     */
    @Test
    public void testSetRequestedSessionIdFromCookie() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertFalse(request.isRequestedSessionIdFromCookie());
        request.setRequestedSessionIdFromCookie(true);
        assertTrue(request.isRequestedSessionIdFromCookie());
    }

    /**
     * Test setRequestedSessionIdFromURL method.
     */
    @Test
    public void testSetRequestedSessionIdFromURL() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertFalse(request.isRequestedSessionIdFromURL());
        request.setRequestedSessionIdFromURL(true);
        assertTrue(request.isRequestedSessionIdFromURL());
    }

    /**
     * Test setServerName method.
     */
    @Test
    public void testSetServerName() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertEquals("localhost", request.getServerName());
        request.setServerName("my.host.com");
        assertEquals("my.host.com", request.getServerName());
    }

    /**
     * Test setServerPort method.
     */
    @Test
    public void testSetServerPort() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertEquals(80, request.getServerPort());
        request.setServerPort(8080);
        assertEquals(8080, request.getServerPort());
    }

    /**
     * Test startAsync method.
     */
    @Test(expected = IllegalStateException.class)
    public void testStartAsync() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setAsyncSupported(false);
        request.startAsync(null, null);
    }

    /**
     * Test startAsync method.
     */
    @Test(expected = IllegalStateException.class)
    public void testStartAsync2() {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setAsyncSupported(false);
        request.startAsync();
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testUpgrade() throws Exception {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        assertNotNull(request.upgrade(TestHandler.class));
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = ServletException.class)
    public void testUpgrade2() throws Exception {
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.upgrade(TestThrowingHandler.class);
    }

    public static class TestHandler implements HttpUpgradeHandler {

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }

    public static class TestThrowingHandler implements HttpUpgradeHandler {

        public TestThrowingHandler() throws IllegalAccessException {
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
