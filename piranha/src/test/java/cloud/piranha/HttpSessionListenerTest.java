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
package cloud.piranha;

import cloud.piranha.api.WebApplication;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * The JUnit tests for testing everything related to the HttpSessionListener
 * API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpSessionListenerTest {

    /**
     * Stores the web application.
     */
    protected WebApplication webApplication;
    
    /**
     * Stores the web application server.
     */
    protected DefaultWebApplicationServer webApplicationServer;

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @Before
    public void setUp() throws Exception {
        webApplicationServer = new DefaultWebApplicationServer();
        webApplication = new DefaultWebApplication();
        webApplication.setHttpSessionManager(new DefaultHttpSessionManager());
        webApplicationServer.addWebApplication(webApplication);
    }

    /**
     * Test sessionCreated method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testSessionCreated() throws Exception {
        webApplication.addListener(new TestHttpSessionListener());
        webApplication.addServlet("sessionCreatedServlet", new TestHttpSessionCreatedServlet());
        webApplication.addServletMapping("sessionCreatedServlet", "/sessionCreated");
        TestHttpServerResponse response = new TestHttpServerResponse();
        TestHttpServerRequest request = new TestHttpServerRequest();
        request.setRequestTarget("/sessionCreated");
        webApplicationServer.initialize();
        webApplicationServer.start();
        webApplicationServer.process(request, response);
        assertNotNull(webApplication.getAttribute("sessionCreated"));
        assertTrue(webApplication.getAttribute("session") instanceof HttpSession);
        webApplicationServer.stop();
    }

    /**
     * Test sessionDestroyed method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testSessionDestroyed() throws Exception {
        webApplication.addListener(new TestHttpSessionListener());
        webApplication.addServlet("sessionDestroyedServlet", new TestHttpSessionDestroyedServlet());
        webApplication.addServletMapping("sessionDestroyedServlet", "/sessionDestroyed");
        TestHttpServerResponse response = new TestHttpServerResponse();
        TestHttpServerRequest request = new TestHttpServerRequest();
        request.setRequestTarget("/sessionDestroyed");
        webApplicationServer.initialize();
        webApplicationServer.start();
        webApplicationServer.process(request, response);
        assertNotNull(webApplication.getAttribute("sessionDestroyed"));
        webApplication.stop();
        webApplication.destroy();
    }

    /**
     * Test HttpServlet to validate the session was actually created.
     */
    public class TestHttpSessionCreatedServlet extends HttpServlet {

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
    public class TestHttpSessionDestroyedServlet extends HttpServlet {

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
    public class TestHttpSessionListener implements HttpSessionListener {

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
