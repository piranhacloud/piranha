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
package cloud.piranha.webapp.webxml;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.impl.WebXml;
import cloud.piranha.webapp.impl.WebXmlManager;

/**
 * The web.xml initializer.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(WebXmlInitializer.class.getName());

    /**
     * On startup.
     *
     * @param classes the classes.
     * @param servletContext the servlet context.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        LOGGER.log(FINE, () -> "Entering WebXmlInitializer.onStartup");

        try {
            WebXmlParser parser = new WebXmlParser();
            WebXmlManager manager = new WebXmlManager();
            servletContext.setAttribute(WebXmlManager.KEY, manager);

            WebApplication webApp = (WebApplication) servletContext;
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

                processor.process(webXml, webApp);

                if (webXml.getMetadataComplete()) {
                    return;
                }

                manager.getOrderedFragments().forEach(fragment -> processor.process(fragment, webApp));
            } else {
                LOGGER.fine("No web.xml found!");
            }
        } catch (IOException e) {
            LOGGER.log(WARNING, "Unable to parse web.xml", e);
        }

        LOGGER.log(FINE, () -> "Exiting WebXmlInitializer.onStartup");
    }

}
