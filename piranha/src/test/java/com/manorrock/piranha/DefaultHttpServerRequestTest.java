/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import com.manorrock.piranha.api.HttpServerResponse;
import com.manorrock.piranha.api.HttpServerRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultHttpServerRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServerRequestTest {

    /**
     * Test addHeader method.
     */
    @Test
    public void testAddHeader() {
        DefaultHttpServer server = new DefaultHttpServer(8081, (HttpServerRequest request, HttpServerResponse response) -> {
            Iterator<String> headerNames = request.getHeaderNames();
            OutputStream outputStream = response.getOutputStream();
            try {
                response.setStatus(200);
                response.writeStatusLine();
                response.writeHeaders();
                while (headerNames.hasNext()) {
                    String name = headerNames.next();
                    outputStream.write(name.getBytes());
                    outputStream.write(request.getHeader(name).getBytes());
                }
                DefaultHttpServerResponse defaultResponse = (DefaultHttpServerResponse) response;
            } catch (IOException ioe) {
            }
        });
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8081");
            request.addHeader("name", "value1");
            request.addHeader("name", "value2");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            entity.writeTo(outputStream);
            String body = outputStream.toString("UTF-8");
            assertTrue(body.contains("NAME"));
            assertTrue(body.contains("value1"));
            assertTrue(body.contains("value2"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        server.stop();
    }

    /**
     * Test getLocalAddress method.
     */
    @Test
    public void testGetLocalAddress() {
        DefaultHttpServer server = new DefaultHttpServer(4321, (HttpServerRequest request, HttpServerResponse response) -> {
            String value = request.getLocalAddress();
            OutputStream outputStream = response.getOutputStream();
            try {
                response.setStatus(200);
                response.writeStatusLine();
                response.writeHeaders();
                outputStream.write(value.getBytes());
                DefaultHttpServerResponse defaultResponse = (DefaultHttpServerResponse) response;
            } catch (IOException ioe) {
            }
        });
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:4321");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            entity.writeTo(outputStream);
            String body = outputStream.toString("UTF-8");
            assertTrue(body.contains("127.0.0.1"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        server.stop();
    }

    /**
     * Test getQueryString method.
     */
    @Test
    public void testGetQueryString() {
        DefaultHttpServer server = new DefaultHttpServer(8083, (HttpServerRequest request, HttpServerResponse response) -> {
            String queryString = request.getQueryString();
            OutputStream outputStream = response.getOutputStream();
            try {
                response.setStatus(200);
                response.writeStatusLine();
                response.writeHeaders();
                outputStream.write(queryString.getBytes());
                DefaultHttpServerResponse defaultResponse = (DefaultHttpServerResponse) response;
            } catch (IOException ioe) {
            }
        });
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8083/?name=value");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            entity.writeTo(outputStream);
            String body = outputStream.toString("UTF-8");
            assertTrue(body.contains("name=value"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        server.stop();
    }

    /**
     * Test getQueryParameter method.
     */
    @Test
    public void testGetQueryParameter() {
        DefaultHttpServer server = new DefaultHttpServer(8084, (HttpServerRequest request, HttpServerResponse response) -> {
            String value = request.getQueryParameter("name");
            OutputStream outputStream = response.getOutputStream();
            try {
                response.setStatus(200);
                response.writeStatusLine();
                response.writeHeaders();
                outputStream.write(value.getBytes());
                DefaultHttpServerResponse defaultResponse = (DefaultHttpServerResponse) response;
            } catch (IOException ioe) {
            }
        });
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8084/?name=value");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            entity.writeTo(outputStream);
            String body = outputStream.toString("UTF-8");
            assertTrue(body.contains("value"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        server.stop();
    }

    /**
     * Test getQueryParameter method.
     */
    @Test
    public void testGetQueryParameter2() {
        DefaultHttpServer server = new DefaultHttpServer(8085, (HttpServerRequest request, HttpServerResponse response) -> {
            String value = request.getQueryParameter("name");
            OutputStream outputStream = response.getOutputStream();
            try {
                response.setStatus(200);
                response.writeStatusLine();
                response.writeHeaders();
                outputStream.write(value.getBytes());
                DefaultHttpServerResponse defaultResponse = (DefaultHttpServerResponse) response;
            } catch (IOException ioe) {
            }
        });
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:8085/?name=value&name=value2");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            entity.writeTo(outputStream);
            String body = outputStream.toString("UTF-8");
            assertTrue(body.contains("value"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        server.stop();
    }
}
