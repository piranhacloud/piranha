/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.feature.isolatedwebapp;

import cloud.piranha.feature.impl.DefaultFeature;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.micro.builder.MicroWebApplication;
import cloud.piranha.micro.loader.MicroConfiguration;
import cloud.piranha.micro.loader.MicroOuterDeployer;
import java.io.File;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
/**
 * The Isolated WebApp feature.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class IsolatedWebAppFeature extends DefaultFeature {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(IsolatedWebAppFeature.class.getName());

    /**
     * Stores the HttpWebApplicationServer.
     */
    private HttpWebApplicationServer httpWebApplicationServer;

    /**
     * Stores the WAR file.
     */
    private File warFile;

    /**
     * Get the HttpWebApplicationServer.
     *
     * @return the HttpWebApplicationServer.
     */
    public HttpWebApplicationServer getHttpWebApplicationServer() {
        return httpWebApplicationServer;
    }

    /**
     * Get the context path.
     *
     * @param warFile the WAR file.
     * @return the context path.
     */
    private String getContextPath(File warFile) {
        String contextPath = warFile.getName().substring(0, warFile.getName().length() - 4);

        if (contextPath.equalsIgnoreCase("ROOT")) {
            contextPath = "";
        } else if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }

        return contextPath;
    }

    /**
     * Get the WAR file.
     *
     * @return the WAR file.
     */
    public File getWarFile() {
        return warFile;
    }

    @Override
    public void init() {
        deploy(warFile, httpWebApplicationServer);
    }

    private void deploy(File warFile, HttpWebApplicationServer webApplicationServer) {
        String contextPath = getContextPath(warFile);

        MicroConfiguration configuration = new MicroConfiguration();
        configuration.setContextPath(contextPath);
        configuration.setHttpStart(false);

        try {
            MicroWebApplication microWebApplication = new MicroWebApplication();
            microWebApplication.setContextPath(contextPath);
            microWebApplication.setDeployedApplication(
                    new MicroOuterDeployer(configuration.postConstruct())
                            .deploy(ShrinkWrap.create(ZipImporter.class, warFile.getName()).importFrom(warFile).as(WebArchive.class))
                            .getDeployedApplication());

            webApplicationServer.addWebApplication(microWebApplication);
        } catch (Exception e) {
            LOGGER.log(ERROR, () -> "Failed to initialize web application at " + contextPath, e);
        }
    }

    /**
     * Set the HttpWebApplicationServer.
     *
     * @param httpWebApplicationServer the HttpWebApplicationServer.
     */
    public void setHttpWebApplicationServer(HttpWebApplicationServer httpWebApplicationServer) {
        this.httpWebApplicationServer = httpWebApplicationServer;
    }

    /**
     * Set the WAR file.
     *
     * @param warFile the WAR file.
     */
    public void setWarFile(File warFile) {
        this.warFile = warFile;
    }
}
