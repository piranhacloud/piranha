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

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationClassLoader;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.embedded.EmbeddedResponseBuilder;
import cloud.piranha.extension.standard.annotationscan.StandardAnnotationScanInitializer;
import cloud.piranha.extension.standard.annotationscan.StandardAnnotationScanAnnotationManager;
import cloud.piranha.extension.webxml.WebXmlInitializer;
import cloud.piranha.resource.impl.ClassResource;
import cloud.piranha.resource.impl.DirectoryResource;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the @WebServlet annotation.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class WebServletTest {

    /**
     * Test @WebServlet annotation.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testWebServletUrl1() throws Exception {
        DefaultWebApplication application = new DefaultWebApplication();
        application.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        application.addResource(new DirectoryResource("src/test/webapp/webservlet"));
        DefaultWebApplicationClassLoader classLoader
                = new DefaultWebApplicationClassLoader(new File("src/test/webapp/webservlet"));
        classLoader.getResourceManager().addResource(new ClassResource(TestServlet.class.getName()));
        application.setClassLoader(classLoader);
        application.addInitializer(new StandardAnnotationScanInitializer());
        application.addInitializer(new WebXmlInitializer());
        application.addInitializer(new ServletAnnotationsInitializer());
        application.initialize();
        application.start();
        EmbeddedRequest request = new EmbeddedRequestBuilder().servletPath("/url1").build();
        EmbeddedResponse response = new EmbeddedResponseBuilder().bodyOnly(true).build();
        application.service(request, response);
        application.stop();
        assertEquals("Hurray, it worked!\n", response.getResponseAsString());
    }

    /**
     * Test @WebServlet annotation.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testWebServletUrl1b() throws Exception {
        DefaultWebApplication application = new DefaultWebApplication();
        application.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        application.addResource(new DirectoryResource("src/test/webapp/webservlet"));
        DefaultWebApplicationClassLoader classLoader
                = new DefaultWebApplicationClassLoader(new File("src/test/webapp/webservlet"));
        classLoader.getResourceManager().addResource(new ClassResource(TestServlet.class.getName()));
        application.setClassLoader(classLoader);
        application.addInitializer(new StandardAnnotationScanInitializer());
        application.addInitializer(new WebXmlInitializer());
        application.addInitializer(new ServletAnnotationsInitializer());
        application.initialize();
        application.start();
        EmbeddedRequest request = new EmbeddedRequestBuilder().servletPath("/url1b").build();
        EmbeddedResponse response = new EmbeddedResponseBuilder().bodyOnly(true).build();
        application.service(request, response);
        application.stop();
        assertEquals("Hurray, it worked!\n", response.getResponseAsString());
    }

    /**
     * Test @WebServlet annotation.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testWebServletUrl2() throws Exception {
        DefaultWebApplication application = new DefaultWebApplication();
        application.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        application.addResource(new DirectoryResource("src/test/webapp/webservlet"));
        DefaultWebApplicationClassLoader classLoader
                = new DefaultWebApplicationClassLoader(new File("src/test/webapp/webservlet"));
        classLoader.getResourceManager().addResource(new ClassResource(TestServlet.class.getName()));
        application.setClassLoader(classLoader);
        application.addInitializer(new StandardAnnotationScanInitializer());
        application.addInitializer(new WebXmlInitializer());
        application.addInitializer(new ServletAnnotationsInitializer());
        application.initialize();
        application.start();
        EmbeddedRequest request = new EmbeddedRequestBuilder().servletPath("/url2/test").build();
        EmbeddedResponse response = new EmbeddedResponseBuilder().bodyOnly(true).build();
        application.service(request, response);
        application.stop();
        assertEquals("Hurray, it worked!\n", response.getResponseAsString());
    }

    /**
     * Test @WebServlet annotation.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testWebServletUrl2b() throws Exception {
        DefaultWebApplication application = new DefaultWebApplication();
        application.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        application.addResource(new DirectoryResource("src/test/webapp/webservlet"));
        DefaultWebApplicationClassLoader classLoader
                = new DefaultWebApplicationClassLoader(new File("src/test/webapp/webservlet"));
        classLoader.getResourceManager().addResource(new ClassResource(TestServlet.class.getName()));
        application.setClassLoader(classLoader);
        application.addInitializer(new StandardAnnotationScanInitializer());
        application.addInitializer(new WebXmlInitializer());
        application.addInitializer(new ServletAnnotationsInitializer());
        application.initialize();
        application.start();
        EmbeddedRequest request = new EmbeddedRequestBuilder().servletPath("/url2b/test").build();
        EmbeddedResponse response = new EmbeddedResponseBuilder().bodyOnly(true).build();
        application.service(request, response);
        application.stop();
        assertEquals("Hurray, it worked!\n", response.getResponseAsString());
    }

    /**
     * Test @WebServlet annotation.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testWebServletUrl3() throws Exception {
        DefaultWebApplication application = new DefaultWebApplication();
        application.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        application.addResource(new DirectoryResource("src/test/webapp/webservlet"));
        DefaultWebApplicationClassLoader classLoader
                = new DefaultWebApplicationClassLoader(new File("src/test/webapp/webservlet"));
        classLoader.getResourceManager().addResource(new ClassResource(TestServlet.class.getName()));
        application.setClassLoader(classLoader);
        application.addInitializer(new StandardAnnotationScanInitializer());
        application.addInitializer(new WebXmlInitializer());
        application.addInitializer(new ServletAnnotationsInitializer());
        application.initialize();
        application.start();
        EmbeddedRequest request = new EmbeddedRequestBuilder().servletPath("/my/extension/test.url3").build();
        EmbeddedResponse response = new EmbeddedResponseBuilder().bodyOnly(true).build();
        application.service(request, response);
        application.stop();
        assertEquals("Hurray, it worked!\n", response.getResponseAsString());
    }

    /**
     * Test @WebServlet annotation.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testWebServletUrl3b() throws Exception {
        DefaultWebApplication application = new DefaultWebApplication();
        application.getManager().setAnnotationManager(new StandardAnnotationScanAnnotationManager());
        application.addResource(new DirectoryResource("src/test/webapp/webservlet"));
        DefaultWebApplicationClassLoader classLoader
                = new DefaultWebApplicationClassLoader(new File("src/test/webapp/webservlet"));
        classLoader.getResourceManager().addResource(new ClassResource(TestServlet.class.getName()));
        application.setClassLoader(classLoader);
        application.addInitializer(new StandardAnnotationScanInitializer());
        application.addInitializer(new WebXmlInitializer());
        application.addInitializer(new ServletAnnotationsInitializer());
        application.initialize();
        application.start();
        EmbeddedRequest request = new EmbeddedRequestBuilder().servletPath("/my/extension/test.url3b").build();
        EmbeddedResponse response = new EmbeddedResponseBuilder().bodyOnly(true).build();
        application.service(request, response);
        application.stop();
        assertEquals("Hurray, it worked!\n", response.getResponseAsString());
    }
}
