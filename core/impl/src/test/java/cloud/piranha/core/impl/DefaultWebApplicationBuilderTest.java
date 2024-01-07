/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
import cloud.piranha.resource.api.ResourceManager;
import cloud.piranha.resource.impl.DirectoryResource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for the DefaultWebApplicationBuilder class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationBuilderTest {

    /**
     * Test build method.
     */
    @Test
    void testBuild() {
        assertNotNull(new DefaultWebApplicationBuilder().build());
    }

    /**
     * Test constructor.
     */
    @Test
    void testConstructor() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals(webApplication, new DefaultWebApplicationBuilder(webApplication).build());
    }
    
    /**
     * Test directoryResource method.
     */
    @Test
    void testDirectoryResource() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .directoryResource("target")
                .build();
        ResourceManager manager = webApplication.getManager().getResourceManager();
        assertTrue(manager.getResourceList().get(0) instanceof DirectoryResource);
    }

    /**
     * Test filter method.
     */
    @Test
    void testFilter() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .filter("testFilter", "TestFilter")
                .build();
        assertEquals("TestFilter",
                webApplication.getFilterRegistration("testFilter").getClassName());
    }

    /**
     * Test filter method.
     */
    @Test
    void testFilter2() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .filter("testFilter", TestFilter.class)
                .build();
        assertEquals(TestFilter.class.getName(),
                webApplication.getFilterRegistration("testFilter").getClassName());
    }

    /**
     * Test filter method.
     */
    @Test
    void testFilter3() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .filter("testFilter", new TestFilter())
                .build();
        assertEquals(TestFilter.class.getName(),
                webApplication.getFilterRegistration("testFilter").getClassName());
    }

    /**
     * Test filterInitParam method.
     */
    @Test
    void testFilterInitParam() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .filter("testFilter", TestFilter.class)
                .filterInitParam("testFilter", "name", "value")
                .build();
        assertEquals("value",
                webApplication.getFilterRegistration("testFilter").getInitParameter("name"));
    }

    /**
     * Test initParam method.
     */
    @Test
    void testInitParam() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .initParam("name", "value")
                .build();
        assertEquals("value", webApplication.getInitParameter("name"));
    }

    /**
     * Test servlet method.
     */
    @Test
    void testServlet() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("testServlet", "TestServlet")
                .build();
        assertEquals("TestServlet",
                webApplication.getServletRegistration("testServlet").getClassName());
    }

    /**
     * Test servlet method.
     */
    @Test
    void testServlet2() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("testServlet", TestServlet.class)
                .build();
        assertEquals(TestServlet.class.getName(),
                webApplication.getServletRegistration("testServlet").getClassName());
    }

    /**
     * Test servlet method.
     */
    @Test
    void testServlet3() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("testServlet", new TestServlet())
                .build();
        assertEquals(TestServlet.class.getName(),
                webApplication.getServletRegistration("testServlet").getClassName());
    }

    /**
     * Test servletInitParam method.
     */
    @Test
    void testServletInitParam() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("testServlet", TestServlet.class)
                .servletInitParam("testServlet", "name", "value")
                .build();
        assertEquals("value",
                webApplication.getServletRegistration("testServlet").getInitParameter("name"));
    }
    
    /**
     * Test servletMapping method.
     */
    @Test
    void testServletMapping() {
        WebApplication webApplication = new DefaultWebApplicationBuilder()
                .servlet("testServlet", TestServlet.class)
                .servletMapping("testServlet", "/test")
                .build();
        assertEquals("/test",
                webApplication.getServletRegistration("testServlet").getMappings().iterator().next());
    }

    /**
     * Test webApplication method.
     */
    @Test
    void testWebApplication() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertEquals(webApplication,
                new DefaultWebApplicationBuilder().webApplication(webApplication).build());
    }

    /**
     * A test filter.
     */
    public class TestFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                FilterChain chain) throws IOException, ServletException {
        }
    }

    /**
     * A test servlet.
     */
    public class TestServlet extends HttpServlet {
    }
}
