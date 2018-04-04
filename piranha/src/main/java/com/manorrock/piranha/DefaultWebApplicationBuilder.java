/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.util.LinkedHashMap;

/**
 * The default WebApplicationBuilder.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationBuilder implements WebApplicationBuilder {

    /**
     * Stores the class loader.
     */
    private ClassLoader classLoader;

    /**
     * Stores the context path.
     */
    private String contextPath;

    /**
     * Stores the servlets.
     */
    private final LinkedHashMap<String, String> servlets = new LinkedHashMap<>();

    /**
     * Stores the servlet mappings.
     */
    private final LinkedHashMap<String, String[]> servletMappings = new LinkedHashMap<>();

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
    @Override
    public WebApplication build() {
        DefaultWebApplication result = new DefaultWebApplication();
        result.setWebApplicationRequestMapper(new DefaultWebApplicationRequestMapper());

        if (classLoader != null) {
            result.setClassLoader(classLoader);
        }
        if (contextPath != null) {
            result.setContextPath(contextPath);
        }
        servlets.entrySet().stream().forEach((servlet) -> {
            String servletName = servlet.getKey();
            String className = servlet.getValue();
            result.addServlet(servletName, className);
        });
        servletMappings.entrySet().stream().forEach((servletMapping) -> {
            String servletName = servletMapping.getKey();
            String[] urlPatterns = servletMapping.getValue();
            result.addServletMapping(servletName, urlPatterns);
        });

        return result;
    }

    /**
     * Set the class loader.
     *
     * @param classLoader the class loader.
     * @return the WebApplicationBuilder.
     */
    @Override
    public WebApplicationBuilder classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     * @return the WebApplicationBuilder.
     */
    @Override
    public WebApplicationBuilder contextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    /**
     * Add a servlet.
     *
     * @param servletName the servlet name.
     * @param className the class name.
     * @return
     */
    @Override
    public WebApplicationBuilder servlet(String servletName, String className) {
        servlets.put(servletName, className);
        return this;
    }

    /**
     * Add a servlet mapping.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns.
     */
    @Override
    public WebApplicationBuilder servletMapping(String servletName, String... urlPatterns) {
        servletMappings.put(servletName, urlPatterns);
        return this;
    }
}
