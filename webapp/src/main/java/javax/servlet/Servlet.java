/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.io.IOException;

/**
 * The Servlet API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface Servlet {

    /**
     * Destroy the servlet.
     */
    public void destroy();

    /**
     * Get the servlet config.
     *
     * @return the servlet config.
     */
    public ServletConfig getServletConfig();

    /**
     * Get the servlet information.
     *
     * @return the servlet information.
     */
    public String getServletInfo();

    /**
     * Initialize the servlet.
     *
     * @param servletConfig the servlet config.
     * @throws ServletException when a servlet error occurs.
     */
    public void init(ServletConfig servletConfig) throws ServletException;

    /**
     * Process the request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException;
}
