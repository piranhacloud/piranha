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
package cloud.piranha.extension.webxml;

import java.util.ArrayList;
import java.util.List;

/**
 * The web.xml filter-mapping.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlFilterMapping {

    /**
     * Stores the filter name.
     */
    private String filterName;

    /**
     * Stores the URL pattern.
     */
    private final List<String> urlPatterns = new ArrayList<>();


    /**
     * Names of the Servlets to which a filter is mapped
     */
    private final List<String> servletNames = new ArrayList<>();

    /**
     * The type of dispatch on which the filter is invoked. Valid values:
     * FORWARD
     * INCLUDE
     * REQUEST
     * ERROR
     */
    private final List<String> dispatchers = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param filterName the filter name.
     */
    public WebXmlFilterMapping(String filterName) {
        this.filterName = filterName;
    }

    /**
     * {@return the filter name}
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * {@return the URL patterns}
     */
    public List<String> getUrlPatterns() {
        return urlPatterns;
    }

    /**
     * {@return the Servlet names}
     */
    public List<String> getServletNames() {
        return servletNames;
    }

    /**
     * {@return the dispatchers}
     */
    public List<String> getDispatchers() {
        return dispatchers;
    }
}

