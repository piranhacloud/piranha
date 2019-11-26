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
package cloud.piranha.security.soteria;

import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.glassfish.soteria.SoteriaServiceProviders;
import org.glassfish.soteria.cdi.spi.WebXmlLoginConfig;

import com.manorrock.piranha.webxml.WebXml;

/**
 * The Soteria initializer.
 *
 * @author Arjan Tijms
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SoteriaPreCDIInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SoteriaPreCDIInitializer.class.getName());

    /**
     * Initialize Soteria.
     *
     * @param classes the classes.
     * @param servletContext the Servlet context.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {

        WebXml webXml = (WebXml) servletContext.getAttribute("com.manorrock.piranha.webxml.WebXml");
        
        if (webXml != null && webXml.loginConfig.authMethod != null) {
            
            LOGGER.info("AuthMethod " + webXml.loginConfig.authMethod + " configured in web.xml and handled by Soteria.");
            
            WebXmlLoginConfig webXmlLoginConfig = SoteriaServiceProviders.getServiceProvider(WebXmlLoginConfig.class);
            
            webXmlLoginConfig.setAuthMethod(webXml.loginConfig.authMethod);
            webXmlLoginConfig.setRealmName(webXml.loginConfig.realmName);
            webXmlLoginConfig.setFormLoginPage(webXml.loginConfig.formLoginPage);
            webXmlLoginConfig.setFormErrorPage(webXml.loginConfig.formErrorPage);
        }
        
    }
}
