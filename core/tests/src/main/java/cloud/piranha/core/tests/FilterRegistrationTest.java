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
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * The JUnit tests for FilterRegistration API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class FilterRegistrationTest {

    /**
     * Create the web application,
     * 
     * @return the web application.
     */
    public abstract WebApplication createWebApplication();
    
    /**
     * Test getName method.
     */
    @Test
    void testGetName() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetNameFilter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        FilterRegistration registration = webApplication.getFilterRegistration("testGetNameFilter");
        assertEquals("testGetNameFilter", registration.getName());
    }

    /**
     * Test getClassName method.
     */
    @Test
    void testGetClassName() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetClassNameFilter", TestGetClassNameFilter.class);
        FilterRegistration registration = webApplication.getFilterRegistration("testGetClassNameFilter");
        assertEquals(TestGetClassNameFilter.class.getName(),
                registration.getClassName());
    }

    /**
     * Test getInitParameters method.
     */
    @Test
    void testGetInitParameters() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetInitParametersFilter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        FilterRegistration registration = webApplication.getFilterRegistration("testGetInitParametersFilter");
        assertNotNull(registration.getInitParameters());
    }

    /**
     * Test getUrlPatternMappings method.
     */
    @Test
    void testGetUrlPatternMappings() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testGetUrlPatternMappingsFilter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        FilterRegistration registration = webApplication.getFilterRegistration("testGetUrlPatternMappingsFilter");
        assertTrue(registration.getUrlPatternMappings().isEmpty());
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testSetInitParametersFilter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParametersFilter");
        registration.setInitParameter("name", "value");
        assertTrue(registration.setInitParameters(new HashMap<>()).isEmpty());
    }

    /**
     * Test setInitParameters method.
     */
    @Test
    void testSetInitParameters2() {
        WebApplication webApplication = createWebApplication();
        webApplication.addFilter("testSetInitParameters2Filter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParameters2Filter");
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
        webApplication.addFilter("testSetInitParameters3Filter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParameters3Filter");
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
        webApplication.addFilter("testSetInitParameters4Filter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParameters4Filter");
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
        webApplication.addFilter("testSetInitParameters5Filter", new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            }
        });
        FilterRegistration registration = webApplication.getFilterRegistration("testSetInitParameters5Filter");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("name", "value");
        assertTrue(registration.setInitParameters(parameters).isEmpty());
        assertFalse(registration.setInitParameters(parameters).isEmpty());
    }
    
    public class TestGetClassNameFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                FilterChain chain) throws IOException, ServletException {
        }
    }
}
