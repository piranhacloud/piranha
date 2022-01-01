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
package cloud.piranha.core.impl.tests;

import cloud.piranha.core.impl.DefaultErrorPageManager;
import cloud.piranha.core.impl.DefaultWebApplicationResponse;
import org.junit.jupiter.api.Test;

import jakarta.servlet.ServletException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DefaultErrorPageManagerTest {
    @Test
    void testPagesByCode() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByCode().put(501, "/501");
        errorPageManager.getErrorPagesByCode().put(404, "/404");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setStatus(501);
        assertEquals("/501", errorPageManager.getErrorPage(null, response));
        response.setStatus(404);
        assertEquals("/404", errorPageManager.getErrorPage(null, response));
    }

    @Test
    void testPagesByCode2() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByCode().put(404, "/404");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setStatus(501);
        assertNull(errorPageManager.getErrorPage(null, response));
    }

    @Test
    void testPagesByCode3() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByCode().put(404, "/404");
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setStatus(500);
        assertNull(errorPageManager.getErrorPage(new NullPointerException(), response));
    }

    @Test
    void testPagesByException() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByException().put(IllegalArgumentException.class.getName(), "/IAE");
        assertEquals("/IAE", errorPageManager.getErrorPage(new IllegalArgumentException(), null));
    }

    @Test
    void testPagesByException2() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByException().put(IndexOutOfBoundsException.class.getName(), "/IOUB");
        assertEquals("/IOUB", errorPageManager.getErrorPage(new ArrayIndexOutOfBoundsException(), null));
    }

    @Test
    void testPagesByException3() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByException().put(IllegalArgumentException.class.getName(), "/IAE");
        assertEquals("/IAE", errorPageManager.getErrorPage(new ServletException(new IllegalArgumentException()), null));
    }

    @Test
    void testPagesByException4() {
        DefaultErrorPageManager errorPageManager = new DefaultErrorPageManager();
        errorPageManager.getErrorPagesByException().put(IllegalArgumentException.class.getName(), "/IAE");
        assertNull(errorPageManager.getErrorPage(new IndexOutOfBoundsException(), null));
    }
}
