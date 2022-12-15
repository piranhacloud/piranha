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
package cloud.piranha.core.tests;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration.Dynamic;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for AsyncContext API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class AsyncContextTest {

    /**
     * Create the web application,
     *
     * @return the web application.
     */
    public abstract WebApplication createWebApplication();

    /**
     * Create the web application request.
     *
     * @return the web application request.
     */
    public abstract WebApplicationRequest createWebApplicationRequest();

    /**
     * Create the web application response.
     *
     * @return the web application response.
     */
    public abstract WebApplicationResponse createWebApplicationResponse();

    /**
     * Test addListener method.
     */
    @Test
    void testAddListener() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        AsyncContext context = request.startAsync();
        context.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                event.getSuppliedRequest().getServletContext().setAttribute("onComplete", "true");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
            }
        });
        context.complete();
        assertNotNull(webApplication.getAttribute("onComplete"));
    }

    /**
     * Test complete method.
     */
    @Test
    void testComplete() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        AsyncContext context = request.startAsync();
        context.complete();
        assertTrue(response.isCommitted());
    }

    /**
     * Test createListener method.
     *
     * @throws ServletException when a serious error occurs.
     */
    @Test
    void testCreateListener() throws ServletException {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        AsyncContext context = request.startAsync();
        assertNotNull(context.createListener(TestCreateListenerListener.class));
    }

    /**
     * Test createListener method.
     *
     * @throws ServletException when a serious error occurs.
     */
    @Test
    void testCreateListener2() throws ServletException {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        AsyncContext context = request.startAsync();
        assertThrows(ServletException.class,
                () -> {
                    context.createListener(TestCreateListener2Listener.class);
                });
    }

    /**
     * Test dispatch method.
     */
    @Test
    void testDispatch() {
        WebApplication webApplication = createWebApplication();
        Dynamic registration = webApplication.addServlet("TestDispatchServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                AsyncContext context = request.startAsync();
                context.dispatch("/testDispatchB");
                context.dispatch("/testDispatchC");
            }
        });
        registration.setAsyncSupported(true);
        webApplication.addServletMapping("TestDispatchServlet", "/testDispatch");
        webApplication.initialize();
        webApplication.start();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setServletPath("/testDispatch");
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        assertThrows(IllegalStateException.class,
                () -> webApplication.service(request, response));
    }

    /**
     * Test getRequest method.
     */
    @Test
    void testGetRequest() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        AsyncContext context = request.startAsync();
        assertNotNull(context.getRequest());
    }

    /**
     * Test getResponse method.
     */
    @Test
    void testGetResponse() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        AsyncContext context = request.startAsync();
        assertNotNull(context.getResponse());
    }

    /**
     * Test getTimeout method.
     */
    @Test
    void testGetTimeout() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        AsyncContext context = request.startAsync();
        assertEquals(30000, context.getTimeout());
    }

    /**
     * Test hasOriginalRequestAndResponse method.
     */
    @Test
    void testHasOriginalRequestAndResponse() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        AsyncContext context = request.startAsync();
        assertTrue(context.hasOriginalRequestAndResponse());
        assertEquals(request, context.getRequest());
        assertEquals(response, context.getResponse());
    }

    /**
     * Test setTimeout method.
     */
    @Test
    void testSetTimeout() {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAsyncSupported(true);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        AsyncContext context = request.startAsync();
        context.setTimeout(60000);
        assertEquals(60000, context.getTimeout());
    }

    /**
     * Listener that is used by testCreateListener.
     */
    public static class TestCreateListenerListener implements AsyncListener {

        @Override
        public void onComplete(AsyncEvent event) throws IOException {
        }

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {
        }
    }

    /**
     * Listener that is used by testCreateListener2.
     */
    public static class TestCreateListener2Listener implements AsyncListener {

        /**
         * Constructor.
         *
         * @throws ServletException when constructor is called.
         */
        public TestCreateListener2Listener() throws ServletException {
            throw new ServletException();
        }

        @Override
        public void onComplete(AsyncEvent event) throws IOException {
        }

        @Override
        public void onTimeout(AsyncEvent event) throws IOException {
        }

        @Override
        public void onError(AsyncEvent event) throws IOException {
        }

        @Override
        public void onStartAsync(AsyncEvent event) throws IOException {
        }
    }
}
