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
package cloud.piranha.extension.servletannotations;

import cloud.piranha.core.api.FilterEnvironment;
import cloud.piranha.core.api.ServletEnvironment;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.core.impl.DefaultWebApplicationExtensionContext;
import cloud.piranha.extension.slim.security.SlimSecurityManager;
import cloud.piranha.extension.standard.annotationscan.StandardAnnotationScanAnnotationManager;
import cloud.piranha.extension.standard.annotationscan.StandardAnnotationScanInitializer;
import cloud.piranha.resource.impl.ClassResource;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletRegistration;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the WebXmlExtension class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ServletAnnotationsExtensionTest {

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(TestServlet.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        ServletRegistration registration = webApplication.getServletRegistration("TestServlet");
        assertNotNull(registration);
    }

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(Test2Servlet.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        ServletRegistration registration = webApplication.getServletRegistration(Test2Servlet.class.getName());
        assertNotNull(registration);
    }

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup3() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(Test3Servlet.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        ServletRegistration registration = webApplication.getServletRegistration("Test3Servlet");
        assertNotNull(registration);
        assertFalse(registration.getMappings().isEmpty());
    }

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup4() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(Test4Servlet.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        ServletRegistration registration = webApplication.getServletRegistration("Test4Servlet");
        assertNotNull(registration);
        ServletEnvironment servletEnvironment = (ServletEnvironment) registration;
        assertEquals("value", servletEnvironment.getInitParameter("name"));
    }

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup5() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(TestFilter.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        FilterRegistration registration = webApplication.getFilterRegistration("TestFilter");
        assertNotNull(registration);
    }

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup6() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(Test2Filter.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        FilterRegistration registration = webApplication.getFilterRegistration(Test2Filter.class.getName());
        assertNotNull(registration);
    }

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup7() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(Test3Filter.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        FilterRegistration registration = webApplication.getFilterRegistration("Test3Filter");
        assertNotNull(registration);
        FilterEnvironment filterEnvironment = (FilterEnvironment) registration;
        assertEquals("value", filterEnvironment.getInitParameter("name"));
    }

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup8() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(Test4Filter.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        FilterRegistration registration = webApplication.getFilterRegistration("Test4Filter");
        assertNotNull(registration);
        assertFalse(registration.getUrlPatternMappings().isEmpty());
    }

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup9() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(Test5Servlet.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        ServletRegistration registration = webApplication.getServletRegistration("Test5Servlet");
        assertNotNull(registration);
        ServletEnvironment servletEnvironment = (ServletEnvironment) registration;
        assertNotNull(servletEnvironment.getMultipartConfig());
    }

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnStartup10() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        webApplication.addInitializer(new StandardAnnotationScanInitializer());
        DefaultWebApplicationClassLoader classLoader = new DefaultWebApplicationClassLoader(new File(""));
        classLoader.getResourceManager().addResource(new ClassResource(TestListener.class.getName()));
        webApplication.setClassLoader(classLoader);
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletAnnotationsExtension.class);
        context.configure(webApplication);
        webApplication.initialize();
        assertNotNull(webApplication.getAttribute("listenerAdded"));
    }
}
