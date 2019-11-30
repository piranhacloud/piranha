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
package cloud.piranha.test.eleos.register.session;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import cloud.piranha.test.utils.TestWebApp;

/**
 * Variant of the {@link RegisterSessionTest}, where a custom principal is used instead
 * of a container provided one. This is particularly challenging since the SAM has to
 * pass the principal obtained from HttpServletRequest into the CallbackHandler, which
 * then somehow has to recognize this as the signal to continue an authenticated session. 
 * 
 * @author Arjan Tijms
 *
 */
public class RegisterSessionCustomPrincipalTest {
    
    TestWebApp webApp;
    
    @Before
    public void testProtected() throws Exception {
        webApp = Application.get();    
    }
    
    protected TestWebApp getWebApp() {
        return webApp;
    }

    @Test
    public void testRemembersSession() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // Accessing protected page without login
        String response = getWebApp().getFromServerPath("protected/servlet");

        // Not logged-in thus should not be accessible.
        assertFalse(response.contains("This is a protected servlet"));

        
        // -------------------- Request 2 ---------------------------

        // We access the protected page again and now login

        response = getWebApp().getFromServerPath("protected/servlet?doLogin=true&customPrincipal=true");

        // Now has to be logged-in so page is accessible
        assertTrue(
            "Could not access protected page, but should be able to. " + 
            "Did the container remember the previously set 'unauthenticated identity'?",
            response.contains("This is a protected servlet")
        );

        // Check principal has right name and right type and roles are available
        checkAuthenticatedIdentity(response);
        
        
        // -------------------- Request 3 ---------------------------

        // JASPIC is normally stateless, but for this test the SAM uses the register session feature so now
        // we should be logged-in when doing a call without explicitly logging in again.

        response = getWebApp().getFromServerPath("protected/servlet?continueSession=true");

        // Logged-in thus should be accessible.
        assertTrue(
            "Could not access protected page, but should be able to. " + 
            "Did the container not remember the authenticated identity via 'javax.servlet.http.registerSession'?",
            response.contains("This is a protected servlet")
        );

        // Both the user name and roles/groups have to be restored

        // *** NOTE ***: The JASPIC 1.1 spec is NOT clear about remembering roles, but spec lead Ron Monzillo clarified that
        // this should indeed be the case. The next JASPIC revision of the spec will have to mention this explicitly.
        // Intuitively it should make sense though that the authenticated identity is fully restored and not partially,
        // but again the spec should make this clear to avoid ambiguity.
    
        checkAuthenticatedIdentity(response);

        // -------------------- Request 4 ---------------------------

        // The session should also be remembered for other resources, including public ones

        response = getWebApp().getFromServerPath("public/servlet?continueSession=true");

        // This test almost can't fail, but include for clarity
        assertTrue(response.contains("This is a public servlet"));

        // When accessing the public page, the username and roles should be restored and be available
        // just as on protected pages
        checkAuthenticatedIdentity(response);
    }

    @Test
    public void testJoinSessionIsOptional() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // We access a protected page and login
        //

        String response = getWebApp().getFromServerPath("protected/servlet?doLogin=true&customPrincipal=true");

		// Now has to be logged-in so page is accessible
		assertTrue(
			"Could not access protected page, but should be able to. " + 
	        "Did the container remember the previously set 'unauthenticated identity'?",
			response.contains("This is a protected servlet")
		);
		
		 // Check principal has right name and right type and roles are available
        checkAuthenticatedIdentity(response);
		
		
		

        // -------------------- Request 2 ---------------------------

        // JASPIC is normally stateless, but for this test the SAM uses the register session feature so now
        // we should be logged-in when doing a call without explicitly logging in again.

        response = getWebApp().getFromServerPath("protected/servlet?continueSession=true");

        // Logged-in thus should be accessible.
        assertTrue(
            "Could not access protected page, but should be able to. " + 
            "Did the container not remember the authenticated identity via 'javax.servlet.http.registerSession'?",
            response.contains("This is a protected servlet")
        );

        // Both the user name and roles/groups have to be restored

        // *** NOTE ***: The JASPIC 1.1 spec is NOT clear about remembering roles, but spec lead Ron Monzillo clarified that
        // this should indeed be the case. The next JASPIC revision of the spec will have to mention this explicitly.
        // Intuitively it should make sense though that the authenticated identity is fully restored and not partially,
        // but again the spec should make this clear to avoid ambiguity.
        // Check principal has right name and right type and roles are available
        checkAuthenticatedIdentity(response);
        

        // -------------------- Request 3 ---------------------------

        // Although the container remembers the authentication session, the SAM needs to OPT-IN to it.
        // If the SAM instead "does nothing", we should not have access to the protected resource anymore
        // even within the same HTTP session.

        response = getWebApp().getFromServerPath("protected/servlet");
        assertFalse(response.contains("This is a protected servlet"));
        

        // -------------------- Request 4 ---------------------------

        // Access to a public page is unaffected by joining or not joining the session, but if we do not join the
        // session we shouldn't see the user's name and roles.

        response = getWebApp().getFromServerPath("public/servlet");
        
        assertTrue(
            "Could not access public page, but should be able to. " +
            "Does the container have an automatic session fixation prevention?",    
            response.contains("This is a public servlet")
        );
        assertFalse(
            "SAM did not join authentication session and should be anonymous, but username is name of session identity.",
            response.contains("web username: test")
        );
        assertFalse(
            "SAM did not join authentication session and should be anonymous without roles, but has role of session identity.",
            response.contains("web user has role \"architect\": true")
        );
    }
    
    private void checkAuthenticatedIdentity( String response) {
        
        // Has to be logged-in with the right principal
        assertTrue(
            "Authenticated but username is not the expected one 'test'",
            response.contains("web username: test")
        );
        assertTrue(
            "Authentication succeeded and username is correct, but the expected role 'architect' is not present.",
            response.contains("web user has role \"architect\": true"));
        
        assertTrue(
            "Authentication succeeded and username and roles are correct, but principal type is not the expected custom type.",
            response.contains("isCustomPrincipal: true")
        );
    }
    
    
    
}