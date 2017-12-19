/*
 * Copyright (c) 2002-2012 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.snoop;

import com.manorrock.webapp.DefaultDirectoryResource;
import com.manorrock.webapp.DefaultWebApplication;
import java.io.File;
import javax.servlet.http.Cookie;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the SnoopServlet class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SnoopServletTest {

    /**
     * Test GET method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetMethod() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setWebApplication(webApp);

        TestHttpServletResponse response = new TestHttpServletResponse();

        webApp.linkRequestAndResponse(request, response);

        SnoopServlet servlet = new SnoopServlet();
        servlet.service(request, response);

        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("Snoop"));

        webApp.unlinkRequestAndResponse(request, response);
    }

    /**
     * Test GET method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetMethod2() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addServletMapping("Snoop", "/Snoop");
        webApp.addServlet("Snoop", "com.manorrock.webapp.test.snoop.SnoopServlet");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/Snoop");
        request.setWebApplication(webApp);
        request.setParameter("Snoop", new String[] { "Snoop" });
        request.setAttribute("Snoop", "Snoop");
        Cookie cookie = new Cookie("COOKIE", "COOKIE");
        request.setCookies(new Cookie[] { cookie });
        TestHttpServletResponse response = new TestHttpServletResponse();

        webApp.service(request, response);

        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("Snoop"));
    }

    /**
     * Test POST method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testPostMethod() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addServletMapping("Snoop", "/Snoop");
        webApp.addServlet("Snoop", "com.manorrock.webapp.test.snoop.SnoopServlet");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/Snoop");
        request.setWebApplication(webApp);
        request.setParameter("Snoop", new String[] { "Snoop" });
        request.setAttribute("Snoop", "Snoop");
        request.setMethod("POST");
        Cookie cookie = new Cookie("COOKIE", "COOKIE");
        request.setCookies(new Cookie[] { cookie });
        TestHttpServletResponse response = new TestHttpServletResponse();

        webApp.service(request, response);

        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("Snoop"));
    }
}
