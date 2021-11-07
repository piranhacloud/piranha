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
package cloud.piranha.core.impl.tests;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationResponse;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.WebConnection;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for testing everything related to the HttpServletRequest API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpServletRequestTest {

    /**
     * Stores the HTTP servlet request.
     */
    protected TestWebApplicationRequest request;

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @BeforeEach
    void setUp() throws Exception {
        request = new TestWebApplicationRequest();
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        assertThrows(IllegalStateException.class, () -> request.changeSessionId());
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        HttpSession session = request.getSession(true);
        String sessionId1 = session.getId();
        request.setRequestedSessionId(session.getId());
        String sessionId2 = request.changeSessionId();
        assertNotEquals(sessionId1, sessionId2);
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        HttpSession session = request.getSession(true);
        String previousSessionId = session.getId();
        String newSessionId = request.changeSessionId();
        assertNotEquals(previousSessionId, newSessionId);
        assertEquals(newSessionId, request.getSession(false).getId());
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    void testGetDateHeader() {
        assertEquals(-1L, request.getDateHeader("notfound"));
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    void testGetIntHeader() {
        assertEquals(-1, request.getIntHeader("notfound"));
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart() throws Exception {
        request.setContentType("text/html");
        assertThrows(ServletException.class, () -> request.getPart("not_there"));
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        request.setWebApplication(webApplication);
        request.setContentType("multipart/form-data");
        assertNull(request.getPart("not_there"));
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        request.setWebApplication(webApp);
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        request.setWebApplication(webApp);
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession(false));
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        request.setWebApplication(webApp);
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(false);
        assertNull(session);
    }

    /**
     * Test setRequestedSessionIdFromURL method.
     */
    @Test
    void testIsRequestedSessionIdFromUrl() {
        assertFalse(request.isRequestedSessionIdFromUrl());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test
    void testIsRequestedSessionIdValid() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        request.setWebApplication(webApp);
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertTrue(request.isRequestedSessionIdValid());
    }

    /**
     * Test isUserInRole method.
     */
    @Test
    void testIsUserInRole() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        request.setWebApplication(webApplication);
        assertFalse(request.isUserInRole("notmatched"));
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testUpgrade() throws Exception {
        assertNotNull(request.upgrade(TestHandler.class));
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testUpgrade2() throws Exception {
        assertThrows(ServletException.class, () -> request.upgrade(TestThrowingHandler.class));
    }

    /**
     * Test upgrade method.
     */
    @Test
    void testUpgrade3() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Upgrade", TestHttpUpgradeServlet.class);
        webApplication.addServletMapping("Upgrade", "/*");
        webApplication.initialize();
        webApplication.start();
        request.setHeader("Upgrade", "YES");
        request.setMethod("POST");
        webApplication.service(request, response);
    }

    public static class TestHandler implements HttpUpgradeHandler {

        public TestHandler() {
        }

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }

    public static class TestThrowingHandler implements HttpUpgradeHandler {

        public TestThrowingHandler() throws IllegalAccessException {
            throw new IllegalAccessException();
        }

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }

    public static class TestReadListenerHandler implements HttpUpgradeHandler {

        public TestReadListenerHandler() {
        }

        @Override
        public void init(WebConnection conection) {
            try {
                ServletInputStream input = conection.getInputStream();
                ServletOutputStream output = conection.getOutputStream();
                TestReadListener readListener = new TestReadListener(input, output);
                input.setReadListener(readListener);
                output.flush();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void destroy() {
        }
    }

    public static class TestReadListener implements ReadListener {

        private ServletInputStream input = null;

        private ServletOutputStream output = null;

        private static final String DELIMITER = "/";

        public TestReadListener(ServletInputStream input, ServletOutputStream output) {
            this.input = input;
            this.output = output;
        }

        @Override
        public void onDataAvailable() {
            try {
                StringBuilder sb = new StringBuilder();
                int len = -1;
                byte b[] = new byte[1024];
                while (input.isReady() && (len = input.read(b)) != -1) {
                    String data = new String(b, 0, len);
                    sb.append(data);
                }
                output.println(DELIMITER + sb.toString());
                output.flush();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void onAllDataRead() {
            try {
                output.close();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void onError(final Throwable t) {
            t.printStackTrace();
        }
    }

    public static class TestHttpUpgradeServlet extends HttpServlet {

        @Override
        public void doPost(
                HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            if (request.getHeader("Upgrade") != null) {
                response.setStatus(101);
                response.setHeader("Upgrade", "YES");
                response.setHeader("Connection", "Upgrade");
                request.upgrade(TestReadListenerHandler.class);
            } else {
                response.getWriter().println("Not upgraded");
            }
        }
    }
}
