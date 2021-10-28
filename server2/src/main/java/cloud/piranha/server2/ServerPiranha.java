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
package cloud.piranha.server2;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ServiceLoader;
import java.lang.System.Logger.Level;
import java.lang.System.Logger;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.micro.embedded.MicroWebApplication;
import cloud.piranha.micro.loader.MicroConfiguration;
import cloud.piranha.micro.loader.MicroOuterDeployer;
import jakarta.servlet.ServletException;

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
 * @author Arjan Tijms
 */
public class ServerPiranha implements Piranha, Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ServerPiranha.class.getPackageName());

    /**
     * Stores the one and only instance of the server.
     */
    private static ServerPiranha INSTANCE;

    /**
     * Stores the SSL flag.
     */
    private boolean ssl = false;

    /**
     * Stores the HTTP web application server.
     */
    private HttpWebApplicationServer webApplicationServer;    
    
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
        LOGGER.log(INFO, () -> "Starting Piranha");

        webApplicationServer = new HttpWebApplicationServer();
        HttpServer httpServer = ServiceLoader.load(HttpServer.class).findFirst().orElseThrow();
        httpServer.setServerPort(8080);
        httpServer.setHttpServerProcessor(webApplicationServer);
        httpServer.setSSL(ssl);
        httpServer.start();
        webApplicationServer.start();

        File[] webapps = new File("webapps").listFiles();
        if (webapps != null) {
            if (webapps.length != 0) {
                // Limit threads used by Weld, since default is Runtime.getRuntime().availableProcessors(), which is per deployment.
                int threadsPerApp = Math.max(2, Runtime.getRuntime().availableProcessors() / webapps.length);

                System.setProperty("org.jboss.weld.executor.threadPoolSize", threadsPerApp + "");
            }

            File deployingFile = createDeployingFile();

            Arrays.stream(webapps)
                    .parallel()
                    .filter(warFile -> warFile.getName().toLowerCase().endsWith(".war"))
                    .forEach(warFile -> deploy(warFile, webApplicationServer));

            if (deployingFile.delete()) {
                LOGGER.log(Level.WARNING, "Unable to delete deploying file");
            }
        }

        long finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Started Piranha");
        LOGGER.log(INFO, "It took {0} milliseconds", finishTime - startTime);

        File startedFile = createStartedFile();

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
                if (!startedFile.delete()) {
                    LOGGER.log(Level.WARNING, "Unable to delete PID file");
                }
                System.exit(0);
            }
        }

        finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Stopped Piranha");
        LOGGER.log(INFO, "We ran for {0} milliseconds", finishTime - startTime);
    }

    private void deploy(File warFile, HttpWebApplicationServer webApplicationServer) {
        String contextPath = getContextPath(warFile);

        MicroConfiguration configuration = new MicroConfiguration();
        configuration.setRoot(contextPath);
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
            LOGGER.log(Level.ERROR, () -> "Failed to initialize app " + contextPath, e);
        }
    }

    private String getContextPath(File warFile) {
        String contextPath = warFile.getName().substring(0, warFile.getName().length() - 4);

        if (contextPath.equalsIgnoreCase("ROOT")) {
            contextPath = "";
        } else if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }

        return contextPath;
    }

    private File createDeployingFile() {
        File deployingFile = new File("webapps/deploying");
        try {
            if (!deployingFile.createNewFile()) {
                LOGGER.log(Level.WARNING, "Unable to create deploying file");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "I/O error occurred creating deploying file", e);
        }
        return deployingFile;
    }

    private File createStartedFile() {
        File startedFile = new File("webapps/started");

        try {
            if (!startedFile.createNewFile()) {
                LOGGER.log(Level.WARNING, "Unable to create started file");
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "I/O error occurred creating started file", e);
        }

        return startedFile;
    }
    
    @Override
    public void service(WebApplicationRequest request, WebApplicationResponse response) 
            throws IOException, ServletException {
        webApplicationServer.service(request, response);
    }    
}
