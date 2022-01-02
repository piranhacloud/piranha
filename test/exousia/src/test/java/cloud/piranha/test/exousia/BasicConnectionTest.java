/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.test.exousia;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.extension.exousia.AuthorizationPreInitializer;
import cloud.piranha.extension.standard.webxml.StandardWebXmlInitializer;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.AUTHZ_FACTORY_CLASS;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.AUTHZ_POLICY_CLASS;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.UNCHECKED_PERMISSIONS;
import cloud.piranha.extension.security.servlet.ServletSecurityManagerInitializer;
import static java.util.Arrays.asList;
import jakarta.security.jacc.WebUserDataPermission;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.glassfish.exousia.modules.def.DefaultPolicy;
import org.glassfish.exousia.modules.def.DefaultPolicyConfigurationFactory;

/**
 * The JUnit tests for the basic connection test
 *
 * @author Arjan Tijms (arjan.tijms@gmail.com)
 */
class BasicConnectionTest {

    /**
     * Test basic connection permission using a non-secure (http) connection.
     *
     * @throws Exception
     */
    @Test
    void testNonSecureConnection() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .initializer(ServletSecurityManagerInitializer.class.getName())
                .initializer(StandardWebXmlInitializer.class.getName())
                .attribute(AUTHZ_FACTORY_CLASS, DefaultPolicyConfigurationFactory.class)
                .attribute(AUTHZ_POLICY_CLASS, DefaultPolicy.class)
                .attribute(UNCHECKED_PERMISSIONS, asList(
                    new WebUserDataPermission("/*", "!GET"),
                    new WebUserDataPermission("/*", "GET:CONFIDENTIAL")))
                .initializer(AuthorizationPreInitializer.class.getName())
                .servlet("PublicServlet", PublicServlet.class.getName())
                .servletMapping("PublicServlet", "/public/servlet")
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .contextPath("")
                .servletPath("/public/servlet")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        assertFalse(response.getResponseAsString().contains("Hello, from Servlet!"));
        piranha.stop().destroy();
    }
    
    /**
     * Test basic connection permission using a secure (https) connection.
     *
     * @throws Exception
     */
    @Test
    void testSecureConnection() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .initializer(ServletSecurityManagerInitializer.class.getName())
                .initializer(StandardWebXmlInitializer.class.getName())
                .attribute(AUTHZ_FACTORY_CLASS, DefaultPolicyConfigurationFactory.class)
                .attribute(AUTHZ_POLICY_CLASS, DefaultPolicy.class)
                .attribute(UNCHECKED_PERMISSIONS, asList(
                    new WebUserDataPermission("/*", "!GET"),
                    new WebUserDataPermission("/*", "GET:CONFIDENTIAL")))
                .initializer(AuthorizationPreInitializer.class.getName())
                .servlet("PublicServlet", PublicServlet.class.getName())
                .servletMapping("PublicServlet", "/public/servlet")
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .contextPath("")
                .servletPath("/public/servlet")
                .scheme("https")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(response.getResponseAsString().contains("Hello, from Servlet!"));
        piranha.stop().destroy();
    }
    
    @Test
    void testSecureConnectionExactMapping() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .initializer(ServletSecurityManagerInitializer.class.getName())
                .initializer(StandardWebXmlInitializer.class.getName())
                .attribute(AUTHZ_FACTORY_CLASS, DefaultPolicyConfigurationFactory.class)
                .attribute(AUTHZ_POLICY_CLASS, DefaultPolicy.class)
                .attribute(UNCHECKED_PERMISSIONS, asList(
                    new WebUserDataPermission("/public/servlet", "!GET"),
                    new WebUserDataPermission("/public/servlet", "GET:CONFIDENTIAL")))
                .initializer(AuthorizationPreInitializer.class.getName())
                .servlet("PublicServlet", PublicServlet.class.getName())
                .servletMapping("PublicServlet", "/public/servlet")
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .contextPath("")
                .servletPath("/public/servlet")
                .scheme("https")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(response.getResponseAsString().contains("Hello, from Servlet!"));
        piranha.stop().destroy();
    }
}
