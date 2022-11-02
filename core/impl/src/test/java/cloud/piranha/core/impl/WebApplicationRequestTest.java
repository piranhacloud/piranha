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
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * All JUnit tests related to WebApplicationRequest functionality.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class WebApplicationRequestTest {

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
    void testGetReader2() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        assertNotNull(request.getReader());
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.getInputStream()));
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
        assertEquals(101, response.getStatus());
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
}
