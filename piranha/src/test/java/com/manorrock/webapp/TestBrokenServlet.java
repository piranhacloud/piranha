/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * A test broken servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestBrokenServlet extends HttpServlet {

    /**
     * Initialize the servlet.
     *
     * @param servletConfig the servlet config.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        DefaultServletEnvironment environment = (DefaultServletEnvironment) servletConfig;
        environment.getWebApplication().setAttribute("Broken Servlet", true);
        throw new ServletException("Broken Servlet");
    }
}
