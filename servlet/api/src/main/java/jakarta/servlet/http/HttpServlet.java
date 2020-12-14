/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package jakarta.servlet.http;

import java.io.IOException;
import jakarta.servlet.GenericServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * The HttpServlet API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class HttpServlet extends GenericServlet {

    /**
     * Constructor.
     */
    public HttpServlet() {
    }

    /**
     * Get the last modified header.
     *
     * @param request the request.
     * @return the last modified, or -1 if not known.
     */
    protected long getLastModified(HttpServletRequest request) {
        return -1;
    }

    /**
     * Handle a DELETE request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * Handle a GET request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    /**
     * Handle a HEAD request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * Handle a POST request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * Handle a PUT request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * Handle an OPTIONS request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * Handle a TRACE request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    protected void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    /**
     * Process the HTTP request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getMethod();
        switch (method) {
            case "GET":
                doGet(request, response);
                break;
            case "HEAD":
                doHead(request, response);
                break;
            case "POST":
                doPost(request, response);
                break;
            case "PUT":
                doPut(request, response);
                break;
            case "DELETE":
                doDelete(request, response);
                break;
            case "OPTIONS":
                doOptions(request, response);
                break;
            case "TRACE":
                doTrace(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "HTTP method not implemented");
        }
    }

    /**
     * Process the request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        try {
            service((HttpServletRequest) request, (HttpServletResponse) response);
        } catch (ClassCastException cce) {
            throw new ServletException("Either request or response is not HTTP based");
        }
    }
}
