/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.tests;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import jakarta.servlet.http.HttpSession;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for any HttpSession implementation.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class HttpSessionTest {

    /**
     * Create the web application.
     *
     * @return the web application.
     */
    protected abstract WebApplication createWebApplication();
    
    /**
     * Create the web application request.
     *
     * @return the web application request.
     */
    protected abstract WebApplicationRequest createWebApplicationRequest();
    
    /**
     * Create the web application response.
     * 
     * @return the web application response.
     */
    protected abstract WebApplicationResponse createWebApplicationResponse();
    
    /**
     * Test getAttribute method.
     */
    @Test
    void testGetAttribute() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        session.setAttribute("TestGetAttribute", "TestGetAttribute");
        assertEquals("TestGetAttribute", session.getAttribute("TestGetAttribute"));
        session.removeAttribute("TestGetAttribute");
        assertNull(session.getAttribute("TestGetAttribute"));
    }

    /**
     * Test getAttributeNames method.
     */
    @Test
    void testGetAttributeNames() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        assertFalse(session.getAttributeNames().hasMoreElements());
    }
    
    /**
     * Test getCreationTime method.
     */
    @Test
    void testGetCreationTime() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        assertTrue(session.getCreationTime() > 0);
    }
    

    /**
     * Test getId method.
     */
    @Test
    void testGetId() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        assertNotNull(session.getId());
    }
    
    /**
     * Test getLastAccessedTime method.
     */
    @Test
    void testGetLastAccessedTime() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        assertTrue(session.getCreationTime() > 0);
        assertTrue(session.getLastAccessedTime() >= session.getCreationTime());
    }
    
    /**
     * Test getMaxInactiveInterval method.
     */
    @Test
    void testGetMaxInactiveInterval() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(1000);
        assertEquals(1000, session.getMaxInactiveInterval());
    }

    /**
     * Test getServletContext method.
     */
    @Test
    void testGetServletContext() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        assertNotNull(session.getServletContext());
        assertEquals(webApplication, session.getServletContext());
    }
    
    /**
     * Test invalidate method.
     */
    @Test
    void testInvalidate() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        session.invalidate();
        try {
            session.isNew();
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * Test isNew method.
     */
    @Test
    void testIsNew() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        assertTrue(session.isNew());
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    void testRemoveAttribute() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        session.setAttribute("TestRemoveAttribute", "TestRemoveAttribute");
        assertEquals("TestRemoveAttribute", session.getAttribute("TestRemoveAttribute"));
        session.removeAttribute("TestRemoveAttribute");
        assertNull(session.getAttribute("TestRemoveAttribute"));
    }

    /**
     * Test setAttribute method.
     */
    @Test
    void testSetAttribute() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        session.setAttribute("TestSetAttribute", "TestSetAttribute");
        assertEquals("TestSetAttribute", session.getAttribute("TestSetAttribute"));
    }

    /**
     * Test setAttribute method.
     */
    @Test
    void testSetAttribute2() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        session.setAttribute("TestSetAttribute", "TestSetAttribute");
        session.setAttribute("TestSetAttribute", null);
        assertNull(session.getAttribute("TestSetAttribute"));
    }

    /**
     * Test setMaxInactiveInterval method.
     */
    @Test
    void testSetMaxInactiveInterval() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(15);
        assertEquals(15, session.getMaxInactiveInterval());
    }
}
