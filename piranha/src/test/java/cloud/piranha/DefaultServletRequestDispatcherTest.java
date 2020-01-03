/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * The JUnit tests for DefaultServletRequestDispatcher.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultServletRequestDispatcherTest {

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testForward() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Snoop", TestSnoopServlet.class);
        webApp.initialize();
        webApp.start();
        RequestDispatcher dispatcher = webApp.getNamedDispatcher("Snoop");
        dispatcher.forward(request, response);
        String responseText = new String(response.getResponseBytes());
        webApp.stop();
        assertTrue(responseText.contains("<title>Snoop</title>"));
    }

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testForward2() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(new DefaultWebApplicationRequestMapper());
        webApp.addServlet("Snoop", TestSnoopServlet.class);
        webApp.addServletMapping("Snoop", "/Snoop");
        webApp.initialize();
        webApp.start();
        RequestDispatcher dispatcher = webApp.getRequestDispatcher("/Snoop");
        dispatcher.forward(request, response);
        String responseText = new String(response.getResponseBytes());
        webApp.stop();
        assertTrue(responseText.contains("<title>Snoop</title>"));
    }

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test(expected = IOException.class)
    public void testForward3() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(new DefaultWebApplicationRequestMapper());
        webApp.addServlet("Error", TestIOExceptionServlet.class);
        webApp.addServletMapping("Error", "/Error");
        webApp.initialize();
        webApp.start();
        RequestDispatcher dispatcher = webApp.getRequestDispatcher("/Error");
        dispatcher.forward(request, response);
        fail();
    }

    /**
     * Test forward method.
     *
     * @throws Exception when an error occurs.
     */
    @Test(expected = RuntimeException.class)
    public void testForward4() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setWebApplicationRequestMapper(new DefaultWebApplicationRequestMapper());
        webApp.addServlet("Runtime", TestRuntimeExceptionServlet.class);
        webApp.addServletMapping("Runtime", "/Runtime");
        webApp.initialize();
        webApp.start();
        RequestDispatcher dispatcher = webApp.getRequestDispatcher("/Runtime");
        dispatcher.forward(request, response);
        fail();
    }

    /**
     * Test include method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testInclude() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Echo", TestEcho1Servlet.class);
        webApp.initialize();
        webApp.start();
        webApp.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApp.getNamedDispatcher("Echo");
        dispatcher.include(request, response);
        response.flushBuffer();
        String responseText = new String(response.getResponseBytes());
        webApp.unlinkRequestAndResponse(request, response);
        webApp.stop();
        assertTrue(responseText.contains("ECHO"));
    }

    /**
     * Test include method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testInclude2() throws Exception {
        TestWebApplicationRequest request = new TestWebApplicationRequest();
        TestWebApplicationResponse response = new TestWebApplicationResponse();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addServlet("Echo2", TestEcho2Servlet.class);
        webApp.initialize();
        webApp.start();
        webApp.linkRequestAndResponse(request, response);
        RequestDispatcher dispatcher = webApp.getNamedDispatcher("Echo2");
        dispatcher.include(request, response);
        response.flushBuffer();
        String responseText = new String(response.getResponseBytes());
        webApp.unlinkRequestAndResponse(request, response);
        webApp.stop();
        assertTrue(responseText.contains("ECHO"));
    }
}
