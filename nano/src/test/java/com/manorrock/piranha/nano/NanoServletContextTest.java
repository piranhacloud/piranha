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

import java.io.IOException;
import java.util.EventListener;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.junit.Test;

/**
 * The JUnit tests for the NanoServletContext class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoServletContextTest {

    /**
     * Test addFilter method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddFilter1() {
        NanoServletContext context = new NanoServletContext();
        context.addFilter("filter", "filterName");
    }

    /**
     * Test addFilter method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddFilter2() {
        NanoServletContext context = new NanoServletContext();
        context.addFilter("filter", new NanoRequestHeadersFilter());
    }

    /**
     * Test addFilter method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddFilter3() {
        NanoServletContext context = new NanoServletContext();
        context.addFilter("filter", Filter.class);
    }

    /**
     * Test addJspFile method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddJspFile() {
        NanoServletContext context = new NanoServletContext();
        context.addJspFile("servletName", "jspFile");
    }

    /**
     * Test addListener method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddListener() {
        NanoServletContext context = new NanoServletContext();
        context.addListener("className");
    }

    /**
     * Test addListener method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddListener2() {
        NanoServletContext context = new NanoServletContext();
        context.addListener(new EventListener() {
        });
    }

    /**
     * Test addListener method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddListener3() {
        NanoServletContext context = new NanoServletContext();
        context.addListener(EventListener.class);
    }

    /**
     * Test addServlet method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddServlet() {
        NanoServletContext context = new NanoServletContext();
        context.addServlet("servletName", "className");
    }

    /**
     * Test addServlet method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddServlet2() {
        NanoServletContext context = new NanoServletContext();
        context.addServlet("servletName", new Servlet() {
            @Override
            public void destroy() {
                throw new UnsupportedOperationException("Not supported");
            }

            @Override
            public ServletConfig getServletConfig() {
                throw new UnsupportedOperationException("Not supported");
            }

            @Override
            public String getServletInfo() {
                throw new UnsupportedOperationException("Not supported");
            }

            @Override
            public void init(ServletConfig servletConfig) throws ServletException {
                throw new UnsupportedOperationException("Not supported");
            }

            @Override
            public void service(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                throw new UnsupportedOperationException("Not supported");
            }
        });
    }

    /**
     * Test addServlet method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddServlet3() {
        NanoServletContext context = new NanoServletContext();
        context.addServlet("servletName", Servlet.class);
    }

    /**
     * Test createFilter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFilter() throws Exception {
        NanoServletContext context = new NanoServletContext();
        context.createFilter(Filter.class);
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateListener() throws Exception {
        NanoServletContext context = new NanoServletContext();
        context.createListener(EventListener.class);
    }

    /**
     * Test createServlet method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateServlet() throws Exception {
        NanoServletContext context = new NanoServletContext();
        context.createServlet(Servlet.class);
    }

    /**
     * Test declareRoles method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDeclareRoles() {
        NanoServletContext context = new NanoServletContext();
        context.declareRoles(new String[0]);
    }

    /**
     * Test getAttribute method.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetAttribute() {
        NanoServletContext context = new NanoServletContext();
        context.getAttribute("name");
    }

    /**
     * Test getAttributeNames method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testGetAttributeNames() throws Exception {
        NanoServletContext context = new NanoServletContext();
        context.getAttributeNames();
    }
}
