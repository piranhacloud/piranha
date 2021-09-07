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
package cloud.piranha.extension.security.file.tests;

import cloud.piranha.extension.security.file.FileAuthenticationManager;
import cloud.piranha.webapp.impl.DefaultWebApplicationRequest;
import static jakarta.servlet.http.HttpServletRequest.BASIC_AUTH;
import static jakarta.servlet.http.HttpServletRequest.FORM_AUTH;
import java.io.File;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the FileAuthenticationManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class FileAuthenticationManagerTest {

    /**
     * Test authenticate method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testAuthenticate() throws Exception {
        FileAuthenticationManager manager = new FileAuthenticationManager(
                new File("src/test/authmanager/authenticate0/users.properties"));
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setAuthType(BASIC_AUTH);
        byte[] authBytes = "joe:password".getBytes();
        String authBase64 = Base64.getEncoder().encodeToString(authBytes);
        request.setHeader("Authorization", "Basic " + authBase64);
        assertTrue(manager.authenticate(request, null));
    }

    /**
     * Test authenticate method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testAuthenticate2() throws Exception {
        FileAuthenticationManager manager = new FileAuthenticationManager(
                new File("src/test/authmanager/authenticate0/users.properties"));
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setAuthType(FORM_AUTH);
        request.setParameter("j_username", new String[]{"joe"});
        request.setParameter("j_password", new String[]{"password"});
        assertTrue(manager.authenticate(request, null));
    }

    /**
     * Test login method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testLogin() throws Exception {
        FileAuthenticationManager manager = new FileAuthenticationManager(
                new File("src/test/authmanager/authenticate0/users.properties"));
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        manager.login(request, "joe", "password");
    }

    /**
     * Test logout method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testLogout() throws Exception {
        FileAuthenticationManager manager = new FileAuthenticationManager(new File(""));
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        manager.logout(request, null);
    }
}
