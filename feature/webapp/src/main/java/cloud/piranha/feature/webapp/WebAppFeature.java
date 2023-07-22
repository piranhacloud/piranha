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
package cloud.piranha.feature.webapp;

import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.impl.DefaultModuleFinder;
import cloud.piranha.core.impl.DefaultModuleLayerProcessor;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.core.impl.DefaultWebApplicationExtensionContext;
import static cloud.piranha.core.impl.WarFileExtractor.extractWarFile;
import cloud.piranha.feature.impl.DefaultFeature;
import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.resource.impl.DirectoryResource;
import java.io.File;
import static java.lang.System.Logger.Level.ERROR;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.List;
import java.util.ServiceLoader;

/**
 * The Web Application feature.
 *
 * <p>
 * The Web Application feature delivers the capability to host a single web
 * application.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebAppFeature extends DefaultFeature {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(WebAppFeature.class.getName());

    /**
     * Stores the context path.
     */
    private String contextPath;

    /**
     * Stores the extension class.
     */
    private Class<? extends WebApplicationExtension> extensionClass;

    /**
     * Stores the JPMS enabled flag.
     */
    private boolean jpmsEnabled;

    /**
     * Stores the war file.
     */
    private File warFile;

    /**
     * Stores the web application directory.
     */
    private File webAppDir;

    /**
     * Stores the HTTP web application server.
     */
    private HttpWebApplicationServer httpWebApplicationServer;

    /**
     * Get the context path.
     *
     * @return the context path.
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Get the HttpServerProcessor.
     *
     * @return the HttpServerProcessor.
     */
    public HttpServerProcessor getHttpServerProcessor() {
        return httpWebApplicationServer;
    }

    /**
     * Get the WAR file.
     *
     * @return the WAR file.
     */
    public File getWarFile() {
        return warFile;
    }

    /**
     * Get the web application directory.
     *
     * @return the web application directory.
     */
    public File getWebAppDir() {
        return webAppDir;
    }
    
    /**
     * Get the HttpWebApplicationServer.
     * 
     * @return the HttpWebApplicationServer.
     */
    public HttpWebApplicationServer getHttpWebApplicationServer() {
        return httpWebApplicationServer;
    }

    @Override
    public void init() {
        if (httpWebApplicationServer == null) {
            httpWebApplicationServer = new HttpWebApplicationServer();
        }

        if (warFile != null && warFile.getName().toLowerCase().endsWith(".war")) {
            if (contextPath == null) {
                contextPath = warFile.getName().substring(0, warFile.getName().length() - 4);
            }
            if (webAppDir == null) {
                webAppDir = new File(contextPath);
            }
            extractWarFile(warFile, webAppDir);
        }

        if (webAppDir != null && webAppDir.exists()) {
            if (contextPath == null) {
                contextPath = webAppDir.getName();
            }

            DefaultWebApplication webApplication = new DefaultWebApplication();
            webApplication.addResource(new DirectoryResource(webAppDir));

            DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(webAppDir);
            webApplication.setClassLoader(classLoader);

            if (jpmsEnabled) {
                setupLayers(classLoader);
            }

            if (classLoader.getResource("/META-INF/services/" + WebApplicationExtension.class.getName()) == null) {
                DefaultWebApplicationExtensionContext extensionContext = new DefaultWebApplicationExtensionContext();
                extensionContext.add((extensionClass));
                extensionContext.configure(webApplication);
            } else {
                DefaultWebApplicationExtensionContext extensionContext = new DefaultWebApplicationExtensionContext();
                ServiceLoader<WebApplicationExtension> serviceLoader = ServiceLoader.load(WebApplicationExtension.class, classLoader);
                extensionContext.add(serviceLoader.iterator().next());
                extensionContext.configure(webApplication);
            }

            if (contextPath.equalsIgnoreCase("ROOT")) {
                contextPath = "";
            } else if (!contextPath.startsWith("/")) {
                contextPath = "/" + contextPath;
            }
            webApplication.setContextPath(contextPath);
            httpWebApplicationServer.addWebApplication(webApplication);

            try {
                webApplication.initialize();
            } catch (Exception e) {
                LOGGER.log(ERROR, "Failed to initialize web application");
            }
        }

        if (webAppDir == null && warFile == null) {
            LOGGER.log(ERROR, "No web application deployed");
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
     * Set the context path.
     *
     * @param contextPath the context path.
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
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
     * Set the HttpWebApplicationServer.
     * 
     * @param httpWebApplicationServer the HttpWebApplicationServer.
     */
    public void setHttpWebApplicationServer(HttpWebApplicationServer httpWebApplicationServer) {
        this.httpWebApplicationServer = httpWebApplicationServer;
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
     * Set the WAR file.
     *
     * @param warFile the WAR file.
     */
    public void setWarFile(File warFile) {
        if (warFile != null) {
            this.warFile = warFile;
        }
    }

    /**
     * Set the web application directory.
     *
     * @param webAppDir the web application directory.
     */
    public void setWebAppDir(File webAppDir) {
        this.webAppDir = webAppDir;
    }

    /**
     * Setup the layers.
     *
     * @param classLoader the web application class loader.
     */
    private void setupLayers(DefaultWebApplicationClassLoader classLoader) {
        ModuleFinder defaultModuleFinder = new DefaultModuleFinder(classLoader.getResourceManager().getResourceList());

        List<String> roots = defaultModuleFinder.findAll()
                .stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .toList();

        if (!roots.isEmpty()) {
            Configuration configuration = ModuleLayer.boot().configuration().resolveAndBind(defaultModuleFinder, ModuleFinder.of(), roots);
            ModuleLayer.Controller controller = ModuleLayer.defineModules(configuration, List.of(ModuleLayer.boot()), x -> classLoader);
            DefaultModuleLayerProcessor.INSTANCE.processModuleLayerOptions(controller);
        }
    }

    @Override
    public void start() {
        httpWebApplicationServer.start();
    }

    @Override
    public void stop() {
        httpWebApplicationServer.stop();
    }
}
