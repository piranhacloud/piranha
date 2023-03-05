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
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The JUnit tests for the ServletOutputStream API.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class ServletOutputStreamTest {
    
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
     * Test isReady method.
     * 
     * @throws Exception when a serious error occurs.
     */
    void testIsReady() throws Exception {
        WebApplicationResponse response = createWebApplicationResponse();
        ServletOutputStream outputStream = response.getOutputStream();
        assertFalse(outputStream.isReady());
    }
    
    /**
     * Test setWriteListener method.
     */
    void testWriteListener() throws Exception {
        WebApplicationResponse response = createWebApplicationResponse();
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.setWriteListener(new WriteListener() {
            @Override
            public void onError(Throwable t) {
            }

            @Override
            public void onWritePossible() throws IOException {
            }
        });
        assertNotNull(response.getWebApplicationOutputStream().getWriteListener());
    }
}
