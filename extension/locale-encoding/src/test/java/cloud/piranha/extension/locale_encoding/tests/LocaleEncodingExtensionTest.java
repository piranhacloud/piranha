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
package cloud.piranha.extension.locale_encoding.tests;

import cloud.piranha.core.api.LocaleEncodingManager;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.extension.locale_encoding.LocaleEncodingExtension;
import cloud.piranha.extension.webxml.WebXmlExtension;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the ByteArrayExtension class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class LocaleEncodingExtensionTest {

    /**
     * Test getCharacterEncoding.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetCharacterEncoding() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .directoryResource("src/test/webxml/localeEncodingMappingList1")
                .extension(LocaleEncodingExtension.class)
                .extension(WebXmlExtension.class)
                .build()
                .start();
        WebApplication webApp = piranha.getWebApplication();

        EmbeddedResponse response = new EmbeddedResponse();
        response.setWebApplication(webApp);

        response.setContentType("text/html");
        assertEquals("iso-8859-1", response.getCharacterEncoding().toLowerCase());

        /*
         * setLocale should change character encoding based on
         * locale-encoding-mapping-list
         */
        response.setLocale(Locale.JAPAN);
        assertEquals("euc-jp", response.getCharacterEncoding().toLowerCase());

        /*
         * setLocale should change character encoding based on
         * locale-encoding-mapping-list
         */
        response.setLocale(Locale.CHINA);
        assertEquals("gb18030", response.getCharacterEncoding().toLowerCase());

        /*
         * setContentType here doesn't define character encoding (so character
         * encoding should stay as it is)
         */
        response.setContentType("text/html");
        assertEquals("gb18030", response.getCharacterEncoding().toLowerCase());

        /*
         * setCharacterEncoding should still be able to change encoding
         */
        response.setCharacterEncoding("utf-8");
        assertEquals("utf-8", response.getCharacterEncoding().toLowerCase());

        /*
         * setLocale should not override explicit character encoding request
         */
        response.setLocale(Locale.JAPAN);
        assertEquals("utf-8", response.getCharacterEncoding().toLowerCase());

        /*
         * setContentType should still be able to change encoding
         */
        response.setContentType("text/html;charset=gb18030");
        assertEquals("gb18030", response.getCharacterEncoding().toLowerCase());

        /*
         * setCharacterEncoding should still be able to change encoding
         */
        response.setCharacterEncoding("utf-8");
        assertEquals("utf-8", response.getCharacterEncoding());

        /*
         * getWriter should freeze the character encoding
         */
        response.getWriter();
        assertEquals("utf-8", response.getCharacterEncoding());

        /*
         * setCharacterEncoding should no longer be able to change the encoding
         */
        response.setCharacterEncoding("iso-8859-1");
        assertEquals("utf-8", response.getCharacterEncoding());

        /*
         * setLocale should not override explicit character encoding request
         */
        response.setLocale(Locale.JAPAN);
        assertEquals("utf-8", response.getCharacterEncoding());
    }

    /**
     * Test getCharacterEncoding method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testGetCharacterEncoding2() throws Exception {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .extension(LocaleEncodingExtension.class)
                .build()
                .start();
        WebApplication webApp = piranha.getWebApplication();

        EmbeddedResponse response = new EmbeddedResponse();
        response.setWebApplication(webApp);
        LocaleEncodingManager localeEncodingManager = webApp.getLocaleEncodingManager();
        localeEncodingManager.addCharacterEncoding(Locale.JAPAN.toString(), "euc-jp");
        localeEncodingManager.addCharacterEncoding(Locale.CHINA.toString(), "gb18030");

        response.setContentType("text/html");
        assertEquals("iso-8859-1", response.getCharacterEncoding().toLowerCase());

        /*
         * setLocale should change character encoding based on
         * locale-encoding-mapping-list
         */
        response.setLocale(Locale.JAPAN);
        assertEquals("euc-jp", response.getCharacterEncoding().toLowerCase());

        /*
         * setLocale should change character encoding based on
         * locale-encoding-mapping-list
         */
        response.setLocale(Locale.CHINA);
        assertEquals("gb18030", response.getCharacterEncoding().toLowerCase());

        /*
         * setContentType here doesn't define character encoding (so character
         * encoding should stay as it is)
         */
        response.setContentType("text/html");
        assertEquals("gb18030", response.getCharacterEncoding().toLowerCase());

        /*
         * setCharacterEncoding should still be able to change encoding
         */
        response.setCharacterEncoding("utf-8");
        assertEquals("utf-8", response.getCharacterEncoding().toLowerCase());

        /*
         * setLocale should not override explicit character encoding request
         */
        response.setLocale(Locale.JAPAN);
        assertEquals("utf-8", response.getCharacterEncoding().toLowerCase());

        /*
         * setContentType should still be able to change encoding
         */
        response.setContentType("text/html;charset=gb18030");
        assertEquals("gb18030", response.getCharacterEncoding().toLowerCase());

        /*
         * setCharacterEncoding should still be able to change encoding
         */
        response.setCharacterEncoding("utf-8");
        assertEquals("utf-8", response.getCharacterEncoding());

        /*
         * getWriter should freeze the character encoding
         */
        response.getWriter();
        assertEquals("utf-8", response.getCharacterEncoding());

        /*
         * setCharacterEncoding should no longer be able to change the encoding
         */
        response.setCharacterEncoding("iso-8859-1");
        assertEquals("utf-8", response.getCharacterEncoding());

        /*
         * setLocale should not override explicit character encoding request
         */
        response.setLocale(Locale.JAPAN);
        assertEquals("utf-8", response.getCharacterEncoding());
    }

    /**
     * Test setLocale method on WebApplicationResponse.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testSetLocale() {
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .extension(LocaleEncodingExtension.class)
                .build()
                .start();
        WebApplication webApp = piranha.getWebApplication();
        webApp.getLocaleEncodingManager().addCharacterEncoding(Locale.ITALY.toString(), "windows-1252");
        EmbeddedResponse response = new EmbeddedResponse();
        response.setWebApplication(webApp);
        response.setLocale(Locale.ITALY);
        assertEquals("windows-1252", response.getCharacterEncoding());
    }
}
