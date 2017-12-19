/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import com.manorrock.webapp.DefaultWebApplication;
import com.manorrock.webapp.TestHttpServletRequest;
import com.manorrock.webapp.TestHttpServletResponse;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the HttpServletRequestWrapper class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpServletRequestWrapperTest {

    /**
     * Test authenticate method.
     *
     * @throws Exception when an serious error occurs.
     */
    @Test
    public void testAuthenticate() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        TestHttpServletResponse response = new TestHttpServletResponse();
        TestHttpServletRequest wrapped = new TestHttpServletRequest();
        wrapped.setWebApplication(webApplication);
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(wrapped);
        assertFalse(wrapper.authenticate(response));
    }
    
    /**
     * Test getHttpServletMapping method.
     */
    @Test
    public void testGetHttpServletMapping() { 
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(null);
        assertNotNull(wrapper.getHttpServletMapping());
        HttpServletMapping mapping = wrapper.getHttpServletMapping();
        assertEquals("", mapping.getMatchValue());
        assertEquals("", mapping.getPattern());
        assertEquals("", mapping.getServletName());
        assertNull(mapping.getMappingMatch());
    }
    
    /**
     * Test getTrailerFields method.
     */
    @Test
    public void testGetTrailerFields() { 
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(null);
        assertNotNull(wrapper.getTrailerFields());
        Map<String, String> trailerFields = wrapper.getTrailerFields();
        assertTrue(trailerFields.isEmpty());
    }
    
    /**
     * Test isTrailerFieldsReady method.
     */
    @Test
    public void testIsTrailerFieldsReady() { 
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(null);
        assertTrue(wrapper.isTrailerFieldsReady());
    }
    
    /**
     * Test newPushBuilder method.
     */
    @Test
    public void testNewPushBuilder() { 
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(null);
        assertNull(wrapper.newPushBuilder());
    }
}
