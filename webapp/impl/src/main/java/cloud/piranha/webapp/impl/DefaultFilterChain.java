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
package cloud.piranha.webapp.impl;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletResponse;

import cloud.piranha.webapp.api.ServletInvocation;

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
     *
     */
    private ServletInvocation servletInvocation;

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
    public DefaultFilterChain(ServletInvocation servletInvocation, Servlet servlet) {
        this.servletInvocation = servletInvocation;
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
            request.setAttribute(DefaultServletEnvironment.class.getName(), servlet.getServletConfig());
            try {
                servlet.service(request, response);
            } finally {
                request.removeAttribute(DefaultServletEnvironment.class.getName());
            }
        } else if (servletInvocation != null && servletInvocation.isServletUnavailable()) {
            // We've reached the servlet, but the servlet is not available (for instance because
            // the init method failed)
            Exception exception;
            Throwable throwable = servletInvocation.getServletEnvironment().getUnavailableException();
            if (throwable instanceof Exception) {
                exception = (Exception) throwable;
            } else {
                exception = new UnavailableException("");
                exception.initCause(throwable);
            }

            if (response instanceof HttpServletResponse) {
                ((HttpServletResponse) response).setStatus(500);
            }

            request.setAttribute("piranha.request.exception", exception);
            throw new ServletException(exception);

        } else if (response instanceof HttpServletResponse) {
            ((HttpServletResponse) response).sendError(SC_NOT_FOUND);
        }
    }
}
