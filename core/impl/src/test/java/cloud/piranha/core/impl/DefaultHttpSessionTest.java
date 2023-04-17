/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultHttpSession class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultHttpSessionTest {

    /**
     * Test attributeAdded method.
     *
     * <p>
     * Validate that an object implementing the HttpSessionBindingListener
     * interface gets notified when the value gets bound into the session.
     * </p>
     */
    @Test
    void testAttributeAdded() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager manager = new DefaultHttpSessionManager();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(manager);
        manager.attributeAdded(session, "name", new HttpSessionBindingListener() {
            @Override
            public void valueBound(HttpSessionBindingEvent event) {
                event.getSession().getServletContext().setAttribute("testAttributeAdded", true);
            }
        });
        assertTrue((boolean) webApplication.getAttribute("testAttributeAdded"));
    }

    /**
     * Test attributeRemoved method.
     *
     * <p>
     * Validate that an object implementing the HttpSessionBindingListener
     * interface gets notified when the value gets unbound from the session.
     * </p>
     */
    @Test
    void testAttributeRemoved() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager manager = new DefaultHttpSessionManager();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(manager);
        manager.attributeRemoved(session, "name", new HttpSessionBindingListener() {
            @Override
            public void valueUnbound(HttpSessionBindingEvent event) {
                event.getSession().getServletContext().setAttribute("testAttributeRemoved", true);
            }
        });
        assertTrue((boolean) webApplication.getAttribute("testAttributeRemoved"));
    }

    /**
     * Test attributeReplaced method.
     *
     * <p>
     * Validate that an object implementing the HttpSessionBindingListener
     * interface gets notified when the old value gets unbound.
     * </p>
     * <p>
     * Validate that an object implementing the HttpSessionBindingListener
     * interface gets notified when the new value gets bound.
     * </p>
     */
    @Test
    void testAttributeReplaced() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager manager = new DefaultHttpSessionManager();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(manager);
        Object value = new HttpSessionBindingListener() {
            @Override
            public void valueBound(HttpSessionBindingEvent event) {
                event.getSession().getServletContext().setAttribute("testAttributeReplacedA", true);
            }
            @Override
            public void valueUnbound(HttpSessionBindingEvent event) {
                event.getSession().getServletContext().setAttribute("testAttributeReplacedB", true);
            }
        };
        manager.attributeReplaced(session, "name", value, value);
        assertTrue((boolean) webApplication.getAttribute("testAttributeReplacedA"));
        assertTrue((boolean) webApplication.getAttribute("testAttributeReplacedB"));
    }
    
    /**
     * Test getAttribute method.
     */
    @Test
    void testGetAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        session.setAttribute("TestGetAttribute", "TestGetAttribute");
        assertEquals("TestGetAttribute", session.getAttribute("TestGetAttribute"));
        session.removeAttribute("TestGetAttribute");
        assertNull(session.getAttribute("TestGetAttribute"));
    }

    /**
     * Test getAttributeNames method.
     */
    @Test
    void testGetAttributeNames() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        assertFalse(session.getAttributeNames().hasMoreElements());
    }

    /**
     * Test getCookies method.
     */
    @Test
    void testGetCookies() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApplication.linkRequestAndResponse(request, response);
        sessionManager.setComment("Comment");
        sessionManager.setDomain("domain");
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        assertTrue(session.getCreationTime() > 0);
    }

    /**
     * Test getId method.
     */
    @Test
    void testGetId() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        assertNotNull(session.getId());
    }

    /**
     * Test getLastAccessedTime method.
     */
    @Test
    void testGetLastAccessedTime() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        assertTrue(session.getLastAccessedTime() >= session.getCreationTime());
    }

    /**
     * Test getMaxInactiveInterval method.
     */
    @Test
    void testGetMaxInactiveInterval() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        session.setMaxInactiveInterval(1000);
        assertEquals(1000, session.getMaxInactiveInterval());
    }

    /**
     * Test getServletContext method.
     */
    @Test
    void testGetServletContext() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        assertNotNull(session.getServletContext());
        assertEquals(webApplication, session.getServletContext());
    }

    /**
     * Test invalidate method.
     */
    @Test
    void testInvalidate() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        session.invalidate();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> session.setAttribute("TEST", "TEST")));
    }

    /**
     * Test isNew method.
     */
    @Test
    void testIsNew() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        session.setNew(true);
        assertTrue(session.isNew());
        session.setNew(false);
        assertFalse(session.isNew());
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    void testRemoveAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        session.setAttribute("TestRemoveAttribute", "TestRemoveAttribute");
        assertEquals("TestRemoveAttribute", session.getAttribute("TestRemoveAttribute"));
        session.removeAttribute("TestRemoveAttribute");
        assertNull(session.getAttribute("TestRemoveAttribute"));
    }

    /**
     * Test setAttribute method.
     */
    @Test
    void testSetAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        session.setAttribute("TestSetAttribute", "TestSetAttribute");
        assertEquals("TestSetAttribute", session.getAttribute("TestSetAttribute"));
    }

    /**
     * Test setAttribute method.
     */
    @Test
    void testSetAttribute2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        session.setAttribute("TestSetAttribute", "TestSetAttribute");
        session.setAttribute("TestSetAttribute", null);
        assertNull(session.getAttribute("TestSetAttribute"));
    }

    /**
     * Test setMaxInactiveInterval method.
     */
    @Test
    void testSetMaxInactiveInterval() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApplication);
        session.setSessionManager(webApplication.getManager().getHttpSessionManager());
        session.setMaxInactiveInterval(15);
        assertEquals(15, session.getMaxInactiveInterval());
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
