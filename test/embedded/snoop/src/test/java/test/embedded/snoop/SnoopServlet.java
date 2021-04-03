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
package test.embedded.snoop;

import java.io.IOException;
import java.io.PrintWriter;
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
import jakarta.servlet.http.Part;
import java.util.Iterator;

/**
 * The Snoop Servlet for Piranha Embedded.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SnoopServlet extends HttpServlet {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(SnoopServlet.class.getName());

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
        response.setContentType("text/plain");
        try (PrintWriter out = response.getWriter()) {

            out.println("== HTTP Request ==");
            out.println();

            out.println("- Request line");
            out.println("Method: " + request.getMethod());
            out.println("Request-Target: " + request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()));
            out.println("HTTP-Version: " + request.getProtocol());
            out.println("- Request Headers");
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                out.println(name + ": " + request.getHeader(name) + "<br>");
            }
            out.println("-");
            out.println();
            
            out.println("== HttpServletRequest ==");
            out.println("- Request Attributes");
            Enumeration<String> attributeNames = request.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement();
                out.println(name + ": " + request.getAttribute(name));
            }
            out.println("-");
            out.println("Auth Type: " + request.getAuthType());
            out.println("Character Encoding: " + request.getCharacterEncoding());
            out.println("Class: " + request.getClass());
            out.println("Content Length: " + request.getContentLength());
            out.println("Content Type: " + request.getContentType());
            out.println("Context Path: " + request.getContextPath());
            out.println("- Cookies");
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    out.println(cookie.getName() + ": " + cookie.getValue());
                }
            }
            out.println("-");
            out.println("Dispatcher Type: " + request.getDispatcherType());
            out.println("Local Address: " + request.getLocalAddr());
            out.println("Local Name: " + request.getLocalName());
            out.println("Local Port: " + request.getLocalPort());
            out.println("Locale: " + request.getLocale());
            out.println("- Locales");
            Enumeration<Locale> locales = request.getLocales();
            while (locales.hasMoreElements()) {
                Locale locale = locales.nextElement();
                out.println(locale);
            }
            out.println("-");
            out.println("- Parameters");
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                out.print(name + ": ");
                String[] values = request.getParameterValues(name);
                for(int i=0; i<values.length; i++) {
                    out.print(values[i]);
                    if (i + 1 != values.length) {
                        out.print(", ");
                    }
                }
                out.println();
            }
            out.println("-");
            out.println("- Parts");
            Iterator<Part> parts = request.getParts().iterator();
            while(parts.hasNext()) {
                Part part = parts.next();
                System.out.println(part.getName());
            }
            out.println("-");
            out.println("Path Info: " + request.getPathInfo());
            out.println("Path Translated: " + request.getPathTranslated());
            out.println("Query String: " + request.getQueryString());
            out.println("Remote Address: " + request.getRemoteAddr());
            out.println("Remote Host: " + request.getRemoteHost());
            out.println("Remote Port: " + request.getRemotePort());
            out.println("Remote User: " + request.getRemoteUser());
            out.println("Request URI: " + request.getRequestURI());
            out.println("Request URL: " + request.getRequestURL());
            out.println("Requested Session Id: " + request.getRequestedSessionId());
            out.println("Scheme: " + request.getScheme());
            out.println("Server Name: " + request.getServerName());
            out.println("Server Port: " + request.getServerPort());
            out.println("Servlet Context: " + request.getServletContext());
            out.println("Servlet Path: " + request.getServletPath());
            out.println("User Principal: " + request.getUserPrincipal());
            out.println("Is Async Started: " + request.isAsyncStarted());
            out.println("Is Async Supported: " + request.isAsyncSupported());
            out.println("Is Requested Session Id From Cookie: " + request.isRequestedSessionIdFromCookie());
            out.println("Is Requested Session Id From URL: " + request.isRequestedSessionIdFromURL());
            out.println("Is Requested Session Id From Url: " + request.isRequestedSessionIdFromUrl());
            out.println("Is Secure: " + request.isSecure());
            out.println("- Session");
            HttpSession session = request.getSession(true);
            out.println("Creation Time: " + session.getCreationTime());
            out.println("Last Accessed Time: " + session.getLastAccessedTime());
            out.println("Servlet Context: " + session.getServletContext());
            out.println("Max Inactive Interval: " + session.getMaxInactiveInterval());
            out.println("Session Context: " + session.getSessionContext());
            out.println("Id: " + session.getId());
            out.println("Is New: " + session.isNew());
            out.println("-");
            out.println("- Session Attributes");
            session.setAttribute("TEST", "TEST");
            Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                out.println("" + name + ": " + session.getAttribute(name));
            }
        }
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
