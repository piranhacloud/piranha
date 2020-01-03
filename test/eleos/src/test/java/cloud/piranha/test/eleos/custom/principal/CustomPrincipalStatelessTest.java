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
package cloud.piranha.test.eleos.custom.principal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import cloud.piranha.test.utils.TestWebApp;

/**
 * Idential test as in basic-authentication, but now performed against a SAM which sets a custom principal.
 * Therefore tests that for this kind of usage of the PrincipalCallback JASPIC is stateless just as well.
 * 
 * @author Arjan Tijms
 * 
 */
public class CustomPrincipalStatelessTest {
    
    TestWebApp webApp;
    
    @Before
    public void testProtected() throws Exception {
        webApp = Application.get();    
    }

    /**
     * Tests that access to a protected page does not depend on the authenticated identity that was established in a previous
     * request.
     */
    @Test
    public void testProtectedAccessIsStateless() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // Accessing protected page without login
        String response = webApp.getFromServerPath("protected/servlet");

        // Not logged-in thus should not be accessible.
        assertFalse(response.contains("This is a protected servlet"));

        // -------------------- Request 2 ---------------------------

        // JASPIC is stateless and login (re-authenticate) has to happen for every request
        //
        // If the following fails but "testProtectedPageLoggedin" has succeeded,
        // the container has probably remembered the "unauthenticated identity", e.g. it has remembered that
        // we're not authenticated and it will deny further attempts to authenticate. This may happen when
        // the container does not correctly recognize the JASPIC protocol for "do nothing".

        response = webApp.getFromServerPath("protected/servlet?doLogin=true");

        // Now has to be logged-in so page is accessible
        assertTrue("Could not access protected page, but should be able to. "
            + "Did the container remember the previously set 'unauthenticated identity'?",
            response.contains("This is a protected servlet"));

        // -------------------- Request 3 ---------------------------

        // JASPIC is stateless and login (re-authenticate) has to happen for every request
        //
        // In the following method we do a call without logging in after one where we did login.
        // The container should not remember this login and has to deny access.
        response = webApp.getFromServerPath("protected/servlet");

        // Not logged-in thus should not be accessible.
        assertFalse("Could access protected page, but should not be able to. "
            + "Did the container remember the authenticated identity that was set in previous request?",
            response.contains("This is a protected servlet"));
    }

    /**
     * Tests that access to a protected page does not depend on the authenticated identity that was established in a previous
     * request, but use a different request order than the previous test.
     */
    @Test
    public void testProtectedAccessIsStateless2() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // Start with doing a login
        String response = webApp.getFromServerPath("protected/servlet?doLogin=true");

        // -------------------- Request 2 ---------------------------

        // JASPIC is stateless and login (re-authenticate) has to happen for every request
        //
        // In the following method we do a call without logging in after one where we did login.
        // The container should not remember this login and has to deny access.

        // Accessing protected page without login
        response = webApp.getFromServerPath("protected/servlet");

        // Not logged-in thus should not be accessible.
        assertFalse(
            "Could access protected page, but should not be able to. " +
	        "Did the container remember the authenticated identity that was set in previous request?",
            response.contains("This is a protected servlet")
        );
    }
    
    @Test
    public void testPublicAccessIsStateless() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        String response = webApp.getFromServerPath("public/servlet");

        // Not logged-in
        assertTrue(response.contains("web username: null"));
        assertTrue(response.contains("web user has role \"architect\": false"));

        // -------------------- Request 2 ---------------------------

        response = webApp.getFromServerPath("public/servlet?doLogin=true");

        // Now has to be logged-in
        assertTrue(
            "Username is not the expected one 'test'",
            response.contains("web username: test")
        );
        assertTrue(
            "Username is correct, but the expected role 'architect' is not present.",
            response.contains("web user has role \"architect\": true")
        );

        // -------------------- Request 3 ---------------------------

        response = webApp.getFromServerPath("public/servlet");

        // Not logged-in
        assertTrue(
            "Should not be authenticated, but username was not null. Did the container remember it from previous request?",
            response.contains("web username: null")
        );
        assertTrue(
            "Request was not authenticated (username correctly null), but unauthenticated user incorrectly has role 'architect'",
            response.contains("web user has role \"architect\": false")
        );
    }

    /**
     * Tests independently from being able to access a protected resource if any details of a previously established
     * authenticated identity are remembered
     */
    @Test
    public void testProtectedThenPublicAccessIsStateless() throws IOException, SAXException {

        // -------------------- Request 1 ---------------------------

        // Accessing protected page with login
        String response = webApp.getFromServerPath("protected/servlet?doLogin=true");

        // -------------------- Request 2 ---------------------------

        // Accessing public page without login
        response = webApp.getFromServerPath("public/servlet");

        // No details should linger around
        assertFalse(
            "User principal was 'test', but it should be null here. " + 
            "The container seemed to have remembered it from the previous request.",
            response.contains("web username: test")
        );
        assertTrue(
            "User principal was not null, but it should be null here. ",
            response.contains("web username: null")
        );
        assertTrue(
            "The unauthenticated user has the role 'architect', which should not be the case. " + 
            "The container seemed to have remembered it from the previous request.",
            response.contains("web user has role \"architect\": false")
        );
    }

}