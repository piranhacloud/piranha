/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A test echo servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestEcho1Servlet extends HttpServlet {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Service the request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.getWriter().print("ECHO");
        response.getWriter().flush();
    }
}
