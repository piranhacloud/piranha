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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpHeaderManagerTest {

    /**
     * Test addHeader method.
     */
    @Test
    public void testAddHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "VALUE");
        assertEquals("VALUE", manager.getHeader("NAME"));
    }

    /**
     * Test addHeader method.
     */
    @Test
    public void testAddHeader2() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "VALUE");
        manager.addHeader("NAME", "VALUE2");
        assertEquals("VALUE", manager.getHeader("NAME"));
        Enumeration<String> values = manager.getHeaders("NAME");
        assertTrue(values.hasMoreElements());
        assertNotNull(values.nextElement());
        assertTrue(values.hasMoreElements());
        assertNotNull(values.nextElement());
        assertFalse(values.hasMoreElements());
    }

    /**
     * Test containsHeader method.
     */
    @Test
    public void testContainsHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "VALUE");
        assertEquals("VALUE", manager.getHeader("NAME"));
        assertTrue(manager.containsHeader("NAME"));
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    public void testGetDateHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        manager.addHeader("NAME", format.format(new Date()));
        assertTrue(manager.getDateHeader("NAME") > 0);
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    public void testGetDateHeader2() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        assertEquals(-1, manager.getDateHeader("NAME"));
    }

    /**
     * Test getDateHeader method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetDateHeader3() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "KABOOM");
        manager.getDateHeader("NAME");
    }

    /**
     * Test getHeader method.
     */
    @Test
    public void testGetHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        assertNull(manager.getHeader("NAME"));
    }

    /**
     * Test getDateHeader method.
     */
    @Test
    public void testGetHeaderNames() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "VALUE");
        Enumeration<String> names = manager.getHeaderNames();
        assertTrue(names.hasMoreElements());
        assertEquals("NAME", names.nextElement());
    }

    /**
     * Test getHeaders method.
     */
    @Test
    public void testGetHeaders() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        assertNotNull(manager.getHeaders("NAME"));
        assertFalse(manager.getHeaders("NAME").hasMoreElements());
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    public void testGetIntHeader() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.addHeader("NAME", "1");
        assertEquals(1, manager.getIntHeader("NAME"));
    }

    /**
     * Test getIntHeader method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetIntHeader2() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        manager.setHeader("NAME", "abcd");
        assertEquals(1, manager.getIntHeader("NAME"));
    }

    /**
     * Test getIntHeader method.
     */
    @Test
    public void testGetIntHeader3() {
        DefaultHttpHeaderManager manager = new DefaultHttpHeaderManager();
        assertEquals(-1, manager.getIntHeader("NAME"));
    }
}
