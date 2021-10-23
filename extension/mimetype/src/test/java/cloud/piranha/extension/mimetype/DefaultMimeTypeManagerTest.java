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
package cloud.piranha.extension.mimetype;

import cloud.piranha.core.api.MimeTypeManager;
import cloud.piranha.core.impl.DefaultWebApplication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the DefaultMimeTypeManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultMimeTypeManagerTest {

    /**
     * Stores the mime type manager.
     */
    protected MimeTypeManager manager;

    /**
     * Before testing.
     */
    @BeforeEach
    void before() {
        manager = new DefaultMimeTypeManager();
    }

    /**
     * Test addMimeType method.
     */
    @Test
    void testAddMimeType() {
        assertNull(manager.getMimeType("my.class"));
        manager.addMimeType("class", "application/x-java-class");
        assertEquals("application/x-java-class", manager.getMimeType("my.class"));
    }

    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType() {
        assertEquals("text/css", manager.getMimeType("TEST.CSS"));
        assertEquals("text/javascript", manager.getMimeType("TEST.JS"));
    }

    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType2() {
        DefaultMimeTypeManager mimeTypeManager = new DefaultMimeTypeManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setAttribute(MimeTypeManager.class.getName(), mimeTypeManager);
        assertNull(webApp.getMimeType("this_maps_to.null"));
    }

    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType3() {
        DefaultMimeTypeManager mimeTypeManager = new DefaultMimeTypeManager();
        mimeTypeManager.addMimeType("class", "application/x-java-class");
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setAttribute(MimeTypeManager.class.getName(), mimeTypeManager);
        assertEquals(webApp.getMimeType("my.class"), "application/x-java-class");
    }

    /**
     * Test getMimeType method.
     */
    @Test
    void testGetMimeType4() {
        DefaultMimeTypeManager mimeTypeManager = new DefaultMimeTypeManager();
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.setAttribute(MimeTypeManager.class.getName(), mimeTypeManager);
        assertNull(webApp.getMimeType("myclass"));
    }
}
