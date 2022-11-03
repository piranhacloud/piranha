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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequest;
import cloud.piranha.core.api.WebApplicationResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The JUnit tests for the DefaultWebApplicationRequest class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationRequestTest extends cloud.piranha.core.tests.WebApplicationRequestTest {

    @Override
    protected WebApplication createWebApplication() {
        return new DefaultWebApplication();
    }

    @Override
    protected WebApplicationRequest createWebApplicationRequest() {
        return new DefaultWebApplicationRequest();
    }

    @Override
    protected WebApplicationResponse createWebApplicationResponse() {
        return new DefaultWebApplicationResponse();
    }
    
    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId3() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        HttpSession session = request.getSession(true);
        String sessionId1 = session.getId();
        request.setRequestedSessionId(session.getId());
        String sessionId2 = request.changeSessionId();
        assertNotEquals(sessionId1, sessionId2);
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        request.setAsyncSupported(true);
        request.startAsync();
        assertNotNull(request.getAsyncContext());
    }

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId2() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        webApp.linkRequestAndResponse(request, response);
        request.setWebApplication(webApp);
        HttpSession session = request.getSession(true);
        String sessionId1 = session.getId();
        request.setRequestedSessionId(session.getId());
        String sessionId2 = request.changeSessionId();
        assertNotEquals(sessionId1, sessionId2);
    }

    /**
     * Test getContentType method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContentType2() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContentType("text/html");
        assertEquals("text/html", request.getContentType());
        assertEquals("text/html", request.getHeader("Content-Type"));
    }
    
    /**
     * Test getContentLength method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContentLength2() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContentLength(1234);
        assertEquals(1234, request.getContentLength());
        assertEquals("1234", request.getHeader("Content-Length"));
    }
}
