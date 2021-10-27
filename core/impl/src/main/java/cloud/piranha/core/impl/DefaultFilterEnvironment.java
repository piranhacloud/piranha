/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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

import cloud.piranha.core.api.FilterEnvironment;
import cloud.piranha.core.api.WebApplication;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * The default FilterEnvironment.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultFilterEnvironment implements FilterEnvironment {

    /**
     * Stores the async supported flag.
     */
    private boolean asyncSupported;

    /**
     * Stores the class name.
     */
    private String className;

    /**
     * Stores the filter.
     */
    private Filter filter;
    /**
     * Stores the filter name.
     */
    private String filterName;
    /**
     * Stores the init parameters.
     */
    private HashMap<String, String> initParameters;

    /**
     * Stores the servlet mame mappings.
     */
    private ConcurrentHashMap<String, String> servletNameMappings;

    /**
     * Stores the status.
     */
    private int status;

    /**
     * Stores the url pattern mappings.
     */
    private ConcurrentHashMap<String, String> urlPatternMappings;

    /**
     * Stores the web application.
     */
    private WebApplication webApplication;

    /**
     * Constructor.
     */
    public DefaultFilterEnvironment() {
        initParameters = new HashMap<>();
        servletNameMappings = new ConcurrentHashMap<>();
        urlPatternMappings = new ConcurrentHashMap<>();
    }

    /**
     * Constructor.
     *
     * @param webApp the web application.
     * @param filterName the filter name.
     * @param filter the filter.
     */
    public DefaultFilterEnvironment(WebApplication webApp, String filterName, Filter filter) {
        this();
        this.webApplication = webApp;
        this.filterName = filterName;
        this.filter = filter;
        this.className = filter.getClass().getName();
    }

    @Override
    public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... servletNames) {
        String[] names = Stream.of(servletNames).map(s -> "servlet:// " + s).toArray(String[]::new);
        webApplication.addFilterMapping(dispatcherTypes, filterName, isMatchAfter, names);
        Arrays.stream(servletNames).forEach(x -> servletNameMappings.put(x, filterName));
    }

    @Override
    public void addMappingForUrlPatterns(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... urlPatterns) {
        webApplication.addFilterMapping(dispatcherTypes, filterName, isMatchAfter, urlPatterns);
        Arrays.stream(urlPatterns).forEach(x -> urlPatternMappings.put(x, filterName));
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
    }

    @Override
    public Map<String, String> getInitParameters() {
        return initParameters;
    }

    @Override
    public String getName() {
        return filterName;
    }

    @Override
    public ServletContext getServletContext() {
        return this.webApplication;
    }

    @Override
    public Collection<String> getServletNameMappings() {
        return Collections.unmodifiableCollection(servletNameMappings.keySet());
    }

    /**
     * Get the status.
     * 
     * @return the status.
     */
    public int getStatus() {
        return status;
    }

    @Override
    public Collection<String> getUrlPatternMappings() {
        return Collections.unmodifiableCollection(urlPatternMappings.keySet());
    }

    @Override
    public WebApplication getWebApplication() {
        return webApplication;
    }

    @Override
    public void initialize() throws ServletException {
        if (filter == null) {
            try {
                Class<? extends Filter> clazz = webApplication.getClassLoader().loadClass(className).asSubclass(Filter.class);
                filter = webApplication.createFilter(clazz);
            } catch (Throwable throwable) {
                throw new ServletException("Unable to initialize the filter", throwable);
            }
        }
    }

    @Override
    public boolean isAsyncSupported() {
        return asyncSupported;
    }

    @Override
    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        boolean result = false;
        if (!initParameters.containsKey(name)) {
            initParameters.put(name, value);
            result = true;
        }
        return result;
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> initParameters) {
        HashSet<String> conflicting = new HashSet<>();
        if (initParameters != null) {
            initParameters.entrySet().forEach(entry -> {
                String name = entry.getKey();
                String value = entry.getValue();
                if (name == null) {
                    throw new IllegalArgumentException("A null name is not allowed");
                }
                if (value == null) {
                    throw new IllegalArgumentException("A null value is not allowed");
                }
                if (!setInitParameter(name, value)) {
                    conflicting.add(name);
                }
            });
        }
        return conflicting;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    @Override
    public String toString() {
        return
            (className != null? className : "") + " " +
            (!urlPatternMappings.isEmpty()? urlPatternMappings : "") +
            (!servletNameMappings.isEmpty()? servletNameMappings : "") + " " +
            super.toString();
    }
}
