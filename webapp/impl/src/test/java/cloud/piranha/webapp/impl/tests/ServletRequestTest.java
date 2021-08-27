/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.webapp.impl.tests;

import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplicationRequestMapper;
import cloud.piranha.webapp.impl.DefaultWebApplicationResponse;
import java.io.UnsupportedEncodingException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for testing everything related to the ServletRequest API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ServletRequestTest {

    /**
     * Stores the servlet request.
     */
    protected TestWebApplicationRequest request;

    /**
     * Setup before testing.
     *
     * @throws Exception when a serious error occurs.
     */
    @BeforeEach
    void setUp() throws Exception {
        request = new TestWebApplicationRequest();
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext() {
        assertThrows(IllegalStateException.class, () -> request.getAsyncContext());
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext2() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        request.setWebApplication(webApplication);
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        request.setAsyncSupported(true);
        request.startAsync();
        assertNotNull(request.getAsyncContext());
    }

    /**
     * Test getContentLengthLong method.
     */
    @Test
    void testGetContentLengthLong() {
        assertEquals(-1L, request.getContentLengthLong());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream() throws Exception {
        assertNotNull(request.getInputStream());
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream2() throws Exception {
        assertNotNull(request.getInputStream());
        assertThrows(IllegalStateException.class, () -> request.getReader());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocale() {
        request.setHeader("Accept-Language", "en");
        assertNotNull(request.getLocale());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocales() {
        request.setHeader("Accept-Language", "en, de");
        assertNotNull(request.getLocales());
    }

    /**
     * Test getReader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetReader() throws Exception {
        assertNotNull(request.getReader());
    }

    /**
     * Test getReader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetReader2() throws Exception {
        assertNotNull(request.getReader());
        assertThrows(IllegalStateException.class, () -> request.getInputStream());
    }

    /**
     * Test getRealPath method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRealPath() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        request.setWebApplication(webApplication);
        assertThrows(UnsupportedOperationException.class, () -> request.getRealPath("/path"));
    }

    /**
     * Test getRequestDispatcher method.
     */
    @Test
    void testGetRequestDispatcher() {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        request.setWebApplication(webApp);
        assertNotNull(request.getRequestDispatcher("/test"));
    }

    /**
     * Test isSecure method.
     */
    @Test
    void testIsSecure() {
        request.setScheme("https");
        assertTrue(request.isSecure());
    }

    /**
     * Test removeAttribute method.
     */
    @Test
    void testRemoveAttribute() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        request.setWebApplication(webApplication);
        request.setAttribute("name", "value");
        assertNotNull(request.getAttribute("name"));
        request.removeAttribute("name");
        assertNull(request.getAttribute("name"));
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding() throws Exception {
        assertNull(request.getCharacterEncoding());
        request.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", request.getCharacterEncoding());
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding2() throws Exception {
        assertNull(request.getCharacterEncoding());
        request.getReader();
        request.setCharacterEncoding("UTF-8");
        assertNotEquals("UTF-8", request.getCharacterEncoding());
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding3() {
        assertThrows(UnsupportedEncodingException.class, () -> request.setCharacterEncoding("doesnotexist"));
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding4() {
        assertThrows(UnsupportedEncodingException.class, () -> request.setCharacterEncoding(null));
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        request.setAttribute("piranha.response", response);
        request.setAsyncSupported(false);
        assertThrows(IllegalStateException.class, () -> request.startAsync(request, response));
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync2() {
        assertThrows(IllegalStateException.class, () -> {
            try {
                request.setAttribute("piranha.response", new TestWebApplicationResponse());
                request.setAsyncSupported(false);
                request.startAsync();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        });
    }
}
