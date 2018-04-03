/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import com.manorrock.webapp.DefaultWebApplication;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * The JUnit tests for the ServletContextAttributeEvent class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletContextAttributeEventTest {

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        ServletContextAttributeEvent event = new ServletContextAttributeEvent(webApp, "name", "value");
        assertEquals("name", event.getName());
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        ServletContextAttributeEvent event = new ServletContextAttributeEvent(webApp, "name", "value");
        assertEquals("value", event.getValue());
    }
}
