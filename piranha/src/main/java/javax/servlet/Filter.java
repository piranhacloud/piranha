/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.io.IOException;

/**
 * The Filter API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface Filter {

    /**
     * Destroy the filter.
     */
    void destroy();

    /**
     * Process the request.
     *
     * @param request the request.
     * @param response the response.
     * @param chain the chain.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;

    /**
     * Initialize the filter.
     *
     * @param filterConfig the filter configuration.
     * @throws ServletException when a servlet error occurs.
     */
    void init(FilterConfig filterConfig) throws ServletException;
}
