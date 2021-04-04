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
package cloud.piranha.test.faces.mojarra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.faces.mojarra.MojarraInitializer;

/**
 * The JUnit tests for the Hello Jakarta Faces web application.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms (arjan.tijms@gmail.com)
 */
class MojarraTest {
    
    String facesConfig = """
            <?xml version='1.0' encoding='UTF-8'?>

            <faces-config version="2.3"
               xmlns="http://java.sun.com/xml/ns/javaee"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2_3.xsd">
            </faces-config>
            """;
    
    String indexXhtml = """
            <!DOCTYPE html>

            <html lang="en" xmlns:h="http://java.sun.com/jsf/html">
                <h:head>
                    <title>Hello Jakarta Faces</title>
                </h:head>    
                <h:body>
                    Hello Jakarta Faces
                </h:body>
            </html>
            """;

    /**
     * Test /faces/notfound.html.
     *
     * @throws Exception
     */
    @Test
    void testNotFound() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .directoryResource("src/main/webapp")
                .initializer(MojarraInitializer.class.getName())
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .contextPath("")
                .servletPath("/faces")
                .pathInfo("/notfound.html")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        assertEquals(404, response.getStatus());
        piranha.stop()
                .destroy();
    }

    /**
     * Test /index.html.
     *
     * @throws Exception
     */
    @Test
    void testIndexHtml() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .directoryResource("src/main/webapp")
                .initializer(MojarraInitializer.class.getName())
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .contextPath("")
                .servletPath("/index.html")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(response.getResponseAsString().contains("Hello Jakarta Faces"));
        piranha.stop()
                .destroy();
    }
    
    /**
     * Test /index.html.
     *
     * @throws Exception
     */
    @Test
    void testIndexHtml1() throws Exception {
        EmbeddedResponse response  = new EmbeddedPiranhaBuilder()
                .stringResource("/index.xhtml", indexXhtml)
                .stringResource("/WEB-INF/faces-config.xml", facesConfig)
                .initializer(MojarraInitializer.class.getName())
                .buildAndStart()
                .service("/index.xhtml");
        
        assertEquals(200, response.getStatus());
        assertTrue(response.getResponseAsString().contains("Hello Jakarta Faces"));
    }
}
