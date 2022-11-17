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
package cloud.piranha.http.webapp;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.impl.DefaultHttpServer;
import jakarta.servlet.ServletRegistration.Dynamic;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for HttpWebApplicationRequest.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HttpWebApplicationRequestTest {

    /**
     * Stores the HTTP server.
     */
    private HttpServer httpServer;
    
    /**
     * Stores the HTTP web application server.
     */
    private HttpWebApplicationServer server;
    
    /**
     * After each.
     */
    @AfterEach
    void afterEach() {
        server.stop();
        httpServer.stop();
    }
    
    /**
     * Before each.
     */
    @BeforeEach
    void beforeEach() {
        server = new HttpWebApplicationServer();
        DefaultWebApplication application = new DefaultWebApplication();
        application.setContextPath("");
        Dynamic registration = application.addServlet("snoop", new TestSnoopServlet());
        registration.setAsyncSupported(true);
        application.addServletMapping("snoop", "/Snoop", "/Snoop/*");
        server.addWebApplication(application);
        server.initialize();
        server.start();
        httpServer = new DefaultHttpServer(-2, server, false);
        httpServer.start();
    }

    /**
     * Test getGetAuthType method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetAuthType() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getAuthType"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("null", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetCharacterEncoding() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getCharacterEncoding"))
                    .header("Content-Type", "text/html; charset=ISO-8859-1")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("ISO-8859-1", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetCharacterEncoding2() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getCharacterEncoding"))
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("UTF-8", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getContentLength method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContentLength() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getContentLength"))
                    .POST(HttpRequest.BodyPublishers.ofString(""))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(Integer.parseInt(response.body().trim()) >= 0);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getContentType method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContentType() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getContentType"))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("application/json", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getContextPath method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetContextPath() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getContextPath"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getCookies method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetCookies() throws Exception {
        try {
            CookieManager cookieManager = new CookieManager();
            HttpCookie cookie = new HttpCookie("name", "value");
            cookie.setPath("/");
            cookie.setVersion(0);
            cookieManager.getCookieStore().add(
                    URI.create("http://localhost:" + httpServer.getServerPort() + "/Snoop"), 
                    cookie);
            HttpClient client = HttpClient
                    .newBuilder()
                    .cookieHandler(cookieManager)
                    .build();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getCookies"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("value", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getDateHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetDateHeader() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getDateHeader"))
                    .header("MY_DATE", "Wed, 16 Oct 2019 07:28:00 GMT")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("1571210880000"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetHeader() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getHeader"))
                    .header("MY_HEADER", "1234")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("1234", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getHeaderNames method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetHeaderNames() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getHeaderNames"))
                    .header("MY_HEADER", "1234")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("MY_HEADER"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getIntHeader method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetIntHeader() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getIntHeader"))
                    .header("MY_INT", "1234")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("1234"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getLocalAddr method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetLocalAddr() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getLocalAddr"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("127.0.0.1"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getLocalName method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetLocalName() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getLocalName"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertNotEquals("", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getLocalPort method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetLocalPort() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getLocalPort"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertNotEquals(-1, Integer.parseInt(response.body().trim()));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getLocale method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetLocale() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getLocale"))
                    .header("Accept-Language", "nl_nl")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("nl_nl", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
        
    /**
     * Test getLocales method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetLocales() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getLocales"))
                    .header("Accept-Language", "nl_nl, en_us")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("nl_nl,en_us,", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getMethod method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetMethod() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getMethod"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("GET"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getParameter method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetParameter() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getParameter&parameter=value"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("value", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getParts method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetParts() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getParts"))
                    .header("Content-Type", "multipart/form-data")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("true", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getPathInfo method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPathInfo() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop/pathInfo?getPathInfo"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("/pathInfo", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getPathTranslated method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetPathTranslated() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop/pathTranslated?getPathTranslated"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("null", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getProtocol method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetProtocol() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getProtocol"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("HTTP/1.1", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getQueryString method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetQueryString() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getQueryString"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("getQueryString"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getRemoteAddr method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRemoteAddr() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getRemoteAddr"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("127.0.0.1"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getRemoteHost method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRemoteHost() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getRemoteHost"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertNotEquals("", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getRemotePort method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRemotePort() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getRemotePort"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(Integer.parseInt(response.body().trim()) > 0);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getRemoteUser method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRemoteUser() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getRemoteUser"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("null", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getRequestURI method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRequestURI() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getRequestURI"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("/Snoop", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getRequestURL method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRequestURL() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getRequestURL"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("http://"));
            assertTrue(response.body().contains(Integer.toString(httpServer.getServerPort())));
            assertTrue(response.body().contains("/Snoop"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getRequestedSessionId method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetRequestedSessionId() throws Exception {
        try {
            CookieManager cookieManager = new CookieManager();
            HttpCookie cookie = new HttpCookie("JSESSIONID", "requestedSessionId");
            cookie.setPath("/");
            cookie.setVersion(0);
            cookieManager.getCookieStore().add(
                    URI.create("http://localhost:" + httpServer.getServerPort() + "/Snoop"), 
                    cookie);
            HttpClient client = HttpClient
                    .newBuilder()
                    .cookieHandler(cookieManager)
                    .build();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getRequestedSessionId"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("requestedSessionId", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getScheme method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetScheme() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getScheme"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("http", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Test getServerName method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetServerName() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getServerName"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertNotEquals("", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getServerPort method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetServerPort() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getServerPort"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains(Integer.toString(httpServer.getServerPort())));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getServletPath method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetServletPath() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getServletPath"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("/Snoop", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test getSession method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetSession() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?getSession"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(Long.parseLong(response.body().trim()) > 0);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test isAsyncStarted method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsAsyncStarted() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?isAsyncStarted"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("false", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test isAsyncSupported method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsAsyncSupported() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?isAsyncSupported"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("true", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test isRequestedSessionIdFromCookie method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsRequestedSessionIdFromCookie() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?isRequestedSessionIdFromCookie"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("false", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test isRequestedSessionIdFromURL method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsRequestedSessionIdFromURL() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?isRequestedSessionIdFromURL"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("false", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test isRequestedSessionIdValid method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsRequestedSessionIdValid() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?isRequestedSessionIdValid"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals("false", response.body().trim());
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    /**
     * Test isSecure method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIsSecure() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest
                    .newBuilder(new URI("http://localhost:" + httpServer.getServerPort() + "/Snoop?isSecure"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("false"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
