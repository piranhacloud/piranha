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
package cloud.piranha.core.impl;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;
import static jakarta.servlet.SessionTrackingMode.COOKIE;
import static jakarta.servlet.SessionTrackingMode.SSL;
import static jakarta.servlet.SessionTrackingMode.URL;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the HttpSession API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultHttpSessionTest {

    /**
     * Test getCookies method.
     */
    /*

      REVIEW FOR SERVLET 6

    @Test
    void testGetCookies() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApp);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);

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
    */

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
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetRequestedSessionIdFromCookie() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromCookie());
        request.setRequestedSessionIdFromCookie(true);
        assertTrue(request.isRequestedSessionIdFromCookie());
        request.close();
    }

    /**
     * Test setRequestedSessionIdFromURL method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetRequestedSessionIdFromURL() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromURL());
        request.setRequestedSessionIdFromURL(true);
        assertTrue(request.isRequestedSessionIdFromURL());
        request.close();
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
