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
package cloud.piranha.dist.coreprofile;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.core.api.PiranhaConfiguration;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.impl.DefaultModuleFinder;
import cloud.piranha.core.impl.DefaultModuleLayerProcessor;
import cloud.piranha.core.impl.DefaultPiranhaConfiguration;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.core.impl.DefaultWebApplicationExtensionContext;
import static cloud.piranha.core.impl.WarFileExtractor.extractWarFile;
import cloud.piranha.extension.coreprofile.CoreProfileExtension;
import cloud.piranha.feature.http.HttpFeature;
import cloud.piranha.feature.https.HttpsFeature;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.ServletException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.List;
import java.util.ServiceLoader;

/**
 * The Piranha Core Profile runtime.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class CoreProfilePiranha implements Piranha, Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(CoreProfilePiranha.class.getName());

    /**
     * Stores the configuration.
     */
    private final PiranhaConfiguration configuration;

    /**
     * Stores the HTTP feature.
     */
    private HttpFeature httpFeature;

    /**
     * Stores the HTTPS feature.
     */
    private HttpsFeature httpsFeature;

    /**
     * Stores the stop flag.
     */
    private boolean stop = false;

    /**
     * Stores the thread we use.
     */
    private Thread thread;

    /**
     * Stores the HTTP web application server.
     */
    private HttpWebApplicationServer webApplicationServer;

    /**
     * Constructor.
     */
    public CoreProfilePiranha() {
        configuration = new DefaultPiranhaConfiguration();
        configuration.setClass("extensionClass", CoreProfileExtension.class);
        configuration.setBoolean("exitOnStop", false);
        configuration.setInteger("httpPort", 8080);
        configuration.setInteger("httpsPort", -1);
    }

    @Override
    public PiranhaConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Run method.
     */
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        LOGGER.log(INFO, () -> "Starting Piranha");

        webApplicationServer = new HttpWebApplicationServer();

        File webAppDir = null;
        File warFile = configuration.getFile("warFile");
        
        if (warFile != null && warFile.getName().toLowerCase().endsWith(".war")) {
            String contextPath = configuration.getString("contextPath");
            if (contextPath == null) {
                contextPath = warFile.getName().substring(0, warFile.getName().length() - 4);
                configuration.setString("contextPath", contextPath);
            }
            if (webAppDir == null) {
                webAppDir = new File(contextPath);
                configuration.setFile("webAppDir", webAppDir);
            }
            extractWarFile(warFile, webAppDir);
        }

        if (webAppDir != null && webAppDir.exists()) {
            String contextPath = configuration.getString("contextPath");
            if (contextPath == null) {
                contextPath = webAppDir.getName();
            }

            DefaultWebApplication webApplication = new DefaultWebApplication();
            webApplication.addResource(new DirectoryResource(webAppDir));

            DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(webAppDir);
            webApplication.setClassLoader(classLoader);

            if (Boolean.getBoolean("cloud.piranha.modular.enable") || configuration.getBoolean("jpmsEnabled", false)) {
                setupLayers(classLoader);
            }

            if (classLoader.getResource("/META-INF/services/" + WebApplicationExtension.class.getName()) == null) {
                DefaultWebApplicationExtensionContext extensionContext = new DefaultWebApplicationExtensionContext();
                extensionContext.add((Class<WebApplicationExtension>) configuration.getClass("extensionClass"));
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
            webApplicationServer.addWebApplication(webApplication);

            try {
                webApplication.initialize();
            } catch (Exception e) {
                LOGGER.log(ERROR, "Failed to initialize web application");
                System.exit(0);
            }
        }

        if (webAppDir == null && warFile == null) {
            LOGGER.log(WARNING, "No web application deployed");
        }

        webApplicationServer.start();

        /*
         * Construct, initialize and start HTTP endpoint (if applicable).
         */
        if (configuration.getInteger("httpPort") > 0) {
            httpFeature = new HttpFeature();
            httpFeature.setHttpServerClass(configuration.getString("httpServerClass"));
            httpFeature.setPort(configuration.getInteger("httpPort"));
            httpFeature.init();
            httpFeature.getHttpServer().setHttpServerProcessor(webApplicationServer);
            httpFeature.start();
        }

        /**
         * Construct, initialize and start the HTTPS endpoint (if applicable).
         */
        if (configuration.getInteger("httpsPort") > 0) {
            httpsFeature = new HttpsFeature();
            httpsFeature.setHttpsKeystoreFile(configuration.getString("httpsKeystoreFile"));
            httpsFeature.setHttpsKeystorePassword(configuration.getString("httpsKeystorePassword"));
            httpsFeature.setHttpsServerClass(configuration.getString("httpsServerClass"));
            httpsFeature.setPort(configuration.getInteger("httpsPort"));
            httpsFeature.init();
            httpsFeature.getHttpsServer().setHttpServerProcessor(webApplicationServer);
            httpsFeature.start();
        }

        long finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Started Piranha");
        LOGGER.log(INFO, "It took {0} milliseconds", finishTime - startTime);

        if (getConfiguration().getLong("pid") != null) {
            File pidFile = new File("tmp", "piranha.pid");
            if (!pidFile.getParentFile().exists() && !pidFile.getParentFile().mkdirs()) {
                LOGGER.log(WARNING, "Unable to create tmp directory for PID file");
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(pidFile))) {
                writer.println(getConfiguration().getLong("pid"));
                writer.flush();
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Unable to write PID file", ioe);
            }
        }

        while (!stop) {
            if (getConfiguration().getLong("pid") != null) {
                File pidFile = new File("tmp", "piranha.pid");
                if (!pidFile.exists()) {
                    stop();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

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
    public void service(WebApplicationRequest request, WebApplicationResponse response)
            throws IOException, ServletException {
        webApplicationServer.service(request, response);
    }

    /**
     * Start the server.
     */
    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stop the server.
     */
    public void stop() {
        stop = true;
        thread = null;
        if (configuration.getBoolean("exitOnStop", false)) {
            System.exit(0);
        }
    }
}
