/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import com.manorrock.webapp.DefaultHttpServletRequest;
import com.manorrock.webapp.DefaultWebApplication;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * The JUnit tests for the ServletRequestEvent class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletRequestEventTest {

    /**
     * Test getServletContext method.
     */
    @Test
    public void testGetServletContext() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        ServletRequestEvent event = new ServletRequestEvent(webApplication, null);
        assertNotNull(event.getServletContext());
    }

    /**
     * Test getServletRequest method.
     */
    @Test
    public void testGetServletRequest() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultHttpServletRequest servletRequest = new DefaultHttpServletRequest() {
        };
        ServletRequestEvent event = new ServletRequestEvent(webApplication, servletRequest);
        assertNotNull(event.getServletRequest());
    }

}
