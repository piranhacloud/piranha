/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package cloud.piranha;

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
