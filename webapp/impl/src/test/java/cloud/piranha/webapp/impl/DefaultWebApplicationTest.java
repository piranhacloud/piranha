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
package cloud.piranha.webapp.impl;

import cloud.piranha.resource.DefaultResourceManager;
import cloud.piranha.resource.DirectoryResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Date;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultWebApplication class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationTest {

    /**
     * Test addJspFile method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAddJspFile() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.addJspFile("MyJspFile", "myjspfile.jsp"));
    }

    /**
     * Test addMapping method (verify the # of mappings > 0).
     */
    @Test
    public void testAddMapping() {
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
     * non-empty set).
     */
    @Test
    public void testAddMapping2() {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        ServletRegistration.Dynamic dynamic
                = webApp.addServlet("echo", "servlet.EchoServlet");
        assertNotNull(dynamic);
        dynamic.addMapping("/echo");
        assertTrue(dynamic.getMappings().size() > 0);
        assertTrue(dynamic.addMapping("/echo").size() > 0);
    }

    /**
     * Test addResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAddResource() throws Exception {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setResourceManager(resourceManager);
        webApp.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApp.getResource("/src/main/java"));
    }

    /**
     * Test declareRoles method.
     */
    @Test
    public void testDeclareRoles() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSecurityManager(new DefaultSecurityManager());
        webApp.declareRoles(new String[]{"ADMIN", "USER"});
    }

    /**
     * Test destroy method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testDestroy() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addListener(new TestWebApplicationDestroyListener());
        webApp.initialize();
        webApp.destroy();
        assertNotNull(webApp.getAttribute("contextDestroyed"));
    }

    /**
     * Test listener to validate the destroy method was called.
     */
    public class TestWebApplicationDestroyListener implements ServletContextListener {

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
    public void testGetAsync() throws Exception {
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
    public void testGetAsync2() throws Exception {
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
    public void testGetAttributeNames() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getAttributeNames());
    }

    /**
     * Test getContext method.
     */
    @Test
    public void testGetContext() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getContext("/does_not_matter"));
    }

    /**
     * Test getDefaultSessionTrackingModes method.
     */
    @Test
    public void testGetDefaultSessionTrackingModes() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertTrue(webApp.getDefaultSessionTrackingModes().contains(SessionTrackingMode.COOKIE));
    }

    /**
     * Test getDependencyInjectionManager method.
     */
    @Test
    public void testGetDependencyInjectionManager() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getObjectInstanceManager());
        webApp.setObjectInstanceManager(null);
        assertNull(webApp.getObjectInstanceManager());
    }

    /**
     * Test getDispatcherType.
     *
     * @throws Exception
     */
    @Test
    public void testGetDispatcherType() throws Exception {
        HttpServletRequest request = new TestWebApplicationRequest();
        assertEquals(DispatcherType.REQUEST, request.getDispatcherType());
    }

    /**
     * Test getEffectiveMajorVersion method.
     */
    @Test
    public void testGetEffectiveMajorVersion() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals(4, webApp.getEffectiveMajorVersion());
    }

    /**
     * Test getEffectiveMinorVersion method.
     */
    @Test
    public void testGetEffectiveMinorVersion() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals(0, webApp.getEffectiveMinorVersion());
    }

    /**
     * Test getEffectiveSessionTrackingModes method.
     */
    @Test
    public void testGetEffectiveSessionTrackingModes() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        Set<SessionTrackingMode> trackingModes = EnumSet.of(SessionTrackingMode.URL);
        webApp.setSessionTrackingModes(trackingModes);
        assertTrue(webApp.getEffectiveSessionTrackingModes().contains(SessionTrackingMode.URL));
    }

    /**
     * Test getInitParameter method.
     */
    @Test
    public void testGetInitParameter() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setInitParameter("initParameter", Boolean.TRUE.toString());
        assertEquals("true", webApp.getInitParameter("initParameter"));
    }

    /**
     * Test getInitParameterNames method.
     */
    @Test
    public void testGetInitParameterNames() {
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
    public void testGetJspConfigDescriptor() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getJspConfigDescriptor());
    }

    /**
     * Test getMajorVersion method.
     */
    @Test
    public void testGetMajorVersion() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals(4, webApp.getMajorVersion());
    }

    /**
     * Test getMimeType method.
     */
    @Test
    public void testGetMimeType() {
        DefaultMimeTypeManager mimeTypeManager = new DefaultMimeTypeManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setMimeTypeManager(mimeTypeManager);
        assertNull(webApp.getMimeType("this_maps_to.null"));
    }

    /**
     * Test getMimeType method.
     */
    @Test
    public void testGetMimeType2() {
        DefaultMimeTypeManager mimeTypeManager = new DefaultMimeTypeManager();
        mimeTypeManager.addMimeType("class", "application/x-java-class");
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setMimeTypeManager(mimeTypeManager);
        assertEquals(webApp.getMimeType("my.class"), "application/x-java-class");
    }

    /**
     * Test getMinorVersion method.
     */
    @Test
    public void testGetMinorVersion() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals(0, webApp.getMinorVersion());
    }

    /**
     * Test getNamedDispatcher method.
     */
    @Test
    public void testGetNamedDispatcher() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Snoop", TestSnoopServlet.class);
        assertNotNull(webApp.getNamedDispatcher("Snoop"));
    }

    /**
     * Test getParameterMap.
     *
     * @throws Exception
     */
    @Test
    public void testGetParameterMap() throws Exception {
        HttpServletRequest request = new TestWebApplicationRequest();
        assertNotNull(request.getParameterMap());
        assertNotNull(request.getParameterNames());
    }

    /**
     * Test getRealPath method.
     */
    @Test
    public void testGetRealPath() {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setResourceManager(resourceManager);
        assertNull(webApp.getRealPath("index.html"));
    }

    /**
     * Test getRealPath method.
     */
    @Test
    public void testGetRealPath2() {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setResourceManager(resourceManager);
        webApp.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApp.getRealPath("/src/main/java"));
    }

    /**
     * Test getRequest method.
     */
    @Test
    public void testGetRequest() {
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
    public void testGetRequestCharacterEncoding() {
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
    public void testGetRequestDispatcher() throws Exception {
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
    public void testGetResourceAsStream() {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setResourceManager(resourceManager);
        webApp.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApp.getResourceAsStream("/pom.xml"));
    }

    /**
     * Test getResourcePaths method.
     */
    @Test
    public void testGetResourcePaths() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getResourcePaths("/this_will_be_null/"));
        assertNull(webApp.getResourcePaths(null));
    }

    @Test
    public void testGetResourcePaths2() {
        // Simulating the Javadoc example of the getResourcePaths method
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setResourceManager(resourceManager);
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

    @Test(expected = IllegalArgumentException.class)
    public void testGetResourcePaths3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getResourcePaths("");
    }

    @Test
    public void testGetResourcePaths4() {
        DefaultResourceManager resourceManager = new DefaultResourceManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setResourceManager(resourceManager);
        webApp.addResource(new DirectoryResource("src/test/webapp/resourcepaths"));

        assertNull(webApp.getResourcePaths("/welcome.html"));
        assertNull(webApp.getResourcePaths("/catalog/products.html"));
        assertNull(webApp.getResourcePaths("/catalog/offers/books.html"));
    }

    /**
     * Test getResponseCharacterEncoding.
     */
    @Test
    public void testGetResponseCharacterEncoding() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getResponseCharacterEncoding());
        webApp.setResponseCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApp.getResponseCharacterEncoding());
    }

    /**
     * Test getSecurityManager.
     */
    @Test
    public void testGetSecurityManager() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        try {
            webApp.getSecurityManager();
        } catch (Exception exception) {
        }
    }

    /**
     * Test getSecurityManager.
     */
    @Test
    public void testGetSecurityManager2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSecurityManager(new DefaultSecurityManager());
        assertNotNull(webApp.getSecurityManager());
    }

    /**
     * Test getServerInfo method.
     */
    @Test
    public void testGetServerInfo() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals("", webApp.getServerInfo());
    }

    /**
     * Test getServlets method.
     *
     * @throws Exception
     */
    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("deprecation")
    public void testGetServlet() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getServlet("always_null");
    }

    /**
     * Test getServletContextName method.
     */
    @Test
    public void testGetServletContextName() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setServletContextName("MYNAME");
        assertNotNull(webApp.getServletContextName());
        assertEquals("MYNAME", webApp.getServletContextName());
    }

    /**
     * Test getServletRegistrations method.
     */
    @Test
    public void testGetServletRegistrations() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getServletRegistrations());
        assertTrue(webApp.getServletRegistrations().isEmpty());
    }

    /**
     * Test getServletNames method.
     */
    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("deprecation")
    public void testGetServletNames() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getServletNames();
    }

    /**
     * Test getServlets method.
     */
    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("deprecation")
    public void testGetServlets() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getServlets();
    }

    /**
     * Test getSession.
     *
     * @throws Exception
     */
    @Test
    public void testGetSession() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        ServletRegistration.Dynamic dynamic = webApp.addServlet("session", "cloud.piranha.TestSessionServlet");
        assertNotNull(dynamic);
        dynamic.addMapping("/session");
        webApp.initialize();
        webApp.start();

        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/session");
        TestWebApplicationResponse response = new TestWebApplicationResponse();

        webApp.service(request, response);

        assertNotNull(response.getResponseBytes());
    }

    /**
     * Test getSessionCookieConfig method.
     */
    @Test
    public void testGetSessionCookieConfig() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getSessionCookieConfig());
    }

    /**
     * Test getSessionManager method.
     */
    @Test
    public void testGetSessionManager() {
        try {
            DefaultWebApplication webApp = new DefaultWebApplication();
            webApp.setHttpSessionManager(null);
            webApp.getHttpSessionManager();
        } catch (IllegalStateException exception) {
        }
    }

    /**
     * Test getSessionManager method.
     */
    @Test
    public void testGetSessionManager2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getHttpSessionManager());
    }

    /**
     * Test getVirtualServerName method.
     */
    @Test
    public void testGetVirtualServerName() {
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
    public void testInclude() throws Exception {
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
    public void testInclude2() throws Exception {
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
    public void testInclude3() throws Exception {
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
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testInitializeStartAndStop() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        webApp.stop();
        webApp.destroy();

        try {
            webApp.start();
        } catch (RuntimeException exception) {
        }
    }

    /**
     * Test log method.
     */
    @Test
    public void testLog() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.log("TEST");
    }

    /**
     * Test log method.
     */
    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("deprecation")
    public void testLog2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.log(new IllegalStateException(), "TEST");
    }

    /**
     * Test log method.
     */
    @Test
    public void testLog3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.log("TEST", new RuntimeException());
    }

    /**
     * Test login.
     *
     * @throws Exception
     */
    @Test
    public void testLogin() throws Exception {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSecurityManager(securityManager);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        try {
            request.login("admin", "password");
        } catch (ServletException exception) {
        }
    }

    /**
     * Test logout.
     *
     * @throws Exception
     */
    @Test
    public void testLogout() throws Exception {
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setSecurityManager(securityManager);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        try {
            request.logout();
        } catch (ServletException exception) {
        }
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    public void testRemoveAttribute() {
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
    public void testService2() throws Exception {
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

    public class TrackServletRequestListener implements ServletRequestListener {

        public StringBuilder trackCalls = new StringBuilder();

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
    public void testService3() throws Exception {
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
    public void testSetAttribute() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setAttribute("myattribute", null);
        assertNull(webApp.getAttribute("myattribute"));
    }

    /**
     * Test setClassLoader method.
     */
    @Test
    public void testSetClassLoader() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getClassLoader());
        webApp.setClassLoader(null);
        assertNull(webApp.getClassLoader());
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    public void testSetInitParameter() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertTrue(webApp.setInitParameter("name", "value"));
        assertFalse(webApp.setInitParameter("name", "value"));
    }

    /**
     * Test setInitParameter method.
     */
    @Test(expected = IllegalStateException.class)
    public void testSetInitParameter2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        webApp.setInitParameter("name", "value");
    }

    /**
     * Test setLoggingManager method.
     */
    @Test(expected = NullPointerException.class)
    public void testSetLoggingManager() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setLoggingManager(null);
        webApp.log("KABOOM");
    }
}
