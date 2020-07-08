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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The web.xml in object format.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXml implements Serializable {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 6143204024206508136L;

    /**
     * Stores the security constraints
     */
    public List<SecurityConstraint> securityConstraints = new ArrayList<>();

    /**
     * The &lt;security-constraint&gt; snippet inside a web.xml /
     * webfragment.xml.
     */
    public static class SecurityConstraint {

        // Example:
        //  <security-constraint>
        //      <web-resource-collection>
        //          <web-resource-name>SecureServlet</web-resource-name>
        //          <url-pattern>/SecureServlet</url-pattern>
        //          <http-method>GET</http-method>
        //          <http-method>POST</http-method>
        //      </web-resource-collection>
        //      <auth-constraint>
        //          <role-name>someRole</role-name>
        //      </auth-constraint>
        //      <user-data-constraint>
        //          <transport-guarantee>NONE</transport-guarantee>
        //      </user-data-constraint>
        //  </security-constraint>
        public List<WebResourceCollection> webResourceCollections = new ArrayList<>();

        /**
         * The list &lt;role-name&gt; snippets inside &lt;auth-constraint&gt;
         *
         * Note that we don't map the &lt;auth-constraint&gt; element separately
         * here
         */
        public List<String> roleNames = new ArrayList<>();

        /**
         * The list &lt;transport-guarantee&gt; snippet inside
         * &lt;user-data-constraint&gt;
         *
         * Note that we don't map the &lt;user-data-constraint&gt; element
         * separately here
         */
        public String transportGuarantee;

        /**
         * The &lt;web-resource-collection&gt; snippet inside a web.xml /
         * webfragment.xml.
         */
        public static class WebResourceCollection {

            /**
             * The list &lt;url-pattern&gt; snippets inside
             * &lt;web-resource-collection&gt;
             */
            public List<String> urlPatterns = new ArrayList<>();

            /**
             * The list &lt;http-method&gt; snippets inside
             * &lt;web-resource-collection&gt;
             */
            public List<String> httpMethods = new ArrayList<>();

            /**
             * The list &lt;http-method-omission&gt; snippets inside
             * &lt;web-resource-collection&gt;
             */
            public List<String> httpMethodOmissions = new ArrayList<>();
        }
    }

    // -------------------------------------------------------------------------
    /**
     * Stores the context params.
     */
    private final ArrayList<WebXmlContextParam> contextParams = new ArrayList<>();

    /**
     * Stores the default context path.
     */
    private String defaultContextPath;

    /**
     * Store if we are denying uncovered HTTP methods.
     */
    private boolean denyUncoveredHttpMethods;

    /**
     * Store the display name.
     */
    private String displayName;

    /**
     * Store if we are distributable.
     */
    private boolean distributable;

    /**
     * Stores the error pages.
     */
    private final ArrayList<WebXmlErrorPage> errorPages = new ArrayList<>();

    /**
     * Stores the filters.
     */
    private final ArrayList<WebXmlFilter> filters = new ArrayList<>();

    /**
     * Stores the filter mappings.
     */
    private final ArrayList<WebXmlFilterMapping> filterMappings = new ArrayList<>();

    /**
     * Stores if we are a fragment.
     */
    private boolean fragment;

    /**
     * Stores the fragment name.
     */
    private String fragmentName;

    /**
     * Stores the listeners.
     */
    private final ArrayList<WebXmlListener> listeners = new ArrayList<>();

    /**
     * Stores the login config.
     */
    private WebXmlLoginConfig loginConfig;

    /**
     * Stores the mime mappings.
     */
    private final ArrayList<WebXmlMimeMapping> mimeMappings = new ArrayList<>();

    /**
     * Stores the request character encoding.
     */
    private String requestCharacterEncoding;

    /**
     * Stores the response character encoding.
     */
    private String responseCharacterEncoding;

    /**
     * Stores the servlets.
     */
    private final List<WebXmlServlet> servlets = new ArrayList<>();

    /**
     * Stores the servlet mappings.
     */
    private final ArrayList<WebXmlServletMapping> servletMappings = new ArrayList<>();

    /**
     * Stores the session configuration.
     */
    private WebXmlSessionConfig sessionConfig;

    /**
     * Stores the welcome files.
     */
    private final ArrayList<String> welcomeFiles = new ArrayList<>();

    /**
     * Get the context params.
     *
     * @return the context params.
     */
    public List<WebXmlContextParam> getContextParams() {
        return contextParams;
    }

    /**
     * Get the default context path.
     *
     * @return the default context path.
     */
    public String getDefaultContextPath() {
        return defaultContextPath;
    }

    /**
     * Get if we are denying uncovered HTTP methods.
     *
     * @return true if we are, false otherwise.
     */
    public boolean getDenyUncoveredHttpMethods() {
        return denyUncoveredHttpMethods;
    }

    /**
     * Get the display name.
     *
     * @return the display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the error pages.
     *
     * @return the error pages.
     */
    public List<WebXmlErrorPage> getErrorPages() {
        return errorPages;
    }

    /**
     * Get the filters.
     *
     * @return the filters.
     */
    public List<WebXmlFilter> getFilters() {
        return filters;
    }

    /**
     * Get the filter mappings.
     *
     * @return the filter mappings.
     */
    public List<WebXmlFilterMapping> getFilterMappings() {
        return filterMappings;
    }

    /**
     * Get the fragment name.
     *
     * @return the fragment name.
     */
    public String getFragmentName() {
        return fragmentName;
    }

    /**
     * Get the login config.
     *
     * @return the login config.
     */
    public WebXmlLoginConfig getLoginConfig() {
        return loginConfig;
    }

    /**
     * Get the listeners.
     *
     * @return the listeners.
     */
    public List<WebXmlListener> getListeners() {
        return listeners;
    }

    /**
     * Get the mime mappings.
     *
     * @return the mime mappings.
     */
    public List<WebXmlMimeMapping> getMimeMappings() {
        return mimeMappings;
    }

    /**
     * Get the request character encoding.
     *
     * @return the request character encoding.
     */
    public String getRequestCharacterEncoding() {
        return requestCharacterEncoding;
    }

    /**
     * Get the response character encoding.
     *
     * @return the response character encoding.
     */
    public String getResponseCharacterEncoding() {
        return responseCharacterEncoding;
    }

    /**
     * Get the servlets.
     *
     * @return the servlets.
     */
    public List<WebXmlServlet> getServlets() {
        return servlets;
    }

    /**
     * Get the servlet mappings.
     *
     * @return the servlet mappings.
     */
    public List<WebXmlServletMapping> getServletMappings() {
        return servletMappings;
    }

    /**
     * Get the session config.
     *
     * @return the session config.
     */
    public WebXmlSessionConfig getSessionConfig() {
        return sessionConfig;
    }

    /**
     * Get the welcome files.
     *
     * @return welcome files.
     */
    public List<String> getWelcomeFiles() {
        return welcomeFiles;
    }

    /**
     * Is the application distributable.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isDistributable() {
        return distributable;
    }

    /**
     * Is this a web-fragment.
     *
     * @return true if it, false otherwise.
     */
    public boolean isFragment() {
        return fragment;
    }

    /**
     * Set the default context path.
     *
     * @param defaultContextPath the default context path.
     */
    public void setDefaultContextPath(String defaultContextPath) {
        this.defaultContextPath = defaultContextPath;
    }

    /**
     * Set if we are denying uncovered HTTP methods.
     *
     * @param denyUncoveredHttpMethods the boolean value.
     */
    public void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods) {
        this.denyUncoveredHttpMethods = denyUncoveredHttpMethods;
    }

    /**
     * Set the display name.
     *
     * @param displayName the display name.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Set if we are distributable.
     *
     * @param distributable the boolean value.
     */
    public void setDistributable(boolean distributable) {
        this.distributable = distributable;
    }

    /**
     * Set if we are a fragment.
     *
     * @param fragment the boolean value.
     */
    public void setFragment(boolean fragment) {
        this.fragment = fragment;
    }

    /**
     * Set the fragment name.
     *
     * @param fragmentName the fragment name.
     */
    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    /**
     * Set the login config.
     *
     * @param loginConfig the login config.
     */
    public void setLoginConfig(WebXmlLoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }

    /**
     * Set the request character encoding.
     *
     * @param requestCharacterEncoding the request character encoding.
     */
    public void setRequestCharacterEncoding(String requestCharacterEncoding) {
        this.requestCharacterEncoding = requestCharacterEncoding;
    }

    /**
     * Set the response character encoding.
     *
     * @param responseCharacterEncoding the response character encoding.
     */
    public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        this.responseCharacterEncoding = responseCharacterEncoding;
    }

    /**
     * Set the session config.
     *
     * @param sessionConfig the session comfig.
     */
    public void setSessionConfig(WebXmlSessionConfig sessionConfig) {
        this.sessionConfig = sessionConfig;
    }
}
