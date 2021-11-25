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
package cloud.piranha.server.core;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.api.WebApplicationServerRequestMapper;
import cloud.piranha.core.impl.DefaultModuleFinder;
import cloud.piranha.core.impl.DefaultModuleLayerProcessor;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.core.impl.DefaultWebApplicationExtensionContext;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.ServletException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.util.List;
import java.util.ServiceLoader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Lite version of Piranha Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServerPiranha implements Piranha, Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ServerPiranha.class.getName());

    /**
     * Stores the 'tmp/piranha.pid' file constant.
     */
    private static final String PID_FILE = "tmp/piranha.pid";

    /**
     * Stores the default extension class.
     */
    private Class<? extends WebApplicationExtension> defaultExtensionClass;

    /**
     * Stores the exit on stop flag.
     */
    private boolean exitOnStop = true;

    /**
     * Stores the HTTP port.
     */
    private int httpPort = 8080;

    /**
     * Stores the HTTP server.
     */
    private HttpServer httpServer;

    /**
     * Stores the HTTPS port.
     */
    private int httpsPort = 8043;

    /**
     * Stores the HTTP server.
     */
    private HttpServer httpsServer;

    /**
     * Stores the JMPS enabled flag.
     */
    private boolean jpmsEnabled = false;

    /**
     * Stores the started flag.
     */
    private boolean started = false;

    /**
     * Stores the thread we use.
     */
    private Thread thread;

    /**
     * Stores the HTTP web application server.
     */
    private HttpWebApplicationServer webApplicationServer;

    /**
     * Stores the web applications directory.
     */
    private File webAppsDir = new File("webapps");

    /**
     * Extract the zip input stream.
     *
     * @param zipInput the zip input stream.
     * @param filePath the file path.
     * @throws IOException when an I/O error occurs.
     */
    private void extractZipInputStream(ZipInputStream zipInput, String filePath) throws IOException {
        try ( BufferedOutputStream bufferOutput = new BufferedOutputStream(new FileOutputStream(filePath))) {
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
        try ( ZipInputStream zipInput = new ZipInputStream(new FileInputStream(warFile))) {
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
     * Are we running?
     *
     * @return true if we are, false otherwise.
     */
    private boolean isRunning() {
        boolean result = false;
        if (httpServer != null) {
            result = httpServer.isRunning();
        } else if (httpsServer != null) {
            result = httpsServer.isRunning();
        }
        return result;
    }

    /**
     * Have we started?
     *
     * @return true if we have, false otherwise.
     */
    private boolean isStarted() {
        return started;
    }

    /**
     * Run method.
     */
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        LOGGER.log(INFO, () -> "Starting Piranha");

        webApplicationServer = new HttpWebApplicationServer();

        if (httpPort > 0) {
            httpServer = ServiceLoader.load(HttpServer.class).findFirst().orElseThrow();
            httpServer.setServerPort(httpPort);
            httpServer.setHttpServerProcessor(webApplicationServer);
            httpServer.start();
        }

        if (httpsPort > 0) {
            httpsServer = ServiceLoader.load(HttpServer.class).findFirst().orElseThrow();
            httpsServer.setHttpServerProcessor(webApplicationServer);
            httpsServer.setServerPort(httpsPort);
            httpsServer.setSSL(true);
            httpsServer.start();
        }
        webApplicationServer.start();

        WebApplicationServerRequestMapper requestMapper = webApplicationServer.getRequestMapper();

        File[] webapps = webAppsDir.listFiles();
        if (webapps != null) {
            for (File webapp : webapps) {
                if (webapp.getName().toLowerCase().endsWith(".war")) {
                    String contextPath = webapp.getName().substring(0, webapp.getName().length() - 4);
                    File webAppDirectory = new File(webAppsDir, contextPath);
                    extractWarFile(webapp, webAppDirectory);

                    DefaultWebApplication webApplication = new ServerWebApplication(requestMapper);

                    webApplication.addResource(new DirectoryResource(webAppDirectory));

                    DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(webAppDirectory);
                    webApplication.setClassLoader(classLoader);

                    if (Boolean.getBoolean("cloud.piranha.modular.enable") || jpmsEnabled) {
                        setupLayers(classLoader);
                    }

                    if (classLoader.getResource("/META-INF/services/" + WebApplicationExtension.class.getName()) == null) {
                        DefaultWebApplicationExtensionContext extensionContext = new DefaultWebApplicationExtensionContext();
                        extensionContext.add(defaultExtensionClass);
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

                    try {
                        webApplication.initialize();
                        webApplication.start();
                    } catch (Exception e) {
                        LOGGER.log(Level.ERROR, () -> "Failed to initialize app " + webapp.getName(), e);
                    }

                    webApplicationServer.addWebApplication(webApplication);
                }
            }
        }
        long finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Started Piranha");
        LOGGER.log(INFO, "It took {0} milliseconds", finishTime - startTime);

        started = true;

        File pidFile = new File(PID_FILE);
        while (isRunning()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            if (!pidFile.exists()) {
                webApplicationServer.stop();
                if (httpServer != null) {
                    httpServer.stop();
                }
                if (httpsServer != null) {
                    httpsServer.stop();
                }
            }
        }

        finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Stopped Piranha");
        LOGGER.log(INFO, "We ran for {0} milliseconds", finishTime - startTime);
        if (exitOnStop) {
            System.exit(0);
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
     * Set the default extension class.
     *
     * @param defaultExtensionClass the default extension class.
     */
    public void setDefaultExtensionClass(
            Class<? extends WebApplicationExtension> defaultExtensionClass) {
        this.defaultExtensionClass = defaultExtensionClass;
    }

    /**
     * Set the exit on stop flag.
     *
     * @param exitOnStop the exit on stop flag.
     */
    public void setExitOnStop(boolean exitOnStop) {
        this.exitOnStop = exitOnStop;
    }

    /**
     * Set the HTTP server port.
     *
     * @param httpPort the HTTP server port.
     */
    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    /**
     * Set the HTTPS server port.
     *
     * @param httpsPort the HTTPS server port.
     */
    public void setHttpsPort(int httpsPort) {
        this.httpsPort = httpsPort;
    }

    /**
     * Enable/disable JPMS.
     *
     * @param jpmsEnabled the JPMS enabled flag.
     */
    public void setJpmsEnabled(boolean jpmsEnabled) {
        this.jpmsEnabled = jpmsEnabled;
    }

    /**
     * Set the SSL keystore file.
     *
     * <p>
     * Convenience wrapper around the <code>javax.net.ssl.keyStore</code> system
     * property. Note using this method sets the property for the entire JVM.
     * </p>
     *
     * @param sslKeystoreFile the SSL keystore file.
     */
    public void setSslKeystoreFile(String sslKeystoreFile) {
        if (sslKeystoreFile != null) {
            System.setProperty("javax.net.ssl.keyStore", sslKeystoreFile);
        }
    }

    /**
     * Set the SSL keystore password.
     *
     * <p>
     * Convenience wrapper around the
     * <code>javax.net.ssl.keyStorePassword</code> system property. Note using
     * this method sets the property for the entire JVM.
     * </p>
     *
     * @param sslKeystorePassword
     */
    void setSslKeystorePassword(String sslKeystorePassword) {
        if (sslKeystorePassword != null) {
            System.setProperty("javax.net.ssl.keyStorePassword", sslKeystorePassword);
        }
    }

    /**
     * Set the web applications directory.
     *
     * @param webAppsDir the web applications directory.
     */
    public void setWebAppsDir(File webAppsDir) {
        this.webAppsDir = webAppsDir;
    }

    /**
     * Start the server.
     */
    public void start() {
        File pidFile = new File(PID_FILE);

        if (!pidFile.exists()) {
            try {
                if (!pidFile.getParentFile().exists()) {
                    pidFile.getParentFile().mkdirs();
                }
                pidFile.createNewFile();
            } catch (IOException ex) {
                LOGGER.log(WARNING, "Unable to create PID file");
            }
        } else {
            LOGGER.log(WARNING, "PID file already exsists");
        }

        thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();

        while (!isStarted()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stop the server.
     */
    public void stop() {
        File pidFile = new File(PID_FILE);

        if (pidFile.exists()) {
            pidFile.delete();
        }

        started = false;
        thread = null;
    }
}
