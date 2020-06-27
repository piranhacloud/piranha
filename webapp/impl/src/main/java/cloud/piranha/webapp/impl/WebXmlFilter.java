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

import java.util.ArrayList;
import java.util.List;

/**
 * The web.xml filter.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlFilter {

    /**
     * Stores the class name.
     */
    private String className;

    /**
     * Stores the filter name.
     */
    private String filterName;

    /**
     * Stores the init params.
     */
    private final ArrayList<WebXmlFilterInitParam> initParams = new ArrayList<>();

    /**
     * Stores the servlet name.
     */
    private String servletName;

    /**
     * Add init param.
     *
     * @param initParam the init param.
     */
    public void addInitParam(WebXmlFilterInitParam initParam) {
        this.initParams.add(initParam);
    }

    /**
     * Get the class name.
     *
     * @return the class name.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Get the filter name.
     *
     * @return the filter name.
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * Get the init params.
     *
     * @return the init params.
     */
    public List<WebXmlFilterInitParam> getInitParams() {
        return initParams;
    }

    /**
     * Get the servlet name.
     *
     * @return the servlet name.
     */
    public String getServletName() {
        return servletName;
    }

    /**
     * Set the class name.
     *
     * @param className the class name.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Set the filter name.
     *
     * @param filterName the filter name.
     */
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    /**
     * Set the servlet name.
     *
     * @param servletName the servlet name.
     */
    public void setServletName(String servletName) {
        this.servletName = servletName;
    }
}
