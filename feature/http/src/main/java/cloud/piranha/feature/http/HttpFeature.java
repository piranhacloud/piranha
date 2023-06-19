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
package cloud.piranha.feature.http;

import cloud.piranha.feature.api.Feature;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.impl.DefaultHttpServer;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * The HTTP feature that exposes an HTTP endpoint.
 * 
 * <br>
 * <br>
 * <table border="1">
 *  <caption>The feature map</caption>
 *  <tr>
 *   <th>key</th>
 *   <th>value</th>
 *  </tr>
 *  <tr>
 *   <td>port</td>
 *   <td>the HTTP port we are listening on</td>
 *  </tr>
 *  <tr>
 *   <td>server</td>
 *   <td>the HttpServer instance</td>
 *  </tr>
 *  <tr>
 *   <td>serverClass</td>
 *   <td>the class name of the HttpServer implementation</td>
 *  </tr>
 * </table>
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpFeature implements Feature {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(HttpFeature.class.getName());

    /**
     * Stores the feature map.
     */
    private Map<String, Object> featureMap;

    @Override
    public void destroy() {
        featureMap.remove("server");
    }
    
    @Override
    public Map<String, Object> getFeatureMap() {
        return featureMap;
    }

    @Override
    public void init() {
        int port = (Integer) featureMap.getOrDefault("port", 8080);
        if (port > 0) {
            HttpServer server = null;
            String serverClass = (String) featureMap.getOrDefault(
                    "serverClass", DefaultHttpServer.class.getName());

            try {
                server = (HttpServer) Class.forName(serverClass)
                        .getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | IllegalAccessException
                    | IllegalArgumentException | InstantiationException
                    | NoSuchMethodException | SecurityException
                    | InvocationTargetException t) {
                LOGGER.log(ERROR, "Unable to construct HTTP server", t);
            }
            if (server != null) {
                server.setServerPort(port);
                featureMap.put("server", server);
            }
        }
    }
    
    @Override
    public void start() {
        HttpServer server = (HttpServer) featureMap.get("server");
        server.start();
    }
    
    @Override
    public void stop() {
        HttpServer server = (HttpServer) featureMap.get("server");
        server.stop();
    }
}
