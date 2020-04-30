/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.appserver.impl;

import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.appserver.api.WebApplicationServerRequestMapper;
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
