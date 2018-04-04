/*
 * Copyright (c) 2002-2017 Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.tapestry;

import com.manorrock.piranha.DefaultDirectoryResource;
import com.manorrock.piranha.DefaultWebApplication;
import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The JUnit tests for Tapestry.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TapestryTest {

    /**
     * Test Index page.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @Ignore
    public void testIndex() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultDirectoryResource(new File("src/main/webapp")));
        webApp.setInitParameter("tapestry.app-package", "com.manorrock.piranha.test.tapestry");
        webApp.addFilter("app", "org.apache.tapestry5.TapestryFilter");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setContextPath("");
        request.setServletPath("/");
        request.setPathInfo(null);
        TestHttpServletResponse response = new TestHttpServletResponse();

        webApp.service(request, response);

        assertEquals(200, response.getStatus());
        assertTrue(new String(response.getResponseBody()).contains("Hello Tapestry"));
    }
}
