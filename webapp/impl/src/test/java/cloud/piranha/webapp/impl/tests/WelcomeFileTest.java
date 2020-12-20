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
package cloud.piranha.webapp.impl.tests;

import cloud.piranha.resource.DirectoryResource;
import cloud.piranha.webapp.impl.DefaultServlet;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplicationRequest;
import cloud.piranha.webapp.impl.DefaultWebApplicationResponse;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests to test the welcome-file functionality.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class WelcomeFileTest {
    
    /**
     * Test an index.html file.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testIndexHtml() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DirectoryResource(new File("src/test/webapp/default")));
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApp);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApp);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.setUnderlyingOutputStream(byteOutput);
        webApp.setDefaultServlet(new DefaultServlet());
        webApp.initialize();
        webApp.start();
        webApp.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(byteOutput.toString().contains("index.html"));
        webApp.stop();
    }
    
    
    /**
     * Test a custom welcome file.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testCustomWelcomeFilel() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getWelcomeFileManager().addWelcomeFile("custom.html");
        webApp.addResource(new DirectoryResource(new File("src/test/webapp/custom")));
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setWebApplication(webApp);
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApp);
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        response.setUnderlyingOutputStream(byteOutput);
        webApp.setDefaultServlet(new DefaultServlet());
        webApp.initialize();
        webApp.start();
        webApp.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(byteOutput.toString().contains("custom.html"));
        webApp.stop();
    }
}
