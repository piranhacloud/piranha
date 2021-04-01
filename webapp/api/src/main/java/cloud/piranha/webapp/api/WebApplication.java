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
package cloud.piranha.webapp.api;

import cloud.piranha.policy.api.PolicyManager;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import cloud.piranha.resource.api.Resource;
import cloud.piranha.resource.api.ResourceManager;

/**
 * The WebApplication API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplication extends ServletContext {

    /**
     * Adds an error page to be forwarded to on the given HTTP status code
     *
     * @param code the HTTP code for which the error page is to be invoked
     * @param location the location of the error page
     */
    void addErrorPage(int code, String location);

    /**
     * Adds an error page to be forwarded to on the given exception
     *
     * @param exception the exception for which the error page is to be invoked
     * @param location the location of the error page
     */
    void addErrorPage(String exception, String location);

    /**
     * Add a mapping for the given filter.
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns.
     * @return the possible empty set of already mapped URL patterns.
     * @see FilterRegistration#addMappingForUrlPatterns(EnumSet, boolean,
     * String...)
     */
    default Set<String> addFilterMapping(String filterName, String... urlPatterns) {
        return addFilterMapping(filterName, true, urlPatterns);
    }

    /**
     * Add a mapping for the given filter.
     *
     * @param filterName the filter name.
     * @param isMatchAfter true to call the filter this mapping applies to after declared ones, false to call it before declared ones.
     * @param urlPatterns the URL patterns.
     * @return the possible empty set of already mapped URL patterns.
     * @see FilterRegistration#addMappingForUrlPatterns(EnumSet, boolean,
     * String...)
     */
    default Set<String> addFilterMapping(String filterName, boolean isMatchAfter, String... urlPatterns) {
        return addFilterMapping(null, filterName, isMatchAfter, urlPatterns);
    }

    /**
     * Add a mapping for the given filter.
     *
     * @param dispatcherTypes the dispatcher types. Can be null to use default DispatcherType.REQUEST.
     * @param filterName the filter name.
     * @param isMatchAfter true to call the filter this mapping applies to after declared ones, false to call it before declared ones.
     * @param urlPatterns the URL patterns.
     * @return the possible empty set of already mapped URL patterns.
     * @see FilterRegistration#addMappingForUrlPatterns(EnumSet, boolean,
     * String...)
     */
    Set<String> addFilterMapping(Set<DispatcherType> dispatcherTypes, String filterName, boolean isMatchAfter, String... urlPatterns);

    /**
     * Add a servlet container initializer.
     *
     * @param className the class name.
     */
    void addInitializer(String className);

    /**
     * Add a servlet container initializer.
     *
     * @param servletContainerInitializer the servletContainerInitializer
     * instance
     */
    void addInitializer(ServletContainerInitializer servletContainerInitializer);

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
     * Destroy the web application.
     */
    void destroy();

    /**
     * Gets the annotation manager.
     *
     * @return the annotation manager.
     */
    AnnotationManager getAnnotationManager();

    /**
     * Get the async manager.
     *
     * @return the async manager.
     */
    AsyncManager getAsyncManager();

    /**
     * Get the default Servlet.
     *
     * @return the default Servlet.
     */
    Servlet getDefaultServlet();

    /**
     * Are we denying uncovered HTTP methods.
     *
     * @return true if we are, false otherwise.
     */
    boolean getDenyUncoveredHttpMethods();

    /**
     * Get the mime type manager.
     *
     * @return the mime type manager.
     */
    MimeTypeManager getMimeTypeManager();

    /**
     * Get the multi part manager.
     *
     * @return the multi part manager.
     */
    MultiPartManager getMultiPartManager();
    
    /**
     * Get the policy manager.
     * 
     * @return the policy manager.
     */
    PolicyManager getPolicyManager();

    /**
     * Returns the unique Id of this web application corresponding to this
     * ServletContext.
     *
     * @return the servlet context id.
     */
    default String getServletContextId() {
        return getVirtualServerName() + " " + getContextPath();
    }

    /**
     * Get the HttpSessionManager.
     *
     * @return the HttpSessionManager.
     */
    HttpSessionManager getHttpSessionManager();

    /**
     * Get the HttpRequestManager.
     *
     * @return the HttpRequestManager.
     */
    HttpRequestManager getHttpRequestManager();

    /**
     * Gets the ServletContainerInitializers
     *
     * @return list of ServletContainerInitializers
     */
    List<ServletContainerInitializer> getInitializers();


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
     * Get the welcome file manager.
     *
     * @return the welcome file manager.
     */
    WelcomeFileManager getWelcomeFileManager();

    /**
     * Get the locale encoding manager
     * @return the locale encoding manager
     */
    LocaleEncodingManager getLocaleEncodingManager();

    /**
     * Initialize the web application.
     */
    void initialize();

    /**
     * Marks the end of initializing declared (web.xml, annotations) artifacts
     */
    void initializeDeclaredFinish();

    /**
     * Finish the initialization.
     */
    void initializeFinish();

    /**
     * Initialize the filters.
     */
    void initializeFilters();

    /**
     * Initialize the servlet container initializers.
     */
    void initializeInitializers();

    /**
     * Initialize the servlets.
     */
    void initializeServlets();

    /**
     * Is the application distributable.
     *
     * @return true if it is, false otherwise.
     */
    boolean isDistributable();
    
    /**
     * Is the web application initialized.
     * 
     * @return true if it is, false otherwise.
     */
    boolean isInitialized();

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
     * Set the default servlet.
     *
     * @param defaultServlet the default servlet.
     */
    void setDefaultServlet(Servlet defaultServlet);

    /**
     * Set if we are denying uncovered HTTP methods.
     *
     * @param denyUncoveredHttpMethods the boolean value.
     */
    void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods);

    /**
     * Set if the web application is distributable.
     *
     * @param distributable the distributable flag.
     */
    void setDistributable(boolean distributable);

    /**
     * Set the effective major version.
     *
     * @param version the effective major version.
     */
    void setEffectiveMajorVersion(int version);

    /**
     * Set the effective minor version.
     *
     * @param version the effective minor version.
     */
    void setEffectiveMinorVersion(int version);

    /**
     * Set the HTTP session manager.
     *
     * @param httpSessionManager the HTTP session manager.
     */
    void setHttpSessionManager(HttpSessionManager httpSessionManager);

    /**
     * Set the HTTP request manager.
     *
     * @param httpRequestManager the HTTP request manager.
     */
    void setHttpRequestManager(HttpRequestManager httpRequestManager);

    /**
     * Set the JSP manager.
     *
     * @param jspManager the JSP manager.
     */
    void setJspManager(JspManager jspManager);

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
     * Set the multi part manager.
     *
     * @param multiPartManager the multi part manager.
     */
    void setMultiPartManager(MultiPartManager multiPartManager);

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
     * Sets the annotation manager.
     *
     * @param annotationManager the annotation manager
     */
    void setAnnotationManager(AnnotationManager annotationManager);

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
     * Set the welcome file manager.
     *
     * @param welcomeFileManager the welcome file manager.
     */
    void setWelcomeFileManager(WelcomeFileManager welcomeFileManager);

    /**
     * Set the locale encoding manager
     * @param localeEncodingManager the locale encoding manager
     */
    void setLocaleEncodingManager(LocaleEncodingManager localeEncodingManager);

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
    
    /**
     * Get the manager for the specific type.
     * 
     * @param <T> the underlying type.
     * @param type the type of manager.
     * @return the manager, or null if not found.
     */
    <T extends Object> T getManager(Class<T> type);
    
    /**
     * Set the manager for the specific type.
     * 
     * @param type the type.
     * @param manager the manager.
     */
    void setManager(Class type, Object manager);
}
