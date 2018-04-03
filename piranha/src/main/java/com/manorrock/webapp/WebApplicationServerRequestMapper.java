/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.util.Set;

/**
 * The WebApplicationServerRequestMapper API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationServerRequestMapper {

    /**
     * Add a mapping.
     *
     * @param webApplication the web application.
     * @param urlPatterns the url patterns to map (aka mappings).
     * @return the url patterns added.
     */
    public Set<String> addMapping(WebApplication webApplication, String... urlPatterns);

    /**
     * Find a mapping for the given path.
     *
     * @param path the path.
     * @return the mapping, or null if not found.
     */
    public WebApplication findMapping(String path);
}
