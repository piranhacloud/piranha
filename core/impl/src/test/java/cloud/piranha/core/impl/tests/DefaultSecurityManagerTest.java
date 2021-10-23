/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl.tests;

import cloud.piranha.core.impl.DefaultSecurityManager;
import cloud.piranha.core.impl.DefaultWebApplication;
import com.sun.security.auth.UserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the SecurityManagerImpl class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultSecurityManagerTest {

    /**
     * Test addUser method.
     */
    @Test
    void testAddUser() {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
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
    @Test
    void testAuthenticate() throws ServletException, IOException {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        request.setAuthType(HttpServletRequest.BASIC_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertThrows(ServletException.class, () -> assertFalse(securityManager.authenticate(request, response)));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because CLIENT_CERT_AUTH authentication is not
     * supported.
     * @throws IOException when an I/O error occurs.
     */
    @Test
    void testAuthenticate2() throws ServletException, IOException {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        request.setAuthType(HttpServletRequest.CLIENT_CERT_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertThrows(ServletException.class, () -> assertFalse(securityManager.authenticate(request, response)));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because DIGEST_AUTH authentication is not
     * supported.
     * @throws IOException when an I/O error occurs.
     */
    @Test
    void testAuthenticate3() throws ServletException, IOException {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        request.setAuthType(HttpServletRequest.DIGEST_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertThrows(ServletException.class, () -> assertFalse(securityManager.authenticate(request, response)));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because DIGEST_AUTH authentication is not
     * supported.
     * @throws IOException when an I/O error occurs.
     */
    @Test
    void testAuthenticate4() throws ServletException, IOException {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        request.setAuthType(HttpServletRequest.FORM_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertThrows(ServletException.class, () -> assertFalse(securityManager.authenticate(request, response)));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    @Test
    void testAuthenticate5() throws ServletException, IOException {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setParameter("j_username", new String[]{"username"});
        request.setParameter("j_password", new String[]{"password"});
        TestWebApplicationResponse response = new TestWebApplicationResponse();
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
    void testAuthenticate6() throws ServletException, IOException {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setParameter("j_username", new String[]{"username"});
        request.setParameter("j_password", new String[]{"password"});
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request);
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        request.setAuthType(HttpServletRequest.FORM_AUTH);
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.addUser("username", "password", new String[]{"role1"});
        assertTrue(securityManager.authenticate(wrappedRequest, response));
    }

    /**
     * Test login method.
     */
    @Test
    void testLogin() {
        try {
            TestWebApplicationRequest request = new TestWebApplicationRequest();
            TestWebApplicationResponse response = new TestWebApplicationResponse();
            DefaultWebApplication webApp = new DefaultWebApplication();
            DefaultSecurityManager securityManager = new DefaultSecurityManager();
            webApp.linkRequestAndResponse(request, response);
            securityManager.setWebApplication(webApp);
            securityManager.addUser("username", "password", new String[]{"role1", "role2"});
            securityManager.login(request, "username", "password");
        } catch (ServletException ex) {
            fail();
        }
    }

    /**
     * Test login method.
     */
    @Test
    void testLogin2() {
        try {
            TestWebApplicationRequest request = new TestWebApplicationRequest();
            TestWebApplicationResponse response = new TestWebApplicationResponse();
            DefaultWebApplication webApp = new DefaultWebApplication();
            DefaultSecurityManager securityManager = new DefaultSecurityManager();
            webApp.linkRequestAndResponse(request, response);
            securityManager.setWebApplication(webApp);
            securityManager.addUser("username", "password", new String[]{"role1", "role2"});
            securityManager.login(request, "username", "wrong");
            fail();
        } catch (ServletException ex) {
        }
    }

    /**
     * Test login method.
     */
    @Test
    void testLogin3() {
        try {
            TestWebApplicationRequest request = new TestWebApplicationRequest();
            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request);
            TestWebApplicationResponse response = new TestWebApplicationResponse();
            DefaultWebApplication webApp = new DefaultWebApplication();
            DefaultSecurityManager securityManager = new DefaultSecurityManager();
            webApp.linkRequestAndResponse(wrapper, response);
            securityManager.setWebApplication(webApp);
            securityManager.addUser("username", "password", new String[]{"role1", "role2"});
            securityManager.login(wrapper, "username", "password");
        } catch (ServletException ex) {
            fail();
        }
    }

    /**
     * Test removeUser method.
     */
    @Test
    void testRemoveUser() {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
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
    void testGetWebApplication() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        assertNull(securityManager.getWebApplication());
    }

    /**
     * Test getWebApplication method.
     */
    @Test
    void testGetWebApplication2() {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setWebApplication(new DefaultWebApplication());
        assertNotNull(securityManager.getWebApplication());
    }
}
