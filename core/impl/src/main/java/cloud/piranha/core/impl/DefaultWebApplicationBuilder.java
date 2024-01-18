/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl;

import cloud.piranha.resource.api.Resource;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The DefaultWebApplication builder.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationBuilder {

    /**
     * Stores the filter init parameters map.
     */
    private final Map<String, Map<String, String>> filterInitParameters = new HashMap<>();

    /**
     * Stores the filters.
     */
    private final Map<String, Object> filters = new HashMap<>();

    /**
     * Stores the init parameters.
     */
    private final Map<String, String> initParams = new HashMap<>();

    /**
     * Stores the list of resources.
     */
    private final List<Resource> resources = new ArrayList<>();

    /**
     * Stores the servlet init parameters map.
     */
    private final Map<String, Map<String, String>> servletInitParameters = new HashMap<>();

    /**
     * Stores the servlet mappings.
     */
    private final Map<String, String> servletMappings = new HashMap<>();

    /**
     * Stores the servlets.
     */
    private final Map<String, Object> servlets = new HashMap<>();

    /**
     * Constructor.
     */
    public DefaultWebApplicationBuilder() {
    }

    /**
     * Build the web application.
     *
     * @return the web application.
     */
    public DefaultWebApplication build() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        for (Resource resource : resources) {
            webApplication.addResource(resource);
        }
        for (Map.Entry<String, Object> filterEntry : filters.entrySet()) {
            String name = filterEntry.getKey();
            Object value = filterEntry.getValue();
            if (value instanceof String className) {
                webApplication.addFilter(name, className);
            }
            if (value instanceof Class clazz) {
                webApplication.addFilter(name, clazz);
            }
            if (value instanceof Filter filter) {
                webApplication.addFilter(name, filter);
            }
            if (filterInitParameters.containsKey(name)) {
                Map<String, String> parameters = filterInitParameters.get(name);
                for (Map.Entry<String, String> initParameter : parameters.entrySet()) {
                    String initParameterName = initParameter.getKey();
                    String initParameterValue = initParameter.getValue();
                    FilterRegistration registration = webApplication.getFilterRegistration(name);
                    registration.setInitParameter(initParameterName, initParameterValue);
                }
            }
        }
        for (Map.Entry<String, String> entry : initParams.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            webApplication.setInitParameter(name, value);
        }
        for (Map.Entry<String, Object> entry : servlets.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String className) {
                webApplication.addServlet(name, className);
            }
            if (value instanceof Class clazz) {
                webApplication.addServlet(name, clazz);
            }
            if (value instanceof Servlet servlet) {
                webApplication.addServlet(name, servlet);
            }
            if (servletInitParameters.containsKey(name)) {
                Map<String, String> parameters = servletInitParameters.get(name);
                for (Map.Entry<String, String> initParameter : parameters.entrySet()) {
                    String initParameterName = initParameter.getKey();
                    String initParameterValue = initParameter.getValue();
                    ServletRegistration registration = webApplication.getServletRegistration(name);
                    registration.setInitParameter(initParameterName, initParameterValue);
                }
            }
        }
        for (Map.Entry<String, String> mappingEntry : servletMappings.entrySet()) {
            String servletName = mappingEntry.getKey();
            String mapping = mappingEntry.getValue();
            webApplication.addServletMapping(servletName, mapping);
        }
        return webApplication;
    }

    /**
     * Add a directory resource.
     *
     * @param directory the directory.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder directoryResource(String directory) {
        resources.add(new DirectoryResource(directory));
        return this;
    }

    /**
     * Add a filter.
     *
     * @param name the name.
     * @param className the class name.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder filter(String name, String className) {
        filters.put(name, className);
        return this;
    }

    /**
     * Add a filter.
     *
     * @param name the name.
     * @param clazz the filter class.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder filter(String name, Class<? extends Filter> clazz) {
        filters.put(name, clazz);
        return this;
    }

    /**
     * Add a filter.
     *
     * @param name the name.
     * @param filter the filter.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder filter(String name, Filter filter) {
        filters.put(name, filter);
        return this;
    }

    /**
     * Set a filter init parameter.
     *
     * @param filterName the name of the filter.
     * @param name the name of the parameter.
     * @param value the value of the parameter.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder filterInitParam(String filterName, String name, String value) {
        Map<String, String> initParameters = filterInitParameters.get(filterName);
        if (initParameters == null) {
            initParameters = new HashMap<>();
            filterInitParameters.put(filterName, initParameters);
        }
        initParameters.put(name, value);
        return this;
    }

    /**
     * Add a init parameter.
     *
     * @param name the name.
     * @param value the value.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder initParam(String name, String value) {
        initParams.put(name, value);
        return this;
    }

    /**
     * Add a servlet.
     *
     * @param name the name of the servlet.
     * @param className the class name of the servlet.
     * @return the servlet builder.
     */
    public DefaultWebApplicationBuilder servlet(String name, String className) {
        servlets.put(name, className);
        return this;
    }

    /**
     * Add a servlet.
     *
     * @param name the name of the servlet.
     * @param clazz the class of the servlet.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder servlet(String name, Class<? extends Servlet> clazz) {
        servlets.put(name, clazz);
        return this;
    }

    /**
     * Add a servlet.
     *
     * @param name the name of the servlet.
     * @param servlet the Servlet.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder servlet(String name, Servlet servlet) {
        servlets.put(name, servlet);
        return this;
    }

    /**
     * Set a servlet init parameter.
     *
     * @param servletName the name of the servlet.
     * @param name the name of the parameter.
     * @param value the value of the parameter.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder servletInitParam(String servletName, String name, String value) {
        Map<String, String> initParameters = servletInitParameters.get(servletName);
        if (initParameters == null) {
            initParameters = new HashMap<>();
            servletInitParameters.put(servletName, initParameters);
        }
        initParameters.put(name, value);
        return this;
    }

    /**
     * Add a servlet mapping.
     *
     * @param servletName the servlet name.
     * @param mapping the mapping.
     * @return the web application builder.
     */
    public DefaultWebApplicationBuilder servletMapping(String servletName, String mapping) {
        servletMappings.put(servletName, mapping);
        return this;
    }
}
