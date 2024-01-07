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
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        out.println("<tr><td>Auth Type:</td><td>" + request.getAuthType() + "</td></tr>");
        out.println("<tr><td>Character Encoding:</td><td>" + request.getCharacterEncoding() + "</td></tr>");
        out.println("<tr><td>Class:</td><td>" + request.getClass() + "</td></tr>");
        out.println("<tr><td>Content Length:</td><td>" + request.getContentLength() + "</td></tr>");
        out.println("<tr><td>Content Type:</td><td>" + request.getContentType() + "</td></tr>");
        out.println("<tr><td>Context Path:</td><td>" + request.getContextPath() + "</td></tr>");
        out.println("<tr><td>Cookies:</td><td>" + Arrays.toString(request.getCookies()) + "</td></tr>");
        out.println("<tr><td>Dispatcher Type:</td><td>" + request.getDispatcherType() + "</td></tr>");
        out.println("<tr><td>Header Names:</td><td>" + request.getHeaderNames() + "</td></tr>");
        out.println("<tr><td>Local Address:</td><td>" + request.getLocalAddr() + "</td></tr>");
        out.println("<tr><td>Local Name:</td><td>" + request.getLocalName() + "</td></tr>");
        out.println("<tr><td>Local Port:</td><td>" + request.getLocalPort() + "</td></tr>");
        out.println("<tr><td>Locale:</td><td>" + request.getLocale() + "</td></tr>");
        out.println("<tr><td>Locales:</td><td>" + request.getLocales() + "</td></tr>");
        out.println("<tr><td>Method:</td><td>" + request.getMethod() + "</td></tr>");
        out.println("<tr><td>Parameter Map:</td><td>" + request.getParameterMap() + "</td></tr>");
        out.println("<tr><td>Parameter Names:</td><td>" + request.getParameterNames() + "</td></tr>");
        out.println("<tr><td>Path Info:</td><td>" + request.getPathInfo() + "</td></tr>");
        out.println("<tr><td>Path Translated:</td><td>" + request.getPathTranslated() + "</td></tr>");
        out.println("<tr><td>Protocol:</td><td>" + request.getProtocol() + "</td></tr>");
        out.println("<tr><td>Query String:</td><td>" + request.getQueryString() + "</td></tr>");
        out.println("<tr><td>Remote Address:</td><td>" + request.getRemoteAddr() + "</td></tr>");
        out.println("<tr><td>Remote Host:</td><td>" + request.getRemoteHost() + "</td></tr>");
        out.println("<tr><td>Remote Port:</td><td>" + request.getRemotePort() + "</td></tr>");
        out.println("<tr><td>Remote User:</td><td>" + request.getRemoteUser() + "</td></tr>");
        out.println("<tr><td>Request URI:</td><td>" + request.getRequestURI() + "</td></tr>");
        out.println("<tr><td>Request URL:</td><td>" + request.getRequestURL() + "</td></tr>");
        out.println("<tr><td>Requested Session Id:</td><td>" + request.getRequestedSessionId() + "</td></tr>");
        out.println("<tr><td>Scheme:</td><td>" + request.getScheme() + "</td></tr>");
        out.println("<tr><td>Server Name:</td><td>" + request.getServerName() + "</td></tr>");
        out.println("<tr><td>Server Port:</td><td>" + request.getServerPort() + "</td></tr>");
        out.println("<tr><td>Servlet Context:</td><td>" + request.getServletContext() + "</td></tr>");
        out.println("<tr><td>Servlet Path:</td><td>" + request.getServletPath() + "</td></tr>");
        out.println("<tr><td>Session:</td><td>" + request.getSession() + "</td></tr>");
        out.println("<tr><td>User Principal:</td><td>" + request.getUserPrincipal() + "</td></tr>");
        out.println("<tr><td>Is Async Started:</td><td>" + request.isAsyncStarted() + "</td></tr>");
        out.println("<tr><td>Is Async Supported:</td><td>" + request.isAsyncSupported() + "</td></tr>");
        out.println("<tr><td>Is Requested Session Id From Cookie:</td><td>" + request.isRequestedSessionIdFromCookie() + "</td></tr>");
        out.println("<tr><td>Is Requested Session Id From URL:</td><td>" + request.isRequestedSessionIdFromURL() + "</td></tr>");
        out.println("<tr><td>Is Secure:</td><td>" + request.isSecure() + "</td></tr>");
        out.println("</table>");
        out.println("<b>Attributes</b>");
        Enumeration<String> attributeNames = request.getAttributeNames();
        out.println("<table>");
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            out.println("<tr><td>" + name + "</td><td>" + request.getAttribute(name) + "</td></tr>");
        }
        out.println("</table>");
        out.println("<b>Cookies</b>");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            out.println("<table>");
            for (Cookie cookie : cookies) {
                out.println("<tr><td>" + cookie.getName() + "</td><td>" + cookie.getValue() + "</td></tr>");
            }
            out.println("</table>");
        }
        out.println("<b>Headers</b>");
        Enumeration<String> headerNames = request.getHeaderNames();
        out.println("<table>");
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            out.println("<tr><td>" + name + "</td><td>" + request.getHeader(name) + "</td></tr>");
        }
        out.println("</table>");
        out.println("<b>Locales</b>");
        Enumeration<Locale> locales = request.getLocales();
        out.println("<table>");
        while (locales.hasMoreElements()) {
            Locale locale = locales.nextElement();
            out.println("<tr><td>" + locale + "</td></tr>");
        }
        out.println("<b>Parameters</b>");
        Enumeration<String> parameterNames = request.getParameterNames();
        out.println("<table>");
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            out.println("<tr><td>" + name + "</td><td>" + Arrays.toString(request.getParameterValues(name)) + "</td></tr>");
        }
        out.println("</table>");
        Enumeration<String> initParameterNames = getServletConfig().getInitParameterNames();
        out.println("<b>Servlet Config</b>");
        out.println("Init Parameters</b>");
        out.println("<table>");
        while (initParameterNames.hasMoreElements()) {
            String name = initParameterNames.nextElement();
            out.println("<tr><td>" + name + "</td><td>" + getServletConfig().getInitParameter(name) + "</td></tr>");
        }
        out.println("Servlet context: " + getServletConfig().getServletContext());
        out.println("Servlet name: " + getServletConfig().getServletName());
        out.println("</body>");
        out.println("</html>");
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
}
