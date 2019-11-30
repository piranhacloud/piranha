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
package cloud.piranha.test.eleos.custom.principal;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import cloud.piranha.test.utils.TestWebApp;

/**
 * This tests that we can login from a protected resource (a resource for which security constraints have been set), then
 * access it and that for this type of page the custom principal correctly arrives in a Servlet.
 * 
 * @author Arjan Tijms
 * 
 */
public class CustomPrincipalProtectedTest  {
    
    TestWebApp webApp;
    
    @Before
    public void testProtected() throws Exception {
        webApp = Application.get();    
    }


    @Test
    public void testProtectedPageLoggedin() throws IOException, SAXException {

        String response = webApp.getFromServerPath("/protected/servlet?doLogin=true");

        // Target resource should be accessible
        assertTrue(
            "Authentication seems to have failed, as the expected response from the requested resource is not correct.",
            response.contains("This is a protected servlet")
        );
        
        // Has to be logged-in with the right principal
        assertTrue(
            "Authentication but username is not the expected one 'test'",
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