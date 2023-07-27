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
package cloud.piranha.feature.webapp;

import cloud.piranha.core.api.WebApplicationExtension;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the WebAppFeature class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class WebAppFeatureTest {
    
    /**
     * Test getExtensionClass method.
     */
    @Test
    public void testGetExtensionClass() {
        WebAppFeature feature = new WebAppFeature();
        assertNull(feature.getExtensionClass());
        feature.setExtensionClass(WebApplicationExtension.class);
        assertNotNull(feature.getExtensionClass());
    }
    
    /**
     * Test getContextPath method.
     */
    @Test
    public void testGetContextPath() {
        WebAppFeature feature = new WebAppFeature();
        assertNull(feature.getContextPath());
        feature.setContextPath("/contextpath");
        assertEquals("/contextpath", feature.getContextPath());
    }
    
    /**
     * Test getWarFile method.
     */
    @Test
    public void testGetWarFile() {
        WebAppFeature feature = new WebAppFeature();
        assertNull(feature.getWarFile());
        File warFile = new File("test.war");
        feature.setWarFile(warFile);
        assertEquals(warFile, feature.getWarFile());
    }
    
    /**
     * Test getWebAppDir method.
     */
    @Test
    public void testGetWebAppDir() {
        WebAppFeature feature = new WebAppFeature();
        assertNull(feature.getWebAppDir());
        File webAppDir = new File("test");
        feature.setWebAppDir(webAppDir);
        assertEquals(webAppDir, feature.getWebAppDir());
    }
}
