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
package cloud.piranha.webapp.api;

import static jakarta.servlet.DispatcherType.REQUEST;

import java.util.Collection;
import java.util.Set;

import jakarta.servlet.DispatcherType;

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
     * <p>
     * This adds the filter mappings at the end of list of existing mappings (if any).
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns to map (aka mappings).
     * @return the URL patterns that were added.
     */
    default Set<String> addFilterMapping(String filterName, String... urlPatterns) {
        return addFilterMapping(null, filterName, urlPatterns);
    }

    /**
     * Add a filter mapping.
     *
     * <p>
     * This adds the filter mappings at the end of list of existing mappings (if any).
     *
     * @param dispatcherTypes the dispatcher types.
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns to map (aka mappings).
     * @return the URL patterns that were added.
     */
    Set<String> addFilterMapping(Set<DispatcherType> dispatcherTypes, String filterName, String... urlPatterns);

    /**
     * Add a filter mapping.
     *
     * <p>
     * This adds the filter mappings at the start of list of existing mappings (if any).
     * If there are existing mappings these are shifted to the right.
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns to map (aka mappings).
     * @return the URL patterns that were added.
     */
    default Set<String> addFilterMappingBeforeExisting(String filterName, String... urlPatterns) {
        return addFilterMappingBeforeExisting(null, filterName, urlPatterns);
    }

    /**
     * Add a filter mapping.
     *
     * <p>
     * This adds the filter mappings at the start of list of existing mappings (if any).
     * If there are existing mappings these are shifted to the right.
     *
     * @param dispatcherTypes the dispatcher types.
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns to map (aka mappings).
     * @return the URL patterns that were added.
     */
    Set<String> addFilterMappingBeforeExisting(Set<DispatcherType> dispatcherTypes, String filterName, String... urlPatterns);


    /**
     * Find the filter mappings for the given path.
     *
     * @param path the path.
     * @return the mappings.
     */
    default Collection<String> findFilterMappings(String path) {
        return findFilterMappings(REQUEST, path);
    }

    /**
     * Find the filter mappings for the given path.
     *
     * @param dispatcherType the dispatcher type.
     * @param path the path.
     * @return the mappings.
     */
    Collection<String> findFilterMappings(DispatcherType dispatcherType, String path);

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
     *  Returns the defaultServlet
     */
    default String getDefaultServlet() {
        return null;
    }

    /**
     * Get the servlet name for the specified mapping.
     *
     * @param mapping the mapping.
     * @return the servlet name, or null if not found.
     */
    String getServletName(String mapping);
}
