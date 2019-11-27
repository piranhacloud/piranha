/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * The JUnit tests for testing everything related to the RequestDispatcher API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class RequestDispatcherTest {

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
     * Test include method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testInclude() throws Exception {
        webApplication.addServlet("Include1aServlet", new Include1aServlet());
        webApplication.addServletMapping("Include1aServlet", "/TestServlet/*");
        webApplication.addServlet("Include1bServlet", new Include1bServlet());
        webApplication.addServletMapping("Include1bServlet", "/include/IncludedServlet");
        TestHttpServerResponse response = new TestHttpServerResponse();
        TestHttpServerRequest request = new TestHttpServerRequest();
        request.setRequestTarget("/TestServlet?test=simple");
        webApplicationServer.initialize();
        webApplicationServer.start();
        webApplicationServer.process(request, response);
        assertTrue(response.getByteArrayOutputStream().toString().contains("200"));
        assertTrue(response.getByteArrayOutputStream().toString().contains("SUCCESS"));
        webApplicationServer.stop();
    }

    /**
     * A test servlet for testing a RequestDispatcher include.
     */
    public static class Include1aServlet extends HttpServlet {

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            RequestDispatcher rd = request.getRequestDispatcher("/include/IncludedServlet?test=me");
            rd.include(request, response);
        }
    }

    /**
     * A test servlet for testing a RequestDispatcher include.
     */
    public static class Include1bServlet extends HttpServlet {

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            PrintWriter writer = response.getWriter();
            writer.print("SUCCESS");
        }
    }
}
