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
package cloud.piranha.feature.https;

import cloud.piranha.http.api.HttpServer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the HttpsFeature class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpsFeatureTest {

    /**
     * Test destroy method.
     */
    @Test
    void testDestroy() {
        HttpsFeature feature = new HttpsFeature();
        feature.init();
        assertNotNull(feature.getHttpsServer());
        feature.destroy();
        assertNull(feature.getHttpsServer());
        feature.setHttpsServerClass("BOGUS");
        feature.init();
        assertNull(feature.getHttpsServer());
        feature.destroy();
        assertNull(feature.getHttpsServer());
    }

    /**
     * Test getHttpsKeystoreFile method.
     */
    @Test
    void testGetHttpsKeystoreFile() {
        HttpsFeature feature = new HttpsFeature();
        feature.setHttpsKeystoreFile("keystore");
        assertEquals("keystore", feature.getHttpsKeystoreFile());
    }

    /**
     * Test getHttpsKeystorePassword method.
     */
    @Test
    public void testGetHttpsKeystorePassword() {
        HttpsFeature feature = new HttpsFeature();
        feature.setHttpsKeystorePassword("password");
        assertEquals("password", feature.getHttpsKeystorePassword());
    }
    
    /**
     * Test getHttpServer method.
     */
    @Test
    void testGetHttpServerMethod() {
        HttpsFeature feature = new HttpsFeature();
        feature.init();
        HttpServer httpsServer = feature.getHttpsServer();
        feature.setHttpsServer(null);
        assertNull(feature.getHttpsServer());
        feature.setHttpsServer(httpsServer);
        assertEquals(httpsServer, feature.getHttpsServer());
    }

    /**
     * Test getHttpServerClass method.
     */
    @Test
    void testGetHttpServerClass() {
        HttpsFeature feature = new HttpsFeature();
        feature.setHttpsServerClass("BOGUS");
        assertEquals("BOGUS", feature.getHttpsServerClass());
        feature.setHttpsServerClass(null);
        assertNotNull(feature.getHttpsServerClass());
    }

    /**
     * Test getHttpsTruststoreFile method.
     */
    @Test
    void testGetHttpsTruststoreFile() {
        HttpsFeature feature = new HttpsFeature();
        feature.setHttpsTruststoreFile("truststore");
        assertEquals("truststore", feature.getHttpsTruststoreFile());
    }

    /**
     * Test getHttpsTruststorePassword method.
     */
    @Test
    void testGetHttpsTruststorePassword() {
        HttpsFeature feature = new HttpsFeature();
        feature.setHttpsTruststorePassword("password");
        assertEquals("password", feature.getHttpsTruststorePassword());
    }

    /**
     * Test getPort method.
     */
    @Test
    void testGetPort() {
        HttpsFeature feature = new HttpsFeature();
        feature.setPort(1234);
        assertEquals(1234, feature.getPort());
    }
    
    /**
     * Test stop method.
     */
    @Test
    void testStop() {
        HttpsFeature feature = new HttpsFeature();
        feature.init();
        assertFalse(feature.getHttpsServer().isRunning());
        feature.start();
        assertTrue(feature.getHttpsServer().isRunning());
        feature.stop();
        assertFalse(feature.getHttpsServer().isRunning());
    }
}
