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
package cloud.piranha.servlet4.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * The web.xml servlet.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlServlet {

    /**
     * Stores if async is supported.
     */
    private boolean asyncSupported;

    /**
     * Stores the class name.
     */
    private String className;

    /**
     * Stores the init params.
     */
    private final List<WebXmlServletInitParam> initParams = new ArrayList<>();

    /**
     * Stores the security role refs.
     */
    private final List<WebXmlServletSecurityRoleRef> securityRoleRefs = new ArrayList<>();

    /**
     * Stores the servlet name.
     */
    private String servletName;

    /**
     * Stores the JSP file
     */
    private String jspFile;

    /**
     * Add init param.
     *
     * @param initParam the init param.
     */
    public void addInitParam(WebXmlServletInitParam initParam) {
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
     * Get the init params.
     *
     * @return the init params.
     */
    public List<WebXmlServletInitParam> getInitParams() {
        return initParams;
    }

    /**
     * Get the security role refs.
     *
     * @return the security role refs.
     */
    public List<WebXmlServletSecurityRoleRef> getSecurityRoleRefs() {
        return securityRoleRefs;
    }

    /**
     * Is async supported.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isAsyncSupported() {
        return asyncSupported;
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
     * Set if async is supported.
     *
     * @param asyncSupported the boolean value.
     */
    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    /**
     * Set the servlet name.
     *
     * @param servletName the servlet name.
     */
    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    /**
     * Get the jsp file
     *
     * @return the jsp file
     */
    public String getJspFile() {
        return jspFile;
    }

    /**
     * Set the JSP file
     *
     * @param jspFile - the JSP file
     */
    public void setJspFile(String jspFile) {
        this.jspFile = jspFile;
    }

    /**
     * Return string representation.
     *
     * @return the string representation.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Servlet[");
        builder.append("servletName=").append(servletName).append(",");
        builder.append("className=").append(className).append(",");
        builder.append("jspFile=").append(jspFile).append(",");
        builder.append("asyncSupported=").append(asyncSupported).append("]");
        return builder.toString();
    }
}
