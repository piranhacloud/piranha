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

import cloud.piranha.http.api.HttpServer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the HttpFeature class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpFeatureTest {

    /**
     * Test destroy method.
     */
    @Test
    void testDestroy() {
        HttpFeature feature = new HttpFeature();
        feature.init();
        assertNotNull(feature.getHttpServer());
        feature.destroy();
        assertNull(feature.getHttpServer());
        feature.setHttpServerClass("BOGUS");
        feature.init();
        assertNull(feature.getHttpServer());
        feature.destroy();
        assertNull(feature.getHttpServer());
    }

    /**
     * Test getHttpServer method.
     */
    @Test
    void testGetHttpServerMethod() {
        HttpFeature feature = new HttpFeature();
        feature.init();
        HttpServer httpsServer = feature.getHttpServer();
        feature.setHttpServer(null);
        assertNull(feature.getHttpServer());
        feature.setHttpServer(httpsServer);
        assertEquals(httpsServer, feature.getHttpServer());
    }

    /**
     * Test getHttpServerClass method.
     */
    @Test
    void testGetHttpServerClass() {
        HttpFeature feature = new HttpFeature();
        feature.setHttpServerClass("BOGUS");
        assertEquals("BOGUS", feature.getHttpServerClass());
        feature.setHttpServerClass(null);
        assertNotNull(feature.getHttpServerClass());
    }

    /**
     * Test getPort method.
     */
    @Test
    void testGetPort() {
        HttpFeature feature = new HttpFeature();
        feature.setPort(1234);
        assertEquals(1234, feature.getPort());
    }
    
    /**
     * Test stop method.
     */
    @Test
    void testStop() {
        HttpFeature feature = new HttpFeature();
        feature.init();
        assertFalse(feature.getHttpServer().isRunning());
        feature.start();
        assertTrue(feature.getHttpServer().isRunning());
        feature.stop();
        assertFalse(feature.getHttpServer().isRunning());
    }
}
