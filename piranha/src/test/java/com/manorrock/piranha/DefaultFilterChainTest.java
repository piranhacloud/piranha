/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultFilterChain class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultFilterChainTest {

    /**
     * Test doFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testDoFilter() throws Exception {
        DefaultFilterChain chain = new DefaultFilterChain();
        chain.doFilter(null, null);
    }

    /**
     * Test doFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testDoFilter2() throws Exception {
        DefaultFilterChain chain = new DefaultFilterChain(new DefaultServlet());
        DefaultWebApplication webApplication = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApplication);
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApplication);
        TestServletOutputStream outputStream = new TestServletOutputStream();
        outputStream.setResponse(response);
        response.setOutputStream(outputStream);
        request.setContextPath("/");
        chain.doFilter(request, response);
        assertEquals(200, response.getStatus());
    }

    /**
     * Test doFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testDoFilter3() throws Exception {
        DefaultFilterChain terminatingChain = new DefaultFilterChain(new DefaultServlet());
        DefaultFilterChain chain = new DefaultFilterChain(new TestPassthroughFilter(), terminatingChain);
        DefaultWebApplication webApplication = new DefaultWebApplication();
        TestHttpServletRequest request = new TestHttpServletRequest();
        request.setWebApplication(webApplication);
        TestHttpServletResponse response = new TestHttpServletResponse();
        response.setWebApplication(webApplication);
        TestServletOutputStream outputStream = new TestServletOutputStream();
        outputStream.setResponse(response);
        response.setOutputStream(outputStream);
        request.setContextPath("/");
        chain.doFilter(request, response);
        assertEquals(200, response.getStatus());
    }
}
