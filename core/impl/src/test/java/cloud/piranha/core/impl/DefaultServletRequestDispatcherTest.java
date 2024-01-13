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

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Snoop", TestSnoopServlet.class);
        webApplication.initialize();
        webApplication.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("Snoop");
        dispatcher.forward(request, response);
        response.flushBuffer();
        String responseText = new String(response.getResponseBytes());
        assertTrue(responseText.contains("<title>Snoop</title>"));
    }

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testForward2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(new DefaultWebApplicationRequestMapper());
        webApplication.addServlet("Snoop", TestSnoopServlet.class);
        webApplication.addServletMapping("Snoop", "/Snoop");
        webApplication.initialize();
        webApplication.start();

        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);
        
        webApplication.linkRequestAndResponse(request, response);
        
        RequestDispatcher dispatcher = webApplication.getRequestDispatcher("/Snoop");
        dispatcher.forward(request, response);
        String responseText = new String(response.getResponseBytes());
        webApplication.stop();
        assertTrue(responseText.contains("<title>Snoop</title>"));
    }

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testForward3() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(new DefaultWebApplicationRequestMapper());
        webApp.addServlet("Error", TestIOExceptionServlet.class);
        webApp.addServletMapping("Error", "/Error");
        webApp.initialize();
        webApp.start();
        RequestDispatcher dispatcher = webApp.getRequestDispatcher("/Error");
        assertThrows(IOException.class, () -> dispatcher.forward(request, response));
    }

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testForward4() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(new DefaultWebApplicationRequestMapper());
        webApp.addServlet("Runtime", TestRuntimeExceptionServlet.class);
        webApp.addServletMapping("Runtime", "/Runtime");
        webApp.initialize();
        webApp.start();
        RequestDispatcher dispatcher = webApp.getRequestDispatcher("/Runtime");
        assertThrows(RuntimeException.class, () -> dispatcher.forward(request, response));
    }
    
    /**
     * Test that a request given to the request dispatcher upon forward is the
     * same as the original request.
     */
    @Test
    void testForwardNoWrapping() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        response.setBodyOnly(true);
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("NoWrapping", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                response.getWriter().print(req.toString());
            }
        });
        webApplication.addServletMapping("NoWrapping", "/nowrapping");
        webApplication.initialize();
        webApplication.start();
        RequestDispatcher dispatcher = webApplication.getRequestDispatcher("/nowrapping");
        assertNotNull(dispatcher);
        dispatcher.forward(request, response);
        assertEquals(request.toString(), byteOutput.toString("UTF-8"));
    }

    /**
     * Test include method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testInclude() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Echo", TestEcho1Servlet.class);
        webApplication.initialize();
        webApplication.start();

        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApplication);

        webApplication.linkRequestAndResponse(request, response);
        
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("Echo");
        dispatcher.include(request, response);
        response.flushBuffer();
        String responseText = new String(response.getResponseBytes());
        assertTrue(responseText.contains("ECHO"));
    }

    /**
     * Test include method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testInclude2() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Echo2", TestEcho2Servlet.class);
        webApp.initialize();
        webApp.start();
        webApp.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApp.getNamedDispatcher("Echo2");
        dispatcher.include(request, response);
        response.flushBuffer();
        String responseText = new String(response.getResponseBytes());
        webApp.unlinkRequestAndResponse(request, response);
        webApp.stop();
        assertTrue(responseText.contains("ECHO"));
    }
    
    /**
     * Test that a request given to the request dispatcher upon include is the
     * same as the original request.
     */
    @Test
    void testIncludeNoWrapping() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("NoWrapping", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                req.getRequestDispatcher("/nowrapping2").include(req, resp);
                resp.flushBuffer();
            }
        });
        webApplication.addServlet("NoWrapping2", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.getWriter().print(req.toString());
            }
        });
        webApplication.addServletMapping("NoWrapping", "/nowrapping");
        webApplication.addServletMapping("NoWrapping2", "/nowrapping2");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        response.setBodyOnly(true);
        webApplication.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApplication.getRequestDispatcher("/nowrapping");
        assertNotNull(dispatcher);
        dispatcher.forward(request, response);
        response.flushBuffer();
        assertEquals(request.toString(), byteOutput.toString("UTF-8"));
        webApplication.unlinkRequestAndResponse(request, response);
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
