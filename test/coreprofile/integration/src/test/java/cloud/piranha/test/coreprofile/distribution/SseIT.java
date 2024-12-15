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
package cloud.piranha.test.coreprofile.distribution;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Integration tests to test SSE integration.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SseIT extends ITBase {

    /**
     * Test string based SSE.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSseString() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI(baseUrl + "/sse/string"))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body());
        assertTrue(response.body().contains("data: Event 4"));
    }

    /**
     * Test SSE broadcast.
     *
     * @throw Exception when a serious error occurs.
     */
    @Disabled
    @Test
    void testSseBroadcast() throws Exception {

        List<String> messages = new ArrayList<>();
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(120))
                .build();

        /*
         * Register client and collect events.
         */
        CompletableFuture<Void> future = client.sendAsync(HttpRequest.newBuilder()
                .uri(new URI(baseUrl + "/sse/register"))
                .build(), BodyHandlers.ofLines())
                .thenAccept(response -> response.body().forEach(message -> {
            messages.add(message);
            System.out.println("Received message: " + message);
        }));

        /*
         * Simulate server broadcast.
         */
        client.send(HttpRequest.newBuilder()
                .uri(new URI(baseUrl + "/sse/broadcast"))
                .POST(HttpRequest.BodyPublishers.ofString("Broadcast message"))
                .build(), HttpResponse.BodyHandlers.ofString());

        /*
         * Wait for the future to complete.
         */
        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            fail("Test timed out");
        }

        /*
         * Check if we have received 10 events.
         */
        assertEquals(10, messages.size(), "Should have received 10 events");
        messages.forEach(System.out::println);
    }
}
