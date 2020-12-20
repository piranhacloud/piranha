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
package cloud.piranha.security.exousia;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;

import java.io.IOException;

import jakarta.security.jacc.PolicyContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import cloud.piranha.webapp.api.FilterPriority;
import cloud.piranha.webapp.api.SecurityManager;
import cloud.piranha.webapp.api.WebApplication;

/**
 * This filter is uses to call a Jakarta Authentication system module at the
 * start of an HTTP request.
 *
 * <p>
 * Note, this Filter *MUST* be installed as the first filter, and it should
 * *NOT* be possible to place a filter before this filter. The standard Servlet
 * API does not provide facilities for this.
 *
 * @author Arjan Tijms
 *
 */
public class AuthorizationPreFilter extends HttpFilter implements FilterPriority {

    /**
     * Stores the local servlet request.
     */
    public static ThreadLocal<HttpServletRequest> localServletRequest = new ThreadLocal<>();

    /**
     * Stores the priority.
     */
    public static int PRIORITY = 0;

    private static final long serialVersionUID = 8478463438252262094L;

    /**
     * Stores the security manager.
     */
    private SecurityManager securityManager;

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        securityManager = ((WebApplication) filterConfig.getServletContext()).getSecurityManager();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        WebApplication context = (WebApplication) request.getServletContext();
        PolicyContext.setContextID(context.getServletContextId());

        if (!securityManager.isRequestSecurityAsRequired(request, response)) {
            response.setStatus(SC_FORBIDDEN);
            return;
        }

        localServletRequest.set(request);
        try {
            chain.doFilter(request, response);
        } finally {
            localServletRequest.remove();
        }
    }
}
