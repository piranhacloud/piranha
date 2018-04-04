/*
 * Copyright (c) 2002-2012 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.struts;

import com.manorrock.piranha.DefaultDirectoryResource;
import com.manorrock.piranha.DefaultLoggingManager;
import com.manorrock.piranha.DefaultWebApplication;
import java.io.File;
import javax.servlet.ServletRegistration.Dynamic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the Struts 2 filter.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloStrutsFilterTest {

    /**
     * Test GET method.
     *
     * @throws Exception when a major error occurs.
     */
    @Test
    public void testGetMethod() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setLoggingManager(new DefaultLoggingManager());
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addFilter("struts2", "org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter");
        webApp.addServletMapping("JSP Servlet", "*.jsp");
        Dynamic dynamic = webApp.addServlet("JSP Servlet", "org.apache.jasper.servlet.JspServlet");
        dynamic.setInitParameter("classpath",
                System.getProperty("user.home") + "/.m2/repository/javax/servlet/javax.servlet-api/4.0.0/javax.servlet-api-4.0.0.jar" + File.pathSeparator
                + System.getProperty("user.home") + "/.m2/repository/javax/servlet/jsp/javax.servlet.jsp-api/2.3.1/javax.servlet.jsp-api-2.3.1.jar" + File.pathSeparator
                + System.getProperty("user.home") + "/.m2/repository/org/glassfish/web/javax.servlet.jsp/2.3.2/javax.servlet.jsp-2.3.2.jar");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/HelloWorld.action");

        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("Hello World Struts"));
    }
}
