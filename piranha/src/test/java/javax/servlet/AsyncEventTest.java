/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import com.manorrock.piranha.DefaultAsyncContext;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * The JUnit tests for the AsyncEvent class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AsyncEventTest {

    /**
     * Test getAsyncContext method.
     */
    @Test
    public void testGetAsyncContext() {
        AsyncEvent event = new AsyncEvent(new DefaultAsyncContext(null, null));
        assertNotNull(event.getAsyncContext());
    }

    /**
     * Test getSuppliedRequest method.
     */
    @Test
    public void testGetSuppliedRequest() {
        AsyncEvent event = new AsyncEvent(new DefaultAsyncContext(null, null), new HttpServletRequestWrapper(null), null);
        assertNotNull(event.getSuppliedRequest());
    }

    /**
     * Test getSuppliedRequest method.
     */
    @Test
    public void testGetSuppliedResponse() {
        AsyncEvent event = new AsyncEvent(new DefaultAsyncContext(null, null), null, new HttpServletResponseWrapper(null));
        assertNotNull(event.getSuppliedResponse());
    }

    /**
     * Test getThrowable method.
     */
    @Test
    public void testGetThrowable() {
        AsyncEvent event = new AsyncEvent(new DefaultAsyncContext(null, null), new RuntimeException());
        assertNotNull(event.getThrowable());
    }
}
