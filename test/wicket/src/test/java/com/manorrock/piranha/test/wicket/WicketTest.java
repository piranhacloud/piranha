/*
 * Copyright (c) 2002-2014 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.wicket;

import com.manorrock.piranha.DefaultDirectoryResource;
import com.manorrock.piranha.DefaultWebApplication;
import java.io.File;
import javax.servlet.FilterRegistration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the WicketFilter.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WicketTest {

    /**
     * Test /index.html
     *
     * @throws Exception
     */
    @Test
    public void testGetMethod2() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/java")));
        FilterRegistration.Dynamic filterReg =
                webApp.addFilter("wicket", "org.apache.wicket.protocol.http.WicketFilter");
        filterReg.setInitParameter("applicationClassName",
                "com.manorrock.piranha.test.wicket.WicketApplication");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setContextPath("");
        request.setServletPath("/");
        request.setPathInfo(null);

        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("Hello Wicket"));
    }
}
