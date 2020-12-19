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
package cloud.piranha.webapp.api;

import static cloud.piranha.webapp.api.ServletEnvironment.UNAVAILABLE;

import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletConfig;

/**
 * The ServletInvocation API.
 *
 * <p>
 * This type holds data necessary to invoke a Servlet.
 *
 *
 * @author Arjan Tijms
 *
 */
public interface ServletInvocation {

    /**
     * The original path used to base the Servlet invocation on.
     *
     * @return the full invocation path
     */
    String getInvocationPath();

    /**
     * Get the servlet name.
     *
     * @return the servlet name.
     */
    String getServletName();

    /**
     * Get the servlet path.
     *
     * @return the servlet path.
     */
    String getServletPath();

    /**
     * Get the path info.
     *
     * @return the path info.
     */
    String getPathInfo();

    /**
     * Get the web application request mapping.
     *
     * @return the web application request mapping.
     */
    WebApplicationRequestMapping getApplicationRequestMapping();

    /**
     * Get the servlet environment.
     *
     * @return the servlet environment.
     */
    ServletEnvironment getServletEnvironment();

    /**
     * Get the filter environments.
     *
     * @return the filter environments.
     */
    List<FilterEnvironment> getFilterEnvironments();

    /**
     * Get the filter chain.
     *
     * @return the filter chain.
     */
    FilterChain getFilterChain();

    /**
     * Do we have a servlet.
     *
     * @return true if we do, false otherwise.
     */
    default boolean hasServlet() {
        return getServletEnvironment() != null && getServletEnvironment().getServlet() != null;
    }

    /**
     * Do we have a filter.
     *
     * @return true if we do, false otherwise.
     */
    default boolean hasFilter() {
        return getFilterEnvironments() != null;
    }

    /**
     * Is the servlet unavailable.
     *
     * @return true if it is, false otherwise.
     */
    default boolean isServletUnavailable() {
        return getServletEnvironment() != null && getServletEnvironment().getStatus() == UNAVAILABLE;
    }

    /**
     * Can we invoke.
     *
     * @return true if we can, false otherwise.
     */
    default boolean canInvoke() {
        return hasServlet() || hasFilter();
    }

    /**
     * Get the servlet configuration.
     *
     * @return the servlet configuration.
     */
    default ServletConfig getServletConfig() {
        if (!hasServlet()) {
            return null;
        }
        return getServletEnvironment().getServlet().getServletConfig();
    }
}
