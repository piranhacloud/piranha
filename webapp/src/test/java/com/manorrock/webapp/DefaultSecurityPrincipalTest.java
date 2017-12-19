/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultSecurityPrincipal class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultSecurityPrincipalTest {

    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        DefaultSecurityPrincipal principal = new DefaultSecurityPrincipal("user");
        assertEquals("user", principal.getName());
    }
}
