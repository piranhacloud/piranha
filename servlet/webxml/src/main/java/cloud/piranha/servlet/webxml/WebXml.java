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
package cloud.piranha.servlet.webxml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The web.xml in object format.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXml implements Serializable {

    private static final long serialVersionUID = 6143204024206508136L;

    /**
     * Stores the context parameters.
     */
    public List<ContextParameter> contextParameters = new ArrayList<>();

    /**
     * Stores the listeners.
     */
    public List<WebXml.Listener> listeners = new ArrayList<>();

    /**
     * Stores the mime mappings.
     */
    public List<WebXml.MimeMapping> mimeMappings = new ArrayList<>();

    /**
     * Stores the servlets.
     */
    public List<WebXml.Servlet> servlets = new ArrayList<>();

    /**
     * Stores the servlet mappings.
     */
    public List<WebXml.ServletMapping> servletMappings = new ArrayList<>();
    
    /**
     * Stores the error pages.
     */
    public List<ErrorPage> errorPages = new ArrayList<>();

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
     * When true, this boolean causes HTTP methods that are not subject to a security constraint to be denied. 
     */
    public boolean denyUncoveredHttpMethods;
    

    /**
     * The &lt;listener&gt; snippet inside a web.xml / webfragment.xml.
     */
    public static class Listener {

        /**
         * Stores the listener class name.
         */
        public String className;
    }

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
         * The list &lt;auth-method-name&gt; snippet inside
         * &lt;login-config&gt;
         * 
         */
        public String authMethod;
        
        /**
         * The list &lt;realm-name&gt; snippet inside
         * &lt;login-config&gt;
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
    
    /**
     * The &lt;error-page&gt; snippet inside a web.xml / webfragment.xml.
     */
    public static class ErrorPage {
     
        /**
         * Stores the code corresponding to the error-code element
         */
        public String errorCode;
        
        /**
         * Stores the exception corresponding to the exception-type element
         */
        public String exceptionType;
        
        /**
         * Stores the location of the resource corresponding to the location element
         */
        public String location;
        
    }

    /**
     * The &lt;mime-mapping&gt; snippet inside a web.xml / webfragment.xml.
     */
    public static class MimeMapping {

        /**
         * Stores the extension.
         */
        public String extension;

        /**
         * Stores the mime type.
         */
        public String mimeType;
        
        /**
         * Set the extension.
         * 
         * @param extension the extension.
         */
        public void setExtension(String extension) {
            this.extension = extension;
        }
    }

    /**
     * Stores the context parameter.
     */
    public static class ContextParameter {

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
