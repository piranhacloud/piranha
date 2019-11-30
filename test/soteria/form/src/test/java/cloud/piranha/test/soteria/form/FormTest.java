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
package cloud.piranha.test.soteria.form;


import cloud.piranha.DefaultWebApplicationResponse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import cloud.piranha.test.utils.TestWebApp;
import org.junit.Ignore;

/**
 * 
 * @author Arjan Tijms
 */
public class FormTest {
    
    TestWebApp webApp;
    

    @Before
    public void testProtected() throws Exception {
        webApp = Application.get();    
    }
    
    protected TestWebApp getWebApp() {
        return webApp;
    }
    
    @Test
    public void testAuthenticated() throws IOException {
        
        String loginPage = getWebApp().getFromServerPath("/protected/servlet");
        
        System.out.println("Received response for login page: \n" + loginPage);
        
        assertTrue(
            "Should have received login page, but did not",
            loginPage.contains("Enter name and password to authenticate")
        );
        
        
        DefaultWebApplicationResponse testResponse = getWebApp().responseFromServerPath ("POST", "/j_security_check?j_username=test&j_password=test");

        // TestWebApp is not following redirects, so check & handle manually here
        
        assertTrue(
            "Should redirect",
            testResponse.getStatus() == 302
        );
        
        URL redirectUrl = new URL(testResponse.getHeader("Location"));
        
        String redirectPath = redirectUrl.getPath(); // omit params
            
        
        System.out.println("Redirecting to: " + redirectPath);
        
        String response = getWebApp().getFromServerPath(redirectPath);
        
        
        // Not only does the page needs to be accessible, the caller should have
        // the correct
        // name and roles as well

        // Being able to access a page protected by a role but then seeing the un-authenticated
        // (anonymous) user would normally be impossible, but could happen if the authorization
        // system checks roles on the authenticated subject, but does not correctly expose
        // or propagate these to the HttpServletRequest
        assertFalse(
            "Protected resource could be accessed, but the user appears to be the unauthenticated user. " + 
            "This should not be possible", 
            response.contains("web username: null")
        );
        
        // An authenticated user should have the exact name "test" and nothing else.
        assertTrue(
            "Protected resource could be accessed, but the username is not correct.",
            response.contains("web username: test")
        );

        // Being able to access a page protected by role "architect" but failing
        // the test for this role would normally be impossible, but could happen if the
        // authorization system checks roles on the authenticated subject, but does not
        // correctly expose or propagate these to the HttpServletRequest
        assertTrue(
            "Resource protected by role \"architect\" could be accessed, but user fails test for this role." + 
            "This should not be possible", 
            response.contains("web user has role \"architect\": true")
        );
        
        response = getWebApp().getFromServerPath("/protected/servlet");
        
        // Being able to access a page protected by a role but then seeing the un-authenticated
        // (anonymous) user would normally be impossible, but could happen if the authorization
        // system checks roles on the authenticated subject, but does not correctly expose
        // or propagate these to the HttpServletRequest
        assertFalse(
            "Protected resource could be accessed, but the user appears to be the unauthenticated user. " + 
            "This should not be possible", 
            response.contains("web username: null")
        );
        
        // An authenticated user should have the exact name "test" and nothing else.
        assertTrue(
            "Protected resource could be accessed, but the username is not correct.",
            response.contains("web username: test")
        );

        // Being able to access a page protected by role "architect" but failing
        // the test for this role would normally be impossible, but could happen if the
        // authorization system checks roles on the authenticated subject, but does not
        // correctly expose or propagate these to the HttpServletRequest
        assertTrue(
            "Resource protected by role \"architect\" could be accessed, but user fails test for this role." + 
            "This should not be possible", 
            response.contains("web user has role \"architect\": true")
        );
    }

}
