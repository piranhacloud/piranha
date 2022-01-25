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
package cloud.piranha.embedded;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.impl.DefaultWebApplication;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The JUnit tests for the EmbeddedResponseBuilder class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EmbeddedResponseBuilderTest {

    /**
     * Test bodyOnly method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testBodyOnly() throws Exception {
        EmbeddedResponse response = new EmbeddedResponseBuilder()
                .bodyOnly(true)
                .build();
        response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        response.addHeader("header", "not there");
        response.flush();
        assertFalse(response.getResponseAsString().contains("not there"));
    }

    /**
     * Test bodyOnly method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testBodyOnly2() throws Exception {
        EmbeddedResponse response = new EmbeddedResponseBuilder()
                .bodyOnly(false)
                .build();
        response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        response.addHeader("header", "there");
        response.flush();
        assertTrue(response.getResponseAsString().contains("header: there"));
    }

    /**
     * Test webApplication method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    public void testWebApplication() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        EmbeddedResponse response = new EmbeddedResponseBuilder()
                .webApplication(webApplication)
                .build();
        assertTrue(response.getWebApplication() instanceof WebApplication);
        assertEquals(webApplication, response.getWebApplication());
    }
}
