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
package cloud.piranha.http.webapp.tests;

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.impl.DefaultHttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the HttpWebApplicationRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpWebApplicationRequestTest {
    
    /**
     * Test getContextPath method.
     */
    @Test
    public void testGetContextPath() {
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        HttpServer httpServer = new DefaultHttpServer(4000, server, false);
        DefaultWebApplication application = new DefaultWebApplication();
        application.setContextPath("/test");
        application.addServlet("test", new TestGetContextPathServlet());
        application.addServletMapping("test", "/TestServlet");
        server.addWebApplication(application);
        server.initialize();
        server.start();
        httpServer.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:4000/test/TestServlet")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("Context Path: /test"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        httpServer.stop();
        server.stop();
    }
    
    /**
     * Test getQueryString method.
     */
    @Test
    public void testGetQueryString() {
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        HttpServer httpServer = new DefaultHttpServer(4001, server, false);
        DefaultWebApplication application = new DefaultWebApplication();
        application.setContextPath("/test");
        application.addServlet("test", new TestGetQueryStringServlet());
        application.addServletMapping("test", "/TestServlet");
        server.addWebApplication(application);
        server.initialize();
        server.start();
        httpServer.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:4001/test/TestServlet?test=getQueryString")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("Query String: test=getQueryString"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        httpServer.stop();
        server.stop();
    }
    
    /**
     * Test getQueryString method.
     */
    @Test
    public void testGetQueryString2() {
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        HttpServer httpServer = new DefaultHttpServer(4002, server, false);
        DefaultWebApplication application = new DefaultWebApplication();
        application.setContextPath("/test");
        application.addServlet("test", new TestGetQueryStringServlet());
        application.addServletMapping("test", "/TestServlet");
        server.addWebApplication(application);
        server.initialize();
        server.start();
        httpServer.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:4002/test/TestServlet")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("Query String: null"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        httpServer.stop();
        server.stop();
    }

    /**
     * Test getRequestURI method.
     */
    @Test
    public void testGetRequestURI() {
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        HttpServer httpServer = new DefaultHttpServer(4003, server, false);
        DefaultWebApplication application = new DefaultWebApplication();
        application.setContextPath("/test");
        application.addServlet("test", new TestGetRequestURIServlet());
        application.addServletMapping("test", "/TestServlet");
        server.addWebApplication(application);
        server.initialize();
        server.start();
        httpServer.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:4003/test/TestServlet")).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("Request URI: /test/TestServlet"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        httpServer.stop();
        server.stop();
    }
}
