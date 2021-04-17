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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The web.xml in object format.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXml implements Serializable {

    /**
     * Stores the others tag.
     */
    public static final String OTHERS_TAG = WebXml.class.getName() + ".ordering.others";

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = 6143204024206508136L;

    /**
     * Stores the major version.
     */
    public int majorVersion = -1;

    /**
     * Stores the minor version.
     */
    public int minorVersion = -1;

    /**
     * Stores the security constraints
     */
    public List<SecurityConstraint> securityConstraints = new ArrayList<>();

    /**
     * Stores the metadata complete flag.
     */
    private boolean metadataComplete;

    /**
     * Stores the absolute ordering.
     */
    private List<String> absoluteOrdering;

    /**
     * Stores the locale encoding mapping.
     */
    private Map<String, String> localeEncodingMapping = new HashMap<>();

    /**
     * A class used to deal with before/after ordering.
     */
    public static class RelativeOrder {

        /**
         * Stores the before.
         */
        private List<String> before;

        /**
         * Stores the after.
         */
        private List<String> after;

        /**
         * Constructor.
         */
        public RelativeOrder() {
            before = Collections.emptyList();
            after = Collections.emptyList();
        }

        /**
         * Constructor.
         *
         * @param before the before.
         * @param after the after.
         */
        public RelativeOrder(List<String> before, List<String> after) {
            this.before = Objects.requireNonNullElseGet(before, Collections::emptyList);
            this.after = Objects.requireNonNullElseGet(after, Collections::emptyList);
        }

        /**
         * {@return the before}
         */
        public List<String> getBefore() {
            return before;
        }

        /**
         * Set the before.
         *
         * @param before the before.
         */
        public void setBefore(List<String> before) {
            this.before = before;
        }

        /**
         * {@return the after}
         */
        public List<String> getAfter() {
            return after;
        }

        /**
         * Set the after.
         *
         * @param after the after.
         */
        public void setAfter(List<String> after) {
            this.after = after;
        }
    }

    /**
     * Stores the relative ordering.
     */
    private RelativeOrder relativeOrdering;

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
        /**
         * Stores the web resource collections.
         */
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
    private final List<WebXmlContextParam> contextParams = new ArrayList<>();

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
    private final List<WebXmlErrorPage> errorPages = new ArrayList<>();

    /**
     * Stores the filters.
     */
    private final List<WebXmlFilter> filters = new ArrayList<>();

    /**
     * Stores the filter mappings.
     */
    private final List<WebXmlFilterMapping> filterMappings = new ArrayList<>();

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
    private final List<WebXmlListener> listeners = new ArrayList<>();

    /**
     * Stores the login config.
     */
    private WebXmlLoginConfig loginConfig;

    /**
     * Stores the mime mappings.
     */
    private final List<WebXmlMimeMapping> mimeMappings = new ArrayList<>();

    /**
     * Stores the request character encoding.
     */
    private String requestCharacterEncoding;

    /**
     * Stores the response character encoding.
     */
    private String responseCharacterEncoding;

    /**
     * Set of all unique role names that have either been explicitly declared,
     * or used in a constraint.
     */
    private final Set<String> roleNames = new HashSet<>();

    /**
     * Stores the servlets.
     */
    private final List<WebXmlServlet> servlets = new ArrayList<>();

    /**
     * Stores the servlet mappings.
     */
    private final List<WebXmlServletMapping> servletMappings = new ArrayList<>();

    /**
     * Stores the session configuration.
     */
    private WebXmlSessionConfig sessionConfig;

    /**
     * Stores the welcome files.
     */
    private final List<String> welcomeFiles = new ArrayList<>();

    /**
     * {@return the context params}
     */
    public List<WebXmlContextParam> getContextParams() {
        return contextParams;
    }

    /**
     * {@return the default context path}
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
     * {@return the display name}
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * {@return the error pages}
     */
    public List<WebXmlErrorPage> getErrorPages() {
        return errorPages;
    }

    /**
     * {@return the filters}
     */
    public List<WebXmlFilter> getFilters() {
        return filters;
    }

    /**
     * {@return the filter mappings}
     */
    public List<WebXmlFilterMapping> getFilterMappings() {
        return filterMappings;
    }

    /**
     * {@return the fragment name}
     */
    public String getFragmentName() {
        return fragmentName;
    }

    /**
     * {@return the login config}
     */
    public WebXmlLoginConfig getLoginConfig() {
        return loginConfig;
    }

    /**
     * {@return the listeners}
     */
    public List<WebXmlListener> getListeners() {
        return listeners;
    }

    /**
     * {@return the mime mappings}
     */
    public List<WebXmlMimeMapping> getMimeMappings() {
        return mimeMappings;
    }

    /**
     * {@return the request character encoding}
     */
    public String getRequestCharacterEncoding() {
        return requestCharacterEncoding;
    }

    /**
     * {@return the response character encoding}
     */
    public String getResponseCharacterEncoding() {
        return responseCharacterEncoding;
    }

    /**
     * Get all the unique role names that have either been explicitly declared,
     * or used in a constraint.
     *
     * @return the unique role names that have either been explicitly declared,
     * or used in a constraint.
     */
    public Set<String> getRoleNames() {
        return roleNames;
    }

    /**
     * {@return the servlets}
     */
    public List<WebXmlServlet> getServlets() {
        return servlets;
    }

    /**
     * {@return the servlet mappings}
     */
    public List<WebXmlServletMapping> getServletMappings() {
        return servletMappings;
    }

    /**
     * {@return the session config}
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

    /**
     * Gets the major version as set by the version attribute in the web app
     * element
     *
     * @return the major version.
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Sets the major version as set by the version attribute in the web app
     * element
     *
     * @param majorVersion the major version.
     */
    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    /**
     * Gets the minor version as set by the version attribute in the web app
     * element
     *
     * @return the major version.
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * Sets the minor version as set by the version attribute in the web app
     * element
     *
     * @param minorVersion the minor version.
     */
    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    /**
     * Set the metadata complete flag.
     *
     * @param metadataComplete the metadata complete
     */
    public void setMetadataComplete(boolean metadataComplete) {
        this.metadataComplete = metadataComplete;
    }

    /**
     * Get the metadata complete flag.
     *
     * @return the metadata complete
     */
    public boolean getMetadataComplete() {
        return metadataComplete;
    }

    /**
     * {@return the absolute ordering}
     */
    public List<String> getAbsoluteOrdering() {
        return absoluteOrdering;
    }

    /**
     * {@return the relative ordering}
     */
    public RelativeOrder getRelativeOrdering() {
        return relativeOrdering;
    }

    /**
     * Set the absolute ordering.
     *
     * @param absoluteOrdering the absolute ordering.
     */
    public void setAbsoluteOrdering(List<String> absoluteOrdering) {
        this.absoluteOrdering = absoluteOrdering;
    }

    /**
     * Set the relative ordering.
     *
     * @param relativeOrdering the relative ordering.
     */
    public void setRelativeOrdering(RelativeOrder relativeOrdering) {
        this.relativeOrdering = relativeOrdering;
    }

    /**
     * {@return the locale encoding mapping}
     */
    public Map<String, String> getLocaleEncodingMapping() {
        return this.localeEncodingMapping;
    }

    /**
     * Set the locale encoding mapping.
     *
     * @param localeEncodingMapping the locale encoding mapping.
     */
    public void setLocaleEncodingMapping(Map<String, String> localeEncodingMapping) {
        this.localeEncodingMapping = localeEncodingMapping;
    }
}
