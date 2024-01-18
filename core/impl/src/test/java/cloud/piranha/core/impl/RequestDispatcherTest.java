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
import jakarta.servlet.RequestDispatcher;
import static jakarta.servlet.RequestDispatcher.FORWARD_CONTEXT_PATH;
import static jakarta.servlet.RequestDispatcher.FORWARD_MAPPING;
import static jakarta.servlet.RequestDispatcher.FORWARD_PATH_INFO;
import static jakarta.servlet.RequestDispatcher.FORWARD_QUERY_STRING;
import static jakarta.servlet.RequestDispatcher.FORWARD_REQUEST_URI;
import static jakarta.servlet.RequestDispatcher.FORWARD_SERVLET_PATH;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the RequestDispatcher API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class RequestDispatcherTest {

    /**
     * Test forward method with a named RequestDispatcher.
     *
     * @jakarta.assertion Servlet:SPEC:181
     * @jakarta.assertion Servlet:SPEC:181.1
     * @jakarta.assertion Servlet:SPEC:181.2
     * @jakarta.assertion Servlet:SPEC:181.3
     * @jakarta.assertion Servlet:SPEC:181.4
     * @jakarta.assertion Servlet:SPEC:181.5
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testForwardWithNamedDispatcher() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestNamedForwardServlet", new HttpServlet() {
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
                })
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestNamedForwardServlet");
        dispatcher.forward(request, response);
        assertEquals("", new String(byteOutput.toByteArray()));
    }

    /**
     * Test forward method on a named RequestDispatcher.
     *
     * @jakarta.assertion Servlet:SPEC:79
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedForward2() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestNamedForward2Servlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        PrintWriter writer = response.getWriter();
                        writer.print(request.getRequestURI());
                        writer.flush();
                    }
                })
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestNamedForward2Servlet");
        dispatcher.forward(request, response);
        assertEquals("/", new String(byteOutput.toByteArray()));
    }

    /**
     * Test forward method on a named RequestDispatcher.
     *
     * @jakarta.assertion Servlet:SPEC:80
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedForward3() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestNamedForward3Servlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        PrintWriter writer = response.getWriter();
                        writer.print("SUCCESS");
                    }
                })
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

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
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestNamedForward4Servlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        PrintWriter writer = response.getWriter();
                        writer.println("FAILED");
                    }
                })
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

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
     * @jakarta.assertion Servlet:SPEC:192.1
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testNamedInclude() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestNamedIncludeServlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        PrintWriter writer = response.getWriter();
                        response.setStatus(202);
                        response.setHeader("header", "value");
                        writer.println("INCLUDED");
                    }
                })
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .underlyingOutputStream(byteOutput)
                .build();

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
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

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
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
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
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
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
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

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
    void testForwardWithNamedDispatcher2() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Forward", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        PrintWriter out = resp.getWriter();
                        out.println("Forward");
                    }
                })
                .servletMapping("Forward", "/forward")
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

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
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .servletPath("/forward")
                .queryString("p=Original")
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        webApplication.service(request, response);
        assertEquals("New", byteOutput.toString());
    }

    /**
     * Test the getNamedDispatcher method on WebApplication instance with an
     * existing Servlet.
     */
    @Test
    void testGetExistingNamedDispatcher() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("ExistingNamedDispatcher", new HttpServlet() {
                })
                .build()
                .initialize()
                .start();

        assertNotNull(webApplication.getNamedDispatcher("ExistingNamedDispatcher"));
    }

    /**
     * Test getNamedDispatcher method on WebApplication instance with a
     * non-existing Servlet.
     */
    @Test
    void testGetNonExistingNamedDispatcher() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .build()
                .initialize()
                .start();

        assertNull(webApplication.getNamedDispatcher("NotExistingNamedDispatcher"));
    }

    /**
     * Test include method using a writer works properly.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testInclude() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Writer", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                        response.getWriter().print("Writer");
                        response.getWriter().flush();
                    }
                })
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("Writer");
        dispatcher.include(request, response);
        response.flushBuffer();
        assertTrue(byteOutput.toString().contains("Writer"));
    }

    /**
     * Test include method using an output stream write works properly.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIncludeOutputStream() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("OutputStream", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                        response.getOutputStream().write("OutputStream".getBytes());
                        response.getOutputStream().flush();
                    }
                })
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("OutputStream");
        dispatcher.include(request, response);
        response.flushBuffer();
        assertTrue(byteOutput.toString().contains("OutputStream"));
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
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        RequestDispatcher dispatcher = webApplication.getRequestDispatcher("/nowrapping");
        dispatcher.forward(request, response);
        assertEquals(request.toString(), byteOutput.toString("UTF-8"));
    }

    /**
     * Test include method using named RequestDispatcher and it throwing an
     * IllegalStateException when the response is already committed.
     *
     * @jakarta.assertion Servlet:SPEC:192.3
     * @jakarta.assertion Servlet:SPEC:192.4
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIncludeWitNamedRequestDispatcherAndIllegalStateException() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("IllegalStateException", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                        PrintWriter writer = response.getWriter();
                        response.flushBuffer();
                        writer.println(request.getSession().getId());
                    }
                })
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .build();

        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("IllegalStateException");
        try {
            dispatcher.include(request, response);
            fail();
        } catch (IllegalStateException ise) {
            assertTrue(ise.getMessage().contains("Response already committed"));
        }
    }

    /**
     * Test include method using a named RequestDispatcher and a fixed buffer
     * size to determine the response was committed after buffer overflow
     * happened.
     *
     * @jakarta.assertion Servlet:SPEC:192.2
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIncludeWitNamedRequestDispatcherAndBufferSize() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("BufferSize", new HttpServlet() {
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
                        } else {
                            throw new ServletException("Response was not committed");
                        }
                    }
                })
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();
        response.setBufferSize(5);

        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("BufferSize");
        dispatcher.include(request, response);
        response.flushBuffer();
        assertEquals("123456", new String(byteOutput.toByteArray()));
    }

    /**
     * Test an include with a query string.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testIncludeWithQueryString() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Include", new HttpServlet() {
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
                                .getRequestDispatcher("/include2?p=New")
                                .include(req, resp);
                    }
                })
                .servletMapping("Include", "/include")
                .servlet("Include2", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        /*
                         * We should be getting the new parameter value here as it takes precendence.
                         */
                        resp.getWriter().print(req.getParameterMap().get("p")[0]);
                    }
                })
                .servletMapping("Include2", "/include2")
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .servletPath("/include")
                .queryString("p=Original")
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        webApplication.service(request, response);
        assertEquals("New", byteOutput.toString());
    }

    @Test
    void testErrorDispatcher() throws Exception {
        WebApplication webApp = new DefaultWebApplicationBuilder()
                .servlet("error-servlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                        if (request.getParameter("send-error") != null) {
                            response.sendError(500, "some-internal-error");
                            return;
                        }
                        throw new UnavailableException("unavailable");
                    }
                })
                .servletMapping("error-servlet", "/sendError")
                .servlet("snoop", TestSnoopServlet.class)
                .servletMapping("snoop", "/snoop")
                .build();
        webApp.getManager().getErrorPageManager().addErrorPage(500, "/snoop");
        webApp.initialize();
        webApp.start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .servletPath("/sendError")
                .build();
        request.setParameter("send-error", new String[]{"true"});

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApp)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        webApp.service(request, response);
        String responseText = new String(byteOutput.toByteArray());
        assertTrue(responseText.contains(RequestDispatcher.ERROR_MESSAGE));
        assertTrue(responseText.contains("some-internal-error"));
    }

    @Test
    void testErrorDispatcher2() throws Exception {
        WebApplication webApp = new DefaultWebApplicationBuilder()
                .servlet("error-servlet", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                        throw new IOException();
                    }
                })
                .servletMapping("error-servlet", "/sendError")
                .build();

        webApp.addServlet("snoop", TestSnoopServlet.class);
        webApp.addServletMapping("snoop", "/snoop");
        webApp.getManager().getErrorPageManager().addErrorPage(IOException.class.getName(), "/snoop");
        webApp.initialize();
        webApp.start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApp)
                .servletPath("/sendError")
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApp)
                .bodyOnly(true)
                .underlyingOutputStream(byteOutput)
                .build();

        webApp.service(request, response);
        String responseText = new String(byteOutput.toByteArray());
        assertEquals(500, response.getStatus());
        assertTrue(responseText.contains(RequestDispatcher.ERROR_EXCEPTION_TYPE));
        assertTrue(responseText.contains(IOException.class.getName()));
    }

    /**
     * Test getRequestDispatcher method.
     */
    @Test
    void testGetRequestDispatcher() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .build();

        assertNotNull(request.getRequestDispatcher("/test"));
    }

    /**
     * Test getRequestDispatcher method.
     */
    @Test
    void testGetRequestDispatcher2() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("TestGetRequestDispatcherServlet", new HttpServlet() {
                })
                .servletMapping("TestGetRequestDispatcherServlet", "testGetRequestDispatcher")
                .build()
                .initialize()
                .start();

        assertNotNull(webApplication.getRequestDispatcher("/testGetRequestDispatcher"));
    }

    /**
     * Test include.
     *
     * @throws Exception when a serious error occurred.
     */
    @Test
    void testInclude2() throws Exception {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("Include", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                            throws IOException, ServletException {
                        RequestDispatcher dispatcher = request
                                .getServletContext().getRequestDispatcher("/include2");
                        dispatcher.include(request, response);
                    }
                })
                .servletMapping("Include", "/include")
                .servlet("Include2", new HttpServlet() {
                    @Override
                    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                            throws IOException, ServletException {
                        response.getWriter().print("This was included");
                        response.getWriter().flush();
                    }
                })
                .servletMapping("Include2", "/include2")
                .build()
                .initialize()
                .start();

        DefaultWebApplicationRequest request = new DefaultWebApplicationRequestBuilder()
                .webApplication(webApplication)
                .servletPath("/include")
                .build();

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponseBuilder()
                .webApplication(webApplication)
                .underlyingOutputStream(byteOutput)
                .build();

        webApplication.service(request, response);
        assertTrue(new String(byteOutput.toByteArray()).contains("This was included"));
    }
}
