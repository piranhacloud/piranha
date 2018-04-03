/*
 *  Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.webapp;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The WebApplication API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplication extends ServletContext {

    /**
     * Add a mapping for the given filter.
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns.
     * @return the possible empty set of already mapped URL patterns.
     * @see FilterRegistration#addMappingForUrlPatterns(EnumSet, boolean,
     * String...)
     */
    Set<String> addFilterMapping(String filterName, String... urlPatterns);

    /**
     * Add a servlet container initializer.
     *
     * @param className the class name.
     */
    void addInitializer(String className);

    /**
     * Add a servlet container initializer.
     *
     * @param className the class name.
     * @param classes the classes to be passed to onStartup.
     */
    void addInitializer(String className, Set<Class<?>> classes);

    /**
     * Add the resource.
     *
     * @param resource the resouce.
     */
    void addResource(Resource resource);

    /**
     * Add a mapping for the given servlet.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns.
     * @return the possible empty set of already mapped URL patterns.
     * @see ServletRegistration#addMapping(String...)
     */
    Set<String> addServletMapping(String servletName, String... urlPatterns);

    /**
     * Get the HttpSessionManager.
     *
     * @return the HttpSessionManager.
     */
    HttpSessionManager getHttpSessionManager();

    /**
     * Get the mappings for a particular servlet.
     *
     * @param servletName the servlet name.
     * @return the possible empty set of mapped URL patterns.
     * @see ServletRegistration#getMappings()
     */
    Collection<String> getMappings(String servletName);

    /**
     * Get the object instance manager.
     *
     * @return the DependencyInjectionManager.
     */
    ObjectInstanceManager getObjectInstanceManager();

    /**
     * Get the associated request.
     *
     * @param response the response.
     * @return the associated request.
     */
    ServletRequest getRequest(ServletResponse response);

    /**
     * Get the associated response.
     *
     * @param request the request.
     * @return the associated response.
     */
    ServletResponse getResponse(ServletRequest request);

    /**
     * Get the security manager.
     *
     * @return the security manager.
     */
    SecurityManager getSecurityManager();

    /**
     * Initialize the web application.
     */
    void initialize();

    /**
     * Link the request and response.
     *
     * @param request the request.
     * @param response the response.
     */
    void linkRequestAndResponse(ServletRequest request, ServletResponse response);

    /**
     * Service the request.
     *
     * @param request the request.
     * @param response the response.
     * @throws ServletException when a servlet error occurs.
     * @throws IOException when an I/O error occurs.
     */
    void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException;

    /**
     * Set the class loader.
     *
     * @param classLoader the class loader.
     */
    void setClassLoader(ClassLoader classLoader);

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     */
    void setContextPath(String contextPath);

    /**
     * Set the HTTP session manager.
     *
     * @param httpSessionManager the HTTP session manager.
     */
    void setHttpSessionManager(HttpSessionManager httpSessionManager);

    /**
     * Set the logging manager.
     *
     * @param loggingManager the logging manager.
     */
    void setLoggingManager(LoggingManager loggingManager);

    /**
     * Set the mimeType manager.
     *
     * @param mimeTypeManager the mimeType manager.
     */
    void setMimeTypeManager(MimeTypeManager mimeTypeManager);

    /**
     * Set the object instance manager.
     *
     * @param objectInstanceManager the object instance manager.
     */
    void setObjectInstanceManager(ObjectInstanceManager objectInstanceManager);

    /**
     * Set the resource manager.
     *
     * @param resourceManager the resource manager.
     */
    void setResourceManager(ResourceManager resourceManager);

    /**
     * Set the security manager.
     *
     * @param securityManager the security manager.
     */
    void setSecurityManager(SecurityManager securityManager);

    /**
     * Set the servlet context name.
     *
     * @param servletContextName the servlet context name.
     */
    void setServletContextName(String servletContextName);

    /**
     * Set the web application request mapper.
     *
     * @param webApplicationRequestMapper the web application request mapper.
     */
    void setWebApplicationRequestMapper(WebApplicationRequestMapper webApplicationRequestMapper);

    /**
     * Start servicing.
     */
    void start();

    /**
     * Stop servicing.
     */
    void stop();

    /**
     * Unlink the request and response.
     *
     * @param request the request.
     * @param response the response.
     */
    void unlinkRequestAndResponse(ServletRequest request, ServletResponse response);
}
