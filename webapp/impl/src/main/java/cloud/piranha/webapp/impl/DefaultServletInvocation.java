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
package cloud.piranha.webapp.impl;

import java.util.List;

import javax.servlet.FilterChain;

import cloud.piranha.webapp.api.FilterEnvironment;
import cloud.piranha.webapp.api.ServletEnvironment;
import cloud.piranha.webapp.api.ServletInvocation;
import cloud.piranha.webapp.api.WebApplicationRequestMapping;

/**
 * The default servlet invocation
 *
 * @author Arjan Tijms
 *
 */
public class DefaultServletInvocation implements ServletInvocation {

    private String invocationPath;
    private String servletName;
    private String servletPath;
    private String pathInfo;
    private WebApplicationRequestMapping applicationRequestMapping;
    private ServletEnvironment servletEnvironment;
    private List<FilterEnvironment> filterEnvironments;
    private FilterChain filterChain;

    @Override
    public String getInvocationPath() {
        return invocationPath;
    }

    public void setInvocationPath(String invocationPath) {
        this.invocationPath = invocationPath;
    }


    @Override
    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    @Override
    public WebApplicationRequestMapping getApplicationRequestMapping() {
        return applicationRequestMapping;
    }

    public void setApplicationRequestMapping(WebApplicationRequestMapping applicationRequestMapping) {
        this.applicationRequestMapping = applicationRequestMapping;
    }

    @Override
    public ServletEnvironment getServletEnvironment() {
        return servletEnvironment;
    }

    public void setServletEnvironment(ServletEnvironment servletEnvironment) {
        this.servletEnvironment = servletEnvironment;
    }

    @Override
    public List<FilterEnvironment> getFilterEnvironments() {
        return filterEnvironments;
    }

    public void setFilterEnvironments(List<FilterEnvironment> filterEnvironments) {
        this.filterEnvironments = filterEnvironments;
    }

    @Override
    public FilterChain getFilterChain() {
        return filterChain;
    }

    public void setFilterChain(FilterChain filterChain) {
        this.filterChain = filterChain;
    }

    public void seedFilterChain() {
        setFilterChain(
            new DefaultFilterChain(
                getServletEnvironment() == null ? null : getServletEnvironment().getServlet()));
    }

}
