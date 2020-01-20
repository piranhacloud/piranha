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
package cloud.piranha.servlet.webxml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The web.xml in object format.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXml implements Serializable {

    private static final long serialVersionUID = 6143204024206508136L;

    /**
     * Stores the servlets.
     */
    public List<WebXml.Servlet> servlets = new ArrayList<>();

    /**
     * Stores the security constraints
     */
    public List<SecurityConstraint> securityConstraints = new ArrayList<>();

    /**
     * When true, this boolean causes HTTP methods that are not subject to a
     * security constraint to be denied.
     */
    public boolean denyUncoveredHttpMethods;

    /**
     * The &lt;servlet&gt; snippet inside a web.xml / webfragment.xml.
     */
    public static class Servlet {

        /**
         * Stores the async supported flag.
         */
        public boolean asyncSupported;

        /**
         * Stores the servlet name.
         */
        public String name;

        /**
         * Stores the servlet class.
         */
        public String className;

        /**
         * Stores the load on startup.
         */
        public int loadOnStartup;

        /**
         * Stores the init parameters.
         */
        public List<InitParam> initParams = new ArrayList<>();

        /**
         * The &lt;init-param&gt; snippet inside a &lt;servlet&gt; snippet.
         */
        public static class InitParam {

            /**
             * Stores the name.
             */
            public String name;

            /**
             * Stores the value.
             */
            public String value;
        }
    }

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
     * Stores the servlet mappings.
     */
    private final ArrayList<WebXmlServletMapping> servletMappings = new ArrayList<>();

    /**
     * Add a context param.
     *
     * @param name the name.
     * @param value the value.
     */
    public void addContextParam(String name, String value) {
        contextParams.add(new WebXmlContextParam(name, value));
    }

    /**
     * Add error page.
     *
     * @param errorCode the error code.
     * @param exceptionType the exception type.
     * @param location the location.
     */
    public void addErrorPage(String errorCode, String exceptionType, String location) {
        errorPages.add(new WebXmlErrorPage(errorCode, exceptionType, location));
    }

    /**
     * Add a filter.
     *
     * @param filter the filter.
     */
    public void addFilter(WebXmlFilter filter) {
        filters.add(filter);
    }

    /**
     * Add the filter mappings.
     *
     * @param filterName the filter name.
     * @param urlPattern the URL pattern.
     */
    public void addFilterMapping(String filterName, String urlPattern) {
        filterMappings.add(new WebXmlFilterMapping(filterName, urlPattern));
    }

    /**
     * Add a listener.
     *
     * @param className the class name.
     */
    public void addListener(String className) {
        listeners.add(new WebXmlListener(className));
    }

    /**
     * Add a mime mapping.
     *
     * @param extension the extension.
     * @param mimeType the mime type.
     */
    public void addMimeMapping(String extension, String mimeType) {
        mimeMappings.add(new WebXmlMimeMapping(extension, mimeType));
    }

    /**
     * Add a servlet mapping.
     *
     * @param servletName the servlet name.
     * @param urlPattern the URL pattern.
     */
    public void addServletMapping(String servletName, String urlPattern) {
        servletMappings.add(new WebXmlServletMapping(servletName, urlPattern));
    }

    /**
     * Get the context params.
     *
     * @return the context params.
     */
    public Collection<WebXmlContextParam> getContextParams() {
        return contextParams;
    }

    /**
     * Get the error pages.
     *
     * @return the error pages.
     */
    public Collection<WebXmlErrorPage> getErrorPages() {
        return errorPages;
    }

    /**
     * Get the filters.
     *
     * @return the filters.
     */
    public Collection<WebXmlFilter> getFilters() {
        return filters;
    }

    /**
     * Get the filter mappings.
     *
     * @return the filter mappings.
     */
    public Collection<WebXmlFilterMapping> getFilterMappings() {
        return filterMappings;
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
    public Collection<WebXmlListener> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }

    /**
     * Get the mime mappings.
     *
     * @return the mime mappings.
     */
    public Collection<WebXmlMimeMapping> getMimeMappings() {
        return mimeMappings;
    }

    /**
     * Get the servlet mappings.
     *
     * @return the servlet mappings.
     */
    public Collection<WebXmlServletMapping> getServletMappings() {
        return servletMappings;
    }

    /**
     * Set the login config.
     *
     * @param loginConfig the login config.
     */
    public void setLoginConfig(WebXmlLoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }
}
