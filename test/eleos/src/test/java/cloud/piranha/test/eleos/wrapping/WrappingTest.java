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
package cloud.piranha.test.eleos.wrapping;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import cloud.piranha.test.utils.TestWebApp;

/**
 * This tests that the wrapped request and response a SAM puts into the MessageInfo structure reaches the Servlet that's
 * invoked as well as all filters executed before that.
 * 
 * @author Arjan Tijms
 * 
 */
public class WrappingTest {

    TestWebApp webApp;
    
    @Before
    public void testProtected() throws Exception {
        webApp = WrappingApplication.get();    
    }
    
    protected TestWebApp getWebApp() {
        return webApp;
    }
    
    @Test
    public void testDeclaredFilterRequestWrapping() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("protected/servlet");

        // The SAM wrapped a request so that it always contains the request attribute "isWrapped" with value true.
        assertTrue("Request wrapped by SAM did not arrive in declared Filter.",
            response.contains("declared filter request isWrapped: true"));
    }

    @Test
    public void testDeclaredFilterResponseWrapping() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("protected/servlet");

        // The SAM wrapped a response so that it always contains the header "isWrapped" with value true.
        assertTrue("Response wrapped by SAM did not arrive in declared Filter.",
            response.contains("declared filter response isWrapped: true"));
    }

    @Test
    public void testRequestWrapping() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("protected/servlet");

        // The SAM wrapped a request so that it always contains the request attribute "isWrapped" with value true.
        assertTrue("Request wrapped by SAM did not arrive in Servlet.",
            response.contains("servlet request isWrapped: true"));
    }

    @Test
    public void testResponseWrapping() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("protected/servlet");

        // The SAM wrapped a response so that it always contains the header "isWrapped" with value true.
        assertTrue("Response wrapped by SAM did not arrive in Servlet.",
            response.contains("servlet response isWrapped: true"));
    }

}