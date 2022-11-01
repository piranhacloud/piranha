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
package cloud.piranha.http.webapp;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.impl.DefaultHttpServer;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for HttpWebApplicationRequest.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpWebApplicationRequestTest {

    /**
     * Stores the HTTP server.
     */
    private HttpServer httpServer;
    
    /**
     * Stores the HTTP web application server.
     */
    private HttpWebApplicationServer server;
    
    /**
     * After each.
     */
    @AfterEach
    void afterEach() {
        server.stop();
    }
    
    /**
     * Before each.
     */
    @BeforeEach
    void beforeEach() {
        server = new HttpWebApplicationServer();
        DefaultWebApplication application = new DefaultWebApplication();
        application.setContextPath("");
        application.addServlet("snoop", new TestSnoopServlet());
        application.addServletMapping("snoop", "Snoop");
        server.addWebApplication(application);
        server.initialize();
        server.start();
        httpServer = new DefaultHttpServer(-2, server, false);
        httpServer.start();
    }
        
    /**
     * Test getContentType method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContentType() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getContentType"))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("application/json"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getDateHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetDateHeader() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getDateHeader"))
                    .header("MY_DATE", "Wed, 16 Oct 2019 07:28:00 GMT")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("1571210880000"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getIntHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetIntHeader() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getIntHeader"))
                    .header("MY_INT", "1234")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("1234"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getLocalAddr method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetLocalAddr() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getLocalAddr"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("127.0.0.1"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test isSecure method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsSecure() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?isSecure"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("false"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
