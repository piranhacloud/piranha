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
public class DefaultHttpSessionManagerTest {

    /**
     * Test createSession method.
     */
    @Test
    public void testCreateSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertThrows(NullPointerException.class, () -> sessionManager.createSession(webApp, null));
    }

    /**
     * Test getComment method.
     */
    @Test
    public void testGetComment() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setComment("COMMENT");
        assertEquals(sessionManager.getComment(), "COMMENT");
    }

    /**
     * Test getDomain method.
     */
    @Test
    public void testGetDomain() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setDomain("domain");
        assertEquals(sessionManager.getDomain(), "domain");
    }

    /**
     * Test getMaxAge method.
     */
    @Test
    public void testGetMaxAge() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals(sessionManager.getMaxAge(), 0);
        sessionManager.setMaxAge(60);
        assertEquals(sessionManager.getMaxAge(), 60);
    }

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setName("JSESSIONID");
        assertEquals(sessionManager.getName(), "JSESSIONID");
    }

    /**
     * Test getPath method.
     */
    @Test
    public void testGetPath() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setPath("/");
        assertEquals(sessionManager.getPath(), "/");
    }

    /**
     * Test getSession method.
     */
    @Test
    public void testGetSession() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertThrows(NullPointerException.class, () -> sessionManager.getSession(webApp, null, null));
    }

    /**
     * Test getSession method.
     */
    @Test
    public void testGetSession2() {
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
    public void testGetSessionCookieConfig() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertNotNull(sessionManager.getSessionCookieConfig());
    }

    /**
     * Test getSessionTimeout method.
     */
    @Test
    public void testGetSessionTimeout() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals(sessionManager.getSessionTimeout(), 10);
        sessionManager.setSessionTimeout(5);
        assertEquals(sessionManager.getSessionTimeout(), 5);
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
        assertEquals(sessionManager.encodeRedirectURL(null, "test"), "test");
    }

    /**
     * Test encodeURL method.
     */
    @Test
    public void testEncodeURL() {
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        assertEquals(sessionManager.encodeURL(null, "test"), "test");
    }
}
