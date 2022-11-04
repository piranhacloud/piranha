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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.resource.impl.DefaultResourceManager;
import cloud.piranha.resource.impl.DirectoryResource;
import cloud.piranha.core.tests.WebApplicationTest;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRegistration.Dynamic;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultWebApplication class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationTest extends WebApplicationTest {

    @Override
    protected WebApplication createWebApplication() {
        return new DefaultWebApplication();
    }

    @Override
    protected WebApplicationRequest createWebApplicationRequest() {
        return new DefaultWebApplicationRequest();
    }

    @Override
    protected WebApplicationResponse createWebApplicationResponse() {
        DefaultWebApplicationResponse  response = new DefaultWebApplicationResponse();
        response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        return response;
    }

    /**
     * Test addMapping method (verify the # of mappings > 0).
     */
    @Test
    void testAddMapping() {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        ServletRegistration.Dynamic dynamic
                = webApp.addServlet("echo", "servlet.EchoServlet");
        assertNotNull(dynamic);
        dynamic.addMapping("/echo");
        assertTrue(dynamic.getMappings().size() > 0);
    }

    /**
     * Test addMapping method (verify when we add twice addMapping will return a
     * empty set).
     */
    @Test
    void testAddMapping2() {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        ServletRegistration.Dynamic dynamic = webApp.addServlet("echo", "servlet.EchoServlet");
        assertNotNull(dynamic);
        dynamic.addMapping("/echo");
        assertTrue(dynamic.getMappings().size() > 0);
        assertEquals(0, dynamic.addMapping("/echo").size());
    }

    /**
     * Test getAsync.
     *
     * @throws Exception
     */
    @Test
    void testGetAsync() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        Dynamic registration = webApp.addServlet("Chat", TestChat1Servlet.class);
        registration.setAsyncSupported(true);
        webApp.addServletMapping("Chat", "/chat");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/chat");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.service(request, response);
        assertNotNull(response.getResponseBytes());
        request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setAsyncSupported(true);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"login"});
        request.setParameter("name", new String[]{"username"});
        response = new TestWebApplicationResponse();
        webApp.service(request, response);
        assertNotNull(response.getResponseBytes());
        request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"post"});
        request.setParameter("name", new String[]{"username"});
        request.setParameter("message", new String[]{new Date().toString()});
        response = new TestWebApplicationResponse();
        webApp.service(request, response);
        assertNotNull(response.getResponseBytes());
    }

    /**
     * Test getAsync.
     *
     * @throws Exception
     */
    @Test
    void testGetAsync2() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        Dynamic registration = webApp.addServlet("Chat", TestChat2Servlet.class);
        registration.setAsyncSupported(true);
        webApp.addServletMapping("Chat", "/chat");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/chat");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.service(request, response);
        assertNotNull(response.getResponseBytes());
        request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"login"});
        request.setParameter("name", new String[]{"username"});
        response = new TestWebApplicationResponse();
        webApp.service(request, response);
        assertNotNull(response.getResponseBytes());
        request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"post"});
        request.setParameter("name", new String[]{"username"});
        request.setParameter("message", new String[]{new Date().toString()});
        response = new TestWebApplicationResponse();
        webApp.service(request, response);
        assertNotNull(response.getResponseBytes());
    }

    /**
     * Test getObjectInstanceManager method.
     */
    @Test
    void testGetObjectInstanceManager() {
        DefaultWebApplication application = new DefaultWebApplication();
        assertNotNull(application.getManager().getObjectInstanceManager());
        application.getManager().setObjectInstanceManager(null);
        assertNull(application.getManager().getObjectInstanceManager());
    }

    /**
     * Test getEffectiveMajorVersion method.
     */
    @Test
    void testGetEffectiveMajorVersion() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals(6, webApp.getEffectiveMajorVersion());
    }

    /**
     * Test getEffectiveMinorVersion method.
     */
    @Test
    void testGetEffectiveMinorVersion() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals(0, webApp.getEffectiveMinorVersion());
    }

    /**
     * Test getJspConfigDescriptor method.
     */
    @Test
    void testGetJspConfigDescriptor() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getJspConfigDescriptor());
    }

    @Test
    void testGetResourcePaths2() {
        // Simulating the Javadoc example of the getResourcePaths method
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setResourceManager(resourceManager);
        webApp.addResource(new DirectoryResource("src/test/webapp/resourcepaths"));

        Set<String> resourcePathsRoot = webApp.getResourcePaths("/");
        assertNotNull(resourcePathsRoot);
        assertTrue(resourcePathsRoot.contains("/catalog/"));
        assertTrue(resourcePathsRoot.contains("/customer/"));
        assertTrue(resourcePathsRoot.contains("/welcome.html"));

        Set<String> resourcePathsCatalog = webApp.getResourcePaths("/catalog/");
        assertNotNull(resourcePathsCatalog);
        assertTrue(resourcePathsCatalog.contains("/catalog/offers/"));
        assertTrue(resourcePathsCatalog.contains("/catalog/products.html"));
        assertTrue(resourcePathsCatalog.contains("/catalog/index.html"));

        Set<String> resourcePathsCatalogOffers = webApp.getResourcePaths("/catalog/offers");
        assertNotNull(resourcePathsCatalogOffers);
        assertTrue(resourcePathsCatalogOffers.contains("/catalog/offers/books.html"));
        assertTrue(resourcePathsCatalogOffers.contains("/catalog/offers/music.html"));
    }

    @Test
    void testGetResourcePaths3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertThrows(IllegalArgumentException.class, () -> webApp.getResourcePaths(""));
    }

    @Test
    void testGetResourcePaths4() {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setResourceManager(resourceManager);
        webApp.addResource(new DirectoryResource("src/test/webapp/resourcepaths"));

        assertNull(webApp.getResourcePaths("/welcome.html"));
        assertNull(webApp.getResourcePaths("/catalog/products.html"));
        assertNull(webApp.getResourcePaths("/catalog/offers/books.html"));
    }

    /**
     * Test getSecurityManager.
     */
    @Test
    void testGetSecurityManager() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getManager().getSecurityManager());
    }

    /**
     * Test getVirtualServerName method.
     */
    @Test
    void testGetVirtualServerName() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setVirtualServerName("myname");
        assertEquals("myname", webApp.getVirtualServerName());
    }

    /**
     * Test include.
     *
     * @throws Exception when a serious error occurred.
     */
    @Test
    void testInclude() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Include", TestIncludeServlet.class);
        webApp.addServletMapping("Include", "/include");
        webApp.addServlet("Include2", TestInclude2Servlet.class);
        webApp.addServletMapping("Include2", "/include2");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/include");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.service(request, response);
        assertTrue(new String(response.getResponseBytes()).contains("This was included"));
    }

    /**
     * Test include.
     *
     * @throws Exception when a serious error occurred.
     */
    @Test
    void testInclude2() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Include", TestIncludeServlet.class);
        webApp.addServletMapping("Include", "/include");
        webApp.addServlet("Include2", TestInclude2Servlet.class);
        webApp.addServletMapping("Include2", "/include2");
        webApp.addServlet("Include", TestIncludeServlet.class);
        webApp.addServletMapping("Include", "/include");
        webApp.addServlet("Include3", TestInclude3Servlet.class);
        webApp.addServletMapping("Include3", "/include3");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/include3");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.service(request, response);
        assertTrue(new String(response.getResponseBytes()).contains("This was included"));
    }

    /**
     * Test include.
     *
     * @throws Exception when a serious error occurred.
     */
    @Test
    void testInclude3() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Include", TestIncludeServlet.class);
        webApp.addServletMapping("Include", "/include");
        webApp.addServlet("Include2", TestInclude2Servlet.class);
        webApp.addServletMapping("Include2", "/include2");
        webApp.addServlet("Include", TestIncludeServlet.class);
        webApp.addServletMapping("Include", "/include");
        webApp.addServlet("Include3", TestInclude3Servlet.class);
        webApp.addServletMapping("Include3", "/include3");
        webApp.addServlet("Include4", TestInclude4Servlet.class);
        webApp.addServletMapping("Include4", "/include4");
        webApp.initialize();
        webApp.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/include4");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.service(request, response);
        assertTrue(new String(response.getResponseBytes()).contains("This was includedThis was included"));
    }

    /**
     * Test initialize, start and stop methods.
     */
    @Test
    void testInitializeStartAndStop() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        webApp.stop();
        webApp.destroy();
        try {
            webApp.start();
            fail();
        } catch (RuntimeException e) {
        }
    }

    /**
     * Test service method (ServletRequestListeners)
     *
     * @throws Exception
     */
    @Test
    void testService2() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/Snoop");
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);
        webApp.addServletMapping("Snoop", "/Snoop");
        webApp.addServlet("DefaultServlet", DefaultServlet.class.getName());
        webApp.addServletMapping("DefaultServlet", "/*");

        webApp.initialize();
        webApp.start();
        webApp.service(request, response);
        assertEquals(404, response.getStatus());
    }

    class TrackServletRequestListener implements ServletRequestListener {

        StringBuilder trackCalls = new StringBuilder();

        @Override
        public void requestDestroyed(ServletRequestEvent sre) {
            trackCalls.append("requestDestroyed,");
        }

        @Override
        public void requestInitialized(ServletRequestEvent sre) {
            trackCalls.append("requestInitialized,");
        }
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService3() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(new DefaultWebApplicationRequestMapper());
        webApp.addServletMapping("Snoop", "/Snoop");
        webApp.addServlet("DefaultServlet", DefaultServlet.class.getName());
        webApp.addServletMapping("DefaultServlet", "/*");

        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/Snoop");

        TestWebApplicationResponse response = new TestWebApplicationResponse();
        response.setWebApplication(webApp);

        webApp.initialize();
        webApp.start();
        webApp.service(request, response);
        assertEquals(404, response.getStatus());
    }

    /**
     * Test setAttribute method.
     */
    @Test
    void testSetAttribute2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertThrows(NullPointerException.class, () -> webApp.setAttribute(null, "KABOOM"));
    }

    /**
     * Test setLoggingManager method.
     */
    @Test
    void testSetLoggingManager() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setLoggingManager(null);
        webApplication.log("KABOOM");
        assertNull(webApplication.getManager().getLoggingManager());
    }
}
