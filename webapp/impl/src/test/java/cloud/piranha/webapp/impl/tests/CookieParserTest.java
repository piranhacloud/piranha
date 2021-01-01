/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of the copyright holder nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
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
 *
 */

package cloud.piranha.webapp.impl.tests;

import cloud.piranha.webapp.impl.CookieParser;
import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CookieParserTest {

    @Nested
    class RFC2109 {
        @Test
        void parseSingleCookie() {
            Cookie[] cookies = CookieParser.parse("$Version=\"1\"; Customer=\"WILE_E_COYOTE\"; $Path=\"/acme\"");
            assertNotNull(cookies);
            assertEquals(1, cookies.length);
            Cookie cookie = cookies[0];
            assertEquals(1, cookie.getVersion());
            assertEquals("Customer", cookie.getName());
            assertEquals("WILE_E_COYOTE", cookie.getValue());
            assertEquals("/acme", cookie.getPath());
        }

        @Test
        void parseMultipleCookies() {
            Cookie[] cookies = CookieParser.parse("$Version=\"1\";Customer=\"WILE_E_COYOTE\"; $Path=\"/acme\";\nPart_Number=\"Rocket_Launcher_0001\"; $Path=\"/ammo\"");
            assertNotNull(cookies);
            assertEquals(2, cookies.length);
            Cookie cookie1 = cookies[0];
            assertEquals(1, cookie1.getVersion());
            assertEquals("Customer", cookie1.getName());
            assertEquals("WILE_E_COYOTE", cookie1.getValue());
            assertEquals("/acme", cookie1.getPath());

            Cookie cookie2 = cookies[1];
            assertEquals(1, cookie2.getVersion());
            assertEquals("Part_Number", cookie2.getName());
            assertEquals("Rocket_Launcher_0001", cookie2.getValue());
            assertEquals("/ammo", cookie2.getPath());
        }
    }

    @Nested
    class Netscape {
        @Test
        void parseSingleCookie() {
            Cookie[] cookies = CookieParser.parse("CUSTOMER=WILE_E_COYOTE");
            assertNotNull(cookies);
            assertEquals(1, cookies.length);
            Cookie cookie = cookies[0];
            assertEquals(0, cookie.getVersion());
            assertEquals("CUSTOMER", cookie.getName());
            assertEquals("WILE_E_COYOTE", cookie.getValue());
        }

        @Test
        void parseMultipleCookies() {
            Cookie[] cookies = CookieParser.parse("CUSTOMER=WILE_E_COYOTE; PART_NUMBER=ROCKET_LAUNCHER_0001");
            assertNotNull(cookies);
            assertEquals(2, cookies.length);
            Cookie cookie1 = cookies[0];
            assertEquals(0, cookie1.getVersion());
            assertEquals("CUSTOMER", cookie1.getName());
            assertEquals("WILE_E_COYOTE", cookie1.getValue());

            Cookie cookie2 = cookies[1];
            assertEquals(0, cookie2.getVersion());
            assertEquals("PART_NUMBER", cookie2.getName());
            assertEquals("ROCKET_LAUNCHER_0001", cookie2.getValue());
        }
    }
}
