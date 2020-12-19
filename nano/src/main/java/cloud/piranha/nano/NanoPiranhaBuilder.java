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

import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.resource.DirectoryResource;
import cloud.piranha.resource.api.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.Filter;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;

/**
 * The builder so you can easily build instances of
 * {@link cloud.piranha.nano.NanoPiranha}.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @see cloud.piranha.nano.NanoPiranha
 */
public class NanoPiranhaBuilder {

    /**
     * Stores the filter init parameters map.
     */
    private final HashMap<String, HashMap<String, String>> filterInitParameters;

    /**
     * Stores the filter map.
     */
    private final LinkedHashMap<String, Filter> filters;

    /**
     * Stores the resources.
     */
    private final List<Resource> resources;

    /**
     * Stores the servlet init parameters.
     */
    private final HashMap<String, String> servletInitParameters;

    /**
     * Stores the servlet.
     */
    private Servlet servlet;

    /**
     * Stores the servlet name.
     */
    private String servletName;

    /**
     * Stores the web application.
     */
    private WebApplication webApplication;

    /**
     * Constructor.
     */
    public NanoPiranhaBuilder() {
        filterInitParameters = new HashMap<>();
        filters = new LinkedHashMap<>();
        resources = new ArrayList<>();
        servletInitParameters = new HashMap<>();
    }

    /**
     * Build Piranha Nano.
     *
     * @return our instance of Piranha Nano.
     */
    public NanoPiranha build() {
        NanoPiranha piranha = new NanoPiranha();
        if (webApplication == null) {
            webApplication = new DefaultWebApplication();
        }
        piranha.setWebApplication(webApplication);
        resources.forEach(resource -> webApplication.addResource(resource));
        filters.entrySet().forEach(entry -> {
            String filterName = entry.getKey();
            Filter filter = entry.getValue();
            NanoFilterConfig filterConfig = new NanoFilterConfig(piranha.getWebApplication());
            filterConfig.setFilterName(filterName);
            Map<String, String> initParameters = filterInitParameters.get(filterName);
            if (initParameters != null) {
                initParameters.entrySet().forEach(initParameter -> {
                    String name = initParameter.getKey();
                    String value = initParameter.getValue();
                    filterConfig.setInitParameter(name, value);
                });
            }
            try {
                filter.init(filterConfig);
            } catch (ServletException se) {
                throw new RuntimeException("Unable to initialize filter", se);
            }
            piranha.addFilter(filter);
        });
        if (servlet != null) {
            NanoServletConfig servletConfig = new NanoServletConfig(piranha.getWebApplication());
            servletConfig.setServletName(servletName);
            servletInitParameters.entrySet().forEach(entry -> {
                String name = entry.getKey();
                String value = entry.getValue();
                servletConfig.setInitParameter(name, value);
            });
            try {
                servlet.init(servletConfig);
            } catch (ServletException se) {
                throw new RuntimeException("Unable to initialize servlet", se);
            }
            piranha.setServlet(servlet);
        }
        return piranha;
    }

    /**
     * Add a directory resource.
     *
     * @param directory the directory resource.
     * @return the builder.
     */
    public NanoPiranhaBuilder directoryResource(String directory) {
        resources.add(new DirectoryResource(directory));
        return this;
    }

    /**
     * Add a filter.
     *
     * @param filterName the filter name.
     * @param filter the filter.
     * @return the builder.
     */
    public NanoPiranhaBuilder filter(String filterName, Filter filter) {
        filters.put(filterName, filter);
        return this;
    }

    /**
     * Set a filter init parameter.
     *
     * @param filterName the filter name.
     * @param name the name.
     * @param value the value.
     * @return the builder.
     */
    public NanoPiranhaBuilder filterInitParam(String filterName, String name, String value) {
        HashMap<String, String> initParameters = filterInitParameters.get(filterName);
        if (initParameters == null) {
            initParameters = new HashMap<>();
            filterInitParameters.put(filterName, initParameters);
        }
        initParameters.put(name, value);
        return this;
    }

    /**
     * Set the Servlet.
     *
     * @param servletName the Servlet name.
     * @param servlet the Servlet.
     * @return the builder.
     */
    public NanoPiranhaBuilder servlet(String servletName, Servlet servlet) {
        this.servlet = servlet;
        this.servletName = servletName;
        return this;
    }

    /**
     * Set a servlet init parameter.
     *
     * @param servletName the servlet name.
     * @param name the name.
     * @param value the value.
     * @return the builder.
     */
    public NanoPiranhaBuilder servletInitParam(String servletName, String name, String value) {
        if (servletName.equals(this.servletName)) {
            servletInitParameters.put(name, value);
        }
        return this;
    }

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     * @return the builder.
     */
    public NanoPiranhaBuilder webApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
        return this;
    }
}
