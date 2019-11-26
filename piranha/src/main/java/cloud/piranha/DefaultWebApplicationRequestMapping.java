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
package cloud.piranha;

import cloud.piranha.api.WebApplicationRequestMapping;

/**
 * The default WebApplicationRequestMapping.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationRequestMapping implements WebApplicationRequestMapping {

    /**
     * Stores the exact flag.
     */
    private boolean exact;
    
    /**
     * Stores the extension flag.
     */
    private boolean extension;
    
    /**
     * Stores the mapping.
     */
    private String mapping;
    
    /**
     * Constructor.
     * 
     * @param mapping the mapping. 
     */
    public DefaultWebApplicationRequestMapping(String mapping) {
        this.mapping = mapping;
    }

    /**
     * Get the mapping.
     * 
     * @return the mapping.
     */
    @Override
    public String getPath() {
        return mapping;
    }

    /**
     * Is this an exact match.
     * 
     * @return true it it is, false otherwise.
     */
    @Override
    public boolean isExact() {
        return exact;
    }

    /**
     * Is this an extension match.
     * 
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isExtension() {
        return extension;
    }

    /**
     * Set the exact flag.
     * 
     * @param exact the exact flag. 
     */
    public void setExact(boolean exact) {
        this.exact = exact;
    }
    
    /**
     * Set the extension flag.
     * 
     * @param extension the extension flag.
     */
    public void setExtension(boolean extension) {
        this.extension = extension;
    }

    /**
     * Set the mapping.
     * 
     * @param mapping the mapping. 
     */
    public void setMapping(String mapping) {
        this.mapping = mapping;
    }
}
