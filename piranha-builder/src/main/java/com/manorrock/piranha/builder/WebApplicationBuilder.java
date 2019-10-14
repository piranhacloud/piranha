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
package com.manorrock.piranha.builder;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.Servlet;

import com.manorrock.piranha.DefaultWebApplication;
import com.manorrock.piranha.api.Resource;
import com.manorrock.piranha.api.WebApplication;

/**
 * A WebAppliction builder.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 * @author Arjan Tijms
 */
public class WebApplicationBuilder {

    /**
     * Stores the WebApplication.
     */
    private final WebApplication webApplication;
    
    private final List<Entry<Class<? extends Filter>, String[]>> filters = new ArrayList<>();

    /**
     * Create a new WebApplication builder.
     * 
     * @return the WebApplication builder.
     */
    public static WebApplicationBuilder newWebApplication() {
        return new WebApplicationBuilder();
    }
    
    /**
     * Create a new WebApplication builder.
     * 
     * @param webApplication the WebApplication.
     * @return the new WebApplication builder.
     */
    public static WebApplicationBuilder newWebApplication(WebApplication webApplication) {
        return new WebApplicationBuilder(webApplication);
    }
    
    /**
     * Constructor.
     */
    public WebApplicationBuilder() {
        this(new DefaultWebApplication());
    }
    
    /**
     * Constructor.
     * 
     * @param webApplication the WebApplication.
     */
    public WebApplicationBuilder(WebApplication webApplication) {
        this.webApplication = webApplication;
    }
    
    /**
     * Adds the init parameter.
     *
     * @param name the name.
     * @param value the value.
     * @return the WebApplication builder.
     */
    public WebApplicationBuilder addInitParameter(String name, String value) {
        webApplication.setInitParameter(name, value);
        return this;
    }
    
    /**
     * Adds the attribute.
     *
     * @param name the name.
     * @param value the object value.
     * @return the WebApplication builder.
     */
    public WebApplicationBuilder addAttribute(String name, Object value) {
        webApplication.setAttribute(name, value);
        return this;
    }

    /**
     * Add a resource.
     * 
     * @param resource the resource.
     * @return the WebApplication builder.
     */
    public WebApplicationBuilder addResource(Resource resource) {
        webApplication.addResource(resource);
        return this;
    }

    /**
     * Add an initializer.
     * 
     * @param clazz the initializer class.
     * @return the WebApplication builder.
     */
    public WebApplicationBuilder addInitializer(Class<?> clazz) {
        webApplication.addInitializer(clazz.getName());
        return this;
    }

    /**
     * Add an initializer.
     * 
     * @param clazz the initializer class.
     * @param classes the onStartup classes.
     * @return the WebApplication builder.
     */
    public WebApplicationBuilder addInitializer(Class<?> clazz, Set<Class<?>> classes) {
        webApplication.addInitializer(clazz.getName(), classes);
        return this;
    }

    /**
     * Add the initializer.
     * 
     * @param className the initializer class name.
     * @return the webApplication builder.
     */
    public WebApplicationBuilder addInitializer(String className) {
        webApplication.addInitializer(className);
        return this;
    }

    /**
     * Add the initializer.
     * 
     * @param className the initializer class name.
     * @param classes the onStartup classes.
     * @return the WebApplicationBuilder.
     */
    public WebApplicationBuilder addInitializer(String className, Set<Class<?>> classes) {
        webApplication.addInitializer(className, classes);
        return this;
    }
    
    /**
     * Add a Filter as the first one in the chain, before initializers have a chance to add filters
     * 
     * @param filterClass class of the filter to add
     * @param urlPatterns URL patterns the filter is mapped to
     * @return the WebApplicationBuilder
     */
    public WebApplicationBuilder addFilterFirst(Class<? extends Filter> filterClass, String... urlPatterns) {
        filters.add(new SimpleImmutableEntry<>(filterClass, urlPatterns));
        return this;
    }
    
    /**
     * Add a Filter ordered by the call sequence of other addFilter calls and filters added by addInitializer calls
     * 
     * @param filterClass class of the filter to add
     * @param urlPatterns URL patterns the filter is mapped to
     * @return the WebApplicationBuilder
     */
    public WebApplicationBuilder addFilter(Class<? extends Filter> filterClass, String... urlPatterns) {
        webApplication.addInitializer(new AddFilterInitializer(filterClass, urlPatterns));
        return this;
    }
    
    /**
     * Add a Servlet
     * 
     * @param servletClass class of the Servlet to add
     * @param urlPatterns URL patterns the Servlet is mapped to
     * @return the WebApplicationBuilder
     */
    public WebApplicationBuilder addServlet(Class<? extends Servlet> servletClass, String... urlPatterns) {
        webApplication.addServlet(servletClass.getSimpleName(), servletClass);
        webApplication.addServletMapping(servletClass.getSimpleName(), urlPatterns);
        
        return this;
    }
    
    /**
     * Gets the web application.
     * 
     * <p>
     * This is useful if the web application needs to be handled before starting it.
     * 
     * @return the web application
     */
    public WebApplication getWebApplication() {
        return webApplication;
    }

    /**
     * Start the WebApplication.
     * 
     * @return the WebApplication.
     */
    public WebApplication start() {
        for (Entry<Class<? extends Filter>, String[]> filterEntry : filters) {
            webApplication.addFilter(filterEntry.getKey().getSimpleName(), filterEntry.getKey());
            webApplication.addFilterMapping(filterEntry.getKey().getSimpleName(), filterEntry.getValue());
        }
        
        webApplication.initialize();
        webApplication.start();
        return webApplication;
    }
}
