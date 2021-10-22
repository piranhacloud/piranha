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
package cloud.piranha.webapp.impl;

import static jakarta.servlet.DispatcherType.REQUEST;
import static java.util.Arrays.stream;
import static java.util.Collections.emptySet;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cloud.piranha.core.api.FilterMapping;
import cloud.piranha.core.api.WebApplicationRequestMapper;
import jakarta.servlet.DispatcherType;

/**
 * The default WebApplicationRequestMapper.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationRequestMapper implements WebApplicationRequestMapper {

    /**
     * Stores the filter mappings.
     */
    protected final List<FilterMapping> filterMappings = new ArrayList<>();

    /**
     * Stores the servlet mappings.
     */
    protected final ConcurrentHashMap<String, String> servletMappings = new ConcurrentHashMap<>();
    
    /**
     * Stores the default servlet
     */
    protected String defaultServlet;
    

    @Override
    public Set<String> addFilterMapping(Set<DispatcherType> dispatcherTypes, String filterName, String... urlPatterns) {
        return doAddFilterMapping(isEmpty(dispatcherTypes)? EnumSet.of(REQUEST) : dispatcherTypes, filterName, urlPatterns);
    }

    @Override
    public Set<String> addFilterMappingBeforeExisting(Set<DispatcherType> dispatcherTypes, String filterName, String... urlPatterns) {
       return doAddFilterMappingBeforeExisting(isEmpty(dispatcherTypes)? EnumSet.of(REQUEST) : dispatcherTypes, filterName, urlPatterns);
    }

    /**
     * Add a servlet mapping.
     * 
     * <p>
     *  This method will throw an IllegalArgumentException if a URL pattern
     *  is null or empty (Servlet:JAVADOC:696.3).
     * 
     * <p>
     *  This method accommodates for a URL pattern that does not start with a
     *  leading slash to support pre-Servlet 2.3 application to operate
     *  properly.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns to map (aka mappings).
     * @return the URL patterns that were already added.
     */
    @Override
    public Set<String> addServletMapping(String servletName, String... urlPatterns) {
        if (isEmpty(urlPatterns)) {
            throw new IllegalArgumentException("Mappings for " + servletName + " cannot be empty");
        }

        // Servlet:JAVADOC:696.1 - If any of the specified URL patterns are already mapped to a different Servlet, no updates will be performed.
        Set<String> mappedToOtherServlet = stream(urlPatterns)
            .filter(servletMappings::containsKey)
            .filter(urlPattern -> !servletMappings.get(urlPattern).equals(servletName))
            .collect(toSet());

        if (!mappedToOtherServlet.isEmpty()) {
            return mappedToOtherServlet;
        }

        for (String urlPattern : urlPatterns) {
            if ("/".equals(urlPattern)) {
                // Spec 12.2. Specification of Mappings
                // "A string containing only the "/" character indicates the "default" servlet of the application"
                defaultServlet = servletName;
            } else {
                if (!urlPattern.startsWith("*") && !urlPattern.startsWith("/")) {
                    urlPattern = "/" + urlPattern;
                }

                servletMappings.put(urlPattern, servletName);
            }
        }

        return emptySet();
    }

    @Override
    public String removeServletMapping(String urlPattern) {
        return servletMappings.remove(urlPattern);
    }

    /**
     * Find the filter mappings.
     *
     * @param path the path.
     * @return the filter mappings.
     */
    @Override
    public Collection<String> findFilterMappings(DispatcherType dispatcherType, String path) {
        List<String> result = new ArrayList<>();

        if (path.contains("?")) {
            path = path.substring(0, path.indexOf("?"));
        }

        for (FilterMapping filterMapping : filterMappings) {
            if (dispatcherType.equals(filterMapping.getDispatcherType())) {
                String filterName = filterMapping.getFilterName();
                String urlPattern = filterMapping.getUrlPattern();

                if (path.equals(urlPattern)) {
                    result.add(filterName);
                } else if (!path.startsWith("servlet:// ")) {

                    // For Servlet "patterns", only do exact matches.
                    // URL patterns are also matched prefix and extension.

                    if (urlPattern.startsWith("*.")) {
                        urlPattern = urlPattern.substring(1);
                        if (path.endsWith(urlPattern)) {
                            result.add(filterName);
                        }
                    } else if (!urlPattern.startsWith("*.") && urlPattern.endsWith("/*")) {
                        urlPattern = urlPattern.substring(0, urlPattern.length() - 2);
                        if (path.startsWith(urlPattern)) {
                            result.add(filterName);
                        }
                    }
                }
            }
        }

        return result;
    }

    private Set<String> doAddFilterMapping(Set<DispatcherType> dispatcherTypes, String filterName, String... urlPatterns) {
        requireNonNull(dispatcherTypes);

        Set<String> result = new HashSet<>();
        for (String urlPattern : urlPatterns) {
            for (DispatcherType dispatcherType : dispatcherTypes) {
                DefaultFilterMapping filterMapping = new DefaultFilterMapping(dispatcherType, filterName, urlPattern);
                if (filterMappings.contains(filterMapping)) {
                    result.add(urlPattern);
                } else {
                    filterMappings.add(filterMapping);
                }
            }
        }
        return result;
    }

    private Set<String> doAddFilterMappingBeforeExisting(Set<DispatcherType> dispatcherTypes, String filterName, String... urlPatterns) {
        requireNonNull(dispatcherTypes);

        Set<String> result = new HashSet<>();
        for (String urlPattern : urlPatterns) {
            for (DispatcherType dispatcherType : dispatcherTypes) {
                DefaultFilterMapping filterMapping = new DefaultFilterMapping(dispatcherType, filterName, urlPattern);
                if (filterMappings.contains(filterMapping)) {
                    result.add(urlPattern);
                } else {
                    filterMappings.add(0, filterMapping);
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
                result.setMatchValue(exact.substring(1));
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
                    // If path is /foo.bar and the initial extension is *.bar, then
                    // the match value is foo.
                    result.setMatchValue(path.substring(1, path.lastIndexOf(extension)));
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
            result.setPattern(result.getPattern() + "*");
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
                if ((path + "/").startsWith(prefix)) {
                    result = new DefaultWebApplicationRequestMapping(prefix);
                    // If path is /foo/bar and the initial prefix is /foo/* then the
                    // match value is bar
                    result.setMatchValue(path.substring(1));
                    break;
                }
            }
        }
        if (result != null && currentPrefix != null
                && result.getPattern().length() <= currentPrefix.getPattern().length()) {
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
        servletMappings.keySet().stream().filter(urlPattern
                -> servletMappings.get(urlPattern).equals(servletName)).forEach(result::add);
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
    
    @Override
    public String getDefaultServlet() {
        return defaultServlet;
    }

    private boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    private boolean isEmpty(String[] strings) {
        return strings == null || strings.length == 0;
    }
}
