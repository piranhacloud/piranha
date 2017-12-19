/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.io.IOException;

/**
 * The FilterChain API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface FilterChain {

    /**
     * Process the request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException;
}
