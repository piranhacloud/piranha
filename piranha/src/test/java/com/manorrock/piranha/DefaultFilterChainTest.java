/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import org.junit.Test;

/**
 * The JUnit tests for the DefaultFilterTerminatingChain class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultFilterChainTest {

    /**
     * Test doFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testDoFilter() throws Exception {
        DefaultFilterChain chain = new DefaultFilterChain();
        chain.doFilter(null, null);
    }
}
