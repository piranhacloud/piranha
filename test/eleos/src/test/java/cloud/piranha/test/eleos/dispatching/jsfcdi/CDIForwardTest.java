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
package cloud.piranha.test.eleos.dispatching.jsfcdi;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import cloud.piranha.test.utils.TestWebApp;

/**
 * The basic forward test tests that a SAM is able to forward to a simple Servlet.
 * 
 * @author Arjan Tijms
 * 
 */
public class CDIForwardTest {
    
    TestWebApp webApp;
    
    @Before
    public void testProtected() throws Exception {
        webApp = Application.get();    
    }
    
    protected TestWebApp getWebApp() {
        return webApp;
    }


    /**
     * Tests that the forwarded resource can utilize a CDI bean
     * 
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public void testCDIForwardViaPublicResource() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("public/servlet");
        
        System.out.println(response);
        
        assertTrue(
            "Response did not contain output from public Servlet with CDI that SAM forwarded to.", 
            response.contains("response from forwardedServlet - Called from CDI")
        );
    }
    
    /**
     * Tests that the forwarded resource can utilize a CDI bean
     * 
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public void testCDIForwardViaProtectedResource() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("protected/servlet");
        assertTrue(
            "Response did not contain output from protected Servlet with CDI that SAM forwarded to.", 
            response.contains("response from forwardedServlet - Called from CDI")
        );
    }
    
    /**
     * Tests that the forwarded resource has the correct servlet path
     * 
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public void testCDIForwardWithRequestPublic() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("public/servlet");
        
        assertTrue(
            "Servletpath reported by servlet request after forward from SAM not as expected.", 
            response.contains("servletPath via Servlet - /forwardedServlet")
        );
    }
    
    /**
     * Tests that the forwarded resource has the correct servlet path
     * 
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public void testCDIForwardWithRequestProtected() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("protected/servlet");
        
        assertTrue(
            "Servletpath reported by servlet request after forward from SAM not as expected.", 
            response.contains("servletPath via Servlet - /forwardedServlet")
        );
    }
    
    /**
     * Tests that the forwarded resource can utilize an injected HttpServletRequest and that
     * the value is correct.
     * 
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public void testCDIForwardWithRequestInjectPublic() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("public/servlet");
        
        System.out.println(response);
        
        assertTrue(
            "Servletpath reported by servlet request after forward from SAM not as expected.", 
            response.contains("servletPath via Servlet - /forwardedServlet")
        );
        
        assertTrue(
            "Response did not contain output from forwarded Servlet using CDI injected request. " +
            "Request appears not to be usable.", 
            response.contains("servletPath via CDI")
        );
        
        assertTrue(
            "Servletpath reported by injected request after forward from SAM not as expected.", 
            response.contains("servletPath via CDI - /forwardedServlet")
        );
    }
    
    /**
     * Tests that the forwarded resource can utilize an injected HttpServletRequest and that
     * the value is correct.
     * 
     * @throws IOException
     * @throws SAXException
     */
    @Test
    public void testCDIForwardWithRequestInjectProtected() throws IOException, SAXException {

        String response = getWebApp().getFromServerPath("protected/servlet");
        
        assertTrue(
            "Servletpath reported by servlet request after forward from SAM not as expected.", 
            response.contains("servletPath via Servlet - /forwardedServlet")
        );
        
        assertTrue(
            "Response did not contain output from forwarded Servlet using CDI injected request. " +
            "Request appears not to be usable.", 
            response.contains("servletPath via CDI")
        );
        
        assertTrue(
            "Servletpath reported by injected request after forward from SAM not as expected.", 
            response.contains("servletPath via CDI - /forwardedServlet")
        );
    }

}