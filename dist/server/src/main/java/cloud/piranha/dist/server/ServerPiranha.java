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
package cloud.piranha.dist.server;

import cloud.piranha.core.api.Piranha;
import cloud.piranha.core.api.PiranhaConfiguration;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.impl.DefaultPiranhaConfiguration;
import cloud.piranha.feature.api.FeatureManager;
import cloud.piranha.feature.exitonstop.ExitOnStopFeature;
import cloud.piranha.feature.http.HttpFeature;
import cloud.piranha.feature.https.HttpsFeature;
import cloud.piranha.feature.impl.DefaultFeatureManager;
import cloud.piranha.feature.logging.LoggingFeature;
import cloud.piranha.feature.webapps.WebAppsFeature;
import cloud.piranha.http.api.HttpServer;
import java.io.File;
import java.io.IOException;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;
import java.nio.file.Files;

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
     * Stores the HTTP server.
     */
    private HttpServer httpServer;
    
    /**
     * Stores the HTTP feature.
     */
    private HttpsFeature httpsFeature;

    /**
     * Stores the HTTP server.
     */
    private HttpServer httpsServer;

    /**
     * Stores the started flag.
     */
    private boolean started = false;

    /**
     * Stores the thread we use.
     */
    private Thread thread;

    /**
     * Stores the WebAppsFeature.
     */
    private WebAppsFeature webAppsFeature;

    /**
     * Constructor.
     */
    public ServerPiranha() {
        configuration = new DefaultPiranhaConfiguration();
        configuration.setBoolean("exitOnStop", false);
        configuration.setInteger("httpPort", 8080);
        configuration.setInteger("httpsPort", -1);
        configuration.setBoolean("jpmsEnabled", false);
        configuration.setFile("webAppsDir", new File("webapps"));
        featureManager = new DefaultFeatureManager();
    }

    @Override
    public PiranhaConfiguration getConfiguration() {
        return configuration;
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

        LoggingFeature loggingFeature = new LoggingFeature();
        featureManager.addFeature(loggingFeature);
        loggingFeature.setLevel(configuration.getString("loggingLevel"));
        loggingFeature.init();
        loggingFeature.start();

        LOGGER.log(INFO, () -> "Starting Piranha");

        webAppsFeature = new WebAppsFeature();
        featureManager.addFeature(webAppsFeature);
        webAppsFeature.setExtensionClass((Class<? extends WebApplicationExtension>) configuration.getClass("extensionClass"));
        webAppsFeature.setJpmsEnabled(configuration.getBoolean("jpmsEnabled", false));
        webAppsFeature.setWebAppsDir(configuration.getFile("webAppsDir"));
        webAppsFeature.init();
        webAppsFeature.start();
        
        /*
         * Construct, initialize and start HTTP endpoint (if applicable).
         */
        if (configuration.getInteger("httpPort") > 0) {
            httpFeature = new HttpFeature();
            httpFeature.setHttpServerClass(configuration.getString("httpServerClass"));
            httpFeature.setPort(configuration.getInteger("httpPort"));
            httpFeature.init();
            httpFeature.getHttpServer().setHttpServerProcessor(webAppsFeature.getHttpServerProcessor());
            httpFeature.start();
            httpServer = httpFeature.getHttpServer();
        }
        
        /*
         * Construct, initialize and start HTTPS endpoint (if applicable).
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
            httpsFeature.getHttpsServer().setHttpServerProcessor(webAppsFeature.getHttpServerProcessor());
            httpsFeature.start();
            httpServer = httpsFeature.getHttpsServer();
        }

        if (configuration.getBoolean("exitOnStop", false)) {
            ExitOnStopFeature exitOnStopFeature = new ExitOnStopFeature();
            featureManager.addFeature(exitOnStopFeature);
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
        
        featureManager.stop();
    }

    /**
     * Set the web applications directory.
     *
     * @param webAppsDir the web applications directory.
     */
    public void setWebAppsDir(File webAppsDir) {
        this.configuration.setFile("webAppsDir", webAppsDir);
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
}
