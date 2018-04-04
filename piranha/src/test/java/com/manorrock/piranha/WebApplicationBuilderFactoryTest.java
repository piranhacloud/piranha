/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * The JUnit tests for the WebApplicationBuilderFactory class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebApplicationBuilderFactoryTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(new WebApplicationBuilderFactory());
    }

    /**
     * Test produce method.
     */
    @Test
    public void testProduce() {
        assertNotNull(WebApplicationBuilderFactory.produce());
    }
}
