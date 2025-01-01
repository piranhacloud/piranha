/*
 * Copyright (c) 2002-2025 Manorrock.com. All Rights Reserved.
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
 * A security web resource collection.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SecurityWebResourceCollection {
    
    /**
     * Stores the HTTP methods.
     */
    private List<String> httpMethods;

    /**
     * Stores the HTTP method omissions.
     */
    private List<String> httpMethodOmissions;
    
    /**
     * Stores the URL patterns.
     */
    private List<String> urlPatterns;

    /**
     * Constructor.
     */
    public SecurityWebResourceCollection() {
        this.httpMethods = new ArrayList<>();
        this.httpMethodOmissions = new ArrayList<>();
        this.urlPatterns = new ArrayList<>();
    }

    /**
     * Get the HTTP methods.
     * 
     * @return the HTTP methods.
     */
    public List<String> getHttpMethods() {
        return httpMethods;
    }
    
    /**
     * Get the HTTP method omissions.
     * 
     * @return the HTTP method omissions.
     */
    public List<String> getHttpMethodOmissions() {
        return httpMethodOmissions;
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
     * Set the HTTP methods.
     * 
     * @param httpMethods the HTTP methods.
     */
    public void setHttpMethods(List<String> httpMethods) {
        this.httpMethods = httpMethods;
    }

    /**
     * Set the HTTP method omissions.
     * 
     * @param httpMethodOmissions the HTTP method omissions.
     */
    public void setHttpMethodOmissions(List<String> httpMethodOmissions) {
        this.httpMethodOmissions = httpMethodOmissions;
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
