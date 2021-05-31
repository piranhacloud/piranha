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
package cloud.piranha.extension.webxml.tests;

import cloud.piranha.extension.webxml.WebXmlInitializer;
import cloud.piranha.resource.DirectoryResource;
import cloud.piranha.webapp.impl.DefaultWebApplication;
import cloud.piranha.webapp.impl.DefaultWebApplicationResponse;
import java.io.File;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for locale-encoding-mapping-list.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class LocaleEncodingMappingListTest {

    /**
     * Test getCharacterEncoding.
     * 
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testGetCharacterEncoding() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addResource(new DirectoryResource(new File("src/test/webxml/localeEncodingMappingList1")));
        webApplication.addInitializer(new WebXmlInitializer());
        webApplication.initialize();

        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setWebApplication(webApplication);

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
}
