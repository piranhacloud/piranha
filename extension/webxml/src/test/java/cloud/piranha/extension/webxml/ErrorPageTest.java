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
package cloud.piranha.extension.webxml;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import cloud.piranha.core.impl.DefaultWebApplicationResponse;
import cloud.piranha.resource.impl.DirectoryResource;
import static jakarta.servlet.DispatcherType.REQUEST;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests testing web.xml &lt;error-page&gt;.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class ErrorPageTest {

    /**
     * Test error-page element.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testErrorPage() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource(new File("src/test/webxml/errorPage")));
        webApplication.addInitializer(new WebXmlInitializer());
        webApplication.initialize();
        webApplication.start();
        
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setDispatcherType(REQUEST);
        request.setWebApplication(webApplication);

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        
        webApplication.linkRequestAndResponse(request, response);
        
        response.setStatus(404);
        assertEquals("/notfound.jsp", webApplication.getManager()
                .getErrorPageManager().getErrorPage(null, response));
    }

    /**
     * Test error-page element.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testErrorPage2() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource(new File("src/test/webxml/errorPage")));
        webApplication.addInitializer(new WebXmlInitializer());
        webApplication.initialize();
        webApplication.start();
        
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        request.setDispatcherType(REQUEST);
        request.setWebApplication(webApplication);

        
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);
        
        webApplication.linkRequestAndResponse(request, response);
        
        response.setStatus(500);
        assertEquals("/error.jsp", webApplication.getManager()
                .getErrorPageManager().getErrorPage(new IllegalStateException(), response));
    }
}
