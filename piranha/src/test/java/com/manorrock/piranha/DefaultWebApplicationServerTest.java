/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

//import com.manorrock.httpclient.HttpClientRequestBuilderFactory;
//import com.manorrock.httpclient.HttpClientResponse;
import com.manorrock.httpserver.DefaultHttpServerBuilder;
import com.manorrock.httpserver.HttpServer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Ignore;
import org.junit.Test;

/**
 * The JUnit tests for the DefaultWebApplicationServer class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationServerTest {

    /**
     * Test addMapping method.
     */
    @Test
    public void testAddMapping() {
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setServletContextName("mycontext");
        server.addWebApplication(webApp);
        server.addMapping("notthere", "notreal");
    }

    /**
     * Test addMapping method.
     */
    @Test
    public void testAddMapping2() {
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setServletContextName("mycontext");
        server.addWebApplication(webApp);
        server.addMapping("mycontext", "mycontextmapping");
    }

    /**
     * Test addMapping method.
     */
    @Test
    public void testAddMapping3() {
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setServletContextName("mycontext");
        server.addWebApplication(webApp);
        server.addMapping("mycontext", "myurlpattern");
        server.addMapping("mycontext", "myurlpattern");
    }

    /**
     * Test getRequestMapper method.
     */
    @Test
    public void testGetRequestMapper() {
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        server.setRequestMapper(new DefaultWebApplicationServerRequestMapper());
        assertNotNull(server.getRequestMapper());
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testService() throws Exception {
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        DefaultHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        server.service(request, response);
        assertEquals(404, response.getStatus());
    }

    /**
     * Test service method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testService2() throws Exception {
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        DefaultWebApplicationRequestMapper webApplicationRequestMapper = new DefaultWebApplicationRequestMapper();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(webApplicationRequestMapper);
        webApp.addServlet("snoop", new TestSnoopServlet());
        webApp.addServletMapping("snoop", "/snoop/*");
        webApp.setContextPath("/context");
        server.addWebApplication(webApp);
        DefaultHttpServletRequest request = new TestHttpServletRequest();
        request.setContextPath("/context");
        request.setPathInfo("/snoop/index.html");
        TestHttpServletResponse response = new TestHttpServletResponse();
        TestServletOutputStream outputStream = new TestServletOutputStream();
        response.setOutputStream(outputStream);
        outputStream.setResponse(response);
        server.initialize();
        server.start();
        server.service(request, response);
        server.stop();
        assertEquals(200, response.getStatus());
    }

    /**
     * Test process method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @Ignore
    public void testProcess() throws Exception {
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        HttpServer httpServer = new DefaultHttpServerBuilder().port(7000).processor(server).build();
        WebApplicationBuilder webAppBuilder = WebApplicationBuilderFactory.produce();
        WebApplication webApp = webAppBuilder.contextPath("/context").build();
        webApp.addServlet("snoop", new TestSnoopServlet());
        webApp.addServletMapping("snoop", "/snoop/*");
        server.addWebApplication(webApp);
        server.initialize();
        server.start();
        httpServer.start();
//        HttpClientResponse response = HttpClientRequestBuilderFactory.produce().build().
//                url("http://localhost:7000/context/snoop/index.html").get();
        httpServer.stop();
        server.stop();
//        assertEquals(200, response.getStatus());
    }
}
