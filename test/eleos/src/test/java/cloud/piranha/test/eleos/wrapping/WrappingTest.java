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
package cloud.piranha.test.eleos.wrapping;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.security.elios.AuthenticationInitializer;
import static cloud.piranha.security.elios.AuthenticationInitializer.AUTH_MODULE_CLASS;
import cloud.piranha.security.exousia.AuthorizationInitializer;
import cloud.piranha.security.exousia.AuthorizationPreInitializer;
import static cloud.piranha.security.exousia.AuthorizationPreInitializer.AUTHZ_FACTORY_CLASS;
import static cloud.piranha.security.exousia.AuthorizationPreInitializer.AUTHZ_POLICY_CLASS;
import static cloud.piranha.security.exousia.AuthorizationPreInitializer.CONSTRAINTS;
import cloud.piranha.security.jakarta.JakartaSecurityInitializer;
import static java.util.Arrays.asList;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.omnifaces.exousia.constraints.SecurityConstraint;
import org.omnifaces.exousia.modules.def.DefaultPolicy;
import org.omnifaces.exousia.modules.def.DefaultPolicyConfigurationFactory;

/**
 * This tests that the wrapped request and response a SAM puts into the
 * MessageInfo structure reaches the Servlet that's invoked as well as all
 * filters executed before that.
 *
 * @author Arjan Tijms
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WrappingTest {

    /**
     * Stores our piranha instance.
     */
    private EmbeddedPiranha piranha;

    /**
     * Stores our request.
     */
    private EmbeddedRequest request;

    /**
     * Stores our response.
     */
    private EmbeddedResponse response;

    /**
     * Cleanup after each test.
     * 
     * @throws Exception when a serious error occurs.
     */
    @After
    public void after() throws Exception {
        piranha.stop().destroy();
    }

    /**
     * Setup before each test.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Before
    public void before() throws Exception {
        piranha = new EmbeddedPiranhaBuilder()
                .attribute(AUTHZ_FACTORY_CLASS, DefaultPolicyConfigurationFactory.class)
                .attribute(AUTHZ_POLICY_CLASS, DefaultPolicy.class)
                .attribute(CONSTRAINTS, asList(
                        new SecurityConstraint("/protected/servlet", "architect")))
                .attribute(AUTH_MODULE_CLASS, TestWrappingServerAuthModule.class)
                .initializer(AuthorizationPreInitializer.class.getName())
                .initializer(AuthenticationInitializer.class.getName())
                .initializer(AuthorizationInitializer.class.getName())
                .initializer(JakartaSecurityInitializer.class.getName())
                .filter("DeclaredFilter", DeclaredFilter.class.getName())
                .filterMapping("DeclaredFilter", "/*")
                .servlet("ProtectedServlet", ProtectedServlet.class.getName())
                .servletMapping("ProtectedServlet", "/protected/servlet")
                .buildAndStart();
        request = new EmbeddedRequest();
        response = new EmbeddedResponse();
    }

    /**
     * The SAM wrapped a request so that it always contains the request
     * attribute "isWrapped" with value true.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testDeclaredFilterRequestWrapping() throws Exception {
        request.setServletPath("/protected/servlet");
        piranha.service(request, response);
        assertTrue("Request wrapped by SAM did not arrive in declared Filter.",
                response.getResponseAsString().contains("declared filter request isWrapped: true"));
    }

    /**
     * The SAM wrapped a response so that it always contains the header
     * "isWrapped" with value true.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testDeclaredFilterResponseWrapping() throws Exception {
        request.setServletPath("/protected/servlet");
        piranha.service(request, response);
        assertTrue("Response wrapped by SAM did not arrive in declared Filter.",
                response.getResponseAsString().contains("declared filter response isWrapped: true"));
    }

    /**
     * The SAM wrapped a request so that it always contains the request
     * attribute "isWrapped" with value true.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testRequestWrapping() throws Exception {
        request.setServletPath("/protected/servlet");
        piranha.service(request, response);
        assertTrue("Request wrapped by SAM did not arrive in Servlet.",
                response.getResponseAsString().contains("servlet request isWrapped: true"));
    }

    /**
     * The SAM wrapped a response so that it always contains the header
     * "isWrapped" with value true.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testResponseWrapping() throws Exception {
        request.setServletPath("/protected/servlet");
        piranha.service(request, response);
        assertTrue("Response wrapped by SAM did not arrive in Servlet.",
                response.getResponseAsString().contains("servlet response isWrapped: true"));
    }
}
