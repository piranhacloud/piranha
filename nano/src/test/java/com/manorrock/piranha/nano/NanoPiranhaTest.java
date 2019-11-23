/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.nano;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jasper.servlet.JspServlet;
import org.apache.wicket.protocol.http.WicketFilter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the NanoPiranha class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoPiranhaTest {

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testService() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .servlet(new TestHelloWorldServlet())
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
    public void testService2() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .directoryResource("src/test/jsp")
                .servlet(new JspServlet())
                .initParam("classpath", System.getProperty("java.class.path"))
                .initParam("compilerSourceVM", "1.8")
                .initParam("compilerTargetVM", "1.8")
                .initServlet()
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
    public void testService3() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .servletContext(new NanoServletContext())
                .directoryResource("src/test/jsp")
                .servlet(new JspServlet())
                .initParam("classpath", System.getProperty("java.class.path"))
                .initParam("compilerSourceVM", "1.8")
                .initParam("compilerTargetVM", "1.8")
                .initServlet()
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
    public void testService4() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .servlet(new TestQueryStringServlet())
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
    public void testService5() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .servlet(new TestHeaderServlet())
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
     * Test service method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testService6() throws Exception {
        NanoPiranha piranha = new NanoPiranhaBuilder()
                .directoryResource("src/test/wicket")
                .filter(new WicketFilter())
                .initParam("applicationClassName", "com.manorrock.piranha.nano.WicketApplication")
                .initParam("filterMappingUrlPattern", "/*")
                .initParam("wicket.configuration", "deployment")
                .initFilter()
                .build();
        
        NanoRequest request = new NanoRequestBuilder()
                .servletPath("/")
                .build();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoResponse response = new NanoResponseBuilder()
                .outputStream(outputStream)
                .build();
        
        piranha.service(request, response);
        assertTrue(outputStream.toString().contains("Hello Wicket"));
    }

    /**
     * A test Hello World servlet.
     */
    public class TestHelloWorldServlet extends HttpServlet {

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
    public class TestQueryStringServlet extends HttpServlet {

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
    public class TestHeaderServlet extends HttpServlet {

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
