/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.webapp.impl;

import static cloud.piranha.webapp.impl.DefaultFilterEnvironment.UNAVAILABLE;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.WARNING;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
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
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

import cloud.piranha.resource.DefaultResourceManager;
import cloud.piranha.resource.api.Resource;
import cloud.piranha.resource.api.ResourceManager;
import cloud.piranha.webapp.api.AnnotationManager;
import cloud.piranha.webapp.api.AsyncManager;
import cloud.piranha.webapp.api.FilterPriority;
import cloud.piranha.webapp.api.HttpRequestManager;
import cloud.piranha.webapp.api.HttpSessionManager;
import cloud.piranha.webapp.api.JspManager;
import cloud.piranha.webapp.api.LoggingManager;
import cloud.piranha.webapp.api.MimeTypeManager;
import cloud.piranha.webapp.api.MultiPartManager;
import cloud.piranha.webapp.api.ObjectInstanceManager;
import cloud.piranha.webapp.api.SecurityManager;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationRequestMapper;
import cloud.piranha.webapp.api.WebApplicationRequestMapping;
import cloud.piranha.webapp.api.WelcomeFileManager;

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
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultWebApplication.class.getName());

    /**
     * Stores the async manager.
     */
    protected AsyncManager asyncManager;
    
    /**
     * Stores the class loader.
     */
    protected ClassLoader classLoader;

    /**
     * Stores the context path.
     */
    protected String contextPath;

    /**
     * Stores the default servlet (if any).
     */
    protected Servlet defaultServlet;

    /**
     * Stores the boolean flag indicating if the web application is
     * distributable.
     */
    protected boolean distributable;

    /**
     * Stores the servlet context name.
     */
    protected String servletContextName;

    /**
     * Stores the virtual server name.
     */
    protected String virtualServerName = "server";

    /**
     * Stores the response character encoding.
     */
    protected String responseCharacterEncoding;

    /**
     * Stores the status.
     */
    protected int status;

    /**
     * Stores the active requests and the associated response.
     */
    protected final Map<ServletRequest, ServletResponse> requests;

    /**
     * Stores the active responses and the associated requests.
     */
    protected final Map<ServletResponse, ServletRequest> responses;

    /**
     * Stores the servlet container initializers.
     */
    protected final List<ServletContainerInitializer> initializers;

    /**
     * Stores the init parameters.
     */
    protected final Map<String, String> initParameters;

    /**
     * Stores the attributes.
     */
    protected final Map<String, Object> attributes;

    /**
     * Stores the servlets.
     */
    protected final Map<String, DefaultServletEnvironment> servlets;

    /**
     * Stores the filters.
     */
    protected final Map<String, DefaultFilterEnvironment> filters;

    protected final Map<Integer, String> errorPagesByCode = new HashMap<>();

    protected final Map<String, String> errorPagesByException = new HashMap<>();

    // ### Listeners
    /**
     * Stores the servlet context attribute listeners.
     */
    protected final List<ServletContextAttributeListener> contextAttributeListeners;

    /**
     * Stores the servlet context listeners.
     */
    protected final ArrayList<ServletContextListener> contextListeners;

    /**
     * Stores the servlet request listeners.
     */
    protected final List<ServletRequestListener> requestListeners;

    // ### Managers
    /**
     * Stores the object instance manager.
     */
    protected ObjectInstanceManager objectInstanceManager;

    /**
     * Stores the annotation manager.
     */
    protected AnnotationManager annotationManager;

    /**
     * Stores the resource manager.
     */
    protected ResourceManager resourceManager;

    /**
     * Stores the session manager.
     */
    protected HttpSessionManager httpSessionManager;

    /**
     * Stores the security manager.
     */
    protected SecurityManager securityManager;

    /**
     * Stores the JSP manager.
     */
    protected JspManager jspManager;

    /**
     * Stores the logging manager.
     */
    protected LoggingManager loggingManager;

    /**
     * Stores the request manager.
     */
    protected HttpRequestManager httpRequestManager;

    /**
     * Stores the mime type manager.
     */
    protected MimeTypeManager mimeTypeManager;

    /**
     * Stores the multi part manager.
     */
    protected MultiPartManager multiPartManager;

    /**
     * Stores the request character encoding.
     */
    protected String requestCharacterEncoding;

    /**
     * Stores the web application request mapper.
     */
    protected WebApplicationRequestMapper webApplicationRequestMapper;

    /**
     * Stores the welcome file manager.
     */
    protected WelcomeFileManager welcomeFileManager;

    /**
     * Constructor.
     */
    public DefaultWebApplication() {
        annotationManager = new DefaultAnnotationManager();
        asyncManager = new DefaultAsyncManager();
        attributes = new HashMap<>(1);
        classLoader = getClass().getClassLoader();
        contextAttributeListeners = new ArrayList<>(1);
        contextListeners = new ArrayList<>(1);
        contextPath = "";
        filters = new LinkedHashMap<>(1);
        httpSessionManager = new DefaultHttpSessionManager();
        httpRequestManager = new DefaultHttpRequestManager();
        initParameters = new ConcurrentHashMap<>(1);
        initializers = new ArrayList<>(1);
        jspManager = new DefaultJspFileManager();
        loggingManager = new DefaultLoggingManager();
        mimeTypeManager = new DefaultMimeTypeManager();
        multiPartManager = new DefaultMultiPartManager();
        objectInstanceManager = new DefaultObjectInstanceManager();
        requestListeners = new ArrayList<>(1);
        requests = new ConcurrentHashMap<>(1);
        resourceManager = new DefaultResourceManager();
        responses = new ConcurrentHashMap<>(1);
        securityManager = new DefaultSecurityManager();
        servletContextName = UUID.randomUUID().toString();
        servlets = new LinkedHashMap<>();
        webApplicationRequestMapper = new DefaultWebApplicationRequestMapper();
        welcomeFileManager = new DefaultWelcomeFileManager();
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
        if (status == SERVICING) {
            throw new IllegalStateException("Cannot call this after web application has started");
        }

        if (filterName == null || filterName.trim().equals("")) {
            throw new IllegalArgumentException("Filter name cannot be null or empty");
        }

        DefaultFilterEnvironment defaultFilterEnvironment;
        if (filters.containsKey(filterName)) {
            defaultFilterEnvironment = filters.get(filterName);
        } else {
            defaultFilterEnvironment = new DefaultFilterEnvironment();
            defaultFilterEnvironment.setFilterName(filterName);
            defaultFilterEnvironment.setWebApplication(this);
            filters.put(filterName, defaultFilterEnvironment);
        }
        defaultFilterEnvironment.setClassName(className);

        return defaultFilterEnvironment;
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
        if (status == SERVICING) {
            throw new IllegalStateException("Cannot call this after web application has started");
        }

        if (filterName == null || filterName.trim().equals("")) {
            throw new IllegalArgumentException("Filter name cannot be null or empty");
        }

        DefaultFilterEnvironment filterEnvironment;
        if (filters.containsKey(filterName)) {
            filterEnvironment = filters.get(filterName);
        } else {
            filterEnvironment = new DefaultFilterEnvironment();
            filterEnvironment.setFilterName(filterName);
            filterEnvironment.setWebApplication(this);
            filters.put(filterName, filterEnvironment);
        }
        filterEnvironment.setClassName(filterClass.getCanonicalName());

        return filterEnvironment;
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
        if (status == SERVICING) {
            throw new IllegalStateException("Cannot call this after web application has started");
        }

        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment(this, filterName, filter);
        filters.put(filterName, filterEnvironment);

        return filterEnvironment;

    }

    @Override
    public Set<String> addFilterMapping(String filterName, String... urlPatterns) {
        return webApplicationRequestMapper.addFilterMapping(filterName, urlPatterns);
    }

    @Override
    public Set<String> addFilterMapping(String filterName, boolean isMatchAfter, String... urlPatterns) {
        if (isMatchAfter) {
            return webApplicationRequestMapper.addFilterMapping(filterName, urlPatterns);
        } else {
            return webApplicationRequestMapper.addFilterMappingBeforeExisting(filterName, urlPatterns);
        }
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
        } catch (Throwable throwable) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to add initializer: " + className, throwable);
            }
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
        if (status != SETUP) {
            throw new IllegalStateException("Illegal to add listener because state is not SETUP");
        }
        try {
            @SuppressWarnings("unchecked")
            Class<EventListener> clazz = (Class<EventListener>) getClassLoader().loadClass(className);
            addListener(clazz);
        } catch (ClassNotFoundException exception) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to add listener: " + className, exception);
            }
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
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to add listener: " + type, exception);
            }
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

    @Override
    public void addErrorPage(int code, String location) {
        errorPagesByCode.put(code, location);
    }

    @Override
    public void addErrorPage(String exception, String location) {
        errorPagesByException.put(exception, location);
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
        if (result instanceof ServletContextListener || result instanceof ServletContextAttributeListener || result instanceof ServletRequestListener
                || result instanceof ServletRequestAttributeListener || result instanceof HttpSessionAttributeListener
                || result instanceof HttpSessionIdListener || result instanceof HttpSessionListener) {
            ok = true;
        }

        if (!ok) {
            LOGGER.log(WARNING, "Unable to create listener: {0}", clazz);
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
     */
    @Override
    public void destroy() {
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
     * Are we denying uncovered HTTP methods.
     *
     * @return true if we are, false otherwise.
     */
    @Override
    public boolean getDenyUncoveredHttpMethods() {
        return securityManager.getDenyUncoveredHttpMethods();
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
     * @see WebApplication#getDefaultSessionTrackingModes()
     */
    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return httpSessionManager.getDefaultSessionTrackingModes();
    }

    /**
     * @see WebApplication#getDefaultServlet()
     */
    @Override
    public Servlet getDefaultServlet() {
        return defaultServlet;
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
     * Get the multi part manager.
     *
     * @return the multi part manager.
     */
    @Override
    public MultiPartManager getMultiPartManager() {
        return multiPartManager;
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
        String realPath = null;
        try {

            URL resourceUrl = getResource(path);

            if (resourceUrl != null && "file".equals(resourceUrl.getProtocol())) {
                File file = new File(resourceUrl.toURI());
                if (file.exists()) {
                    realPath = file.toString();
                }
            }
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException exception) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to get real path: " + path, exception);
            }
        }
        return realPath;
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
        RequestDispatcher requestDispatcher = null;
        WebApplicationRequestMapping mapping = webApplicationRequestMapper.findServletMapping(path);
        if (mapping != null) {
            String servletName = webApplicationRequestMapper.getServletName(mapping.getPath());
            if (servletName != null) {
                requestDispatcher = getNamedDispatcher(servletName, path);
            }
        }

        return requestDispatcher;
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
     * Returns the file path or the first nested folder
     *
     * @apiNote
     *  <p><b>Examples.</b>
     * <pre>{@code
     *  getFileOrFirstFolder("/rootFolder", "/rootFolder/file.html").equals("/rootFolder/file.html")
     * }</pre>
     *
     * <pre>{@code
     *  getFileOrFirstFolder("/rootFolder", "/rootFolder/nestedFolder/file.html").equals("/rootFolder/nestedFolder/")
     * }</pre>
     *
     * <pre>{@code
     *  getFileOrFirstFolder("/rootFolder/nestedFolder", "/rootFolder/nestedFolder/file.html")
     *      .equals("/rootFolder/nestedFolder/file.html")
     * }</pre>
     *
     * @param path the path of root folder
     * @param resource the resource that is a file directory or file
     * @return the file path or the first nested folder
     */
    private String getFileOrFirstFolder(String path, String resource){
        String normalizedPath = path.endsWith("/") ? path : path + "/";
        String[] split = resource.replace(normalizedPath, "/").split("/");
        // It's a directory
        if (split.length > 2)
            return normalizedPath + split[1] + "/";
        // It's a file
        return normalizedPath + split[1];
    }

    /**
     * Returns a directory-like listing of all the paths to resources
     * within the web application whose longest sub-path matches the supplied path argument.
     * @param path the partial path used to match the resources
     * @return a Set containing the directory listing, or null if there are no resources in the web application
     * whose path begins with the supplied path.
     */
    private Set<String> getResourcePathsImpl(String path) {
        Set<String> collect = resourceManager.getAllLocations()
                .filter(resource -> resource.startsWith(path))
                .map(resource -> getFileOrFirstFolder(path, resource))
                .collect(Collectors.toSet());
        if (collect.isEmpty())
            return null;
        return collect;
    }
    /**
     * Get the resource paths.
     *
     * @param path the path.
     * @return the resource paths.
     */
    @Override
    public Set<String> getResourcePaths(String path) {
        if (path == null)
            return null;
        if (!path.startsWith("/"))
            throw new IllegalArgumentException("Path must start with /");
        return getResourcePathsImpl(path);
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
     * Get the welcome file manager.
     *
     * @return the welcome file manager.
     */
    @Override
    public WelcomeFileManager getWelcomeFileManager() {
        return welcomeFileManager;
    }

    /**
     * Initialize the web application.
     */
    @Override
    public void initialize() {
        LOGGER.log(FINE, "Initializing web application at {0}", contextPath);
        verifyState(SETUP, "Unable to initialize web application");
        initializeInitializers();
        initializeFilters();
        initializeServlets();
        initializeFinish();
    }

    /**
     * Finish the initialization.
     */
    @Override
    public void initializeFinish() {
        if (status == SETUP) {
            status = INITIALIZED;
            LOGGER.log(FINE, "Initialized web application at {0}", contextPath);
        }
        if (status == ERROR && LOGGER.isLoggable(WARNING)) {
            LOGGER.log(WARNING, "An error occurred initializing webapplication at {0}", contextPath);
        }
    }

    /**
     * Initialize the filters.
     */
    @Override
    public void initializeFilters() {
        if (status == SETUP) {
            List<String> filterNames = new ArrayList<>(filters.keySet());
            filterNames.stream().map((filterName) -> filters.get(filterName)).forEach((environment) -> {
                try {
                    environment.initialize();
                    environment.getFilter().init(environment);
                } catch (Throwable t) {
                    if (LOGGER.isLoggable(WARNING)) {
                        LOGGER.log(WARNING, "Unable to initialize filter: " + environment.getFilterName(), t);
                    }
                    environment.setStatus(UNAVAILABLE);
                }
            });
        }
    }

    /**
     * Initialize the servlet container initializers.
     */
    @Override
    public void initializeInitializers() {
        boolean error = false;
        for (ServletContainerInitializer initializer : initializers) {
            try {
                initializer.onStartup(annotationManager.getAnnotatedClasses(), this);
            } catch (Throwable t) {
                if (LOGGER.isLoggable(WARNING)) {
                    LOGGER.log(Level.WARNING, "Initializer " + initializer.getClass().getName() + " failing onStartup", t);
                }
                error = true;
            }
        }
        if (!error) {
            @SuppressWarnings("unchecked")
            List<ServletContextListener> listeners = (List<ServletContextListener>) contextListeners.clone();
            listeners.stream().forEach((listener) -> {
                listener.contextInitialized(new ServletContextEvent(this));
            });
        } else {
            status = ERROR;
        }
    }

    /**
     * Initialize the servlets.
     */
    @Override
    public void initializeServlets() {
        if (status == SETUP) {
            List<String> servletNames = new ArrayList<>(servlets.keySet());
            servletNames.stream().map((servletName) -> servlets.get(servletName)).forEach((environment) -> {
                initializeServlet(environment);
            });
        }
    }

    /**
     * Is the web application distributable.
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isDistributable() {
        return distributable;
    }

    /**
     * Initialize the servlet.
     *
     * @param environment the default servlet environment.
     */
    private void initializeServlet(DefaultServletEnvironment environment) {
        try {
            LOGGER.log(FINE, "Initializing servlet: {0}", environment.servletName);
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
            LOGGER.log(FINE, "Initialized servlet: {0}", environment.servletName);
        } catch (Throwable t) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "Unable to initialize servlet: " + environment.className, t);
            }
            environment.setServlet(null);
            environment.setStatus(DefaultServletEnvironment.UNAVAILABLE);
        }
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
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        verifyState(SERVICING, "Unable to service request");
        verifyRequestResponseTypes(request, response);

        linkRequestAndResponse(request, response);
        requestInitialized(request);

        DefaultWebApplicationRequest webappRequest = (DefaultWebApplicationRequest) request;
        DefaultWebApplicationResponse httpResponse = (DefaultWebApplicationResponse) response;

        // Obtain a reference to the target resource (target Servlet)
        Servlet servlet = getTargetServlet(webappRequest);

        /*
         * We did not find a Servlet, so we are now going to see if we can map any
         * of the welcome-file entries to a file available using
         * ServletContext.getResource, if so we will change the original request
         * by adding it to the end of the request and then we let the default
         * Servlet handle it.
         */
        boolean matchedResource = false;
        if (servlet == null) {
            String originalPathInfo = webappRequest.getPathInfo() != null ? webappRequest.getPathInfo() : "";
            for (String welcomeFile : getWelcomeFileManager().getWelcomeFileList()) {
                webappRequest.setPathInfo(originalPathInfo + welcomeFile);
                if (getResource(webappRequest.getRequestURI()) != null) {
                    matchedResource = true;
                    break;
                }
            }
            if (!matchedResource) {
                webappRequest.setPathInfo(originalPathInfo);
            }
        }

        /*
         * We did not find a Servlet, so we are now going to see if we can map any of the welcome-file entries to a Servlet. If
         * we can we will use the first match we find.
         */
        if (servlet == null && !matchedResource) {
            String originalPathInfo = webappRequest.getPathInfo() != null ? webappRequest.getPathInfo() : "";
            for (String welcomeFile : getWelcomeFileManager().getWelcomeFileList()) {
                webappRequest.setPathInfo(originalPathInfo + welcomeFile);
                servlet = getTargetServlet(webappRequest);
                if (servlet != null) {
                    break;
                }
            }
            if (servlet == null) {
                webappRequest.setPathInfo(originalPathInfo);
            }
        }

        /**
         * We did not find a Servlet, so we are now going to check if a default
         * Servlet is set and if set we are going to use it.
         */
        if (servlet == null && defaultServlet != null) {
            servlet = defaultServlet;
        }

        // Invoke the Servlet, or first the Filter chain and then the Servlet
        List<DefaultFilterEnvironment> filterEnvironments = findFilterEnvironments(webappRequest);

        Exception exception = null;
        if (servlet == null && filterEnvironments == null) {
            httpResponse.sendError(404);
        } else {
            try {
                if (filterEnvironments != null) {
                    getFilterChain(filterEnvironments, servlet).doFilter(request, response);
                } else {
                    servlet.service(request, response);
                }
            } catch (Exception e) {
                exception = e;
            }
        }

        String location = null;
        if (exception != null) {
            location = errorPagesByException.get(exception.getClass().getName());
        } else if (httpResponse.getStatus() >= 400 && httpResponse.getStatus() <= 500) {
            location = errorPagesByCode.get(httpResponse.getStatus());
        }

        if (location != null) {
            request.getRequestDispatcher(location)
                    .forward(webappRequest, httpResponse);
        } else if (exception != null) {
            rethrow(exception);
        }

        if (!httpResponse.isCommitted() && !webappRequest.isAsyncStarted()) {
            httpResponse.flushBuffer();
        }

        requestDestroyed(request);
        unlinkRequestAndResponse(request, response);
    }

    private void rethrow(Exception exception) throws ServletException, IOException {
        if (exception instanceof ServletException) {
            throw (ServletException) exception;
        }

        if (exception instanceof IOException) {
            throw (IOException) exception;
        }

        if (exception instanceof RuntimeException) {
            throw (RuntimeException) exception;
        }

        throw new IllegalStateException(exception);
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
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "Setting context path to: {0}", contextPath);
        }
        this.contextPath = contextPath;
    }

    /**
     * @see WebApplication#setDefaultServlet(javax.servlet.Servlet) 
     */
    @Override
    public void setDefaultServlet(Servlet defaultServlet) {
        this.defaultServlet = defaultServlet;
    }

    /**
     * Set if we are denying uncovered HTTP methods.
     *
     * @param denyUncoveredHttpMethods the boolean value.
     */
    @Override
    public void setDenyUncoveredHttpMethods(boolean denyUncoveredHttpMethods) {
        securityManager.setDenyUncoveredHttpMethods(denyUncoveredHttpMethods);
    }

    /**
     * Set if the web application is distributable.
     *
     * @param distributable the boolean value.
     */
    @Override
    public void setDistributable(boolean distributable) {
        this.distributable = distributable;
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
     * Set the multi part manager.
     *
     * @param multiPartManager the multi part manager.
     */
    @Override
    public void setMultiPartManager(MultiPartManager multiPartManager) {
        this.multiPartManager = multiPartManager;
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
     * Set the welcome file manager.
     *
     * @param welcomeFileManager the welcome file manager.
     */
    @Override
    public void setWelcomeFileManager(WelcomeFileManager welcomeFileManager) {
        this.welcomeFileManager = welcomeFileManager;
    }

    /**
     * Start servicing.
     */
    @Override
    public void start() {
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "Starting web application at {0}", contextPath);
        }
        verifyState(INITIALIZED, "Unable to start servicing");
        status = SERVICING;
        if (LOGGER.isLoggable(FINE)) {
            LOGGER.log(FINE, "Started web application at {0}", contextPath);
        }
    }

    /**
     * Stop servicing.
     */
    @Override
    public void stop() {
        LOGGER.log(FINE, "Stopping web application at {0}", contextPath);
        verifyState(SERVICING, "Unable to stop servicing");
        status = INITIALIZED;
        LOGGER.log(FINE, "Stopped web application at {0}", contextPath);
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

    // ### Private methods
    private void verifyRequestResponseTypes(ServletRequest request, ServletResponse response) throws ServletException {
        if (!(request instanceof DefaultWebApplicationRequest && response instanceof DefaultWebApplicationResponse)) {
            throw new ServletException("Invalid request or response");
        }
    }

    private Servlet getTargetServlet(DefaultWebApplicationRequest httpRequest) {
        String path = httpRequest.getServletPath() + (httpRequest.getPathInfo() == null ? "" : httpRequest.getPathInfo());
        WebApplicationRequestMapping mapping = webApplicationRequestMapper.findServletMapping(path);
        if (mapping == null) {
            return null;
        }

        String servletName = webApplicationRequestMapper.getServletName(mapping.getPath());
        if (servletName == null || !servlets.containsKey(servletName)) {
            return null;
        }

        Servlet targetServlet = servlets.get(servletName).getServlet();
        httpRequest.asyncSupported = servlets.get(servletName).asyncSupported;

        if (mapping.isExact()) {
            httpRequest.setServletPath(path);
            httpRequest.setPathInfo(null);
        } else if (!mapping.isExtension()) {
            httpRequest.setServletPath(mapping.getPath().substring(0, mapping.getPath().length() - 2));
            httpRequest.setPathInfo(path.substring(mapping.getPath().length() - 2));
        }

        return targetServlet;
    }

    private FilterChain getFilterChain(List<DefaultFilterEnvironment> filterEnvironments, Servlet servlet) {

        List<DefaultFilterEnvironment> prioritisedFilters = filterEnvironments.stream()
                .filter(e -> e.getFilter() instanceof FilterPriority)
                .sorted((x, y) -> sortOnPriority(x, y))
                .collect(toList());

        List<DefaultFilterEnvironment> notPrioritisedFilters = filterEnvironments.stream()
                .filter(e -> e.getFilter() instanceof FilterPriority == false)
                .collect(toList());

        List<DefaultFilterEnvironment> currentEnvironments = new ArrayList<>();
        currentEnvironments.addAll(prioritisedFilters);
        currentEnvironments.addAll(notPrioritisedFilters);

        Collections.reverse(currentEnvironments);

        DefaultFilterChain downFilterChain = new DefaultFilterChain(servlet);
        DefaultFilterChain upFilterChain;
        for (DefaultFilterEnvironment filterEnvironment : currentEnvironments) {
            upFilterChain = new DefaultFilterChain(filterEnvironment.getFilter(), downFilterChain);
            downFilterChain = upFilterChain;
        }

        return downFilterChain;
    }

    private int sortOnPriority(DefaultFilterEnvironment x, DefaultFilterEnvironment y) {
        FilterPriority filterX = (FilterPriority) x.getFilter();
        FilterPriority filterY = (FilterPriority) y.getFilter();

        return Integer.compare(filterX.getPriority(), filterY.getPriority());
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
     * Fire the request initialized event
     *
     * @param request the request
     */
    private void requestInitialized(ServletRequest request) {
        if (!requestListeners.isEmpty()) {
            requestListeners.stream().forEach((servletRequestListener) -> {
                servletRequestListener.requestInitialized(new ServletRequestEvent(this, request));
            });
        }
    }

    /**
     * Fire the request destroyed event
     *
     * @param request the request
     */
    private void requestDestroyed(ServletRequest request) {
        if (!requestListeners.isEmpty()) {
            requestListeners.stream().forEach(servletRequestListener -> {
                servletRequestListener.requestDestroyed(new ServletRequestEvent(this, request));
            });
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

    /**
     * Get the async manager.
     * 
     * @return the async manager.
     */
    @Override
    public AsyncManager getAsyncManager() {
        return asyncManager;
    }
}
