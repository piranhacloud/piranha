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
package cloud.piranha.extension.security.file;

import cloud.piranha.core.api.SecurityManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.impl.DefaultSecurityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * The ServletContainerInitializer that is used to bootstrap the
 * DefaultSecurityManager using information from a user.properties and
 * userrole.properties file.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class FileSecurityInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext)
            throws ServletException {

        WebApplication webApplication = (WebApplication) servletContext;
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        webApplication.setManager(SecurityManager.class, securityManager);

        File userFile = new File("user.properties");
        if (!userFile.exists()) {
            userFile = new File("etc/user.properties");
        }
        if (userFile.exists()) {
            Properties userProperties = new Properties();
            try ( FileInputStream fileInput = new FileInputStream(userFile)) {
                userProperties.load(fileInput);
            } catch (IOException ioe) {
            }
            userProperties.entrySet().forEach(entry -> {
                String username = (String) entry.getKey();
                String password = (String) entry.getValue();
                securityManager.addUser(username, password);
            });
        }

        File userRoleFile = new File("userrole.properties");
        if (!userRoleFile.exists()) {
            userRoleFile = new File("etc/userrole.properties");
        }
        if (userRoleFile.exists()) {
            Properties userRoleProperties = new Properties();
            try ( FileInputStream fileInput = new FileInputStream(userRoleFile)) {
                userRoleProperties.load(fileInput);
            } catch (IOException ioe) {
            }
            userRoleProperties.entrySet().forEach(entry -> {
                String username = (String) entry.getKey();
                String roles = (String) entry.getValue();
                securityManager.addUserRole(username, roles.split(","));
            });

        }

        webApplication.setAuthenticationManager(new FileAuthenticationManager(userFile));
        FilterRegistration.Dynamic dynamic = webApplication.addFilter(
                FileAuthenticationFilter.class.getName(),
                FileAuthenticationFilter.class);
        dynamic.setAsyncSupported(true);
        webApplication.addFilterMapping(
                FileAuthenticationFilter.class.getName(),
                "/*");
    }
}
