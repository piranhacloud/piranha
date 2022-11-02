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

import cloud.piranha.resource.impl.DefaultResourceManager;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRegistration.Dynamic;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;
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
class DefaultWebApplicationTest {

    /**
     * Test addJspFile method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddJspFile() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.addJspFile("MyJspFile", "myjspfile.jsp"));
    }

    /**
     * Test addJspFile method
     */
    @Test
    void testAddJspFile2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        assertThrows(IllegalStateException.class, () -> webApp.addJspFile("MyJspFile", "myjspfile.jsp"));
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
     * Test addResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddResource() throws Exception {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setResourceManager(resourceManager);
        webApp.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApp.getResource("/src/main/java"));
    }

    /**
     * Test destroy method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testDestroy() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addListener(new TestWebApplicationDestroyListener());
        webApp.initialize();
        webApp.destroy();
        assertNotNull(webApp.getAttribute("contextDestroyed"));
    }

    /**
     * Test listener to validate the destroy method was called.
     */
    class TestWebApplicationDestroyListener implements ServletContextListener {

        /**
         * Context initialized event.
         *
         * @param event the event.
         */
        @Override
        public void contextInitialized(ServletContextEvent event) {
        }

        /**
         * Context destroyed event.
         *
         * @param event the event.
         */
        @Override
        public void contextDestroyed(ServletContextEvent event) {
            event.getServletContext().setAttribute("contextDestroyed", true);
        }
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
     * Test getAttributeNames method.
     */
    @Test
    void testGetAttributeNames() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getAttributeNames());
    }

    /**
     * Test getContext method.
     */
    @Test
    void testGetContext() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getContext("/does_not_matter"));
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
     * Test getInitParameter method.
     */
    @Test
    void testGetInitParameter() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setInitParameter("initParameter", Boolean.TRUE.toString());
        assertEquals("true", webApp.getInitParameter("initParameter"));
    }

    /**
     * Test getInitParameterNames method.
     */
    @Test
    void testGetInitParameterNames() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setInitParameter("initParameter", Boolean.TRUE.toString());
        Enumeration<String> enumeration = webApp.getInitParameterNames();
        assertEquals("initParameter", enumeration.nextElement());
        assertFalse(enumeration.hasMoreElements());
    }

    /**
     * Test getJspConfigDescriptor method.
     */
    @Test
    void testGetJspConfigDescriptor() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getJspConfigDescriptor());
    }

    /**
     * Test getMajorVersion method.
     */
    @Test
    void testGetMajorVersion() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals(6, webApp.getMajorVersion());
    }

    /**
     * Test getMinorVersion method.
     */
    @Test
    void testGetMinorVersion() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals(0, webApp.getMinorVersion());
    }

    /**
     * Test getNamedDispatcher method.
     */
    @Test
    void testGetNamedDispatcher() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Snoop", TestSnoopServlet.class);
        assertNotNull(webApp.getNamedDispatcher("Snoop"));
    }

    /**
     * Test getRealPath method.
     */
    @Test
    void testGetRealPath() {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setResourceManager(resourceManager);
        assertNull(webApp.getRealPath("index.html"));
    }

    /**
     * Test getRealPath method.
     */
    @Test
    void testGetRealPath2() {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setResourceManager(resourceManager);
        webApp.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApp.getRealPath("/src/main/java"));
    }

    /**
     * Test getRequest method.
     */
    @Test
    void testGetRequest() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        webApp.linkRequestAndResponse(request, response);
        assertNotNull(webApp.getRequest(response));
    }

    /**
     * Test getRequestCharacterEncoding method.
     */
    @Test
    void testGetRequestCharacterEncoding() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getRequestCharacterEncoding());
        webApp.setRequestCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApp.getRequestCharacterEncoding());
    }

    /**
     * Test getRequestDispatcher.
     *
     * @throws Exception
     */
    @Test
    void testGetRequestDispatcher() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        webApp.addServlet("Snoop", TestSnoopServlet.class);
        webApp.addServletMapping("Snoop", "/Snoop");
        assertNotNull(webApp.getRequestDispatcher("/Snoop"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    void testGetResourceAsStream() {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setResourceManager(resourceManager);
        webApp.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApp.getResourceAsStream("/pom.xml"));
    }

    /**
     * Test getResourcePaths method.
     */
    @Test
    void testGetResourcePaths() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getResourcePaths("/this_will_be_null/"));
        assertNull(webApp.getResourcePaths(null));
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
     * Test getResponseCharacterEncoding.
     */
    @Test
    void testGetResponseCharacterEncoding() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getResponseCharacterEncoding());
        webApp.setResponseCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApp.getResponseCharacterEncoding());
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
     * Test getServerInfo method.
     */
    @Test
    void testGetServerInfo() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals("", webApp.getServerInfo());
    }

    /**
     * Test getServletContextName method.
     */
    @Test
    void testGetServletContextName() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setServletContextName("MYNAME");
        assertNotNull(webApp.getServletContextName());
        assertEquals("MYNAME", webApp.getServletContextName());
    }

    /**
     * Test getServletRegistrations method.
     */
    @Test
    void testGetServletRegistrations() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getServletRegistrations());
        assertTrue(webApp.getServletRegistrations().isEmpty());
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
     * Test removeAttribute method.
     */
    @Test
    void testRemoveAttribute() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setAttribute("name", "value");
        assertNotNull(webApp.getAttribute("name"));
        webApp.removeAttribute("name");
        assertNull(webApp.getAttribute("name"));
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
    void testSetAttribute() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setAttribute("myattribute", null);
        assertNull(webApp.getAttribute("myattribute"));
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
     * Test getAttribute method.
     */
    @Test
    void testGetAttribute() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertThrows(NullPointerException.class, () -> webApp.getAttribute(null));
    }

    /**
     * Test setClassLoader method.
     */
    @Test
    void testSetClassLoader() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getClassLoader());
        webApp.setClassLoader(null);
        assertNull(webApp.getClassLoader());
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertTrue(webApp.setInitParameter("name", "value"));
        assertFalse(webApp.setInitParameter("name", "value"));
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        assertThrows(IllegalStateException.class, () -> webApp.setInitParameter("name", "value"));
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertThrows(NullPointerException.class, () -> webApp.setInitParameter(null, "KABOOM"));
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter4() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        assertThrows(NullPointerException.class, () -> webApp.setInitParameter(null, "KABOOM"));
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

    @Test
    void testGetContentType() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        assertNull(response.getContentType());
        response.setContentType("text/html");
        response.getWriter();
        assertEquals("text/html;charset=ISO-8859-1", response.getContentType());
        response.close();
    }
    
    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType() {
        DefaultWebApplication application = new DefaultWebApplication();
        assertNull(application.getMimeType("index.html"));
    }

    /**
     * Test setBufferSize method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetBufferSize() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        try ( ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            response.setUnderlyingOutputStream(baos);
            response.flush();
            assertThrows(IllegalStateException.class, () -> response.setBufferSize(20));
        }
        response.close();
    }
}
