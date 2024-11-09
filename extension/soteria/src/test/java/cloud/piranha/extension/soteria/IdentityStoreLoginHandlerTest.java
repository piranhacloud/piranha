/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.soteria;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationExtensionContext;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import cloud.piranha.extension.herring.HerringExtension;
import cloud.piranha.extension.herring.HerringInitialContextFactory;
import cloud.piranha.extension.scinitializer.ServletContainerInitializerExtension;
import cloud.piranha.extension.weld.WeldExtension;
import cloud.piranha.resource.impl.AliasedDirectoryResource;
import cloud.piranha.resource.impl.DirectoryResource;
import java.io.File;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;

/**
 * The JUnit test for the IdentityStoreLoginHandler class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class IdentityStoreLoginHandlerTest {

    /**
     * Test login method with correct username / password.
     */
    @Test
    public void testLoginWithCorrectUsernameAndPassword() {

        /*
         * Setup web application with JNDI and Weld.
         */
        System.getProperties().put(INITIAL_CONTEXT_FACTORY, HerringInitialContextFactory.class.getName());
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource("src/test/webapp"));
        webApplication.addResource(new AliasedDirectoryResource(
                new File("target/test-classes"), "/WEB-INF/classes"));
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(HerringExtension.class);
        context.add(WeldExtension.class);
        context.add(ServletContainerInitializerExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        webApplication.start();

        /*
         * Validate the test_user / test_password are correct.
         */
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        String username = "test_user";
        String password = "test_password";
        IdentityStoreLoginHandler handler = new IdentityStoreLoginHandler();
        assertNotNull(handler.login(request, username, password));
        
        /*
         * Shutdown and cleanup.
         */
        webApplication.stop();
        System.getProperties().remove(INITIAL_CONTEXT_FACTORY);
    }

    /**
     * Test login method with incorrect username / password.
     */
    @Test
    public void testLoginWithIncorrectUsernameAndPassword() {

        /*
         * Setup web application with JNDI and Weld.
         */
        System.getProperties().put(INITIAL_CONTEXT_FACTORY, HerringInitialContextFactory.class.getName());
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource("src/test/webapp"));
        webApplication.addResource(new AliasedDirectoryResource(
                new File("target/test-classes"), "/WEB-INF/classes"));
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(HerringExtension.class);
        context.add(WeldExtension.class);
        context.add(ServletContainerInitializerExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        webApplication.start();

        /*
         * Validate the invalid_user / invalid_password are incorrect.
         */
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        String username = "invalid_user";
        String password = "invalid_password";
        IdentityStoreLoginHandler handler = new IdentityStoreLoginHandler();
        assertNull(handler.login(request, username, password));
        
        /*
         * Shutdown and cleanup.
         */
        webApplication.stop();
        System.getProperties().remove(INITIAL_CONTEXT_FACTORY);
    }
}
