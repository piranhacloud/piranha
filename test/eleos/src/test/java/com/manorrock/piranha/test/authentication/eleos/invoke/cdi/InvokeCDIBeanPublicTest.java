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
package com.manorrock.piranha.test.authentication.eleos.invoke.cdi;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.manorrock.piranha.test.utils.TestWebApp;

/**
 * This tests that a SAM is able to obtain and call a CDI bean when the request is to a public resource 
 * (a resource for which no security constraints have been set).
 * 
 * @author Arjan Tijms
 * 
 */
public class InvokeCDIBeanPublicTest {
    
    TestWebApp webApp;
    
    @Before
    public void testProtected() throws Exception {
        webApp = Application.get();    
    }
    
    protected TestWebApp getWebApp() {
        return webApp;
    }

    @Test
    public void publicInvokeCDIFromValidateRequest() throws IOException {
        String response = getWebApp().getFromServerPath("public/servlet?tech=cdi");
        
        assertTrue(
            "Response did not contain output from CDI bean for validateRequest for public resource. (note: this is not required by the spec)", 
            response.contains("validateRequest: Called from CDI")
        );
    }
    
    @Test
    public void publicInvokeCDIFromCleanSubject() throws IOException {
        String response = getWebApp().getFromServerPath("public/servlet?tech=cdi");
        
        assertTrue(
            "Response did not contain output from CDI bean for cleanSubject for public resource. (note: this is not required by the spec)", 
            response.contains("cleanSubject: Called from CDI")
        );
    }
    
    @Test
    public void publicInvokeCDIFromSecureResponse() throws IOException {
        String response = getWebApp().getFromServerPath("public/servlet?tech=cdi");
        
        assertTrue(
            "Response did not contain output from CDI bean for secureResponse for public resource. (note: this is not required by the spec)", 
            response.contains("secureResponse: Called from CDI")
        );
    }
    
    @Test
    public void publicInvokeCDIUseInjectedRequestFromValidateRequest() throws IOException {
        String response = getWebApp().getFromServerPath("public/servlet?tech=cdi");
        
        assertTrue(
            "Response did not contain output from CDI bean using an inject request for validateRequest for public resource. (note: this is not required by the spec)", 
            response.contains("validateRequest: Called from CDI via injected request")
        );
    }
    
    @Test
    public void publicInvokeCDIUseInjectedRequestFromCleanSubject() throws IOException {
        String response = getWebApp().getFromServerPath("public/servlet?tech=cdi");
        
        assertTrue(
            "Response did not contain output from CDI bean using an inject request for cleanSubject for public resource. (note: this is not required by the spec)", 
            response.contains("cleanSubject: Called from CDI via injected request")
        );
    }
    
    @Test
    public void publicInvokeCDIUseInjectedRequestFromSecureResponse() throws IOException {
        String response = getWebApp().getFromServerPath("public/servlet?tech=cdi");
        
        assertTrue(
            "Response did not contain output from CDI bean using an inject request for secureResponse for public resource. (note: this is not required by the spec)", 
            response.contains("secureResponse: Called from CDI via injected request")
        );
    }

}