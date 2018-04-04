/*
 * Copyright (c) 2002-2017 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.vaadin;

import com.manorrock.piranha.DefaultDirectoryResource;
import com.manorrock.piranha.DefaultWebApplication;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit tests for the Vaadin tests.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloVaadinTest {
    /**
     * Test index.html page.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testIndexHtmlPage() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addServletMapping("Vaadin", "/*");
        webApp.addServlet("Vaadin", "com.manorrock.piranha.test.vaadin.HelloVaadinServlet");
        webApp.initialize();
        webApp.start();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/index.html");
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        webApp.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("Vaadin"));
        webApp.stop();
        webApp.destroy();
    }
}
