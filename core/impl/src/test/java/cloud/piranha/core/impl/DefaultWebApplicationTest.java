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

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.resource.impl.DefaultResourceManager;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRegistration.Dynamic;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestEvent;
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
import java.util.Collection;
import java.util.Date;
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
 * The JUnit tests for the DefaultWebApplication class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationTest {

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter() {
        WebApplication webApplication = new DefaultWebApplication();
        webApplication.addFilter("TestAddFilter", TestAddFilterFilter.class);
        assertNotNull(webApplication.getFilterRegistration("TestAddFilter"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/testAddFilter3");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(200, response.getStatus());
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.addFilter("TestAddFilter6", "doesnotexit"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter7() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.addFilter(null, "filter name is null so throw IllegalArgumentException"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter9() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.addFilter(null, Filter.class));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter10() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        assertNotNull(webApplication.addFilter("filter", Filter.class));
        assertNull(webApplication.addFilter("filter", Filter.class));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter11() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.addFilter("filter", "InCompleteRegistrationFilter"));
        assertNull(webApplication.addFilter("filter", "InCompleteRegistrationFilter"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter12() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.addFilter("filter", "InCompleteRegistrationFilter"));
        assertNotNull(webApplication.getFilterRegistration("filter"));
    }

    /**
     * Test addFilter method.
     */
    @Test
    void testAddFilter13() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addFilter("TestAddFilterMapping", new Filter() {

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                request.getServletContext().setAttribute("TestAddFilterMapping", true);
            }
        });
        webApplication.addFilterMapping("TestAddFilterMapping", "/*");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setServletPath("/testAddFilterMapping");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addInitializer(TestAddInitializer2Initializer.class.getName());
        webApplication.initialize();
        assertNotNull(webApplication.getAttribute("TestAddInitializer2"));
    }

    /**
     * Test addInitialized method
     */
    @Test
    void testAddInitializer3() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.addJspFile("TestAddJspFile", "testAddJspFile.jsp"));
    }

    /**
     * Test addJspFile method.
     */
    @Test
    void testAddJspFile2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        WebApplication webApplication = new DefaultWebApplication();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.addJspFile(null, "testAddJspFile3.jsp"));
    }

    /**
     * Test addMimeType method.
     */
    @Test
    void testAddMimeType() {
        WebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getMimeType("my.class"));
        webApplication.addMimeType("class", "application/x-java-class");
        assertEquals("application/x-java-class", webApplication.getMimeType("my.class"));
    }
    
    /**
     * Test addListener method.
     */
    @Test
    void testAddListener() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        assertThrows(IllegalStateException.class, ()
                -> webApplication.addListener("TestAddListener"));
    }

    /**
     * Test addListener method.
     */
    @Test
    void testAddListener2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.addListener(TestAddListener2Listener.class));
    }

    /**
     * Test addListener method.
     *
     * <p>
     * Add a ServletRequestListener and validate the requestDestroyed method is
     * called when the request is processed.
     * </p>
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddListener3() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new ServletRequestListener() {
            @Override
            public void requestDestroyed(ServletRequestEvent event) {
                event.getServletContext().setAttribute("testAddListener3", true);
            }
        });
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testAddListener3"));
    }

    /**
     * Test addListener method.
     *
     * <p>
     * Add a ServletRequestListener and validate the requestInitialized method
     * is called when the request is processed.
     * </p>
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddListener4() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new ServletRequestListener() {
            @Override
            public void requestInitialized(ServletRequestEvent event) {
                event.getServletContext().setAttribute("testAddListener4", true);
            }
        });
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertNotNull(webApplication.getAttribute("testAddListener4"));
    }

    /**
     * Test addListener method.
     *
     * <p>
     * Add a ServletContextListener and validate the contextDestroyed method is
     * called when the ServletContext gets destroyed.
     * </p>
     */
    @Test
    void testAddListener5() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new ServletContextListener() {
            @Override
            public void contextDestroyed(ServletContextEvent event) {
                event.getServletContext().setAttribute("testAddListener5", true);
            }
        });
        webApplication.initialize();
        webApplication.destroy();
        assertNotNull(webApplication.getAttribute("testAddListener5"));
    }

    /**
     * Test addListener method.
     *
     * <p>
     * Add a ServletContextListener and validate the contextInitialized method
     * is called when the ServletContext gets initialized.
     * </p>
     */
    @Test
    void testAddListener6() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent event) {
                event.getServletContext().setAttribute("testAddListener6", true);
            }
        });
        webApplication.initialize();
        webApplication.destroy();
        assertNotNull(webApplication.getAttribute("testAddListener6"));
    }

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
     * Verify when we add twice addMapping will return a empty set.
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
     * Test addResource method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testAddResource() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/testAddServlet");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.addServlet("TestAddServlet2Servlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException {
                response.getWriter().println("TestAddServlet2Servlet");
            }
        }));
        webApplication.addServletMapping("TestAddServlet2Servlet", "/testAddServlet2");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/testAddServlet2");
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(200, response.getStatus());
    }

    /**
     * Test addServlet method.
     */
    @Test
    void testAddServlet3() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        ServletRegistration.Dynamic dynamic = webApplication.addServlet(
                "TestAddServlet3Servlet", Servlet.class);
        assertNotNull(dynamic);
    }

    /**
     * Test addServlet method.
     */
    @Test
    void testAddServlet4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.addServlet("TestAddServletMappingServlet",
                new HttpServlet() {

            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException {
                response.getWriter().println("TestAddServletMappingServlet");
            }
        }));
        webApplication.addServletMapping("TestAddServletMappingServlet",
                "/testAddServletMappingServlet");
        WebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/testAddServletMappingServlet");
        request.setWebApplication(webApplication);
        WebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertThrows(ServletException.class, () -> webApplication.createFilter(Filter.class));
    }

    /**
     * Test createListener method.
     */
    @Test
    void testCreateListener() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertThrows(IllegalArgumentException.class, ()
                -> webApplication.createListener(TestCreateListenerEventListener.class));
    }

    /**
     * Test createListener method.
     */
    @Test
    void testCreateListener2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.createListener(
                TestCreateListener4HttpSessionListener.class));
    }

    /**
     * Test createServlet method.
     */
    @Test
    void testCreateServlet() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertThrows(ServletException.class, () -> webApplication.createServlet(Servlet.class));
    }

    /**
     * Test declareRoles method.
     */
    @Test
    void testDeclareRoles() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.declareRoles("testDeclareRoles");
    }

    /**
     * Test destroy method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testDestroy() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
     * Test getAsync.
     *
     * REVIEW LOCATION
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
     * REVIEW LOCATION
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
     * Test getAttribute method.
     */
    @Test
    void testGetAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertThrows(NullPointerException.class, () -> webApplication.getAttribute(null));
    }

    /**
     * Test getAttributeNames method.
     */
    @Test
    void testGetAttributeNames() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getAttributeNames());
    }

    /**
     * Test getContext method.
     */
    @Test
    void testGetContext() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getContext("/context"));
    }

    /**
     * Test getContextPath method.
     */
    @Test
    void testGetContextPath() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals("", webApplication.getContextPath());
    }

    /**
     * Test getDefaultServlet method.
     */
    @Test
    void testGetDefaultServlet() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getDefaultServlet());
    }

    /**
     * Test getDefaultSessionTrackingModes method.
     */
    @Test
    void testGetDefaultSessionTrackingModes() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertTrue(webApplication.getDefaultSessionTrackingModes().contains(COOKIE));
    }

    /**
     * Test getEffectiveMajorVersion method.
     */
    @Test
    void testGetEffectiveMajorVersion() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals(6, webApplication.getEffectiveMajorVersion());
    }

    /**
     * Test getEffectiveMinorVersion method.
     */
    @Test
    void testGetEffectiveMinorVersion() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals(0, webApplication.getEffectiveMinorVersion());
    }

    /**
     * Test getEffectiveSessionTrackingMode method.
     */
    @Test
    void testGetEffectiveSessionTrackingMode() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertTrue(webApplication.getEffectiveSessionTrackingModes().contains(COOKIE));
    }

    /**
     * Test getEffectiveSessionTrackingModes method.
     */
    @Test
    void testGetEffectiveSessionTrackingModes2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        Set<SessionTrackingMode> trackingModes = EnumSet.of(URL);
        webApplication.setSessionTrackingModes(trackingModes);
        assertTrue(webApplication.getEffectiveSessionTrackingModes().contains(URL));
    }

    /**
     * Test getFilterRegistration method.
     */
    @Test
    void testGetFilterRegistration() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addFilter("testGetFilterRegistration2Filter", "TestGetFilterRegistration2Filter");
        assertNotNull(webApplication.getFilterRegistration("testGetFilterRegistration2Filter"));
    }

    /**
     * Test getFilterRegistrations method.
     */
    @Test
    void testGetFilterRegistrations() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addFilter("testGetFilterRegistrationsFilter", "testGetFilterRegistrationsFilter");
        assertFalse(webApplication.getFilterRegistrations().isEmpty());
    }

    /**
     * Test getInitParameter method.
     */
    @Test
    void testGetInitParameter() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setInitParameter("testGetInitParameter", Boolean.TRUE.toString());
        assertEquals("true", webApplication.getInitParameter("testGetInitParameter"));
    }

    /**
     * Test getInitParameterNames method.
     */
    @Test
    void testGetInitParameterNames() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getInitializers());
        assertTrue(webApplication.getInitializers().isEmpty());
    }

    /**
     * Test getJspConfigDescriptor method.
     *
     * @jaarta.assertion Servlet:JAVADOC:690
     */
    @Test
    void testGetJspConfigDescriptor() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getJspConfigDescriptor());
    }

    /**
     * Test getMajorVersion method.
     */
    @Test
    void testGetMajorVersion() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals(6, webApplication.getMajorVersion());
    }

    /**
     * Test getManager method.
     */
    @Test
    void testGetManager() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getManager());
    }

    /**
     * Test getMappings method.
     */
    @Test
    void testGetMappings() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getMappings("TestGetMappingsServlet"));
        assertTrue(webApplication.getMappings("TestGetMappingsServlet").isEmpty());
    }

    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getMimeType("index.html"));
        assertEquals("text/html", webApplication.getMimeType("index.html"));
    }
    
    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals("text/css", webApplication.getMimeType("TEST.CSS"));
        assertEquals("text/javascript", webApplication.getMimeType("TEST.JS"));
    }

    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType3() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getMimeType("this_maps_to.null"));
    }

    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addMimeType("class", "application/x-java-class");
        assertEquals(webApplication.getMimeType("my.class"), "application/x-java-class");
    }

    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType5() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getMimeType("myclass"));
    }

    /**
     * Test getMinorVersion method.
     */
    @Test
    void testGetMinorVersion() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals(0, webApplication.getMinorVersion());
    }

    /**
     * Test getNamedDispatcher method.
     */
    @Test
    void testGetNamedDispatcher() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("TestGetNamedDispatcherServlet", new HttpServlet() {
        });
        webApplication.initialize();
        assertNotNull(webApplication.getNamedDispatcher("TestGetNamedDispatcherServlet"));
    }

    /**
     * Test getNamedDispatcher method.
     */
    @Test
    void testGetNamedDispatcher2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        assertNull(webApplication.getNamedDispatcher("TestGetNamedDispatcher2Servlet"));
    }

    /**
     * Test getRealPath method.
     */
    @Test
    void testGetRealPath() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getRealPath("index.html"));
    }

    /**
     * Test getRealPath method.
     */
    @Test
    void testGetRealPath2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApplication.getRealPath("/src/main/java"));
    }

    /**
     * Test getRequest method.
     */
    @Test
    void testGetRequest() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(webApplication.getRequest(response));
    }

    /**
     * Test getRequestCharacterEncoding method.
     */
    @Test
    void testGetRequestCharacterEncoding() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getRequestCharacterEncoding());
        webApplication.setRequestCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApplication.getRequestCharacterEncoding());
    }

    /**
     * Test getRequestDispatcher method.
     */
    @Test
    void testGetRequestDispatcher() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getResource("/testGetResource"));
    }

    /**
     * Test getResourceAsStream method.
     */
    @Test
    void testGetResourceAsStream() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource(new File(".")));
        assertNotNull(webApplication.getResourceAsStream("/pom.xml"));
    }

    /**
     * Test getResourcePaths method.
     */
    @Test
    void testGetResourcePaths() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getResourcePaths("/testGetResourcePaths/"));
        assertNull(webApplication.getResourcePaths(null));
    }

    /**
     * Test getResourcePaths method.
     *
     * <p>
     * Simulating the Javadoc example of the getResourcePaths method.
     * </p>
     */
    @Test
    void testGetResourcePaths2() {
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

    /**
     * Test getResourcePaths method.
     */
    @Test
    void testGetResourcePaths3() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertThrows(IllegalArgumentException.class, () -> webApp.getResourcePaths(""));
    }

    /**
     * Test getResourcePaths method.
     */
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
     * Test getResponse method.
     */
    @Test
    void testGetResponse() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        webApplication.linkRequestAndResponse(request, response);
        assertNotNull(webApplication.getResponse(request));
    }

    /**
     * Test getResponseCharacterEncoding.
     */
    @Test
    void testGetResponseCharacterEncoding() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getResponseCharacterEncoding());
        webApplication.setResponseCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApplication.getResponseCharacterEncoding());
    }

    /**
     * Test getSecurityManager.
     *
     * REVIEW LOCATION.
     */
    @Test
    void testGetSecurityManager() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        assertNotNull(webApp.getManager().getSecurityManager());
    }

    /**
     * Test getServerInfo method.
     */
    @Test
    void testGetServerInfo() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getServerInfo());
        assertFalse(webApplication.getServerInfo().trim().equals(""));
    }

    /**
     * Test getServletContextId method.
     */
    @Test
    void testGetServletContextId() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getServletContextId());
    }

    /**
     * Test getServletContextName method.
     */
    @Test
    void testGetServletContextName() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getServletContextName());
    }

    /**
     * Test getServletRegistration method.
     */
    @Test
    void testGetServletRegistration() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("TestGetServletRegistrationServlet", "TestGetServletRegistrationServlet");
        assertNotNull(webApplication.getServletRegistration("TestGetServletRegistrationServlet"));
    }

    /**
     * Test getServletRegistrations method.
     */
    @Test
    void testGetServletRegistration2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("TestGetServletRegistration2Servlet",
                TestGetServletRegistration2Servlet.class);
        assertNotNull(webApplication.getServletRegistration("TestGetServletRegistration2Servlet"));
    }

    /**
     * Test getServletRegistrations method.
     */
    @Test
    void testGetServletRegistrations() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("servlet", TestGetServletRegistrationsServlet.class);
        assertFalse(webApplication.getServletRegistrations().isEmpty());
    }

    /**
     * Test getServletRegistrations method.
     */
    @Test
    void testGetServletRegistrations2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getServletRegistrations());
        assertTrue(webApplication.getServletRegistrations().isEmpty());
    }

    /**
     * Test getSessionCookieConfig method.
     */
    @Test
    void testGetSessionCookieConfig() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getSessionCookieConfig());
    }

    /**
     * Test getSessionTimeout method.
     */
    @Test
    void testGetSessionTimeout() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertTrue(webApplication.getSessionTimeout() > 0);
    }

    /**
     * Test getVirtualServerName method.
     */
    @Test
    void testGetVirtualServerName() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals("server", webApplication.getVirtualServerName());
    }

    /**
     * Test include.
     *
     * REVIEW LOCATION
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
     * REVIEW LOCATION
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
     * REVIEW LOCATION
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
     * Test initialize method.
     */
    @Test
    void testInitialize() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        assertTrue(webApplication.isInitialized());
    }

    /**
     * Test initializeDeclaredFinish method.
     */
    @Test
    void testInitializeDeclaredFinish() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initializeDeclaredFinish();
        assertFalse(webApplication.isInitialized());
    }

    /**
     * Test initializeFilters method.
     */
    @Test
    void testInitializeFilters() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initializeFilters();
        assertFalse(webApplication.isInitialized());
    }

    /**
     * Test initializeFinish method.
     */
    @Test
    void testInitializeFinish() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initializeFinish();
        assertTrue(webApplication.isInitialized());
    }

    /**
     * Test initializeInitializers method.
     */
    @Test
    void testInitializeInitializers() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initializeInitializers();
        assertFalse(webApplication.isInitialized());
    }

    /**
     * Test initializeServlets method.
     */
    @Test
    void testInitializeServlets() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initializeServlets();
        assertFalse(webApplication.isInitialized());
    }

    /**
     * Test isDistributable method.
     */
    @Test
    void testIsDistributable() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertFalse(webApplication.isDistributable());
    }

    /**
     * Test isInitialized method.
     */
    @Test
    void testIsInitialized() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertFalse(webApplication.isInitialized());
        webApplication.initialize();
        assertTrue(webApplication.isInitialized());
    }

    /**
     * Test isMetadataComplete method.
     */
    @Test
    void testIsMetadataComplete() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertFalse(webApplication.isMetadataComplete());
    }

    /**
     * Test linkRequestAndResponse method.
     */
    @Test
    void testLinkRequestAndResponse() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.log("TestLog");
    }

    /**
     * Test log method.
     */
    @Test
    void testLog2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.log("TestLog", new RuntimeException());
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.addServlet("TestServiceServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException {
                response.setStatus(403);
            }
        }));
        webApplication.addServletMapping("TestServiceServlet", "/testService");
        webApplication.initialize();
        webApplication.start();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setServletPath("/testService");
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.service(request, response);
        assertEquals(403, response.getStatus());
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
    void testSetAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setAttribute("testSetAttribute", true);
        assertTrue((boolean) webApplication.getAttribute("testSetAttribute"));
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
     * Test setClassLoader method.
     */
    @Test
    void testSetClassLoader() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getClassLoader());
        webApplication.setClassLoader(null);
        assertNull(webApplication.getClassLoader());
    }

    /**
     * Test setContextPath method.
     */
    @Test
    void testSetContextPath() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setContextPath("/setContextPath");
        assertEquals("/setContextPath", webApplication.getContextPath());
    }

    /**
     * Test setDefaultServlet method.
     */
    @Test
    void testSetDefaultServlet() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertFalse(webApplication.isDistributable());
        webApplication.setDistributable(true);
        assertTrue(webApplication.isDistributable());
    }

    /**
     * Test setEffectiveMajorVersion method.
     */
    @Test
    void testSetEffectiveMajorVersion() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals(6, webApplication.getEffectiveMajorVersion());
        webApplication.setEffectiveMajorVersion(5);
        assertEquals(5, webApplication.getEffectiveMajorVersion());
    }

    /**
     * Test setEffectiveMinoVersion method.
     */
    @Test
    void testSetEffectiveMinorVersion() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals(0, webApplication.getEffectiveMinorVersion());
        webApplication.setEffectiveMinorVersion(5);
        assertEquals(5, webApplication.getEffectiveMinorVersion());
    }

    /**
     * Test setEffectiveSessionTrackingModes method.
     */
    @Test
    void testSetEffectiveSessionTrackingModes() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertTrue(webApplication.setInitParameter("name", "value"));
        assertFalse(webApplication.setInitParameter("name", "value"));
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertThrows(NullPointerException.class, ()
                -> webApplication.setInitParameter(null, "TestSetInitParameter3"));
    }

    /**
     * Test setInitParameter method.
     */
    @Test
    void testSetInitParameter4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertFalse(webApplication.isMetadataComplete());
        webApplication.setMetadataComplete(true);
        assertTrue(webApplication.isMetadataComplete());
    }

    /**
     * Test setRequestCharacterEncoding method.
     */
    @Test
    void testSetRequestCharacterEncoding() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getRequestCharacterEncoding());
        webApplication.setRequestCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApplication.getRequestCharacterEncoding());
    }

    /**
     * Test setResponseCharacterEncoding.
     */
    @Test
    void testSetResponseCharacterEncoding() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNull(webApplication.getResponseCharacterEncoding());
        webApplication.setResponseCharacterEncoding("UTF-8");
        assertEquals("UTF-8", webApplication.getResponseCharacterEncoding());
    }

    /**
     * Test setServletContextName method.
     */
    @Test
    void testSetServletContextName() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setServletContextName("TestSetServletContextName");
        assertNotNull(webApplication.getServletContextName());
        assertEquals("TestSetServletContextName", webApplication.getServletContextName());
    }

    /**
     * Test setSessionTimeout method.
     */
    @Test
    void testSetSessionTimeout() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setSessionTrackingModes(new HashSet() {
        });
        assertTrue(webApplication.getEffectiveSessionTrackingModes().isEmpty());
    }

    /**
     * Test setVirtualServerName method.
     */
    @Test
    void testSetVirtualServerName() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.setWebApplicationRequestMapper(null);
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertTrue(webApplication.isServicing());
    }

    /**
     * Test start method.
     */
    @Test
    void testStart2() {
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
     * Test stop method.
     */
    @Test
    void testStop() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.start();
        assertTrue(webApplication.isServicing());
        webApplication.stop();
        assertFalse(webApplication.isServicing());
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    void testRemoveAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
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
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.addServlet("TestRemoveServletMappingServlet",
                new HttpServlet() {
        }));
        webApplication.addServletMapping("TestRemoveServletMappingServlet",
                "/testRemoveServletMapping");
        assertEquals("TestRemoveServletMappingServlet", webApplication
                .removeServletMapping("/testRemoveServletMapping"));
    }

    /**
     * Test unlinkRequestAndResponse method.
     */
    @Test
    void testUnlinkRequestAndResponse() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
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
