/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * The JUnit tests for the HttpMethodConstraintElement class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpMethodConstraintElementTest {

    /**
     * Test getMethodName method.
     */
    @Test
    public void testGetMethodName() {
        HttpMethodConstraintElement element = new HttpMethodConstraintElement("methodName");
        assertEquals("methodName", element.getMethodName());
    }

    /**
     * Test getMethodName method.
     */
    @Test
    public void testGetMethodName2() {
        HttpMethodConstraintElement element = new HttpMethodConstraintElement("methodName", new HttpConstraintElement());
        assertEquals("methodName", element.getMethodName());
    }
}
