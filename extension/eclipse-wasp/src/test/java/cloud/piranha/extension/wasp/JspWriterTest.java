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
package cloud.piranha.extension.wasp;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedRequestBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests verifying JspWriter functionality.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class JspWriterTest {

    /**
     * Test clearBuffer method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testClearBuffer() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .directoryResource("src/test/webapp/jspwriter")
                .initializer(WaspInitializer.class.getName())
                .buildAndStart();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/clearBuffer.jsp")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        piranha.stop().destroy();
        assertEquals(200, response.getStatus());
        assertFalse(response.getResponseAsString().contains("Test FAILED"));
        assertTrue(response.getResponseAsString().contains("Test PASSED"));
    }

    /**
     * Test close method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testClose() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .directoryResource("src/test/webapp/jspwriter")
                .initializer(WaspInitializer.class.getName())
                .buildAndStart();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/close.jsp")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        try {
            piranha.service(request, response);
        } catch (IOException ioe) {
            assertEquals("Stream closed", ioe.getMessage());
        }
        piranha.stop().destroy();
    }

    /**
     * Test close method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testClose2() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .directoryResource("src/test/webapp/jspwriter")
                .initializer(WaspInitializer.class.getName())
                .buildAndStart();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/close2.jsp")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        try {
            piranha.service(request, response);
        } catch (IOException ioe) {
            assertEquals("Stream closed", ioe.getMessage());
        }
        piranha.stop().destroy();
    }

    /**
     * Test close method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testClose3() {
        try {
            EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                    .directoryResource("src/test/webapp/jspwriter")
                    .initializer(WaspInitializer.class.getName())
                    .buildAndStart();
            EmbeddedRequest request = new EmbeddedRequestBuilder()
                    .servletPath("/close3.jsp")
                    .build();
            EmbeddedResponse response = new EmbeddedResponse();
            piranha.service(request, response);
            piranha.stop().destroy();
        } catch (IOException | ServletException ex) {
            fail();
        }
    }

    /**
     * Test close method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testClose4() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .directoryResource("src/test/webapp/jspwriter")
                .initializer(WaspInitializer.class.getName())
                .buildAndStart();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/close4.jsp")
                .build();
        EmbeddedResponse response = new EmbeddedResponse();
        try {
            piranha.service(request, response);
        } catch (IOException ioe) {
            assertEquals("Stream closed", ioe.getMessage());
        }
        WebApplication webApplication = piranha.getWebApplication();
        assertNotNull(webApplication.getAttribute("flush.exception"));
        assertNotNull(webApplication.getAttribute("write.exception"));
        assertNull(webApplication.getAttribute("close.exception"));
        piranha.stop().destroy();
    }
}
