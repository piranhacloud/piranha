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
package cloud.piranha.test.soteria.basic;

import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.AUTHZ_FACTORY_CLASS;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.AUTHZ_POLICY_CLASS;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.CONSTRAINTS;
import static java.util.Arrays.asList;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;

import org.junit.jupiter.api.Test;
import org.glassfish.exousia.constraints.SecurityConstraint;
import org.glassfish.exousia.modules.def.DefaultPolicy;
import org.glassfish.exousia.modules.def.DefaultPolicyConfigurationFactory;

import cloud.piranha.extension.eleos.AuthenticationInitializer;
import cloud.piranha.extension.weld.WeldInitializer;
import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.extension.webxml.WebXmlInitializer;
import cloud.piranha.extension.exousia.AuthorizationInitializer;
import cloud.piranha.extension.exousia.AuthorizationPreInitializer;
import cloud.piranha.extension.herring.HerringExtension;
import cloud.piranha.extension.security.servlet.ServletSecurityManagerExtension;
import cloud.piranha.extension.soteria.SoteriaInitializer;

@BasicAuthenticationMechanismDefinition(realmName = "test")
class BasicTest {

    @Test
    void testAuthenticated() throws Exception {
        System.getProperties().put(INITIAL_CONTEXT_FACTORY, DynamicInitialContextFactory.class.getName());

        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .extension(ServletSecurityManagerExtension.class)
                .extension(HerringExtension.class)
                .initializer(WebXmlInitializer.class.getName())
                .attribute(AUTHZ_FACTORY_CLASS, DefaultPolicyConfigurationFactory.class)
                .attribute(AUTHZ_POLICY_CLASS, DefaultPolicy.class)
                .attribute(CONSTRAINTS, asList(
                        new SecurityConstraint("/protected/servlet", "architect")))
                .initializer(WeldInitializer.class.getName())
                .initializer(AuthorizationPreInitializer.class.getName())
                .initializer(AuthenticationInitializer.class.getName())
                .initializer(AuthorizationInitializer.class.getName())
                .initializer(SoteriaInitializer.class.getName())
                .servlet("PublicServlet", PublicServlet.class.getName())
                .servletMapping("PublicServlet", "/public/servlet")
                .servlet("ProtectedServlet", ProtectedServlet.class.getName())
                .servletMapping("ProtectedServlet", "/protected/servlet")
                .buildAndStart();

        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/protected/servlet")
                .header("Authorization", "Basic " + Base64.getEncoder().
                        encodeToString("test:test".getBytes()))
                .parameter("doLogin", "true")
                .build();

        EmbeddedResponse response = new EmbeddedResponse();

        piranha.service(request, response);

        // Now has to be logged-in so page is accessible
        assertTrue(
                response.getResponseAsString().contains("This is a protected servlet"),
                "Should have been authenticated, but could not access protected resource"
        );

        // Not only does the page needs to be accessible, the caller should have
        // the correct
        // name and roles as well
        // Being able to access a page protected by a role but then seeing the un-authenticated
        // (anonymous) user would normally be impossible, but could happen if the authorization
        // system checks roles on the authenticated subject, but does not correctly expose
        // or propagate these to the HttpServletRequest
        assertFalse(
                response.getResponseAsString().contains("web username: null"),
                "Protected resource could be accessed, but the user appears to be the unauthenticated user. "
                        + "This should not be possible"
        );

        // An authenticated user should have the exact name "test" and nothing else.
        assertTrue(
                response.getResponseAsString().contains("web username: test"),
                "Protected resource could be accessed, but the username is not correct."
        );

        // Being able to access a page protected by role "architect" but failing
        // the test for this role would normally be impossible, but could happen if the
        // authorization system checks roles on the authenticated subject, but does not
        // correctly expose or propagate these to the HttpServletRequest
        assertTrue(
                response.getResponseAsString().contains("web user has role \"architect\": true"),
                "Resource protected by role \"architect\" could be accessed, but user fails test for this role."
                        + "This should not be possible"
        );
        piranha.stop().destroy();
    }
}
