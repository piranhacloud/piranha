/*
 * Copyright (c) 2002-2011 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.springmvc;

import com.manorrock.webapp.DefaultDirectoryResource;
import com.manorrock.webapp.DefaultLoggingManager;
import com.manorrock.webapp.DefaultWebApplication;
import java.io.File;
import javax.servlet.ServletRegistration.Dynamic;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit tests for the HelloSpringController class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HelloSpringControllerTest {
    /**
     * Test GET method.
     * 
     * @throws Exception
     */
    @Test
    public void testGetMethod2() throws Exception {
        DefaultLoggingManager loggingManager = new DefaultLoggingManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setLoggingManager(loggingManager);
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addServletMapping("Spring", "*.do");
        webApp.addServlet("Spring", "org.springframework.web.servlet.DispatcherServlet");
        webApp.addServletMapping("JSP Servlet", "*.jsp");
        Dynamic dynamic = webApp.addServlet("JSP Servlet", "org.apache.jasper.servlet.JspServlet");
        dynamic.setInitParameter("classpath",
                System.getProperty("user.home") + "/.m2/repository/javax/servlet/javax.servlet-api/4.0.0/javax.servlet-api-4.0.0.jar" + File.pathSeparator +
                System.getProperty("user.home") + "/.m2/repository/javax/servlet/jsp/javax.servlet.jsp-api/2.3.1/javax.servlet.jsp-api-2.3.1.jar" + File.pathSeparator +
                System.getProperty("user.home") + "/.m2/repository/org/glassfish/web/javax.servlet.jsp/2.3.2/javax.servlet.jsp-2.3.2.jar");
        
        webApp.initialize();
        webApp.start();
        
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setServletPath("/hellospring.do");
        request.setWebApplication(webApp);
        
        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        
        webApp.service(request, response);

        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("Hello Spring"));

        webApp.stop();
        webApp.destroy();
    }
}
