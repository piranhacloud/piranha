/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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

import cloud.piranha.DefaultHttpServerProcessor;
import cloud.piranha.api.HttpServer;
import cloud.piranha.api.HttpServerProcessor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

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
     * Test start and stop method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testStartAndStop() throws Exception {
        HttpServer server = createServer(28001);
        server.start();
        assertTrue(server.isRunning());
        server.stop();
        assertFalse(server.isRunning());
    }

    /**
     * Test processing.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testProcessing() throws Exception {
        HttpServer server = createServer(28002);
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:28002");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
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
    public void testProcessing2() throws Exception {
        HttpServer server = createServer(28003, new DefaultHttpServerProcessor());
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:28003");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
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
    public void testFileNotFound() throws Exception {
        HttpServer server = createServer(28004);
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:28004/this_is_certainly_not_there");
            HttpResponse response = client.execute(request);
            assertEquals(404, response.getStatusLine().getStatusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            server.stop();
        }
        assertFalse(server.isRunning());
    }

    /**
     * Test file.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testFile() throws Exception {
        HttpServer server = createServer(28005);
        server.start();
        try {
            HttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("http://localhost:28005/pom.xml");
            HttpResponse response = client.execute(request);
            assertEquals(200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            entity.writeTo(byteOutput);
            String responseText = byteOutput.toString("UTF-8");
            assertTrue(responseText.contains("modelVersion"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } finally {
            server.stop();
        }
        assertFalse(server.isRunning());
    }
}
