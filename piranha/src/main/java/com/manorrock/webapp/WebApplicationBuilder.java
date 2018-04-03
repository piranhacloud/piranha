/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

/**
 * The WebApplicationBuilder API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationBuilder {

    /**
     * Build the web application.
     *
     * @return the web application.
     */
    WebApplication build();

    /**
     * Set the class loader.
     *
     * @param classLoader the class loader.
     * @return the WebApplicationBuilder.
     */
    WebApplicationBuilder classLoader(ClassLoader classLoader);

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     * @return the WebApplicationBuilder.
     */
    WebApplicationBuilder contextPath(String contextPath);

    /**
     * Add the servlet.
     *
     * @param servletName the servlet name.
     * @param className the class name.
     * @return
     */
    WebApplicationBuilder servlet(String servletName, String className);

    /**
     * Add the servlet mapping.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns.
     * @return the WebApplicationBuilder.
     */
    WebApplicationBuilder servletMapping(String servletName, String... urlPatterns);
}
