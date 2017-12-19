/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import javax.servlet.annotation.ServletSecurity;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The JUnit tests for the HttpConstraintElement class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpConstraintElementTest {
    
    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        HttpConstraintElement element = new HttpConstraintElement(
                ServletSecurity.TransportGuarantee.NONE, "developer");
        assertEquals(ServletSecurity.EmptyRoleSemantic.PERMIT, element.getEmptyRoleSemantic());
        assertEquals(ServletSecurity.TransportGuarantee.NONE, element.getTransportGuarantee());
        assertEquals("developer", element.getRolesAllowed()[0]);
    }
}
