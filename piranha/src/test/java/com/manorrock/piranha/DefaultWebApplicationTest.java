/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import java.io.File;
import java.util.Date;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.Set;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultWebApplication class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationTest {

    /**
     * Test addFilter method.
     */
    @Test
    public void testAddFilter() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.addFilter("filter", "com.manorrock.piranha.TestSnoopFilter"));
        assertNotNull(webApp.getFilterRegistration("filter"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    public void testAddFilter2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.addFilter("filter", TestSnoopFilter.class));
        assertNotNull(webApp.getFilterRegistration("filter"));
        assertEquals(TestSnoopFilter.class.getName(), webApp.getFilterRegistration("filter").getClassName());
    }

    /**
     * Test addFilter method.
     */
    @Test
    public void testAddFilter3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.addFilter("filter", new TestSnoopFilter()));
        assertNotNull(webApp.getFilterRegistration("filter"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    public void testAddFilter4() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.addFilter("filter", "doesnotexit"));
    }

    /**
     * Test addFilter methd.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddFilter5() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        webApp.addFilter("filter", "doesnotmatter");
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddFilter6() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.start();
        webApp.addFilter("filter", TestSnoopFilter.class);
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddFilter7() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.addFilter(null, "TestSnoopFilter");
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAddFilter8() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.addFilter("filter", "TestSnoopFilter");
        webApp.addFilter("filter", "TestSnoopFilter");
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddFilter9() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        webApp.addFilter(null, Filter.class);
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testAddFilter10() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.initialize();
        assertNotNull(webApp.addFilter("filter", Filter.class));
        assertNotNull(webApp.addFilter("filter", Filter.class));
    }

    /**
     * Test addInitializer method.
     */
    @Test
    public void testAddInitializer() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addInitializer("com.manorrock.piranha.TestInitializer");
        webApp.initialize();
        webApp.start();
        webApp.stop();
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addListener(new TestServletRequestListener());
        webApp.requestListeners.stream().forEach((listener) -> {
            listener.requestInitialized(new ServletRequestEvent(webApp, null));
        });
        assertNotNull(webApp.getAttribute("requestInitialized"));
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addListener(new TestServletRequestAttributeListener());
        webApp.requestAttributeListeners.stream().forEach((listener) -> {
            listener.attributeAdded(new ServletRequestAttributeEvent(webApp, null, "attributeAdded", true));
        });
        assertNotNull(webApp.getAttribute("attributeAdded"));
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addListener(new TestServletContextListener());
        webApp.contextListeners.stream().forEach((listener) -> {
            listener.contextInitialized(new ServletContextEvent(webApp));
        });
        assertNotNull(webApp.getAttribute("contextInitialized"));
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener4() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addListener(new TestServletContextAttributeListener());
        webApp.contextAttributeListeners.stream().forEach((listener) -> {
            listener.attributeAdded(new ServletContextAttributeEvent(webApp, "attributeAdded", true));
        });
        assertNotNull(webApp.getAttribute("attributeAdded"));
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener5() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        TestHttpServletResponse response = new TestHttpServletResponse();
        webApp.linkRequestAndResponse(request, response);
        webApp.addListener(new TestHttpSessionAttributeListener());
        HttpSession session = webApp.getHttpSessionManager().createSession(webApp, request);
        session.setAttribute("attributedAdded", Boolean.TRUE);
        assertNotNull(webApp.getAttribute("attributeAdded"));
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener6() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        webApp.addListener(new TestHttpSessionIdListener());
        HttpSession session = request.getSession(true);
        request.setRequestedSessionId(session.getId());
        webApp.getHttpSessionManager().changeSessionId(request);
        assertNotNull(webApp.getAttribute("sessionIdChanged"));
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener7() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        webApp.addListener(new TestHttpSessionListener());
        request.getSession(true);
        assertNotNull(webApp.getAttribute("sessionCreated"));
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener8() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        webApp.addListener(TestHttpSessionListener.class);
        request.getSession(true);
        assertNotNull(webApp.getAttribute("sessionCreated"));
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener9() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        webApp.linkRequestAndResponse(request, response);
        webApp.addListener("com.manorrock.piranha.TestHttpSessionListener");
        request.getSession(true);
        assertNotNull(webApp.getAttribute("sessionCreated"));
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener10() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        TestHttpServletResponse response = new TestHttpServletResponse();
        webApp.linkRequestAndResponse(request, response);
        webApp.addListener(new TestHttpSessionAttributeListener());
        HttpSession session = webApp.getHttpSessionManager().createSession(webApp, request);
        session.setAttribute("attributeAdded", Boolean.TRUE);
        session.setAttribute("attributeAdded", Boolean.FALSE);
        assertNotNull(webApp.getAttribute("attributeAdded"));
        assertFalse((Boolean) webApp.getAttribute("attributeAdded"));
    }

    /**
     * Test addListener method.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddListener11() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.addListener(TestHttpSessionListener.class);
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener12() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(TestIllegalAccessExceptionServletContextListener.class);
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
        webApp.addResource(new DefaultDirectoryResource(new File(".")));
        assertNotNull(webApp.getResource("/src/main/java"));
    }

    /**
     * Test addServlet(name, type) method.
     */
    @Test
    public void testAddServlet() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        Dynamic dynamic = webApp.addServlet("echo", TestEcho1Servlet.class);
        assertNotNull(dynamic);
    }

    /**
     * Test addServlet(name, type) method.
     */
    @Test
    public void testAddServlet2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        Dynamic dynamic = webApp.addServlet("bogus", Servlet.class);
        assertNull(dynamic);
    }

    /**
     * Test addServlet(name, className) method.
     */
    @Test
    public void testAddServlet3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        Dynamic dynamic = webApp.addServlet("echo", "servlet.EchoServlet");
        assertNotNull(dynamic);
    }

    /**
     * Test addServlet(name, className) method.
     */
    @Test
    public void testAddServlet4() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        Dynamic dynamic = webApp.addServlet("bogus", "servlet.BogusServlet");
        assertNotNull(dynamic);
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateListener() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.createListener(TestEventListener.class);
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = ServletException.class)
    public void testCreateListener2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.createListener(TestInstantiationExceptionServletContextListener.class);
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = ServletException.class)
    public void testCreateListener3() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.createListener(TestIllegalAccessExceptionServletContextListener.class);
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
        webApp.addServlet("Snoop", TestSnoopServlet.class);
        webApp.addListener(TestServletContextListener.class);
        webApp.initialize();
        webApp.destroy();
        assertNotNull(webApp.getAttribute("contextDestroyed"));
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
        webApp.addServlet("Chat", TestChat1Servlet.class);
        webApp.addServletMapping("Chat", "/chat");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/chat");
        request.setAsyncSupported(true);
        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertNotNull(response.getResponseBody());

        request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setAsyncSupported(true);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"login"});
        request.setParameter("name", new String[]{"username"});
        response = new TestHttpServletResponse();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertNotNull(response.getResponseBody());

        request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setAsyncSupported(true);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"post"});
        request.setParameter("name", new String[]{"username"});
        request.setParameter("message", new String[]{new Date().toString()});
        response = new TestHttpServletResponse();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertNotNull(response.getResponseBody());
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
        webApp.addServlet("Chat", TestChat2Servlet.class);
        webApp.addServletMapping("Chat", "/chat");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/chat");
        request.setAsyncSupported(true);
        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertNotNull(response.getResponseBody());

        request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setAsyncSupported(true);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"login"});
        request.setParameter("name", new String[]{"username"});
        response = new TestHttpServletResponse();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertNotNull(response.getResponseBody());

        request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setAsyncSupported(true);
        request.setServletPath("/chat");
        request.setMethod("POST");
        request.setParameter("action", new String[]{"post"});
        request.setParameter("name", new String[]{"username"});
        request.setParameter("message", new String[]{new Date().toString()});
        response = new TestHttpServletResponse();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);

        webApp.service(request, response);

        assertNotNull(response.getResponseBody());
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
        HttpServletRequest request = new TestHttpServletRequest();
        assertEquals(DispatcherType.REQUEST, request.getDispatcherType());
    }

    /**
     * Test getEffectiveMajorVersion method.
     */
    @Test
    public void testGetEffectiveMajorVersion() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertEquals(3, webApp.getEffectiveMajorVersion());
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
     * Test getFilterRegistrations method.
     */
    @Test
    public void testGetFilterRegistrations() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addFilter("filter", TestSnoopFilter.class);
        assertFalse(webApp.getFilterRegistrations().isEmpty());
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
        assertEquals(3, webApp.getMajorVersion());
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
        HttpServletRequest request = new TestHttpServletRequest();
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
        webApp.addResource(new DefaultDirectoryResource(new File(".")));
        assertNotNull(webApp.getRealPath("/src/main/java"));
    }

    /**
     * Test getRequest method.
     */
    @Test
    public void testGetRequest() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        TestHttpServletResponse response = new TestHttpServletResponse();
        webApp.linkRequestAndResponse(request, response);
        assertNotNull(webApp.getRequest(response));
    }
    
    /**
     * Test getRequestCharacterEncoding method.
     */
    @Test
    public void testGetRequestCharacterEncoding () {
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
        webApp.addResource(new DefaultDirectoryResource(new File(".")));
        assertNotNull(webApp.getResourceAsStream("/pom.xml"));
    }

    /**
     * Test getResourcePaths method.
     */
    @Test
    public void testGetResourcePaths() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNull(webApp.getResourcePaths("/this_will_be_null/"));
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
        assertNull(webApp.getServletContextName());
        webApp.setServletContextName("MYNAME");
        assertNotNull(webApp.getServletContextName());
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
        ServletRegistration.Dynamic dynamic = webApp.addServlet("session", "com.manorrock.piranha.TestSessionServlet");
        assertNotNull(dynamic);
        dynamic.addMapping("/session");
        webApp.initialize();
        webApp.start();

        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/session");
        TestHttpServletResponse response = new TestHttpServletResponse();

        webApp.service(request, response);

        assertNotNull(response.getResponseBody());
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
     * Test initializeFilters method.
     */
    @Test
    public void testInitializeFilters() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addFilter("Broken Filter", TestBrokenFilter.class);
        webApp.initialize();
        assertNotNull(webApp.getAttribute("Broken Filter"));
    }

    /**
     * Test initializeServlets method.
     */
    @Test
    public void testInitializeServlets() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Broken Servlet", TestBrokenServlet.class);
        webApp.initialize();
        assertNotNull(webApp.getAttribute("Broken Servlet"));
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
        TestHttpServletRequest request = new TestHttpServletRequest();
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
        TestHttpServletRequest request = new TestHttpServletRequest();
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
    public void testService() throws Exception {
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/Snoop");
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApp);
        TrackServletRequestListener listener = new TrackServletRequestListener();
        webApp.addServlet("Snoop", TestSnoopServlet.class);
        webApp.addServletMapping("Snoop", "/Snoop");
        webApp.addListener(listener);
        webApp.initialize();
        webApp.start();
        webApp.service(request, response);
        assertTrue(listener.trackCalls.length() > 0);
        assertEquals("requestInitialized,requestDestroyed,", listener.trackCalls.toString());
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
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApp);
        request.setServletPath("/Snoop");
        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        response.setWebApplication(webApp);
        webApp.addServletMapping("Snoop", "/Snoop");
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
