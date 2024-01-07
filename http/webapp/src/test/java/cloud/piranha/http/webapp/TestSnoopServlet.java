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
package cloud.piranha.http.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Principal;

/**
 * A test Snoop Servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestSnoopServlet extends HttpServlet {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Processes the request.
     *
     * @param request the servlet request.
     * @param response the servlet response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getQueryString() == null) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println(" <head>");
            out.println("  <title>Snoop</title>");
            out.println(" </head>");
            out.println(" <body>");
            out.println("  <h1>Snoop</h1>");
            out.println("   <table>");
            out.println("    <tr><td>Attribute Names:</td><td>" + request.getAttributeNames() + "</td></tr>");
            out.println("<tr><td>Dispatcher Type:</td><td>" + request.getDispatcherType() + "</td></tr>");
            out.println("<tr><td>Parameter Map:</td><td>" + request.getParameterMap() + "</td></tr>");
            out.println("<tr><td>Parameter Names:</td><td>" + request.getParameterNames() + "</td></tr>");
            out.println("<tr><td>Requested Session Id:</td><td>" + request.getRequestedSessionId() + "</td></tr>");
            out.println("<tr><td>Is Requested Session Id From Cookie:</td><td>" + request.isRequestedSessionIdFromCookie() + "</td></tr>");
            out.println("<tr><td>Is Requested Session Id From URL:</td><td>" + request.isRequestedSessionIdFromURL() + "</td></tr>");
            out.println("</table>");
            out.println("<b>Attributes</b>");
            Enumeration<String> attributeNames = request.getAttributeNames();
            out.println("<table>");
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement();
                out.println("<tr><td>" + name + "</td><td>" + request.getAttribute(name) + "</td></tr>");
            }
            out.println("</table>");
            out.println("<b>Parameters</b>");
            Enumeration<String> parameterNames = request.getParameterNames();
            out.println("<table>");
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                out.println("<tr><td>" + name + "</td><td>" + Arrays.toString(request.getParameterValues(name)) + "</td></tr>");
            }
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } else {
            switch (request.getQueryString()) {
                case "getAuthType" -> testGetAuthType(request, response);
                case "getCharacterEncoding" -> testGetCharacterEncoding(request, response);
                case "getContentLength" -> testGetContentLength(request, response);
                case "getContentType" -> testGetContentType(request, response);
                case "getContextPath" -> testGetContextPath(request, response);
                case "getCookies" -> testGetCookies(request, response);
                case "getDateHeader" -> testGetDateHeader(request, response);
                case "getHeader" -> testGetHeader(request, response);
                case "getHeaderNames" -> testGetHeaderNames(request, response);
                case "getIntHeader" -> testGetIntHeader(request, response);
                case "getLocalAddr" -> testGetLocalAddr(request, response);
                case "getLocalName" -> testGetLocalName(request, response);
                case "getLocalPort" -> testGetLocalPort(request, response);
                case "getLocale" -> testGetLocale(request, response);
                case "getLocales" -> testGetLocales(request, response);
                case "getMethod" -> testGetMethod(request, response);
                case "getParts" -> testGetParts(request, response);
                case "getPathInfo" -> testGetPathInfo(request, response);
                case "getPathTranslated" -> testGetPathTranslated(request, response);
                case "getProtocol" -> testGetProtocol(request, response);
                case "getQueryString" -> testGetQueryString(request, response);
                case "getRemoteAddr" -> testGetRemoteAddr(request, response);
                case "getRemoteHost" -> testGetRemoteHost(request, response);
                case "getRemotePort" -> testGetRemotePort(request, response);
                case "getRemoteUser" -> testGetRemoteUser(request, response);
                case "getRequestedSessionId" -> testGetRequestedSessionId(request, response);
                case "getRequestURI" -> testGetRequestURI(request, response);
                case "getRequestURL" -> testGetRequestURL(request, response);
                case "getScheme" -> testGetScheme(request, response);
                case "getServerName" -> testGetServerName(request, response);
                case "getServerPort" -> testGetServerPort(request, response);
                case "getServletPath" -> testGetServletPath(request, response);
                case "getSession" -> testGetSession(request, response);
                case "getUserPrincipal" -> testGetUserPrincipal(request, response);
                case "isAsyncStarted" -> testIsAsyncStarted(request, response);
                case "isAsyncSupported" -> testIsAsyncSupported(request, response);
                case "isRequestedSessionIdFromCookie" -> testIsRequestedSessionIdFromCookie(request, response);
                case "isRequestedSessionIdFromURL" -> testIsRequestedSessionIdFromURL(request, response);
                case "isRequestedSessionIdValid" -> testIsRequestedSessionIdValid(request, response);
                case "isSecure" -> testIsSecure(request, response);
            }
            if (request.getQueryString().contains("getParameter")) {
                testGetParameter(request, response);
            }
        }
    }

    /**
     * Handles the GET request.
     *
     * @param request the servlet request.
     * @param response the servlet response.
     * @throws IOException when an I/O error occurs
     * @throws ServletException when a servlet error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * Handles the POST request.
     *
     * @param request the servlet request.
     * @param response the servlet response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        processRequest(request, response);
    }

    /**
     * Test getAuthType method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetAuthType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authType = request.getAuthType();
        PrintWriter writer = response.getWriter();
        writer.println(authType);
    }

    /**
     * Test getCharacterEncoding method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetCharacterEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String characterEncoding = request.getCharacterEncoding();
        PrintWriter writer = response.getWriter();
        writer.println(characterEncoding);
    }

    /**
     * Test getContentLength method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetContentLength(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int contentLength = request.getContentLength();
        PrintWriter writer = response.getWriter();
        writer.println(contentLength);
    }

    /**
     * Test getContentType method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetContentType(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentType = request.getContentType();
        PrintWriter writer = response.getWriter();
        writer.println(contentType);
    }

    /**
     * Test getContextPath method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetContextPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contextPath = request.getContextPath();
        PrintWriter writer = response.getWriter();
        writer.println(contextPath);
    }

    /**
     * Test getCookies method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetCookies(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie = request.getCookies()[0];
        PrintWriter writer = response.getWriter();
        writer.println(cookie.getValue());
    }

    /**
     * Test getDateHeader method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetDateHeader(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long dateHeader = request.getDateHeader("MY_DATE");
        PrintWriter writer = response.getWriter();
        writer.println(dateHeader);
    }

    /**
     * Test getHeader method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetHeader(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String value = request.getHeader("MY_HEADER");
        PrintWriter writer = response.getWriter();
        writer.println(value);
    }

    /**
     * Test getHeaderNames method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetHeaderNames(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder builder = new StringBuilder();
        request.getHeaderNames().asIterator().forEachRemaining(header -> builder.append(header));
        PrintWriter writer = response.getWriter();
        writer.println(builder.toString());
    }

    /**
     * Test getIntHeader method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetIntHeader(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int intHeader = request.getIntHeader("MY_INT");
        PrintWriter writer = response.getWriter();
        writer.println(intHeader);
    }

    /**
     * Test getLocalAddr method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetLocalAddr(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String localAddr = request.getLocalAddr();
        PrintWriter writer = response.getWriter();
        writer.println(localAddr);
    }

    /**
     * Test getLocalName method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetLocalName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String localName = request.getLocalName();
        PrintWriter writer = response.getWriter();
        writer.println(localName);
    }

    /**
     * Test getLocalPort method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetLocalPort(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int localPort = request.getLocalPort();
        PrintWriter writer = response.getWriter();
        writer.println(localPort);
    }

    /**
     * Test getLocale method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetLocale(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String locale = request.getLocale().toString();
        PrintWriter writer = response.getWriter();
        writer.println(locale);
    }

    /**
     * Test getLocales method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetLocales(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder locales = new StringBuilder();
        request.getLocales().asIterator().forEachRemaining(locale -> locales.append(locale).append(","));
        PrintWriter writer = response.getWriter();
        writer.println(locales.toString());
    }

    /**
     * Test getMethod method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetMethod(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        PrintWriter writer = response.getWriter();
        writer.println(method);
    }

    /**
     * Test getParameter method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetParameter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String value = request.getParameter("parameter");
        PrintWriter writer = response.getWriter();
        writer.println(value);
    }
    
    /**
     * Test getParts method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetParts(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        try {
            String noParts = Boolean.toString(request.getParts().isEmpty());
            writer.println(noParts);
        } catch(ServletException se) {
            writer.println("FAILED");
        }
    }

    /**
     * Test getPathInfo method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetPathInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        PrintWriter writer = response.getWriter();
        writer.println(pathInfo);
    }

    /**
     * Test getPathTranslated method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetPathTranslated(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathTranslated = request.getPathTranslated();
        PrintWriter writer = response.getWriter();
        writer.println(pathTranslated);
    }

    /**
     * Test getProtocol method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetProtocol(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String protocol = request.getProtocol();
        PrintWriter writer = response.getWriter();
        writer.println(protocol);
    }

    /**
     * Test getQueryString method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetQueryString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String queryString = request.getQueryString();
        PrintWriter writer = response.getWriter();
        writer.println(queryString);
    }

    /**
     * Test getRemoteAddr method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetRemoteAddr(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteAddr = request.getRemoteAddr();
        PrintWriter writer = response.getWriter();
        writer.println(remoteAddr);
    }

    /**
     * Test getRemoteHost method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetRemoteHost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteHost = request.getRemoteHost();
        PrintWriter writer = response.getWriter();
        writer.println(remoteHost);
    }

    /**
     * Test getRemotePort method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetRemotePort(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int remotePort = request.getRemotePort();
        PrintWriter writer = response.getWriter();
        writer.println(remotePort);
    }

    /**
     * Test getRemoteUser method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetRemoteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String remoteUser = request.getRemoteUser();
        PrintWriter writer = response.getWriter();
        writer.println(remoteUser);
    }

    /**
     * Test getRequestURI method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetRequestURI(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        PrintWriter writer = response.getWriter();
        writer.println(requestURI);
    }

    /**
     * Test getRequestURL method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetRequestURL(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURL = request.getRequestURL().toString();
        PrintWriter writer = response.getWriter();
        writer.println(requestURL);
    }

    /**
     * Test getRequestedSessionId method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetRequestedSessionId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestedSessionId = request.getRequestedSessionId();
        PrintWriter writer = response.getWriter();
        writer.println(requestedSessionId);
    }

    /**
     * Test getScheme method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetScheme(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String scheme = request.getScheme();
        PrintWriter writer = response.getWriter();
        writer.println(scheme);
    }

    /**
     * Test getServerName method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetServerName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String serverName = request.getServerName();
        PrintWriter writer = response.getWriter();
        writer.println(serverName);
    }

    /**
     * Test getServerPort method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetServerPort(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int serverPort = request.getServerPort();
        PrintWriter writer = response.getWriter();
        writer.println(serverPort);
    }

    /**
     * Test getServletPath method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetServletPath(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String servletPath = request.getServletPath();
        PrintWriter writer = response.getWriter();
        writer.println(servletPath);
    }

    /**
     * Test getSession method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetSession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long creationTime = request.getSession().getCreationTime();
        PrintWriter writer = response.getWriter();
        writer.println(creationTime);
    }
    
    /**
     * Test getUserPrincipal method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testGetUserPrincipal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Principal userPrincipal = request.getUserPrincipal();
        PrintWriter writer = response.getWriter();
        writer.println(userPrincipal);
    }

    /**
     * Test isAsyncStarted method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testIsAsyncStarted(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isAsyncStarted = request.isAsyncStarted();
        PrintWriter writer = response.getWriter();
        writer.println(isAsyncStarted);
    }

    /**
     * Test isAsyncSupported method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testIsAsyncSupported(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isAsyncSupported = request.isAsyncSupported();
        PrintWriter writer = response.getWriter();
        writer.println(isAsyncSupported);
    }

    /**
     * Test isRequestedSessionIdFromCookie method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testIsRequestedSessionIdFromCookie(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isRequestedSessionIdFromCookie = request.isRequestedSessionIdFromCookie();
        PrintWriter writer = response.getWriter();
        writer.println(isRequestedSessionIdFromCookie);
    }

    /**
     * Test isRequestedSessionIdFromURL method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testIsRequestedSessionIdFromURL(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isRequestedSessionIdFromURL = request.isRequestedSessionIdFromURL();
        PrintWriter writer = response.getWriter();
        writer.println(isRequestedSessionIdFromURL);
    }

    /**
     * Test isRequestedSessionIdValid method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testIsRequestedSessionIdValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isRequestedSessionIdValid = request.isRequestedSessionIdValid();
        PrintWriter writer = response.getWriter();
        writer.println(isRequestedSessionIdValid);
    }

    /**
     * Test isSecure method.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     */
    private void testIsSecure(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isSecure = request.isSecure();
        PrintWriter writer = response.getWriter();
        writer.println(isSecure);
    }
}
