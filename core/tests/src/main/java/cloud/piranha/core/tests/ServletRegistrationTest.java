/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.tests;

import cloud.piranha.core.api.WebApplication;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServlet;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for the ServletRegistration API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class ServletRegistrationTest {

    /**
     * Create the web application.
     * 
     * @return the web application.
     */
    public abstract WebApplication createWebApplication();

    /**
     * Test getClassName method.
     */
    @Test
    void testGetClassName() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testGetClassNameServlet", new TestGetClassNameServlet());
        ServletRegistration registration = webApplication.getServletRegistration("testGetClassNameServlet");
        assertEquals(TestGetClassNameServlet.class.getName(), registration.getClassName());
    }

    /**
     * Test getInitParameter method.
     */
    @Test
    void testGetInitParameter() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testGetInitParameterServlet", new HttpServlet() {});
        ServletRegistration registration = webApplication.getServletRegistration("testGetInitParameterServlet");
        registration.setInitParameter("name", "value");
        assertEquals("value", registration.getInitParameter("name"));
    }
    /**
     * Test getInitParameters method.
     */
    @Test
    void testGetInitParameters() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testGetInitParametersServlet", new HttpServlet() {});
        ServletRegistration registration = webApplication.getServletRegistration("testGetInitParametersServlet");
        assertNotNull(registration.getInitParameters());
    }

    /**
     * Test getName method.
     */
    @Test
    void testGetName() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testGetNameServlet", new HttpServlet() {});
        assertNotNull("servlet", webApplication.getServletRegistration("testGetNameServlet").getName());
    }

    /**
     * Test getRunAsRole method.
     */
    @Test
    void testGetRunAsRole() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testGetRunAsRoleServlet", new HttpServlet() {});
        ServletRegistration.Dynamic registration = (ServletRegistration.Dynamic) 
                webApplication.getServletRegistration("testGetRunAsRoleServlet");
        registration.setRunAsRole("role");
        assertNotNull(registration.getRunAsRole());
        assertEquals("role", registration.getRunAsRole());
    }

    /**
     * Test getFilterRegistration method.
     */
    @Test
    void testGetServletRegistration2() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testGetServletRegistration2Servlet", "TestGetServletRegistration2");
        assertNotNull(webApplication.getServletRegistration("testGetServletRegistration2Servlet"));
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testSetInitParametersServlet", new HttpServlet() {});
        ServletRegistration registration = webApplication.getServletRegistration("testSetInitParametersServlet");
        registration.setInitParameter("name", "value");
        assertTrue(registration.setInitParameters(new HashMap<>()).isEmpty());
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters2() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testSetInitParameters2Servlet", new HttpServlet() {});
        ServletRegistration registration = webApplication.getServletRegistration("testSetInitParameters2Servlet");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(null, null);
        assertThrows(IllegalArgumentException.class, () -> registration.setInitParameters(parameters));
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters3() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testSetInitParameters3Servlet", new HttpServlet() {});
        ServletRegistration registration = webApplication.getServletRegistration("testSetInitParameters3Servlet");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", null);
        assertThrows(IllegalArgumentException.class, () -> registration.setInitParameters(parameters));
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters4() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testSetInitParameters4Servlet", new HttpServlet() {});
        ServletRegistration registration = webApplication.getServletRegistration("testSetInitParameters4Servlet");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        assertTrue(registration.setInitParameters(parameters).isEmpty());
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters5() {
        WebApplication webApplication = createWebApplication();
        webApplication.addServlet("testSetInitParameters5Servlet", new HttpServlet() {});
        ServletRegistration registration = webApplication.getServletRegistration("testSetInitParameters5Servlet");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        assertTrue(registration.setInitParameters(parameters).isEmpty());
        assertFalse(registration.setInitParameters(parameters).isEmpty());
    }

    /**
     * Test servlet that is used by testGetClassName.
     */
    public static class TestGetClassNameServlet extends HttpServlet {
    }
}
