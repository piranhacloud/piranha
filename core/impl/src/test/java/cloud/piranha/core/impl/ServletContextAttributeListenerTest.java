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
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextAttributeEvent;
import jakarta.servlet.ServletContextAttributeListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the ServletContextAttributeListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ServletContextAttributeListenerTest {

    private WebApplication createWebApplication() {
        return new DefaultWebApplication();
    }

    private WebApplicationRequest createWebApplicationRequest() {
        return new DefaultWebApplicationRequest();
    }

    private WebApplicationResponse createWebApplicationResponse() {
        DefaultWebApplicationResponse response  = new DefaultWebApplicationResponse();
        response.getWebApplicationOutputStream().setOutputStream(new ByteArrayOutputStream());
        return response;
    }

    /**
     * Test attributeAdded method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeAdded() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addListener(new ServletContextAttributeListener() {
            @Override
            public void attributeAdded(ServletContextAttributeEvent event) {
                event.getServletContext().setAttribute("testAttributeAdded", true);
            }
        });
        webApplication.addServlet("TestAttributeAddedServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                request.getServletContext().setAttribute("name", "value");
            }
        });
        webApplication.addServletMapping(
                "TestAttributeAddedServlet",
                "/testAttributeAdded");
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testAttributeAdded");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testAttributeAdded"));
        webApplication.stop();
    }

    /**
     * Test attributeRemoved method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeRemoved() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addListener(new ServletContextAttributeListener() {
            @Override
            public void attributeRemoved(ServletContextAttributeEvent event) {
                event.getServletContext().setAttribute("testAttributeRemoved", true);
            }
        });
        webApplication.addServlet("TestAttributeRemovedServlet",  new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                ServletContext context = request.getServletContext();
                context.setAttribute("name", "value");
                context.removeAttribute("name");
            }
        });
        webApplication.addServletMapping(
                "TestAttributeRemovedServlet",
                "/testAttributeRemoved");
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testAttributeRemoved");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testAttributeRemoved"));
        webApplication.stop();
    }

    /**
     * Test attributeReplaced method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeReplaced() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addListener(new ServletContextAttributeListener() {
            @Override
            public void attributeReplaced(ServletContextAttributeEvent event) {
                event.getServletContext().setAttribute("testAttributeReplaced", true);
            }
        });
        webApplication.addServlet("TestAttributeReplacedServlet",  new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                ServletContext context = request.getServletContext();
                context.setAttribute("name", "value");
                context.setAttribute("name", "value2");
            }
        });
        webApplication.addServletMapping(
                "TestAttributeReplacedServlet",
                "/testAttributeReplaced");
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testAttributeReplaced");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testAttributeReplaced"));
        webApplication.stop();
    }
}
