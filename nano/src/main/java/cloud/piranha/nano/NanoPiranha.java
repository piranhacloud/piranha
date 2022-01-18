/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.nano;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.impl.DefaultWebApplication;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;

/**
 * The smallest version of Piranha in our lineup.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.core.api
 */
public class NanoPiranha implements Piranha {

    /**
     * Stores the filters.
     */
    private final LinkedList<Filter> filters;

    /**
     * Stores the servlet.
     */
    private Servlet servlet;

    /**
     * Stores the web application.
     */
    private WebApplication webApplication;

    /**
     * Constructor.
     */
    public NanoPiranha() {
        filters = new LinkedList<>();
        webApplication = new DefaultWebApplication();
    }

    /**
     * Add a filter.
     *
     * @param filter the filter.
     */
    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    /**
     * {@return the filters}
     */
    public List<Filter> getFilters() {
        return filters;
    }

    /**
     * {@return the servlet}
     */
    public Servlet getServlet() {
        return servlet;
    }

    /**
     * {@return the web application}
     */
    public WebApplication getWebApplication() {
        return webApplication;
    }

    @Override
    public void service(WebApplicationRequest request, WebApplicationResponse response)
            throws IOException, ServletException {
        Iterator<Filter> iterator = filters.descendingIterator();
        NanoFilterChain chain = new NanoFilterChain(servlet);
        while (iterator.hasNext()) {
            Filter filter = iterator.next();
            NanoFilterChain previousChain = chain;
            chain = new NanoFilterChain(filter, previousChain);
        }
        if (request instanceof NanoRequest nanoRequest) {
            nanoRequest.setWebApplication(webApplication);
        }
        if (response instanceof NanoResponse nanoResponse) {
            nanoResponse.setWebApplication(webApplication);
        }
        chain.doFilter(request, response);
        response.flushBuffer();
    }

    /**
     * Set the servlet.
     *
     * @param servlet the servlet.
     */
    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }
}
