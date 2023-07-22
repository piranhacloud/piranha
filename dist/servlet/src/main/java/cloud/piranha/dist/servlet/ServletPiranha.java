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
package cloud.piranha.dist.servlet;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.core.api.PiranhaConfiguration;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.impl.DefaultPiranhaConfiguration;
import cloud.piranha.extension.servlet.ServletExtension;
import cloud.piranha.feature.api.FeatureManager;
import cloud.piranha.feature.exitonstop.ExitOnStopFeature;
import cloud.piranha.feature.http.HttpFeature;
import cloud.piranha.feature.https.HttpsFeature;
import cloud.piranha.feature.impl.DefaultFeatureManager;
import cloud.piranha.feature.webapp.WebAppFeature;
import cloud.piranha.http.api.HttpServer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import java.lang.reflect.InvocationTargetException;

/**
 * The Piranha Servlet runtime.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletPiranha implements Piranha, Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ServletPiranha.class.getName());

    /**
     * Stores the configuration.
     */
    private final PiranhaConfiguration configuration;

    /**
     * Stores the feature manager.
     */
    private final FeatureManager featureManager;

    /**
     * Stores the HTTP feature.
     */
    private HttpFeature httpFeature;

    /**
     * Stores the HTTP feature.
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
     * Stores the web application feature.
     */
    private WebAppFeature webAppFeature;

    /**
     * Constructor.
     */
    public ServletPiranha() {
        configuration = new DefaultPiranhaConfiguration();
        configuration.setBoolean("cracEnabled", false);
        configuration.setBoolean("exitOnStop", false);
        configuration.setClass("extensionClass", ServletExtension.class);
        configuration.setInteger("httpPort", 8080);
        configuration.setInteger("httpsPort", -1);
        configuration.setBoolean("jpmsEnabled", false);
        featureManager = new DefaultFeatureManager();
    }

    @Override
    public PiranhaConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        LOGGER.log(INFO, () -> "Starting Piranha");
        
        webAppFeature = new WebAppFeature();
        featureManager.addFeature(webAppFeature);
        webAppFeature.setContextPath(configuration.getString("contextPath"));
        webAppFeature.setExtensionClass((Class<? extends WebApplicationExtension>) configuration.getClass("extensionClass"));
        webAppFeature.setJpmsEnabled(configuration.getBoolean("jpmsEnabled", false));
        webAppFeature.setWarFile(configuration.getFile("warFile"));
        webAppFeature.setWebAppDir(configuration.getFile("webAppDir"));
        webAppFeature.init();
        webAppFeature.start();

        /*
         * Construct, initialize and start HTTP endpoint (if applicable).
         */
        if (configuration.getInteger("httpPort") > 0) {
            httpFeature = new HttpFeature();
            httpFeature.setHttpServerClass(configuration.getString("httpServerClass"));
            httpFeature.setPort(configuration.getInteger("httpPort"));
            httpFeature.init();
            httpFeature.getHttpServer().setHttpServerProcessor(webAppFeature.getHttpServerProcessor());

            /*
             * Enable Project CRaC.
             */
            if (configuration.getBoolean("cracEnabled", false)) {
                HttpServer httpServer = httpFeature.getHttpServer();
                try {
                    HttpServer cracHttpServer = (HttpServer) Class
                            .forName("cloud.piranha.http.crac.CracHttpServer")
                            .getDeclaredConstructor(HttpServer.class)
                            .newInstance(httpServer);
                    httpServer = cracHttpServer;
                } catch (ClassNotFoundException | IllegalAccessException
                        | IllegalArgumentException | InstantiationException
                        | NoSuchMethodException | SecurityException
                        | InvocationTargetException t) {
                    LOGGER.log(ERROR, "Unable to construct HTTP server", t);
                }
                httpFeature.setHttpServer(httpServer);
            }
            httpFeature.start();
        }

        /*
         * Construct, initialize and start HTTP endpoint (if applicable).
         */
        if (configuration.getInteger("httpsPort") > 0) {
            httpsFeature = new HttpsFeature();
            httpsFeature.setHttpsKeystoreFile(configuration.getString("httpsKeystoreFile"));
            httpsFeature.setHttpsKeystorePassword(configuration.getString("httpsKeystorePassword"));
            httpsFeature.setHttpsServerClass(configuration.getString("httpsServerClass"));
            httpsFeature.setHttpsTruststoreFile(configuration.getString("httpsTruststoreFile"));
            httpsFeature.setHttpsTruststorePassword(configuration.getString("httpsTruststorePassword"));
            httpsFeature.setPort(configuration.getInteger("httpsPort"));
            httpsFeature.init();
            httpsFeature.getHttpsServer().setHttpServerProcessor(webAppFeature.getHttpServerProcessor());

            /*
             * Enable Project CRaC.
             */
            if (configuration.getBoolean("cracEnabled", false)) {
                HttpServer httpServer = httpsFeature.getHttpsServer();
                try {
                    HttpServer cracHttpServer = (HttpServer) Class
                            .forName("cloud.piranha.http.crac.CracHttpServer")
                            .getDeclaredConstructor(HttpServer.class)
                            .newInstance(httpServer);
                    httpServer = cracHttpServer;
                } catch (ClassNotFoundException | IllegalAccessException
                        | IllegalArgumentException | InstantiationException
                        | NoSuchMethodException | SecurityException
                        | InvocationTargetException t) {
                    LOGGER.log(ERROR, "Unable to construct HTTP server", t);
                }
                httpsFeature.setHttpsServer(httpServer);
            }
            httpsFeature.start();
        }

        if (configuration.getBoolean("exitOnStop", false)) {
            ExitOnStopFeature exitOnStopFeature = new ExitOnStopFeature();
            featureManager.addFeature(exitOnStopFeature);
        }

        long finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Started Piranha");
        LOGGER.log(INFO, "It took {0} milliseconds", finishTime - startTime);

        if (configuration.getLong("pid") != null) {
            File pidFile = new File("tmp", "piranha.pid");
            if (!pidFile.getParentFile().exists() && !pidFile.getParentFile().mkdirs()) {
                LOGGER.log(WARNING, "Unable to create tmp directory for PID file");
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter(pidFile))) {
                writer.println(configuration.getLong("pid"));
                writer.flush();
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "Unable to write PID file", ioe);
            }
        }

        while (!stop) {
            if (configuration.getLong("pid") != null) {
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
        featureManager.stop();
    }
}
