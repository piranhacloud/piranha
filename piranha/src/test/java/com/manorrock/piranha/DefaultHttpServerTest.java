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

//import com.manorrock.httpclient.DefaultHttpClientRequest;
//import com.manorrock.httpclient.HttpClientResponse;
import com.manorrock.piranha.DefaultHttpServer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultHttpServer class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServerTest {

    /**
     * Test start and stop method.
     */
    @Test
    public void testStartAndStop() {
        DefaultHttpServer server = new DefaultHttpServer();
        server.serverPort = 8001;
        server.start();
        assertTrue(server.isRunning());
        server.stop();
        assertFalse(server.isRunning());
    }

    /**
     * Test thread factory.
     */
    @Test
    @Ignore
    public void testThreadFactory() {
        DefaultHttpServer server = new DefaultHttpServer();
        server.serverPort = 8002;
        server.start();
//        HttpClientResponse response = new DefaultHttpClientRequest().
//                url("http://localhost:8002").
//                method("GET").
//                response();
//        assertEquals(200, response.getStatus());
        server.stop();
        assertFalse(server.isRunning());
    }

    /**
     * Test file not found.
     */
    @Test
    @Ignore
    public void testFileNotFound() {
        DefaultHttpServer server = new DefaultHttpServer();
        server.serverPort = 8003;
        server.start();
//        HttpClientResponse response = new DefaultHttpClientRequest().
//                url("http://localhost:8003/this_is_certainly_not_there").
//                method("GET").
//                response();
//        assertEquals(404, response.getStatus());
        server.stop();
        assertFalse(server.isRunning());
    }

    /**
     * Test file.
     */
    @Test
    @Ignore
    public void testFile() {
        DefaultHttpServer server = new DefaultHttpServer();
        server.serverPort = 8004;
        server.start();
//        HttpClientResponse response = new DefaultHttpClientRequest().
//                url("http://localhost:8004/pom.xml").
//                method("GET").
//                response();
//        assertEquals(200, response.getStatus());
        server.stop();
        assertFalse(server.isRunning());
    }
}
