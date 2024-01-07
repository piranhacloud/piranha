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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The JUnit tests for the DefaultWebConnection class.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
class DefaultWebConnectionTest {

    /**
     * Test close method.
     */
    @Test
    void testClose() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        DefaultWebConnection connection = new DefaultWebConnection(request, response);
        connection.close();
        assertFalse(connection.getInputStream().isReady());
        assertFalse(connection.getOutputStream().isReady());
    }
    
    /**
     * Test getInputStream method.
     */
    @Test
    void testGetInputStream() throws Exception {
        DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
        DefaultWebConnection connection = new DefaultWebConnection(request, null);
        assertNotNull(connection.getInputStream());
    }

    /**
     * Test getOutputStream method.
     */
    @Test
    void testGetOutputStream() throws Exception {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        DefaultWebConnection connection = new DefaultWebConnection(null, response);
        assertNotNull(connection.getOutputStream());
    }
}
