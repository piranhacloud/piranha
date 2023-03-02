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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.WebApplicationInputStream;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.WebConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for the DefaultWebApplicationRequest class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationRequestTest {

    /**
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId() {
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
     * Test changeSessionId method.
     */
    @Test
    void testChangeSessionId2() {
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
     * Test getContentLength method.
     */
    @Test
    void testGetContentLength() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContentLength(1234);
        assertEquals(1234, request.getContentLength());
        assertEquals("1234", request.getHeader("Content-Length"));
    }

    /**
     * Test getContentType method.
     */
    @Test
    void testGetContentType() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContentType("text/html");
        assertEquals("text/html", request.getContentType());
        assertEquals("text/html", request.getHeader("Content-Type"));
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocale() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setHeader("Accept-Language", "en");
        assertNotNull(request.getLocale());
    }

    /**
     * Test getLocale method.
     */
    @Test
    void testGetLocales() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setHeader("Accept-Language", "en, de");
        assertNotNull(request.getLocales());
    }

    /**
     * Test getParameterMap method.
     */
    @Test
    void testGetParameterMap() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        WebApplicationInputStream requestInput = request.getWebApplicationInputStream();
        requestInput.setInputStream(new ByteArrayInputStream("param1=value1".getBytes(UTF_8)));
        request.setContentType("application/x-www-form-urlencoded");
        Map<String, String[]> parameterMap = request.getParameterMap();
        assertEquals(1, parameterMap.size());
        assertArrayEquals(new String[]{"value1"}, parameterMap.get("param1"));
    }

    /**
     * Test getParameterMap method.
     */
    @Test
    void testGetParameterMap2() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        WebApplicationInputStream requestInput = request.getWebApplicationInputStream();
        requestInput.setInputStream(new ByteArrayInputStream("param1=value1".getBytes(UTF_8)));
        request.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
        Map<String, String[]> parameterMap = request.getParameterMap();
        assertEquals(1, parameterMap.size());
        assertArrayEquals(new String[]{"value1"}, parameterMap.get("param1"));
    }

    /**
     * Test getPart method.
     */
    @Test
    void testGetPart() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setContentType("text/html");
        assertNotNull(assertThrows(ServletException.class,
                () -> request.getPart("not_there")));
    }

    /**
     * Test getPart method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPart2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setContentType("multipart/form-data");
        assertNull(request.getPart("not_there"));
    }

    /**
     * Test getReader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetReader2() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNotNull(request.getReader());
        assertNotNull(assertThrows(IllegalStateException.class,
                () -> request.getInputStream()));
    }

    /**
     * Test isSecure method.
     */
    @Test
    void testIsSecure() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setScheme("https");
        assertTrue(request.isSecure());
    }

    /**
     * Test isUpgraded method.
     */
    @Test
    void testIsUpgraded() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertFalse(request.isUpgraded());
    }

    /**
     * Test setCookies method.
     */
    @Test
    void testSetCookies() {
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
    }

    /**
     * Test setLocalAddr method.
     */
    @Test
    void testSetLocalAddr() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getLocalAddr());
        request.setLocalAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getLocalAddr());
    }

    /**
     * Test setLocalName method.
     */
    @Test
    void testSetLocalName() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getLocalName());
        request.setLocalName("localhost");
        assertEquals("localhost", request.getLocalName());
    }

    /**
     * Test setLocalPort method.
     */
    @Test
    void testSetLocalPort() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(0, request.getLocalPort());
        request.setLocalPort(12345);
        assertEquals(12345, request.getLocalPort());
    }

    /**
     * Test setProtocol method.
     */
    @Test
    void testSetProtocol() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("HTTP/1.1", request.getProtocol());
        request.setProtocol("HTTP/1.0");
        assertEquals("HTTP/1.0", request.getProtocol());
    }

    /**
     * Test setRemoteAddr method.
     */
    @Test
    void testSetRemoteAddr() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getRemoteAddr());
        request.setRemoteAddr("127.0.0.1");
        assertEquals("127.0.0.1", request.getRemoteAddr());
    }

    /**
     * Test setRemoteHost method.
     */
    @Test
    void testSetRemoteHost() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertNull(request.getRemoteHost());
        request.setRemoteHost("localhost");
        assertEquals("localhost", request.getRemoteHost());
    }

    /**
     * Test setRemotePort method.
     */
    @Test
    void testSetRemotePort() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(0, request.getRemotePort());
        request.setRemotePort(12345);
        assertEquals(12345, request.getRemotePort());
    }

    /**
     * Test setServerName method.
     */
    @Test
    void testSetServerName() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals("localhost", request.getServerName());
        request.setServerName("my.host.com");
        assertEquals("my.host.com", request.getServerName());
    }

    /**
     * Test setServerPort method.
     */
    @Test
    void testSetServerPort() {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        assertEquals(80, request.getServerPort());
        request.setServerPort(8080);
        assertEquals(8080, request.getServerPort());
    }

    /**
     * Test upgrade method.
     */
    @Test
    void testUpgrade() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addServlet("Upgrade", new HttpServlet() {
            @Override
            public void doPost(
                    HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {

                if (request.getHeader("Upgrade") != null) {
                    response.setStatus(101);
                    response.setHeader("Upgrade", "YES");
                    response.setHeader("Connection", "Upgrade");
                    request.upgrade(TestUpgradeHttpUpgradeHandler.class);
                } else {
                    response.getWriter().println("Not upgraded");
                }
            }
        });
        webApplication.addServletMapping("Upgrade", "/*");
        webApplication.initialize();
        webApplication.start();
        
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApplication);
        request.setHeader("Upgrade", "YES");
        request.setMethod("POST");
        
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.getWebApplicationOutputStream().setOutputStream(byteOutput);
        
        webApplication.service(request, response);
        assertEquals(101, response.getStatus());
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
        public void destroy() {
        }

        @Override
        public void init(WebConnection conection) {
            try {
                ServletInputStream input = conection.getInputStream();
                ServletOutputStream output = conection.getOutputStream();
                TestUpgradeReadListener readListener = new TestUpgradeReadListener(input, output);
                input.setReadListener(readListener);
                output.flush();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    /**
     * A ReadListener used by the testUpgrade method.
     */
    public static class TestUpgradeReadListener implements ReadListener {

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
        public TestUpgradeReadListener(ServletInputStream inputStream, ServletOutputStream outputStream) {
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
