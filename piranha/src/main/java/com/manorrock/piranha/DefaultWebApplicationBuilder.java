/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
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
