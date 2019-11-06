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
package com.manorrock.piranha;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.ServletResponse;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

import com.manorrock.piranha.api.AnnotationManager;
import com.manorrock.piranha.api.Feature;
import com.manorrock.piranha.api.HttpRequestManager;
import com.manorrock.piranha.api.HttpSessionManager;
import com.manorrock.piranha.api.JspManager;
import com.manorrock.piranha.api.LoggingManager;
import com.manorrock.piranha.api.MimeTypeManager;
import com.manorrock.piranha.api.ObjectInstanceManager;
import com.manorrock.piranha.api.Resource;
import com.manorrock.piranha.api.ResourceManager;
import com.manorrock.piranha.api.SecurityManager;
import com.manorrock.piranha.api.WebApplication;
import com.manorrock.piranha.api.WebApplicationRequestMapper;
import com.manorrock.piranha.api.WebApplicationRequestMapping;

/**
 * The default WebApplication.
 *
 * <p>
 * The <code>filters</code> field is backed by a LinkedHashMap so we get an
 * insertion-order key set. If you change this, be aware that methods using this
 * field should be changed to account for that.
 * </p>
 *
 * <p>
 * The <code>servlets</code> field is backed by a LinkedHashMap so we get an
 * insertion-order key set. If you change this, be aware that methods using this
 * field should be changed to account for that.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplication implements WebApplication {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultWebApplication.class.getName());

    /**
     * Stores the INITIALIZED constant.
     */
    protected static final int INITIALIZED = 1;

    /**
     * Stores the ERROR constant.
     */
    protected static final int ERROR = 3;

    /**
     * Stores the SETUP constant.
     */
    protected static final int SETUP = 0;

    /**
     * Stores the SERVICING constant.
     */
    protected static final int SERVICING = 2;

    /**
     * Stores the annotation manager.
     */
    protected AnnotationManager annotationManager;

    /**
     * Stores the attributes.
     */
    protected final HashMap<String, Object> attributes;

    /**
     * Stores the class loader.
     */
    protected ClassLoader classLoader;

    /**
     * Stores the servlet context attribute listeners.
     */
    protected final List<ServletContextAttributeListener> contextAttributeListeners;

    /**
     * Stores the servlet context listeners.
     */
    protected final List<ServletContextListener> contextListeners;

    /**
     * Stores the context path.
     */
    protected String contextPath;

    /**
     * Stores the object instance manager.
     */
    protected ObjectInstanceManager objectInstanceManager;

    /**
     * Stores our features.
     */
    protected List<Feature> features;

    /**
     * Stores the filters.
     */
    protected final Map<String, DefaultFilterEnvironment> filters;

    /**
     * Stores the session manager.
     */
    protected HttpSessionManager httpSessionManager;
    
    /**
     * Stores the request manager.
     */
    protected HttpRequestManager httpRequestManager;

    /**
     * Stores the init parameters.
     */
    protected final Map<String, String> initParameters;

    /**
     * Stores the servlet container initializers.
     */
    protected final List<ServletContainerInitializer> initializers;

    /**
     * Stores the JSP manager.
     */
    protected JspManager jspManager;

    /**
     * Stores the logging manager.
     */
    protected LoggingManager loggingManager;

    /**
     * Stores the mime tyoe manager.
     */
    protected MimeTypeManager mimeTypeManager;

    /**
     * Stores the request character encoding.
     */
    protected String requestCharacterEncoding;

    /**
     * Stores the servlet request listeners.
     */
    protected final List<ServletRequestListener> requestListeners;

    /**
     * Stores the active requests and the associated response.
     */
    protected final Map<ServletRequest, ServletResponse> requests;

    /**
     * Stores the resource manager.
     */
    protected ResourceManager resourceManager;

    /**
     * Stores the response character encoding.
     */
    protected String responseCharacterEncoding;

    /**
     * Stores the active responses and the associated requests.
     */
    protected final Map<ServletResponse, ServletRequest> responses;

    /**
     * Stores the security manager.
     */
    protected SecurityManager securityManager;

    /**
     * Stores the servlet context name.
     */
    protected String servletContextName;

    /**
     * Stores the servlets.
     */
    protected final Map<String, DefaultServletEnvironment> servlets;

    /**
     * Stores the status.
     */
    protected int status;

    /**
     * Stores the virtual server name.
     */
    protected String virtualServerName = "server";

    /**
     * Stores the web application request mapper.
     */
    protected WebApplicationRequestMapper webApplicationRequestMapper;

    /**
     * Constructor.
     */
    public DefaultWebApplication() {
        annotationManager = new DefaultAnnotationManager();
        attributes = new HashMap<>(1);
        classLoader = getClass().getClassLoader();
        contextAttributeListeners = new ArrayList<>(1);
        contextListeners = new ArrayList<>(1);
        contextPath = "";
        features = new ArrayList<>(1);
        filters = new LinkedHashMap<>(1);
        httpSessionManager = new DefaultHttpSessionManager();
        httpRequestManager = new DefaultHttpRequestManager();
        initParameters = new ConcurrentHashMap<>(1);
        initializers = new ArrayList<>(1);
        jspManager = new DefaultJspFileManager();
        loggingManager = new DefaultLoggingManager();
        mimeTypeManager = new DefaultMimeTypeManager();
        objectInstanceManager = new DefaultObjectInstanceManager();
        requestListeners = new ArrayList<>(1);
        requests = new ConcurrentHashMap<>(1);
        resourceManager = new DefaultResourceManager();
        responses = new ConcurrentHashMap<>(1);
        securityManager = new DefaultSecurityManager();
        servlets = new LinkedHashMap<>();
        webApplicationRequestMapper = new DefaultWebApplicationRequestMapper();
    }

    /**
     * Add the feature.
     *
     * @param feature the feature.
     */
    @Override
    public void addFeature(Feature feature) {
        features.add(feature);
    }

    /**
     * Add the filter.
     *
     * @param filterName the filter name.
     * @param className the class name.
     * @return the filter dynamic.
     */
    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        if (status != SERVICING) {
            if (filterName == null || filterName.trim().equals("")) {
                throw new IllegalArgumentException("Filter name cannot be null or empty");
            }

            DefaultFilterEnvironment result;
            if (filters.containsKey(filterName)) {
                result = filters.get(filterName);
            } else {
                result = new DefaultFilterEnvironment();
                result.setFilterName(filterName);
                result.setWebApplication(this);
                filters.put(filterName, result);
            }
            result.setClassName(className);
            return result;
        } else {
            throw new IllegalStateException("Cannot call this after web application has started");
        }
    }

    /**
     * Add the filter.
     *
     * @param filterName the filter name.
     * @param filterClass the filter class.
     * @return the filter dynamic.
     * @see ServletContext#addFilter(java.lang.String, java.lang.Class)
     */
    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        if (status != SERVICING) {
            if (filterName == null || filterName.trim().equals("")) {
                throw new IllegalArgumentException("Filter name cannot be null or empty");
            }

            DefaultFilterEnvironment result;
            if (filters.containsKey(filterName)) {
                result = filters.get(filterName);
            } else {
                result = new DefaultFilterEnvironment();
                result.setFilterName(filterName);
                result.setWebApplication(this);
                filters.put(filterName, result);
            }
            result.setClassName(filterClass.getCanonicalName());
            return result;
        } else {
            throw new IllegalStateException("Cannot call this after web application has started");
        }
    }

    /**
     * Add the filter.
     *
     * @param filterName the filter name.
     * @param filter the filter.
     * @return the filter dynamic registration.
     */
    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        DefaultFilterEnvironment result = new DefaultFilterEnvironment(this, filterName, filter);
        filters.put(filterName, result);
        return result;
    }

    /**
     * Add a mapping for the given filter.
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns.
     * @return the possible empty set of already mapped URL patterns.
     * @see FilterRegistration#addMappingForUrlPatterns(EnumSet, boolean,
     * String...)
     */
    @Override
    public Set<String> addFilterMapping(String filterName, String... urlPatterns) {
        return webApplicationRequestMapper.addFilterMapping(filterName, urlPatterns);
    }

    /**
     * Add a servlet container initializer.
     *
     * @param className the class name.
     */
    @Override
    public void addInitializer(String className) {
        try {
            @SuppressWarnings("unchecked")
            Class<ServletContainerInitializer> clazz = (Class<ServletContainerInitializer>) getClassLoader().loadClass(className);
            initializers.add(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
        }
    }

    @Override
    public void addInitializer(ServletContainerInitializer servletContainerInitializer) {
        initializers.add(servletContainerInitializer);
    }

    /**
     * Add a JSP file.
     *
     * @param servletName the name of the servlet.
     * @param jspFile the JSP file.
     * @return the dynamic servlet registration.
     */
    @Override
    public ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
        return jspManager.addJspFile(this, servletName, jspFile);
    }

    /**
     * Add listener.
     *
     * @param className the class name.
     */
    @Override
    public void addListener(String className) {
        try {
            @SuppressWarnings("unchecked")
            Class<EventListener> clazz = (Class<EventListener>) getClassLoader().loadClass(className);
            addListener(clazz);
        } catch (ClassNotFoundException exception) {
        }
    }

    /**
     * Add the listener.
     *
     * @param type the type.
     */
    @Override
    public void addListener(Class<? extends EventListener> type) {
        if (status != SETUP) {
            throw new IllegalStateException("Illegal to add listener because state is not SETUP");
        }

        try {
            EventListener listener = createListener(type);
            addListener(listener);
        } catch (ServletException exception) {
        }
    }

    /**
     * Add the listener.
     *
     * @param <T> the type.
     * @param listener the listener
     */
    @Override
    public <T extends EventListener> void addListener(T listener) {
        if (listener instanceof ServletContextListener) {
            contextListeners.add((ServletContextListener) listener);
        }

        if (listener instanceof ServletContextAttributeListener) {
            contextAttributeListeners.add((ServletContextAttributeListener) listener);
        }

        if (listener instanceof ServletRequestListener) {
            requestListeners.add((ServletRequestListener) listener);
        }

        if (listener instanceof ServletRequestAttributeListener) {
            httpRequestManager.addListener((ServletRequestAttributeListener) listener);
        }

        if (listener instanceof HttpSessionAttributeListener) {
            httpSessionManager.addListener(listener);
        }

        if (listener instanceof HttpSessionIdListener) {
            httpSessionManager.addListener(listener);
        }

        if (listener instanceof HttpSessionListener) {
            httpSessionManager.addListener(listener);
        }
    }

    /**
     * Add the resource.
     *
     * @param resource the resource.
     */
    @Override
    public void addResource(Resource resource) {
        resourceManager.addResource(resource);
    }

    /**
     * Add the servlet.
     *
     * @param servletName the servlet name.
     * @param servletClass the class type.
     * @return the servlet dynamic.
     */
    @Override
    public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return addServlet(servletName, servletClass.getName());
    }

    /**
     * Add the servlet.
     *
     * @param servletName the servlet name.
     * @param className the class name.
     * @return the servlet dynamic.
     */
    @Override
    public Dynamic addServlet(String servletName, String className) {
        DefaultServletEnvironment result = servlets.get("servletName");
        if (result == null) {
            result = new DefaultServletEnvironment(this, servletName);
            result.setClassName(className);
            servlets.put(servletName, result);
        } else {
            result.setClassName(className);
        }
        return result;
    }

    /**
     * Add the servlet.
     *
     * @param servletName the servlet name.
     * @param servlet the servlet.
     * @return the servlet dynamic.
     */
    @Override
    public Dynamic addServlet(String servletName, Servlet servlet) {
        DefaultServletEnvironment result = new DefaultServletEnvironment(this, servletName, servlet);
        servlets.put(servletName, result);
        return result;
    }

    /**
     * Add the servlet mapping.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns.
     * @return the set of added mappings.
     */
    @Override
    public Set<String> addServletMapping(String servletName, String... urlPatterns) {
        return webApplicationRequestMapper.addServletMapping(servletName, urlPatterns);
    }

    /**
     * Create the filter.
     *
     * @param <T> the return type.
     * @param filterClass the filter class.
     * @return the filter.
     * @throws ServletException when a Filter error occurs.
     */
    @Override
    public <T extends Filter> T createFilter(Class<T> filterClass) throws ServletException {
        return objectInstanceManager.createFilter(filterClass);
    }

    /**
     * Create the listener.
     *
     * @param <T> the type.
     * @param clazz the class of the listener to create.
     * @return the listener.
     * @throws ServletException when it fails to create the listener.
     */
    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        T result = objectInstanceManager.createListener(clazz);
        boolean ok = false;
        if (result instanceof ServletContextListener
                || result instanceof ServletContextAttributeListener
                || result instanceof ServletRequestListener
                || result instanceof ServletRequestAttributeListener
                || result instanceof HttpSessionAttributeListener
                || result instanceof HttpSessionIdListener
                || result instanceof HttpSessionListener) {
            ok = true;
        }
        if (!ok) {
            throw new IllegalArgumentException("Invalid type");
        }
        return result;
    }

    /**
     * Create the servlet.
     *
     * @param <T> the return type.
     * @param servletClass the servlet class.
     * @return the servlet.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public <T extends Servlet> T createServlet(Class<T> servletClass) throws ServletException {
        return objectInstanceManager.createServlet(servletClass);
    }

    /**
     * Declare roles.
     *
     * @param roles the roles.
     */
    @Override
    public void declareRoles(String... roles) {
        securityManager.declareRoles(roles);
    }

    /**
     * Destroy the web application.
     *
     * @throws ServletException when a Servlet error occurs.
     */
    public void destroy() throws ServletException {
        verifyState(INITIALIZED, "Unable to destroy web application");

        servlets.values().stream().forEach((servletEnv) -> {
            servletEnv.getServlet().destroy();
        });
        servlets.clear();

        Collections.reverse(contextListeners);
        contextListeners.stream().forEach((listener) -> {
            listener.contextDestroyed(new ServletContextEvent(this));
        });
        contextListeners.clear();
        status = SETUP;
    }

    /**
     * Find the filter environments.
     *
     * @param request the HTTP servlet request.
     * @return the filter environments.
     */
    protected List<DefaultFilterEnvironment> findFilterEnvironments(HttpServletRequest request) {
        List<DefaultFilterEnvironment> result = null;
        String path = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        Collection<String> filterNames = webApplicationRequestMapper.findFilterMappings(path);
        if (!filterNames.isEmpty()) {
            result = new ArrayList<>();
            for (String filterName : filterNames) {
                if (filters.get(filterName) != null) {
                    result.add(filters.get(filterName));
                }
            }
        }
        return result;
    }

    /**
     * Get the attribute.
     *
     * @param name the attribute name.
     * @return the attribute value.
     */
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * Get the attribute names.
     *
     * @return the attribute names.
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    /**
     * Get the class loader.
     *
     * @return the class loader.
     */
    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Get the servlet context for the given uripath.
     *
     * @param uripath the uripath.
     * @return the servlet context.
     */
    @Override
    public ServletContext getContext(String uripath) {
        return null;
    }

    /**
     * Get the context path.
     *
     * @return the context path.
     */
    @Override
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Get the default session tracking modes.
     *
     * @return the default session tracking modes.
     */
    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return httpSessionManager.getDefaultSessionTrackingModes();
    }

    /**
     * Get the DependencyInjectionManager.
     *
     * @return the DependencyInjectionManager.
     */
    @Override
    public ObjectInstanceManager getObjectInstanceManager() {
        return objectInstanceManager;
    }

    /**
     * Get the effective major version.
     *
     * @return the effective major version.
     */
    @Override
    public int getEffectiveMajorVersion() {
        return getMajorVersion();
    }

    /**
     * Get the effective minor version.
     *
     * @return the effective minor version.
     */
    @Override
    public int getEffectiveMinorVersion() {
        return getMinorVersion();
    }

    /**
     * Get the effective tracking modes.
     *
     * @return the effective tracking modes.
     */
    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return httpSessionManager.getEffectiveSessionTrackingModes();
    }

    /**
     * Get the filter registration.
     *
     * @param filterName the filter name.
     * @return the filter registration, or null if not found.
     */
    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        return filters.get(filterName);
    }

    /**
     * Get the filter registrations.
     *
     * @return the filter registrations.
     */
    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return Collections.unmodifiableMap(filters);
    }

    /**
     * Get the init parameter.
     *
     * @param name the init parameter name.
     * @return the init parameter value.
     */
    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    /**
     * Get the init parameter names.
     *
     * @return the enumeration.
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
    }

    /**
     * Get the JSP config descriptor.
     *
     * @return the JSP config descriptor.
     */
    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return jspManager.getJspConfigDescriptor();
    }

    /**
     * Get the major version.
     *
     * @return the major version.
     */
    @Override
    public int getMajorVersion() {
        return 4;
    }

    /**
     * Get the servlet mappings for the given servlet.
     *
     * @param servletName the name of the servlet.
     * @return the servlet mappings.
     */
    @Override
    public Collection<String> getMappings(String servletName) {
        return webApplicationRequestMapper.getServletMappings(servletName);
    }

    /**
     * Get the mime type.
     *
     * @param filename the filename.
     * @return the mime type.
     */
    @Override
    public String getMimeType(String filename) {
        return mimeTypeManager.getMimeType(filename);
    }

    /**
     * Get the mime type manager.
     * 
     * @return the mime type manager.
     */
    @Override
    public MimeTypeManager getMimeTypeManager() {
        return mimeTypeManager;
    }

    /**
     * Get the minor version.
     *
     * @return the minor version.
     */
    @Override
    public int getMinorVersion() {
        return 0;
    }

    /**
     * Get the name request dispatcher.
     *
     * @param name the name.
     * @return the request dispatcher.
     */
    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        return getNamedDispatcher(name, null);
    }

    /**
     * Get the name request dispatcher.
     *
     * @param name the name.
     * @param path the path.
     * @return the request dispatcher.
     */
    protected RequestDispatcher getNamedDispatcher(String name, String path) {
        RequestDispatcher result = null;
        if (servlets.get(name) != null) {
            result = new DefaultServletRequestDispatcher(servlets.get(name), path);
        }
        return result;
    }

    /**
     * Get the real path.
     *
     * @param path the path
     * @return the real path.
     */
    @Override
    public String getRealPath(String path) {
        String result = null;

        try {
            if (getResource(path) != null) {
                File file = new File(getResource(path).toURI());
                if (file.exists()) {
                    result = file.toString();
                }
            }
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException exception) {
        }

        return result;
    }

    /**
     * Get the request associated with the response.
     *
     * @param response the response.
     * @return the request.
     */
    @Override
    public ServletRequest getRequest(ServletResponse response) {
        return responses.get(response);
    }

    /**
     * Get the default request character encoding.
     *
     * @return the default request character encoding.
     */
    @Override
    public String getRequestCharacterEncoding() {
        return requestCharacterEncoding;
    }

    /**
     * Get the request dispatcher.
     *
     * @param path the path.
     * @return the request dispatcher.
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        RequestDispatcher result = null;
        WebApplicationRequestMapping mapping = webApplicationRequestMapper.findServletMapping(path);
        if (mapping != null) {
            String servletName = webApplicationRequestMapper.getServletName(mapping.getPath());
            if (servletName != null) {
                result = getNamedDispatcher(servletName, path);
            }
        }
        return result;
    }

    /**
     * Get the default response character encoding.
     *
     * @return the default response character encoding.
     */
    @Override
    public String getResponseCharacterEncoding() {
        return responseCharacterEncoding;
    }

    /**
     * Get the resource.
     *
     * @param location the location.
     * @return the URL.
     * @throws MalformedURLException when the URL is malformed.
     */
    @Override
    public URL getResource(String location) throws MalformedURLException {
        return resourceManager.getResource(location);
    }

    /**
     * Get the resource as a stream.
     *
     * @param location the resource location
     * @return the input stream, or null if not found.
     */
    @Override
    public InputStream getResourceAsStream(String location) {
        return resourceManager.getResourceAsStream(location);
    }

    /**
     * Get the resource paths.
     *
     * @param path the path.
     * @return the resource paths.
     */
    @Override
    public Set<String> getResourcePaths(String path) {
        return null;
    }

    /**
     * Get the response.
     *
     * @param request the request.
     * @return the response.
     */
    @Override
    public ServletResponse getResponse(ServletRequest request) {
        return requests.get(request);
    }

    /**
     * Get the security manager.
     *
     * @return the security manager.
     */
    @Override
    public SecurityManager getSecurityManager() {
        return securityManager;
    }
    
    @Override
    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }

    @Override
    public void setAnnotationManager(AnnotationManager annotationManager) {
        this.annotationManager = annotationManager;
    }

    /**
     * Get the server info.
     *
     * @return the server info.
     */
    @Override
    public String getServerInfo() {
        return "";
    }

    /**
     * Get the servlet.
     *
     * @param name the name of the servlet.
     * @return null
     * @throws ServletException when a Servlet error occurs.
     * @deprecated
     */
    @Override
    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException("ServletContext.getServlet(String) is no longer supported");
    }

    /**
     * Get the servlet context name (aka display-name).
     *
     * @return the servlet context name.
     */
    @Override
    public String getServletContextName() {
        return servletContextName;
    }

    /**
     * Get the servlet names.
     *
     * @return the servlet names.
     * @deprecated
     */
    @Override
    public Enumeration<String> getServletNames() {
        throw new UnsupportedOperationException("ServletContext.getServletNames() is no longer supported");
    }

    /**
     * Get the servlet registration.
     *
     * @param servletName the servlet name.
     * @return the servlet registration, or null if not found.
     */
    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        return servlets.get(servletName);
    }

    /**
     * Get the servlet registrations.
     *
     * @return the servlet registrations.
     */
    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return Collections.unmodifiableMap(servlets);
    }

    /**
     * Get the servlets.
     *
     * @return the servlets (empty enumeration).
     * @deprecated
     */
    @Override
    public Enumeration<Servlet> getServlets() {
        throw new UnsupportedOperationException("ServletContext.getServlets() is no longer supported");
    }

    /**
     * Get the session cookie config.
     *
     * @return the session cookie config.
     */
    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return httpSessionManager.getSessionCookieConfig();
    }

    /**
     * Get the default session timeout.
     *
     * @return the default session timeout.
     */
    @Override
    public int getSessionTimeout() {
        return httpSessionManager.getSessionTimeout();
    }

    /**
     * Get the session manager.
     *
     * @return the session manager.
     */
    @Override
    public HttpSessionManager getHttpSessionManager() {
        return httpSessionManager;
    }

    /**
     * Get the virtual server name.
     *
     * @return the virtual server name.
     */
    @Override
    public String getVirtualServerName() {
        return virtualServerName;
    }

    /**
     * Initialize the web application.
     */
    @Override
    public void initialize() {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Initializing web application at {0}", contextPath);
        }
        verifyState(SETUP, "Unable to initialize web application");

        try {
            initializeFeatures();
            initializeInitializers();

            contextListeners.stream().forEach((listener) -> {
                listener.contextInitialized(new ServletContextEvent(this));
            });

            initializeFilters();
            initializeServlets();

            status = INITIALIZED;

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Initialized web application at {0}", contextPath);
            }
        } catch (ServletException se) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "An error occured initializing webapplication at " + contextPath, se);
            }
            status = ERROR;
        }
    }

    /**
     * Initialize the features.
     */
    protected void initializeFeatures() {
        Iterator<Feature> iterator = features.iterator();
        while (iterator.hasNext()) {
            Feature feature = iterator.next();
            feature.initialize(this);
        }
    }

    /**
     * Initialize the filters.
     */
    protected void initializeFilters() {
        List<String> filterNames = new ArrayList<>(filters.keySet());
        filterNames.stream().map((filterName) -> filters.get(filterName))
                .forEach((environment) -> {
                    try {
                        environment.initialize();
                        environment.getFilter().init(environment);
                    } catch (ServletException exception) {
                        environment.setStatus(DefaultFilterEnvironment.UNAVAILABLE);
                    }
                });
    }

    /**
     * Initialize the servlet container initializers.
     *
     * @throws ServletException when an error occurs.
     */
    protected void initializeInitializers() throws ServletException {
        for (ServletContainerInitializer initializer : initializers) {
            initializer.onStartup(annotationManager.getAnnotatedClasses(), this);
        }
    }

    /**
     * Initialize the servlets.
     */
    protected void initializeServlets() {
        List<String> servletNames = new ArrayList<>(servlets.keySet());
        servletNames.stream().map((servletName) -> servlets.get(servletName))
                .forEach((environment) -> {
                    try {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(Level.INFO, "Initializing servlet: {0}", environment.servletName);
                        }
                        if (environment.getServlet() == null) {
                            Class clazz = environment.getServletClass();
                            if (clazz == null) {
                                ClassLoader loader = getClassLoader();
                                if (loader == null) {
                                    loader = getClass().getClassLoader();
                                }
                                if (loader == null) {
                                    loader = ClassLoader.getSystemClassLoader();
                                }
                                clazz = loader.loadClass(environment.getClassName());
                            }
                            environment.setServlet(createServlet(clazz));
                        }
                        environment.getServlet().init(environment);
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(Level.INFO, "Initialized servlet: {0}", environment.servletName);
                        }
                    } catch (ServletException | ClassNotFoundException exception) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, "Unable to initialize servlet: " + environment.className, exception);
                        }
                        environment.setStatus(DefaultServletEnvironment.UNAVAILABLE);
                    }
                });
    }

    /**
     * Link the request and response.
     *
     * @param request the request.
     * @param response the response.
     */
    @Override
    public void linkRequestAndResponse(ServletRequest request, ServletResponse response) {
        requests.put(request, response);
        responses.put(response, request);
    }

    /**
     * Log a message.
     *
     * @param exception the exception.
     * @param message the message.
     * @deprecated
     */
    @Override
    public void log(Exception exception, String message) {
        throw new UnsupportedOperationException("ServletContext.log(Exception, String) is no longer supported");
    }

    /**
     * Log a message.
     *
     * @param message the message.
     * @param throwable the throwable.
     */
    @Override
    public void log(String message, Throwable throwable) {
        loggingManager.log(message, throwable);
    }

    /**
     * Log a message.
     *
     * @param message the message.
     */
    @Override
    public void log(String message) {
        log(message, null);
    }

    /**
     * Remove the attribute with the given name.
     *
     * @param name the name.
     */
    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
        attributeRemoved(name);
    }

    /**
     * Service the request using this web application.
     *
     * @param request the servlet request.
     * @param response the servlet response.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a servlet error occurs.
     */
    @Override
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
        verifyState(SERVICING, "Unable to service request");
        linkRequestAndResponse(request, response);
        if (!requestListeners.isEmpty()) {
            requestListeners.stream().forEach((servletRequestListener) -> {
                servletRequestListener.requestInitialized(new ServletRequestEvent(this, request));
            });
        }
        if (request instanceof DefaultWebApplicationRequest
                && response instanceof DefaultWebApplicationResponse) {
            DefaultWebApplicationRequest httpRequest = (DefaultWebApplicationRequest) request;
            DefaultWebApplicationResponse httpResponse = (DefaultWebApplicationResponse) response;
            List<DefaultFilterEnvironment> filterEnvironments = findFilterEnvironments(httpRequest);
            if (filterEnvironments == null) {
                String path = httpRequest.getServletPath() + (httpRequest.getPathInfo() == null ? "" : httpRequest.getPathInfo());
                WebApplicationRequestMapping mapping = webApplicationRequestMapper.findServletMapping(path);
                if (mapping != null) {
                    String servletName = webApplicationRequestMapper.getServletName(mapping.getPath());
                    if (servletName != null && servlets.containsKey(servletName)) {
                        Servlet servlet = servlets.get(servletName).getServlet();
                        if (mapping.isExact()) {
                            httpRequest.setServletPath(path);
                            httpRequest.setPathInfo(null);
                        } else if (!mapping.isExtension()) {
                            httpRequest.setServletPath(mapping.getPath().substring(0, mapping.getPath().length() - 2));
                            httpRequest.setPathInfo(path.substring(mapping.getPath().length() - 2));
                        }
                        if (servlet != null) {
                            servlet.service(request, response);
                        } else {
                            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                String path = httpRequest.getServletPath() + (httpRequest.getPathInfo() == null ? "" : httpRequest.getPathInfo());
                WebApplicationRequestMapping mapping = webApplicationRequestMapper.findServletMapping(path);
                Servlet servlet = null;
                if (mapping != null) {
                    String servletName = webApplicationRequestMapper.getServletName(mapping.getPath());
                    if (servletName != null && servlets.containsKey(servletName)) {
                        servlet = servlets.get(servletName).getServlet();
                        if (mapping.isExact()) {
                            httpRequest.setServletPath(path);
                            httpRequest.setPathInfo(null);
                        } else if (!mapping.isExtension()) {
                            httpRequest.setServletPath(mapping.getPath().substring(0, mapping.getPath().length() - 2));
                            httpRequest.setPathInfo(path.substring(mapping.getPath().length() - 2));
                        }
                    }
                }
                if (servlet == null) {
                    servlet = new DefaultServlet();
                }
                List<DefaultFilterEnvironment> currentEnvironments = new ArrayList<>(filterEnvironments);
                Collections.reverse(currentEnvironments);
                DefaultFilterChain downFilterChain = new DefaultFilterChain(servlet);
                DefaultFilterChain upFilterChain;
                for (DefaultFilterEnvironment filterEnvironment : currentEnvironments) {
                    upFilterChain = new DefaultFilterChain(filterEnvironment.getFilter(), downFilterChain);
                    downFilterChain = upFilterChain;
                }
                downFilterChain.doFilter(request, response);
            }
            if (!requestListeners.isEmpty()) {
                requestListeners.stream().forEach((servletRequestListener) -> {
                    servletRequestListener.requestDestroyed(new ServletRequestEvent(this, request));
                });
            }
        } else {
            throw new ServletException("Invalid request or response");
        }
        unlinkRequestAndResponse(request, response);
    }

    /**
     * Set the attribute.
     *
     * @param name the attribute name.
     * @param value the attribute value.
     */
    @Override
    public void setAttribute(String name, Object value) {
        if (value != null) {
            boolean added = true;
            if (attributes.containsKey(name)) {
                added = false;
            }
            attributes.put(name, value);
            if (added) {
                attributeAdded(name, value);
            } else {
                attributeReplaced(name, value);
            }
        } else {
            removeAttribute(name);
        }
    }

    /**
     * Set the class-loader.
     *
     * @param classLoader the class loader.
     */
    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Set the context path.
     *
     * @param contextPath the context path.
     */
    @Override
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * Set the HTTP session manager.
     *
     * @param httpSessionManager the HTTP session manager.
     */
    @Override
    public void setHttpSessionManager(HttpSessionManager httpSessionManager) {
        this.httpSessionManager = httpSessionManager;
    }
    
    @Override
    public HttpRequestManager getHttpRequestManager() {
        return httpRequestManager;
    }

    @Override
    public void setHttpRequestManager(HttpRequestManager httpRequestManager) {
        this.httpRequestManager = httpRequestManager;
    }

    /**
     * Set the init parameter.
     *
     * @param name the name.
     * @param value the value.
     * @return true if it could be set, false otherwise.
     */
    @Override
    public boolean setInitParameter(String name, String value) {
        boolean result = true;
        if (status != SETUP) {
            throw new IllegalStateException("Cannot set init parameter once web application is initialized");
        }
        if (initParameters.containsKey(name)) {
            result = false;
        } else {
            initParameters.put(name, value);
        }
        return result;
    }

    /**
     * Set the JSP manager.
     *
     * @param jspManager the JSP manager.
     */
    @Override
    public void setJspManager(JspManager jspManager) {
        this.jspManager = jspManager;
    }

    /**
     * Set the logging manager.
     *
     * @param loggingManager the logging manager.
     */
    @Override
    public void setLoggingManager(LoggingManager loggingManager) {
        this.loggingManager = loggingManager;
    }

    /**
     * Set the mimeType manager.
     *
     * @param mimeTypeManager the mimeType manager.
     */
    @Override
    public void setMimeTypeManager(MimeTypeManager mimeTypeManager) {
        this.mimeTypeManager = mimeTypeManager;
    }

    /**
     * Set the object instance manager.
     *
     * @param objectInstanceManager the object instance manager.
     */
    @Override
    public void setObjectInstanceManager(ObjectInstanceManager objectInstanceManager) {
        this.objectInstanceManager = objectInstanceManager;
    }

    /**
     * Set the default request character encoding.
     *
     * @param requestCharacterEncoding the default request character encoding.
     */
    @Override
    public void setRequestCharacterEncoding(String requestCharacterEncoding) {
        this.requestCharacterEncoding = requestCharacterEncoding;
    }

    /**
     * Set the resource manager.
     *
     * @param resourceManager the resource manager.
     */
    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * Set the default response character encoding.
     *
     * @param responseCharacterEncoding the default response character encoding.
     */
    @Override
    public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        this.responseCharacterEncoding = responseCharacterEncoding;
    }

    /**
     * Set the security manager.
     *
     * @param securityManager the security manager.
     */
    @Override
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * Set the servlet context name.
     *
     * @param servletContextName the servlet context name.
     */
    @Override
    public void setServletContextName(String servletContextName) {
        this.servletContextName = servletContextName;
    }

    /**
     * Set the session tracking modes.
     *
     * @param sessionTrackingModes the session tracking modes.
     */
    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        httpSessionManager.setSessionTrackingModes(sessionTrackingModes);
    }

    /**
     * Set the default session timeout.
     *
     * @param sessionTimeout the default session timeout.
     */
    @Override
    public void setSessionTimeout(int sessionTimeout) {
        httpSessionManager.setSessionTimeout(sessionTimeout);
    }

    /**
     * Set the virtual server name.
     *
     * @param virtualServerName the virtual server name.
     */
    public void setVirtualServerName(String virtualServerName) {
        this.virtualServerName = virtualServerName;
    }

    /**
     * Set the web application request mapper.
     *
     * @param webApplicationRequestMapper the web application request mapper.
     */
    @Override
    public void setWebApplicationRequestMapper(WebApplicationRequestMapper webApplicationRequestMapper) {
        this.webApplicationRequestMapper = webApplicationRequestMapper;
    }

    /**
     * Start servicing.
     */
    @Override
    public void start() {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Starting web application at {0}", contextPath);
        }
        verifyState(INITIALIZED, "Unable to start servicing");
        status = SERVICING;
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Started web application at {0}", contextPath);
        }
    }

    /**
     * Stop servicing.
     */
    @Override
    public void stop() {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Stopping web application at {0}", contextPath);
        }
        verifyState(SERVICING, "Unable to stop servicing");
        status = INITIALIZED;
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO, "Stopped web application at {0}", contextPath);
        }
    }

    /**
     * Unlink the request and response.
     *
     * @param request the request.
     * @param response the response.
     */
    @Override
    public void unlinkRequestAndResponse(ServletRequest request, ServletResponse response) {
        requests.remove(request);
        responses.remove(response);
    }

    /**
     * Verify the web application state.
     *
     * @param desiredStatus the desired status.
     * @param message the message.
     */
    protected void verifyState(int desiredStatus, String message) {
        if (status != desiredStatus) {
            throw new RuntimeException(message);
        }
    }
    
    /**
     * Attribute added.
     *
     * @param name the name.
     * @param value the value.
     */
    private void attributeAdded(String name, Object value) {
        contextAttributeListeners.stream().forEach((listener) -> {
            listener.attributeAdded(new ServletContextAttributeEvent(this, name, value));
        });
    }

    /**
     * Attributed removed.
     * 
     * @param name the name.
     */
    private void attributeRemoved(String name) {
        contextAttributeListeners.stream().forEach(listener -> {
            listener.attributeRemoved(new ServletContextAttributeEvent(this, name, null));
        });
    }

    /**
     * Attribute removed.
     *
     * @param name the name.
     * @param value the value.
     */
    private void attributeReplaced(String name, Object value) {
        contextAttributeListeners.stream().forEach((listener) -> {
            listener.attributeReplaced(new ServletContextAttributeEvent(this, name, value));
        });
    }
}
