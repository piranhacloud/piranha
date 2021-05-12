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

import java.util.List;

import cloud.piranha.webapp.api.FilterEnvironment;
import cloud.piranha.webapp.api.ServletEnvironment;
import cloud.piranha.webapp.api.ServletInvocation;
import cloud.piranha.webapp.api.WebApplicationRequestMapping;
import jakarta.servlet.FilterChain;

/**
 * The default servlet invocation
 *
 * @author Arjan Tijms
 *
 */
public class DefaultServletInvocation implements ServletInvocation {

    /**
     * Stores the invocation path.
     */
    private String invocationPath;
    
    /**
     * Stores the servlet name.
     */
    private String servletName;
    
    /**
     * Stores the servlet path.
     */
    private String servletPath;
    
    /**
     * The original servlet path that was requested.
     * This is used if the invocation locator tried alternative 
     * servlet paths, such as with welcome files.
     * 
     */
    private String originalServletPath;

    /**
     * Stores the path info.
     */
    private String pathInfo;
    
    /**
     * Stores the web application request mapping.
     */
    private WebApplicationRequestMapping applicationRequestMapping;
    
    /**
     * Stores the DefaultHttpServletMapping
     */
    private DefaultHttpServletMapping httpServletMapping = new DefaultHttpServletMapping();

    /**
     * Stores the servlet environment.
     */
    private ServletEnvironment servletEnvironment;
    
    /**
     * Stores the filter environment.
     */
    private List<FilterEnvironment> filterEnvironments;
    
    /**
     * Stores the filter chain.
     */
    private FilterChain filterChain;
    
    /**
     * Boolean to indicate whether this invocation results
     * from a getNamedDispatcher
     */
    private boolean fromNamed;

    @Override
    public String getInvocationPath() {
        return invocationPath;
    }

    /**
     * Set the invocation path.
     * 
     * @param invocationPath the invocation path.
     */
    public void setInvocationPath(String invocationPath) {
        this.invocationPath = invocationPath;
    }

    @Override
    public String getServletName() {
        return servletName;
    }

    /**
     * Set the servlet name.
     * 
     * @param servletName the servlet name.
     */
    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    /**
     * Set the servlet path.
     * 
     * @param servletPath the servlet path.
     */
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }
    
    @Override
    public String getOriginalServletPath() {
        return originalServletPath;
    }

    /**
     * Set the originalServletPath
     * 
     * @param originalServletPath the originalServletPath
     */
    public void setOriginalServletPath(String originalServletPath) {
        this.originalServletPath = originalServletPath;
    }

    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    /**
     * Set the path info.
     * 
     * @param pathInfo the path info.
     */
    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    @Override
    public WebApplicationRequestMapping getApplicationRequestMapping() {
        return applicationRequestMapping;
    }

    /**
     * Set the web application request mapping.
     * 
     * @param applicationRequestMapping the web application request mapping.
     */
    public void setApplicationRequestMapping(WebApplicationRequestMapping applicationRequestMapping) {
        this.applicationRequestMapping = applicationRequestMapping;
    }
    
    /**
     * @return the httpServletMapping
     */
    public DefaultHttpServletMapping getHttpServletMapping() {
        return httpServletMapping;
    }

    @Override
    public ServletEnvironment getServletEnvironment() {
        return servletEnvironment;
    }

    /**
     * Set the servlet environment.
     * 
     * @param servletEnvironment the servlet environment.
     */
    public void setServletEnvironment(ServletEnvironment servletEnvironment) {
        this.servletEnvironment = servletEnvironment;
    }

    @Override
    public List<FilterEnvironment> getFilterEnvironments() {
        return filterEnvironments;
    }

    /**
     * Set the filter environments.
     * 
     * @param filterEnvironments the filter environments.
     */
    public void setFilterEnvironments(List<FilterEnvironment> filterEnvironments) {
        this.filterEnvironments = filterEnvironments;
    }

    @Override
    public FilterChain getFilterChain() {
        return filterChain;
    }

    /**
     * Set the filter chain.
     * 
     * @param filterChain the filter chain.
     */
    public void setFilterChain(FilterChain filterChain) {
        this.filterChain = filterChain;
    }
    
    @Override
    public boolean isFromNamed() {
        return fromNamed;
    }

    @Override
    public void setFromNamed(boolean fromNamed) {
        this.fromNamed = fromNamed;
    }

    /**
     * Seed the filter chain.
     */
    public void seedFilterChain() {
        setFilterChain(
            new DefaultFilterChain(
                this,
                getServletEnvironment() == null ? null : getServletEnvironment().getServlet()));
    }

}
