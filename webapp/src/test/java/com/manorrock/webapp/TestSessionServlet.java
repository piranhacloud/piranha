/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A test HTTP session Servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestSessionServlet extends HttpServlet {

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

    /**
     * Handles the GET request.
     *
     * @param request the servlet request.
     * @param response the servlet response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
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
