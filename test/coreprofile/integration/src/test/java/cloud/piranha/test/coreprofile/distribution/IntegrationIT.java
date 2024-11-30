/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.test.coreprofile.distribution;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * The Piranha Core Profile distribution integration tests.
 *
 * <ol>
 *   <li>testDependencyInjection validates Jakarta Dependency Injection works</li>
 *   <li>testInterceptor validates Jakarta Interceptors works</li>
 *   <li>testJsonBinding validates Jakarta JSON binding works</li>
 *   <li>testJsonProcessing validates Jakarta JSON processing works</li>
 *   <li>testREST validates Jakarta REST works</li>
 * </ol>
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
@ExtendWith(ArquillianExtension.class)
class IntegrationIT {
 
    @ArquillianResource
    private URL baseUrl;
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addClass(DependencyInjectionBean.class)
                .addClass(IntegrationApplication.class)
                .addClass(RestBean.class)
                .addClass(InterceptedBean.class)
                .addClass(InterceptInterceptor.class)
                .addClass(Jsonb.class)
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"));
    }    
 
    /**
     * Test interceptors.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @RunAsClient
    void testInterceptor() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI(baseUrl + "/intercept"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("Interceptor works!"));
    }
 
    /**
     * Test JSON binding.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @RunAsClient
    void testJsonBinding() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI(baseUrl + "jsonb"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("{\"string\":\"JSON Binding works!\"}"));
    }
 
    /**
     * Test JSON Processing.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @RunAsClient
    void testJsonProcessing() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI(baseUrl + "/jsonp"))
                .POST(HttpRequest.BodyPublishers.ofString("\"JSON Processing works!\""))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("{\"string\":\"JSON Processing works!\"}"));
    }
 
    /**
     * Test REST.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testREST() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(new URI(baseUrl + "/rest"))
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertTrue(response.body().contains("REST works!"));
    }
}
