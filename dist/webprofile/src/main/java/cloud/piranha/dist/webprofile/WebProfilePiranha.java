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
package cloud.piranha.dist.webprofile;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.impl.DefaultModuleFinder;
import cloud.piranha.core.impl.DefaultModuleLayerProcessor;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.core.impl.DefaultWebApplicationExtensionContext;
import cloud.piranha.extension.webprofile.WebProfileExtension;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.ServletException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ServiceLoader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The Piranha Micro runtime.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebProfilePiranha implements Piranha, Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(WebProfilePiranha.class.getName());

    /**
     * Stores the extension class.
     */
    private Class<? extends WebApplicationExtension> extensionClass = WebProfileExtension.class;

    /**
     * Stores the context path.
     */
    private String contextPath = null;

    /**
     * Stores the exit on stop flag.
     */
    private boolean exitOnStop = true;

    /**
     * Stores the HTTP port.
     */
    private int httpPort = 8080;

    /**
     * Stores the HTTP server class.
     */
    private String httpServerClass;

    /**
     * Stores the HTTPS port.
     */
    private int httpsPort = 8043;

    /**
     * Stores the HTTPS server class.
     */
    private String httpsServerClass;

    /**
     * Stores the JMPS enabled flag.
     */
    private boolean jpmsEnabled = false;

    /**
     * Stores the stop flag.
     */
    private boolean stop = false;

    /**
     * Stores the thread we use.
     */
    private Thread thread;

    /**
     * Stores the WAR file.
     */
    private File warFile;

    /**
     * Stores the HTTP web application server.
     */
    private HttpWebApplicationServer webApplicationServer;

    /**
     * Stores the web application directory;
     */
    private File webAppDir;

    /**
     * Stores the PID.
     */
    private Long pid;

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
     *
     * @param warFile the WAR file.
     * @param webApplicationDirectory the web application directory.
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
            LOGGER.log(WARNING, "I/O error occurred while extracting WAR file", ioe);
        }
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
            HttpServer httpServer = null;
            if (httpServerClass != null) {
                try {
                    httpServer = (HttpServer) Class.forName(httpServerClass)
                            .getDeclaredConstructor().newInstance();
                } catch (ClassNotFoundException | IllegalAccessException
                        | IllegalArgumentException | InstantiationException
                        | NoSuchMethodException | SecurityException
                        | InvocationTargetException t) {
                    LOGGER.log(ERROR, "Unable to construct HTTP server", t);
                }
            } else {
                //
                // this mechanism is deprecated and will be removed in the next release.
                //
                httpServer = ServiceLoader.load(HttpServer.class).findFirst().orElseThrow();
                LOGGER.log(WARNING, "HttpServer service loading is deprecated, use --http-server-class instead");
            }
            if (httpServer != null) {
            httpServer.setServerPort(httpPort);
            httpServer.setHttpServerProcessor(webApplicationServer);
            httpServer.start();
        }
        }

        if (httpsPort > 0) {
            HttpServer httpsServer = null;
            if (httpServerClass != null) {
                try {
                    httpsServer = (HttpServer) Class.forName(httpServerClass)
                            .getDeclaredConstructor().newInstance();
                } catch (ClassNotFoundException | IllegalAccessException
                        | IllegalArgumentException | InstantiationException
                        | NoSuchMethodException | SecurityException
                        | InvocationTargetException t) {
                    LOGGER.log(ERROR, "Unable to construct HTTPS server", t);
                }
            } else {
                //
                // this mechanism is deprecated and will be removed in the next release.
                //
                httpsServer = ServiceLoader.load(HttpServer.class).findFirst().orElseThrow();
                LOGGER.log(WARNING, "HttpServer service loading is deprecated, use --https-server-class instead");
            }
            httpsServer.setHttpServerProcessor(webApplicationServer);
            httpsServer.setServerPort(httpsPort);
            httpsServer.setSSL(true);
            httpsServer.start();
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

            if (Boolean.getBoolean("cloud.piranha.modular.enable") || jpmsEnabled) {
                setupLayers(classLoader);
            }

            if (classLoader.getResource("/META-INF/services/" + WebApplicationExtension.class.getName()) == null) {
                DefaultWebApplicationExtensionContext extensionContext = new DefaultWebApplicationExtensionContext();
                extensionContext.add(extensionClass);
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

        long finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Started Piranha");
        LOGGER.log(INFO, "It took {0} milliseconds", finishTime - startTime);

        if (pid != null) {
            File pidFile = new File("tmp", "piranha.pid");
            if (!pidFile.getParentFile().exists()) {
                if (!pidFile.getParentFile().mkdirs()) {
                    LOGGER.log(WARNING, "Unable to create tmp directory for PID file");
                }
            }
            try ( PrintWriter writer = new PrintWriter(new FileWriter(pidFile))) {
                writer.println(pid);
                writer.flush();
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Unable to write PID file", ioe);
            }
        }

        while (!stop) {
            if (pid != null) {
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
     * Set the context path.
     *
     * @param contextPath the context path.
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * Set the default extension class.
     *
     * @param extensionClass the default extension class.
     */
    public void setExtensionClass(
            Class<? extends WebApplicationExtension> extensionClass) {
        this.extensionClass = extensionClass;
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
     * Set the HTTP server class.
     *
     * @param httpServerClass the HTTP server class.
     */
    public void setHttpServerClass(String httpServerClass) {
        this.httpServerClass = httpServerClass;
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
     * Set the HTTPS server class.
     *
     * @param httpsServerClass the HTTPS server class.
     */
    public void setHttpsServerClass(String httpsServerClass) {
        this.httpsServerClass = httpsServerClass;
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
     * Set the PID.
     *
     * @param pid the PID.
     */
    public void setPid(Long pid) {
        this.pid = pid;
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
     * Set the WAR file.
     *
     * @param warFile the WAR file.
     */
    public void setWarFile(String warFile) {
        this.warFile = new File(warFile);
    }

    /**
     * Set the web application directory.
     *
     * @param webAppDir the web application directory.
     */
    public void setWebAppDir(String webAppDir) {
        this.webAppDir = new File(webAppDir);
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
        if (exitOnStop) {
            System.exit(0);
        }
    }
}
