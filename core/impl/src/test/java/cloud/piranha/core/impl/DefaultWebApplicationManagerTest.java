/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.WebXml;
import cloud.piranha.core.api.WebXmlManager;
import cloud.piranha.core.api.WelcomeFileManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The JUnit tests for the DefaultWebApplicationManager class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebApplicationManagerTest {

    /**
     * Test getAnnotationManager method.
     */
    @Test
    void testGetAnnotationManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNull(manager.getAnnotationManager());
        manager.setAnnotationManager(new DefaultAnnotationManager());
        assertNotNull(manager.getAnnotationManager());
    }

    /**
     * Test getAsyncManager method.
     */
    @Test
    void testGetAsyncManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getAsyncManager());
        manager.setAsyncManager(null);
        assertNull(manager.getAsyncManager());
    }

    /**
     * Test getDispatcherManager method
     */
    @Test
    void testGetDispatcherManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getDispatcherManager());
    }

    /**
     * Test getErrorPageManager method.
     */
    @Test
    void testGetErrorPageManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getErrorPageManager());
        manager.setErrorPageManager(null);
        assertNull(manager.getErrorPageManager());
    }

    /**
     * Test getHttpSessionManager method.
     */
    @Test
    void testGetHttpSessionManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        manager.setHttpSessionManager(null);
        assertNull(manager.getHttpSessionManager());
    }

    /**
     * Test getHttpSessionManager method.
     */
    @Test
    void testGetHttpSessionManager2() {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        assertNotNull(webApplication.getManager().getHttpSessionManager());
    }

    /**
     * Test getJspManager method.
     */
    @Test
    void testGetJspManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getJspManager());
        manager.setJspManager(null);
        assertNull(manager.getJspManager());
    }

    /**
     * Test getLocaleEncodingManager.
     */
    @Test
    void testGetLocaleEncodingManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getLocaleEncodingManager());
        manager.setLocaleEncodingManager(null);
        assertNull(manager.getLocaleEncodingManager());
    }

    /**
     * Test getMultiPartManager method.
     */
    @Test
    void testGetMultiPartManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getMultiPartManager());
        manager.setMultiPartManager(null);
        assertNull(manager.getMultiPartManager());
    }

    /**
     * Test getObjectInstanceManager method.
     */
    @Test
    void testGetObjectInstanceManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getObjectInstanceManager());
        manager.setObjectInstanceManager(null);
        assertNull(manager.getObjectInstanceManager());
    }

    /**
     * Test getResourceManager method.
     */
    @Test
    void testGetResourceManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getResourceManager());
        manager.setResourceManager(null);
        assertNull(manager.getResourceManager());
    }

    /**
     * Test getSecurityManager method.
     */
    @Test
    void testGetSecurityManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getSecurityManager());
        manager.setSecurityManager(null);
        assertNull(manager.getSecurityManager());
    }

    /**
     * Test getServletRequestManager method.
     */
    @Test
    void testGetServletRequestManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNotNull(manager.getServletRequestManager());
        manager.setServletRequestManager(null);
        assertNull(manager.getServletRequestManager());
    }

    /**
     * Test getWebXmlManager method.
     */
    @Test
    void testGetWebXmlManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNull(manager.getWebXmlManager());
        manager.setWebXmlManager(new WebXmlManager() {
            @Override
            public WebXml getWebXml() {
                return null;
            }
        });
    }

    /**
     * Test getWelcomeFileManager method.
     */
    @Test
    void testGetWelcomeFileManager() {
        DefaultWebApplicationManager manager = new DefaultWebApplicationManager();
        assertNull(manager.getWelcomeFileManager());
        manager.setWelcomeFileManager(new WelcomeFileManager() {
            @Override
            public void addWelcomeFile(String welcomeFile) {
            }

            @Override
            public List<String> getWelcomeFileList() {
                return null;
            }
        });
        assertNotNull(manager.getWelcomeFileManager());
    }
}
