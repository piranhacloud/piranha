/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package cloud.piranha;

import cloud.piranha.DefaultWebApplication;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * The JUnit tests for testing everything related to the ServletContextListener
 * API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletContextListenerTest {

    /**
     * Test contextDestroyed method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testContextDestroyed() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new TestServletContextListener());
        webApplication.initialize();
        webApplication.destroy();
        assertNotNull(webApplication.getAttribute("contextDestroyed"));
    }

    /**
     * Test contextInitialized method.
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    public void testContextInitialized() throws Exception {
        DefaultWebApplication webApplication = new DefaultWebApplication();
        webApplication.addListener(new TestServletContextListener());
        webApplication.initialize();
        webApplication.destroy();
        assertNotNull(webApplication.getAttribute("contextInitialized"));
    }

    /**
     * Test ServletContextListener to validate contextInitialized and
     * contextDestroyed are properly called.
     */
    public class TestServletContextListener implements ServletContextListener {

        /**
         * Context destroyed event.
         *
         * @param event the event.
         */
        @Override
        public void contextDestroyed(ServletContextEvent event) {
            event.getServletContext().setAttribute("contextDestroyed", true);
        }

        /**
         * Context initialized event.
         *
         * @param event the event.
         */
        @Override
        public void contextInitialized(ServletContextEvent event) {
            event.getServletContext().setAttribute("contextInitialized", true);
        }
    }
}
