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

import org.apache.jasper.servlet.JspServlet;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for the NanoPiranha class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class NanoPiranhaTest {

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .servlet("TestHelloWorldServlet", new TestHelloWorldServlet())
                .build();

        NanoRequest request = new NanoRequestBuilder()
                .method("GET")
                .servletPath("/index.html")
                .build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoResponse response = new NanoResponseBuilder()
                .outputStream(outputStream)
                .build();

        piranha.service(request, response);
        assertEquals("Hello World", outputStream.toString());
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService2() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .directoryResource("src/test/jsp")
                .servlet("JSP Servlet", new JspServlet())
                .servletInitParam("JSP Servlet", "classpath", System.getProperty("jdk.module.path") +
                        System.getProperty("path.separator") +
                        System.getProperty("java.class.path"))
                .servletInitParam("JSP Servlet", "compilerSourceVM", "1.8")
                .servletInitParam("JSP Servlet", "compilerTargetVM", "1.8")
                .build();

        NanoRequest request = new NanoRequestBuilder()
                .method("GET")
                .servletPath("/index.jsp")
                .build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoResponse response = new NanoResponseBuilder()
                .outputStream(outputStream)
                .build();

        piranha.service(request, response);
        assertTrue(outputStream.toString().contains("Hello JSP"));
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService3() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .directoryResource("src/test/jsp")
                .servlet("JSP Servlet", new JspServlet())
                .servletInitParam("JSP Servlet", "classpath", System.getProperty("jdk.module.path") +
                        System.getProperty("path.separator") +
                        System.getProperty("java.class.path"))
                .servletInitParam("JSP Servlet", "compilerSourceVM", "1.8")
                .servletInitParam("JSP Servlet", "compilerTargetVM", "1.8")
                .build();

        NanoRequest request = new NanoRequestBuilder()
                .method("GET")
                .servletPath("/date.jsp")
                .build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoResponse response = new NanoResponseBuilder()
                .outputStream(outputStream)
                .build();

        piranha.service(request, response);
        assertTrue(outputStream.toString().contains("Date = "));
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService4() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .servlet("TestQueryStringServlet", new TestQueryStringServlet())
                .build();

        NanoRequest request = new NanoRequestBuilder()
                .method("GET")
                .servletPath("/index.html")
                .queryString("q=value")
                .build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoResponse response = new NanoResponseBuilder()
                .outputStream(outputStream)
                .build();

        piranha.service(request, response);
        assertEquals("q=value", outputStream.toString());
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testService5() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .servlet("TestHeaderServlet", new TestHeaderServlet())
                .build();

        NanoRequest request = new NanoRequestBuilder()
                .method("GET")
                .servletPath("/index.xhtml")
                .header("header", "value")
                .build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoResponse response = new NanoResponseBuilder()
                .bodyOnly(true)
                .outputStream(outputStream)
                .build();

        piranha.service(request, response);
        assertEquals("value", outputStream.toString());
    }

    /**
     * A test Hello World servlet.
     */
    class TestHelloWorldServlet extends HttpServlet {

        /**
         * Perform a GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            response.getWriter().print("Hello World");
        }
    }

    /**
     * A test servlet to verify query string parsing.
     */
    class TestQueryStringServlet extends HttpServlet {

        /**
         * Perform a GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            response.getWriter().print(request.getQueryString());
        }
    }

    /**
     * A test servlet to verify header parsing.
     */
    class TestHeaderServlet extends HttpServlet {

        /**
         * Perform a GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            response.getWriter().print(request.getHeader("header"));
        }
    }
}
