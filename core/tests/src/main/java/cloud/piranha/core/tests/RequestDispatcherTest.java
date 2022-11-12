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
package cloud.piranha.core.tests;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import jakarta.servlet.RequestDispatcher;
import static jakarta.servlet.RequestDispatcher.FORWARD_CONTEXT_PATH;
import static jakarta.servlet.RequestDispatcher.FORWARD_MAPPING;
import static jakarta.servlet.RequestDispatcher.FORWARD_PATH_INFO;
import static jakarta.servlet.RequestDispatcher.FORWARD_QUERY_STRING;
import static jakarta.servlet.RequestDispatcher.FORWARD_REQUEST_URI;
import static jakarta.servlet.RequestDispatcher.FORWARD_SERVLET_PATH;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for any RequestDispatcher implementation.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class RequestDispatcherTest {

    /**
     * Create the web application.
     *
     * @return the web application.
     */
    protected abstract WebApplication createWebApplication();

    /**
     * Create the web application request.
     *
     * @return the web application request.
     */
    protected abstract WebApplicationRequest createWebApplicationRequest();

    /**
     * Create the web application response.
     *
     * @return the web application response.
     */
    protected abstract WebApplicationResponse createWebApplicationResponse();

    /**
     * Test forward method on a named RequestDispatcher.
     *
     * @assertion Servlet:SPEC:181
     * @assertion Servlet:SPEC:181.1
     * @assertion Servlet:SPEC:181.2
     * @assertion Servlet:SPEC:181.3
     * @assertion Servlet:SPEC:181.4
     * @assertion Servlet:SPEC:181.5
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedForward() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestNamedForwardServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                PrintWriter writer = response.getWriter();
                if (request.getAttribute(FORWARD_CONTEXT_PATH) != null) {
                    writer.println(request.getAttribute(FORWARD_CONTEXT_PATH));
                }
                if (request.getAttribute(FORWARD_MAPPING) != null) {
                    writer.println(request.getAttribute(FORWARD_CONTEXT_PATH));
                }
                if (request.getAttribute(FORWARD_PATH_INFO) != null) {
                    writer.println(request.getAttribute(FORWARD_PATH_INFO));
                }
                if (request.getAttribute(FORWARD_QUERY_STRING) != null) {
                    writer.println(request.getAttribute(FORWARD_QUERY_STRING));
                }
                if (request.getAttribute(FORWARD_REQUEST_URI) != null) {
                    writer.println(request.getAttribute(FORWARD_REQUEST_URI));
                }
                if (request.getAttribute(FORWARD_SERVLET_PATH) != null) {
                    writer.println(request.getAttribute(FORWARD_SERVLET_PATH));
                }
                writer.flush();
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        response.setBodyOnly(true);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestNamedForwardServlet");
        dispatcher.forward(request, response);
        assertEquals("", new String(byteOutput.toByteArray()));
    }

    /**
     * Test forward method on a named RequestDispatcher.
     *
     * @assertion Servlet:SPEC:79
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedForward2() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestNamedForward2Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                PrintWriter writer = response.getWriter();
                writer.print(request.getRequestURI());
                writer.flush();
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        response.setBodyOnly(true);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestNamedForward2Servlet");
        dispatcher.forward(request, response);
        assertEquals("/", new String(byteOutput.toByteArray()));
    }

    /**
     * Test forward method on a named RequestDispatcher.
     *
     * @assertion Servlet:SPEC:80
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedForward3() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestNamedForward3Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                PrintWriter writer = response.getWriter();
                writer.print("SUCCESS");
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        response.setBodyOnly(true);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestNamedForward3Servlet");
        dispatcher.forward(request, response);
        assertEquals("SUCCESS", new String(byteOutput.toByteArray()));
    }

    /**
     * Test forward method on a named RequestDispatcher.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedForward4() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestNamedForward4Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                PrintWriter writer = response.getWriter();
                writer.println("FAILED");
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setBodyOnly(true);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        response.flushBuffer();      
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestNamedForward4Servlet");
        try {
            dispatcher.forward(request, response);
            fail();
        } catch (IllegalStateException ise) {
        }
    }
    
    /**
     * Test include method on a named RequestDispatcher.
     * 
     * @assertion Servlet:SPEC:192.1
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedInclude() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestNamedIncludeServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                PrintWriter writer = response.getWriter();
                response.setStatus(202);
                response.setHeader("header", "value");
                writer.println("INCLUDED");
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        response.setBodyOnly(false);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestNamedIncludeServlet");
        dispatcher.include(request, response);
        response.flushBuffer();
        assertTrue(new String(byteOutput.toByteArray()).contains("HTTP/1.1"));
        assertFalse(new String(byteOutput.toByteArray()).contains("202"));
        assertFalse(new String(byteOutput.toByteArray()).contains("header"));
        assertFalse(new String(byteOutput.toByteArray()).contains("value"));
        assertTrue(new String(byteOutput.toByteArray()).contains("INCLUDED"));
    }
    
    /**
     * Test include method on a named RequestDispatcher.
     * 
     * @assertion Servlet:SPEC:192.2
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedInclude2() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestNamedInclude2Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                OutputStream output = response.getOutputStream();
                output.write('1');
                output.write('2');
                output.write('3');
                output.write('4');
                output.write('5');
                if (response.isCommitted()) {
                    output.write('6');
                }
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setBufferSize(5);
        response.setWebApplication(webApplication);
        response.setBodyOnly(true);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestNamedInclude2Servlet");
        dispatcher.include(request, response);
        response.flushBuffer();
        assertEquals("123456", new String(byteOutput.toByteArray()));
    }
    
    /**
     * Test include method on a named RequestDispatcher.
     * 
     * @assertion Servlet:SPEC:192.3
     * @assertion Servlet:SPEC:192.4
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedInclude3() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestNamedInclude3Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                PrintWriter writer = response.getWriter();
                response.flushBuffer();
                writer.println(request.getSession().getId());
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        response.setWebApplication(webApplication);
        response.setBodyOnly(true);
        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestNamedInclude3Servlet");
        try {
            dispatcher.include(request, response);
            fail();
        } catch(IllegalStateException ise) {
            assertTrue(ise.getMessage().contains("addCookie"));
        }
    }
}
