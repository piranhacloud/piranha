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
package cloud.piranha.embedded;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.junit.jupiter.api.Test;

import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationExtension;

/**
 * The JUnit tests for the EmbeddedPiranha class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class EmbeddedPiranhaTest {

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService() throws Exception {
        EmbeddedRequest request = new EmbeddedRequest();
        EmbeddedResponse response = new EmbeddedResponse();
        EmbeddedPiranha piranha = new EmbeddedPiranha();
        piranha.initialize();
        piranha.start();
        piranha.service(request, response);
        piranha.stop();
    }

    /**
     * Test extension handling.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testExtensionHandling() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .extension(TestExtension.class)
                .buildAndStart();
        assertNotNull(piranha.getWebApplication().getAttribute(TestInitializer.class.getName()));
    }

    /**
     * A test extension.
     */
    public static class TestExtension implements WebApplicationExtension {

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
     * A test servlet container initializer.
     */
    public static class TestInitializer implements ServletContainerInitializer {

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
