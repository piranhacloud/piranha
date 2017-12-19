/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

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
