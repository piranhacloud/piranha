/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.io.IOException;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

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
