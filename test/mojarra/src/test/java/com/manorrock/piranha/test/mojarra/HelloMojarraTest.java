/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.mojarra;

import com.manorrock.piranha.DefaultDirectoryResource;
import com.manorrock.piranha.DefaultWebApplication;
import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the Hello Mojarra web application.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloMojarraTest {

    /**
     * Test /faces/notfound.html.
     *
     * @throws Exception
     */
    @Test
    public void testNotFound() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addServlet("Faces Servlet", "javax.faces.webapp.FacesServlet");
        webApp.addServletMapping("Faces Servlet", "/faces/*");
        webApp.addListener("com.sun.faces.config.ConfigureListener");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setContextPath("");
        request.setServletPath("/faces");
        request.setPathInfo("/notfound.html");

        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertEquals(404, response.getStatus());
    }

    /**
     * Test /index.html.
     *
     * @throws Exception
     */
    @Test
    public void testIndexHtml() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addServletMapping("Faces Servlet", "*.html");
        webApp.addServletMapping("Faces Servlet", "/faces/*");
        webApp.addServlet("Faces Servlet", "javax.faces.webapp.FacesServlet");
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
        assertTrue(responseString.contains("Hello Mojarra"));
    }
}
