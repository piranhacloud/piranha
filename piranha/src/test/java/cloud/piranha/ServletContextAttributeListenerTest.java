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
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * The JUnit tests for testing everything related to the
 * ServletContextAttributeListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletContextAttributeListenerTest {

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
     * Test attributeAdded method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAttributeAdded() throws Exception {
        webApplication.addListener(new TestServletContextAttributeListener());
        webApplication.addServlet("servletContextAttributeServlet",
                new TestServletContextAttributeServlet());
        webApplication.addServletMapping("servletContextAttributeServlet",
                "/servletContextAttribute");
        TestHttpServerResponse response = new TestHttpServerResponse();
        TestHttpServerRequest request = new TestHttpServerRequest();
        request.setRequestTarget("/servletContextAttribute");
        webApplicationServer.initialize();
        webApplicationServer.start();
        webApplicationServer.process(request, response);
        assertNotNull(webApplication.getAttribute("attributeAdded2"));
        webApplicationServer.stop();
    }

    /**
     * Test attributeRemoved method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAttributeRemoved() throws Exception {
        webApplication.addListener(new TestServletContextAttributeListener());
        webApplication.addServlet("servletContextAttributeServlet",
                new TestServletContextAttributeServlet());
        webApplication.addServletMapping("servletContextAttributeServlet",
                "/servletContextAttribute");
        TestHttpServerResponse response = new TestHttpServerResponse();
        TestHttpServerRequest request = new TestHttpServerRequest();
        request.setRequestTarget("/servletContextAttribute");
        webApplicationServer.initialize();
        webApplicationServer.start();
        webApplicationServer.process(request, response);
        assertNotNull(webApplication.getAttribute("attributeRemoved2"));
        webApplicationServer.stop();
    }

    /**
     * Test attributeReplaced method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAttributeReplaced() throws Exception {
        webApplication.addListener(new TestServletContextAttributeListener());
        webApplication.addServlet("servletContextAttributeServlet",
                new TestServletContextAttributeServlet());
        webApplication.addServletMapping("servletContextAttributeServlet",
                "/servletContextAttribute");
        TestHttpServerResponse response = new TestHttpServerResponse();
        TestHttpServerRequest request = new TestHttpServerRequest();
        request.setRequestTarget("/servletContextAttribute");
        webApplicationServer.initialize();
        webApplicationServer.start();
        webApplicationServer.process(request, response);
        assertNotNull(webApplication.getAttribute("attributeReplaced2"));
        webApplicationServer.stop();
    }

    /**
     * Test HttpServlet to validate the servlet context attributes where added,
     * remove and replaced.
     */
    public class TestServletContextAttributeServlet extends HttpServlet {

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
            request.getServletContext().setAttribute("attributeAdded", true);
            request.getServletContext().setAttribute("attributeRemoved", true);
            request.getServletContext().removeAttribute("attributeRemoved");
            request.getServletContext().setAttribute("attributeReplaced", false);
            request.getServletContext().setAttribute("attributeReplaced", true);
        }
    }

    /**
     * Test ServletContextAttributeListener to validate attributeAdded,
     * attributeRemoved and attributeReplaced are properly called.
     */
    public class TestServletContextAttributeListener
            implements ServletContextAttributeListener {

        /**
         * Handle attribute added event.
         *
         * @param event the event.
         */
        @Override
        public void attributeAdded(ServletContextAttributeEvent event) {
            if (event.getName().equals("attributeAdded")) {
                event.getServletContext().setAttribute("attributeAdded2", true);
            }
        }

        /**
         * Handle attribute removed event.
         *
         * @param event the event.
         */
        @Override
        public void attributeRemoved(ServletContextAttributeEvent event) {
            if (event.getName().equals("attributeRemoved")) {
                event.getServletContext().setAttribute("attributeRemoved2", true);
            }
        }

        /**
         * Handle attribute replaced event.
         *
         * @param event the event.
         */
        @Override
        public void attributeReplaced(ServletContextAttributeEvent event) {
            if (event.getName().equals("attributeReplaced")) {
                event.getServletContext().setAttribute("attributeReplaced2", true);
            }
        }
    }
}
