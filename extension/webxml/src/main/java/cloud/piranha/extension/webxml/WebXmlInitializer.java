/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebXml;
import cloud.piranha.core.api.WebXmlServletMapping;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The web.xml initializer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(WebXmlInitializer.class.getName());

    /**
     * On startup.
     *
     * @param classes the classes.
     * @param servletContext the servlet context.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        LOGGER.log(DEBUG, () -> "Entering WebXmlInitializer.onStartup");

        try {
            WebApplication webApplication = (WebApplication) servletContext;
            DefaultWebXmlManager manager = new DefaultWebXmlManager();
            webApplication.getManager().setWebXmlManager(manager);

            WebXmlParser parser = new WebXmlParser();
            InputStream inputStream = servletContext.getResourceAsStream("WEB-INF/web.xml");
            if (inputStream != null) {
                WebXml webXml = parser.parse(servletContext.getResourceAsStream("WEB-INF/web.xml"));
                manager.setWebXml(webXml);
                manager.setInitialWebXml(webXml);
            }

            ArrayList<WebXml> webFragments = new ArrayList<>();
            List<URL> webFragmentUrls = Collections.list(servletContext.getClassLoader().getResources("META-INF/web-fragment.xml"));
            for (URL url : webFragmentUrls) {
                try (InputStream stream = url.openStream()) {
                    WebXml webFragment = parser.parse(stream);
                    webFragment.setFragment(true);
                    webFragments.add(webFragment);
                }
            }
            if (!webFragments.isEmpty()) {
                manager.setWebFragments(webFragments);
            }

            if (manager.getWebXml() == null) {
                manager.setWebXml(new WebXml());
            }

            if (manager.getWebXml() != null) {
                WebXml webXml = manager.getWebXml();
                WebXmlProcessor processor = new WebXmlProcessor();

                processor.process(webXml, webApplication);

                if (webXml.getMetadataComplete()) {
                    return;
                }
                
                removeExistingServletMappings(webApplication, manager);

                manager.getOrderedFragments().forEach(fragment -> processor.process(fragment, webApplication));
            } else {
                LOGGER.log(DEBUG, "No web.xml found!");
            }
        } catch (IOException e) {
            LOGGER.log(WARNING, "Unable to parse web.xml", e);
        }

        LOGGER.log(DEBUG, () -> "Exiting WebXmlInitializer.onStartup");
    }

    /**
     * Remove any servlet mappings in the web fragments that are already mapped
     * by the regular web.xml.
     * 
     * @param webApp the web application.
     * @param manager the web.xml manager.
     */
    private void removeExistingServletMappings(WebApplication webApp, DefaultWebXmlManager manager) {
        for(WebXmlServletMapping mapping : manager.getWebXml().getServletMappings()) {
            for(WebXml fragment : manager.getOrderedFragments()) {
                ArrayList<WebXmlServletMapping> candidateList = new ArrayList<>(fragment.getServletMappings());
                for(WebXmlServletMapping candidateMapping : candidateList) {
                    if (candidateMapping.servletName().equals(mapping.servletName())) {
                        fragment.getServletMappings().remove(candidateMapping);
                    }
                }
            }
        }
    }
}
