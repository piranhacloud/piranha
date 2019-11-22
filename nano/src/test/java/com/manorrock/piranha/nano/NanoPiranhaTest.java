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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertEquals;
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
    public void testProcess() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoPiranha piranha = new NanoPiranha();
        piranha.setServlet(new TestHelloWorldServlet());
        piranha.process(inputStream, outputStream);
        assertEquals("Hello World", outputStream.toString());
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testProcess2() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                "GET /index.html HTTP/1.1\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoPiranha piranha = new NanoPiranha();
        piranha.addFilter(new NanoRequestLineFilter());
        piranha.setServlet(new TestHelloWorldServlet());
        piranha.process(inputStream, outputStream);
        assertEquals("Hello World", outputStream.toString());
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testProcess3() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                "GET /index.html?q=value HTTP/1.1\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoPiranha piranha = new NanoPiranha();
        piranha.addFilter(new NanoRequestLineFilter());
        piranha.setServlet(new TestQueryStringServlet());
        piranha.process(inputStream, outputStream);
        assertEquals("q=value", outputStream.toString());
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testProcess4() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                "GET /index.html?q=value HTTP/1.1\r\nmyheader: myvalue\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoPiranha piranha = new NanoPiranha();
        piranha.addFilter(new NanoRequestLineFilter());
        piranha.addFilter(new NanoRequestHeadersFilter());
        piranha.setServlet(new TestHeaderServlet());
        piranha.process(inputStream, outputStream);
        assertEquals("myvalue", outputStream.toString());
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testService() throws Exception {
        NanoHttpServletRequest request = new NanoHttpServletRequest(new ByteArrayInputStream(new byte[0]));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        NanoHttpServletResponse response = new NanoHttpServletResponse(outputStream);
        request.setMethod("GET");
        request.setServletPath("/index.html");
        NanoPiranha piranha = new NanoPiranha();
        piranha.setServlet(new TestHelloWorldServlet());
        piranha.service(request, response);
        assertEquals("Hello World", outputStream.toString());
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
            response.getWriter().print(request.getHeader("myheader"));
        }
    }
}
