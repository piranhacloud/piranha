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
package cloud.piranha.servlet4.impl;

import cloud.piranha.servlet4.impl.DefaultWebApplication;
import cloud.piranha.servlet4.impl.DefaultWebApplicationExtensionContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;

import cloud.piranha.servlet4.webapp.WebApplication;
import cloud.piranha.servlet4.webapp.WebApplicationExtension;
import cloud.piranha.servlet4.webapp.WebApplicationExtensionContext;

/**
 * The JUnit tests for the DefaultWebApplicationExtensionContext.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationExtensionContextTest {

    /**
     * Test add method.
     */
    @Test
    void testAdd() {
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        context.add(TestExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        assertNotNull(webApplication.getAttribute(TestInitializer.class.getName()));
    }

    /**
     * Test remove method.
     */
    @Test
    void testRemove() {
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        context.add(Test3Extension.class);
        context.configure(webApplication);
        webApplication.initialize();
        assertNull(webApplication.getAttribute(TestInitializer.class.getName()));
    }

    /**
     * A test extension.
     */
    static class TestExtension implements WebApplicationExtension {

        /**
         * Configure the web application.
         *
         * @param webApplication the web application.
         */
        @Override
        public void configure(WebApplication webApplication) {
            webApplication.addInitializer(TestInitializer.class.getName());
        }
    }

    /**
     * A test extension.
     */
    static class Test2Extension implements WebApplicationExtension {

        /**
         * Extend the web application.
         *
         * @param context the context.
         */
        @Override
        public void extend(WebApplicationExtensionContext context) {
            context.remove(TestExtension.class);
        }
    }

    /**
     * A test extension.
     */
    static class Test3Extension implements WebApplicationExtension {

        /**
         * Extend the web application.
         *
         * @param context the context.
         */
        @Override
        public void extend(WebApplicationExtensionContext context) {
            context.add(TestExtension.class);
            context.add(Test2Extension.class);
        }
    }

    /**
     * A test servlet container initializer.
     */
    static class TestInitializer implements ServletContainerInitializer {

        /**
         * On startup.
         *
         * @param classes the list of annotated classes.
         * @param servletContext the Servlet context.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
            servletContext.setAttribute(TestInitializer.class.getName(), true);
        }
    }
}
