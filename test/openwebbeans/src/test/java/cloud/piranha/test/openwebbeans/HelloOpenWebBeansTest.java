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
package cloud.piranha.test.openwebbeans;

import cloud.piranha.cdi.openwebbeans.OpenWebBeansInitializer;
import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.faces.myfaces.MyFacesInitializer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the Hello OpenWebBeans web application.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloOpenWebBeansTest {

    /**
     * Test /index.html.
     *
     * @throws Exception
     */
    @Test
    public void testIndexHtml() throws Exception {
        System.getProperties().put("java.naming.factory.initial",
                "cloud.piranha.jndi.memory.DefaultInitialContextFactory");
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .directoryResource("src/main/webapp")
                .aliasedDirectoryResource("target/classes", "/WEB-INF/classes")
                .initializer(MyFacesInitializer.class.getName())
                .initializer(OpenWebBeansInitializer.class.getName())
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .contextPath("")
                .servletPath("/index.html")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(response.getResponseAsString().contains("Hello OpenWebBeans"));
        piranha.stop()
                .destroy();
    }
}
