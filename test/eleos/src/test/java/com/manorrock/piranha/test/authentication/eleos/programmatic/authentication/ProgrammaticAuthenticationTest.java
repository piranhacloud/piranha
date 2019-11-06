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
package com.manorrock.piranha.test.authentication.eleos.programmatic.authentication;

import static com.manorrock.piranha.authentication.elios.AuthenticationInitializer.AUTH_MODULE_CLASS;
import static com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.AUTHZ_FACTORY_CLASS;
import static com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.AUTHZ_POLICY_CLASS;
import static com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer.UNCHECKED_PERMISSIONS;
import static com.manorrock.piranha.builder.WebApplicationBuilder.newWebApplication;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.security.jacc.WebUserDataPermission;

import org.junit.Before;
import org.junit.Test;
import org.omnifaces.exousia.modules.def.DefaultPolicy;
import org.omnifaces.exousia.modules.def.DefaultPolicyConfigurationFactory;
import org.xml.sax.SAXException;

import com.manorrock.piranha.authentication.elios.AuthenticationInitializer;
import com.manorrock.piranha.authorization.exousia.AuthorizationPreInitializer;
import com.manorrock.piranha.security.jakarta.JakartaSecurityInitializer;
import com.manorrock.piranha.test.utils.TestWebApp;

/**
 * This tests that a call from a Servlet to HttpServletRequest#authenticate can result
 * in a successful authentication.
 * 
 * @author Arjan Tijms
 * 
 */
public class ProgrammaticAuthenticationTest {
    
    TestWebApp webApp;
    
    @Before
    public void testPublic() throws Exception {
        webApp = 
            new TestWebApp(newWebApplication()  
                .addAttribute(AUTHZ_FACTORY_CLASS, DefaultPolicyConfigurationFactory.class)
                .addAttribute(AUTHZ_POLICY_CLASS, DefaultPolicy.class)
                .addAttribute(UNCHECKED_PERMISSIONS, asList(
                    new WebUserDataPermission("/*", null)))
                .addInitializer(AuthorizationPreInitializer.class)
                    
                .addAttribute(AUTH_MODULE_CLASS, TestServerAuthModule.class)
                .addInitializer(AuthenticationInitializer.class)
                
                .addInitializer(JakartaSecurityInitializer.class)
                .addServlet(AuthenticateServlet.class, "/public/authenticate")
                .start());
    }

    @Test
    public void testAuthenticate() throws IOException, SAXException {
        assertAuthenticated(webApp.getFromServerPath("/public/authenticate"));
    }
    
    @Test
    public void testAuthenticateFailFirstOnce() throws IOException, SAXException {
        // Before authenticating successfully, call request.authenticate which
        // is known to fail (do nothing)
        assertAuthenticated(webApp.getFromServerPath("/public/authenticate?failFirst=1"));
    }
    
    @Test
    public void testAuthenticateFailFirstTwice() throws IOException, SAXException {
        // Before authenticating successfully, call request.authenticate twice which
        // are both known to fail (do nothing)
        assertAuthenticated(webApp.getFromServerPath("/public/authenticate?failFirst=2"));
    }
    
    private void assertAuthenticated(String response) {
        
        // Should not be authenticated in the "before" case, which is 
        // before request.authentiate is called
        assertTrue(
            "Should not be authenticated yet, but a username other than null was encountered. " +
            "This is not correct.",
            response.contains("before web username: null")
        );
        assertTrue(
            "Should not be authenticated yet, but the user seems to have the role \"architect\". " +
            "This is not correct.",
            response.contains("before web user has role \"architect\": false")
        );
        
        // The main request.authenticate causes the SAM to be called which always authenticates
        assertTrue(
                "Calling request.authenticate should have returned true, but did not.",
                response.contains("request.authenticate outcome: true")
            );
        
        // Should be authenticated in the "after" case, which is 
        // after request.authentiate is called
        assertTrue(
            "User should have been authenticated and given name \"test\", " + 
            " but does not appear to have this name",
            response.contains("after web username: test")
        );
        assertTrue(
            "User should have been authenticated and given role \"architect\", " + 
            " but does not appear to have this role",
            response.contains("after web user has role \"architect\": true")
        );
    }
    
    

}