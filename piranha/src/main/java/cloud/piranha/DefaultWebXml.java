/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package cloud.piranha;

import cloud.piranha.api.WebXml;
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
public class DefaultWebXml implements Serializable, WebXml {

    private static final long serialVersionUID = 6143204024206508136L;

    /**
     * Stores the servlets.
     */
    public List<DefaultWebXml.Servlet> servlets = new ArrayList<>();

    /**
     * Stores the servlet mappings.
     */
    public List<DefaultWebXml.ServletMapping> servletMappings = new ArrayList<>();

    /**
     * Stores the security constraints
     */
    public List<SecurityConstraint> securityConstraints = new ArrayList<>();

    /**
     * Stores the login config - This sets and configures the build-in Servlet
     * authentication mechanisms.
     *
     */
    public LoginConfig loginConfig = new LoginConfig();

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

    /**
     * The &lt;login-config&gt; snippet inside a web.xml / webfragment.xml.
     */
    public static class LoginConfig {

        /**
         * The list &lt;auth-method-name&gt; snippet inside &lt;login-config&gt;
         *
         */
        public String authMethod;

        /**
         * The list &lt;realm-name&gt; snippet inside &lt;login-config&gt;
         *
         */
        public String realmName;

        /**
         * The list &lt;form-login-page&gt; snippet inside
         * &lt;form-login-config&gt;
         *
         * Note that we don't map the &lt;form-login-config&gt; element
         * separately here
         */
        public String formLoginPage;

        /**
         * The list &lt;form-error-page&gt; snippet inside
         * &lt;form-login-config&gt;
         *
         * Note that we don't map the &lt;form-login-config&gt; element
         * separately here
         */
        public String formErrorPage;
    }

    /**
     * The &lt;servlet-mapping&gt; snippet inside a web.xml / webfragment.xml.
     */
    public static class ServletMapping {

        /**
         * Stores the servlet name.
         */
        public String servletName;

        /**
         * Stores the URL pattern.
         */
        public String urlPattern;
    }

    // -------------------------------------------------------------------------
    /**
     * The error-page.
     */
    class DefaultErrorPage implements ErrorPage {

        /**
         * Stores the error code.
         */
        private final String errorCode;

        /**
         * Stores the exception type.
         */
        private final String exceptionType;

        /**
         * Stores the location.
         */
        private final String location;

        /**
         * Constructor.
         * 
         * @param errorCode the error code.
         * @param exceptionType the exception type.
         * @param location the location.
         */
        public DefaultErrorPage(String errorCode, String exceptionType, String location) {
            this.errorCode = errorCode;
            this.exceptionType = exceptionType;
            this.location = location;
        }

        /**
         * Get the error code.
         * 
         * @return the error code.
         */
        @Override
        public String getErrorCode() {
            return errorCode;
        }

        /**
         * Get the exception type.
         * 
         * @return the exception type.
         */
        @Override
        public String getExceptionType() {
            return exceptionType;
        }

        /**
         * Get the location.
         * 
         * @return the location.
         */
        @Override
        public String getLocation() {
            return location;
        }
    }
    
    /**
     * Stores the error pages.
     */
    ArrayList<ErrorPage> errorPages = new ArrayList<>();

    /**
     * Add error page.
     * 
     * @param errorCode the error code.
     * @param exceptionType the exception type.
     * @param location the location.
     */
    @Override
    public void addErrorPage(String errorCode, String exceptionType, String location) {
        errorPages.add(new DefaultErrorPage(errorCode, exceptionType, location));
    }

    /**
     * Get the error pages.
     * 
     * @return the error pages.
     */
    @Override
    public Collection<ErrorPage> getErrorPages() {
        return Collections.unmodifiableCollection(errorPages);
    }
    
    // -------------------------------------------------------------------------
    /**
     * The context-param.
     */
    class DefaultContextParam implements ContextParam {

        /**
         * Stores the name.
         */
        private final String name;

        /**
         * Stores the value.
         */
        private final String value;

        /**
         * Constructor.
         *
         * @param name the name.
         * @param value the value.
         */
        private DefaultContextParam(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Get the name.
         *
         * @return the name.
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * Get the value.
         *
         * @return the value.
         */
        @Override
        public String getValue() {
            return value;
        }
    }

    /**
     * Stores the context params.
     */
    ArrayList<ContextParam> contextParams = new ArrayList<>();

    /**
     * Add a context param.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void addContextParam(String name, String value) {
        contextParams.add(new DefaultContextParam(name, value));
    }

    /**
     * Get the context params.
     *
     * @return the context params.
     */
    @Override
    public Collection<ContextParam> getContextParams() {
        return Collections.unmodifiableCollection(contextParams);
    }

    // -------------------------------------------------------------------------
    /**
     * The listener.
     */
    class DefaultListener implements Listener {

        /**
         * Stores the class name.
         */
        String className;

        /**
         * Constructor.
         */
        public DefaultListener(String className) {
            this.className = className;
        }

        /**
         * Get the class name.
         *
         * @return the class name.
         */
        @Override
        public String getClassName() {
            return className;
        }
    }

    /**
     * Stores the listeners.
     */
    ArrayList<Listener> listeners = new ArrayList<>();

    /**
     * Add a listener.
     *
     * @param className the class name.
     */
    @Override
    public void addListener(String className) {
        listeners.add(new DefaultListener(className));
    }

    /**
     * Get the listeners.
     *
     * @return the listeners.
     */
    @Override
    public Collection<Listener> getListeners() {
        return Collections.unmodifiableCollection(listeners);
    }

    // -------------------------------------------------------------------------
    /**
     * The mime-mapping.
     */
    class DefaultMimeMapping implements MimeMapping {

        /**
         * Stores the extension.
         */
        private final String extension;

        /**
         * Stores the mime type.
         */
        private final String mimeType;

        /**
         * Constructor.
         *
         * @param extension the extension.
         * @param mimeType the mime type.
         */
        private DefaultMimeMapping(String extension, String mimeType) {
            this.extension = extension;
            this.mimeType = mimeType;
        }

        /**
         * Get the extension.
         *
         * @return the extension.
         */
        @Override
        public String getExtension() {
            return extension;
        }

        /**
         * Get the mime type.
         *
         * @return the mime type.
         */
        @Override
        public String getMimeType() {
            return mimeType;
        }
    }

    /**
     * Stores the mime mappings.
     */
    ArrayList<MimeMapping> mimeMappings = new ArrayList<>();

    /**
     * Add a mime mapping.
     *
     * @param extension the extension.
     * @param mimeType the mime type.
     */
    @Override
    public void addMimeMapping(String extension, String mimeType) {
        mimeMappings.add(new DefaultMimeMapping(extension, mimeType));
    }

    /**
     * Get the mime mappings.
     *
     * @return the mime mappings.
     */
    @Override
    public Collection<MimeMapping> getMimeMappings() {
        return Collections.unmodifiableCollection(mimeMappings);
    }
}
