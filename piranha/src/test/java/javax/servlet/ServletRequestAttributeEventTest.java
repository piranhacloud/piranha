/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import com.manorrock.piranha.DefaultWebApplication;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * The JUnit tests for the ServletRequestAttributeEvent class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletRequestAttributeEventTest {

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        DefaultWebApplication webapp = new DefaultWebApplication();
        ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(webapp, null, "name", null);
        assertEquals("name", event.getName());
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        DefaultWebApplication webapp = new DefaultWebApplication();
        ServletRequestAttributeEvent event = new ServletRequestAttributeEvent(webapp, null, "name", "value");
        assertEquals("value", event.getValue());
    }
}
