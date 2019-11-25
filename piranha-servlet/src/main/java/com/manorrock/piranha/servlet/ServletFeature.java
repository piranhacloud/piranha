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

import com.manorrock.piranha.webxml.WebXmlInitializer;
import cloud.piranha.api.Feature;
import cloud.piranha.api.WebApplication;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Servlet feature.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletFeature implements Feature {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ServletFeature.class.getName());

    /**
     * Stores the Pages Jasper initializer class name.
     */
    private static final String PAGES_INITIALIZER
            = "com.manorrock.piranha.pages.jasper.JasperInitializer";

    /**
     * Stores the Mojarra initializer class name.
     */
    private static final String MOJARRA_INITIALIZER
            = "com.manorrock.piranha.faces.mojarra.MojarraInitializer";

    /**
     * Stores the MyFaces initializer class name.
     */
    private static final String MYFACES_INITIALIZER
            = "com.manorrock.piranha.faces.myfaces.MyFacesInitializer";

    /**
     * Stores the OpenWebBeans initializer class name.
     */
    private static final String OPENWEBBEANS_INITIALIZER
            = "com.manorrock.piranha.cdi.openwebbeans.OpenWebBeansInitializer";

    /**
     * Stores the Weld initializer class name.
     */
    private static final String WELD_INITIALIZER
            = "com.manorrock.piranha.cdi.weld.WeldInitializer";

    /**
     * Initialize the feature.
     *
     * @param webApplication the web application.
     */
    @Override
    public void initialize(WebApplication webApplication) {
        webApplication.addInitializer(new LiveObjectAnnotationScannerInitializer());
        webApplication.addInitializer(new WebXmlInitializer());
        webApplication.addInitializer(new WebAnnotationInitializer());

        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Adding Jasper initializer");
            }
            getClass().getClassLoader().loadClass(PAGES_INITIALIZER);
            webApplication.addInitializer(PAGES_INITIALIZER);
        } catch (ClassNotFoundException cnfe) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Unable to add Jasper initializer");
            }
        }
        try {
            getClass().getClassLoader().loadClass(OPENWEBBEANS_INITIALIZER);
            webApplication.addInitializer(OPENWEBBEANS_INITIALIZER);
        } catch (ClassNotFoundException cnfe) {
        }
        try {
            getClass().getClassLoader().loadClass(WELD_INITIALIZER);
            webApplication.addInitializer(WELD_INITIALIZER);
        } catch (ClassNotFoundException cnfe) {
        }
        try {
            getClass().getClassLoader().loadClass(MOJARRA_INITIALIZER);
            webApplication.addInitializer(MOJARRA_INITIALIZER);
        } catch (ClassNotFoundException cnfe) {
        }
        try {
            getClass().getClassLoader().loadClass(MYFACES_INITIALIZER);
            webApplication.addInitializer(MYFACES_INITIALIZER);
        } catch (ClassNotFoundException cnfe) {
        }
    }
}
