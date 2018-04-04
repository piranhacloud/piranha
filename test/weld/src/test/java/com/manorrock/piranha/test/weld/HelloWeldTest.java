/*
 * Copyright (c) 2002-2017 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.weld;

import com.manorrock.piranha.DefaultAliasedDirectoryResource;
import com.manorrock.piranha.DefaultDirectoryResource;
import com.manorrock.piranha.DefaultWebApplication;
import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the Hello Weld web application.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloWeldTest {

    /**
     * Test /index.html.
     *
     * @throws Exception
     */
    @Test
    public void testIndexHtml() throws Exception {
        System.getProperties().put("java.naming.factory.initial", "com.manorrock.piranha.jndi.DefaultInitialContextFactory");
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addResource(new DefaultAliasedDirectoryResource(new File("target/classes"), "/WEB-INF/classes"));
        webApp.addServletMapping("Faces Servlet", "*.html");
        webApp.addServletMapping("Faces Servlet", "/faces/*");
        webApp.addServlet("Faces Servlet", "javax.faces.webapp.FacesServlet");
        webApp.addListener("org.jboss.weld.environment.servlet.Listener");
        webApp.addListener("com.sun.faces.config.ConfigureListener");
        webApp.initialize();
        webApp.start();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setContextPath("");
        request.setServletPath("/index.html");
        request.setPathInfo(null);
        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        webApp.service(request, response);
        assertEquals(200, response.getStatus());
        String responseString = new String(response.getResponseBody());
        assertTrue(responseString.contains("Hello Weld"));
    }
}
