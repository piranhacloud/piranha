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

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.WebConnection;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * All JUnit tests related to WebApplicationRequest functionality.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class WebApplicationRequestTest {

    /**
     * Test authenticate method.
     */
    @Test
    void testAuthenticate() {
        try {
            DefaultWebApplication webApp = new DefaultWebApplication();
            DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
            request.setWebApplication(webApp);
            TestWebApplicationResponse response = new TestWebApplicationResponse();
            assertFalse(request.authenticate(response));
            request.close();
        } catch (IOException | ServletException exception) {
            fail();
        }
    }

    /**
     * Test authenticate method.
     */
    @Test
    void testAuthenticate2() {
        try {
            DefaultWebApplication webApp = new DefaultWebApplication();
            TestWebApplicationRequest request = new TestWebApplicationRequest();
            request.setWebApplication(webApp);
            TestWebApplicationResponse response = new TestWebApplicationResponse();
            request.authenticate(response);
            request.close();
        } catch (IOException | ServletException ex) {
            fail();
        }
    }

    /**
     * Test getAsyncContext method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetAsyncContext() throws IOException {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.getAsyncContext()));
        request.close();    
    }

    /**
     * Test getAsyncContext method.
     */
    @Test
    void testGetAsyncContext2() {
        DefaultWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        response.setWebApplication(webApplication);
        webApplication.linkRequestAndResponse(request, response);
        request.setAsyncSupported(true);
        request.startAsync();
        assertNotNull(request.getAsyncContext());
    }

    /**
     * Test getContentLengthLong method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContentLengthLong() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(-1L, request.getContentLengthLong());
        request.close();
    }

    /**
     * Test getDateHeader method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetDateHeader() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(-1L, request.getDateHeader("notfound"));
        request.close();
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(request.getInputStream());
        request.close();
    }

    /**
     * Test getInputStream method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetInputStream2() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(request.getInputStream());
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.getReader()));
        request.close();
    }

    /**
     * Test getIntHeader method.
     * 
     * @throws Exception when a serious error ocucrs.
     */
    @Test
    void testGetIntHeader() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(-1, request.getIntHeader("notfound"));
        request.close();
    }

    /**
     * Test getLocale method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetLocale() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setHeader("Accept-Language", "en");
        assertNotNull(request.getLocale());
        request.close();
    }

    /**
     * Test getLocale method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetLocales() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setHeader("Accept-Language", "en, de");
        assertNotNull(request.getLocales());
        request.close();
    }

    /**
     * Test getParameterMap method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetParameterMap() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setInputStream(new ByteArrayInputStream("param1=value1".getBytes(UTF_8)));
        request.setContentType("application/x-www-form-urlencoded");
        Map<String, String[]> parameterMap = request.getParameterMap();
        assertEquals(1, parameterMap.size());
        assertArrayEquals(new String[]{"value1"}, parameterMap.get("param1"));
        request.close();
    }

    /**
     * Test getParameterMap method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetParameterMap2() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setInputStream(new ByteArrayInputStream("param1=value1".getBytes(UTF_8)));
        request.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
        Map<String, String[]> parameterMap = request.getParameterMap();
        assertEquals(1, parameterMap.size());
        assertArrayEquals(new String[]{"value1"}, parameterMap.get("param1"));
        request.close();
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setContentType("text/html");
        assertNotNull(assertThrows(ServletException.class,
                () -> request.getPart("not_there")));
        request.close();
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setContentType("multipart/form-data");
        assertNull(request.getPart("not_there"));
        request.close();
    }

    /**
     * Test getReader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetReader() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(request.getReader());
        request.close();
    }

    /**
     * Test getReader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetReader2() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(request.getReader());
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.getInputStream()));
        request.close();
    }

    /**
     * Test getRealPath method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @SuppressWarnings({"deprecation"})
    void testGetRealPath() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertNotNull(assertThrows(UnsupportedOperationException.class,
                () -> request.getRealPath("/path")));
        request.close();
    }

    /**
     * Test getRequestDispatcher method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRequestDispatcher() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        DefaultWebApplicationRequestMapper webAppRequestMapper = new DefaultWebApplicationRequestMapper();
        webApp.setWebApplicationRequestMapper(webAppRequestMapper);
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApp);
        assertNotNull(request.getRequestDispatcher("/test"));
        request.close();
    }

    /**
     * Test getUpgradeHandler method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetUpgradeHandler() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getUpgradeHandler());
        request.close();
    }

    /**
     * Test isSecure method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsSecure() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setScheme("https");
        assertTrue(request.isSecure());
        request.close();
    }

    /**
     * Test isUpgraded method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsUpgraded() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertFalse(request.isUpgraded());
        request.close();
    }

    /**
     * Test isUserInRole method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsUserInRole() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        assertFalse(request.isUserInRole("notmatched"));
        request.close();
    }

    /**
     * Test login method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testLogin() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.login("username", "password");
        assertNull(request.getUserPrincipal());
        request.close();
    }

    /**
     * Test removeAttribute method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testRemoveAttribute() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setAttribute("name", "value");
        assertNotNull(request.getAttribute("name"));
        request.removeAttribute("name");
        assertNull(request.getAttribute("name"));
        request.close();
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getCharacterEncoding());
        request.setCharacterEncoding("UTF-8");
        assertEquals("UTF-8", request.getCharacterEncoding());
        request.close();
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding2() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getCharacterEncoding());
        request.getReader();
        request.setCharacterEncoding("UTF-8");
        assertNotEquals("UTF-8", request.getCharacterEncoding());
        request.close();
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding3() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(assertThrows(UnsupportedEncodingException.class,
                () -> request.setCharacterEncoding("doesnotexist")));
        request.close();
    }

    /**
     * Test setCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCharacterEncoding4() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(assertThrows(UnsupportedEncodingException.class,
                () -> request.setCharacterEncoding(null)));
        request.close();
    }

    /**
     * Test setCookies method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetCookies() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getCookies());
        request.setCookies(new Cookie[0]);
        assertNull(request.getCookies());
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("name", "value");
        request.setCookies(cookies);
        assertNotNull(request.getCookies());
        assertEquals("name", request.getCookies()[0].getName());
        assertEquals("value", request.getCookies()[0].getValue());
        request.close();
    }

    /**
     * Test setLocalAddr method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetLocalAddr() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getLocalAddr());
        request.setLocalAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getLocalAddr());
        request.close();
    }

    /**
     * Test setLocalName method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetLocalName() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getLocalName());
        request.setLocalName("localhost");
        assertEquals("localhost", request.getLocalName());
        request.close();
    }

    /**
     * Test setLocalPort method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetLocalPort() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(0, request.getLocalPort());
        request.setLocalPort(12345);
        assertEquals(12345, request.getLocalPort());
        request.close();
    }

    /**
     * Test setProtocol method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetProtocol() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals("HTTP/1.1", request.getProtocol());
        request.setProtocol("HTTP/1.0");
        assertEquals("HTTP/1.0", request.getProtocol());
        request.close();
    }

    /**
     * Test setRemoteAddr method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetRemoteAddr() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getRemoteAddr());
        request.setRemoteAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getRemoteAddr());
        request.close();
    }

    /**
     * Test setRemoteHost method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetRemoteHost() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertNull(request.getRemoteHost());
        request.setRemoteHost("localhost");
        assertEquals("localhost", request.getRemoteHost());
        request.close();
    }

    /**
     * Test setRemotePort method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetRemotePort() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(0, request.getRemotePort());
        request.setRemotePort(12345);
        assertEquals(12345, request.getRemotePort());
        request.close();
    }

    /**
     * Test setServerName method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetServerName() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals("localhost", request.getServerName());
        request.setServerName("my.host.com");
        assertEquals("my.host.com", request.getServerName());
        request.close();
    }

    /**
     * Test setServerPort method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetServerPort() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        assertEquals(80, request.getServerPort());
        request.setServerPort(8080);
        assertEquals(8080, request.getServerPort());
        request.close();
    }

    /**
     * Test startAsync method.
     */
    @Test
    void testStartAsync() {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setAttribute("piranha.response", response);
        request.setAsyncSupported(false);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.startAsync(request, response)));
    }

    /**
     * Test startAsync method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testStartAsync2() throws Exception {
        DefaultWebApplicationRequest request = new TestWebApplicationRequest();
        request.setAttribute("piranha.response", new TestWebApplicationResponse());
        request.setAsyncSupported(false);
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.startAsync()));
        request.close();
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testUpgrade() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(request.upgrade(TestUpgradeHttpUpgradeHandler.class));
        request.close();
    }

    /**
     * Test upgrade method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testUpgrade2() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(assertThrows(ServletException.class,
                () -> request.upgrade(TestUpgrade2HttpUpgradeHandler.class)));
        request.close();
    }

    /**
     * Test upgrade method.
     */
    @Test
    void testUpgrade3() throws Exception {
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Upgrade", TestUpgrade3HttpServlet.class);
        webApplication.addServletMapping("Upgrade", "/*");
        webApplication.initialize();
        webApplication.start();
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        request.setHeader("Upgrade", "YES");
        request.setMethod("POST");
        webApplication.service(request, response);
    }

    /**
     * A HttpUpgradeHandler used by testUpgrade2 method.
     */
    public static class TestUpgrade2HttpUpgradeHandler implements HttpUpgradeHandler {

        /**
         * Constructor.
         *
         * @throws IllegalAccessException on purpose.
         */
        public TestUpgrade2HttpUpgradeHandler() throws IllegalAccessException {
            throw new IllegalAccessException();
        }

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }

    /**
     * A HttpServlet used by the testUpgrade3 method.
     */
    public static class TestUpgrade3HttpServlet extends HttpServlet {

        @Override
        public void doPost(
                HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            if (request.getHeader("Upgrade") != null) {
                response.setStatus(101);
                response.setHeader("Upgrade", "YES");
                response.setHeader("Connection", "Upgrade");
                request.upgrade(TestUpgrade3HttpUpgradeHandler.class);
            } else {
                response.getWriter().println("Not upgraded");
            }
        }
    }

    /**
     * A HttpUpgradeHandler used by testUpgrade3 method.
     */
    public static class TestUpgrade3HttpUpgradeHandler implements HttpUpgradeHandler {

        /**
         * Constructor.
         */
        public TestUpgrade3HttpUpgradeHandler() {
        }

        @Override
        public void init(WebConnection conection) {
            try {
                ServletInputStream input = conection.getInputStream();
                ServletOutputStream output = conection.getOutputStream();
                TestUpgrade3ReadListener readListener = new TestUpgrade3ReadListener(input, output);
                input.setReadListener(readListener);
                output.flush();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void destroy() {
        }
    }

    /**
     * A ReadListener used by the testUpgrade3 method.
     */
    public static class TestUpgrade3ReadListener implements ReadListener {

        /**
         * Stores the input stream.
         */
        private ServletInputStream inputStream = null;

        /**
         * Stores the output stream.
         */
        private ServletOutputStream outputStream = null;

        /**
         * Stores the DELIMITER constant.
         */
        private static final String DELIMITER = "/";

        /**
         * Constructor.
         *
         * @param inputStream the input steam.
         * @param outputStream the output stream.
         */
        public TestUpgrade3ReadListener(ServletInputStream inputStream, ServletOutputStream outputStream) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
        }

        @Override
        public void onDataAvailable() {
            try {
                StringBuilder sb = new StringBuilder();
                int len = -1;
                byte b[] = new byte[1024];
                while (inputStream.isReady() && (len = inputStream.read(b)) != -1) {
                    String data = new String(b, 0, len);
                    sb.append(data);
                }
                outputStream.println(DELIMITER + sb.toString());
                outputStream.flush();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void onAllDataRead() {
            try {
                outputStream.close();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void onError(final Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * A HttpUpgradeHandler used by testUpgrade method.
     */
    public static class TestUpgradeHttpUpgradeHandler implements HttpUpgradeHandler {

        /**
         * Constructor.
         */
        public TestUpgradeHttpUpgradeHandler() {
        }

        @Override
        public void init(WebConnection wc) {
        }

        @Override
        public void destroy() {
        }
    }
}
