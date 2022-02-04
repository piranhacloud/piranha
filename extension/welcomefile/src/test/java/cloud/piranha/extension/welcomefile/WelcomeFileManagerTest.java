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
package cloud.piranha.extension.welcomefile;

import cloud.piranha.extension.welcomefile.WelcomeFileManager;
import cloud.piranha.core.impl.DefaultServlet;
import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import cloud.piranha.core.impl.DefaultWebApplicationResponse;
import cloud.piranha.resource.impl.DirectoryResource;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for the StandardWelcomeFileManager class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class WelcomeFileManagerTest {
    
    /**
     * Test an index.html file.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testWelcomeFile1() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DirectoryResource(new File("src/test/webapp/welcomefile1")));
        webApp.getManager().setWelcomeFileManager(new WelcomeFileManager());
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
    void testWelcomeFile2() throws Exception {
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.getManager().setWelcomeFileManager(new WelcomeFileManager());
        webApp.getManager().getWelcomeFileManager().addWelcomeFile("custom.html");
        webApp.addResource(new DirectoryResource(new File("src/test/webapp/welcomefile2")));
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
