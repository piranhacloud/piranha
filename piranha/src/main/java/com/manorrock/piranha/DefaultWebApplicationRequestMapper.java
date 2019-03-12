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
package com.manorrock.piranha;

import com.manorrock.piranha.api.WebApplicationRequestMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default WebApplicationRequestMapper.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationRequestMapper implements WebApplicationRequestMapper {

    /**
     * Stores the filter mappings.
     */
    protected final ConcurrentHashMap<String, String> filterMappings = new ConcurrentHashMap<>();

    /**
     * Stores the servlet mappings.
     */
    protected final ConcurrentHashMap<String, String> servletMappings = new ConcurrentHashMap<>();

    /**
     * Add the filter mapping.
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns.
     * @return the URL patterns that were already added.
     */
    @Override
    public Set<String> addFilterMapping(String filterName, String... urlPatterns) {
        Set<String> result = new HashSet<>();
        for (String urlPattern : urlPatterns) {
            if (filterMappings.containsKey(urlPattern)) {
                result.add(urlPattern);
            } else {
                filterMappings.put(urlPattern, filterName);
            }
        }
        return result;
    }

    /**
     * Add a servlet mapping.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns to map (aka mappings).
     * @return the URL patterns that were already added.
     */
    @Override
    public Set<String> addServletMapping(String servletName, String... urlPatterns) {
        Set<String> result = new HashSet<>();

        for (String urlPattern : urlPatterns) {
            if (servletMappings.containsKey(urlPattern)) {
                result.add(urlPattern);
            } else {
                servletMappings.put(urlPattern, servletName);
            }
        }

        return result;
    }

    /**
     * Find the filter mappings.
     *
     * @param path the path.
     * @return the filter mappings.
     */
    @Override
    public Collection<String> findFilterMappings(String path) {
        ArrayList<String> result = new ArrayList<>();
        if (path.contains("?")) {
            path = path.substring(0, path.indexOf("?"));
        }
        for (Map.Entry<String, String> filterMapping : filterMappings.entrySet()) {
            String key = filterMapping.getKey();
            String value = filterMapping.getValue();
            if (path.equals(key)) {
                result.add(value);
            } else if (key.startsWith("*.")) {
                key = key.substring(1);
                if (path.endsWith(key)) {
                    result.add(value);
                }
            } else if (!key.startsWith("*.") && key.endsWith("/*")) {
                key = key.substring(0, key.length() - 1);
                if (path.startsWith(key)) {
                    result.add(value);
                }
            }
        }
        return result;
    }

    /**
     * Find a servlet mapping with an exact mapping.
     *
     * @param path the path.
     * @return the mapping, or null if not found.
     */
    private DefaultWebApplicationRequestMapping findServletExactMatch(String path) {
        DefaultWebApplicationRequestMapping result = null;

        Enumeration<String> exacts = servletMappings.keys();
        while (exacts.hasMoreElements()) {
            String exact = exacts.nextElement();

            if (path.equals(exact)) {
                result = new DefaultWebApplicationRequestMapping(exact);
                result.setExact(true);
                break;
            }
        }

        return result;
    }

    /**
     * Find a servlet mapping with an extension match.
     *
     * @param path the path.
     * @return the mapping, or null if not found.
     */
    private DefaultWebApplicationRequestMapping findServletExtensionMatch(String path) {
        DefaultWebApplicationRequestMapping result = null;

        Enumeration<String> extensions = servletMappings.keys();
        while (extensions.hasMoreElements()) {
            String extension = extensions.nextElement();
            /*
             * Make sure it really is an extension first.
             */
            if (extension.startsWith("*.")) {
                extension = extension.substring(1);
                if (path.endsWith(extension)) {
                    result = new DefaultWebApplicationRequestMapping("*" + extension);
                    result.setExtension(true);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Find a servlet mapping for the given path.
     *
     * @param path the path.
     * @return the mapping, or null if not found.
     */
    @Override
    public DefaultWebApplicationRequestMapping findServletMapping(String path) {
        if (path.contains("?")) {
            path = path.substring(0, path.indexOf("?"));
        }
        DefaultWebApplicationRequestMapping result = findServletExactMatch(path);
        if (result == null) {
            result = findServletPrefixMatch(path);
        }
        if (result == null) {
            result = findServletExtensionMatch(path);
        }
        return result;
    }

    /**
     * Find a servlet mapping with the longest prefix mapping.
     *
     * @param path the path.
     * @return the mapping, or null if not found.
     */
    private DefaultWebApplicationRequestMapping findServletPrefixMatch(String path) {
        DefaultWebApplicationRequestMapping result = null;
        DefaultWebApplicationRequestMapping found;

        for (;;) {
            found = findServletPrefixMatch(path, result);
            if (found != null) {
                result = found;
            } else {
                break;
            }
        }

        if (result != null) {
            result.setMapping(result.getPath() + "*");
        }
        return result;
    }

    /**
     * Find a servlet mapping with a prefix mapping longer than the given
     * current prefix.
     *
     * @param path the path.
     * @param currentPrefix the current matched prefix.
     * @return the mapping, or null if not found.
     */
    private DefaultWebApplicationRequestMapping findServletPrefixMatch(String path, DefaultWebApplicationRequestMapping currentPrefix) {
        DefaultWebApplicationRequestMapping result = null;
        Enumeration<String> prefixes = servletMappings.keys();

        while (prefixes.hasMoreElements()) {
            String prefix = prefixes.nextElement();
            if (!prefix.startsWith("*.") && prefix.endsWith("/*")) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (path.startsWith(prefix)) {
                    if (result == null) {
                        result = new DefaultWebApplicationRequestMapping(prefix);
                    }
                    break;
                }
            }
        }

        if (result != null && currentPrefix != null
                && result.getPath().length() <= currentPrefix.getPath().length()) {
            result = null;
        }
        return result;
    }

    /**
     * Get the mappings for the specified servlet.
     *
     * @param servletName the servlet name.
     * @return the mappings, or an empty collection if none.
     */
    @Override
    public Collection<String> getServletMappings(String servletName) {
        Collection<String> result = new ArrayList<>();
        servletMappings.keySet().stream().filter((urlPattern)
                -> (servletMappings.get(urlPattern).equals(servletName))).forEach((urlPattern) -> {
            result.add(urlPattern);
        });
        return result;
    }

    /**
     * Get the servlet name for the specified mapping..
     *
     * @param mapping the mapping.
     * @return the servlet name, or null if not found.
     */
    @Override
    public String getServletName(String mapping) {
        return servletMappings.get(mapping);
    }
}
