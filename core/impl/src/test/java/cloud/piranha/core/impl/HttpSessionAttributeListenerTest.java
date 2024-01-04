/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the HttpSessionAttributeListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpSessionAttributeListenerTest {

    /**
     * Create the web application.
     *
     * @return the web application.
     */
    private WebApplication createWebApplication() {
        return new DefaultWebApplication();
    }

    /**
     * Create the web application request.
     *
     * @return the web application request.
     */
    private WebApplicationRequest createWebApplicationRequest() {
        return new DefaultWebApplicationRequest();
    }

    /**
     * Create the web application response.
     *
     * @return the web application response.
     */
    private WebApplicationResponse createWebApplicationResponse() {
        return new DefaultWebApplicationResponse();
    }

    /**
     * Test attributeAdded method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAttributeAdded() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addListener(new HttpSessionAttributeListener() {
            @Override
            public void attributeAdded(HttpSessionBindingEvent event) {
                event.getSession().getServletContext()
                        .setAttribute("testAttributeAdded", true);
            }
        });
        webApplication.addServlet("TestAttributeAddedServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                request.getSession().setAttribute("name", "value");
            }
        });
        webApplication.addServletMapping("TestAttributeAddedServlet", "/testAttributeAdded");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testAttributeAdded");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
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
        WebApplication webApplication = createWebApplication();
        webApplication.addListener(new HttpSessionAttributeListener() {
            @Override
            public void attributeRemoved(HttpSessionBindingEvent event) {
                event.getSession().getServletContext()
                        .setAttribute("testAttributeRemoved", true);
            }
        });
        webApplication.addServlet("TestAttributeRemovedServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                request.getSession().setAttribute("name", "value");
                request.getSession().removeAttribute("name");
            }
        });
        webApplication.addServletMapping("TestAttributeRemovedServlet", "/testAttributeRemoved");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testAttributeRemoved");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
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
        WebApplication webApplication = createWebApplication();
        webApplication.addListener(new HttpSessionAttributeListener() {
            @Override
            public void attributeReplaced(HttpSessionBindingEvent event) {
                event.getSession().getServletContext()
                        .setAttribute("testAttributeReplaced", true);
            }
        });
        webApplication.addServlet("TestAttributeReplacedServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                request.getSession().setAttribute("name", "value");
                request.getSession().setAttribute("name", "value2");
            }
        });
        webApplication.addServletMapping("TestAttributeReplacedServlet", 
                "/testAttributeReplaced");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testAttributeReplaced");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testAttributeReplaced"));
    }
}
