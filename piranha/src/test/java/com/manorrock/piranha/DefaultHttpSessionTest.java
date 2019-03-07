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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultHttpSession class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpSessionTest {

    /**
     * Test getAttribute method.
     */
    @Test
    public void testGetAttribute() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
        session.setAttribute("TEST", "TEST");
        assertEquals("TEST", session.getAttribute("TEST"));
        session.removeAttribute("TEST");
        assertNull(session.getAttribute("TEST"));
    }

    /**
     * Test getAttributeNames method.
     */
    @Test
    public void testGetAttributeNames() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertFalse(session.getAttributeNames().hasMoreElements());
    }

    /**
     * Test getCreationTime method.
     */
    @Test
    public void testGetCreationTime() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertTrue(session.getCreationTime() > 0);
    }

    /**
     * Test getId method.
     */
    @Test
    public void testGetId() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setId("ID");
        assertEquals("ID", session.getId());
    }

    /**
     * Test getLastAccessedTime method.
     */
    @Test
    public void testGetLastAccessedTime() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertTrue(session.getCreationTime() > 0);
        assertTrue(session.getLastAccessedTime() >= session.getCreationTime());
    }

    /**
     * Test getMaxInactiveInterval method.
     */
    @Test
    public void testGetMaxInactiveInterval() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setMaxInactiveInterval(1000);
        assertEquals(1000, session.getMaxInactiveInterval());
    }

    /**
     * Test getServletContext method.
     */
    @Test
    public void testGetServletContext() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(webApp.getHttpSessionManager());
        assertNotNull(session.getServletContext());
    }

    /**
     * Test getSessionContext method.
     */
    @Test
    public void testGetSessionContext() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertNull(session.getSessionContext());
    }

    /**
     * Test getSessionManager method.
     */
    @Test
    public void testGetSessionManager() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
    }

    /**
     * Test getValue method.
     */
    @Test
    public void testGetValue() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
        session.putValue("TEST", "TEST");
        assertEquals("TEST", session.getValue("TEST"));
        session.removeValue("TEST");
        assertNull(session.getValue("TEST"));
    }

    /**
     * Test getValueNames method.
     */
    @Test
    public void testGetValueNames() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        assertTrue(session.getValueNames().length == 0);
    }

    /**
     * Test invalidate method.
     */
    @Test
    public void testInvalidate() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.invalidate();
    }

    /**
     * Test invalidate method.
     *
     * @throws IllegalStateException when the session is invalid.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidate2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.invalidate();
        session.setAttribute("TEST", "TEST");
    }

    /**
     * Test isNew method.
     */
    @Test
    public void testIsNew() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setNew(true);
        assertTrue(session.isNew());
        session.setNew(false);
        assertFalse(session.isNew());
    }

    /**
     * Test setAttribute method.
     */
    @Test
    public void testSetAttribute() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultHttpSession session = new DefaultHttpSession(webApp);
        session.setSessionManager(new DefaultHttpSessionManager());
        session.setAttribute("TEST", "TEST");
        session.setAttribute("TEST", null);
        assertNull(session.getAttribute("TEST"));
    }
}
