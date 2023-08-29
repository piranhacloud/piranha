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
import cloud.piranha.core.impl.DefaultPiranhaConfiguration;
import cloud.piranha.extension.coreprofile.CoreProfileExtension;
import cloud.piranha.feature.api.FeatureManager;
import cloud.piranha.feature.exitonstop.ExitOnStopFeature;
import cloud.piranha.feature.http.HttpFeature;
import cloud.piranha.feature.https.HttpsFeature;
import cloud.piranha.feature.impl.DefaultFeatureManager;
import cloud.piranha.feature.logging.LoggingFeature;
import cloud.piranha.feature.pid.PidFeature;
import cloud.piranha.feature.webapp.WebAppFeature;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.INFO;

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
     * Stores the feature manager.
     */
    private final FeatureManager featureManager;

    /**
     * Constructor.
     */
    public CoreProfilePiranha() {
        configuration = new DefaultPiranhaConfiguration();
        configuration.setClass("extensionClass", CoreProfileExtension.class);
        configuration.setBoolean("exitOnStop", false);
        configuration.setInteger("httpPort", 8080);
        configuration.setInteger("httpsPort", -1);
        featureManager = new DefaultFeatureManager();
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
        
        /*
         * Setup logging.
         */
        LoggingFeature loggingFeature = new LoggingFeature();
        featureManager.addFeature(loggingFeature);
        loggingFeature.setLevel(configuration.getString("loggingLevel"));
        loggingFeature.init();
        loggingFeature.start();

        LOGGER.log(INFO, () -> "Starting Piranha");

        /*
         * Setup the web application.
         */
        WebAppFeature webAppFeature = new WebAppFeature();
        featureManager.addFeature(webAppFeature);
        webAppFeature.setContextPath(configuration.getString("contextPath"));
        webAppFeature.setExtensionClass((Class<? extends WebApplicationExtension>) configuration.getClass("extensionClass"));
        webAppFeature.setJpmsEnabled(configuration.getBoolean("jpmsEnabled", false));
        webAppFeature.setWarFile(configuration.getFile("warFile"));
        webAppFeature.setWebAppDir(configuration.getFile("webAppDir"));
        webAppFeature.init();
        webAppFeature.start();

        /*
         * Setup the HTTP endpoint (if applicable).
         */
        if (configuration.getInteger("httpPort") > 0) {
            HttpFeature httpFeature = new HttpFeature();
            featureManager.addFeature(httpFeature);
            httpFeature.setHttpServerClass(configuration.getString("httpServerClass"));
            httpFeature.setPort(configuration.getInteger("httpPort"));
            httpFeature.init();
            httpFeature.getHttpServer().setHttpServerProcessor(webAppFeature.getHttpServerProcessor());
            httpFeature.start();
        }

        /**
         * Setup the HTTPS endpoint (if applicable).
         */
        if (configuration.getInteger("httpsPort") > 0) {
            HttpsFeature httpsFeature = new HttpsFeature();
            featureManager.addFeature(httpsFeature);
            httpsFeature.setHttpsKeystoreFile(configuration.getString("httpsKeystoreFile"));
            httpsFeature.setHttpsKeystorePassword(configuration.getString("httpsKeystorePassword"));
            httpsFeature.setHttpsServerClass(configuration.getString("httpsServerClass"));
            httpsFeature.setHttpsTruststoreFile(configuration.getString("httpsTruststoreFile"));
            httpsFeature.setHttpsTruststorePassword(configuration.getString("httpsTruststorePassword"));
            httpsFeature.setPort(configuration.getInteger("httpsPort"));
            httpsFeature.init();
            httpsFeature.getHttpsServer().setHttpServerProcessor(webAppFeature.getHttpServerProcessor());
            httpsFeature.start();
        }

        /*
         * Setup the 'Exit on Stop' (if applicable).
         */
        if (configuration.getBoolean("exitOnStop", false)) {
            ExitOnStopFeature exitOnStopFeature = new ExitOnStopFeature();
            featureManager.addFeature(exitOnStopFeature);
            exitOnStopFeature.init();
            exitOnStopFeature.start();
        }
        
        /*
         * Setup the PID feature.
         */
        PidFeature pidFeature = new PidFeature();
        if (configuration.getLong("pid") != null) {
            pidFeature.setPid(configuration.getLong("pid"));
        }

        long finishTime = System.currentTimeMillis();
        LOGGER.log(INFO, "Started Piranha");
        LOGGER.log(INFO, "It took {0} milliseconds", finishTime - startTime);

        try {
            pidFeature.getThread().join();
        } catch (InterruptedException ie) {
            // safe to ignore.
        }
    }

    /**
     * Start the server.
     */
    public void start() {
    }

    /**
     * Stop the server.
     */
    public void stop() {
        featureManager.stop();
    }
}
