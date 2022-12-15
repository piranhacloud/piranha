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
import static jakarta.servlet.DispatcherType.INCLUDE;
import static jakarta.servlet.DispatcherType.REQUEST;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.WebConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for any WebApplicationRequest implementation.
 *
 * <p>
 * Note all these tests only use the public APIs of WebApplication,
 * WebApplicationRequest and WebApplicationResponse.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class WebApplicationRequestTest {

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
     * @return the web application responses.
     */
    protected abstract WebApplicationResponse createWebApplicationResponse();

    /**
     * Test authenticate method.
     */
    @Test
    void testAuthenticate() {
        try {
            WebApplication webApplication = createWebApplication();
            WebApplicationRequest request = createWebApplicationRequest();
            request.setWebApplication(webApplication);
            WebApplicationResponse response = createWebApplicationResponse();
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
            WebApplication webApplication = createWebApplication();
            WebApplicationRequest request = createWebApplicationRequest();
            request.setWebApplication(webApplication);
            WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.changeSessionId()));
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId2() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.getAsyncContext()));
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext2() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAttribute("name", "value");
        assertNotNull(request.getAttribute("name"));
    }

    /**
     * Test getAttributeNames method.
     */
    @Test
    void testGetAttributeNames() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
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
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getAuthType());
    }

    /**
     * Test getCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetCharacterEncoding() throws Exception {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getCharacterEncoding());
        request.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", request.getCharacterEncoding());
    }

    /**
     * Test getContentLength method.
     */
    @Test
    void testGetContentLength() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals(-1, request.getContentLength());
    }

    /**
     * Test getContentLengthLong method.
     *
     */
    @Test
    void testGetContentLengthLong() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals(-1L, request.getContentLengthLong());
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getContentType());
    }

    /**
     * Test getContextPath method.
     */
    @Test
    void testGetContextPath() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals("", request.getContextPath());
    }

    /**
     * Test getCookies method.
     */
    @Test
    void testGetCookies() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getCookies());
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    void testGetDateHeader() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals(-1L, request.getDateHeader("notfound"));
    }

    /**
     * Test getDispatcherType.
     *
     */
    @Test
    void testGetDispatcherType() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals(REQUEST, request.getDispatcherType());
    }

    /**
     * Test getHeader method.
     */
    @Test
    void testGetHeader() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getHeader("notfound"));
    }

    /**
     * Test getHeaderNames method.
     */
    @Test
    void testGetHeaderNames() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getHeaderNames());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    void testGetHeaders() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getHeaders("notfound"));
    }

    /**
     * Test getHttpServletMapping method.
     */
    @Test
    void testGetHttpServletMapping() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getHttpServletMapping());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream() throws Exception {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getInputStream());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream2() throws Exception {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getInputStream());
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.getReader()));
    }

    /**
     * Test getIntHeader method.
     *
     */
    @Test
    void testGetIntHeader() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals(-1, request.getIntHeader("notfound"));
    }

    /**
     * Test getLocalAddr method.
     */
    @Test
    void testGetLocalAddr() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getLocalAddr());
    }

    /**
     * Test getLocalName method.
     */
    @Test
    void testGetLocalName() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getLocalName());
    }

    /**
     * Test getLocalPort method.
     */
    @Test
    void testGetLocalPort() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals(0, request.getLocalPort());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocale() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getLocale());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocales() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getLocales());
    }

    /**
     * Test getMethod method.
     */
    @Test
    void testGetMethod() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals("GET", request.getMethod());
    }

    /**
     * Test getMultipartConfig method.
     */
    @Test
    void testGetMultipartConfig() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getMultipartConfig());
    }

    /**
     * Test getParameter method.
     */
    @Test
    void testGetParameter() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getParameter("notfound"));
    }

    /**
     * Test getParameterMap.
     */
    @Test
    void testGetParameterMap() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getParameterMap());
    }

    /**
     * Test getParameterNames.
     */
    @Test
    void testGetParameterNames() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getParameterNames());
    }

    /**
     * Test getParameterValues.
     */
    @Test
    void testGetParameterValues() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getParameterValues("notfound"));
    }

    /**
     * Test getPart method.
     *
     */
    @Test
    void testGetPart() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertThrows(ServletException.class, () -> {
            request.getPart("notfound");
        });
    }

    /**
     * Test getParts method.
     *
     */
    @Test
    void testGetParts() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertThrows(ServletException.class, () -> {
            request.getParts();
        });
    }

    /**
     * Test getPathInfo method.
     */
    @Test
    void testGetPathInfo() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getPathInfo());
    }

    /**
     * Test getPathTranslated method.
     */
    @Test
    void testGetPathTranslated() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getPathTranslated());
    }

    /**
     * Test getProtocol method.
     */
    @Test
    void testGetProtocol() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals("HTTP/1.1", request.getProtocol());
    }

    /**
     * Test getProtocolRequestId method.
     */
    @Test
    void testGetProtocolRequestId() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals("", request.getProtocolRequestId());
    }

    /**
     * Test getQueryString method.
     */
    @Test
    void testGetQueryString() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getQueryString());
    }

    /**
     * Test getReader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetReader() throws Exception {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getReader());
    }

    /**
     * Test getRemoteAddr method.
     */
    @Test
    void testGetRemoteAddr() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getRemoteAddr());
    }

    /**
     * Test getRemoteHost method.
     */
    @Test
    void testGetRemoteHost() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getRemoteHost());
    }

    /**
     * Test getRemotePort method.
     */
    @Test
    void testGetRemotePort() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals(0, request.getRemotePort());
    }

    /**
     * Test getRemoteUser method.
     */
    @Test
    void testGetRemoteUser() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getRemoteUser());
    }

    /**
     * Test getRequestDispatcher method.
     *
     */
    @Test
    void testGetRequestDispatcher() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertNotNull(request.getRequestDispatcher("/test"));
    }

    /**
     * Test getRequestId method.
     */
    @Test
    void testGetRequestId() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getRequestId());
    }

    /**
     * Test getRequestURI method.
     */
    @Test
    void testGetRequestURI() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getRequestURI());
    }

    /**
     * Test getRequestURL method.
     */
    @Test
    void testGetRequestURL() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getRequestURL());
    }

    /**
     * Test getRequestedSessionId method.
     */
    @Test
    void testGetRequestedSessionId() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getRequestedSessionId());
    }

    /**
     * Test getScheme method.
     */
    @Test
    void testGetScheme() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals("http", request.getScheme());
    }

    /**
     * Test getServerName method.
     */
    @Test
    void testGetServerName() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals("localhost", request.getServerName());
    }

    /**
     * Test getServerPort method.
     */
    @Test
    void testGetServerPort() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals(80, request.getServerPort());
    }

    /**
     * Test getServletConnection method.
     */
    @Test
    void testGetServletConnection() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.getServletConnection());
    }

    /**
     * Test getServletContext method.
     */
    @Test
    void testGetServletContext() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertNotNull(request.getServletContext());
    }

    /**
     * Test getServletPath method.
     */
    @Test
    void testGetServletPath() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertEquals("", request.getServletPath());
    }

    /**
     * Test getSession method.
     */
    @Test
    void testGetSession() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplication webApplication = createWebApplication();
        ServletRegistration.Dynamic dynamic = webApplication.addServlet("session",
                new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
                response.setContentType("text/plain");
                try ( PrintWriter out = response.getWriter()) {
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

        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/session");

        WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplicationRequest request = createWebApplicationRequest();
        Map<String, String> trailerFields = request.getTrailerFields();
        assertTrue(trailerFields.isEmpty());
    }

    /**
     * Test getUpgradeHandler method.
     */
    @Test
    void testGetUpgradeHandler() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getUpgradeHandler());
    }

    /**
     * Test getUserPrincipal method.
     */
    @Test
    void testGetUserPrincipal() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.getUserPrincipal());
    }

    /**
     * Test isAsyncStarted method.
     */
    @Test
    void testIsAsyncStarted() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertFalse(request.isAsyncStarted());
    }

    /**
     * Test isAsyncSupported method.
     */
    @Test
    void testIsAsyncSupported() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertFalse(request.isAsyncSupported());
    }

    /**
     * Test isRequestedSessionIdFromCookie method.
     */
    @Test
    void testIsRequestedSessionIdFromCookie() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromCookie());
    }

    /**
     * Test isRequestedSessionIdFromURL method.
     */
    @Test
    void testIsRequestedSessionIdFromURL() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdFromURL());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test
    void testIsRequestedSessionIdValid() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertFalse(request.isRequestedSessionIdValid());
    }

    /**
     * Test isRequestedSessionIdValid method.
     */
    @Test
    void testIsRequestedSessionIdValid2() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
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
        WebApplicationRequest request = createWebApplicationRequest();
        assertFalse(request.isSecure());
    }

    /**
     * Test isTrailerFieldsReady method.
     */
    @Test
    void testIsTrailerFieldsReady() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertTrue(request.isTrailerFieldsReady());
    }

    /**
     * Test isUpgraded method.
     */
    @Test
    void testIsUpgraded() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertFalse(request.isUpgraded());
    }

    /**
     * Test isUserInRole method.
     */
    @Test
    void testIsUserInRole() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertFalse(request.isUserInRole("notmatched"));
    }

    /**
     * Test login method.
     */
    @Test
    void testLogin() {
        try {
            WebApplication webApplication = createWebApplication();
            WebApplicationRequest request = createWebApplicationRequest();
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
            WebApplication webApplication = createWebApplication();
            WebApplicationRequest request = createWebApplicationRequest();
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
        WebApplicationRequest request = createWebApplicationRequest();
        assertNull(request.newPushBuilder());
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    void testRemoveAttribute() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
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
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAttribute("name", "value");
        assertNotNull(request.getAttribute("name"));
    }

    /**
     * Test setAuthType method.
     */
    @Test
    void testSetAuthType() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
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
        WebApplicationRequest request = createWebApplicationRequest();
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
        WebApplicationRequest request = createWebApplicationRequest();
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
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(assertThrows(UnsupportedEncodingException.class,
                () -> request.setCharacterEncoding("doesnotexist")));
    }

    /**
     * Test setCharacterEncoding method.
     *
     */
    @Test
    void testSetCharacterEncoding4() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(assertThrows(UnsupportedEncodingException.class,
                () -> request.setCharacterEncoding(null)));
    }

    /**
     * Test setContextPath method.
     */
    @Test
    void testSetContextPath() {
        WebApplicationRequest request = createWebApplicationRequest();
        request.setContextPath("/contextPath");
        assertEquals("/contextPath", request.getContextPath());
    }

    /**
     * Test setDispatcherType method.
     */
    @Test
    void testSetDispatcherType() {
        WebApplicationRequest request = createWebApplicationRequest();
        request.setDispatcherType(INCLUDE);
        assertEquals(INCLUDE, request.getDispatcherType());
    }

    /**
     * Test setServletPath method.
     */
    @Test
    void testSetServletPath() {
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/servletPath");
        assertEquals("/servletPath", request.getServletPath());
    }

    /**
     * Test setUserPrincipal method.
     */
    @Test
    void testSetUserPrincipal() {
        WebApplicationRequest request = createWebApplicationRequest();
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
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertEquals(webApplication, request.getServletContext());
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertThrows(IllegalStateException.class, () -> {
            request.startAsync();
        });
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync2() {
        WebApplicationRequest request = createWebApplicationRequest();
        WebApplicationResponse response = createWebApplicationResponse();
        assertThrows(IllegalStateException.class, () -> {
            request.startAsync(request, response);
        });
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync3() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(request.startAsync());
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync4() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setAsyncSupported(false);
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.startAsync(request, response)));
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testUpgrade() throws Exception {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(request.upgrade(TestUpgradeHttpUpgradeHandler.class));
    }

    /**
     * Test upgrade method.
     *
     */
    @Test
    void testUpgrade2() {
        WebApplicationRequest request = createWebApplicationRequest();
        assertNotNull(assertThrows(ServletException.class,
                () -> request.upgrade(TestUpgrade2HttpUpgradeHandler.class)));
    }

    /**
     * A HttpUpgradeHandler used by testUpgrade method.
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
     * A HttpUpgradeHandler used by testUpgrade2 method.
     */
    public static class TestUpgrade2HttpUpgradeHandler implements HttpUpgradeHandler {

        /**
         * Constructor.
         *
         * @throws IllegalAccessException on purpose.
         */
        public TestUpgrade2HttpUpgradeHandler() throws IllegalAccessException {
            throw new IllegalAccessException();
        }

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }
}
