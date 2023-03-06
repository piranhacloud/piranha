/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
class DefaultWebApplicationTest {

    /**
     * Test addMapping method (verify the # of mappings > 0).
     */
    @Test
    void testAddMapping() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        ServletRegistration.Dynamic dynamic
                = webApplication.addServlet("echo", "servlet.EchoServlet");
        assertNotNull(dynamic);
        dynamic.addMapping("/echo");
        assertFalse(dynamic.getMappings().isEmpty());
    }

    /**
     * Test addMapping method.
     * 
     * <p>
     *  Verify when we add twice addMapping will return a empty set.
     * </p>
     */
    @Test
    void testAddMapping2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        ServletRegistration.Dynamic dynamic = webApplication.addServlet("echo", "servlet.EchoServlet");
        assertNotNull(dynamic);
        dynamic.addMapping("/echo");
        assertFalse(dynamic.getMappings().isEmpty());
        assertEquals(0, dynamic.addMapping("/echo").size());
    }

    /**
     * Test getAsync.
     *
     * @throws Exception
     */
    @Test
    void testGetAsync() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        Dynamic registration = webApplication.addServlet("Chat", TestChat1Servlet.class);
        registration.setAsyncSupported(true);
        webApplication.addServletMapping("Chat", "/chat");
        webApplication.initialize();
        webApplication.start();
        
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/chat");
        request.setWebApplication(webApplication);
        
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.setWebApplication(webApplication);
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        
        webApplication.service(request, response);
        assertTrue(byteOutput.toByteArray().length > 0);

        request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"login"});
        request.setParameter("name", new String[]{"username"});

        response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        
        webApplication.service(request, response);
        assertTrue(byteOutput.toByteArray().length > 0);
        
        request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"post"});
        request.setParameter("name", new String[]{"username"});
        request.setParameter("message", new String[]{new Date().toString()});
        
        response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        
        webApplication.service(request, response);
        assertTrue(byteOutput.toByteArray().length > 0);
    }

    /**
     * Test getAsync.
     *
     * @throws Exception
     */
    @Test
    void testGetAsync2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        Dynamic registration = webApplication.addServlet("Chat", TestChat2Servlet.class);
        registration.setAsyncSupported(true);
        webApplication.addServletMapping("Chat", "/chat");
        webApplication.initialize();
        webApplication.start();
        
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/chat");
        
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        
        webApplication.service(request, response);
        assertTrue(byteOutput.toByteArray().length > 0);
        
        request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"login"});
        request.setParameter("name", new String[]{"username"});
        
        response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        
        webApplication.service(request, response);
        assertTrue(byteOutput.toByteArray().length > 0);
        
        request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"post"});
        request.setParameter("name", new String[]{"username"});
        request.setParameter("message", new String[]{new Date().toString()});
        
        response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        
        webApplication.service(request, response);
        assertTrue(byteOutput.toByteArray().length > 0);
    }

    /**
     * Test getObjectInstanceManager method.
     */
    @Test
    void testGetObjectInstanceManager() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getManager().getObjectInstanceManager());
        webApplication.getManager().setObjectInstanceManager(null);
        assertNull(webApplication.getManager().getObjectInstanceManager());
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
        assertNotNull(webApp.getManager().getSecurityManager());
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
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/include");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        response.setWebApplication(webApp);
        webApp.service(request, response);
        assertTrue(new String(byteOutput.toByteArray()).contains("This was included"));
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
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/include3");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        response.setWebApplication(webApp);
        webApp.service(request, response);
        assertTrue(new String(byteOutput.toByteArray()).contains("This was included"));
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
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/include4");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        response.setWebApplication(webApp);
        webApp.service(request, response);
        assertTrue(new String(byteOutput.toByteArray()).contains("This was includedThis was included"));
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
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/Snoop");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
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
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/Snoop");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
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
