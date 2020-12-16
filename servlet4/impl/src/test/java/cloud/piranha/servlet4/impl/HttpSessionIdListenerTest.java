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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cloud.piranha.servlet4.webapp.WebApplication;

/**
 * The JUnit tests for testing everything related to the HttpSessionIdListener
 * API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpSessionIdListenerTest {

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
        DefaultHttpSessionManager httpSessionManager = new DefaultHttpSessionManager();
        httpSessionManager.setWebApplication(webApplication);
        webApplication.setHttpSessionManager(httpSessionManager);
    }

    /**
     * Test sessionIdChanged method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSessionIdChanged() throws Exception {
        webApplication.addListener(new TestHttpSessionIdListener());
        webApplication.addServlet("sessionIdChangedServlet",
                new TestHttpSessionIdChangedServlet());
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
     * Test HttpServlet to validate the session was actually created.
     */
    class TestHttpSessionIdChangedServlet extends HttpServlet {

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
     * Test HttpSessionIdListener to validate sessionIdChanged is properly
     * called.
     */
    class TestHttpSessionIdListener implements HttpSessionIdListener {

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
}
