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
package cloud.piranha.dist.server;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.impl.DefaultModuleFinder;
import cloud.piranha.core.impl.DefaultModuleLayerProcessor;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.core.impl.DefaultWebApplicationExtensionContext;
import static cloud.piranha.core.impl.WarFileExtractor.extractWarFile;
import cloud.piranha.feature.http.HttpFeature;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.impl.DefaultHttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.List;
import java.util.ServiceLoader;

/**
 * The Piranha Server runtime.
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
     * Stores the HTTP feature.
     */
    private HttpFeature httpFeature;

    /**
     * Stores the HTTP port.
     */
    private int httpPort = 8080;

    /**
     * Stores the HTTP server.
     */
    private HttpServer httpServer;

    /**
     * Stores the HTTP server class.
     */
    private String httpServerClass;

    /**
     * Stores the HTTPS port.
     */
    private int httpsPort = 8043;

    /**
     * Stores the HTTP server.
     */
    private HttpServer httpsServer;

    /**
     * Stores the HTTPS server class.
     */
    private String httpsServerClass;

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

        startHttpsServer();

        webApplicationServer.start();

        File[] webapps = webAppsDir.listFiles();
        if (webapps != null) {
            for (File webapp : webapps) {
                if (webapp.getName().toLowerCase().endsWith(".war")) {
                    String contextPath = webapp.getName().substring(0, webapp.getName().length() - 4);
                    File webAppDirectory = new File(webAppsDir, contextPath);
                    extractWarFile(webapp, webAppDirectory);

                    DefaultWebApplication webApplication = new DefaultWebApplication();
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

                        LOGGER.log(INFO, "Deployed " + webapp.getName() + " at " + webApplication.getContextPath());

                    } catch (Error e) {
                        LOGGER.log(ERROR, () -> "Failed to initialize app " + webapp.getName(), e);
                    }

                    webApplicationServer.addWebApplication(webApplication);
                }
            }
        }
        
        /*
         * Construct, initialize and start HTTP endpoint (if applicable).
         */
        if (httpPort > 0) {
            httpFeature = new HttpFeature();
            httpFeature.setHttpServerClass(httpServerClass);
            httpFeature.setPort(httpPort);
            httpFeature.init();
            httpFeature.getHttpServer().setHttpServerProcessor(webApplicationServer);
            httpFeature.start();
            httpServer = httpFeature.getHttpServer();
        }

        long finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Started Piranha");
        LOGGER.log(INFO, "It took {0} milliseconds", finishTime - startTime);

        started = true;

        File startedFile = new File("tmp/piranha.started");
        File stoppedFile = new File("tmp/piranha.stopped");

        if (stoppedFile.exists()) {
            try {
                Files.delete(stoppedFile.toPath());
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Error while deleting existing piranha.stopped file", ioe);
            }
        }

        if (!startedFile.exists()) {
            try {
                startedFile.createNewFile();
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Unable to create piranha.started file", ioe);
            }
        }

        File pidFile = new File(PID_FILE);
        while (isRunning()) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            if (!pidFile.exists()) {
                webApplicationServer.stop();
                stopHttpServer();
                if (httpsServer != null) {
                    httpsServer.stop();
                }
            }
        }

        finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Stopped Piranha");
        LOGGER.log(INFO, "We ran for {0} milliseconds", finishTime - startTime);

        if (startedFile.exists()) {
            try {
                Files.delete(startedFile.toPath());
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Error while deleting existing piranha.started file", ioe);
            }
        }
        if (!stoppedFile.exists()) {
            try {
                stoppedFile.createNewFile();
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Unable to create piranha.stopped file", ioe);
            }
        }

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
     * Set the HTTP server class.
     *
     * @param httpServerClass the HTTP server class.
     */
    public void setHttpServerClass(String httpServerClass) {
        this.httpServerClass = httpServerClass;
    }
    
    /**
     * Set the HTTPS keystore file.
     *
     * <p>
     * Convenience wrapper around the <code>javax.net.ssl.keyStore</code> system
     * property. Note using this method sets the property for the entire JVM.
     * </p>
     *
     * @param httpsKeystoreFile the HTTPS keystore file.
     */
    public void setHttpsKeystoreFile(String httpsKeystoreFile) {
        if (httpsKeystoreFile != null) {
            System.setProperty("javax.net.ssl.keyStore", httpsKeystoreFile);
        }
    }

    /**
     * Set the HTTPS keystore password.
     *
     * <p>
     * Convenience wrapper around the
     * <code>javax.net.ssl.keyStorePassword</code> system property. Note using
     * this method sets the property for the entire JVM.
     * </p>
     *
     * @param httpsKeystorePassword the HTTP keystore password.
     */
    public void setHttpsKeystorePassword(String httpsKeystorePassword) {
        if (httpsKeystorePassword != null) {
            System.setProperty("javax.net.ssl.keyStorePassword", httpsKeystorePassword);
        }
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
     * Set the HTTPS truststore file.
     *
     * <p>
     * Convenience wrapper around the <code>javax.net.ssl.trustStore</code>
     * system property. Note using this method sets the property for the entire
     * JVM.
     * </p>
     *
     * @param httpsTruststoreFile the HTTPS truststore file.
     */
    public void setHttpsTruststoreFile(String httpsTruststoreFile) {
        if (httpsTruststoreFile != null) {
            System.setProperty("javax.net.ssl.trustStore", httpsTruststoreFile);
        }
    }

    /**
     * Set the SSL truststore password.
     *
     * <p>
     * Convenience wrapper around the
     * <code>javax.net.ssl.trustStorePassword</code> system property. Note using
     * this method sets the property for the entire JVM.
     * </p>
     *
     * @param httpsTruststorePassword the HTTPS truststore password.
     */
    void setHttpsTruststorePassword(String httpsTruststorePassword) {
        if (httpsTruststorePassword != null) {
            System.setProperty("javax.net.ssl.trustStorePassword", httpsTruststorePassword);
        }
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
     * Set the SSL truststore file.
     *
     * <p>
     * Convenience wrapper around the <code>javax.net.ssl.trustStore</code>
     * system property. Note using this method sets the property for the entire
     * JVM.
     * </p>
     *
     * @param sslTruststoreFile the SSL truststore file.
     * @deprecated 
     */
    @Deprecated(since = "23.7.0", forRemoval = true)
    public void setSslTruststoreFile(String sslTruststoreFile) {
        if (sslTruststoreFile != null) {
            System.setProperty("javax.net.ssl.trustStore", sslTruststoreFile);
        }
    }

    /**
     * Set the SSL truststore password.
     *
     * <p>
     * Convenience wrapper around the
     * <code>javax.net.ssl.trustStorePassword</code> system property. Note using
     * this method sets the property for the entire JVM.
     * </p>
     *
     * @param sslTruststorePassword the SSL truststore password.
     * @deprecated 
     */
    @Deprecated(since = "23.7.0", forRemoval = true)
    void setSslTruststorePassword(String sslTruststorePassword) {
        if (sslTruststorePassword != null) {
            System.setProperty("javax.net.ssl.trustStorePassword", sslTruststorePassword);
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
            LOGGER.log(WARNING, "PID file already exists");
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
     * Start the HTTPS server (if requested).
     */
    private void startHttpsServer() {
        if (httpsPort > 0) {
            if (httpsServerClass == null) {
                httpsServerClass = DefaultHttpServer.class.getName();
            }
            try {
                httpsServer = (HttpServer) Class.forName(httpsServerClass)
                        .getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | IllegalAccessException
                    | IllegalArgumentException | InstantiationException
                    | NoSuchMethodException | SecurityException
                    | InvocationTargetException t) {
                LOGGER.log(ERROR, "Unable to construct HTTPS server", t);
            }
            if (httpsServer != null) {
                httpsServer.setHttpServerProcessor(webApplicationServer);
                httpsServer.setServerPort(httpsPort);
                httpsServer.setSSL(true);
                httpsServer.start();
            }
        }
    }

    /**
     * Stop the server.
     */
    public void stop() {
        File pidFile = new File(PID_FILE);

        if (pidFile.exists()) {
            try {
                Files.delete(pidFile.toPath());
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Error occurred while deleting PID file", ioe);
            }
        }

        started = false;
        thread = null;
    }

    /**
     * Stores the HTTP server (if requested).
     */
    private void stopHttpServer() {
        if (httpServer != null) {
            httpServer.stop();
        }
    }
}
