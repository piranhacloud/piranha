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

import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplicationRequest;
import cloud.piranha.webapp.impl.DefaultWebApplicationResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * Tests related to SessionCookieConfig.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SessionCookieConfigTest {
    
    /**
     * Test changing using a ServletContextListener.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testServletContextListener() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getHttpSessionManager().setWebApplication(webApplication);
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.setUnderlyingOutputStream(byteOutput);
        webApplication.addServlet("TestServletContextListenerServlet", new TestServletContextListenerServlet());
        webApplication.addServletMapping("TestServletContextListenerServlet", "/*");
        webApplication.addListener(new TestServletContextListener());
        webApplication.initialize();
        webApplication.start();
        try {
            webApplication.service(request, response);
        } catch (ServletException se) {
            fail();
        }
        webApplication.stop();
    }
    
    public class TestServletContextListener implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent event) {
            event.getServletContext().getSessionCookieConfig().setComment("MY COMMENT");
        }
    }

    public static class TestServletContextListenerServlet extends HttpServlet {

        @Override
        protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {

            request.getSession(true);
            
            if (!request.getServletContext().getSessionCookieConfig().getComment().equals("MY COMMENT")) {
                throw new ServletException("ServletContextListener did not work");
            }
        }
    }
    /**
     * Test setName method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetName() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getHttpSessionManager().setWebApplication(webApplication);
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.setUnderlyingOutputStream(byteOutput);
        webApplication.addServlet("TestSetNameServlet", new TestSetNameServlet());
        webApplication.addServletMapping("TestSetNameServlet", "/*");
        webApplication.initialize();
        webApplication.start();
        try {
            webApplication.service(request, response);
            fail();
        } catch (IllegalStateException ise) {
        }
        webApplication.stop();
    }

    public static class TestSetNameServlet extends HttpServlet {

        @Override
        protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {

            request.getSession(true);
            request.getServletContext().getSessionCookieConfig().setName("MYNAME");
        }
    }

    /**
     * Test setSecure method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetSecure() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getHttpSessionManager().setWebApplication(webApplication);
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.setUnderlyingOutputStream(byteOutput);
        webApplication.addServlet("TestSetSecureServlet", new TestSetSecureServlet());
        webApplication.addServletMapping("TestSetSecureServlet", "/*");
        webApplication.initialize();
        webApplication.start();
        try {
            webApplication.service(request, response);
            fail();
        } catch (IllegalStateException ise) {
        }
        webApplication.stop();
    }

    public static class TestSetSecureServlet extends HttpServlet {

        @Override
        protected void service(HttpServletRequest request,
                HttpServletResponse response) throws ServletException, IOException {

            request.getSession(true);
            request.getServletContext().getSessionCookieConfig().setSecure(true);
        }
    }
}
