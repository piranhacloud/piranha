/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.util.Collection;
import java.util.Set;

/**
 * The WebApplicationRequestMapper API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationRequestMapper {

    /**
     * Add a servlet mapping.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns to map (aka mappings).
     * @return the URL patterns that were added.
     */
    Set<String> addServletMapping(String servletName, String... urlPatterns);

    /**
     * Add a filter mapping.
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns to map (aka mappings).
     * @return the URL patterns that were added.
     */
    Set<String> addFilterMapping(String filterName, String... urlPatterns);

    /**
     * Find the filter mappings for the given path.
     *
     * @param path the path.
     * @return the mappings.
     */
    Collection<String> findFilterMappings(String path);

    /**
     * Find the servlet mapping for the given path.
     *
     * @param path the path.
     * @return the mapping, or null if not found.
     */
    WebApplicationRequestMapping findServletMapping(String path);

    /**
     * Get the mappings for the specified servlet.
     *
     * @param servletName the servlet name.
     * @return the servlet mappings, or an empty collection if none.
     */
    Collection<String> getServletMappings(String servletName);

    /**
     * Get the servlet name for the specified mapping.
     *
     * @param mapping the mapping.
     * @return the servlet name, or null if not found.
     */
    String getServletName(String mapping);
}
