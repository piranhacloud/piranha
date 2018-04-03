/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import com.manorrock.webapp.DefaultHttpSession;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The JUnit tests for the HttpSessionBindingEventTest class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpSessionBindingEventTest {
    
    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
         DefaultHttpSession session = new DefaultHttpSession(null);
        HttpSessionBindingEvent event = new HttpSessionBindingEvent(session, "name");
        assertEquals("name", event.getName());
    }

    /**
     * Test getSession method.
     */
    @Test
    public void testGetSession() {
        DefaultHttpSession session = new DefaultHttpSession(null);
        HttpSessionBindingEvent event = new HttpSessionBindingEvent(session, null);
        assertNotNull(event.getSession());
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        DefaultHttpSession session = new DefaultHttpSession(null);
        HttpSessionBindingEvent event = new HttpSessionBindingEvent(session, "name", "value");
        assertEquals("value", event.getValue());
    }
}
