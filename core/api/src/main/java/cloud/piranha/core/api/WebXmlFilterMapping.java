/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.api;

import java.util.ArrayList;
import java.util.List;

/**
 * A filter-mapping inside of web.xml/web-fragment.xml.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlFilterMapping {

    /**
     * The type of dispatch on which the filter is invoked.
     *
     * <p>
     * Valid values are FORWARD, INCLUDE, REQUEST, and ERROR.
     * </p>
     */
    private List<String> dispatchers = new ArrayList<>();

    /**
     * Stores the filter name.
     */
    private String filterName;

    /**
     * Stores the servlet names to which a filter is mapped.
     */
    private List<String> servletNames = new ArrayList<>();

    /**
     * Stores the URL pattern.
     */
    private List<String> urlPatterns = new ArrayList<>();

    /**
     * Get the dispatchers.
     *
     * @return the dispatchers.
     */
    public List<String> getDispatchers() {
        return dispatchers;
    }

    /**
     * Get the filter name.
     *
     * @return the filter name.
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * Get the servlet names.
     *
     * @return the servlet names.
     */
    public List<String> getServletNames() {
        return servletNames;
    }

    /**
     * Get the URL patterns.
     *
     * @return the URL patterns.
     */
    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    /**
     * Set the dispatchers.
     *
     * @param dispatchers the dispatchers.
     */
    public void setDispatchers(List<String> dispatchers) {
        this.dispatchers = dispatchers;
    }

    /**
     * Set the filter name.
     *
     * @param filterName the filter name.
     */
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    /**
     * Set the servlet names.
     *
     * @param servletNames the servlet names.
     */
    public void setServletNames(List<String> servletNames) {
        this.servletNames = servletNames;
    }

    /**
     * Set the URL patterns.
     *
     * @param urlPatterns the URL patterns.
     */
    public void setUrlPatterns(List<String> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }
}
