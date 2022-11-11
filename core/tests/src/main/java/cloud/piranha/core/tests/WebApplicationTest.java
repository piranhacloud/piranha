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
package cloud.piranha.core.tests;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.RequestDispatcher;
import static jakarta.servlet.RequestDispatcher.FORWARD_CONTEXT_PATH;
import static jakarta.servlet.RequestDispatcher.FORWARD_MAPPING;
import static jakarta.servlet.RequestDispatcher.FORWARD_PATH_INFO;
import static jakarta.servlet.RequestDispatcher.FORWARD_QUERY_STRING;
import static jakarta.servlet.RequestDispatcher.FORWARD_REQUEST_URI;
import static jakarta.servlet.RequestDispatcher.FORWARD_SERVLET_PATH;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.SessionTrackingMode;
import static jakarta.servlet.SessionTrackingMode.COOKIE;
import static jakarta.servlet.SessionTrackingMode.URL;
import jakarta.servlet.descriptor.JspConfigDescriptor;
import jakarta.servlet.descriptor.JspPropertyGroupDescriptor;
import jakarta.servlet.descriptor.TaglibDescriptor;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSessionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashSet;
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
 * The JUnit tests for any WebApplication implementation.
 *
 * <p>
 * Note all these tests only use the public APIs of WebApplication,
 * WebApplicationRequest and WebApplicationResponse.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class WebApplicationTest {

    /**
     * Create the web application.
     *
     * @return the web application.
     */
    protected abstract WebApplication createWebApplication();

    /**
     * Create the web application request.
     *
     * @return the web application request.
     */
    protected abstract WebApplicationRequest createWebApplicationRequest();

    /**
     * Create the web application response.
     *
     * @return the web application response.
     */
    protected abstract WebApplicationResponse createWebApplicationResponse();

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("TestAddFilter", TestAddFilterFilter.class);
        assertNotNull(webApplication.getFilterRegistration("TestAddFilter"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter2() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("TestAddFilter2", new Filter() {

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                ServletContext servletContext = filterConfig.getServletContext();
                servletContext.setAttribute("TestAddFilter2", true);
                throw new ServletException("TestAddFilter2");
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        webApplication.initialize();
        assertNotNull(webApplication.getAttribute("TestAddFilter2"));
    }

    /**
     * Test addFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddFilter3() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("TestAddFilter3a", new Filter() {

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                request.setAttribute("TestAddFilter3a", "true");
                chain.doFilter(request, response);
            }
        });
        webApplication.addFilterMapping("TestAddFilter3a", "/*");
        webApplication.addFilter("TestAddFilter3b", new Filter() {

            @Override
            public void destroy() {
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                request.setAttribute("TestAddFilter3b", "true");
                chain.doFilter(request, response);
            }

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }
        });
        webApplication.addFilterMapping("TestAddFilter3b", "/*");
        webApplication.addServlet("TestAddFilter3Servlet", new HttpServlet() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                response.setStatus(200);
            }
        });
        webApplication.addServletMapping("TestAddFilter3Servlet", "/testAddFilter3");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/testAddFilter3");
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(200, response.getStatus());
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter4() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        webApplication.start();
        Filter filter = new Filter() {
            @Override
            public void destroy() {
            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }
        };
        assertThrows(IllegalStateException.class, ()
                -> webApplication.addFilter("TestAddFilter4", filter));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter5() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.addFilter("TestAddFilter5", TestAddFilter5Filter.class));
        assertNotNull(webApplication.getFilterRegistration("TestAddFilter5"));
        assertEquals(TestAddFilter5Filter.class.getName(),
                webApplication.getFilterRegistration("TestAddFilter5").getClassName());
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter6() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.addFilter("TestAddFilter6", "doesnotexit"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter7() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertThrows(IllegalStateException.class, ()
                -> webApplication.addFilter("TestAddFilter7", "should throw IllegalStateException"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter8() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.addFilter(null, "filter name is null so throw IllegalArgumentException"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter9() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.addFilter(null, Filter.class));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter10() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        assertNotNull(webApplication.addFilter("filter", Filter.class));
        assertNull(webApplication.addFilter("filter", Filter.class));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter11() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.addFilter("filter", "InCompleteRegistrationFilter"));
        assertNull(webApplication.addFilter("filter", "InCompleteRegistrationFilter"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter12() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.addFilter("filter", "InCompleteRegistrationFilter"));
        assertNotNull(webApplication.getFilterRegistration("filter"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter13() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.addFilter("filter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        }));
        assertNull(webApplication.addFilter("filter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        }));
    }

    /**
     * Test addFilterMapping method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddFilterMapping() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("TestAddFilterMapping", new Filter() {

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                request.getServletContext().setAttribute("TestAddFilterMapping", true);
            }
        });
        webApplication.addFilterMapping("TestAddFilterMapping", "/*");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/testAddFilterMapping");
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(200, response.getStatus());
        assertNotNull(webApplication.getAttribute("TestAddFilterMapping"));
    }

    /**
     * Test addInitializer method.
     */
    @Test
    void testAddInitializer() {
        WebApplication webApplication = createWebApplication();
        webApplication.addInitializer((Set<Class<?>> classes, ServletContext servletContext) -> {
            servletContext.setAttribute("TestAddInitializer", true);
        });
        webApplication.initialize();
        assertNotNull(webApplication.getAttribute("TestAddInitializer"));
    }

    /**
     * Test addInitializer method.
     */
    @Test
    void testAddInitializer2() {
        WebApplication webApplication = createWebApplication();
        webApplication.addInitializer(TestAddInitializer2Initializer.class.getName());
        webApplication.initialize();
        assertNotNull(webApplication.getAttribute("TestAddInitializer2"));
    }

    /**
     * Test addInitialized method
     */
    @Test
    void testAddInitializer3() {
        WebApplication webApplication = createWebApplication();
        webApplication.addInitializer(TestAddInitializer3Initializer.class.getName());
        webApplication.addInitializer(new ServletContainerInitializer() {
            @Override
            public void onStartup(Set<Class<?>> classes, ServletContext context) throws ServletException {
                WebApplication webApplication = (WebApplication) context;
                context.setAttribute("testAddInitializer3", webApplication.getInitializers().size());
            }
        });
        webApplication.initialize();
        assertEquals(1, webApplication.getAttribute("testAddInitializer3"));
    }

    /**
     * Test addJspFile method.
     */
    @Test
    void testAddJspFile() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.addJspFile("TestAddJspFile", "testAddJspFile.jsp"));
    }

    /**
     * Test addJspFile method.
     */
    @Test
    void testAddJspFile2() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertThrows(IllegalStateException.class, ()
                -> webApplication.addJspFile("TestAddJspFile2", "testAddJspFile2.jsp"));
    }

    /**
     * Test addJspFile method.
     */
    @Test
    void testAddJspFile3() {
        WebApplication webApplication = createWebApplication();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.addJspFile(null, "testAddJspFile3.jsp"));
    }

    /**
     * Test addListener method.
     */
    @Test
    void testAddListener() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        assertThrows(IllegalStateException.class, ()
                -> webApplication.addListener("TestAddListener"));
    }

    /**
     * Test addListener method.
     */
    @Test
    void testAddListener2() {
        WebApplication webApplication = createWebApplication();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.addListener(TestAddListener2Listener.class));
    }

    /**
     * Test addResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddResource() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApplication.getResource("/src/main/java"));
    }

    /**
     * Test addServlet method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddServlet() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestAddServletServlet", new HttpServlet() {
            @Override
            public void init(ServletConfig config) throws ServletException {
                config.getServletContext().setAttribute("TestAddServlet", true);
                throw new RuntimeException("TestAddServlet");
            }
        });
        webApplication.addServletMapping("TestAddServletServlet", "/testAddServlet");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testAddServlet");
        WebApplicationResponse response = createWebApplicationResponse();
        try {
            webApplication.service(request, response);
            fail();
        } catch (RuntimeException ue) {
        }
        assertNotNull(webApplication.getAttribute("TestAddServlet"));
    }

    /**
     * Test addServlet method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddServlet2() throws Exception {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.addServlet("TestAddServlet2Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException {
                response.getWriter().println("TestAddServlet2Servlet");
            }
        }));
        webApplication.addServletMapping("TestAddServlet2Servlet", "/testAddServlet2");
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testAddServlet2");
        WebApplicationResponse response = createWebApplicationResponse();
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertEquals(200, response.getStatus());
    }

    /**
     * Test addServlet method.
     */
    @Test
    void testAddServlet3() {
        WebApplication webApplication = createWebApplication();
        ServletRegistration.Dynamic dynamic = webApplication.addServlet(
                "TestAddServlet3Servlet", Servlet.class);
        assertNotNull(dynamic);
    }

    /**
     * Test addServlet method.
     */
    @Test
    void testAddServlet4() {
        WebApplication webApplication = createWebApplication();
        ServletRegistration.Dynamic dynamic = webApplication.addServlet(
                "TestAddServlet4Servlet", "TestAddServlet4Servlet");
        assertNotNull(dynamic);
    }

    /**
     * Test addServletMapping test.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddServletMapping() throws Exception {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.addServlet("TestAddServletMappingServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException {
                response.getWriter().println("TestAddServletMappingServlet");
            }
        }));
        webApplication.addServletMapping("TestAddServletMappingServlet",
                "/testAddServletMappingServlet");
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testAddServletMappingServlet");
        WebApplicationResponse response = createWebApplicationResponse();
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertEquals(200, response.getStatus());
    }

    /**
     * Test createFilter method.
     */
    @Test
    void testCreateFilter() {
        WebApplication webApplication = createWebApplication();
        assertThrows(ServletException.class, () -> webApplication.createFilter(Filter.class));
    }

    /**
     * Test createListener method.
     */
    @Test
    void testCreateListener() {
        WebApplication webApplication = createWebApplication();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.createListener(TestCreateListenerEventListener.class));
    }

    /**
     * Test createListener method.
     */
    @Test
    void testCreateListener2() throws Exception {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.createListener(
                TestCreateListener2ServletContextListener.class));
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testCreateListener3() throws Exception {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.createListener(
                TestCreateListener3ServletRequestListener.class));
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testCreateListener4() throws Exception {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.createListener(
                TestCreateListener4HttpSessionListener.class));
    }

    /**
     * Test createServlet method.
     */
    @Test
    void testCreateServlet() {
        WebApplication webApplication = createWebApplication();
        assertThrows(ServletException.class, () -> webApplication.createServlet(Servlet.class));
    }

    /**
     * Test declareRoles method.
     */
    @Test
    void testDeclareRoles() {
        WebApplication webApplication = createWebApplication();
        webApplication.declareRoles("testDeclareRoles");
    }

    /**
     * Test destroy method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testDestroy() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addListener(new ServletContextListener() {
            @Override
            public void contextDestroyed(ServletContextEvent event) {
                event.getServletContext().setAttribute("testDestroy", true);
            }
        });
        webApplication.initialize();
        webApplication.destroy();
        assertNotNull(webApplication.getAttribute("testDestroy"));
    }

    /**
     * Test getAttribute method.
     */
    @Test
    void testGetAttribute() {
        WebApplication webApplication = createWebApplication();
        assertThrows(NullPointerException.class, () -> webApplication.getAttribute(null));
    }

    /**
     * Test getAttributeNames method.
     */
    @Test
    void testGetAttributeNames() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getAttributeNames());
    }

    /**
     * Test getContext method.
     */
    @Test
    void testGetContext() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getContext("/context"));
    }

    /**
     * Test getContextPath method.
     */
    @Test
    void testGetContextPath() {
        WebApplication webApplication = createWebApplication();
        assertEquals("", webApplication.getContextPath());
    }

    /**
     * Test getDefaultServlet method.
     */
    @Test
    void testGetDefaultServlet() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getDefaultServlet());
    }

    /**
     * Test getDefaultSessionTrackingModes method.
     */
    @Test
    void testGetDefaultSessionTrackingModes() {
        WebApplication webApplication = createWebApplication();
        assertTrue(webApplication.getDefaultSessionTrackingModes().contains(COOKIE));
    }

    /**
     * Test getEffectiveMajorVersion method.
     */
    @Test
    void testGetEffectiveMajorVersion() {
        WebApplication webApplication = createWebApplication();
        assertEquals(6, webApplication.getEffectiveMajorVersion());
    }

    /**
     * Test getEffectiveMinorVersion method.
     */
    @Test
    void testGetEffectiveMinorVersion() {
        WebApplication webApplication = createWebApplication();
        assertEquals(0, webApplication.getEffectiveMinorVersion());
    }

    /**
     * Test getEffectiveSessionTrackingMode method.
     */
    @Test
    void testGetEffectiveSessionTrackingMode() {
        WebApplication webApplication = createWebApplication();
        assertTrue(webApplication.getEffectiveSessionTrackingModes().contains(COOKIE));
    }

    /**
     * Test getEffectiveSessionTrackingModes method.
     */
    @Test
    void testGetEffectiveSessionTrackingModes2() {
        WebApplication webApplication = createWebApplication();
        Set<SessionTrackingMode> trackingModes = EnumSet.of(URL);
        webApplication.setSessionTrackingModes(trackingModes);
        assertTrue(webApplication.getEffectiveSessionTrackingModes().contains(URL));
    }
    
    /**
     * Test getFilterRegistration method.
     */
    @Test
    void testGetFilterRegistration() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("TestGetFilterRegistrationFilter", new Filter() {

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        assertNotNull(webApplication.getFilterRegistration("TestGetFilterRegistrationFilter"));
    }

    /**
     * Test getFilterRegistration method.
     */
    @Test
    void testGetFilterRegistration2() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetFilterRegistration2Filter", "TestGetFilterRegistration2Filter");
        assertNotNull(webApplication.getFilterRegistration("testGetFilterRegistration2Filter"));
    }

    /**
     * Test getFilterRegistrations method.
     */
    @Test
    void testGetFilterRegistrations() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetFilterRegistrationsFilter", "testGetFilterRegistrationsFilter");
        assertFalse(webApplication.getFilterRegistrations().isEmpty());
    }

    /**
     * Test getInitParameter method.
     */
    @Test
    void testGetInitParameter() {
        WebApplication webApplication = createWebApplication();
        webApplication.setInitParameter("testGetInitParameter", Boolean.TRUE.toString());
        assertEquals("true", webApplication.getInitParameter("testGetInitParameter"));
    }

    /**
     * Test getInitParameterNames method.
     */
    @Test
    void testGetInitParameterNames() {
        WebApplication webApplication = createWebApplication();
        webApplication.setInitParameter("testGetInitParameterNames", Boolean.TRUE.toString());
        Enumeration<String> enumeration = webApplication.getInitParameterNames();
        assertEquals("testGetInitParameterNames", enumeration.nextElement());
        assertFalse(enumeration.hasMoreElements());
    }

    /**
     * Test getInitializers method.
     */
    @Test
    void testGetInitializers() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getInitializers());
        assertTrue(webApplication.getInitializers().isEmpty());
    }

    /**
     * Test getJspConfigDescriptor method.
     */
    @Test
    void testGetJspConfigDescriptor() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getJspConfigDescriptor());
    }

    /**
     * Test getMajorVersion method.
     */
    @Test
    void testGetMajorVersion() {
        WebApplication webApplication = createWebApplication();
        assertEquals(6, webApplication.getMajorVersion());
    }

    /**
     * Test getManager method.
     */
    @Test
    void testGetManager() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getManager());
    }

    /**
     * Test getMappings method.
     */
    @Test
    void testGetMappings() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getMappings("TestGetMappingsServlet"));
        assertTrue(webApplication.getMappings("TestGetMappingsServlet").isEmpty());
    }

    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getMimeType("index.html"));
    }

    /**
     * Test getMinorVersion method.
     */
    @Test
    void testGetMinorVersion() {
        WebApplication webApplication = createWebApplication();
        assertEquals(0, webApplication.getMinorVersion());
    }

    /**
     * Test getNamedDispatcher method.
     */
    @Test
    void testGetNamedDispatcher() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestGetNamedDispatcherServlet", new HttpServlet() {
        });
        webApplication.initialize();
        assertNotNull(webApplication.getNamedDispatcher("TestGetNamedDispatcherServlet"));
    }
    
    /**
     * Test getNamedDispatcher method
     * 
     * @assertion Servlet:SPEC:181
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetNamedDispatcher2() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestGetNamedDispatcher2Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                PrintWriter writer = response.getWriter();
                if (request.getAttribute(FORWARD_CONTEXT_PATH) != null) {
                    writer.println(request.getAttribute(FORWARD_CONTEXT_PATH));
                }
                if (request.getAttribute(FORWARD_MAPPING) != null) {
                    writer.println(request.getAttribute(FORWARD_CONTEXT_PATH));
                }
                if (request.getAttribute(FORWARD_PATH_INFO) != null) {
                    writer.println(request.getAttribute(FORWARD_PATH_INFO));
                }
                if (request.getAttribute(FORWARD_QUERY_STRING) != null) {
                    writer.println(request.getAttribute(FORWARD_QUERY_STRING));
                }
                if (request.getAttribute(FORWARD_REQUEST_URI) != null) {
                    writer.println(request.getAttribute(FORWARD_REQUEST_URI));
                }
                if (request.getAttribute(FORWARD_SERVLET_PATH) != null) {
                    writer.println(request.getAttribute(FORWARD_SERVLET_PATH));
                }
                writer.flush();
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setBodyOnly(true);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestGetNamedDispatcher2Servlet");
        dispatcher.forward(request, response);
        assertEquals("", new String(byteOutput.toByteArray()));
    }
    
    /**
     * Test getNamedDispatcher method.
     * 
     * @assertion Servlet:SPEC:79
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetNamedDispatcher3() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestGetNamedDispatcher3Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                PrintWriter writer = response.getWriter();
                writer.print("Request URI: " + request.getRequestURI());
                writer.flush();
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setBodyOnly(true);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestGetNamedDispatcher3Servlet");
        dispatcher.forward(request, response);
        assertEquals("Request URI: /", new String(byteOutput.toByteArray()));
    }
    
    /**
     * Test getNamedDispatcher method.
     * 
     * @assertion Servlet:SPEC:80
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetNamedDispatcher4() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestGetNamedDispatcher4Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                PrintWriter writer = response.getWriter();
                writer.print("Flushed!");
            }
        });
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        RequestDispatcher dispatcher = webApplication.getNamedDispatcher("TestGetNamedDispatcher4Servlet");
        dispatcher.forward(request, response);
        assertEquals("HTTP/1.1 200\n\nFlushed!", new String(byteOutput.toByteArray()));
    }

    /**
     * Test getNamedDispatcher method.
     */
    @Test
    void testGetNamedDispatcher5() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        assertNull(webApplication.getNamedDispatcher("TestGetNamedDispatcher5Servlet"));
    }
    
    /**
     * Test getRealPath method.
     */
    @Test
    void testGetRealPath() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getRealPath("index.html"));
    }

    /**
     * Test getRealPath method.
     */
    @Test
    void testGetRealPath2() {
        WebApplication webApplication = createWebApplication();
        webApplication.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApplication.getRealPath("/src/main/java"));
    }

    /**
     * Test getRequest method.
     */
    @Test
    void testGetRequest() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        WebApplicationResponse response = createWebApplicationResponse();
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(webApplication.getRequest(response));
    }

    /**
     * Test getRequestCharacterEncoding method.
     */
    @Test
    void testGetRequestCharacterEncoding() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getRequestCharacterEncoding());
        webApplication.setRequestCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApplication.getRequestCharacterEncoding());
    }

    /**
     * Test getRequestDispatcher method.
     */
    @Test
    void testGetRequestDispatcher() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestGetRequestDispatcherServlet", new HttpServlet() {
        });
        webApplication.addServletMapping("TestGetRequestDispatcherServlet", "/testGetRequestDispatcher");
        assertNotNull(webApplication.getRequestDispatcher("/testGetRequestDispatcher"));
    }

    /**
     * Test getResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetResource() throws Exception {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getResource("/testGetResource"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    void testGetResourceAsStream() {
        WebApplication webApplication = createWebApplication();
        webApplication.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApplication.getResourceAsStream("/pom.xml"));
    }

    /**
     * Test getResourcePaths method.
     */
    @Test
    void testGetResourcePaths() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getResourcePaths("/testGetResourcePaths/"));
        assertNull(webApplication.getResourcePaths(null));
    }

    /**
     * Test getResponse method.
     */
    @Test
    void testGetResponse() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        WebApplicationResponse response = createWebApplicationResponse();
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(webApplication.getResponse(request));
    }

    /**
     * Test getResponseCharacterEncoding.
     */
    @Test
    void testGetResponseCharacterEncoding() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getResponseCharacterEncoding());
        webApplication.setResponseCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApplication.getResponseCharacterEncoding());
    }

    /**
     * Test getServerInfo method.
     */
    @Test
    void testGetServerInfo() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getServerInfo());
        assertFalse(webApplication.getServerInfo().trim().equals(""));
    }

    /**
     * Test getServletContextId method.
     */
    @Test
    void testGetServletContextId() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getServletContextId());
    }

    /**
     * Test getServletContextName method.
     */
    @Test
    void testGetServletContextName() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getServletContextName());
    }

    /**
     * Test getServletRegistration method.
     */
    @Test
    void testGetServletRegistration() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestGetServletRegistrationServlet", "TestGetServletRegistrationServlet");
        assertNotNull(webApplication.getServletRegistration("TestGetServletRegistrationServlet"));
    }

    /**
     * Test getServletRegistrations method.
     */
    @Test
    void testGetServletRegistration2() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("TestGetServletRegistration2Servlet", TestGetServletRegistration2Servlet.class);
        assertNotNull(webApplication.getServletRegistration("TestGetServletRegistration2Servlet"));
    }

    /**
     * Test getServletRegistrations method.
     */
    @Test
    void testGetServletRegistrations() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("servlet", TestGetServletRegistrationsServlet.class);
        assertFalse(webApplication.getServletRegistrations().isEmpty());
    }

    /**
     * Test getServletRegistrations method.
     */
    @Test
    void testGetServletRegistrations2() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getServletRegistrations());
        assertTrue(webApplication.getServletRegistrations().isEmpty());
    }

    /**
     * Test getSessionCookieConfig method.
     */
    @Test
    void testGetSessionCookieConfig() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getSessionCookieConfig());
    }

    /**
     * Test getSessionTimeout method.
     */
    @Test
    void testGetSessionTimeout() {
        WebApplication webApplication = createWebApplication();
        assertTrue(webApplication.getSessionTimeout() > 0);
    }

    /**
     * Test getVirtualServerName method.
     */
    @Test
    void testGetVirtualServerName() {
        WebApplication webApplication = createWebApplication();
        assertEquals("server", webApplication.getVirtualServerName());
    }

    /**
     * Test initialize method.
     */
    @Test
    void testInitialize() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        assertTrue(webApplication.isInitialized());
    }

    /**
     * Test initializeDeclaredFinish method.
     */
    @Test
    void testInitializeDeclaredFinish() {
        WebApplication webApplication = createWebApplication();
        webApplication.initializeDeclaredFinish();
        assertFalse(webApplication.isInitialized());
    }

    /**
     * Test initializeFilters method.
     */
    @Test
    void testInitializeFilters() {
        WebApplication webApplication = createWebApplication();
        webApplication.initializeFilters();
        assertFalse(webApplication.isInitialized());
    }

    /**
     * Test initializeFinish method.
     */
    @Test
    void testInitializeFinish() {
        WebApplication webApplication = createWebApplication();
        webApplication.initializeFinish();
        assertTrue(webApplication.isInitialized());
    }

    /**
     * Test initializeInitializers method.
     */
    @Test
    void testInitializeInitializers() {
        WebApplication webApplication = createWebApplication();
        webApplication.initializeInitializers();
        assertFalse(webApplication.isInitialized());
    }

    /**
     * Test initializeServlets method.
     */
    @Test
    void testInitializeServlets() {
        WebApplication webApplication = createWebApplication();
        webApplication.initializeServlets();
        assertFalse(webApplication.isInitialized());
    }

    /**
     * Test isDistributable method.
     */
    @Test
    void testIsDistributable() {
        WebApplication webApplication = createWebApplication();
        assertFalse(webApplication.isDistributable());
    }

    /**
     * Test isInitialized method.
     */
    @Test
    void testIsInitialized() {
        WebApplication webApplication = createWebApplication();
        assertFalse(webApplication.isInitialized());
        webApplication.initialize();
        assertTrue(webApplication.isInitialized());
    }

    /**
     * Test isMetadataComplete method.
     */
    @Test
    void testIsMetadataComplete() {
        WebApplication webApplication = createWebApplication();
        assertFalse(webApplication.isMetadataComplete());
    }

    /**
     * Test linkRequestAndResponse method.
     */
    @Test
    void testLinkRequestAndResponse() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        WebApplicationResponse response = createWebApplicationResponse();
        assertNull(webApplication.getRequest(response));
        assertNull(webApplication.getResponse(request));
        webApplication.linkRequestAndResponse(request, response);
        assertEquals(response, webApplication.getResponse(request));
        assertEquals(request, webApplication.getRequest(response));
    }

    /**
     * Test log method.
     */
    @Test
    void testLog() {
        WebApplication webApplication = createWebApplication();
        webApplication.log("TestLog");
    }

    /**
     * Test log method.
     */
    @Test
    void testLog2() {
        WebApplication webApplication = createWebApplication();
        webApplication.log("TestLog", new RuntimeException());
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    void testRemoveAttribute() {
        WebApplication webApplication = createWebApplication();
        webApplication.setAttribute("name", "value");
        assertNotNull(webApplication.getAttribute("name"));
        webApplication.removeAttribute("name");
        assertNull(webApplication.getAttribute("name"));
    }

    /**
     * Test removeServletMapping method.
     */
    @Test
    void testRemoveServletMapping() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.addServlet("TestRemoveServletMappingServlet",
                new HttpServlet() {
        }));
        webApplication.addServletMapping("TestRemoveServletMappingServlet",
                "/testRemoveServletMapping");
        assertEquals("TestRemoveServletMappingServlet", webApplication
                .removeServletMapping("/testRemoveServletMapping"));
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService() throws Exception {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.addServlet("TestServiceServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException {
                response.setStatus(403);
            }
        }));
        webApplication.addServletMapping("TestServiceServlet", "/testService");
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testService");
        WebApplicationResponse response = createWebApplicationResponse();
        webApplication.initialize();
        webApplication.start();
        webApplication.service(request, response);
        assertEquals(403, response.getStatus());
    }

    /**
     * Test setAttribute method.
     */
    @Test
    void testSetAttribute() {
        WebApplication webApplication = createWebApplication();
        webApplication.setAttribute("testSetAttribute", true);
        assertTrue((boolean) webApplication.getAttribute("testSetAttribute"));
    }

    /**
     * Test setClassLoader method.
     */
    @Test
    void testSetClassLoader() {
        WebApplication webApplication = createWebApplication();
        assertNotNull(webApplication.getClassLoader());
        webApplication.setClassLoader(null);
        assertNull(webApplication.getClassLoader());
    }

    /**
     * Test setContextPath method.
     */
    @Test
    void testSetContextPath() {
        WebApplication webApplication = createWebApplication();
        webApplication.setContextPath("/setContextPath");
        assertEquals("/setContextPath", webApplication.getContextPath());
    }

    /**
     * Test setDefaultServlet method.
     */
    @Test
    void testSetDefaultServlet() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getDefaultServlet());
        webApplication.setDefaultServlet(new HttpServlet() {
        });
        assertNotNull(webApplication.getDefaultServlet());
    }

    /**
     * Test setDistributable method.
     */
    @Test
    void testSetDistributable() {
        WebApplication webApplication = createWebApplication();
        assertFalse(webApplication.isDistributable());
        webApplication.setDistributable(true);
        assertTrue(webApplication.isDistributable());
    }

    /**
     * Test setEffectiveMajorVersion method.
     */
    @Test
    void testSetEffectiveMajorVersion() {
        WebApplication webApplication = createWebApplication();
        assertEquals(6, webApplication.getEffectiveMajorVersion());
        webApplication.setEffectiveMajorVersion(5);
        assertEquals(5, webApplication.getEffectiveMajorVersion());
    }

    /**
     * Test setEffectiveMinoVersion method.
     */
    @Test
    void testSetEffectiveMinorVersion() {
        WebApplication webApplication = createWebApplication();
        assertEquals(0, webApplication.getEffectiveMinorVersion());
        webApplication.setEffectiveMinorVersion(5);
        assertEquals(5, webApplication.getEffectiveMinorVersion());
    }

    /**
     * Test setEffectiveSessionTrackingModes method.
     */
    @Test
    void testSetEffectiveSessionTrackingModes() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        webApplication.start();
        Set<SessionTrackingMode> trackingModes = EnumSet.of(URL);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> webApplication.setSessionTrackingModes(trackingModes)));
    }
    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter() {
        WebApplication webApplication = createWebApplication();
        assertTrue(webApplication.setInitParameter("name", "value"));
        assertFalse(webApplication.setInitParameter("name", "value"));
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter2() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertThrows(IllegalStateException.class, ()
                -> webApplication.setInitParameter("name", "value"));
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter3() {
        WebApplication webApplication = createWebApplication();
        assertThrows(NullPointerException.class, ()
                -> webApplication.setInitParameter(null, "TestSetInitParameter3"));
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter4() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertThrows(NullPointerException.class, ()
                -> webApplication.setInitParameter(null, "TestSetInitParameter4"));
    }

    /**
     * Test setJspConfigDescriptor method.
     */
    @Test
    void testSetJspConfigDescriptor() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getJspConfigDescriptor());
        webApplication.setJspConfigDescriptor(new JspConfigDescriptor() {
            @Override
            public Collection<JspPropertyGroupDescriptor> getJspPropertyGroups() {
                return null;
            }

            @Override
            public Collection<TaglibDescriptor> getTaglibs() {
                return null;
            }
        });
        assertNotNull(webApplication.getJspConfigDescriptor());
    }

    /**
     * Test setMetadataComplete method.
     */
    @Test
    void testSetMetadataComplete() {
        WebApplication webApplication = createWebApplication();
        assertFalse(webApplication.isMetadataComplete());
        webApplication.setMetadataComplete(true);
        assertTrue(webApplication.isMetadataComplete());
    }

    /**
     * Test setRequestCharacterEncoding method.
     */
    @Test
    void testSetRequestCharacterEncoding() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getRequestCharacterEncoding());
        webApplication.setRequestCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApplication.getRequestCharacterEncoding());
    }

    /**
     * Test setResponseCharacterEncoding.
     */
    @Test
    void testSetResponseCharacterEncoding() {
        WebApplication webApplication = createWebApplication();
        assertNull(webApplication.getResponseCharacterEncoding());
        webApplication.setResponseCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApplication.getResponseCharacterEncoding());
    }

    /**
     * Test setServletContextName method.
     */
    @Test
    void testSetServletContextName() {
        WebApplication webApplication = createWebApplication();
        webApplication.setServletContextName("TestSetServletContextName");
        assertNotNull(webApplication.getServletContextName());
        assertEquals("TestSetServletContextName", webApplication.getServletContextName());
    }

    /**
     * Test setSessionTimeout method.
     */
    @Test
    void testSetSessionTimeout() {
        WebApplication webApplication = createWebApplication();
        webApplication.setSessionTimeout(5);
        assertEquals(5, webApplication.getSessionTimeout());
        webApplication.setSessionTimeout(10);
        assertEquals(10, webApplication.getSessionTimeout());
    }

    /**
     * Test setSessionTimeout method
     */
    @Test
    void testSessionTimeout2() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> webApplication.setSessionTimeout(50)));
    }

    /**
     * Test setSessionTrackingModes method.
     */
    @Test
    void testSetSessionTrackingModes() {
        WebApplication webApplication = createWebApplication();
        webApplication.setSessionTrackingModes(new HashSet() {
        });
        assertTrue(webApplication.getEffectiveSessionTrackingModes().isEmpty());
    }

    /**
     * Test setVirtualServerName method.
     */
    @Test
    void testSetVirtualServerName() {
        WebApplication webApplication = createWebApplication();
        webApplication.setVirtualServerName("myname");
        assertEquals("myname", webApplication.getVirtualServerName());
    }

    /**
     * Test setWebApplicationRequestMapper method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetWebApplicationRequestMapper() throws Exception {
        WebApplication webApplication = createWebApplication();
        webApplication.setWebApplicationRequestMapper(null);
        WebApplicationRequest request = createWebApplicationRequest();
        WebApplicationResponse response = createWebApplicationResponse();
        webApplication.initialize();
        webApplication.start();
        assertThrows(NullPointerException.class, ()
                -> webApplication.service(request, response));
    }

    /**
     * Test start method.
     */
    @Test
    void testStart() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertTrue(webApplication.isServicing());
    }

    /**
     * Test stop method.
     */
    @Test
    void testStop() {
        WebApplication webApplication = createWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertTrue(webApplication.isServicing());
        webApplication.stop();
        assertFalse(webApplication.isServicing());
    }

    /**
     * Test unlinkRequestAndResponse method.
     */
    @Test
    void testUnlinkRequestAndResponse() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        WebApplicationResponse response = createWebApplicationResponse();
        webApplication.linkRequestAndResponse(request, response);
        assertEquals(response, webApplication.getResponse(request));
        assertEquals(request, webApplication.getRequest(response));
        webApplication.unlinkRequestAndResponse(request, response);
        assertNull(webApplication.getRequest(response));
        assertNull(webApplication.getResponse(request));
    }

    /**
     * Test filter that is used by testAddFilter.
     */
    class TestAddFilterFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        }
    }

    /**
     * Test filter that is used by testAddFilter5.
     */
    class TestAddFilter5Filter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            chain.doFilter(request, response);
        }
    }

    /**
     * Test initializer that is used by testAddInitializer2.
     */
    public static class TestAddInitializer2Initializer implements ServletContainerInitializer {

        @Override
        public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
            servletContext.setAttribute("TestAddInitializer2", true);
        }
    }

    /**
     * Test initializer that is used by testAddInitializer2.
     */
    public static class TestAddInitializer3Initializer implements ServletContainerInitializer {

        /**
         * Constructor.
         *
         * @throws IOException when an I/O error occurs.
         */
        public TestAddInitializer3Initializer() throws IOException {
            throw new IOException();
        }

        @Override
        public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        }
    }

    /**
     * Test listener that is used by testAddListener2.
     */
    public static class TestAddListener2Listener implements EventListener {
    }

    /**
     * Test listener that is used by testCreateListener.
     */
    public static class TestCreateListenerEventListener implements EventListener {
    }

    /**
     * Test listener that is used by testCreateListener2.
     */
    public static class TestCreateListener2ServletContextListener implements ServletContextListener {
    }

    /**
     * Test listener that is used by testCreateListener3.
     */
    public static class TestCreateListener3ServletRequestListener implements ServletRequestListener {
    }

    /**
     * Test HttpSessionListener to validate createListener was called.
     */
    public static class TestCreateListener4HttpSessionListener implements HttpSessionListener {
    }

    /**
     * Test servlet that is used by testGetServletRegistration2.
     */
    public static class TestGetServletRegistration2Servlet extends HttpServlet {
    }

    /**
     * Test servlet that is used by testGetServletRegistrations.
     */
    public static class TestGetServletRegistrationsServlet extends HttpServlet {
    }
}
