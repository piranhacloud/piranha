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
}
