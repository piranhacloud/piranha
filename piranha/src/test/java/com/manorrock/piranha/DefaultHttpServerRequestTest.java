/*
 *  Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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

//import com.manorrock.httpclient.DefaultHttpClientRequest;
//import com.manorrock.httpclient.HttpClientResponse;
import com.manorrock.piranha.DefaultHttpServerResponse;
import com.manorrock.piranha.DefaultHttpServer;
import com.manorrock.piranha.HttpServerRequest;
import com.manorrock.piranha.HttpServerResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import org.junit.Ignore;
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
    @Ignore
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
//        HttpClientResponse response = new DefaultHttpClientRequest().
//                url("http://localhost:8081").
//                header("name", "value1").
//                header("name", "value2").
//                method("GET").
//                response();
//        assertEquals(200, response.getStatus());
//        assertTrue(response.getBody().toString().contains("NAME"));
//        assertTrue(response.getBody().toString().contains("value1"));
//        assertTrue(response.getBody().toString().contains("value2"));
        server.stop();
    }

    /**
     * Test getLocalAddress method.
     */
    @Test
    @Ignore
    public void testGetLocalAddress() {
        DefaultHttpServer server = new DefaultHttpServer(8082, (HttpServerRequest request, HttpServerResponse response) -> {
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
//        HttpClientResponse response = new DefaultHttpClientRequest().
//                url("http://localhost:8082").
//                method("GET").
//                response();
//        assertEquals(200, response.getStatus());
//        assertTrue(response.getBody().toString().contains("127.0.0.1"));
        server.stop();
    }
    
    /**
     * Test getQueryString method.
     */
    @Test
    @Ignore
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
//        HttpClientResponse response = new DefaultHttpClientRequest().
//                url("http://localhost:8083/?name=value").
//                method("GET").
//                response();
//        assertEquals(200, response.getStatus());
//        assertTrue(response.getBody().toString().contains("name=value"));
        server.stop();
    }

    /**
     * Test getQueryParameter method.
     */
    @Test
    @Ignore
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
//        HttpClientResponse response = new DefaultHttpClientRequest().
//                url("http://localhost:8084/?name=value").
//                method("GET").
//                response();
//        assertEquals(200, response.getStatus());
//        assertTrue(response.getBody().toString().contains("value"));
        server.stop();
    }

    /**
     * Test getQueryParameter method.
     */
    @Test
    @Ignore
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
//        HttpClientResponse response = new DefaultHttpClientRequest().
//                url("http://localhost:8085/?name=value&name=value2").
//                method("GET").
//                response();
//        assertEquals(200, response.getStatus());
//        assertTrue(response.getBody().toString().contains("value"));
        server.stop();
    }
}
