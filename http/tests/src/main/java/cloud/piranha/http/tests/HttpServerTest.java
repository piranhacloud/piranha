/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
import cloud.piranha.http.api.HttpServerRequest;
import cloud.piranha.http.api.HttpServerResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * An abstract JUnit test for any HttpServer implementation.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class HttpServerTest {

    /**
     * Create server with a port.
     *
     * @param portNumber the port number.
     * @return the HTTP server.
     */
    protected abstract HttpServer createServer(int portNumber);

    /**
     * Create server with a port and processor.
     *
     * @param portNumber the port number.
     * @param processor the HTTP processor.
     * @return the HTTP server.
     */
    protected abstract HttpServer createServer(int portNumber, HttpServerProcessor processor);

    /**
     * Test addHeader method.
     */
    @Test
    void testAddHeader() {
        HttpServer server = createServer(8765,
                (HttpServerRequest request, HttpServerResponse response) -> {
                    try {
                        response.setStatus(200);
                        response.setHeader("Content-Type", "text/plain");
                        response.setHeader("Keep-Alive", "close");
                        response.writeStatusLine();
                        response.writeHeaders();
                        OutputStream outputStream = response.getOutputStream();
                        Iterator<String> headerNames = request.getHeaderNames();
                        while (headerNames.hasNext()) {
                            String name = headerNames.next();
                            outputStream.write(name.toLowerCase().getBytes());
                            outputStream.write("\n".getBytes());
                            outputStream.write(request.getHeader(name).getBytes());
                            outputStream.write("\n".getBytes());
                        }
                        outputStream.flush();
                    } catch (IOException ioe) {
                    }

                    return false;
                });
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8765")).header("name", "value1").build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            assertEquals(200, response.statusCode());
            String body = response.body();
            assertTrue(body.contains("name"));
            assertTrue(body.contains("value1"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

    /**
     * Test file.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testFile() throws Exception {
        HttpServer server = createServer(8764);
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8764/pom.xml")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            assertEquals(200, response.statusCode());
            String responseText = response.body();
            assertTrue(responseText.contains("modelVersion"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            server.stop();
        }
    }

    /**
     * Test file not found.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testFileNotFound() throws Exception {
        HttpServer server = createServer(8763);
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8763/this_is_certainly_not_there")).build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            assertEquals(404, response.statusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            server.stop();
        }
    }

    /**
     * Test getLocalAddress method.
     */
    @Test
    void testGetLocalAddress() {
        HttpServer server = createServer(8762,
                (HttpServerRequest request, HttpServerResponse response) -> {
                    try {
                        response.setStatus(200);
                        response.setHeader("Content-Type", "text/plain");
                        response.setHeader("Keep-Alive", "close");
                        response.writeStatusLine();
                        response.writeHeaders();
                        String value = request.getLocalAddress();
                        OutputStream outputStream = response.getOutputStream();
                        outputStream.write(value.getBytes());
                        outputStream.flush();
                    } catch (IOException ioe) {
                    }

                    return false;
                });
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8762")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            assertEquals(200, response.statusCode());
            String body = response.body();
            assertTrue(body.contains("127.0.0.1"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

    /**
     * Test getQueryParameter method.
     */
    @Test
    void testGetQueryParameter() {
        HttpServer server = createServer(8761,
                (HttpServerRequest request, HttpServerResponse response) -> {
                    try {
                        response.setStatus(200);
                        response.setHeader("Content-Type", "text/plain");
                        response.setHeader("Keep-Alive", "close");
                        response.writeStatusLine();
                        response.writeHeaders();
                        String value = request.getQueryParameter("name");
                        OutputStream outputStream = response.getOutputStream();
                        outputStream.write(value.getBytes());
                        outputStream.flush();
                    } catch (IOException ioe) {
                    }

                    return false;
                });
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8761/?name=value")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            assertEquals(200, response.statusCode());
            String body = response.body();
            assertTrue(body.contains("value"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

    /**
     * Test getQueryParameter method.
     */
    @Test
    void testGetQueryParameter2() {
        HttpServer server = createServer(8760,
                (HttpServerRequest request, HttpServerResponse response) -> {
                    try {
                        response.setStatus(200);
                        response.setHeader("Content-Type", "text/plain");
                        response.setHeader("Keep-Alive", "close");
                        response.writeStatusLine();
                        response.writeHeaders();
                        String value = request.getQueryParameter("name");
                        OutputStream outputStream = response.getOutputStream();
                        outputStream.write(value.getBytes());
                        outputStream.flush();
                    } catch (IOException ioe) {
                    }

                    return false;
                });
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8760/?name=value&name=value2")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String body = response.body();
            assertTrue(body.contains("value"));
            assertFalse(body.contains("value2"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

    /**
     * Test getQueryString method.
     */
    @Test
    void testGetQueryString() {
        HttpServer server = createServer(8759,
                (HttpServerRequest request, HttpServerResponse response) -> {
                    try {
                        response.setStatus(200);
                        response.setHeader("Content-Type", "text/plain");
                        response.setHeader("Keep-Alive", "close");
                        response.writeStatusLine();
                        response.writeHeaders();
                        String queryString = request.getQueryString();
                        OutputStream outputStream = response.getOutputStream();
                        outputStream.write(queryString.getBytes());
                        outputStream.flush();
                    } catch (IOException ioe) {
                    }

                    return false;
                });
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8759/?name=value")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            assertEquals(200, response.statusCode());
            String body = response.body();
            assertTrue(body.contains("name=value"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

    /**
     * Test processing.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testProcessing() throws Exception {
        HttpServer server = createServer(8758);
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8758")).build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            assertEquals(200, response.statusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            server.stop();
        }
    }

    /**
     * Test processing.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testProcessing2() throws Exception {
        HttpServer server = createServer(8757);
        server.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8757")).build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            assertEquals(200, response.statusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            server.stop();
        }
    }

    /**
     * Test start and stop method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testStartAndStop() throws Exception {
        HttpServer server = createServer(8756);
        server.start();
        assertTrue(server.isRunning());
        server.stop();
        assertFalse(server.isRunning());
    }

    /**
     * Test if the server supports HTTP/1.0.
     * 
     * @throws Exception when an error occurs.
     */
    @Test
    void testRequestHTTP10() throws Exception {
        HttpServer server = createServer(8755, HttpServerTest::returnProtocol);
        server.start();
        try (Socket socket = new Socket("localhost", 8755);
            OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write("GET / HTTP/1.0\r\nHost: localhost:8755\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream response = new ByteArrayOutputStream();
            inputStream.transferTo(response);
            assertTrue(response.toString(StandardCharsets.UTF_8).contains("HTTP/1.0"));
        }

        server.stop();
    }

    /**
     * Test if the server supports HTTP/1.1.
     * 
     * @throws Exception when an error occurs.
     */
    @Test
    void testRequestHTTP11() throws Exception {
        HttpServer server = createServer(8754, HttpServerTest::returnProtocol);
        server.start();

        try (Socket socket = new Socket("localhost", 8754);
             OutputStream outputStream = socket.getOutputStream()) {
            outputStream.write("GET / HTTP/1.1\r\nHost: localhost:8754\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            InputStream inputStream = socket.getInputStream();
            ByteArrayOutputStream response = new ByteArrayOutputStream();
            inputStream.transferTo(response);
            assertTrue(response.toString(StandardCharsets.UTF_8).contains("HTTP/1.1"));
        }

        server.stop();
    }

    /**
     * Return the HTTP protocol.
     * 
     * @param request the request.
     * @param response the response.
     * @return false as this HTTP processor does not support async.
     */
    private static boolean returnProtocol(HttpServerRequest request, HttpServerResponse response) {
        try {
            response.setStatus(200);
            response.setHeader("Content-Type", "text/plain");
            response.setHeader("Keep-Alive", "close");
            response.writeStatusLine();
            response.writeHeaders();
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(request.getProtocol().getBytes());
            outputStream.flush();
        } catch (IOException ioe) {
        }
        return false;
    }
}
