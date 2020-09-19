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
package cloud.piranha.webapp.impl;

import javax.servlet.http.HttpSession;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultHttpSessionManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultHttpSessionManagerTest {

    /**
     * Test createSession method.
     */
    @Test
    void testCreateSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertThrows(NullPointerException.class, () -> sessionManager.createSession(webApp, null));
    }

    /**
     * Test getComment method.
     */
    @Test
    void testGetComment() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setComment("COMMENT");
        assertEquals("COMMENT", sessionManager.getComment());
    }

    /**
     * Test getDomain method.
     */
    @Test
    void testGetDomain() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setDomain("domain");
        assertEquals("domain", sessionManager.getDomain());
    }

    /**
     * Test getMaxAge method.
     */
    @Test
    void testGetMaxAge() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals(0, sessionManager.getMaxAge());
        sessionManager.setMaxAge(60);
        assertEquals(60, sessionManager.getMaxAge());
    }

    /**
     * Test getName method.
     */
    @Test
    void testGetName() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setName("JSESSIONID");
        assertEquals("JSESSIONID", sessionManager.getName());
    }

    /**
     * Test getPath method.
     */
    @Test
    void testGetPath() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setPath("/");
        assertEquals("/", sessionManager.getPath());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertThrows(NullPointerException.class, () -> sessionManager.getSession(webApp, null, null));
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession2() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = sessionManager.createSession(webApp, request);
        request.setRequestedSessionId(session.getId());
        assertNotNull(sessionManager.getSession(webApp, request, session.getId()));
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
     * Test getSessionTimeout method.
     */
    @Test
    void testGetSessionTimeout() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals(10, sessionManager.getSessionTimeout());
        sessionManager.setSessionTimeout(5);
        assertEquals(5, sessionManager.getSessionTimeout());
    }

    /**
     * Test isHttpOnly method.
     */
    @Test
    void testIsHttpOnly() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertFalse(sessionManager.isHttpOnly());
        sessionManager.setHttpOnly(true);
        assertTrue(sessionManager.isHttpOnly());
    }

    /**
     * Test isSecure method.
     */
    @Test
    void testIsSecure() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertFalse(sessionManager.isSecure());
        sessionManager.setSecure(true);
        assertTrue(sessionManager.isSecure());
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

    @Test
    void testEffectiveSessionTracking() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals(sessionManager.getDefaultSessionTrackingModes(), sessionManager.getEffectiveSessionTrackingModes());
    }
}
