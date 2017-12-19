/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.myfaces;

import com.manorrock.webapp.DefaultDirectoryResource;
import com.manorrock.webapp.DefaultLoggingManager;
import com.manorrock.webapp.DefaultWebApplication;
import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the MyFaces web application.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloMyFacesTest {

    /**
     * Test /faces/notfound.html.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testNotFound() throws Exception {
        DefaultLoggingManager loggingManager = new DefaultLoggingManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setLoggingManager(loggingManager);
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addServletMapping("Faces Servlet", "/faces/*");
        webApp.addServlet("Faces Servlet", "javax.faces.webapp.FacesServlet");
        webApp.addListener("org.apache.myfaces.webapp.StartupServletContextListener");
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
        webApp.addServlet("Faces Servlet", "javax.faces.webapp.FacesServlet");
        webApp.addListener("org.apache.myfaces.webapp.StartupServletContextListener");
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
        assertTrue(responseString.contains("Hello MyFaces"));
    }
}
