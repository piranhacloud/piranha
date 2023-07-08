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
package cloud.piranha.feature.crac;

import cloud.piranha.feature.api.Feature;
import cloud.piranha.feature.api.FeatureManager;
import cloud.piranha.feature.http.HttpFeature;
import cloud.piranha.feature.https.HttpsFeature;
import cloud.piranha.http.api.HttpServer;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import java.lang.reflect.InvocationTargetException;

/**
 * The CRaC feature that enables Project CRaC.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class CracFeature implements Feature {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(CracFeature.class.getName());

    /**
     * Stores the feature manager.
     */
    private FeatureManager featureManager;

    /**
     * Create the CRaC HttpServer instance.
     *
     * @param httpServer the HttpServer instance to wrap.
     * @return the CRaC HttpServer instance.
     */
    private HttpServer createCracHttpServer(HttpServer httpServer) {
        HttpServer cracHttpServer = null;
        try {
            cracHttpServer = (HttpServer) Class
                    .forName("cloud.piranha.http.crac.CracHttpServer")
                    .getDeclaredConstructor(HttpServer.class)
                    .newInstance(httpServer);
        } catch (ClassNotFoundException | IllegalAccessException
                | IllegalArgumentException | InstantiationException
                | NoSuchMethodException | SecurityException
                | InvocationTargetException t) {
            LOGGER.log(ERROR, "Unable to construct CracHttpServer", t);
        }
        return cracHttpServer;
    }

    @Override
    public FeatureManager getFeatureManager() {
        return featureManager;
    }

    @Override
    public void setFeatureManager(FeatureManager featureManager) {
        this.featureManager = featureManager;
    }

    @Override
    public void init() {
        for (Feature feature : featureManager.getFeatures()) {
            if (feature instanceof HttpFeature httpFeature) {
                HttpServer cracHttpServer = createCracHttpServer(httpFeature.getHttpServer());
                httpFeature.setHttpServer(cracHttpServer);
            }
            if (feature instanceof HttpsFeature httpsFeature) {
                HttpServer cracHttpServer = createCracHttpServer(httpsFeature.getHttpsServer());
                httpsFeature.setHttpsServer(cracHttpServer);
            }
        }
    }
}
