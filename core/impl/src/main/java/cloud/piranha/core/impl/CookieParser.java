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
package cloud.piranha.core.impl;

import jakarta.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Cookie parser
 */
public class CookieParser {

    /**
     * Stores the $Version= constant
     */
    private static final String VERSION = "$Version=";

    private CookieParser() {}

    /**
     * Parse the Cookie header
     * @param cookieValues the Cookie header, without "Cookie:"
     * @return the {@link Cookie} array
     * @throws NullPointerException if cookieValues is null
     * @throws IllegalArgumentException if some cookie contains a illegal character
     */
    public static Cookie[] parse(String cookieValues) {
        Objects.requireNonNull(cookieValues);
        if (cookieValues.startsWith(VERSION)) {
            return parseRFC2109(cookieValues.substring(cookieValues.indexOf(';') + 1));
        }
        return parseNetscape(cookieValues);
    }

    private static Cookie[] parseNetscape(String cookiesValue) {
        ArrayList<Cookie> cookieList = new ArrayList<>();
        String[] cookieCandidates = cookiesValue.split(";");
        for (String cookieCandidate : cookieCandidates) {
            String[] cookieString = cookieCandidate.split("=");
            String cookieName = cookieString[0].trim();
            String cookieValue = null;

            if (cookieString.length == 2) {
                cookieValue = cookieString[1].trim();
            }

            Cookie cookie = new Cookie(cookieName, cookieValue);
            cookieList.add(cookie);
        }
        return cookieList.toArray(new Cookie[0]);
    }

    private static Cookie[] parseRFC2109(String cookiesValue) {
        List<Cookie> cookieList = new ArrayList<>();
        String[] cookieCandidates = cookiesValue.split("[;,]");

        Cookie currentCookie = null;
        for (String cookieCandidate : cookieCandidates) {
            String[] values = cookieCandidate.trim().split("=", 2);
            String name = removeQuotes(values[0].trim());
            String value = values.length == 2 ? removeQuotes(values[1].trim()) : null;


            if (name.startsWith("$")) {
                if (currentCookie == null)
                    throw new IllegalArgumentException("Invalid Cookie");

                if ("$Domain".equals(name)) {
                    currentCookie.setDomain(value);
                } else if ("$Path".equals(name)) {
                    currentCookie.setPath(value);
                }

            } else {
                currentCookie = new Cookie(name, value);
                currentCookie.setVersion(1);
                cookieList.add(currentCookie);
            }
        }
        return cookieList.toArray(new Cookie[0]);
    }

    private static String removeQuotes(String value) {
        Objects.requireNonNull(value);
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

}
