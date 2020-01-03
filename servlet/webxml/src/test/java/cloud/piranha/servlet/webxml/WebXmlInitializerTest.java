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
package cloud.piranha.servlet.webxml;

import cloud.piranha.DefaultWebXml;
import cloud.piranha.servlet.webxml.WebXmlInitializer;
import cloud.piranha.DefaultDirectoryResource;
import cloud.piranha.DefaultWebApplication;
import java.io.File;
import java.io.InputStream;
import javax.servlet.ServletRegistration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the WebXmlInitializer class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlInitializerTest {

    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testOnStartup() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DefaultDirectoryResource(new File("src/test/webxml/test1")));
        webApplication.addInitializer(new WebXmlInitializer());
        webApplication.initialize();
        ServletRegistration registration = webApplication.getServletRegistration("Test Servlet");
        assertNotNull(registration);
        assertFalse(registration.getMappings().isEmpty());
        assertEquals("*.html", registration.getMappings().iterator().next());
        assertEquals("application/x-java-class", webApplication.getMimeType("my.class"));
        assertEquals("myvalue", webApplication.getInitParameter("myname"));
        assertEquals("myservletcontext", webApplication.getServletContextName());
    }
    
    /**
     * Test onStartup method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testOnStartup2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DefaultDirectoryResource(new File("src/test/webxml/test2")));
        webApplication.addInitializer(new WebXmlInitializer());
        webApplication.initialize();
    }
    
    /**
     * Test parseWebXml method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testParseWebXml() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DefaultDirectoryResource(new File("src/test/webxml/test3")));
        WebXmlInitializer initializer = new WebXmlInitializer();
        InputStream inputStream = webApplication.getResourceAsStream("WEB-INF/web.xml");
        DefaultWebXml webXml = initializer.parseWebXml(inputStream);
        assertFalse(webXml.servlets.isEmpty());
        assertEquals(2, webXml.servlets.size());
        assertNotEquals(webXml.servlets.get(0).name, webXml.servlets.get(1).name);
        assertTrue(webXml.servlets.get(0).asyncSupported);
    }
}
