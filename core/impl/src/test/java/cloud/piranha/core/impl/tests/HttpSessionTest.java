/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl.tests;

import cloud.piranha.core.impl.DefaultHttpSession;
import cloud.piranha.core.impl.DefaultHttpSessionManager;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import cloud.piranha.core.impl.DefaultWebApplicationRequestMapper;
import cloud.piranha.core.impl.DefaultWebApplicationResponse;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.SessionTrackingMode;
import static jakarta.servlet.SessionTrackingMode.COOKIE;
import static jakarta.servlet.SessionTrackingMode.SSL;
import static jakarta.servlet.SessionTrackingMode.URL;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionIdListener;
import jakarta.servlet.http.HttpSessionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * All JUnit tests related to HttpSession functionality.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpSessionTest {

    /**
     * Test attributeAdded method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeAdded() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new TestAttributeAddedListener());
        webApplication.addServlet("testAttributeAddedServlet",
                new TestAttributeAddedServlet());
        webApplication.addServletMapping("testAttributeAddedServlet",
                "/attributeAdded");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/attributeAdded");
        request.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("attributeAdded"));
        webApplication.stop();
    }

    /**
     * Test attributeRemoved method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeRemoved() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new TestAttributeRemovedListener());
        webApplication.addServlet("attributeRemovedServlet",
                new TestAttributeRemovedServlet());
        webApplication.addServletMapping("attributeRemovedServlet",
                "/attributeRemoved");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/attributeRemoved");
        request.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("attributeRemoved"));
        webApplication.stop();
    }

    /**
     * Test attributeReplaced method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeReplaced() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new TestAttributeReplacedListener());
        webApplication.addServlet("attributeReplacedServlet",
                new TestAttributeReplacedServlet());
        webApplication.addServletMapping("attributeReplacedServlet",
                "/attributeReplaced");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/attributeReplaced");
        request.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("attributeReplaced"));
        webApplication.stop();
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.changeSessionId()));
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        HttpSession session = request.getSession(true);
        String sessionId1 = session.getId();
        request.setRequestedSessionId(session.getId());
        String sessionId2 = request.changeSessionId();
        assertNotEquals(sessionId1, sessionId2);
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        HttpSession session = request.getSession(true);
        String previousSessionId = session.getId();
        String newSessionId = request.changeSessionId();
        assertNotEquals(previousSessionId, newSessionId);
        assertEquals(newSessionId, request.getSession(false).getId());
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testCreateListener() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.createListener(TestCreateListenerHttpSessionListener.class));
    }

    /**
     * Test createSession method.
     */
    @Test
    void testCreateSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertNotNull(assertThrows(NullPointerException.class,
                () -> sessionManager.createSession(null)));
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    void testEncodeRedirectURL() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals("test", sessionManager.encodeRedirectURL(null, "test"));
    }

    /**
     * Test encodeURL method.
     */
    @Test
    void testEncodeURL() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals("test", sessionManager.encodeURL(null, "test"));
    }

    /**
     * Test getAttribute method.
     */
    @Test
    void testGetAttribute() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
        session.setAttribute("TEST", "TEST");
        assertEquals("TEST", session.getAttribute("TEST"));
        session.removeAttribute("TEST");
        assertNull(session.getAttribute("TEST"));
    }

    /**
     * Test getAttributeNames method.
     */
    @Test
    void testGetAttributeNames() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertFalse(session.getAttributeNames().hasMoreElements());
    }

    /**
     * Test getComment method.
     */
    @Test
    void testGetComment() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        sessionManager.setComment("COMMENT");
        assertEquals("COMMENT", sessionManager.getComment());
    }

    /**
     * Test getCookies method.
     */
    @Test
    void testGetCookies() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApp);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);

        sessionManager.setComment("Comment");
        sessionManager.setDomain("SessionCookie");
        sessionManager.setHttpOnly(true);
        sessionManager.setName("SessionCookie");
        sessionManager.setMaxAge(100);
        sessionManager.setPath("/context");
        sessionManager.setSecure(true);

        sessionManager.createSession(request);

        Cookie sessionCookie = response.getCookies()
                .stream()
                .filter(cookie -> "SessionCookie".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(sessionCookie);
        assertEquals(sessionManager.getComment(), sessionCookie.getComment());
        assertEquals(sessionManager.getDomain(), sessionCookie.getDomain());
        assertTrue(sessionCookie.isHttpOnly());
        assertEquals(sessionManager.getMaxAge(), sessionCookie.getMaxAge());
        assertEquals(sessionManager.getPath(), sessionCookie.getPath());
        assertTrue(sessionCookie.getSecure());
    }

    /**
     * Test getCreationTime method.
     */
    @Test
    void testGetCreationTime() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertTrue(session.getCreationTime() > 0);
    }

    /**
     * Test getDefaultSessionTrackingModes method.
     */
    @Test
    void testGetDefaultSessionTrackingModes() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertTrue(webApp.getDefaultSessionTrackingModes().contains(SessionTrackingMode.COOKIE));
    }

    /**
     * Test getDomain method.
     */
    @Test
    void testGetDomain() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        sessionManager.setDomain("domain");
        assertEquals("domain", sessionManager.getDomain());
    }

    /**
     * Test getEffectivfeSessionTrackingModes method.
     */
    @Test
    void testGetEffectiveSessionTrackingModes() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals(sessionManager.getDefaultSessionTrackingModes(), sessionManager.getEffectiveSessionTrackingModes());
    }

    /**
     * Test getEffectiveSessionTrackingModes method.
     */
    @Test
    void testGetEffectiveSessionTrackingModes2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        Set<SessionTrackingMode> trackingModes = EnumSet.of(SessionTrackingMode.URL);
        webApp.setSessionTrackingModes(trackingModes);
        assertTrue(webApp.getEffectiveSessionTrackingModes().contains(SessionTrackingMode.URL));
    }

    /**
     * Test getEffectiveSessionTrackingModes method.
     */
    @Test
    void testGetEffectiveSessionTrackingModes3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        Set<SessionTrackingMode> trackingModes = EnumSet.of(SessionTrackingMode.URL);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> webApp.setSessionTrackingModes(trackingModes)));
    }

    /**
     * Test getId method.
     */
    @Test
    void testGetId() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setId("ID");
        assertEquals("ID", session.getId());
    }

    /**
     * Test getLastAccessedTime method.
     */
    @Test
    void testGetLastAccessedTime() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertTrue(session.getCreationTime() > 0);
        assertTrue(session.getLastAccessedTime() >= session.getCreationTime());
    }

    /**
     * Test getMaxAge method.
     */
    @Test
    void testGetMaxAge() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        assertEquals(-1, sessionManager.getMaxAge());
        sessionManager.setMaxAge(60);
        assertEquals(60, sessionManager.getMaxAge());
    }

    /**
     * Test getMaxInactiveInterval method.
     */
    @Test
    void testGetMaxInactiveInterval() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setMaxInactiveInterval(1000);
        assertEquals(1000, session.getMaxInactiveInterval());
    }

    /**
     * Test getName method.
     */
    @Test
    void testGetName() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        sessionManager.setName("JSESSIONID");
        assertEquals("JSESSIONID", sessionManager.getName());
    }

    /**
     * Test getPath method.
     */
    @Test
    void testGetPath() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        sessionManager.setPath("/");
        assertEquals("/", sessionManager.getPath());
    }

    /**
     * Test getServletContext method.
     */
    @Test
    void testGetServletContext() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(webApp.getManager().getHttpSessionManager());
        assertNotNull(session.getServletContext());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertNotNull(assertThrows(NullPointerException.class,
                () -> sessionManager.getSession(null, null)));
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApp);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = sessionManager.createSession(request);
        request.setRequestedSessionId(session.getId());
        assertNotNull(sessionManager.getSession(request, session.getId()));
    }

    /**
     * Test getSession.
     *
     * @throws Exception
     */
    @Test
    void testGetSession3() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        ServletRegistration.Dynamic dynamic = webApp.addServlet("session", TestGetSession3Servlet.class);
        assertNotNull(dynamic);
        dynamic.addMapping("/session");
        webApp.initialize();
        webApp.start();

        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/session");
        TestWebApplicationResponse response = new TestWebApplicationResponse();

        webApp.service(request, response);

        assertNotNull(response.getResponseBytes());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession4() {
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
    void testGetSession5() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
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
    void testGetSession6() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(false);
        assertNull(session);
    }

    /**
     * Test getSessionContext method.
     */
    @Test
    void testGetSessionContext() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertNull(session.getSessionContext());
    }

    /**
     * Test getSessionCookieConfig method.
     */
    @Test
    void testGetSessionCookieConfig() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertNotNull(sessionManager.getSessionCookieConfig());
    }

    /**
     * Test getSessionCookieConfig method.
     */
    @Test
    void testGetSessionCookieConfig2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getSessionCookieConfig());
    }

    /**
     * Test getSessionManager method.
     */
    @Test
    void testGetSessionManager() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setHttpSessionManager(null);
        assertNull(webApp.getManager().getHttpSessionManager());
    }

    /**
     * Test getSessionManager method.
     */
    @Test
    void testGetSessionManager2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getManager().getHttpSessionManager());
    }

    /**
     * Test getSessionTimeout method.
     */
    @Test
    void testGetSessionTimeout() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        assertEquals(10, sessionManager.getSessionTimeout());
        sessionManager.setSessionTimeout(5);
        assertEquals(5, sessionManager.getSessionTimeout());
    }

    /**
     * Test getValue method.
     */
    @Test
    void testGetValue() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
        session.putValue("TEST", "TEST");
        assertEquals("TEST", session.getValue("TEST"));
        session.removeValue("TEST");
        assertNull(session.getValue("TEST"));
    }

    /**
     * Test getValueNames method.
     */
    @Test
    void testGetValueNames() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertEquals(0, session.getValueNames().length);
    }

    /**
     * Test invalidate method.
     */
    @Test
    void testInvalidate() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
        session.invalidate();
        try {
            session.isNew();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * Test invalidate method.
     *
     * @throws IllegalStateException when the session is invalid.
     */
    @Test
    void testInvalidate2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
        session.invalidate();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> session.setAttribute("TEST", "TEST")));
    }

    /**
     * Test isHttpOnly method.
     */
    @Test
    void testIsHttpOnly() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        assertFalse(sessionManager.isHttpOnly());
        sessionManager.setHttpOnly(true);
        assertTrue(sessionManager.isHttpOnly());
    }

    /**
     * Test isNew method.
     */
    @Test
    void testIsNew() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setNew(true);
        assertTrue(session.isNew());
        session.setNew(false);
        assertFalse(session.isNew());
    }

    /**
     * Test isRequestedSessionIdFromUrl method.
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
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        assertFalse(sessionManager.isSecure());
        sessionManager.setSecure(true);
        assertTrue(sessionManager.isSecure());
    }

    /**
     * Test sessionCreated method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSessionCreated() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new TestSessionCreatedListener());
        webApplication.addServlet("sessionCreatedServlet", new TestSessionCreatedServlet());
        webApplication.addServletMapping("sessionCreatedServlet", "/sessionCreated");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/sessionCreated");
        request.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("sessionCreated"));
        assertTrue(webApplication.getAttribute("session") instanceof HttpSession);
        webApplication.stop();
    }

    /**
     * Test sessionDestroyed method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSessionDestroyed() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new TestSessionDestroyedListener());
        webApplication.addServlet("sessionDestroyedServlet", new TestSessionDestroyedServlet());
        webApplication.addServletMapping("sessionDestroyedServlet", "/sessionDestroyed");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/sessionDestroyed");
        request.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("sessionDestroyed"));
        webApplication.stop();
        webApplication.destroy();
    }

    /**
     * Test sessionIdChanged method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSessionIdChanged() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new TestSessionIdChangedListener());
        webApplication.addServlet("sessionIdChangedServlet",
                new TestSessionIdChangedServlet());
        webApplication.addServletMapping("sessionIdChangedServlet", "/sessionIdChanged");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/sessionIdChanged");
        request.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("originalSessionId"));
        assertNotNull(webApplication.getAttribute("oldSessionId"));
        assertNotNull(webApplication.getAttribute("newSessionId"));
        assertEquals(webApplication.getAttribute("originalSessionId"),
                webApplication.getAttribute("oldSessionId"));
        assertNotEquals(webApplication.getAttribute("oldSessionId"),
                webApplication.getAttribute("newSessionId"));
        webApplication.stop();
    }

    /**
     * Test setAttribute method.
     */
    @Test
    void testSetAttribute() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
        session.setAttribute("TEST", "TEST");
        session.setAttribute("TEST", null);
        assertNull(session.getAttribute("TEST"));
    }

    /**
     * Test setComment.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetComment() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.setUnderlyingOutputStream(byteOutput);
        webApplication.addServlet("TestSetCommentServlet", new TestSetCommentServlet());
        webApplication.addServletMapping("TestSetCommnetrServlet", "/*");
        webApplication.addListener(new TestSetCommentListener());
        webApplication.initialize();
        webApplication.start();
        try {
            webApplication.service(request, response);
        } catch (ServletException se) {
            fail();
        }
        webApplication.stop();
    }

    /**
     * Test setComment.
     */
    @Test
    void testSetComment2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertNotNull(assertThrows(IllegalStateException.class, 
                () -> webApplication.getManager().getHttpSessionManager()
                        .getSessionCookieConfig().setComment("MY COMMENT")));
        webApplication.stop();
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
     * Test setSessionTimeout method.
     */
    @Test
    void testSetSessionTimeout() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSessionTimeout(50);
        assertEquals(50, webApp.getSessionTimeout());
    }

    /**
     * Test setSessionTimeout method
     */
    @Test
    void testSessionTimeout2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> webApp.setSessionTimeout(50)));
    }

    /**
     * Test setSessionTrackingModes method.
     */
    @Test
    void testSetSessionTrackingModes() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();

        EnumSet<SessionTrackingMode> sslAndUrl = EnumSet.of(SSL, URL);
        assertNotNull(assertThrows(IllegalArgumentException.class,
                () -> sessionManager.setSessionTrackingModes(sslAndUrl)));

        EnumSet<SessionTrackingMode> sslAndCookie = EnumSet.of(COOKIE, SSL);
        assertNotNull(assertThrows(IllegalArgumentException.class,
                () -> sessionManager.setSessionTrackingModes(sslAndCookie)));
    }

    /**
     * Test HttpSessionAttributeListener to validate attributeAdded was properly
     * called.
     */
    public static class TestAttributeAddedListener
            implements HttpSessionAttributeListener {

        /**
         * Handle attribute added event.
         *
         * @param event the event.
         */
        @Override
        public void attributeAdded(HttpSessionBindingEvent event) {
            if (event.getName().equals("attributeAdded")) {
                event.getSession().getServletContext().setAttribute("attributeAdded", true);
            }
        }

        /**
         * Handle attribute removed event.
         *
         * @param event the event.
         */
        @Override
        public void attributeRemoved(HttpSessionBindingEvent event) {
        }

        /**
         * Handle attribute replaced event.
         *
         * @param event the event.
         */
        @Override
        public void attributeReplaced(HttpSessionBindingEvent event) {
        }
    }

    /**
     * Test HttpServlet to validate the HTTP session attribute was added.
     */
    public static class TestAttributeAddedServlet extends HttpServlet {

        /**
         * Process GET method.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            HttpSession session = request.getSession();
            session.setAttribute("attributeAdded", true);
        }
    }

    /**
     * Test HttpSessionAttributeListener to validate attributeRemoved was
     * properly called.
     */
    public static class TestAttributeRemovedListener
            implements HttpSessionAttributeListener {

        /**
         * Handle attribute added event.
         *
         * @param event the event.
         */
        @Override
        public void attributeAdded(HttpSessionBindingEvent event) {
        }

        /**
         * Handle attribute removed event.
         *
         * @param event the event.
         */
        @Override
        public void attributeRemoved(HttpSessionBindingEvent event) {
            if (event.getName().equals("attributeRemoved")) {
                event.getSession().getServletContext().setAttribute("attributeRemoved", true);
            }
        }
    }

    /**
     * Test HttpServlet to validate the HTTP session attribute was removed.
     */
    public static class TestAttributeRemovedServlet extends HttpServlet {

        /**
         * Process GET method.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            HttpSession session = request.getSession();
            session.setAttribute("attributeRemoved", true);
            session.removeAttribute("attributeRemoved");
        }
    }

    /**
     * Test HttpSessionAttributeListener to validate attributeReplaced was
     * properly called.
     */
    public static class TestAttributeReplacedListener
            implements HttpSessionAttributeListener {

        /**
         * Handle attribute added event.
         *
         * @param event the event.
         */
        @Override
        public void attributeAdded(HttpSessionBindingEvent event) {
        }

        /**
         * Handle attribute removed event.
         *
         * @param event the event.
         */
        @Override
        public void attributeRemoved(HttpSessionBindingEvent event) {
        }

        /**
         * Handle attribute replaced event.
         *
         * @param event the event.
         */
        @Override
        public void attributeReplaced(HttpSessionBindingEvent event) {
            if (event.getName().equals("attributeReplaced")) {
                event.getSession().getServletContext().setAttribute("attributeReplaced", true);
            }
        }
    }

    /**
     * Test HttpServlet to validate the HTTP session attribute was replaced.
     */
    public static class TestAttributeReplacedServlet extends HttpServlet {

        /**
         * Process GET method.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            HttpSession session = request.getSession();
            session.setAttribute("attributeReplaced", false);
            session.setAttribute("attributeReplaced", true);
        }
    }

    /**
     * Test HttpSessionListener to validate createListener was called.
     */
    public static class TestCreateListenerHttpSessionListener implements HttpSessionListener {

        /**
         * Constructor.
         */
        public TestCreateListenerHttpSessionListener() {
        }
    }

    /**
     * Test HttpServlet to validate getSession works.
     */
    public static class TestGetSession3Servlet extends HttpServlet {

        /**
         * Processes the request.
         *
         * @param request the servlet request.
         * @param response the servlet response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a servlet error occurs.
         */
        protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setContentType("text/plain");
            try (PrintWriter out = response.getWriter()) {
                if (request.isRequestedSessionIdValid()) {
                    HttpSession session = request.getSession(false);
                    out.println("Session is " + session);
                    if (session == null) {
                        session = request.getSession();
                        out.println("Session is " + session);
                    }
                } else {
                    HttpSession session = request.getSession();
                    out.println("Session is " + session + ", from request");
                }
            }
        }

        /**
         * Handles the GET request.
         *
         * @param request the servlet request.
         * @param response the servlet response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            processRequest(request, response);
        }

        /**
         * Handles the POST request.
         *
         * @param request the servlet request.
         * @param response the servlet response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a servlet error occurs.
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            processRequest(request, response);
        }
    }

    /**
     * Test HttpSessionListener to validate sessionCreated was properly called.
     */
    public static class TestSessionCreatedListener implements HttpSessionListener {

        /**
         * Handle the session created event.
         *
         * @param event the event.
         */
        @Override
        public void sessionCreated(HttpSessionEvent event) {
            event.getSession().getServletContext().setAttribute("sessionCreated", true);
        }

        /**
         * Handle the session destroyed event.
         *
         * @param event the event.
         */
        @Override
        public void sessionDestroyed(HttpSessionEvent event) {
        }
    }

    /**
     * Test HttpServlet to validate the session was actually created.
     */
    public static class TestSessionCreatedServlet extends HttpServlet {

        private static final long serialVersionUID = 1L;

        /**
         * Process GET method.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            request.getServletContext().setAttribute("session", request.getSession());
        }
    }

    /**
     * Test HttpSessionListener to validate sessionDestroyed was properly
     * called.
     */
    public static class TestSessionDestroyedListener implements HttpSessionListener {

        /**
         * Handle the session created event.
         *
         * @param event the event.
         */
        @Override
        public void sessionCreated(HttpSessionEvent event) {
        }

        /**
         * Handle the session destroyed event.
         *
         * @param event the event.
         */
        @Override
        public void sessionDestroyed(HttpSessionEvent event) {
            event.getSession().getServletContext().setAttribute("sessionDestroyed", true);
        }
    }

    /**
     * Test HttpServlet to validate the session was actually destroyed.
     */
    public static class TestSessionDestroyedServlet extends HttpServlet {

        private static final long serialVersionUID = 1L;

        /**
         * Process GET method.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            HttpSession session = request.getSession();
            session.invalidate();
        }
    }

    /**
     * Test HttpSessionIdListener to validate sessionIdChanged is properly
     * called.
     */
    public static class TestSessionIdChangedListener implements HttpSessionIdListener {

        /**
         * Handle the session id changed event.
         *
         * @param event the event.
         * @param oldSessionId the old session id.
         */
        @Override
        public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
            HttpSession session = event.getSession();
            session.getServletContext().setAttribute("newSessionId", session.getId());
            session.getServletContext().setAttribute("oldSessionId", oldSessionId);
        }
    }

    /**
     * Test HttpServlet to validate the session was actually changed.
     */
    public static class TestSessionIdChangedServlet extends HttpServlet {

        /**
         * Process GET method.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            request.getServletContext().setAttribute("originalSessionId",
                    request.getSession().getId());
            request.changeSessionId();
        }
    }

    /**
     * Test ServletContextListener that sets the comment of the session cookie
     * config.
     */
    public static class TestSetCommentListener implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent event) {
            event.getServletContext().getSessionCookieConfig().setComment("MY COMMENT");
        }
    }

    /**
     * Test HttpServlet to validate the servlet context listener can change
     * session cookie config values.
     */
    public static class TestSetCommentServlet extends HttpServlet {

        @Override
        protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {

            if (!request.getServletContext().getSessionCookieConfig()
                    .getComment().equals("MY COMMENT")) {
                throw new ServletException("ServletContextListener did not work");
            }
        }
    }
}
