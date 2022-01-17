/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.slim.security;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import cloud.piranha.core.impl.DefaultWebApplicationResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import static jakarta.servlet.http.HttpServletRequest.BASIC_AUTH;
import static jakarta.servlet.http.HttpServletRequest.CLIENT_CERT_AUTH;
import static jakarta.servlet.http.HttpServletRequest.DIGEST_AUTH;
import static jakarta.servlet.http.HttpServletRequest.FORM_AUTH;
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
 * The JUnit tests for the SlimSecurityManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class SlimSecurityManagerTest {

    /**
     * Test addUser method.
     */
    @Test
    void testAddUser() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setUserPrincipal(new SlimSecurityManagerPrincipal("username"));
        SlimSecurityManager securityManager = new SlimSecurityManager();
        securityManager.addUser("username", "password", new String[]{"role1", "role2"});
        assertTrue(securityManager.isUserInRole(request, "role1"));
    }
    
    /**
     * Test addUserRole method.
     */
    @Test
    void testAddUserRole() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setUserPrincipal(new SlimSecurityManagerPrincipal("username"));
        SlimSecurityManager securityManager = new SlimSecurityManager();
        securityManager.addUser("username", "password");
        securityManager.addUserRole("username", new String[]{"role1", "role2"});
        assertTrue(securityManager.isUserInRole(request, "role1"));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because BASIC authentication is not supported.
     */
    @Test
    void testAuthenticate() throws ServletException {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        request.setAuthType(BASIC_AUTH);
        SlimSecurityManager securityManager = new SlimSecurityManager();
        assertNotNull(assertThrows(ServletException.class, 
                () -> securityManager.authenticate(request, response)));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because CLIENT_CERT_AUTH authentication is not
     * supported.
     */
    @Test
    void testAuthenticate2() throws ServletException {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        request.setAuthType(CLIENT_CERT_AUTH);
        SlimSecurityManager securityManager = new SlimSecurityManager();
        assertNotNull(assertThrows(ServletException.class, 
                () -> securityManager.authenticate(request, response)));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because DIGEST_AUTH authentication is not
     * supported.
     */
    @Test
    void testAuthenticate3() throws ServletException {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        request.setAuthType(DIGEST_AUTH);
        SlimSecurityManager securityManager = new SlimSecurityManager();
        assertNotNull(assertThrows(ServletException.class, 
                () -> securityManager.authenticate(request, response)));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException because DIGEST_AUTH authentication is not
     * supported.
     */
    @Test
    void testAuthenticate4() throws ServletException {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        request.setAuthType(HttpServletRequest.FORM_AUTH);
        SlimSecurityManager securityManager = new SlimSecurityManager();
        assertNotNull(assertThrows(ServletException.class, 
                () -> securityManager.authenticate(request, response)));
    }

    /**
     * Test authenticate method.
     *
     * @throws ServletException when a servlet error occurs.
     */
    @Test
    void testAuthenticate5() throws ServletException, IOException {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setParameter("j_username", new String[]{"username"});
        request.setParameter("j_password", new String[]{"password"});
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        request.setAuthType(FORM_AUTH);
        SlimSecurityManager securityManager = new SlimSecurityManager();
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
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setParameter("j_username", new String[]{"username"});
        request.setParameter("j_password", new String[]{"password"});
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        request.setAuthType(FORM_AUTH);
        SlimSecurityManager securityManager = new SlimSecurityManager();
        securityManager.addUser("username", "password", new String[]{"role1"});
        assertTrue(securityManager.authenticate(wrappedRequest, response));
    }

    /**
     * Test declareRoles method.
     */
    @Test
    void testDeclareRoles() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        SlimSecurityManager manager = new SlimSecurityManager();
        webApp.getManager().setSecurityManager(manager);
        webApp.declareRoles(new String[]{"ADMIN", "USER"});
        assertTrue(manager.getRoles().contains("ADMIN"));
        assertTrue(manager.getRoles().contains("USER"));
    }

    /**
     * Test login method.
     */
    @Test
    void testLogin() {
        try {
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
            DefaultWebApplication webApp = new DefaultWebApplication();
            SlimSecurityManager securityManager = new SlimSecurityManager();
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
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
            DefaultWebApplication webApp = new DefaultWebApplication();
            SlimSecurityManager securityManager = new SlimSecurityManager();
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
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request);
            DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
            DefaultWebApplication webApp = new DefaultWebApplication();
            SlimSecurityManager securityManager = new SlimSecurityManager();
            webApp.linkRequestAndResponse(wrapper, response);
            securityManager.setWebApplication(webApp);
            securityManager.addUser("username", "password", new String[]{"role1", "role2"});
            securityManager.login(wrapper, "username", "password");
        } catch (ServletException ex) {
            fail();
        }
    }

    /**
     * Test logout method.
     */
    @Test
    void testLogout() {
        try {
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
            DefaultWebApplication webApp = new DefaultWebApplication();
            SlimSecurityManager securityManager = new SlimSecurityManager();
            webApp.linkRequestAndResponse(request, response);
            securityManager.setWebApplication(webApp);
            securityManager.addUser("username", "password", new String[]{"role1", "role2"});
            securityManager.login(request, "username", "password");
            securityManager.logout(request, response);
            assertNull(request.getUserPrincipal());
        } catch (ServletException ex) {
            fail();
        }
    }

    /**
     * Test logout method.
     */
    @Test
    void testLogout2() {
        try {
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request);
            DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
            DefaultWebApplication webApp = new DefaultWebApplication();
            SlimSecurityManager securityManager = new SlimSecurityManager();
            webApp.linkRequestAndResponse(wrapper, response);
            securityManager.setWebApplication(webApp);
            securityManager.addUser("username", "password", new String[]{"role1", "role2"});
            securityManager.login(wrapper, "username", "password");
            securityManager.logout(wrapper, response);
            assertNull(request.getUserPrincipal());
        } catch (ServletException ex) {
            fail();
        }
    }

    /**
     * Test logout method.
     */
    @Test
    void testLogout3() {
        try {
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
            DefaultWebApplication webApp = new DefaultWebApplication();
            SlimSecurityManager securityManager = new SlimSecurityManager();
            webApp.linkRequestAndResponse(request, response);
            securityManager.setWebApplication(webApp);
            securityManager.logout(request, response);
            fail();
        } catch (ServletException ex) {
            // nothing to do here.
        }
    }

    /**
     * Test removeUser method.
     */
    @Test
    void testRemoveUser() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setUserPrincipal(new SlimSecurityManagerPrincipal("username"));
        SlimSecurityManager securityManager = new SlimSecurityManager();
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
        SlimSecurityManager securityManager = new SlimSecurityManager();
        assertNull(securityManager.getWebApplication());
    }

    /**
     * Test getWebApplication method.
     */
    @Test
    void testGetWebApplication2() {
        SlimSecurityManager securityManager = new SlimSecurityManager();
        securityManager.setWebApplication(new DefaultWebApplication());
        assertNotNull(securityManager.getWebApplication());
    }
}
