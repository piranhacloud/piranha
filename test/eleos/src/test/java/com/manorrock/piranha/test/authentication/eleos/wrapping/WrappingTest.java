package com.manorrock.piranha.test.authentication.eleos.wrapping;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.manorrock.piranha.test.utils.TestWebApp;

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