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
package cloud.piranha.http.tests;

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.api.HttpServerProcessor;
import static cloud.piranha.http.api.HttpServerProcessorEndState.COMPLETED;
import cloud.piranha.http.api.HttpServerRequest;
import cloud.piranha.http.api.HttpServerResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import me.alexpanov.net.FreePortFinder;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the HttpServerRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class HttpServerRequestTest {

    /**
     * Constructor.
     */
    public HttpServerRequestTest() {
    }

    /**
     * Create server with a port and processor.
     *
     * @param portNumber the port number.
     * @param processor the HTTP processor.
     * @return the HTTP server.
     */
    protected abstract HttpServer createServer(int portNumber, HttpServerProcessor processor);
    
    /**
     * Test getRequestTarget method.
     */
    @Test
    void testGetRequestTarget() {
        System.clearProperty("requestTarget");
        int port = FreePortFinder.findFreeLocalPort();
        HttpServer server = createServer(port,
                (HttpServerRequest request, HttpServerResponse response) -> {
                    System.setProperty("requestTarget", request.getRequestTarget());
                    return COMPLETED;
                });
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                    URI.create("http://localhost:" + port)).build();
            client.send(request, BodyHandlers.discarding());
        } catch (Exception e) {
            // do nothing
        } finally {
            server.stop();
        }
        assertNotNull(System.getProperty("requestTarget"));
        System.clearProperty("requestTarget");
    }
    
    /**
     * Test getRequestTarget method (looking for query string marker).
     */
    @Test
    void testGetRequestTarget2() {
        System.clearProperty("requestTarget");
        int port = FreePortFinder.findFreeLocalPort();
        HttpServer server = createServer(port,
                (HttpServerRequest request, HttpServerResponse response) -> {
                    System.setProperty("requestTarget", request.getRequestTarget());
                    return COMPLETED;
                });
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                    URI.create("http://localhost:" + port + "?queryParam=queryParam")).build();
            client.send(request, BodyHandlers.discarding());
        } catch (Exception e) {
            // do nothing
        } finally {
            server.stop();
        }
        assertNotNull(System.getProperty("requestTarget"));
        assertTrue(System.getProperty("requestTarget").indexOf("?") > 0);
        System.clearProperty("requestTarget");
    }
}
