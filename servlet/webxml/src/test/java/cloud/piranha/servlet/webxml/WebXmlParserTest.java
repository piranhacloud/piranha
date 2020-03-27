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

import cloud.piranha.resource.DirectoryResource;
import cloud.piranha.DefaultWebApplication;
import java.io.File;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The JUnit tests for the WebXmlParser class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebXmlParserTest {
    
    /**
     * Test parse method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testParseWebXml() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource(new File("src/test/webxml/test3")));
        InputStream inputStream = webApplication.getResourceAsStream("WEB-INF/web.xml");
        WebXmlParser parser = new WebXmlParser();
        WebXml webXml = parser.parse(inputStream);
        assertFalse(webXml.getServlets().isEmpty());
        assertEquals(2, webXml.getServlets().size());
        assertNotEquals(webXml.getServlets().get(0).getServletName(), webXml.getServlets().get(1).getServletName());
        assertTrue(webXml.getServlets().get(0).isAsyncSupported());
        assertFalse(webXml.getFilters().isEmpty());
        assertEquals(1, webXml.getFilters().size());
        assertEquals("/defaultContextPath", webXml.getDefaultContextPath());
        assertTrue(webXml.getDenyUncoveredHttpMethods());
        assertEquals("myServletContextName", webXml.getDisplayName());
        assertTrue(webXml.isDistributable());
        assertEquals("UTF-8", webXml.getResponseCharacterEncoding());
    }
    
    /**
     * Test parse method.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testParseWebXml2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource(new File("src/test/webxml/test4")));
        InputStream inputStream = webApplication.getResourceAsStream("WEB-INF/web.xml");
        WebXmlParser parser = new WebXmlParser();
        WebXml webXml = parser.parse(inputStream);
        assertNotEquals("/defaultContextPath", webXml.getDefaultContextPath());
        assertFalse(webXml.getDenyUncoveredHttpMethods());
        assertNotEquals("myServletContextName", webXml.getDisplayName());
        assertFalse(webXml.isDistributable());
        assertNotEquals("UTF-8", webXml.getResponseCharacterEncoding());
    }
}
