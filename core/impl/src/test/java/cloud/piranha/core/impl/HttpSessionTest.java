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

import cloud.piranha.core.api.WebApplication;
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
import jakarta.servlet.http.HttpSessionBindingListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionIdListener;
import jakarta.servlet.http.HttpSessionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumSet;
import java.util.HashSet;
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
 * The JUnit tests for the HttpSession related APIs.
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
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestAttributeAddedServlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        request.getSession().setAttribute("name", "value");
                    }
                })
                .servletMapping("TestAttributeAddedServlet", "/testAttributeAdded")
                .build();
        webApplication.addListener(new HttpSessionAttributeListener() {
            @Override
            public void attributeAdded(HttpSessionBindingEvent event) {
                event.getSession().getServletContext()
                        .setAttribute("testAttributeAdded", true);
            }
        });
        webApplication.initialize();
        webApplication.start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .servletPath("/testAttributeAdded")
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testAttributeAdded"));
    }

    /**
     * Test attributeRemoved method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeRemoved() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestAttributeRemovedServlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        request.getSession().setAttribute("name", "value");
                        request.getSession().removeAttribute("name");
                    }
                })
                .servletMapping("TestAttributeRemovedServlet", "/testAttributeRemoved")
                .build();
        webApplication.addListener(new HttpSessionAttributeListener() {
            @Override
            public void attributeRemoved(HttpSessionBindingEvent event) {
                event.getSession().getServletContext()
                        .setAttribute("testAttributeRemoved", true);
            }
        });
        webApplication.initialize();
        webApplication.start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .servletPath("/testAttributeRemoved")
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testAttributeRemoved"));
    }

    /**
     * Test attributeReplaced method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeReplaced() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestAttributeReplacedServlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        request.getSession().setAttribute("name", "value");
                        request.getSession().setAttribute("name", "value2");
                    }
                })
                .servletMapping("TestAttributeReplacedServlet",
                        "/testAttributeReplaced")
                .build();
        webApplication.addListener(new HttpSessionAttributeListener() {
            @Override
            public void attributeReplaced(HttpSessionBindingEvent event) {
                event.getSession().getServletContext()
                        .setAttribute("testAttributeReplaced", true);
            }
        });
        webApplication.initialize();
        webApplication.start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .servletPath("/testAttributeReplaced")
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testAttributeReplaced"));
    }

    /**
     * Test sessionCreated method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSessionCreated() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestSessionCreatedServlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        request.getSession();
                    }
                })
                .servletMapping("TestSessionCreatedServlet", "/testSessionCreatedServlet")
                .build();
        webApplication.addListener(new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                event.getSession().getServletContext().setAttribute("testSessionCreated", true);
            }
        });
        webApplication.initialize();
        webApplication.start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .servletPath("/testSessionCreatedServlet")
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testSessionCreated"));
    }

    /**
     * Test sessionCreated method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSessionDestroyed() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestSessionDestroyedServlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        request.getSession().invalidate();
                    }
                })
                .servletMapping("TestSessionDestroyedServlet", "/testSessionDestroyedServlet")
                .build();
        webApplication.addListener(new HttpSessionListener() {
            @Override
            public void sessionDestroyed(HttpSessionEvent event) {
                event.getSession().getServletContext().setAttribute("testSessionDestroyed", true);
            }
        });
        webApplication.initialize();
        webApplication.start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .servletPath("/testSessionDestroyedServlet")
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testSessionDestroyed"));
    }

    /**
     * Test sessionIdChanged method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSessionIdChanged() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestSessionIdChangedServlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        request.getSession();
                        request.changeSessionId();
                    }
                })
                .servletMapping("TestSessionIdChangedServlet", "/testSessionIdChangedServlet")
                .build();
        webApplication.addListener(new HttpSessionIdListener() {
            @Override
            public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
                event.getSession().getServletContext().setAttribute("testSessionIdChanged", true);
            }
        });
        webApplication.initialize();
        webApplication.start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .servletPath("/testSessionIdChangedServlet")
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testSessionIdChanged"));
    }

    /**
     * Test valueBound method.
     *
     * <p>
     * Validate that an object implementing the HttpSessionBindingListener
     * interface gets notified when the value gets bound into the session.
     * </p>
     */
    @Test
    void testValueBound() {
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
     * Test valueUnbound method.
     *
     * <p>
     * Validate that an object implementing the HttpSessionBindingListener
     * interface gets notified when the value gets unbound from the session.
     * </p>
     */
    @Test
    void testValueUnbound() {
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
     * Test valueBound and valueUnbound method.
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
    void testValueBoundUnbound() {
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
    @SuppressWarnings({"deprecation", "removal"})
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

        @SuppressWarnings({"deprecation", "removal"})
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

        @SuppressWarnings({"deprecation", "removal"})
        @Override
        protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {
            if (!request.getServletContext().getSessionCookieConfig()
                    .getComment().equals("MY COMMENT")) {
                throw new ServletException("ServletContextListener did not work");
            }
        }
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
        assertTrue((boolean) webApplication.getAttribute("destroySession"));
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
    @SuppressWarnings({"deprecation", "removal"})
    @Test
    void testSetComment() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager manager = (DefaultHttpSessionManager) webApplication.getManager().getHttpSessionManager();
        manager.setComment("Comment");
        assertNull(webApplication.getSessionCookieConfig().getComment());
    }

    /**
     * Test setComment.
     */
    @SuppressWarnings({"deprecation", "removal"})
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
     * Validate we are throwing an IllegalStateException once the ServletContext
     * has been initialized.
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
     * Validate we are throwing an IllegalStateException once the ServletContext
     * has been initialized.
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
     * Validate we are throwing an IllegalStateException once the ServletContext
     * has been initialized.
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
     * Validate we are throwing an IllegalStateException once the ServletContext
     * has been initialized.
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
     * Validate we are throwing an IllegalStateException once the ServletContext
     * has been initialized.
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
     * Validate we are throwing an IllegalStateException once the ServletContext
     * has been initialized.
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

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
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
    void testChangeSessionId2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(assertThrows(IllegalStateException.class,
                request::changeSessionId));
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        String previousSessionId = session.getId();
        String newSessionId = request.changeSessionId();
        assertNotEquals(previousSessionId, newSessionId);
        assertEquals(newSessionId, request.getSession(false).getId());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession3() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        assertNotNull(session.getId());
        assertTrue(session.isNew());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(false);
        assertNull(session);
    }

    // --------------------- TODO finish alphabetizng -------------------------
    /**
     * Test createListener method to create a HttpSessionListener.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testCreateListenerWithHttpSessionListener() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .build();

        assertNotNull(webApplication.createListener(
                TestCreateListenerWithHttpSessionListener.class));
    }

    /**
     * Test getSessionCookieConfig method is not null.
     */
    @Test
    void testGetSessionCookieConfigIsNotNull() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .build();

        assertNotNull(webApplication.getSessionCookieConfig());
    }

    /**
     * Test getSessionTimeout method to see if it returns a value > 0.
     */
    @Test
    void testGetSessionTimeoutToSeeIfValueIsGreaterThanZero() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .build();

        assertTrue(webApplication.getSessionTimeout() > 0);
    }

    /**
     * Test getSession method when passing in false returns null.
     */
    @Test
    void testGetSessionWhenPassingInFalseReturnsNull() {
        DefaultWebApplication webApplication = new DefaultWebApplicationBuilder()
                .build();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(false);
        assertNull(session);
    }

    /**
     * Test getSession method when passing in true and then passing in false
     * returns not null on the last call.
     */
    @Test
    void testGetSessionWhenPassingInTrueAndFalseReturnsNotNull() {
        DefaultWebApplication webApplication = new DefaultWebApplicationBuilder()
                .build();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession(false));
    }

    /**
     * Test getSession method when passing in true returns not null.
     */
    @Test
    void testGetSessionWhenPassingInTrueReturnNotNull() {
        DefaultWebApplication webApplication = new DefaultWebApplicationBuilder()
                .build();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession());
    }

    /**
     * Test getSession without parameters creates a new session.
     *
     * @throws Exception
     */
    @Test
    void testGetSessionWithoutParameters() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplicationBuilder()
                .build();
        ServletRegistration.Dynamic dynamic = webApplication.addServlet("session",
                new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
                response.setContentType("text/plain");
                try (PrintWriter out = response.getWriter()) {
                    if (request.isRequestedSessionIdValid()) {
                        out.println("FAILED as session is already active");
                    } else {
                        HttpSession session = request.getSession();
                        out.println("New session is " + session);
                    }
                }
            }
        });
        assertNotNull(dynamic);
        dynamic.addMapping("/session");
        webApplication.initialize();
        webApplication.start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .servletPath("/session")
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        webApplication.service(request, response);
        assertTrue(byteOutput.toString().contains("New session is"));
    }

    /**
     * Test isRequestedSessionIdFromCookie method to validate it returns false
     * by default.
     */
    @Test
    void testIsRequestedSessionIdFromCookie() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .build();

        assertFalse(request.isRequestedSessionIdFromCookie());
    }

    /**
     * Test isRequestedSessionIdFromURL method to validate it returns false by
     * default.
     */
    @Test
    void testIsRequestedSessionIdFromURLReturnsFalseByDefault() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .build();

        assertFalse(request.isRequestedSessionIdFromURL());
    }

    /**
     * Test isRequestedSessionIdValid method to return false by default.
     */
    @Test
    void testIsRequestedSessionIdValidReturnsFalseByDefault() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .build();

        assertFalse(request.isRequestedSessionIdValid());
    }

    /**
     * Test isRequestedSessionIdValid method returns true when requested session
     * id has been set by a call to setRequestedSessionId.
     */
    @Test
    void testIsRequestedSessionIdValidReturnsTrue() {
        DefaultWebApplication webApplication = new DefaultWebApplicationBuilder()
                .build();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertTrue(request.isRequestedSessionIdValid());
    }

    /**
     * Test setRequestedSessionIdFromCookie method by passing in true.
     */
    @Test
    void testSetRequestedSessionIdFromCookieToTrue() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .build();

        assertFalse(request.isRequestedSessionIdFromCookie());
        request.setRequestedSessionIdFromCookie(true);
        assertTrue(request.isRequestedSessionIdFromCookie());
    }

    /**
     * Test setRequestedSessionIdFromURL method by passing in true.
     */
    @Test
    void testSetRequestedSessionIdFromURLToTrue() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .build();

        assertFalse(request.isRequestedSessionIdFromURL());
        request.setRequestedSessionIdFromURL(true);
        assertTrue(request.isRequestedSessionIdFromURL());
    }

    /**
     * Test setSessionTimeout method throws an IllegalStateException after the
     * web application has been started.
     */
    @Test
    void testSetSessionTimeoutThrowsIllegalStateExceptionAfterStart() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .build()
                .initialize()
                .start();

        assertNotNull(assertThrows(IllegalStateException.class,
                () -> webApplication.setSessionTimeout(50)));
    }

    /**
     * Test setSessionTimeout method with multiple timeouts.
     */
    @Test
    void testSetSessionTimeoutWithMultipleTimeouts() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .build();

        webApplication.setSessionTimeout(5);
        assertEquals(5, webApplication.getSessionTimeout());
        webApplication.setSessionTimeout(10);
        assertEquals(10, webApplication.getSessionTimeout());
    }

    /**
     * Test setSessionTrackingModes method with an empty HashSet.
     */
    @Test
    void testSetSessionTrackingModesWithEmptyHashSet() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .sessionTrackingModes(new HashSet())
                .build();

        assertTrue(webApplication.getEffectiveSessionTrackingModes().isEmpty());
    }

    /**
     * Test HttpSessionListener to validate createListener was called.
     */
    public static class TestCreateListenerWithHttpSessionListener implements HttpSessionListener {
    }
}
