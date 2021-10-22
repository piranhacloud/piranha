/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.webxml.tests;

import cloud.piranha.extension.mimetype.DefaultMimeTypeManager;
import cloud.piranha.extension.webxml.WebXmlInitializer;
import cloud.piranha.resource.DirectoryResource;
import cloud.piranha.core.api.MimeTypeManager;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests testing web.xml &lt;mime-mapping&gt;.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class MimeMappingTest {

    /**
     * Test getMimeType method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetMimeType() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        DefaultMimeTypeManager manager = new DefaultMimeTypeManager();
        webApplication.setAttribute(MimeTypeManager.class.getName(), manager);
        webApplication.addResource(new DirectoryResource(new File("src/test/webxml/mimeMapping")));
        webApplication.addInitializer(new WebXmlInitializer());
        webApplication.initialize();
        assertEquals("application/x-java-class", webApplication.getMimeType("my.class"));
    }
}
