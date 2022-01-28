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

import cloud.piranha.core.api.ServletEnvironment;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationExtension;
import cloud.piranha.core.impl.DefaultHttpSessionManager;
import cloud.piranha.core.impl.DefaultWebApplication;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the EmbeddedPiranhaBuilder class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class EmbeddedPiranhaBuilderTest {


    /**
     * Test aliasedDirectoryResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAliasedDirectoryResource() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .aliasedDirectoryResource(".", "/myalias")
                .build();
        assertNotNull(piranha.getWebApplication().getResource("/myalias/pom.xml"));
    }
    
    /**
     * Test attribute method.
     */
    @Test
    void testAttribute() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .attribute("name", "value")
                .build();
        assertEquals("value", piranha.getWebApplication().getAttribute("name"));
    }

    /**
     * Test directoryResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testDirectoryResource() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .directoryResource(".")
                .build();
        assertNotNull(piranha.getWebApplication().getResource("/pom.xml"));
    }

    /**
     * Test extension method.
     */
    @Test
    void testExtension() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .extension(TestExtension.class)
                .buildAndStart();
        assertEquals("value", piranha.getWebApplication().getAttribute("name"));
    }

    /**
     * Test extension method.
     */
    @Test
    void testExtensions() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .extensions(TestExtension.class)
                .buildAndStart();
        assertEquals("value", piranha.getWebApplication().getAttribute("name"));
    }

    /**
     * Test filter method.
     */
    @Test
    void testFilter() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .filter("filter", TestFilter.class)
                .build();
        assertNotNull(piranha.getWebApplication().getFilterRegistration("filter"));
    }

    /**
     * Test filter method.
     */
    @Test
    void testFilter2() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .filter("filter", TestFilter.class.getName())
                .build();
        assertNotNull(piranha.getWebApplication().getFilterRegistration("filter"));
    }

    /**
     * Test filterInitParam method.
     */
    @Test
    void testFilterInitParam() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .filter("filter", TestFilter.class)
                .filterInitParam("filter", "name", "value")
                .build();
        assertEquals("value", piranha.getWebApplication().
                getFilterRegistration("filter").getInitParameter("name"));
    }

    /**
     * Test filterMapping method.
     */
    @Test
    void testFilterMapping() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .filter("filter", TestFilter.class)
                .filterMapping("filter", "/filter")
                .build();
        assertEquals("/filter", piranha.getWebApplication()
                .getFilterRegistration("filter").getUrlPatternMappings()
                .iterator().next());
    }

    /**
     * Test httpSessionManager method.
     */
    @Test
    void testHttpSessionManager() {
        DefaultHttpSessionManager httpSessionManager = new DefaultHttpSessionManager();
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .httpSessionManager(httpSessionManager)
                .build();
        assertEquals(httpSessionManager, piranha.getWebApplication()
                .getManager().getHttpSessionManager());
    }

    /**
     * Test initializer method.
     */
    @Test
    void testInitializer() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .initializer(TestInitializer.class)
                .buildAndStart();
        assertTrue((boolean) piranha.getWebApplication().getAttribute("initialized"));
    }

    /**
     * Test initializer method.
     */
    @Test
    void testInitializer2() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .initializer(TestInitializer.class.getName())
                .buildAndStart();
        assertTrue((boolean) piranha.getWebApplication().getAttribute("initialized"));
    }

    /**
     * Test initializers method.
     */
    @Test
    void testInitializers() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .initializers(TestInitializer.class)
                .buildAndStart();
        assertTrue((boolean) piranha.getWebApplication().getAttribute("initialized"));
    }

    /**
     * Test listener method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    void testListener() throws Exception {
        EmbeddedPiranhaBuilder builder = new EmbeddedPiranhaBuilder();
        EmbeddedPiranha piranha = builder
                .listener(TestServletRequestListener.class.getName())
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequest();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        assertTrue((Boolean) piranha.getWebApplication().getAttribute("requestInitialized"));
    }

    /**
     * Test servlet method.
     */
    @Test
    void testServlet() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("servlet", TestServlet.class)
                .build();
        assertNotNull(piranha.getWebApplication().getServletRegistration("servlet"));
    }

    /**
     * Test servlet method.
     */
    @Test
    void testServlet2() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("servlet", TestServlet.class.getName())
                .build();
        assertNotNull(piranha.getWebApplication().getServletRegistration("servlet"));
    }

    /**
     * Test servlet method.
     */
    @Test
    void testServlet3() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("servlet", TestServlet.class, true)
                .build();
        assertNotNull(piranha.getWebApplication().getServletRegistration("servlet"));
        ServletEnvironment servletEnvironment = (ServletEnvironment) piranha
                .getWebApplication().getServletRegistration("servlet");
        assertTrue(servletEnvironment.isAsyncSupported());
    }

    /**
     * Test servlet method.
     */
    @Test
    void testServlet4() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("servlet", TestServlet.class.getName(), true)
                .build();
        assertNotNull(piranha.getWebApplication().getServletRegistration("servlet"));
        ServletEnvironment servletEnvironment = (ServletEnvironment) piranha
                .getWebApplication().getServletRegistration("servlet");
        assertTrue(servletEnvironment.isAsyncSupported());
    }

    /**
     * Test servletInitParam method.
     */
    @Test
    void testServletInitParam() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("servlet", TestServlet.class)
                .servletInitParam("servlet", "name", "value")
                .build();
        assertEquals("value", piranha.getWebApplication().
                getServletRegistration("servlet").getInitParameter("name"));
    }

    /**
     * Test servletMapping method.
     */
    @Test
    void testServletMapping() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("servlet", TestServlet.class)
                .servletMapping("servlet", "/servlet")
                .build();
        assertEquals("/servlet", piranha.getWebApplication()
                .getServletRegistration("servlet").getMappings().iterator()
                .next());
    }

    /**
     * Test servletMapped method.
     */
    @Test
    void testServletMapped() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servletMapped(TestServlet.class, "/servlet")
                .buildAndStart();
        assertEquals("/servlet", piranha.getWebApplication()
                .getServletRegistration("TestServlet").getMappings().iterator()
                .next());
    }

    /**
     * Test servletMapped method.
     */
    @Test
    void testServletMapped2() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servletMapped(TestServlet.class, true, "/servlet")
                .buildAndStart();
        assertNotNull(piranha.getWebApplication().getServletRegistration("TestServlet"));
        ServletEnvironment servletEnvironment = (ServletEnvironment) piranha
                .getWebApplication().getServletRegistration("TestServlet");
        assertTrue(servletEnvironment.isAsyncSupported());
    }

    /**
     * Test servletsMapped method.
     */
    @Test
    void testServletsMapped() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servletsMapped(TestServlet.class, "/servlet")
                .buildAndStart();
        assertEquals("/servlet", piranha.getWebApplication()
                .getServletRegistration("TestServlet").getMappings().iterator()
                .next());
    }

    /**
     * Test servletsMapped method.
     */
    @Test
    void testServletsMapped2() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servletsMapped(TestServlet.class, "/servlet", Test2Servlet.class, "/servlet2")
                .buildAndStart();
        assertEquals("/servlet", piranha.getWebApplication()
                .getServletRegistration("TestServlet").getMappings().iterator()
                .next());
        assertEquals("/servlet2", piranha.getWebApplication()
                .getServletRegistration("Test2Servlet").getMappings().iterator()
                .next());
    }

    /**
     * Test servletsMapped method.
     */
    @Test
    void testServletsMapped3() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servletsMapped(
                        TestServlet.class, "/servlet",
                        Test2Servlet.class, "/servlet2",
                        Test3Servlet.class, "/servlet3")
                .buildAndStart();
        assertEquals("/servlet", piranha.getWebApplication()
                .getServletRegistration("TestServlet").getMappings().iterator()
                .next());
        assertEquals("/servlet2", piranha.getWebApplication()
                .getServletRegistration("Test2Servlet").getMappings().iterator()
                .next());
        assertEquals("/servlet3", piranha.getWebApplication()
                .getServletRegistration("Test3Servlet").getMappings().iterator()
                .next());
    }

    /**
     * Test servletsMapped method.
     */
    @Test
    void testServletsMapped4() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servletsMapped(
                        TestServlet.class, "/servlet",
                        Test2Servlet.class, "/servlet2",
                        Test3Servlet.class, "/servlet3",
                        Test4Servlet.class, "/servlet4")
                .buildAndStart();
        assertEquals("/servlet", piranha.getWebApplication()
                .getServletRegistration("TestServlet").getMappings().iterator()
                .next());
        assertEquals("/servlet2", piranha.getWebApplication()
                .getServletRegistration("Test2Servlet").getMappings().iterator()
                .next());
        assertEquals("/servlet3", piranha.getWebApplication()
                .getServletRegistration("Test3Servlet").getMappings().iterator()
                .next());
        assertEquals("/servlet4", piranha.getWebApplication()
                .getServletRegistration("Test4Servlet").getMappings().iterator()
                .next());
    }

    /**
     * Test servletsMapped method.
     */
    @Test
    void testServletsMapped5() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servletsMapped(
                        TestServlet.class, "/servlet",
                        Test2Servlet.class, "/servlet2",
                        Test3Servlet.class, "/servlet3",
                        Test4Servlet.class, "/servlet4",
                        Test5Servlet.class, "/servlet5")
                .buildAndStart();
        assertEquals("/servlet", piranha.getWebApplication()
                .getServletRegistration("TestServlet").getMappings().iterator()
                .next());
        assertEquals("/servlet2", piranha.getWebApplication()
                .getServletRegistration("Test2Servlet").getMappings().iterator()
                .next());
        assertEquals("/servlet3", piranha.getWebApplication()
                .getServletRegistration("Test3Servlet").getMappings().iterator()
                .next());
        assertEquals("/servlet4", piranha.getWebApplication()
                .getServletRegistration("Test4Servlet").getMappings().iterator()
                .next());
        assertEquals("/servlet5", piranha.getWebApplication()
                .getServletRegistration("Test5Servlet").getMappings().iterator()
                .next());
    }

    /**
     * Test stringResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testStringResource() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .stringResource("/textfile.txt", "This is text")
                .build();
        assertNotNull(piranha.getWebApplication().getResource("/textfile.txt"));
    }

    /**
     * Test webApplication method.
     */
    @Test
    void testWebApplication() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .webApplication(webApplication)
                .build();
        assertEquals(webApplication, piranha.getWebApplication());
    }

    /**
     * A test extension used to test the extension method.
     */
    public static class TestExtension implements WebApplicationExtension {

        @Override
        public void configure(WebApplication webApplication) {
            webApplication.setAttribute("name", "value");
        }
    }

    /**
     * A test filter used to test the filter method.
     */
    public static class TestFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                FilterChain chain) throws IOException, ServletException {
        }
    }

    /**
     * A test initializer used to test the initializer method.
     */
    public static class TestInitializer implements ServletContainerInitializer {

        @Override
        public void onStartup(Set<Class<?>> classes,
                ServletContext servletContext) throws ServletException {
            servletContext.setAttribute("initialized", true);
        }
    }

    /**
     * A test servlet used to test various servlet related methods.
     */
    public static class TestServlet extends HttpServlet {
    }

    /**
     * A test servlet used to test the servletsMapped method.
     */
    public static class Test2Servlet extends HttpServlet {
    }

    /**
     * A test servlet used to test the servletsMapped method.
     */
    public static class Test3Servlet extends HttpServlet {
    }

    /**
     * A test servlet used to test the servletsMapped method.
     */
    public static class Test4Servlet extends HttpServlet {
    }

    /**
     * A test servlet used to test the servletsMapped method.
     */
    public static class Test5Servlet extends HttpServlet {
    }

    /**
     * A test ServletRequestListener used to test the listener method.
     */
    public static class TestServletRequestListener implements ServletRequestListener {

        public TestServletRequestListener() {
        }

        @Override
        public void requestInitialized(ServletRequestEvent event) {
            event.getServletContext().setAttribute("requestInitialized", true);
        }
    }
}
