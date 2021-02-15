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
package cloud.piranha.test.unit.http.webapp;

import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.http.impl.DefaultHttpServer;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.webapp.HttpWebApplicationServer;
import cloud.piranha.http.webapp.HttpWebApplicationServerRequestMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the HttpWebApplicationServer class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpWebApplicationServerTest {

    /**
     * Test addMapping method.
     */
    @Test
    void testAddMapping() {
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setServletContextName("mycontext");
        server.addWebApplication(webApp);
        server.addMapping("notthere", "notreal");
    }

    /**
     * Test addMapping method.
     */
    @Test
    void testAddMapping2() {
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setServletContextName("mycontext");
        server.addWebApplication(webApp);
        server.addMapping("mycontext", "mycontextmapping");
    }

    /**
     * Test addMapping method.
     */
    @Test
    void testAddMapping3() {
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setServletContextName("mycontext");
        server.addWebApplication(webApp);
        server.addMapping("mycontext", "myurlpattern");
        server.addMapping("mycontext", "myurlpattern");
    }

    /**
     * Test getRequestMapper method.
     */
    @Test
    void testGetRequestMapper() {
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        server.setRequestMapper(new HttpWebApplicationServerRequestMapper());
        assertNotNull(server.getRequestMapper());
    }
    
    /**
     * Test process method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testProcess() throws Exception {
        HttpWebApplicationServer server = new HttpWebApplicationServer();
        HttpServer httpServer = new DefaultHttpServer(8180, server, false);
        DefaultWebApplication application = new DefaultWebApplication();
        application.setContextPath("/context");
        application.addServlet("snoop", new TestSnoopServlet());
        application.addServletMapping("snoop", "/snoop/*");
        server.addWebApplication(application);
        server.initialize();
        server.start();
        httpServer.start();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(new URI("http://localhost:8180/context/snoop/index.html")).build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            assertEquals(200, response.statusCode());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        httpServer.stop();
        server.stop();
    }
}
