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
package com.manorrock.piranha.servlet;

import java.util.ArrayList;
import java.util.List;

/**
 * The web.xml in object format.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class WebXml {

    /**
     * Stores the context parameters.
     */
    public final List<ContextParameter> contextParameters;

    /**
     * Stores the listeners.
     */
    public final List<WebXml.Listener> listeners;

    /**
     * Stores the mime mappings.
     */
    public final List<WebXml.MimeMapping> mimeMappings;

    /**
     * Stores the servlets.
     */
    public final List<WebXml.Servlet> servlets;

    /**
     * Stores the servlet mappings.
     */
    public final List<WebXml.ServletMapping> servletMappings;

    /**
     * Constructor.
     */
    public WebXml() {
        contextParameters = new ArrayList<>();
        listeners = new ArrayList<>();
        mimeMappings = new ArrayList<>();
        servlets = new ArrayList<>();
        servletMappings = new ArrayList<>();
    }

    /**
     * The &lt;servlet&gt; snippet inside a web.xml / webfragment.xml.
     */
    public static class Servlet {

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
        List<InitParam> initParams = new ArrayList<>();

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
     * The &lt;security-constraint&gt; snippet inside a web.xml / webfragment.xml.
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
        
        List<WebResourceCollection> webResourceCollections = new ArrayList<>();
        
        /**
         * The list &lt;role-name&gt; snippets inside &lt;auth-constraint&gt;
         * 
         * Note that we don't map the &lt;auth-constraint&gt element separately here
         */
        List<String> roleNames;
        
        /**
         * The list &lt;transport-guarantee&gt; snippet inside &lt;user-data-constraint&gt;
         * 
         * Note that we don't map the &lt;user-data-constraint&gt element separately here
         */
        String transportGuarantee;
        
        /**
         * The &lt;web-resource-collection&gt; snippet inside a web.xml / webfragment.xml.
         */
        public class WebResourceCollection {
            
            /**
             * The list &lt;url-pattern&gt; snippets inside &lt;web-resource-collection&gt;
             */
            List<String> urlPatterns;
            
            /**
             * The list &lt;http-method&gt; snippets inside &lt;web-resource-collection&gt;
             */
            List<String> httpMethods;
            
            /**
             * The list &lt;http-method-omission&gt; snippets inside &lt;web-resource-collection&gt;
             */
            List<String> httpMethodOmissions;
        }
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
     * The &lt;listener&gt; snippet inside a web.xml / webfragment.xml.
     */
    public static class Listener {

        /**
         * Stores the listener class name.
         */
        public String className;
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
