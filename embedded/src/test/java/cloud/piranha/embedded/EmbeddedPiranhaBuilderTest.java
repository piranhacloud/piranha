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
package cloud.piranha.embedded;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the EmbeddedPiranhaBuilder class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class EmbeddedPiranhaBuilderTest {

    /**
     * Test listener method.
     * 
     * @throws Exception when an error occurs.
     */
    @Test
    void testListener() throws Exception {
        EmbeddedPiranhaBuilder builder = new EmbeddedPiranhaBuilder();
        EmbeddedPiranha piranha = builder
                .listener(TestServletRequestListener.class.getName())
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequest();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        assertTrue((Boolean) piranha.getWebApplication().getAttribute("requestInitialized"));
    }
    
    /**
     * A test ServletRequestListener used to test the listener method.
     */
    public static class TestServletRequestListener implements ServletRequestListener {

        public TestServletRequestListener() {
        }
        
        @Override
        public void requestInitialized(ServletRequestEvent event) {
            event.getServletContext().setAttribute("requestInitialized", true);
        }
    }
}
