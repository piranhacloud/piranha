/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultFilterEnvironment class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultFilterEnvironmentTest {
    
    /**
     * Test getName method.
     */
    @Test
    public void testGetName() {
        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment(null, "myfilter", null);
        assertEquals("myfilter", filterEnvironment.getName());
    }
    
    /**
     * Test getInitParameters method.
     */
    @Test
    public void testGetInitParameters() {
        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment();
        assertNotNull(filterEnvironment.getInitParameters());
    }
        
    /**
     * Test getUrlPatternMappings method.
     */
    @Test
    public void testGetUrlPatternMappings() {
        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment(null, "myfilter", null);
        assertTrue(filterEnvironment.getUrlPatternMappings().isEmpty());
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test
    public void testSetInitParameters() {
        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment();
        assertTrue(filterEnvironment.setInitParameters(new HashMap<>()).isEmpty());
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInitParameters2() {
        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(null, null);
        filterEnvironment.setInitParameters(parameters);
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInitParameters3() {
        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", null);
        filterEnvironment.setInitParameters(parameters);
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test
    public void testSetInitParameters4() {
        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        assertTrue(filterEnvironment.setInitParameters(parameters).isEmpty());
    }
    
    /**
     * Test setInitParameters method.
     */
    @Test
    public void testSetInitParameters5() {
        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment();
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        assertTrue(filterEnvironment.setInitParameters(parameters).isEmpty());
        assertFalse(filterEnvironment.setInitParameters(parameters).isEmpty());
    }
}
