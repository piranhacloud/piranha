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
package cloud.piranha.nano;

import cloud.piranha.api.Piranha;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * The smallest version of Piranha in our lineup.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.webapp.api
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
     * Get the filters.
     *
     * @return the filters.
     */
    public List<Filter> getFilters() {
        return filters;
    }

    /**
     * Get the servlet.
     *
     * @return the servlet.
     */
    public Servlet getServlet() {
        return servlet;
    }

    /**
     * Get the version.
     *
     * @return the version.
     */
    @Override
    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
    }

    /**
     * Get the web application.
     *
     * @return the web application.
     */
    public WebApplication getWebApplication() {
        return webApplication;
    }

    /**
     * Service.
     *
     * @param servletRequest the request.
     * @param servletResponse the response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    public void service(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IOException, ServletException {
        Iterator<Filter> iterator = filters.descendingIterator();
        NanoFilterChain chain = new NanoFilterChain(servlet);
        while (iterator.hasNext()) {
            Filter filter = iterator.next();
            NanoFilterChain previousChain = chain;
            chain = new NanoFilterChain(filter, previousChain);
        }
        if (servletRequest.getServletContext() == null
                && servletRequest instanceof NanoRequest) {
            NanoRequest nanoRequest = (NanoRequest) servletRequest;
            nanoRequest.setWebApplication(webApplication);
        }
        if (servletResponse instanceof NanoResponse) {
            NanoResponse nanoResponse = (NanoResponse) servletResponse;
            nanoResponse.setWebApplication(webApplication);
        }
        chain.doFilter(servletRequest, servletResponse);
        servletResponse.flushBuffer();
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
