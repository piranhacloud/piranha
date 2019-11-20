/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.http.singlethread;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultHttpServer class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SingleThreadHttpServerTest {

    /**
     * Test start and stop method.
     */
    @Test
    public void testStartAndStop() {
        SingleThreadHttpServer server = new SingleThreadHttpServer();
        server.serverPort = 18001;
        server.start();
        assertTrue(server.isRunning());
        server.stop();
        assertFalse(server.isRunning());
    }

    /**
     * Test processing.
     */
    @Test
    public void testProcessing() {
        SingleThreadHttpServer server = new SingleThreadHttpServer();
        server.serverPort = 18002;
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:18002");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        server.stop();
        assertFalse(server.isRunning());
    }

    /**
     * Test file not found.
     */
    @Test
    public void testFileNotFound() {
        SingleThreadHttpServer server = new SingleThreadHttpServer();
        server.serverPort = 18003;
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:18003/this_is_certainly_not_there");
            HttpResponse response = client.execute(request);
            assertEquals(404, response.getStatusLine().getStatusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        server.stop();
        assertFalse(server.isRunning());
    }

    /**
     * Test file.
     */
    @Test
    public void testFile() {
        SingleThreadHttpServer server = new SingleThreadHttpServer();
        server.serverPort = 18004;
        server.start();        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:18004/pom.xml");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        server.stop();
        assertFalse(server.isRunning());
    }
}
