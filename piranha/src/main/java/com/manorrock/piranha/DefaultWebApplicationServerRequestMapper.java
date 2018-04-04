/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default WebApplicationServerRequestMapper.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationServerRequestMapper implements WebApplicationServerRequestMapper {

    /**
     * Stores the mappings.
     */
    private final ConcurrentHashMap<String, WebApplication> mappings = new ConcurrentHashMap<>();

    /**
     * Add a mapping.
     *
     * @param webApplication the web application.
     * @param urlPatterns the url patterns to map (aka mappings).
     * @return the url patterns not added.
     */
    @Override
    public Set<String> addMapping(WebApplication webApplication, String... urlPatterns) {
        Set<String> result = new HashSet<>();

        for (String urlPattern : urlPatterns) {
            if (this.mappings.containsKey(urlPattern)) {
                result.add(urlPattern);
            } else {
                this.mappings.put(urlPattern, webApplication);
            }
        }

        return result;
    }

    /**
     * Find a mapping for the given path.
     *
     * @param path the path.
     * @return the mapping, or null if not found.
     */
    @Override
    public WebApplication findMapping(String path) {
        WebApplication result = null;
        String mapping = findPrefixMatch(path);
        if (mapping != null) {
            result = this.mappings.get(mapping);
        }
        return result;
    }

    /**
     * Find a mapping with the longest prefix mapping.
     *
     * @param path the path.
     * @return the mapping, or null if not found.
     */
    private String findPrefixMatch(String path) {
        String result = null;
        String found;

        for (;;) {
            found = findPrefixMatch(path, result);
            if (found != null) {
                result = found;
            } else {
                break;
            }
        }

        return result;
    }

    /**
     * Find a mapping with a prefix mapping longer than the given current
     * prefix.
     *
     * @param path the path.
     * @param currentPrefix the current matched prefix.
     * @return the mapping, or null if not found.
     */
    public String findPrefixMatch(String path, String currentPrefix) {
        String result = null;
        Enumeration<String> prefixes = mappings.keys();

        while (prefixes.hasMoreElements()) {
            String prefix = prefixes.nextElement();
            prefix = prefix.substring(0, prefix.length());
            if (path.startsWith(prefix)) {
                if (result == null) {
                    result = prefix;
                }
                break;
            }
        }

        if (result != null && currentPrefix != null
                && result.length() <= currentPrefix.length()) {
            result = null;
        }
        return result;
    }
}
