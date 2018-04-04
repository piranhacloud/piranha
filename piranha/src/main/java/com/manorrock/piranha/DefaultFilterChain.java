/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The default FilterChain.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultFilterChain implements FilterChain {

    /**
     * Stores the filter.
     */
    private Filter filter;

    /**
     * Stores the next filter chain.
     */
    private FilterChain nextFilterChain;

    /**
     * Stores the servlet.
     */
    private Servlet servlet;

    /**
     * Constructor.
     */
    public DefaultFilterChain() {
    }

    /**
     * Constructor.
     *
     * @param servlet the servlet.
     */
    public DefaultFilterChain(Servlet servlet) {
        this.servlet = servlet;
    }

    /**
     * Constructor.
     *
     * @param filter the filter.
     * @param nextFilterChain the next filter chain.
     */
    public DefaultFilterChain(Filter filter, FilterChain nextFilterChain) {
        this.filter = filter;
        this.nextFilterChain = nextFilterChain;
    }

    /**
     * Process the request.
     *
     * @param request the request.
     * @param response the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (filter != null) {
            filter.doFilter(request, response, nextFilterChain);
        } else if (servlet != null) {
            servlet.service(request, response);
        }
    }
}
