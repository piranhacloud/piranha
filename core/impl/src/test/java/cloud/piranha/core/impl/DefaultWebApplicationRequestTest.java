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

import cloud.piranha.core.api.WebApplicationInputStream;
import cloud.piranha.core.api.WebApplicationRequest;
import static jakarta.servlet.DispatcherType.INCLUDE;
import static jakarta.servlet.DispatcherType.REQUEST;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.WebConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Enumeration;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * The JUnit tests for the DefaultWebApplicationRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationRequestTest {

    /**
     * Test authenticate method.
     */
    @Test
    void testAuthenticate() {
        try {
            DefaultWebApplication webApplication = new DefaultWebApplication();
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            request.setWebApplication(webApplication);
            DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
            assertFalse(request.authenticate(response));
        } catch (IOException | ServletException exception) {
            fail();
        }
    }

    /**
     * Test authenticate method.
     */
    @Test
    void testAuthenticate2() {
        try {
            DefaultWebApplication webApplication = new DefaultWebApplication();
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            request.setWebApplication(webApplication);
            DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
            request.authenticate(response);
        } catch (IOException | ServletException ex) {
            fail();
        }
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
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
    void testChangeSessionId2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(assertThrows(IllegalStateException.class,
                request::changeSessionId));
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        String previousSessionId = session.getId();
        String newSessionId = request.changeSessionId();
        assertNotEquals(previousSessionId, newSessionId);
        assertEquals(newSessionId, request.getSession(false).getId());
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(assertThrows(IllegalStateException.class,
                request::getAsyncContext));
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        request.setAsyncSupported(true);
        request.startAsync();
        assertNotNull(request.getAsyncContext());
    }

    /**
     * Test getAttribute method.
     */
    @Test
    void testGetAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAttribute("name", "value");
        assertNotNull(request.getAttribute("name"));
    }

    /**
     * Test getAttributeNames method.
     */
    @Test
    void testGetAttributeNames() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAttribute("name", "value");
        request.setAttribute("name2", "value2");
        assertNotNull(request.getAttributeNames());
        Enumeration<String> attributeNames = request.getAttributeNames();
        assertNotNull(attributeNames.nextElement());
        assertNotNull(attributeNames.nextElement());
        assertFalse(attributeNames.hasMoreElements());
    }

    /**
     * Test getAuthType method.
     */
    @Test
    void testGetAuthType() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getAuthType());
    }

    /**
     * Test getCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetCharacterEncoding() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getCharacterEncoding());
        request.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", request.getCharacterEncoding());
    }

    /**
     * Test getContentLength method.
     */
    @Test
    void testGetContentLength() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(-1, request.getContentLength());
    }

    /**
     * Test getContentLength method.
     */
    @Test
    void testGetContentLength2() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContentLength(1234);
        assertEquals(1234, request.getContentLength());
        assertEquals("1234", request.getHeader("Content-Length"));
    }

    /**
     * Test getContentLengthLong method.
     */
    @Test
    void testGetContentLengthLong() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(-1L, request.getContentLengthLong());
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getContentType());
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType2() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContentType("text/html");
        assertEquals("text/html", request.getContentType());
        assertEquals("text/html", request.getHeader("Content-Type"));
    }

    /**
     * Test getContextPath method.
     */
    @Test
    void testGetContextPath() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("", request.getContextPath());
    }

    /**
     * Test getCookies method.
     */
    @Test
    void testGetCookies() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getCookies());
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    void testGetDateHeader() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(-1L, request.getDateHeader("notfound"));
    }

    /**
     * Test getDispatcherType.
     */
    @Test
    void testGetDispatcherType() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(REQUEST, request.getDispatcherType());
    }

    /**
     * Test getHeader method.
     */
    @Test
    void testGetHeader() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getHeader("notfound"));
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    void testGetHeaderNames() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getHeaderNames());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    void testGetHeaders() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getHeaders("notfound"));
    }

    /**
     * Test getHttpServletMapping method.
     */
    @Test
    void testGetHttpServletMapping() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getHttpServletMapping());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getInputStream());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream2() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getInputStream());
        assertNotNull(assertThrows(IllegalStateException.class,
                request::getReader));
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    void testGetIntHeader() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(-1, request.getIntHeader("notfound"));
    }

    /**
     * Test getLocalAddr method.
     */
    @Test
    void testGetLocalAddr() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getLocalAddr());
    }

    /**
     * Test getLocalName method.
     */
    @Test
    void testGetLocalName() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getLocalName());
    }

    /**
     * Test getLocalPort method.
     */
    @Test
    void testGetLocalPort() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(0, request.getLocalPort());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocale() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setHeader("Accept-Language", "en");
        assertNotNull(request.getLocale());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocale2() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getLocale());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocales() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setHeader("Accept-Language", "en, de");
        assertNotNull(request.getLocales());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocales2() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getLocales());
    }

    /**
     * Test getMethod method.
     */
    @Test
    void testGetMethod() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("GET", request.getMethod());
    }

    /**
     * Test getMultipartConfig method.
     */
    @Test
    void testGetMultipartConfig() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getMultipartConfig());
    }

    /**
     * Test getParameter method.
     */
    @Test
    void testGetParameter() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getParameter("notfound"));
    }

    /**
     * Test getParameterMap method.
     */
    @Test
    void testGetParameterMap() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        WebApplicationInputStream requestInput = request.getWebApplicationInputStream();
        requestInput.setInputStream(new ByteArrayInputStream("param1=value1".getBytes(UTF_8)));
        request.setContentType("application/x-www-form-urlencoded");
        Map<String, String[]> parameterMap = request.getParameterMap();
        assertEquals(1, parameterMap.size());
        assertArrayEquals(new String[]{"value1"}, parameterMap.get("param1"));
    }

    /**
     * Test getParameterMap method.
     */
    @Test
    void testGetParameterMap2() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        WebApplicationInputStream requestInput = request.getWebApplicationInputStream();
        requestInput.setInputStream(new ByteArrayInputStream("param1=value1".getBytes(UTF_8)));
        request.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
        Map<String, String[]> parameterMap = request.getParameterMap();
        assertEquals(1, parameterMap.size());
        assertArrayEquals(new String[]{"value1"}, parameterMap.get("param1"));
    }

    /**
     * Test getParameterMap.
     */
    @Test
    void testGetParameterMap3() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getParameterMap());
    }

    /**
     * Test getParameterNames.
     */
    @Test
    void testGetParameterNames() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getParameterNames());
    }

    /**
     * Test getParameterValues.
     */
    @Test
    void testGetParameterValues() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getParameterValues("notfound"));
    }

    /**
     * Test getPart method.
     */
    @Test
    void testGetPart() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContentType("text/html");
        assertNotNull(assertThrows(ServletException.class,
                () -> request.getPart("not_there")));
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setContentType("multipart/form-data");
        assertNull(request.getPart("not_there"));
    }

    /**
     * Test getPart method.
     */
    @Test
    void testGetPart3() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertThrows(ServletException.class, () -> {
            request.getPart("notfound");
        });
    }

    /**
     * Test getParts method.
     */
    @Test
    void testGetParts() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertThrows(ServletException.class, request::getParts);
    }

    /**
     * Test getPathInfo method.
     */
    @Test
    void testGetPathInfo() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getPathInfo());
    }

    /**
     * Test getPathTranslated method.
     */
    @Test
    void testGetPathTranslated() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getPathTranslated());
    }

    /**
     * Test getProtocol method.
     */
    @Test
    void testGetProtocol() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("HTTP/1.1", request.getProtocol());
    }

    /**
     * Test getProtocolRequestId method.
     */
    @Test
    void testGetProtocolRequestId() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("", request.getProtocolRequestId());
    }

    /**
     * Test getQueryString method.
     */
    @Test
    void testGetQueryString() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getQueryString());
    }

    /**
     * Test getReader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetReader() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getReader());
    }

    /**
     * Test getReader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetReader2() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getReader());
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.getInputStream()));
    }

    /**
     * Test getRemoteAddr method.
     */
    @Test
    void testGetRemoteAddr() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getRemoteAddr());
    }

    /**
     * Test getRemoteHost method.
     */
    @Test
    void testGetRemoteHost() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getRemoteHost());
    }

    /**
     * Test getRemotePort method.
     */
    @Test
    void testGetRemotePort() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(0, request.getRemotePort());
    }

    /**
     * Test getRemoteUser method.
     */
    @Test
    void testGetRemoteUser() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getRemoteUser());
    }

    /**
     * Test getRequestDispatcher method.
     */
    @Test
    void testGetRequestDispatcher() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertNotNull(request.getRequestDispatcher("/test"));
    }

    /**
     * Test getRequestId method.
     */
    @Test
    void testGetRequestId() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getRequestId());
    }

    /**
     * Test getRequestURI method.
     */
    @Test
    void testGetRequestURI() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getRequestURI());
    }

    /**
     * Test getRequestURL method.
     */
    @Test
    void testGetRequestURL() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getRequestURL());
    }

    /**
     * Test getRequestedSessionId method.
     */
    @Test
    void testGetRequestedSessionId() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getRequestedSessionId());
    }

    /**
     * Test getScheme method.
     */
    @Test
    void testGetScheme() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("http", request.getScheme());
    }

    /**
     * Test getServerName method.
     */
    @Test
    void testGetServerName() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("localhost", request.getServerName());
    }

    /**
     * Test getServerPort method.
     */
    @Test
    void testGetServerPort() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(80, request.getServerPort());
    }

    /**
     * Test getServletConnection method.
     */
    @Test
    void testGetServletConnection() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getServletConnection());
    }

    /**
     * Test getServletContext method.
     */
    @Test
    void testGetServletContext() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertNotNull(request.getServletContext());
    }

    /**
     * Test getServletPath method.
     */
    @Test
    void testGetServletPath() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("", request.getServletPath());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        assertNotNull(session.getId());
        assertTrue(session.isNew());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(false);
        assertNull(session);
    }

    /**
     * Test getSession.
     *
     * @throws Exception
     */
    @Test
    void testGetSession3() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        ServletRegistration.Dynamic dynamic = webApplication.addServlet("session",
                new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
                response.setContentType("text/plain");
                try (PrintWriter out = response.getWriter()) {
                    if (request.isRequestedSessionIdValid()) {
                        HttpSession session = request.getSession(false);
                        out.println("Session is " + session);
                        if (session == null) {
                            session = request.getSession();
                            out.println("Session is " + session);
                        }
                    } else {
                        HttpSession session = request.getSession();
                        out.println("Session is " + session + ", from request");
                    }
                }
            }
        });
        assertNotNull(dynamic);
        dynamic.addMapping("/session");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/session");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        webApplication.service(request, response);
        assertNotNull(byteOutput.toByteArray().length > 0);
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession(false));
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession5() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertNotNull(request.getSession());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession6() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(false);
        assertNull(session);
    }

    /**
     * Test getTrailerFields method.
     */
    @Test
    void testGetTrailerFields() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        Map<String, String> trailerFields = request.getTrailerFields();
        assertTrue(trailerFields.isEmpty());
    }

    /**
     * Test getUpgradeHandler method.
     */
    @Test
    void testGetUpgradeHandler() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getUpgradeHandler());
    }

    /**
     * Test getUserPrincipal method.
     */
    @Test
    void testGetUserPrincipal() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getUserPrincipal());
    }

    /**
     * Test isAsyncStarted method.
     */
    @Test
    void testIsAsyncStarted() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertFalse(request.isAsyncStarted());
    }

    /**
     * Test isAsyncSupported method.
     */
    @Test
    void testIsAsyncSupported() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertFalse(request.isAsyncSupported());
    }

    /**
     * Test isRequestedSessionIdFromCookie method.
     */
    @Test
    void testIsRequestedSessionIdFromCookie() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromCookie());
    }

    /**
     * Test isRequestedSessionIdFromURL method.
     */
    @Test
    void testIsRequestedSessionIdFromURL() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromURL());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test
    void testIsRequestedSessionIdValid() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdValid());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test
    void testIsRequestedSessionIdValid2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        assertTrue(request.isRequestedSessionIdValid());
    }

    /**
     * Test isSecure method.
     */
    @Test
    void testIsSecure() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertFalse(request.isSecure());
    }

    /**
     * Test isSecure method.
     */
    @Test
    void testIsSecure2() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setScheme("https");
        assertTrue(request.isSecure());
    }

    /**
     * Test isTrailerFieldsReady method.
     */
    @Test
    void testIsTrailerFieldsReady() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertTrue(request.isTrailerFieldsReady());
    }

    /**
     * Test isUpgraded method.
     */
    @Test
    void testIsUpgraded() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertFalse(request.isUpgraded());
    }

    /**
     * Test isUserInRole method.
     */
    @Test
    void testIsUserInRole() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertFalse(request.isUserInRole("notmatched"));
    }

    /**
     * Test login method.
     */
    @Test
    void testLogin() {
        try {
            DefaultWebApplication webApplication = new DefaultWebApplication();
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            request.setWebApplication(webApplication);
            request.login("admin", "password");
            fail();
        } catch (ServletException exception) {
        }
    }

    /**
     * Test logout method.
     */
    @Test
    void testLogout() {
        try {
            DefaultWebApplication webApplication = new DefaultWebApplication();
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            request.setWebApplication(webApplication);
            request.logout();
            assertNull(request.getAuthType());
            assertNull(request.getRemoteUser());
            assertNull(request.getUserPrincipal());
        } catch (Exception exception) {
            fail();
        }
    }

    /**
     * Test newPushBuilder method.
     */
    @Test
    void testNewPushBuilder() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.newPushBuilder());
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    void testRemoveAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAttribute("name", "value");
        assertNotNull(request.getAttribute("name"));
        request.removeAttribute("name");
        assertNull(request.getAttribute("name"));
    }

    /**
     * Test setAttribute method.
     */
    @Test
    void testSetAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAttribute("name", "value");
        assertNotNull(request.getAttribute("name"));
    }

    /**
     * Test setAuthType method.
     */
    @Test
    void testSetAuthType() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAuthType("MYTYPE");
        assertEquals("MYTYPE", request.getAuthType());
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getCharacterEncoding());
        request.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", request.getCharacterEncoding());
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding2() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getCharacterEncoding());
        request.getReader();
        request.setCharacterEncoding("UTF-8");
        assertNotEquals("UTF-8", request.getCharacterEncoding());
    }

    /**
     * Test setCharacterEncoding method.
     *
     */
    @Test
    void testSetCharacterEncoding3() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(assertThrows(UnsupportedEncodingException.class,
                () -> request.setCharacterEncoding("doesnotexist")));
    }

    /**
     * Test setCharacterEncoding method.
     *
     */
    @Test
    void testSetCharacterEncoding4() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(assertThrows(UnsupportedEncodingException.class,
                () -> request.setCharacterEncoding(null)));
    }

    /**
     * Test setContextPath method.
     */
    @Test
    void testSetContextPath() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContextPath("/contextPath");
        assertEquals("/contextPath", request.getContextPath());
    }

    /**
     * Test setCookies method.
     */
    @Test
    void testSetCookies() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getCookies());
        request.setCookies(new Cookie[0]);
        assertNull(request.getCookies());
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("name", "value");
        request.setCookies(cookies);
        assertNotNull(request.getCookies());
        assertEquals("name", request.getCookies()[0].getName());
        assertEquals("value", request.getCookies()[0].getValue());
    }

    /**
     * Test setDispatcherType method.
     */
    @Test
    void testSetDispatcherType() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setDispatcherType(INCLUDE);
        assertEquals(INCLUDE, request.getDispatcherType());
    }

    /**
     * Test setLocalAddr method.
     */
    @Test
    void testSetLocalAddr() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getLocalAddr());
        request.setLocalAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getLocalAddr());
    }

    /**
     * Test setLocalName method.
     */
    @Test
    void testSetLocalName() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getLocalName());
        request.setLocalName("localhost");
        assertEquals("localhost", request.getLocalName());
    }

    /**
     * Test setLocalPort method.
     */
    @Test
    void testSetLocalPort() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(0, request.getLocalPort());
        request.setLocalPort(12345);
        assertEquals(12345, request.getLocalPort());
    }

    /**
     * Test setProtocol method.
     */
    @Test
    void testSetProtocol() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("HTTP/1.1", request.getProtocol());
        request.setProtocol("HTTP/1.0");
        assertEquals("HTTP/1.0", request.getProtocol());
    }

    /**
     * Test setRemoteAddr method.
     */
    @Test
    void testSetRemoteAddr() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getRemoteAddr());
        request.setRemoteAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getRemoteAddr());
    }

    /**
     * Test setRemoteHost method.
     */
    @Test
    void testSetRemoteHost() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getRemoteHost());
        request.setRemoteHost("localhost");
        assertEquals("localhost", request.getRemoteHost());
    }

    /**
     * Test setRemotePort method.
     */
    @Test
    void testSetRemotePort() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(0, request.getRemotePort());
        request.setRemotePort(12345);
        assertEquals(12345, request.getRemotePort());
    }

    /**
     * Test setServerName method.
     */
    @Test
    void testSetServerName() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("localhost", request.getServerName());
        request.setServerName("my.host.com");
        assertEquals("my.host.com", request.getServerName());
    }

    /**
     * Test setServerPort method.
     */
    @Test
    void testSetServerPort() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(80, request.getServerPort());
        request.setServerPort(8080);
        assertEquals(8080, request.getServerPort());
    }

    /**
     * Test setServletPath method.
     */
    @Test
    void testSetServletPath() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/servletPath");
        assertEquals("/servletPath", request.getServletPath());
    }

    /**
     * Test setUserPrincipal method.
     */
    @Test
    void testSetUserPrincipal() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setUserPrincipal(() -> {
            return "User Principal";
        });
        assertEquals("User Principal", request.getUserPrincipal().getName());
    }

    /**
     * Test setWebApplication method.
     */
    @Test
    void testSetWebApplication() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertEquals(webApplication, request.getServletContext());
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertThrows(IllegalStateException.class, request::startAsync);
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync2() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        assertThrows(IllegalStateException.class, () -> {
            request.startAsync(request, response);
        });
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync3() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(request.startAsync());
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setAsyncSupported(false);
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.startAsync(request, response)));
    }

    /**
     * Test upgrade method.
     */
    @Test
    void testUpgrade() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Upgrade", new HttpServlet() {
            @Override
            public void doPost(
                    HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {

                if (request.getHeader("Upgrade") != null) {
                    response.setStatus(101);
                    response.setHeader("Upgrade", "YES");
                    response.setHeader("Connection", "Upgrade");
                    request.upgrade(TestUpgradeHttpUpgradeHandler.class);
                } else {
                    response.getWriter().println("Not upgraded");
                }
            }
        });
        webApplication.addServletMapping("Upgrade", "/*");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setHeader("Upgrade", "YES");
        request.setMethod("POST");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        webApplication.service(request, response);
        assertEquals(101, response.getStatus());
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testUpgrade2() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.upgrade(TestUpgrade2HttpUpgradeHandler.class));
    }

    /**
     * Test upgrade method.
     */
    @Test
    void testUpgrade3() {
        WebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(assertThrows(ServletException.class,
                () -> request.upgrade(TestUpgrade3HttpUpgradeHandler.class)));
    }

    /**
     * A HttpUpgradeHandler used by the testUpgrade1 method.
     */
    public static class TestUpgrade2HttpUpgradeHandler implements HttpUpgradeHandler {

        /**
         * Constructor.
         */
        public TestUpgrade2HttpUpgradeHandler() {
        }

        @Override
        public void destroy() {
        }

        @Override
        public void init(WebConnection conection) {
            try {
                ServletInputStream input = conection.getInputStream();
                ServletOutputStream output = conection.getOutputStream();
                TestUpgradeReadListener readListener = new TestUpgradeReadListener(input, output);
                input.setReadListener(readListener);
                output.flush();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * A HttpUpgradeHandler used by testUpgrade3 method.
     */
    public static class TestUpgrade3HttpUpgradeHandler implements HttpUpgradeHandler {

        /**
         * Constructor.
         *
         * @throws IllegalAccessException on purpose.
         */
        public TestUpgrade3HttpUpgradeHandler() throws IllegalAccessException {
            throw new IllegalAccessException();
        }

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }

    /**
     * A HttpUpgradeHandler used by the testUpgrade2 method.
     */
    public static class TestUpgradeHttpUpgradeHandler implements HttpUpgradeHandler {

        /**
         * Constructor.
         */
        public TestUpgradeHttpUpgradeHandler() {
        }

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }

    /**
     * A ReadListener used by the testUpgrade method.
     */
    public static class TestUpgradeReadListener implements ReadListener {

        /**
         * Stores the input stream.
         */
        private ServletInputStream inputStream = null;

        /**
         * Stores the output stream.
         */
        private ServletOutputStream outputStream = null;

        /**
         * Stores the DELIMITER constant.
         */
        private static final String DELIMITER = "/";

        /**
         * Constructor.
         *
         * @param inputStream the input steam.
         * @param outputStream the output stream.
         */
        public TestUpgradeReadListener(ServletInputStream inputStream, ServletOutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void onDataAvailable() {
            try {
                StringBuilder sb = new StringBuilder();
                int len = -1;
                byte b[] = new byte[1024];
                while (inputStream.isReady() && (len = inputStream.read(b)) != -1) {
                    String data = new String(b, 0, len);
                    sb.append(data);
                }
                outputStream.println(DELIMITER + sb.toString());
                outputStream.flush();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void onAllDataRead() {
            try {
                outputStream.close();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void onError(final Throwable t) {
            t.printStackTrace();
        }
    }
}
