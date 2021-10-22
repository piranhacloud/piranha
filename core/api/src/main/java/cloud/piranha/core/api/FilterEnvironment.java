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
package cloud.piranha.core.api;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.FilterRegistration.Dynamic;
import jakarta.servlet.ServletException;

/**
 *
 * @author Arjan Tijms
 *
 */
public interface FilterEnvironment extends Dynamic, FilterConfig {

    /**
     * Defines the UNAVAILABLE constant.
     */
    int UNAVAILABLE = -1;

    /**
     * {@return the filter}
     */
    Filter getFilter();

    /**
     * Initialize the filter.
     *
     * @throws ServletException when a servlet error occurs.
     */
    void initialize() throws ServletException;

    /**
     * Set the class name.
     *
     * @param className the class name.
     */
    void setClassName(String className);

    /**
     * Set the filter name.
     *
     * @param filterName the filter name.
     */
    void setFilterName(String filterName);

    /**
     * Set status.
     *
     * @param status the status.
     */
    void setStatus(int status);

    /**
     * {@return the web application}
     */
    WebApplication getWebApplication();

    /**
     * Set the web application.
     *
     * @param webApplication the web application.
     */
    void setWebApplication(WebApplication webApplication);

    /**
     * Is async supported.
     *
     * @return true if it is, false otherwise.
     */
    boolean isAsyncSupported();
}
