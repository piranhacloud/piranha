/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the ServletException class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletExceptionTest {

    /**
     * Test getRootCause method.
     */
    @Test
    public void testGetRootCause() {
        ServletException exception = new ServletException();
        assertNull(exception.getRootCause());
    }

    /**
     * Test getRootCause method.
     */
    @Test
    public void testGetRootCause2() {
        ServletException exception = new ServletException(new RuntimeException());
        assertNotNull(exception.getRootCause());
        assertTrue(exception.getRootCause() instanceof RuntimeException);
    }
}
