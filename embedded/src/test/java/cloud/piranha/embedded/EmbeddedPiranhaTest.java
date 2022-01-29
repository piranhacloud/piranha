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
package cloud.piranha.embedded;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the EmbeddedPiranha class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class EmbeddedPiranhaTest {

    /**
     * Test extension method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testExtension() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .extension(TestExtension.class)
                .buildAndStart();
        assertNotNull(piranha.getWebApplication().getAttribute(TestInitializer.class.getName()));
    }

    /**
     * Test service method.
     */
    @Test
    void testService() {
        try {
            EmbeddedRequest request = new EmbeddedRequest();
            EmbeddedResponse response = new EmbeddedResponse();
            EmbeddedPiranha piranha = new EmbeddedPiranha();
            piranha.initialize();
            piranha.start();
            piranha.service(request, response);
            piranha.stop();
        } catch (IOException | ServletException ex) {
            fail();
        }
    }

    /**
     * Test service method.
     */
    @Test
    void testService2() {
        try {
            EmbeddedRequest request = new EmbeddedRequest();
            EmbeddedPiranha piranha = new EmbeddedPiranha();
            piranha.initialize();
            piranha.start();
            piranha.service(request);
            piranha.stop();
            piranha.destroy();
        } catch (IOException | ServletException ex) {
            fail();
        }
    }

    /**
     * Test service method.
     */
    @Test
    void testService3() {
        try {
            EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                    .servlet("TestServlet", TestServlet.class)
                    .servletMapping("TestServlet", "/servletPath")
                    .buildAndStart();
            EmbeddedResponse response = piranha.service("/servletPath");
            piranha.stop();
            piranha.destroy();
            assertTrue(response.getResponseAsString().contains("/servletPath"));
        } catch (IOException | ServletException ex) {
            fail();
        }
    }

    /**
     * Test service method.
     */
    @Test
    void testService4() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("TestServlet", TestServlet.class)
                .servletMapping("TestServlet", "/servletPath")
                .buildAndStart();
        try {
            piranha.service("/servletPath", "wrong");
        } catch (IOException | ServletException ex) {
            fail();
        } catch (IllegalStateException ise) {
        }
        piranha.stop();
        piranha.destroy();
    }

    /**
     * Test service method.
     */
    @Test
    void testService5() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("TestServlet", TestServlet.class)
                .servletMapping("TestServlet", "/servletPath")
                .buildAndStart();
        try {
            EmbeddedResponse response = piranha.service("/servletPath", "name", "value");
            assertTrue(response.getResponseAsString().contains("/servletPath"));
            assertTrue(response.getResponseAsString().contains("name: value"));
        } catch (IOException | ServletException ex) {
            fail();
        } catch (IllegalStateException ise) {
        }
        piranha.stop();
        piranha.destroy();
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
     * A test ServletContainerInitializer.
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

    /**
     * A test Servlet.
     */
    public static class TestServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setContentType("text/plain");
            PrintWriter writer = response.getWriter();
            writer.println(request.getServletPath());
            for (Entry<String, String[]> en : request.getParameterMap().entrySet()) {
                String key = en.getKey();
                String[] values = en.getValue();
                writer.println(key + ": " + values[0]);
            }
        }
    }
}
