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
package cloud.piranha.extension.webxml;

import java.util.ArrayList;
import java.util.List;

/**
 * The &lt;security-constraint&gt; snippet inside a web.xml / webfragment.xml.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlSecurityConstraint {

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
    private List<WebResourceCollection> webResourceCollections = new ArrayList<>();

    /**
     * The list &lt;role-name&gt; snippets inside &lt;auth-constraint&gt;
     *
     * Note that we don't map the &lt;auth-constraint&gt; element separately
     * here
     */
    private List<String> roleNames = new ArrayList<>();

    /**
     * The list &lt;transport-guarantee&gt; snippet inside
     * &lt;user-data-constraint&gt;
     *
     * Note that we don't map the &lt;user-data-constraint&gt; element
     * separately here
     */
    private String transportGuarantee;

    /**
     * Get the transport guarantee.
     *
     * @return the transport guarantee.
     */
    public String getTransportGuarantee() {
        return transportGuarantee;
    }

    /**
     * Set the transport guarantee.
     *
     * @param transportGuarantee the transport guarantee.
     */
    public void setTransportGuarantee(String transportGuarantee) {
        this.transportGuarantee = transportGuarantee;
    }

    /**
     * Get the role names.
     *
     * @return the role names.
     */
    public List<String> getRoleNames() {
        return roleNames;
    }

    /**
     * Set the role names.
     *
     * @param roleNames the role names.
     */
    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    /**
     * Get the web resource collections.
     *
     * @return the web resource collections.
     */
    public List<WebResourceCollection> getWebResourceCollections() {
        return webResourceCollections;
    }

    /**
     * Set the web resource collections.
     *
     * @param webResourceCollections the web resource collections.
     */
    public void setWebResourceCollections(List<WebResourceCollection> webResourceCollections) {
        this.webResourceCollections = webResourceCollections;
    }

    /**
     * The &lt;web-resource-collection&gt; snippet inside a web.xml /
     * webfragment.xml.
     */
    public static class WebResourceCollection {

        /**
         * The list &lt;url-pattern&gt; snippets inside
         * &lt;web-resource-collection&gt;
         */
        private List<String> urlPatterns = new ArrayList<>();

        /**
         * The list &lt;http-method&gt; snippets inside
         * &lt;web-resource-collection&gt;
         */
        private List<String> httpMethods = new ArrayList<>();

        /**
         * The list &lt;http-method-omission&gt; snippets inside
         * &lt;web-resource-collection&gt;
         */
        private List<String> httpMethodOmissions = new ArrayList<>();

        /**
         * Get the URL patterns.
         *
         * @return the URL patterns.
         */
        public List<String> getUrlPatterns() {
            return urlPatterns;
        }

        /**
         * Set the URL patterns.
         *
         * @param urlPatterns the URL patterns.
         */
        public void setUrlPatterns(List<String> urlPatterns) {
            this.urlPatterns = urlPatterns;
        }

        /**
         * Get the HTTP methods.
         *
         * @return the HTTP methods.
         */
        public List<String> getHttpMethods() {
            return httpMethods;
        }

        /**
         * Set the HTTP methods.
         *
         * @param httpMethods the HTTP methods.
         */
        public void setHttpMethods(List<String> httpMethods) {
            this.httpMethods = httpMethods;
        }

        /**
         * Get the HTTP method omissions.
         *
         * @return the HTTP method omissions.
         */
        public List<String> getHttpMethodOmissions() {
            return httpMethodOmissions;
        }

        /**
         * Set the HTTP method omissions.
         *
         * @param httpMethodOmissions the HTTP method omissions.
         */
        public void setHttpMethodOmissions(List<String> httpMethodOmissions) {
            this.httpMethodOmissions = httpMethodOmissions;
        }
    }
}
