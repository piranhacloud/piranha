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

import cloud.piranha.servlet4.impl.DefaultHttpSessionManager;
import cloud.piranha.servlet4.impl.DefaultWebApplication;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cloud.piranha.servlet4.webapp.WebApplication;

/**
 * The JUnit tests for the HttpSessionListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpSessionListenerTest {

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @BeforeEach
    void setUp() throws Exception {
        webApplication = new DefaultWebApplication();
        DefaultHttpSessionManager sessionManager = new DefaultHttpSessionManager();
        sessionManager.setWebApplication(webApplication);
        webApplication.setHttpSessionManager(sessionManager);
    }

    /**
     * Test sessionCreated method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSessionCreated() throws Exception {
        webApplication.addListener(new TestHttpSessionListener());
        webApplication.addServlet("sessionCreatedServlet", new TestHttpSessionCreatedServlet());
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
        webApplication.addListener(new TestHttpSessionListener());
        webApplication.addServlet("sessionDestroyedServlet", new TestHttpSessionDestroyedServlet());
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
     * Test HttpServlet to validate the session was actually created.
     */
    class TestHttpSessionCreatedServlet extends HttpServlet {

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
     * Test HttpServlet to validate the session was actually destroyed.
     */
    class TestHttpSessionDestroyedServlet extends HttpServlet {

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
     * Test HttpSessionListener to validate sessionCreated and sessionDestroyed
     * are properly called.
     */
    class TestHttpSessionListener implements HttpSessionListener {

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
            event.getSession().getServletContext().setAttribute("sessionDestroyed", true);
        }
    }
}
