/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha;

import com.sun.security.auth.UserPrincipal;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the SecurityManagerImpl class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultSecurityManagerTest {

    /**
     * Test addUser method.
     */
    @Test
    public void testAddUser() {
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setUserPrincipal(new UserPrincipal("username"));
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.addUser("username", "password", new String[]{"role1", "role2"});
        assertTrue(securityManager.isUserInRole(request, "role1"));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because BASIC authentication is not supported.
     * @throws IOException when an I/O error occurs.
     */
    @Test(expected = ServletException.class)
    public void testAuthenticate() throws ServletException, IOException {
        TestHttpServletRequest request = new TestHttpServletRequest();
        TestHttpServletResponse response = new TestHttpServletResponse();
        request.setAuthType(HttpServletRequest.BASIC_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertFalse(securityManager.authenticate(request, response));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because CLIENT_CERT_AUTH authentication is not
     * supported.
     * @throws IOException when an I/O error occurs.
     */
    @Test(expected = ServletException.class)
    public void testAuthenticate2() throws ServletException, IOException {
        TestHttpServletRequest request = new TestHttpServletRequest();
        TestHttpServletResponse response = new TestHttpServletResponse();
        request.setAuthType(HttpServletRequest.CLIENT_CERT_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertFalse(securityManager.authenticate(request, response));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because DIGEST_AUTH authentication is not
     * supported.
     * @throws IOException when an I/O error occurs.
     */
    @Test(expected = ServletException.class)
    public void testAuthenticate3() throws ServletException, IOException {
        TestHttpServletRequest request = new TestHttpServletRequest();
        TestHttpServletResponse response = new TestHttpServletResponse();
        request.setAuthType(HttpServletRequest.DIGEST_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertFalse(securityManager.authenticate(request, response));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because DIGEST_AUTH authentication is not
     * supported.
     * @throws IOException when an I/O error occurs.
     */
    @Test(expected = ServletException.class)
    public void testAuthenticate4() throws ServletException, IOException {
        TestHttpServletRequest request = new TestHttpServletRequest();
        TestHttpServletResponse response = new TestHttpServletResponse();
        request.setAuthType(HttpServletRequest.FORM_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertFalse(securityManager.authenticate(request, response));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testAuthenticate5() throws ServletException, IOException {
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setParameter("j_username", new String[]{"username"});
        request.setParameter("j_password", new String[]{"password"});
        TestHttpServletResponse response = new TestHttpServletResponse();
        request.setAuthType(HttpServletRequest.FORM_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.addUser("username", "password", new String[]{"role1"});
        assertTrue(securityManager.authenticate(request, response));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    @Test
    public void testAuthenticate6() throws ServletException, IOException {
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setParameter("j_username", new String[]{"username"});
        request.setParameter("j_password", new String[]{"password"});
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request);
        TestHttpServletResponse response = new TestHttpServletResponse();
        request.setAuthType(HttpServletRequest.FORM_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.addUser("username", "password", new String[]{"role1"});
        assertTrue(securityManager.authenticate(wrappedRequest, response));
    }

    /**
     * Test login method.
     *
     * @throws ServletException when a servlet error occurs.
     */
    @Test
    public void testLogin() throws ServletException {
        TestHttpServletRequest request = new TestHttpServletRequest();
        TestHttpServletResponse response = new TestHttpServletResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        webApp.linkRequestAndResponse(request, response);
        securityManager.setWebApplication(webApp);
        securityManager.addUser("username", "password", new String[]{"role1", "role2"});
        securityManager.login(request, "username", "password");
    }

    /**
     * Test removeUser method.
     */
    @Test
    public void testRemoveUser() {
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setUserPrincipal(new UserPrincipal("username"));
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.addUser("username", "password", new String[]{"role1", "role2"});
        assertTrue(securityManager.isUserInRole(request, "role1"));
        securityManager.removeUser("username");
        assertFalse(securityManager.isUserInRole(request, "role1"));
    }

    /**
     * Test getWebApplication method.
     */
    @Test
    public void testGetWebApplication() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertNull(securityManager.getWebApplication());
    }

    /**
     * Test getWebApplication method.
     */
    @Test
    public void testGetWebApplication2() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setWebApplication(new DefaultWebApplication());
        assertNotNull(securityManager.getWebApplication());
    }
}
