/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * A test Snoop filter.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestSnoopFilter implements Filter {

    /**
     * Destroy the filter.
     */
    @Override
    public void destroy() {
    }

    /**
     * Process the filter.
     *
     * @param request the request.
     * @param response the response.
     * @param chain the chain.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    }

    /**
     * Initialize the filter.
     *
     * @param filterConfig the filter configuration.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
