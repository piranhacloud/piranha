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
package cloud.piranha.nano;

import cloud.piranha.core.impl.DefaultWebApplication;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for the NanoPiranhaBuilder class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoPiranhaBuilderTest {

    /**
     * Test filterInitParam method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testFilterInitParam() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .filter("TestFilter", new TestInitParamFilter())
                .filterInitParam("TestFilter", "name", "value")
                .build();
        NanoRequest request = new NanoRequest();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        NanoResponse response = new NanoResponseBuilder()
                .bodyOnly(true)
                .outputStream(byteOutput)
                .build();
        piranha.service(request, response);
        assertNotNull(piranha.getFilters());
        assertFalse(piranha.getFilters().isEmpty());
        assertTrue(byteOutput.toString().contains("value"));
    }

    /**
     * Test filterInitParam method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testFilterInitParam2() throws Exception {
        assertNotNull(assertThrows(RuntimeException.class, () -> {
            new NanoPiranhaBuilder()
                    .filter("TestFilter", new TestInitParam2Filter())
                    .filterInitParam("TestFilter", "name", "value")
                    .build();
        }));
    }

    /**
     * Test servletInitParam method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testServletInitParam() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .servlet("TestServlet", new TestInitParamServlet())
                .servletInitParam("TestServlet", "name", "value")
                .build();
        NanoRequest request = new NanoRequest();
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        NanoResponse response = new NanoResponseBuilder()
                .bodyOnly(true)
                .outputStream(byteOutput)
                .build();
        piranha.service(request, response);
        assertTrue(byteOutput.toString().contains("value"));
    }

    /**
     * Test servletInitParam method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testServletInitParam2() throws Exception {
        assertNotNull(assertThrows(RuntimeException.class, () -> {
            new NanoPiranhaBuilder()
                    .servlet("TestServlet", new TestInitParam2Servlet())
                    .servletInitParam("TestServlet", "name", "value")
                    .build();
        }));
    }
    
    /**
     * Test getWebApplication method.
     */
    @Test
    public void testGetWebApplication() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .webApplication(webApplication)
                .build();
        assertEquals(webApplication, piranha.getWebApplication());
    }

    /**
     * A test filter to test filterInitParam method.
     */
    public static class TestInitParamFilter implements Filter {

        private String value;

        @Override
        public void init(FilterConfig config) throws ServletException {
            value = config.getInitParameter("name");
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            response.setContentType("text/plain");
            PrintWriter writer = response.getWriter();
            writer.println(value);
        }
    }

    /**
     * A test filter to test filterInitParam method.
     */
    public static class TestInitParam2Filter implements Filter {

        @Override
        public void init(FilterConfig config) throws ServletException {
            throw new ServletException();
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        }
    }

    /**
     * A test servlet to test servletInitParam method.
     */
    public static class TestInitParamServlet extends HttpServlet {

        private String value;

        @Override
        public void init(ServletConfig config) throws ServletException {
            value = config.getInitParameter("name");
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            response.setContentType("text/plain");
            PrintWriter writer = response.getWriter();
            writer.println(value);
        }
    }

    /**
     * A test servlet to test servletInitParam method.
     */
    public static class TestInitParam2Servlet extends HttpServlet {

        @Override
        public void init(ServletConfig config) throws ServletException {
            throw new ServletException();
        }
    }
}
