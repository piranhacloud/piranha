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
package cloud.piranha.embedded;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * The tests for the AsyncContext API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AsyncContextTest {

    /**
     * Test a bad async listener.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testBadAsyncListener() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("BadAsyncServlet", BadAsyncServlet.class.getName(), true)
                .servletMapping("BadAsyncServlet", "/badasync/*")
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/badasync/BadAsyncListener")
                .build();
        EmbeddedResponse response = new EmbeddedResponseBuilder()
                .bodyOnly(false)
                .build();
        piranha.service(request, response);
        while (!response.isCommitted()) {
            Thread.sleep(500);
        }
        assertTrue(response.getResponseAsString().contains("HTTP/1.1 200"));
        assertTrue(response.getResponseAsString().contains("SUCCESS"));
        piranha.stop()
                .destroy();
    }

    /**
     * Test dispatching to another servlet.
     */
    @Test
    public void testDispatch() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .servlet("Dispatch1Servlet", Dispatch1Servlet.class.getName(), true)
                .servletMapping("Dispatch1Servlet", "/dispatch1/*")
                .servlet("Dispatch1bServlet", Dispatch1bServlet.class.getName(), true)
                .servletMapping("Dispatch1bServlet", "/dispatch1b/*")
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequestBuilder()
                .servletPath("/dispatch1/Dispatch")
                .build();
        EmbeddedResponse response = new EmbeddedResponseBuilder()
                .bodyOnly(false)
                .build();
        piranha.service(request, response);
        while (!response.isCommitted()) {
            Thread.sleep(500);
        }
        assertTrue(response.getResponseAsString().contains("HTTP/1.1 200"));
        assertFalse(response.getResponseAsString().contains("IsAsyncSupported=false"));
        piranha.stop()
                .destroy();
    }
}
