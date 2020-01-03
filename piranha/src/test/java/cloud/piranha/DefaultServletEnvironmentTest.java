/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha;

import javax.servlet.MultipartConfigElement;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultServletEnvironment class.
 *
 * @author Manfred Riem (mriem@manorrock.com).
 */
public class DefaultServletEnvironmentTest {

    /**
     * Test getLoadOnStartup method.
     */
    @Test
    public void testGetLoadOnStartup() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, null, servlet);
        environment.setLoadOnStartup(1);
        assertEquals(1, environment.getLoadOnStartup());
    }

    /**
     * Test setAsyncSupported method.
     */
    @Test
    public void testSetAsyncSupported() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, null, servlet);
        assertFalse(environment.isAsyncSupported());
        environment.setAsyncSupported(true);
        assertTrue(environment.isAsyncSupported());
    }

    /**
     * Test setMultipartConfig method.
     */
    @Test
    public void testMultipartConfig() {
        TestSnoopServlet servlet = new TestSnoopServlet();
        DefaultServletEnvironment environment = new DefaultServletEnvironment(null, null, servlet);
        assertNull(environment.getMultipartConfig());
        environment.setMultipartConfig(new MultipartConfigElement("/location"));
        assertNotNull(environment.getMultipartConfig());
    }
}
