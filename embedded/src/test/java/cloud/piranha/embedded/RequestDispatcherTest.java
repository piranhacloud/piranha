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
package cloud.piranha.embedded;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The tests for the RequestDispatcher API.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class RequestDispatcherTest {
    /**
     * Test include method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testInclude() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("Include1aServlet", Include1aServlet.class.getName())
                .servletMapping("Include1aServlet", "/TestServlet/*")
                .servlet("Include1bServlet", Include1bServlet.class.getName())
                .servletMapping("Include1bServlet", "/include/IncludedServlet")
                .build()
                .initialize()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/TestServlet")
                .build();
        EmbeddedResponse response = new EmbeddedResponseBuilder()
                .bodyOnly(false)
                .build();
        piranha.service(request, response);
        assertTrue(response.getResponseAsString().contains("HTTP/1.1 200"));
        assertTrue(response.getResponseAsString().endsWith("SUCCESS"));
        piranha.stop()
                .destroy();
    }

    /**
     * A test servlet for testing a RequestDispatcher include.
     */
    public static class Include1aServlet extends HttpServlet {

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            RequestDispatcher rd = request.getRequestDispatcher("/include/IncludedServlet");
            rd.include(request, response);
        }
    }

    /**
     * A test servlet for testing a RequestDispatcher include.
     */
    public static class Include1bServlet extends HttpServlet {

        /**
         * Handle GET request.
         *
         * @param request the request.
         * @param response the response.
         * @throws IOException when an I/O error occurs.
         * @throws ServletException when a Servlet error occurs.
         */
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            PrintWriter writer = response.getWriter();
            writer.print("SUCCESS");
        }
    }
}
