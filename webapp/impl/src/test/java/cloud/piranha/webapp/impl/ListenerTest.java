/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.webapp.impl;

import cloud.piranha.webapp.impl.DefaultWebApplication;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.http.HttpSessionListener;

import java.util.EventListener;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * The JUnit tests for testing everything related to the addListener and
 * createListener methods.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ListenerTest {

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener("ClassNotFoundListener");
    }

    /**
     * Test addListener method.
     */
    @Test(expected = IllegalStateException.class)
    public void testAddListener2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.initialize();
        webApplication.addListener("ClassNotFoundListener");
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener3() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(TestHttpSessionListener.class.getName());
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener4() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(TestHttpSessionListener.class);
    }

    /**
     * Test addListener method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddListener5() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(TestInvalidTypeOfListener.class);
    }

    /**
     * Test addListener method.
     */
    @Test
    public void testAddListener6() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(TestBrokenHttpSessionListener.class);
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateListener() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.createListener(TestInvalidTypeOfListener.class);
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testCreateListener2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.createListener(
                TestHttpSessionListener.class));
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testCreateListener3() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.createListener(
                TestServletContextListener.class));
    }

    /**
     * Test createListener method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testCreateListener4() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.createListener(
                TestServletRequestListener.class));
    }

    /**
     * Test HTTP session listener.
     */
    public static class TestHttpSessionListener implements HttpSessionListener {

        /**
         * Constructor.
         */
        public TestHttpSessionListener() {
        }
    }

    /**
     * Test event listener.
     */
    public static class TestInvalidTypeOfListener implements EventListener {

        /**
         * Constructor.
         */
        public TestInvalidTypeOfListener() {
        }
    }

    /**
     * Test Servlet context listener.
     */
    public static class TestServletContextListener implements ServletContextListener {

        /**
         * Constructor.
         */
        public TestServletContextListener() {
        }
    }

    /**
     * Test Servlet request listener.
     */
    public static class TestServletRequestListener implements ServletRequestListener {

        /**
         * Constructor.
         */
        public TestServletRequestListener() {
        }
    }

    /**
     * Test HTTP session listener.
     */
    public static class TestBrokenHttpSessionListener implements HttpSessionListener {

        /**
         * Constructor.
         */
        public TestBrokenHttpSessionListener() {
            throw new UnsupportedOperationException();
        }
    }
}
