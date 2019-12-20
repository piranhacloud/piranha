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
package cloud.piranha.embedded;

import cloud.piranha.DefaultAliasedDirectoryResource;
import cloud.piranha.DefaultDirectoryResource;
import cloud.piranha.api.HttpSessionManager;
import cloud.piranha.api.Resource;
import cloud.piranha.api.WebApplication;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletRegistration;

/**
 * The Embedded Piranha builder.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EmbeddedPiranhaBuilder {
    
    /**
     * Stores the attributes.
     */
    private final LinkedHashMap<String, Object> attributes;

    /**
     * Stores the features.
     */
    private final ArrayList<String> features;

    /**
     * Stores the initializers.
     */
    private final ArrayList<String> initializers;

    /**
     * Stores the filters.
     */
    private final LinkedHashMap<String, String> filters;

    /**
     * Stores the filter init parameters map.
     */
    private final LinkedHashMap<String, HashMap<String, String>> filterInitParameters;

    /**
     * Stores the filter mappings.
     */
    private final LinkedHashMap<String, List<String>> filterMappings;

    /**
     * Stores the HTTP session manager.
     */
    private HttpSessionManager httpSessionManager;

    /**
     * Stores the resources.
     */
    private final ArrayList<Resource> resources;

    /**
     * Stores the servlets.
     */
    private final LinkedHashMap<String, String> servlets;

    /**
     * Stores the servlet init parameters map.
     */
    private final LinkedHashMap<String, HashMap<String, String>> servletInitParameters;

    /**
     * Stores the servlet mappings.
     */
    private final LinkedHashMap<String, List<String>> servletMappings;

    /**
     * Constructor.
     */
    public EmbeddedPiranhaBuilder() {
        attributes = new LinkedHashMap<>();
        features = new ArrayList<>();
        filters = new LinkedHashMap<>();
        filterInitParameters = new LinkedHashMap<>();
        filterMappings = new LinkedHashMap<>();
        initializers = new ArrayList<>();
        resources = new ArrayList<>();
        servlets = new LinkedHashMap<>();
        servletInitParameters = new LinkedHashMap<>();
        servletMappings = new LinkedHashMap<>();
    }

    /**
     * Add an aliased directory resource.
     *
     * @param path the path.
     * @param alias the alias.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder aliasedDirectoryResource(String path, String alias) {
        resources.add(new DefaultAliasedDirectoryResource(new File(path), alias));
        return this;
    }
    
    /**
     * Add an attribute.
     * 
     * @param name the name.
     * @param value the value.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder attribute(String name, Object value) {
        attributes.put(name, value);
        return this;
    }

    /**
     * Build the Piranha Embedded instance.
     *
     * @return the instance.
     */
    public EmbeddedPiranha build() {
        EmbeddedPiranha piranha = new EmbeddedPiranha();
        WebApplication webApplication = piranha.getWebApplication();
        if (httpSessionManager != null) {
            webApplication.setHttpSessionManager(httpSessionManager);
        }
        attributes.entrySet().forEach((attribute) -> {
            String attributeName = attribute.getKey();
            Object attributeValue = attribute.getValue();
            webApplication.setAttribute(attributeName, attributeValue);
        });
        resources.forEach((resource) -> {
            webApplication.addResource(resource);
        });
        features.forEach((feature) -> {
            webApplication.addFeature(feature);
        });
        webApplication.initializeFeatures();
        initializers.forEach((initializer) -> {
            webApplication.addInitializer(initializer);
        });
        webApplication.initializeInitializers();
        filters.entrySet().forEach((entry) -> {
            String filterName = entry.getKey();
            String className = entry.getValue();
            FilterRegistration.Dynamic filter = webApplication.addFilter(filterName, className);
            HashMap<String, String> initParameters = filterInitParameters.get(filterName);
            if (initParameters != null) {
                initParameters.entrySet().forEach((initParameter) -> {
                    String name = initParameter.getKey();
                    String value = initParameter.getValue();
                    filter.setInitParameter(name, value);
                });
            }
        });
        filterMappings.entrySet().forEach((filterMapping) -> {
            String filterName = filterMapping.getKey();
            List<String> urlPatterns = filterMapping.getValue();
            webApplication.addFilterMapping(filterName, urlPatterns.toArray(new String[0]));
        });
        webApplication.initializeFilters();
        servlets.entrySet().forEach((entry) -> {
            String servletName = entry.getKey();
            String className = entry.getValue();
            ServletRegistration.Dynamic servlet = webApplication.addServlet(servletName, className);
            HashMap<String, String> initParameters = servletInitParameters.get(servletName);
            if (initParameters != null) {
                initParameters.entrySet().forEach((initParameter) -> {
                    String name = initParameter.getKey();
                    String value = initParameter.getValue();
                    servlet.setInitParameter(name, value);
                });
            }
        });
        servletMappings.entrySet().forEach((servetMapping) -> {
            String servletName = servetMapping.getKey();
            List<String> urlPatterns = servetMapping.getValue();
            webApplication.addServletMapping(servletName, urlPatterns.toArray(new String[0]));
        });
        webApplication.initializeServlets();
        webApplication.initializeFinish();
        return piranha;
    }

    /**
     * Build and starts the Piranha Embedded instance.
     *
     * @return the instance.
     */
    public EmbeddedPiranha buildAndStart() {
        return build()
                .start();
    }

    /**
     * Add a directory resource.
     *
     * @param path the path.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder directoryResource(String path) {
        resources.add(new DefaultDirectoryResource(path));
        return this;
    }

    /**
     * Add a feature.
     *
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder feature(String className) {
        features.add(className);
        return this;
    }

    /**
     * Add a filter.
     *
     * @param filterName the filter name.
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder filter(String filterName, String className) {
        filters.put(filterName, className);
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
    public EmbeddedPiranhaBuilder filterInitParam(String filterName, String name, String value) {
        HashMap<String, String> initParameters = filterInitParameters.get(filterName);
        if (initParameters == null) {
            initParameters = new HashMap<>();
            filterInitParameters.put(filterName, initParameters);
        }
        initParameters.put(name, value);
        return this;
    }

    /**
     * Add a filter mapping.
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder filterMapping(String filterName, String... urlPatterns) {
        filterMappings.put(filterName, Arrays.asList(urlPatterns));
        return this;
    }

    /**
     * Set the HTTP session manager.
     *
     * @param httpSessionManager the HTTP session manager.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder httpSessionManager(HttpSessionManager httpSessionManager) {
        this.httpSessionManager = httpSessionManager;
        return this;
    }

    /**
     * Add an initializer.
     *
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder initializer(String className) {
        initializers.add(className);
        return this;
    }

    /**
     * Add a servlet.
     *
     * @param servletName the servlet name.
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servlet(String servletName, String className) {
        servlets.put(servletName, className);
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
    public EmbeddedPiranhaBuilder servletInitParam(String servletName, String name, String value) {
        HashMap<String, String> initParameters = servletInitParameters.get(servletName);
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
     * @param urlPatterns the URL patterns.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletMapping(String servletName, String... urlPatterns) {
        servletMappings.put(servletName, Arrays.asList(urlPatterns));
        return this;
    }
}
