/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.WebApplication;

/**
 * The builder for DefaultWebApplicationRequest instances.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationRequestBuilder {
    
    /**
     * Stores the query string.
     */
    private String queryString;
    
    /**
     * Stores the servlet path.
     */
    private String servletPath;
    
    /**
     * Stores the web application.
     */
    private WebApplication webApplication;

    /**
     * Build the request.
     * 
     * @return the request.
     */
    public DefaultWebApplicationRequest build() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setQueryString(queryString);
        request.setServletPath(servletPath);
        request.setWebApplication(webApplication);
        return request;
    }
    
    /**
     * Set the query string.
     * 
     * @param queryString the query string.
     * @return the builder.
     */
    public DefaultWebApplicationRequestBuilder queryString(String queryString) {
        this.queryString = queryString;
        return this;
    }
    
    /**
     * Set the servlet path.
     * 
     * @param servletPath the servlet path.
     * @return the builder.
     */
    public DefaultWebApplicationRequestBuilder servletPath(String servletPath) {
        this.servletPath = servletPath;
        return this;
    }
    
    /**
     * Set the web application.
     * 
     * @param webApplication the web application.
     * @return the builder.
     */
    public DefaultWebApplicationRequestBuilder webApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
        return this;
    }
}
