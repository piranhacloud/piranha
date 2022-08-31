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
package cloud.piranha.test.coreprofile;

import cloud.piranha.core.impl.DefaultWebApplication;
import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.extension.coreprofile.CoreProfileExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the 'Hello World!' bean.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class HelloWorldBeanTest {

    /**
     * Test /helloworld.
     *
     * @throws Exception
     */
    @Test
    @Disabled
    void testHelloWorld() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .webApplication(webApplication)
                .directoryResource("src/main/webapp")
                .aliasedDirectoryResource("target/classes", "/WEB-INF/classes")
                .extension(CoreProfileExtension.class)
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .contextPath("")
                .servletPath("/helloworld")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        assertEquals(200, response.getStatus());
        assertTrue(response.getResponseAsString().contains("Hello World!"));
        piranha.stop().destroy();
    }
}
