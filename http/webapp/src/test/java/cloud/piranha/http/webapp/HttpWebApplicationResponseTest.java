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
import static jakarta.servlet.DispatcherType.values;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * The JUnit tests for HttpWebApplicationRequest.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpWebApplicationResponseTest {

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
        httpServer.stop();
    }

    /**
     * Before each.
     */
    @BeforeEach
    void beforeEach() {
        server = new HttpWebApplicationServer();
        DefaultWebApplication application = new DefaultWebApplication();
        application.setContextPath("");
        Dynamic registration = application.addServlet("ResponseServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                if (request.getQueryString().equals("addIntHeader")) { 
                    addIntHeader(request, response);
                } else if (request.getQueryString().equals("setCharacterEncoding")) {
                    setCharacterEncoding(request, response);
                }
            }

            private void addIntHeader(HttpServletRequest request, HttpServletResponse response) throws IOException {
                response.addIntHeader("name", 1234);
                response.addIntHeader("name", 2345);
                response.flushBuffer();
            }

            private void setCharacterEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
                response.setContentType("text/html");
                response.setCharacterEncoding("ISO-8859-7");
                response.flushBuffer();
            }
        });
        registration.setAsyncSupported(true);
        application.addServletMapping("ResponseServlet", "/response");
        server.addWebApplication(application);
        server.initialize();
        server.start();
        httpServer = new DefaultHttpServer(-2, server, false);
        httpServer.start();
    }

    /**
     * Test setAddIntHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddIntHeader() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/response?addIntHeader"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("1234,2345", response.headers().firstValue("name").get());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/response?setCharacterEncoding"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("text/html;charset=ISO-8859-7", response.headers().firstValue("Content-Type").get());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
