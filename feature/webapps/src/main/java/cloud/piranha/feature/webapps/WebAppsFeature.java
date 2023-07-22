/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.feature.webapps;

import cloud.piranha.core.api.WebApplicationExtension;
import static cloud.piranha.core.impl.WarFileExtractor.extractWarFile;
import cloud.piranha.feature.impl.DefaultFeature;
import cloud.piranha.feature.webapp.WebAppFeature;
import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import java.io.File;
import java.util.ArrayList;

/**
 * The WebApps feature.
 *
 * <p>
 * The WebApps feature delivers the capability to host multiple web
 * applications.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebAppsFeature extends DefaultFeature {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(WebAppsFeature.class.getName());

    /**
     * Stores the extension class.
     */
    private Class<? extends WebApplicationExtension> extensionClass;

    /**
     * Stores the JPMS enabled flag.
     */
    private boolean jpmsEnabled;

    /**
     * Stores the list of WebAppFeatures.
     */
    private final ArrayList<WebAppFeature> webAppFeatures = new ArrayList<>();

    /**
     * Stores the web applications directory.
     */
    private File webAppsDir;

    /**
     * Stores the HTTP web application server.
     */
    private HttpWebApplicationServer webApplicationServer;

    /**
     * Get the HttpServerProcessor.
     *
     * @return the HttpServerProcessor.
     */
    public HttpServerProcessor getHttpServerProcessor() {
        return webApplicationServer;
    }

    /**
     * Get the web applications directory.
     *
     * @return the web application directory.
     */
    public File getWebAppsDir() {
        return webAppsDir;
    }

    /**
     * Get the HttpWebApplicationServer.
     *
     * @return the HttpWebApplicationServer.
     */
    public HttpWebApplicationServer getHttpWebApplicationServer() {
        return webApplicationServer;
    }

    @Override
    public void init() {
        if (webApplicationServer == null) {
            webApplicationServer = new HttpWebApplicationServer();
        }

        File[] webapps = webAppsDir.listFiles();
        if (webapps != null) {
            for (File warFile : webapps) {
                String contextPath = null;
                File webAppDir = null;

                /*
                 * Extract into webAppDir.
                 */
                if (warFile.getName().toLowerCase().endsWith(".war")) {
                    contextPath = warFile.getName().substring(0, warFile.getName().length() - 4);
                    webAppDir = new File(webAppsDir, contextPath);
                    extractWarFile(warFile, webAppDir);
                }

                /*
                 * Construct, init and start the WebAppFeature for the web application.
                 */
                WebAppFeature webAppFeature = new WebAppFeature();
                webAppFeatures.add(webAppFeature);
                webAppFeature.setContextPath(contextPath);
                webAppFeature.setExtensionClass(extensionClass);
                webAppFeature.setHttpWebApplicationServer(webApplicationServer);
                webAppFeature.setJpmsEnabled(jpmsEnabled);
                webAppFeature.setWarFile(warFile);
                webAppFeature.setWebAppDir(webAppDir);
                webAppFeature.init();
            }
        }
    }

    /**
     * Get the JPMS enabled flag.
     *
     * @return true if JPMS should be enabled, false otherwise.
     */
    public boolean isJpmsEnabled() {
        return jpmsEnabled;
    }

    /**
     * Set the extension class.
     *
     * @param extensionClass the extension class.
     */
    public void setExtensionClass(Class<? extends WebApplicationExtension> extensionClass) {
        this.extensionClass = extensionClass;
    }

    /**
     * Set the JPMS enabled flag.
     *
     * @param jpmsEnabled the JPMS enabled flag.
     */
    public void setJpmsEnabled(boolean jpmsEnabled) {
        this.jpmsEnabled = jpmsEnabled;
    }

    /**
     * Set the web applications directory.
     * 
     * @param webAppsDir the web applications directory.
     */
    public void setWebAppsDir(File webAppsDir) {
        this.webAppsDir = webAppsDir;
    }

    @Override
    public void start() {
        webAppFeatures.forEach(f -> f.start());
        webApplicationServer.start();
    }

    @Override
    public void stop() {
        webApplicationServer.stop();
        webAppFeatures.forEach(f -> f.stop());
    }
}
