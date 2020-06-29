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
package cloud.piranha.micro;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The JUnit tests for the MicroPiranha class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class MicroPiranhaTest {

    /**
     * Test start method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    @Ignore
    public void testStart() throws Exception {
        System.setProperty("java.naming.factory.initial", "cloud.piranha.jndi.memory.DefaultInitialContextFactory");
        final MicroPiranha piranha = new MicroPiranha();
        piranha.configure(new String[]{});
        Thread thread = new Thread(piranha);
        thread.start();
        Thread.sleep(3000);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8080/does-not-exist")).build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            assertEquals(404, response.statusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        piranha.stop();
        Thread.sleep(3000);
    }

    /**
     * Test changing port.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    @Ignore
    public void testChangingPort() throws Exception {
        System.setProperty("java.naming.factory.initial", "cloud.piranha.jndi.memory.DefaultInitialContextFactory");
        final MicroPiranha piranha = new MicroPiranha();
        piranha.configure(new String[]{"--port", "8088"});
        Thread thread = new Thread(piranha);
        thread.start();
        Thread.sleep(3000);
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8088/does-not-exist")).build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            assertEquals(404, response.statusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        piranha.stop();
        Thread.sleep(3000);
    }
}
