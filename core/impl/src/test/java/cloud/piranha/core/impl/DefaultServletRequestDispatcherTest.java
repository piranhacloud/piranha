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
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for DefaultServletRequestDispatcher.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultServletRequestDispatcherTest {

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testForward() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Forward", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        PrintWriter out = resp.getWriter();
                        out.println("Forward");
                        out.flush();
                    }
                })
                .servletMapping("Forward", "/forward")
                .build();
        webApplication.initialize();
        webApplication.start();

        WebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        WebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .build();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);

        RequestDispatcher dispatcher = webApplication.getRequestDispatcher("/forward");
        dispatcher.forward(request, response);

        String responseText = new String(byteOutput.toByteArray());
        assertTrue(responseText.contains("Forward"));
    }

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testForward3() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Error", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                        throw new IOException();
                    }
                })
                .servletMapping("Error", "/error")
                .build();
        webApplication.initialize();
        webApplication.start();

        WebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        WebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        RequestDispatcher dispatcher = webApplication.getRequestDispatcher("/error");
        assertThrows(IOException.class, () -> dispatcher.forward(request, response));
    }

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testForward4() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Runtime", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                        throw new RuntimeException();
                    }
                })
                .servletMapping("Runtime", "/runtime")
                .build();
        webApplication.initialize();
        webApplication.start();

        WebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        WebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();

        RequestDispatcher dispatcher = webApplication.getRequestDispatcher("/runtime");
        assertThrows(RuntimeException.class, () -> dispatcher.forward(request, response));
    }

    /**
     * Test that a request given to the request dispatcher upon forward is the
     * same as the original request.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testForwardNoWrapping() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("NoWrapping", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        resp.getWriter().print(req.toString());
                    }
                })
                .servletMapping("NoWrapping", "/nowrapping")
                .build();
        webApplication.initialize();
        webApplication.start();

        WebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        WebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        response.setBodyOnly(true);

        RequestDispatcher dispatcher = webApplication.getRequestDispatcher("/nowrapping");
        dispatcher.forward(request, response);
        assertEquals(request.toString(), byteOutput.toString("UTF-8"));
    }

    /**
     * Test a forward using a named dispatcher.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testForwardWithNamedDispatcher() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Forward", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        PrintWriter out = resp.getWriter();
                        out.println("Forward");
                    }
                })
                .servletMapping("Forward", "/forward")
                .build();
        webApplication.initialize();
        webApplication.start();

        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);

        WebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .build();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        response.setBodyOnly(true);

        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("Forward");
        dispatcher.forward(request, response);
        response.flushBuffer();
        String responseText = new String(byteOutput.toByteArray());
        assertTrue(responseText.contains("Forward"));
    }

    /**
     * Test a forward with a query string.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testForwardWithQueryString() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Forward", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        /*
                         * Force the original parameter to be parsed out of the query string.
                         */
                        req.getParameter("p");
                        /*
                         * Dispatch with a new parameter value.
                         */
                        getServletContext()
                                .getRequestDispatcher("/forward2?p=New")
                                .forward(req, resp);
                    }
                })
                .servletMapping("Forward", "/forward")
                .servlet("Forward2", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        /*
                         * We should be getting the new parameter value here as it takes precendence.
                         */
                        resp.getWriter().print(req.getParameterMap().get("p")[0]);
                    }
                })
                .servletMapping("Forward2", "/forward2")
                .build();
        webApplication.initialize();
        webApplication.start();

        WebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .servletPath("/forward")
                .queryString("p=Original")
                .build();

        WebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .build();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);

        webApplication.service(request, response);
        assertEquals("New", byteOutput.toString());
    }

    /**
     * Test include method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testInclude() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Echo", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                        response.getWriter().print("ECHO");
                        response.getWriter().flush();
                    }
                })
                .build();
        webApplication.initialize();
        webApplication.start();

        WebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        WebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .build();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);

        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("Echo");
        dispatcher.include(request, response);
        response.flushBuffer();
        assertTrue(byteOutput.toString().contains("ECHO"));
    }

    /**
     * Test include method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testInclude2() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Echo2", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                        response.getOutputStream().write("ECHO".getBytes());
                        response.getOutputStream().flush();
                    }
                })
                .build();
        webApplication.initialize();
        webApplication.start();

        WebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        WebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .build();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);

        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("Echo2");
        dispatcher.include(request, response);
        response.flushBuffer();
        assertTrue(byteOutput.toString().contains("ECHO"));
    }

    /**
     * Test that a request given to the request dispatcher upon include is the
     * same as the original request.
     */
    @Test
    void testIncludeNoWrapping() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("NoWrapping", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        req.getRequestDispatcher("/nowrapping2").include(req, resp);
                        resp.flushBuffer();
                    }
                })
                .servletMapping("NoWrapping", "/nowrapping")
                .servlet("NoWrapping2", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        resp.getWriter().print(req.toString());
                    }
                })
                .servletMapping("NoWrapping2", "/nowrapping2")
                .build();
        webApplication.initialize();
        webApplication.start();

        WebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        WebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .build();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);

        RequestDispatcher dispatcher = webApplication.getRequestDispatcher("/nowrapping");
        dispatcher.forward(request, response);
        assertEquals(request.toString(), byteOutput.toString("UTF-8"));
    }

    @Test
    void testErrorDispatcher() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("error-servlet", new TestSendError());
        webApp.addServletMapping("error-servlet", "/sendError");
        webApp.addServlet("snoop", TestSnoopServlet.class);
        webApp.addServletMapping("snoop", "/snoop");
        webApp.getManager().getErrorPageManager().addErrorPage(500, "/snoop");
        webApp.initialize();
        webApp.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/sendError");
        request.setParameter("send-error", new String[]{"true"});
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.service(request, response);
        String responseText = new String(response.getResponseBytes());
        webApp.stop();
        assertTrue(responseText.contains(RequestDispatcher.ERROR_MESSAGE));
        assertTrue(responseText.contains("some-internal-error"));
    }

    @Test
    void testErrorDispatcher2() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("error-servlet", TestIOExceptionServlet.class);
        webApp.addServletMapping("error-servlet", "/sendError");
        webApp.addServlet("snoop", TestSnoopServlet.class);
        webApp.addServletMapping("snoop", "/snoop");
        webApp.getManager().getErrorPageManager().addErrorPage(IOException.class.getName(), "/snoop");
        webApp.initialize();
        webApp.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/sendError");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.service(request, response);
        String responseText = new String(response.getResponseBytes());
        webApp.stop();
        assertEquals(500, response.getStatus());
        assertTrue(responseText.contains(RequestDispatcher.ERROR_EXCEPTION_TYPE));
        assertTrue(responseText.contains(IOException.class.getName()));
    }

    static class TestSendError extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            if (request.getParameter("send-error") != null) {
                response.sendError(500, "some-internal-error");
                return;
            }
            throw new UnavailableException("unavailable");
        }
    }
}
