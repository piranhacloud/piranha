/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.soteria;

import com.manorrock.webapp.DefaultAliasedDirectoryResource;
import com.manorrock.webapp.DefaultDirectoryResource;
import com.manorrock.webapp.DefaultWebApplication;
import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The JUnit tests for the Soteria web application.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SoteriaTest {

    /**
     * Test /servlet.
     *
     * @throws Exception
     */
    @Test
    @Ignore
    public void testSoteria() throws Exception {
        System.getProperties().put("java.naming.factory.initial", "com.manorrock.jndi.DefaultInitialContextFactory");
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addResource(new DefaultAliasedDirectoryResource(new File("target/classes"), "/WEB-INF/classes"));
        webApp.addServlet("Servlet", "com.manorrock.webapp.test.soteria.Servlet");
        webApp.addServletMapping("Servlet", "/servlet");
        webApp.addInitializer("org.jboss.weld.environment.servlet.EnhancedListener");
        webApp.addInitializer("org.glassfish.soteria.servlet.SamRegistrationInstaller");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setContextPath("");
        request.setServletPath("/servlet");

        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("This is a servlet"));
    }
}
