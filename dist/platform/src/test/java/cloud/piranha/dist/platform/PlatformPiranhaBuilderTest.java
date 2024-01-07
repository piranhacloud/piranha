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
package cloud.piranha.dist.platform;

import cloud.piranha.extension.platform.PlatformExtension;
import java.net.ConnectException;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the PlatformPiranhaBuilder class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class PlatformPiranhaBuilderTest {

    /**
     * Test httpPort method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testHttpPort() throws Exception {
        PlatformPiranha piranha = new PlatformPiranhaBuilder()
                .extensionClass(PlatformExtension.class)
                .httpPort(8118)
                .build();
        piranha.start();
        Thread.sleep(5000);
        try ( Socket socket = new Socket("localhost", 8118)) {
            assertNotNull(socket.getOutputStream());
        }
        piranha.stop();
        Thread.sleep(5000);
    }

    /**
     * Test httpsPort method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testHttpPort2() throws Exception {
        PlatformPiranha piranha = new PlatformPiranhaBuilder()
                .extensionClass(PlatformExtension.class)
                .httpPort(-1)
                .httpsPort(8043)
                .build();
        piranha.start();
        Thread.sleep(5000);
        try ( Socket socket = new Socket("localhost", 8080)) {
            fail();
        } catch (ConnectException e) {
        }
        piranha.stop();
        Thread.sleep(5000);
    }

    /**
     * Test httpsPort method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testHttpsPort2() throws Exception {
        PlatformPiranha piranha = new PlatformPiranhaBuilder()
                .extensionClass(PlatformExtension.class)
                .httpsKeystoreFile("src/main/zip/etc/keystore.jks")
                .httpsKeystorePassword("password")
                .httpPort(8228)
                .httpsPort(8338)
                .build();
        piranha.start();
        Thread.sleep(5000);
        SocketFactory factory = SSLSocketFactory.getDefault();
        try ( SSLSocket socket = (SSLSocket) factory.createSocket("localhost", 8338)) {
            assertNotNull(socket.getOutputStream());
            assertNotNull(socket.getSSLParameters());
            assertEquals("TLSv1.3", socket.getSSLParameters().getProtocols()[0]);
        }
        piranha.stop();
        Thread.sleep(5000);
    }

    /**
     * Test extensionClass method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testDefaultExtensionClass() throws Exception {
        PlatformPiranha piranha = new PlatformPiranhaBuilder()
                .extensionClass(PlatformExtension.class.getName())
                .httpPort(8080)
                .verbose(true)
                .build();
        piranha.start();
        Thread.sleep(5000);
        try ( Socket socket = new Socket("localhost", 8080)) {
            assertNotNull(socket.getOutputStream());
        } catch (ConnectException e) {
        }
        piranha.stop();
        Thread.sleep(5000);
    }
}
