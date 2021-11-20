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
package cloud.piranha.test.webapp.snoop;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.lang.System.Logger.Level;
import java.lang.System.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * A simple Snoop Servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SnoopServlet extends HttpServlet {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(SnoopServlet.class.getName());
    
    /**
     * Stores the '&lt;table>' constant.
     */
    private static final String TABLE_START = "<table>";

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            String template = """
            <html>
              <head>
                <title>Snoop</title>
              </head>
              <body>
                <h1>Snoop</h1>
                <table>
                  <tr><td>Attribute Names:</td><td>%s</td></tr>
                  <tr><td>Auth Type:</td><td>%s</td></tr>
                  <tr><td>Character Encoding:</td><td>%s</td></tr>
                  <tr><td>Class:</td><td>%s</td></tr>
                  <tr><td>Content Length:</td><td>%s</td></tr>
                  <tr><td>Content Type:</td><td>%s</td></tr>
                  <tr><td>Context Path:</td><td>%s</td></tr>
                  <tr><td>Cookies:</td><td>%s</td></tr>
                        """;
            out.println(String.format(template,
                    request.getAttributeNames(),
                    request.getAuthType(),
                    request.getCharacterEncoding(),
                    request.getClass(),
                    request.getContentLength(),
                    request.getContentType(),
                    request.getContextPath(),
                    Arrays.toString(request.getCookies())
            ));
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
            if (request.getContentType() != null && request.getContentType().contains("multipart/form-data")) {
                out.println("<tr><td>Parts:</td><td>" + request.getParts() + "</td></tr>");
            }
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
            out.println("<tr><td>User Principal:</td><td>" + request.getUserPrincipal() + "</td></tr>");
            out.println("<tr><td>Is Async Started:</td><td>" + request.isAsyncStarted() + "</td></tr>");
            out.println("<tr><td>Is Async Supported:</td><td>" + request.isAsyncSupported() + "</td></tr>");
            out.println("<tr><td>Is Requested Session Id From Cookie:</td><td>" + request.isRequestedSessionIdFromCookie() + "</td></tr>");
            out.println("<tr><td>Is Requested Session Id From URL:</td><td>" + request.isRequestedSessionIdFromURL() + "</td></tr>");
            out.println("<tr><td>Is Requested Session Id From Url:</td><td>" + request.isRequestedSessionIdFromUrl() + "</td></tr>");
            out.println("<tr><td>Is Secure:</td><td>" + request.isSecure() + "</td></tr>");
            out.println("</table>");
            out.println("<b>Attributes</b>");
            out.println(collectRequestAttributes(request));
            out.println("<b>Cookies</b>");
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                out.println(TABLE_START);
                for (int i = 0; i < cookies.length; i++) {
                    out.println("<tr><td>" + cookies[i].getName() + "</td><td>" + cookies[i].getValue() + "</td></tr>");
                }
                out.println("</table>");
            }
            out.println("<b>Headers</b>");
            Enumeration<String> headerNames = request.getHeaderNames();
            out.println(TABLE_START);
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                out.println("<tr><td>" + name + "</td><td>" + request.getHeader(name) + "</td></tr>");
            }
            out.println("</table>");
            out.println("<b>Locales</b>");
            Enumeration<Locale> locales = request.getLocales();
            out.println(TABLE_START);
            while (locales.hasMoreElements()) {
                Locale locale = locales.nextElement();
                out.println("<tr><td>" + locale + "</td></tr>");
            }
            out.println("</table>");
            out.println("<b>Parameters</b>");
            Enumeration<String> parameterNames = request.getParameterNames();
            out.println(TABLE_START);
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                out.println("<tr><td>" + name + "</td><td>" + Arrays.toString(request.getParameterValues(name)) + "</td></tr>");
            }
            out.println("</table>");
            out.println("<b>Session</b>");
            HttpSession session = request.getSession(true);
            out.println(TABLE_START);
            out.println("<tr><td>Creation Time</td><td>" + session.getCreationTime() + "</td></td>");
            out.println("<tr><td>Last Accessed Time</td><td>" + session.getLastAccessedTime() + "</td></td>");
            out.println("<tr><td>Servlet Context</td><td>" + session.getServletContext() + "</td></td>");
            out.println("<tr><td>Max Inactive Interval</td><td>" + session.getMaxInactiveInterval() + "</td></td>");
            out.println("<tr><td>Session Context</td><td>" + session.getSessionContext() + "</td></td>");
            out.println("<tr><td>Id</td><td>" + session.getId() + "</td></td>");
            out.println("<tr><td>Is New</td><td>" + session.isNew() + "</td></td>");
            out.println("</table>");
            out.println("<b>Session Attributes</b>");
            out.println(TABLE_START);
            session.setAttribute("TEST", "TEST");
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                out.println("<tr><td>" + name + "</td><td>" + session.getAttribute(name) + "</td></tr>");
            }
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * Collect the request attributes.
     *
     * @param request the request attributes.
     * @return the HTML for the request attributes.
     */
    private String collectRequestAttributes(HttpServletRequest request) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        StringBuilder result = new StringBuilder();
        result.append(TABLE_START);
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            result.append("<tr><td>")
                    .append(name)
                    .append("</td><td>")
                    .append(request.getAttribute(name))
                    .append("</td></tr>");
        }
        result.append("</table>");
        return result.toString();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ServletException | IOException e) {
            LOGGER.log(Level.ERROR, "Unable to process request", e);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ServletException | IOException e) {
            LOGGER.log(Level.ERROR, "Unable to process request", e);
        }
    }
}
