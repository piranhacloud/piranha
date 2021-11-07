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
package cloud.piranha.extension.security.file.tests;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.extension.security.file.FileSecurityInitializer;
import cloud.piranha.extension.webxml.WebXmlInitializer;
import cloud.piranha.resource.impl.DirectoryResource;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for BASIC authentication.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class BasicTest {

    /**
     * Test getUserPrincipal method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetUserPrincipal() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource("src/test/webapp/basic_user_principal"));
        EmbeddedRequest request = new EmbeddedRequest();
        request.setAuthType("BASIC");
        request.setServletPath("/basic_user_principal/prefix.html");
        request.setWebApplication(webApplication);
        EmbeddedResponse response = new EmbeddedResponse();
        response.setBodyOnly(true);
        response.setWebApplication(webApplication);
        webApplication.addInitializer(FileSecurityInitializer.class.getName());
        webApplication.addInitializer(WebXmlInitializer.class.getName());
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertEquals("Basic realm=\"My Realm\"", response.getHeader("WWW-Authenticate"));
        request = new EmbeddedRequest();
        request.setAuthType("BASIC");
        request.setServletPath("/basic_user_principal/prefix.html");
        request.setHeader("Authorization", 
                "Basic " + Base64.getEncoder().encodeToString("j2ee:j2ee".getBytes()));
        request.setWebApplication(webApplication);
        response = new EmbeddedResponse();
        response.setBodyOnly(true);
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        webApplication.stop();
        assertNotEquals("The user principal is: j2ee", response.getResponseAsString());
    }
}
