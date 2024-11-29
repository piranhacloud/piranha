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
package cloud.piranha.arquillian.managed;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The JUnit tests for the ManagedPiranhaContainerConfiguration class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ManagedPiranhaContainerConfigurationTest {

    @Test
    void testGetHttpPortDefault() {
        System.clearProperty("piranha.httpPort");
        ManagedPiranhaContainerConfiguration configuration = new ManagedPiranhaContainerConfiguration();
        int port = configuration.getHttpPort();
        assertTrue(port > 0, "The port should be a positive integer");
        System.clearProperty("piranha.httpPort");
    }

    @Test
    void testGetHttpPortSet() {
        System.setProperty("piranha.httpPort", "8080");
        ManagedPiranhaContainerConfiguration configuration = new ManagedPiranhaContainerConfiguration();
        int port = configuration.getHttpPort();
        assertEquals(8080, port, "The port should be 8080");
        System.clearProperty("piranha.httpPort");
    }

    @Test
    void testGetHttpPortInvalid() {
        System.setProperty("piranha.httpPort", "invalid");
        assertThrows(NumberFormatException.class, () -> {
            ManagedPiranhaContainerConfiguration configuration = new ManagedPiranhaContainerConfiguration();
            configuration.getHttpPort();
        }, "A NumberFormatException should be thrown for invalid port");
        System.clearProperty("piranha.httpPort");
    }

    @Test
    void testGetDistribution() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        assertEquals("coreprofile", config.getDistribution());
    }

    @Test
    void testSetDistribution() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        config.setDistribution("webprofile");
        assertEquals("webprofile", config.getDistribution());
    }

    @Test
    void testGetHttpPort() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        assertTrue(config.getHttpPort() > 0);
    }

    @Test
    void testSetHttpPort() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        config.setHttpPort(8080);
        assertEquals(8080, config.getHttpPort());
    }

    @Test
    void testGetJvmArguments() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        assertEquals("", config.getJvmArguments());
    }

    @Test
    void testSetJvmArguments() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        config.setJvmArguments("-Xmx512m");
        assertEquals("-Xmx512m", config.getJvmArguments());
    }

    @Test
    void testGetProtocol() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        assertEquals("Servlet 6.0", config.getProtocol());
    }

    @Test
    void testSetProtocol() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        config.setProtocol("Servlet 5.0");
        assertEquals("Servlet 5.0", config.getProtocol());
    }

    @Test
    void testIsDebug() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        assertFalse(config.isDebug());
    }

    @Test
    void testSetDebug() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        config.setDebug(true);
        assertTrue(config.isDebug());
    }

    @Test
    void testIsSuspend() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        assertFalse(config.isSuspend());
    }

    @Test
    void testSetSuspend() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        config.setSuspend(true);
        assertTrue(config.isSuspend());
    }

    @Test
    void testValidate() {
        ManagedPiranhaContainerConfiguration config = new ManagedPiranhaContainerConfiguration();
        assertDoesNotThrow(config::validate);
    }
}
