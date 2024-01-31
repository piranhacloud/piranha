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
package cloud.piranha.feature.https;

import cloud.piranha.feature.impl.DefaultFeature;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.impl.DefaultHttpServer;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;
import java.lang.reflect.InvocationTargetException;

/**
 * The HTTPS feature that exposes an HTTPS endpoint.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpsFeature extends DefaultFeature {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(HttpsFeature.class.getName());

    /**
     * Stores the HTTPS server.
     */
    private HttpServer httpsServer;

    /**
     * Stores the HTTPS server class.
     */
    private String httpsServerClass = DefaultHttpServer.class.getName();

    /**
     * Stores the HTTPS port.
     */
    private int port = 8043;

    @Override
    public void destroy() {
        httpsServer = null;
    }

    /**
     * Get the HTTPS keystore file.
     * 
     * @return the HTTPS keystore file.
     */
    public String getHttpsKeystoreFile() {
        return System.getProperty("javax.net.ssl.keyStore");
    }

    /**
     * Get the HTTPS keystore password.
     * 
     * @return the HTTPS keystore password.
     */
    public String getHttpsKeystorePassword() {
        return System.getProperty("javax.net.ssl.keyStorePassword");
    }
    
    /**
     * Get the HTTPS server.
     *
     * @return the HTTPS server.
     */
    public HttpServer getHttpsServer() {
        return httpsServer;
    }

    /**
     * Get the HTTPS server class.
     *
     * @return the HTTPS server class.
     */
    public String getHttpsServerClass() {
        return httpsServerClass;
    }

    /**
     * Get the HTTPS truststore file.
     * 
     * @return the HTTPS truststore file.
     */
    public String getHttpsTruststoreFile() {
        return System.getProperty("javax.net.ssl.trustStore");
    }

    /**
     * Get the HTTPS truststore password.
     * 
     * @return the HTTPS truststore password.
     */
    public String getHttpsTruststorePassword() {
        return System.getProperty("javax.net.ssl.trustStorePassword");
    }    
    /**
     * Get the port.
     *
     * @return the port.
     */
    public int getPort() {
        return port;
    }

    @Override
    public void init() {
        if (port > 0) {
            try {
                httpsServer = (HttpServer) Class.forName(httpsServerClass)
                        .getDeclaredConstructor().newInstance();
            } catch (ClassNotFoundException | IllegalAccessException
                    | IllegalArgumentException | InstantiationException
                    | NoSuchMethodException | SecurityException
                    | InvocationTargetException t) {
                LOGGER.log(ERROR, "Unable to construct HTTP server", t);
            }
            if (httpsServer != null) {
                httpsServer.setServerPort(port);
                httpsServer.setSSL(true);
            }
        }
    }

    /**
     * Set the HTTPS keystore file.
     *
     * <p>
     * This is currently a convenience wrapper around the
     * <code>javax.net.ssl.keyStore</code> system property. Note using this
     * method sets the property for the entire JVM.
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
     * This is currently a convenience wrapper around the
     * <code>javax.net.ssl.keyStorePassword</code> system property. Note using
     * this method sets the property for the entire JVM.
     * </p>
     *
     * @param httpsKeystorePassword the HTTPS keystore password.
     */
    public void setHttpsKeystorePassword(String httpsKeystorePassword) {
        if (httpsKeystorePassword != null) {
            System.setProperty("javax.net.ssl.keyStorePassword", httpsKeystorePassword);
        }
    }

    /**
     * Set the HTTPS server.
     *
     * @param httpsServer the HTTPS server.
     */
    public void setHttpsServer(HttpServer httpsServer) {
        this.httpsServer = httpsServer;
    }

    /**
     * Set the HTTP server class.
     *
     * @param httpsServerClass the HTTP server class.
     */
    public void setHttpsServerClass(String httpsServerClass) {
        if (httpsServerClass != null) {
            this.httpsServerClass = httpsServerClass;
        } else {
            this.httpsServerClass = DefaultHttpServer.class.getName();
        }
    }

    /**
     * Set the HTTPS truststore file.
     *
     * <p>
     * This is currently a convenience wrapper around the
     * <code>javax.net.ssl.trustStore</code> system property. Note using this
     * method sets the property for the entire JVM.
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
     * Set the HTTPS truststore password.
     *
     * <p>
     * This is currently a convenience wrapper around the
     * <code>javax.net.ssl.trustStorePassword</code> system property. Note using
     * this method sets the property for the entire JVM.
     * </p>
     *
     * @param httpsTruststorePassword the HTTPS truststore password.
     */
    public void setHttpsTruststorePassword(String httpsTruststorePassword) {
        if (httpsTruststorePassword != null) {
            System.setProperty("javax.net.ssl.trustStorePassword", httpsTruststorePassword);
        }
    }
    
    /**
     * Set the port.
     *
     * @param port the port.
     */
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        if (httpsServer != null) {
            httpsServer.start();
        }
    }

    @Override
    public void stop() {
        if (httpsServer != null) {
            httpsServer.stop();
        }
    }
}
