/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import javax.servlet.http.HttpSession;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultHttpSessionManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpSessionManagerTest {

    /**
     * Test createSession method.
     */
    @Test(expected = NullPointerException.class)
    public void testCreateSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        sessionManager.createSession(webApp, null);
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setComment("COMMENT");
        assertEquals("COMMENT", sessionManager.getComment());
    }

    /**
     * Test getDomain method.
     */
    @Test
    public void testGetDomain() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setDomain("domain");
        assertEquals("domain", sessionManager.getDomain());
    }

    /**
     * Test getMaxAge method.
     */
    @Test
    public void testGetMaxAge() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals(0, sessionManager.getMaxAge());
        sessionManager.setMaxAge(60);
        assertEquals(60, sessionManager.getMaxAge());
    }

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setName("JSESSIONID");
        assertEquals("JSESSIONID", sessionManager.getName());
    }

    /**
     * Test getPath method.
     */
    @Test
    public void testGetPath() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setPath("/");
        assertEquals("/", sessionManager.getPath());
    }

    /**
     * Test getSession method.
     */
    @Test(expected = NullPointerException.class)
    public void testGetSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        sessionManager.getSession(webApp, null, null);
    }

    /**
     * Test getSession method.
     */
    @Test
    public void testGetSession2() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        TestHttpServletResponse response = new TestHttpServletResponse();
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = sessionManager.createSession(webApp, request);
        request.setRequestedSessionId(session.getId());
        assertNotNull(sessionManager.getSession(webApp, request, session.getId()));
    }

    /**
     * Test getSessionCookieConfig method.
     */
    @Test
    public void testGetSessionCookieConfig() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertNotNull(sessionManager.getSessionCookieConfig());
    }

    /**
     * Test isHttpOnly method.
     */
    @Test
    public void testIsHttpOnly() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertFalse(sessionManager.isHttpOnly());
        sessionManager.setHttpOnly(true);
        assertTrue(sessionManager.isHttpOnly());
    }

    /**
     * Test isSecure method.
     */
    @Test
    public void testIsSecure() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertFalse(sessionManager.isSecure());
        sessionManager.setSecure(true);
        assertTrue(sessionManager.isSecure());
    }

    /**
     * Test encodeRedirectUrl method.
     */
    @Test
    public void testEncodeRedirectURL() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals("test", sessionManager.encodeRedirectURL(null, "test"));
    }

    /**
     * Test encodeURL method.
     */
    @Test
    public void testEncodeURL() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals("test", sessionManager.encodeURL(null, "test"));
    }
}
