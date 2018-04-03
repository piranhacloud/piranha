/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the UnavailableException class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class UnavailableExceptionTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        UnavailableException exception = new UnavailableException("Unavailable", 100);
        assertEquals(100, exception.getUnavailableSeconds());
    }

    /**
     * Test constructor.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor2() {
        new UnavailableException(null, "Message");
    }

    /**
     * Test constructor.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor3() {
        new UnavailableException(100, null, "Message");
    }

    /**
     * Test getServlet method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetServlet() {
        UnavailableException exception = new UnavailableException("Unavailable");
        exception.getServlet();
    }

    /**
     * Test getUnavailableSeconds method.
     */
    @Test
    public void testGetUnavailableSeconds() {
        UnavailableException exception = new UnavailableException("Unavailable");
        assertEquals(-1, exception.getUnavailableSeconds());
    }

    /**
     * Test isPermanent method.
     */
    @Test
    public void testIsPermanent() {
        UnavailableException exception = new UnavailableException("Unavailable");
        assertTrue(exception.isPermanent());
    }
}
