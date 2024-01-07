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
package cloud.piranha.extension.naming;

import static javax.naming.Context.INITIAL_CONTEXT_FACTORY;
import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.jupiter.api.Test;

import cloud.piranha.embedded.EmbeddedPiranha;
import cloud.piranha.embedded.EmbeddedPiranhaBuilder;
import cloud.piranha.embedded.EmbeddedRequest;
import cloud.piranha.embedded.EmbeddedResponse;
import cloud.piranha.extension.naming.cdi.DefaultInitialContextFactory;
import cloud.piranha.extension.naming.cdi.NamingExtension;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
/**
 * The JUnit tests for the NamingExtension class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class NamingExtensionTest {

    /**
     * Test configure method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testConfigure() throws Exception {
        System.setProperty(INITIAL_CONTEXT_FACTORY, DefaultInitialContextFactory.class.getName());
        EmbeddedPiranha piranha = new EmbeddedPiranhaBuilder()
                .extension(NamingExtension.class)
                .listener(TestServletRequestListener.class.getName())
                .build()
                .start();
        EmbeddedRequest request = new EmbeddedRequest();
        EmbeddedResponse response = new EmbeddedResponse();
        piranha.service(request, response);
        Context context1 = (Context) piranha.getWebApplication().getAttribute(Context.class.getName());
        Context context2 = (Context) piranha.getWebApplication().getAttribute("InitialContext");
        assertEquals(
                context1.getEnvironment().get("TheSame"),
                context2.getEnvironment().get("TheSame"));
    }

    /**
     * Test configure method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    void testConfigure2() throws Exception {
        new EmbeddedPiranhaBuilder()
                .extension(NamingExtension.class)
                .listener(TestServletRequestListener.class.getName())
                .build()
                .start();
        assertEquals(System.getProperty(INITIAL_CONTEXT_FACTORY),
                DefaultInitialContextFactory.class.getName());
    }

    /**
     * Inner class used to test if the Context set by listener is equal to the
     * one set as the attribute on the WebApplication instance.
     */
    public static class TestServletRequestListener implements ServletRequestListener {

        /**
         * Constructor.
         */
        public TestServletRequestListener() {
        }

        @Override
        public void requestInitialized(ServletRequestEvent event) {
            try {
                InitialContext context = new InitialContext();
                context.addToEnvironment("TheSame", true);
                event.getServletContext().setAttribute("InitialContext", context);
            } catch (NamingException ne) {
                ne.printStackTrace();
            }
        }
    }
}
