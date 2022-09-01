/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.test.coreprofile;

import cloud.piranha.coreprofile.CoreProfilePiranha;
import cloud.piranha.coreprofile.CoreProfilePiranhaBuilder;
import cloud.piranha.extension.coreprofile.CoreProfileExtension;
import cloud.piranha.test.common.PiranhaStartup;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import me.alexpanov.net.FreePortFinder;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The 'Hello World!' integration test.
 *
 * <p>
 * This test illustrates how to do integration testing with Piranha Core Profile
 * using a programmatic API. Note that doing integration testing this way is
 * really meant for the Piranha project itself. If you are developing a web
 * application we recommend you use the Piranha Maven plugin instead.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HelloWorldServletMicroIT {

    /**
     * Stores the Piranha instance.
     */
    private CoreProfilePiranha piranha;

    /**
     * The port the piranha instance will bind to
     */
    private int piranhaPort;

    /**
     * After each test.
     */
    @AfterEach
    void afterAll() {
        piranha.stop();
    }

    /**
     * Before each test.
     */
    @BeforeEach
    void beforeEach() throws InterruptedException {
        piranhaPort = FreePortFinder.findFreeLocalPort();
        piranha = new CoreProfilePiranhaBuilder()
                .extensionClass(CoreProfileExtension.class)
                .warFile("target/webapps/ROOT.war")
                .webAppDir("target/webapps/ROOT")
                .httpPort(piranhaPort)
                .verbose(true)
                .build();
        piranha.start();
        PiranhaStartup.waitUntilPiranhaReady(piranhaPort);
    }

    /**
     * Test the 'Hello World!' resource.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testHelloWorld() throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(60)).build();
        HttpRequest request = HttpRequest
                .newBuilder(new URI("http://localhost:" + piranhaPort + "/helloworld"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("Hello World"));
    }
}
