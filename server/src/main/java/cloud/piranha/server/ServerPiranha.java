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
package cloud.piranha.server;

import cloud.piranha.api.Piranha;
import cloud.piranha.appserver.impl.DefaultWebApplicationServer;
import cloud.piranha.extension.servlet.ServletExtension;
import cloud.piranha.http.impl.DefaultHttpServer;
import cloud.piranha.jndi.memory.DefaultInitialContextFactory;
import cloud.piranha.resource.DirectoryResource;
import cloud.piranha.webapp.api.WebApplicationExtension;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.webapp.impl.DefaultWebApplicationExtensionContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
public class ServerPiranha implements Piranha, Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ServerPiranha.class.getPackageName());

    /**
     * Stores the one and only instance of the server.
     */
    private static ServerPiranha INSTANCE;

    /**
     * Defines the attribute name for the ServerPiranha reference.
     */
    private static final String SERVER_PIRANHA = "cloud.piranha.server.ServerPiranha";

    /**
     * Stores the SSL flag.
     */
    private boolean ssl = false;

    /**
     * Get the instance.
     *
     * @return the instance.
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
        if (System.getProperty("java.naming.factory.initial") == null) {
            System.setProperty("java.naming.factory.initial", DefaultInitialContextFactory.class.getName());
        }
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
     * Get the version.
     *
     * @return the version.
     */
    @Override
    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
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
                    ssl = true;
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
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Starting Piranha");
        }
        DefaultWebApplicationServer webApplicationServer = new DefaultWebApplicationServer();
        DefaultHttpServer httpServer = new DefaultHttpServer(8080, webApplicationServer, ssl);
        httpServer.start();
        webApplicationServer.start();
        File webappsDirectory = new File("webapps");
        File[] webapps = webappsDirectory.listFiles();
        if (webapps != null && webapps.length > 0) {
            for (File webapp : webapps) {
                if (webapp.getName().toLowerCase().endsWith(".war")) {
                    String contextPath = webapp.getName().substring(0, webapp.getName().length() - 4);
                    File webAppDirectory = new File(webappsDirectory, contextPath);
                    extractWarFile(webapp, webAppDirectory);

                    DefaultWebApplication webApplication = new DefaultWebApplication();
                    webApplication.setAttribute(SERVER_PIRANHA, this);
                    webApplication.addResource(new DirectoryResource(webAppDirectory));
                    DefaultWebApplicationClassLoader classLoader
                            = new DefaultWebApplicationClassLoader(webAppDirectory);
                    webApplication.setClassLoader(classLoader);

                    if (classLoader.getResource("/META-INF/services/" + WebApplicationExtension.class.getName()) == null) {
                        DefaultWebApplicationExtensionContext extensionContext = new DefaultWebApplicationExtensionContext();
                        extensionContext.add(ServletExtension.class);
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
                    webApplication.initialize();
                    webApplication.start();
                }
            }
        }
        long finishTime = System.currentTimeMillis();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Started Piranha");
            LOGGER.log(Level.INFO, "It took {0} milliseconds", finishTime - startTime);
        }

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
                System.exit(0);
            }
        }
        finishTime = System.currentTimeMillis();
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Stopped Piranha");
            LOGGER.log(Level.INFO, "We ran for {0} milliseconds", finishTime - startTime);
        }
    }
}
