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
package test.embedded.snoop;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for basic functionality of Piranha Embedded.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SnoopTest {
    
    /**
     * Test basic functionality.
     */
    @Test
    public void testBasicFunctionality() {
        
        try (EmbeddedRequest request = new EmbeddedRequest();
                EmbeddedResponse response = new EmbeddedResponse()) {
            EmbeddedPiranha embedded = new EmbeddedPiranhaBuilder()
                    .servlet("SnoopServlet", SnoopServlet.class)
                    .servletMapping("SnoopServlet", "/Snoop")
                    .buildAndStart();
            request.setCookies(new Cookie[] { new Cookie("MY", "COOKIE") });
            request.setQueryString("snooping=great");
            request.setServletPath("/Snoop");
            embedded.service(request, response);
            embedded.stop();
            
            String result = response.getResponseAsString();

            assertNotNull(result);
            assertNotEquals(0, result.length());

            assertTrue(result.contains("Method: GET"));
            assertTrue(result.contains("Request-Target: /Snoop?snooping=great"));
            assertTrue(result.contains("HTTP-Version: HTTP/1.1"));
            assertTrue(result.contains("Accept: */*"));
            assertTrue(result.contains("MY: COOKIE"));
            assertTrue(result.contains("Dispatcher Type: REQUEST"));
            assertTrue(result.contains("Locale: en_US"));
            assertTrue(result.contains("snooping: great"));
            assertTrue(result.contains("Query String: snooping=great"));

            assertEquals(200, response.getStatus());

        } catch(IOException | ServletException e) {
            fail();
        }
    }
}
