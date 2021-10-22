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
package cloud.piranha.webapp.impl.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.webapp.impl.DefaultHttpSessionManager;
import cloud.piranha.webapp.impl.DefaultWebApplication;

/**
 * The JUnit tests for the HttpSessionAttributeListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpSessionAttributeListenerTest {

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
     * Test attributeAdded method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeAdded() throws Exception {
        webApplication.addListener(new TestHttpSessionAttributeListener());
        webApplication.addServlet("httpSessionAttributeServlet",
                new TestHttpSessionAttributeServlet());
        webApplication.addServletMapping("httpSessionAttributeServlet",
                "/httpSessionAttribute");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/httpSessionAttribute");
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
        webApplication.addListener(new TestHttpSessionAttributeListener());
        webApplication.addServlet("httpSessionAttributeServlet",
                new TestHttpSessionAttributeServlet());
        webApplication.addServletMapping("httpSessionAttributeServlet",
                "/httpSessionAttribute");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/httpSessionAttribute");
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
        webApplication.addListener(new TestHttpSessionAttributeListener());
        webApplication.addServlet("httpSessionAttributeServlet",
                new TestHttpSessionAttributeServlet());
        webApplication.addServletMapping("httpSessionAttributeServlet",
                "/httpSessionAttribute");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setServletPath("/httpSessionAttribute");
        request.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("attributeReplaced"));
        webApplication.stop();
    }

    /**
     * Test HttpServlet to validate the HTTP session attributes where added,
     * remove and replaced.
     */
    class TestHttpSessionAttributeServlet extends HttpServlet {

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
            session.setAttribute("attributeRemoved", true);
            session.removeAttribute("attributeRemoved");
            session.setAttribute("attributeReplaced", false);
            session.setAttribute("attributeReplaced", true);
        }
    }

    /**
     * Test HttpSessionAttributeListener to validate attributeAdded,
     * attributeRemoved and attributeReplaced are properly called.
     */
    class TestHttpSessionAttributeListener
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
            if (event.getName().equals("attributeRemoved")) {
                event.getSession().getServletContext().setAttribute("attributeRemoved", true);
            }
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
}
