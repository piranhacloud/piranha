/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl;

import jakarta.servlet.SessionTrackingMode;
import static jakarta.servlet.SessionTrackingMode.COOKIE;
import static jakarta.servlet.SessionTrackingMode.SSL;
import static jakarta.servlet.SessionTrackingMode.URL;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
import java.util.EnumSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        assertNotNull(assertThrows(NullPointerException.class,
                () -> sessionManager.createSession(null)));
    }
    
    /**
     * Test destroySession method.
     */
    @Test
    void testDestroySession() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager manager = new DefaultHttpSessionManager();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(manager);
        session.setAttribute("name", "value");
        manager.addListener(new HttpSessionAttributeListener() {
            @Override
            public void attributeRemoved(HttpSessionBindingEvent event) {
                event.getSession().getServletContext().setAttribute("destroySession", true);
            }
        });
        manager.destroySession(session);
        assertTrue((boolean)webApplication.getAttribute("destroySession"));
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
     * Test getComment method.
     */
    @Test
    void testGetComment() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        sessionManager.setComment("COMMENT");
        assertNull(sessionManager.getComment());
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
        assertEquals(sessionManager.getDefaultSessionTrackingModes(),
                sessionManager.getEffectiveSessionTrackingModes());
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = sessionManager.createSession(request);
        request.setRequestedSessionId(session.getId());
        assertNotNull(sessionManager.getSession(request, session.getId()));
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        assertEquals(10, sessionManager.getSessionTimeout());
        sessionManager.setSessionTimeout(5);
        assertEquals(5, sessionManager.getSessionTimeout());
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
     * Test setComment.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetComment() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager manager = (DefaultHttpSessionManager) 
                webApplication.getManager().getHttpSessionManager();
        manager.setComment("Comment");
        assertNull(webApplication.getSessionCookieConfig().getComment());
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
                        .getSessionCookieConfig().setComment("Comment")));
        webApplication.stop();
    }
    /**
     * Test setDomain.
     * 
     * <p>
     *  Validate we are throwing an IllegalStateException once the 
     *  ServletContext has been initialized.
     * </p>
     */
    @Test
    void testSetDomain() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertNotNull(assertThrows(IllegalStateException.class, 
                () -> webApplication.getManager().getHttpSessionManager()
                        .getSessionCookieConfig().setDomain("Domain")));
        webApplication.stop();
    }

    /**
     * Test setHttpOnly.
     * 
     * <p>
     *  Validate we are throwing an IllegalStateException once the 
     *  ServletContext has been initialized.
     * </p>
     */
    @Test
    void testSetHttpOnly() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertNotNull(assertThrows(IllegalStateException.class, 
                () -> webApplication.getManager().getHttpSessionManager()
                        .getSessionCookieConfig().setHttpOnly(true)));
        webApplication.stop();
    }

    /**
     * Test setMaxAge.
     * 
     * <p>
     *  Validate we are throwing an IllegalStateException once the 
     *  ServletContext has been initialized.
     * </p>
     */
    @Test
    void testSetMaxAge() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertNotNull(assertThrows(IllegalStateException.class, 
                () -> webApplication.getManager().getHttpSessionManager()
                        .getSessionCookieConfig().setMaxAge(1234)));
        webApplication.stop();
    }

    /**
     * Test setName.
     * 
     * <p>
     *  Validate we are throwing an IllegalStateException once the 
     *  ServletContext has been initialized.
     * </p>
     */
    @Test
    void testSetName() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertNotNull(assertThrows(IllegalStateException.class, 
                () -> webApplication.getManager().getHttpSessionManager()
                        .getSessionCookieConfig().setName("Name")));
        webApplication.stop();
    }

    /**
     * Test setPath.
     * 
     * <p>
     *  Validate we are throwing an IllegalStateException once the 
     *  ServletContext has been initialized.
     * </p>
     */
    @Test
    void testSetPath() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertNotNull(assertThrows(IllegalStateException.class, 
                () -> webApplication.getManager().getHttpSessionManager()
                        .getSessionCookieConfig().setPath("/path")));
        webApplication.stop();
    }

    /**
     * Test setSecure.
     * 
     * <p>
     *  Validate we are throwing an IllegalStateException once the 
     *  ServletContext has been initialized.
     * </p>
     */
    @Test
    void testSetSecure() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertNotNull(assertThrows(IllegalStateException.class, 
                () -> webApplication.getManager().getHttpSessionManager()
                        .getSessionCookieConfig().setSecure(true)));
        webApplication.stop();
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
}
