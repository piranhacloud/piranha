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
package com.manorrock.piranha.test.jakartafaces;

import static com.manorrock.piranha.builder.WebApplicationBuilder.newWebApplication;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.manorrock.piranha.DefaultDirectoryResource;
import com.manorrock.piranha.api.WebApplication;
import com.manorrock.piranha.jakartafaces.JakartaFacesInitializer;
import com.manorrock.piranha.test.utils.TestHttpServletRequest;
import com.manorrock.piranha.test.utils.TestHttpServletResponse;

/**
 * The JUnit tests for the Hello Jakarta Faces web application.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms (arjan.tijms@gmail.com)
 */
public class HelloJakartaFacesTest {

    /**
     * Test /faces/notfound.html.
     *
     * @throws Exception
     */
    @Test
    public void testNotFound() throws Exception {
        WebApplication webApp = newWebApplication()
                .addResource(new DefaultDirectoryResource("src/main/webapp"))
                .addInitializer(JakartaFacesInitializer.class)
                .start();

        TestHttpServletRequest request = new TestHttpServletRequest(
                webApp, "", "/faces", "/notfound.html");
        TestHttpServletResponse response = new TestHttpServletResponse();
        webApp.service(request, response);
        assertEquals(404, response.getStatus());
    }

    /**
     * Test /index.html.
     *
     * @throws Exception
     */
    @Test
    public void testIndexHtml() throws Exception {
        WebApplication webApp = newWebApplication()
                .addResource(new DefaultDirectoryResource("src/main/webapp"))
                .addInitializer(JakartaFacesInitializer.class)
                .start();

        TestHttpServletRequest request = new TestHttpServletRequest(webApp, "", "/index.html");
        TestHttpServletResponse response = new TestHttpServletResponse();
        webApp.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(response.getResponseBodyAsString().contains("Hello Jakarta Faces"));
    }
}
