/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the ReadListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class ReadListenerTest {

    /**
     * Create a web application.
     *
     * @return the web application.
     */
    public abstract WebApplication createWebApplication();

    /**
     * Create a web application request.
     *
     * @return the web application request.
     */
    public abstract WebApplicationRequest createWebApplicationRequest();

    /**
     * Create a web application response.
     *
     * @return the web application response.
     */
    public abstract WebApplicationResponse createWebApplicationResponse();

    /**
     * Test onAllDataRead method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnAllDataRead() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setAsyncSupported(true);
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        request.startAsync();
        try (ServletInputStream inputStream = request.getInputStream()) {
            request.getWebApplicationInputStream().setInputStream(new ByteArrayInputStream("read this".getBytes()));
            inputStream.setReadListener(new TestOnAllDataReadReadListener(webApplication));
            int character = inputStream.read();
            while(character != -1) {
                character = inputStream.read();
            }
        }
        Thread.sleep(2000);
        assertNotNull(webApplication.getAttribute("onAllDataRead"));
    }

    /**
     * Test onDataAvailable method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testOnDataAvailable() throws Exception {
        WebApplication webApplication = createWebApplication();
        WebApplicationRequest request = createWebApplicationRequest();
        request.setAsyncSupported(true);
        request.setWebApplication(webApplication);
        WebApplicationResponse response = createWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        request.startAsync();
        ServletInputStream inputStream = request.getInputStream();
        request.getWebApplicationInputStream().setInputStream(new ByteArrayInputStream("a".getBytes()));
        inputStream.setReadListener(new TestOnDataAvailableReadListener(webApplication));
        Thread.sleep(5000);
        assertNotNull(webApplication.getAttribute("onDataAvailable"));
    }

    public static class TestOnAllDataReadReadListener implements ReadListener {

        /**
         * Stores the web application.
         */
        private final WebApplication webApplication;
        
        /**
         * Constructor.
         * 
         * @param webApplication the web application.
         */
        public TestOnAllDataReadReadListener(WebApplication webApplication) {
            this.webApplication = webApplication;
        }
        
        @Override
        public void onAllDataRead() throws IOException {
            webApplication.setAttribute("onAllDataRead", true);
        }

        @Override
        public void onDataAvailable() throws IOException {
        }

        @Override
        public void onError(Throwable t) {
        }
    }

    public static class TestOnDataAvailableReadListener implements ReadListener {

        /**
         * Stores the web application.
         */
        private final WebApplication webApplication;
        
        /**
         * Constructor.
         * 
         * @param webApplication the web application.
         */
        public TestOnDataAvailableReadListener(WebApplication webApplication) {
            this.webApplication = webApplication;
        }
        
        @Override
        public void onAllDataRead() throws IOException {
        }

        @Override
        public void onDataAvailable() throws IOException {
            webApplication.setAttribute("onDataAvailable", true);
        }

        @Override
        public void onError(Throwable t) {
        }
    }
}
