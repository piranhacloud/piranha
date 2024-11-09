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
package cloud.piranha.extension.soteria;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.extension.security.servlet.ServletSecurityManager;
import org.glassfish.soteria.SoteriaServiceProviders;
import org.glassfish.soteria.cdi.spi.WebXmlLoginConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the SoteriaPreCDIInitializer class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SoteriaPreCDIInitializerTest {
    
    /**
     * Test onStartup method.
     */
    @Test
    public void testOnStartup() throws Exception {

        /*
         * Setup web application with BASIC auth.
         */
        DefaultWebApplication webApplication = new DefaultWebApplication();
        ServletSecurityManager securityManager = new ServletSecurityManager();
        webApplication.getManager().setSecurityManager(securityManager);
        securityManager.setAuthMethod("BASIC");
        
        /*
         * Run the onStartup of SoteriaPreCDIInitializer to configure itself 
         * correctly for BASIC auth as per web.xml.
         */
        SoteriaPreCDIInitializer initializer = new SoteriaPreCDIInitializer();
        initializer.onStartup(null, webApplication);
        
        /*
         * Validate BASIC auth is configured.
         */
        WebXmlLoginConfig webXmlLoginConfig = SoteriaServiceProviders
                    .getServiceProvider(WebXmlLoginConfig.class);
        assertEquals("BASIC", webXmlLoginConfig.getAuthMethod());
    }
}
