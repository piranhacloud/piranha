/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.jersey;

import com.manorrock.piranha.DefaultDirectoryResource;
import com.manorrock.piranha.DefaultWebApplication;
import java.io.File;
import javax.servlet.ServletRegistration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the HelloServlet class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class JerseyTest {

    /**
     * Test GET /HelloServlet.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testHello() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addServletMapping("Jersey", "/rest/*");
        webApp.addServlet("Jersey", "org.glassfish.jersey.servlet.ServletContainer");
        ServletRegistration registration = webApp.getServletRegistration("Jersey");
        registration.setInitParameter("jersey.config.server.provider.packages", "com.manorrock.piranha.test.jersey");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setInputStream(new TestServletInputStream("input".getBytes(), request));
        request.setContextPath("");
        request.setServletPath("/rest");
        request.setPathInfo("/hello");
        TestHttpServletResponse response = new TestHttpServletResponse();
        
        webApp.service(request, response);
        
        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("Hello"));
    }
}
