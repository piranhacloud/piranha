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

import com.manorrock.piranha.api.FilterMapping;
import java.util.Objects;

/**
 * The default FilterMapping.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultFilterMapping implements FilterMapping {

    /**
     * Stores the filter name.
     */
    private final String filterName;

    /**
     * Stores the URL pattern.
     */
    private final String urlPattern;

    /**
     * Constructor.
     *
     * @param filterName the filter name.
     * @param urlPattern the URL pattern.
     */
    public DefaultFilterMapping(String filterName, String urlPattern) {
        this.filterName = filterName;
        this.urlPattern = urlPattern;
    }

    /**
     * Equals.
     *
     * @param object the object to compare against.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object instanceof DefaultFilterMapping) {
            DefaultFilterMapping mapping = (DefaultFilterMapping) object;
            if (mapping.filterName.equals(filterName)
                    && mapping.urlPattern.equals(urlPattern)) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Get the filter name.
     * 
     * @return the filter name.
     */
    @Override
    public String getFilterName() {
        return filterName;
    }

    /**
     * Get the URL pattern.
     * 
     * @return the URL pattern.
     */
    @Override
    public String getUrlPattern() {
        return urlPattern;
    }

    /**
     * Hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.filterName);
        hash = 83 * hash + Objects.hashCode(this.urlPattern);
        return hash;
    }
}
