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
package cloud.piranha.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.List;
import java.util.ServiceLoader;
import java.lang.System.Logger.Level;
import java.lang.System.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.api.WebApplicationServerRequestMapper;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.core.impl.DefaultWebApplicationExtensionContext;
import cloud.piranha.core.impl.DefaultModuleLayerProcessor;
import cloud.piranha.core.impl.DefaultModuleFinder;
import cloud.piranha.extension.server.ServerExtension;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.resource.DirectoryResource;

import static java.lang.System.Logger.Level.INFO;

/**
 * The Servlet container version of Piranha.
 *
 * <p>
 * This version of Piranha makes it possible for you to run multiple web
 * applications at the same time.
 * </p>
 *
 * <p>
 * It has a shutdown mechanism that allows you to shutdown the server by
 * removing the piranha.pid file that should be created by the startup script.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServerPiranha implements Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ServerPiranha.class.getPackageName());

    /**
     * Stores the one and only instance of the server.
     */
    private static ServerPiranha INSTANCE;

    /**
     * Stores the SSL enabled flag.
     */
    private boolean sslEnabled = false;

    /**
     * {@return the instance}
     */
    public static ServerPiranha get() {
        return INSTANCE;
    }

    /**
     * Main method.
     *
     * @param arguments the arguments.
     */
    public static void main(String[] arguments) {
        INSTANCE = new ServerPiranha();
        INSTANCE.processArguments(arguments);
        INSTANCE.run();
    }

    /**
     * Extract the zip input stream.
     *
     * @param zipInput the zip input stream.
     * @param filePath the file path.
     * @throws IOException when an I/O error occurs.
     */
    private void extractZipInputStream(ZipInputStream zipInput, String filePath) throws IOException {
        try (BufferedOutputStream bufferOutput = new BufferedOutputStream(new FileOutputStream(filePath))) {
            byte[] bytesIn = new byte[8192];
            int read;
            while ((read = zipInput.read(bytesIn)) != -1) {
                bufferOutput.write(bytesIn, 0, read);
            }
        }
    }

    /**
     * Extract the WAR file.
     */
    private void extractWarFile(File warFile, File webApplicationDirectory) {
        if (!webApplicationDirectory.exists()) {
            webApplicationDirectory.mkdirs();
        }
        try (ZipInputStream zipInput = new ZipInputStream(new FileInputStream(warFile))) {
            ZipEntry entry = zipInput.getNextEntry();
            while (entry != null) {
                String filePath = webApplicationDirectory + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    File file = new File(filePath);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    extractZipInputStream(zipInput, filePath);
                }
                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }
        } catch (IOException ioe) {
        }
    }

    /**
     * Process the arguments.
     *
     * @param arguments the arguments.
     */
    private void processArguments(String[] arguments) {
        if (arguments != null) {
            for (String argument : arguments) {
                if (argument.equals("--ssl")) {
                    sslEnabled = true;
                }
            }
        }
    }

    /**
     * Start method.
     */
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        LOGGER.log(INFO, () -> "Starting Piranha");

        HttpWebApplicationServer webApplicationServer = new HttpWebApplicationServer();
        HttpServer httpServer = ServiceLoader.load(HttpServer.class).findFirst().orElseThrow();
        httpServer.setServerPort(8080);
        httpServer.setHttpServerProcessor(webApplicationServer);
        httpServer.start();
        HttpServer httpsServer = null;
        if (sslEnabled) {
            httpsServer = ServiceLoader.load(HttpServer.class).findFirst().orElseThrow();
            httpsServer.setHttpServerProcessor(webApplicationServer);
            httpsServer.setServerPort(8443);
            httpsServer.setSSL(true);
            httpsServer.start();
        }
        webApplicationServer.start();

        WebApplicationServerRequestMapper requestMapper = webApplicationServer.getRequestMapper();

        File webappsDirectory = new File("webapps");
        File[] webapps = webappsDirectory.listFiles();
        if (webapps != null) {
            for (File webapp : webapps) {
                if (webapp.getName().toLowerCase().endsWith(".war")) {
                    String contextPath = webapp.getName().substring(0, webapp.getName().length() - 4);
                    File webAppDirectory = new File(webappsDirectory, contextPath);
                    extractWarFile(webapp, webAppDirectory);

                    DefaultWebApplication webApplication = new ServerWebApplication(requestMapper);

                    webApplication.addResource(new DirectoryResource(webAppDirectory));

                    DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(webAppDirectory);
                    webApplication.setClassLoader(classLoader);

                    if (Boolean.getBoolean("cloud.piranha.modular.enable")) {
                        setupLayers(classLoader);
                    }

                    if (classLoader.getResource("/META-INF/services/" + WebApplicationExtension.class.getName()) == null) {
                        DefaultWebApplicationExtensionContext extensionContext = new DefaultWebApplicationExtensionContext();
                        extensionContext.add(ServerExtension.class);
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
                        webApplication.start();
                    } catch (Exception e) {
                        LOGGER.log(Level.ERROR, () -> "Failed to initialize app " + webapp.getName(), e);
                    }
                }
            }
        }
        long finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Started Piranha");
        LOGGER.log(INFO, "It took {0} milliseconds", finishTime - startTime);

        File pidFile = new File("tmp/piranha.pid");
        while (httpServer.isRunning()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            if (!pidFile.exists()) {
                webApplicationServer.stop();
                httpServer.stop();
                if (sslEnabled) {
                    httpsServer.stop();
                }
                System.exit(0);
            }
        }

        finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Stopped Piranha");
        LOGGER.log(INFO, "We ran for {0} milliseconds", finishTime - startTime);
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

    /**
     * Enable/disable SSL.
     *
     * @param sslEnabled the SSL enabled flag.
     */
    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }
}
