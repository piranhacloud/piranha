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
package cloud.piranha.core.impl;

import cloud.piranha.resource.api.Resource;
import cloud.piranha.core.api.AnnotationInfo;
import cloud.piranha.core.api.ServletEnvironment;
import static cloud.piranha.core.api.ServletEnvironment.UNAVAILABLE;
import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationRequestMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextAttributeEvent;
import jakarta.servlet.ServletContextAttributeListener;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRegistration.Dynamic;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletRequestAttributeListener;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.SessionTrackingMode;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.annotation.HandlesTypes;
import jakarta.servlet.descriptor.JspConfigDescriptor;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionIdListener;
import jakarta.servlet.http.HttpSessionListener;
import jakarta.servlet.http.WebConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.WARNING;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import static java.util.Collections.enumeration;
import static java.util.Collections.reverse;
import static java.util.Collections.unmodifiableMap;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.function.Predicate.isEqual;
import static java.util.function.Predicate.not;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import java.util.stream.Stream;
import cloud.piranha.core.api.WebApplicationManager;

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
     * Stores the SETUP constant.
     */
    protected static final int SETUP = 0;

    /**
     * Stores the INITIALIZED_DECLARED constant. This signals that web.xml,
     * web-fragment.xml and annotations have been processed.
     */
    protected static final int INITIALIZED_DECLARED = 4;

    /**
     * Stores the INITIALIZED constant.
     */
    protected static final int INITIALIZED = 1;

    /**
     * Stores the SERVICING constant.
     */
    protected static final int SERVICING = 2;

    /**
     * Stores the ERROR constant.
     */
    protected static final int ERROR = 3;

    /**
     * Stores the 'Illegal to add listener because state is not SETUP' message.
     */
    private static final String ILLEGAL_TO_ADD_LISTENER = "Illegal to add listener because state is not SETUP";

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(DefaultWebApplication.class.getName());

    /**
     * Stores the piranha.response constant
     */
    private static final String PIRANHA_RESPONSE = "piranha.response";
        
    /**
     * Stores the attributes.
     */
    protected final Map<String, Object> attributes;

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
     * Stores the effective major version.
     */
    protected int effectiveMajorVersion = -1;

    /**
     * Stores the effective minor version.
     */
    protected int effectiveMinorVersion = -1;

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
     * Stores the servlet environments
     */
    protected final Map<String, DefaultServletEnvironment> servletEnvironments;

    /**
     * Stores the filters.
     */
    protected final Map<String, DefaultFilterEnvironment> filters;

    // ### Listeners
    /**
     * Stores the servlet context attribute listeners.
     */
    protected final List<ServletContextAttributeListener> contextAttributeListeners;

    /**
     * Stores the servlet context listeners that were declared in web.xml,
     * web-fragment.xml, or via annotations
     */
    protected final List<ServletContextListener> declaredContextListeners;

    /**
     * Stores the servlet context listeners that were not declared in web.xml,
     * web-fragment.xml, or via annotations
     */
    protected final List<ServletContextListener> contextListeners;

    /**
     * Stores the servlet request listeners.
     */
    protected final List<ServletRequestListener> requestListeners;

    /**
     * Stores the error page manager
     */
    protected DefaultErrorPageManager errorPageManager;

    /**
     * Stores the invocation finder, which finds a Servlet, Filter(chain) and
     * variants thereof to invoke for a given request path.
     */
    protected DefaultInvocationFinder invocationFinder;
    
    /**
     * Stores the metadata complete flag.
     */
    protected boolean metadataComplete;
    
    /**
     * Stores the request character encoding.
     */
    protected String requestCharacterEncoding;

    /**
     * The source object where this web application instance originates from,
     * i.e. the artifact this was last passed into by the container. Compare to
     * the source object of an event.
     */
    protected Object source;

    /**
     * When we're in tainted mode, we have to throw exceptions for a large
     * number of methods.
     *
     * Tainted mode is required for ServletContextListeners which have not been
     * declared. At the moment of writing it's not clear why this tainted mode
     * is needed.
     */
    protected boolean tainted;

    /**
     * Stores the web application request mapper.
     */
    protected WebApplicationRequestMapper webApplicationRequestMapper;
    
    /**
     * Stores the web application manager.
     */
    protected WebApplicationManager manager;

    /**
     * Constructor.
     */
    public DefaultWebApplication() {
        attributes = new HashMap<>(1);
        classLoader = getClass().getClassLoader();
        contextAttributeListeners = new ArrayList<>(1);
        declaredContextListeners = new ArrayList<>(1);
        contextListeners = new ArrayList<>(1);
        contextPath = "";
        filters = new LinkedHashMap<>(1);
        requestListeners = new ArrayList<>(1);
        initParameters = new ConcurrentHashMap<>(1);
        initializers = new ArrayList<>(1);
        responses = new ConcurrentHashMap<>(1);
        errorPageManager = new DefaultErrorPageManager();
        invocationFinder = new DefaultInvocationFinder(this);
        servletContextName = UUID.randomUUID().toString();
        servletEnvironments = new LinkedHashMap<>();
        webApplicationRequestMapper = new DefaultWebApplicationRequestMapper();
        manager = new DefaultWebApplicationManager();
        manager.getHttpSessionManager().setWebApplication(this);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        return addFilter(filterName, filterClass.getCanonicalName());
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        checkTainted();
        checkServicing();

        if (filterName == null || filterName.trim().equals("")) {
            throw new IllegalArgumentException("Filter name cannot be null or empty");
        }

        DefaultFilterEnvironment defaultFilterEnvironment;
        if (filters.containsKey(filterName)) {
            defaultFilterEnvironment = filters.get(filterName);
            if (defaultFilterEnvironment.getClassName() != null) {
                // Filter already set, can't override
                return null;
            }
        } else {
            defaultFilterEnvironment = new DefaultFilterEnvironment();
            defaultFilterEnvironment.setFilterName(filterName);
            defaultFilterEnvironment.setWebApplication(this);
            filters.put(filterName, defaultFilterEnvironment);
        }
        defaultFilterEnvironment.setClassName(className);

        return defaultFilterEnvironment;
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        checkTainted();
        checkServicing();

        if (filters.containsKey(filterName)) {
            DefaultFilterEnvironment filterEnvironment = filters.get(filterName);
            if (filterEnvironment.getClassName() != null) {
                // Filter already set, can't override
                return null;
            }
        }

        DefaultFilterEnvironment filterEnvironment = new DefaultFilterEnvironment(this, filterName, filter);
        filters.put(filterName, filterEnvironment);

        return filterEnvironment;

    }

    @Override
    public Set<String> addFilterMapping(Set<DispatcherType> dispatcherTypes, String filterName, boolean isMatchAfter, String... urlPatterns) {
        if (isMatchAfter) {
            return webApplicationRequestMapper.addFilterMapping(dispatcherTypes, filterName, urlPatterns);
        }

        return webApplicationRequestMapper.addFilterMappingBeforeExisting(dispatcherTypes, filterName, urlPatterns);
    }

    @Override
    public void addInitializer(String className) {
        LOGGER.log(DEBUG, "Adding ServletContainerInitializer: " + className);
        try {
            @SuppressWarnings("unchecked")
            Class<ServletContainerInitializer> clazz = (Class<ServletContainerInitializer>) getClassLoader().loadClass(className);
            initializers.add(clazz.getDeclaredConstructor().newInstance());
        } catch (Throwable throwable) {
            LOGGER.log(WARNING, () -> "Unable to add initializer: " + className, throwable);
        }
    }

    @Override
    public void addInitializer(ServletContainerInitializer servletContainerInitializer) {
        initializers.add(servletContainerInitializer);
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
        if (status != SETUP && status != INITIALIZED_DECLARED) {
            throw new IllegalStateException("Illegal to add JSP file because state is not SETUP");
        }
        if (isEmpty(servletName)) {
            throw new IllegalArgumentException("Servlet name cannot be null or empty");
        }
        return manager.getJspManager() != null 
                ? manager.getJspManager().addJspFile(this, servletName, jspFile)
                : null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addListener(String className) {
        checkTainted();

        if (status != SETUP && status != INITIALIZED_DECLARED) {
            throw new IllegalStateException(ILLEGAL_TO_ADD_LISTENER);
        }

        try {
            addListener((Class<EventListener>) getClassLoader().loadClass(className));
        } catch (ClassNotFoundException exception) {
            LOGGER.log(WARNING, () -> "Unable to add listener: " + className, exception);
        }
    }

    @Override
    public void addListener(Class<? extends EventListener> type) {
        checkTainted();

        if (status != SETUP && status != INITIALIZED_DECLARED) {
            throw new IllegalStateException(ILLEGAL_TO_ADD_LISTENER);
        }

        try {
            addListener(createListener(type));
        } catch (ServletException exception) {
            LOGGER.log(WARNING, () -> "Unable to add listener: " + type, exception);
        }
    }

    @Override
    public <T extends EventListener> void addListener(T listener) {
        checkTainted();

        if (status != SETUP && status != INITIALIZED_DECLARED) {
            throw new IllegalStateException(ILLEGAL_TO_ADD_LISTENER);
        }

        if (listener instanceof ServletContextListener servletContextListener) {
            if (source != null && !(source instanceof ServletContainerInitializer)) {
                throw new IllegalArgumentException("Illegal to add ServletContextListener because this context was not passed to a ServletContainerInitializer");
            }

            if (status == INITIALIZED_DECLARED) {
                contextListeners.add(servletContextListener);
            } else {
                declaredContextListeners.add(servletContextListener);
            }
        }

        if (listener instanceof ServletContextAttributeListener servletContextAttributeListener) {
            contextAttributeListeners.add(servletContextAttributeListener);
        }
        if (listener instanceof ServletRequestListener servletRequestListener) {
            requestListeners.add(servletRequestListener);
        }
        if (listener instanceof ServletRequestAttributeListener servletRequestAttributeListener) {
            getManager().getHttpRequestManager().addListener(servletRequestAttributeListener);
        }
        if (listener instanceof HttpSessionAttributeListener) {
            getManager().getHttpSessionManager().addListener(listener);
        }
        if (listener instanceof HttpSessionIdListener) {
            getManager().getHttpSessionManager().addListener(listener);
        }
        if (listener instanceof HttpSessionListener) {
            getManager().getHttpSessionManager().addListener(listener);
        }
    }

    @Override
    public void addResource(Resource resource) {
        getManager().getResourceManager().addResource(resource);
    }

    @Override
    public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return addServlet(servletName, servletClass.getName());
    }

    @Override
    public Dynamic addServlet(String servletName, String className) {
        checkTainted();
        checkServicing();

        DefaultServletEnvironment servletEnvironment = servletEnvironments.get(servletName);
        if (servletEnvironment == null) {
            servletEnvironment = new DefaultServletEnvironment(this, servletName);
            servletEnvironment.setClassName(className);
            servletEnvironments.put(servletName, servletEnvironment);
        } else {
            if (!isEmpty(servletEnvironment.getClassName())) {
                // Servlet already set, can't override
                return null;
            }
            servletEnvironment.setClassName(className);
        }

        return servletEnvironment;
    }

    @Override
    public Dynamic addServlet(String servletName, Servlet servlet) {
        checkTainted();
        checkServicing();

        if (servletEnvironments.containsKey(servletName)) {
            DefaultServletEnvironment servletEnvironment = servletEnvironments.get(servletName);
            if (!isEmpty(servletEnvironment.getClassName())) {
                // Servlet already set, can't override
                return null;
            }
        }

        DefaultServletEnvironment servletEnvironment = new DefaultServletEnvironment(this, servletName, servlet);
        servletEnvironments.put(servletName, servletEnvironment);

        return servletEnvironment;
    }

    @Override
    public Set<String> addServletMapping(String servletName, String... urlPatterns) {
        return webApplicationRequestMapper.addServletMapping(servletName, urlPatterns);
    }

    @Override
    public String removeServletMapping(String urlPattern) {
        return webApplicationRequestMapper.removeServletMapping(urlPattern);
    }

    @Override
    public void addErrorPage(int code, String location) {
        errorPageManager.getErrorPagesByCode().put(code, location);
    }

    @Override
    public void addErrorPage(String exception, String location) {
        errorPageManager.getErrorPagesByException().put(exception, location);
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> filterClass) throws ServletException {
        checkTainted();
        return manager.getObjectInstanceManager().createFilter(filterClass);
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        checkTainted();

        T result = manager.getObjectInstanceManager().createListener(clazz);
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

    @Override
    public <T extends Servlet> T createServlet(Class<T> servletClass) throws ServletException {
        checkTainted();
        return manager.getObjectInstanceManager().createServlet(servletClass);
    }

    @Override
    public void declareRoles(String... roles) {
        if (manager.getSecurityManager() != null) {
            manager.getSecurityManager().declareRoles(roles);
        }
    }

    @Override
    public void destroy() {
        verifyState(INITIALIZED, "Unable to destroy web application");

        servletEnvironments.values().stream().forEach(servletEnv -> servletEnv.getServlet().destroy());
        servletEnvironments.clear();

        reverse(contextListeners);
        contextListeners.stream().forEach(listener -> listener.contextDestroyed(new ServletContextEvent(this)));
        contextListeners.clear();

        reverse(declaredContextListeners);
        declaredContextListeners.stream().forEach(listener -> listener.contextDestroyed(new ServletContextEvent(this)));
        declaredContextListeners.clear();
        status = SETUP;
    }

    @Override
    public Object getAttribute(String name) {
        Objects.requireNonNull(name);
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return enumeration(attributes.keySet());
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public ServletContext getContext(String uripath) {
        return null;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        checkTainted();
        return getManager().getHttpSessionManager().getDefaultSessionTrackingModes();
    }

    @Override
    public Servlet getDefaultServlet() {
        return defaultServlet;
    }

    @Override
    public int getEffectiveMajorVersion() {
        checkTainted();
        if (effectiveMajorVersion == -1) {
            return getMajorVersion();
        }
        return effectiveMajorVersion;
    }

    @Override
    public int getEffectiveMinorVersion() {
        checkTainted();
        if (effectiveMinorVersion == -1) {
            return getMinorVersion();
        }
        return effectiveMinorVersion;
    }

    @Override
    public void setEffectiveMajorVersion(int effectiveMajorVersion) {
        this.effectiveMajorVersion = effectiveMajorVersion;

    }

    @Override
    public void setEffectiveMinorVersion(int effectiveMinorVersion) {
        this.effectiveMinorVersion = effectiveMinorVersion;

    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        checkTainted();
        return getManager().getHttpSessionManager().getEffectiveSessionTrackingModes();
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        checkTainted();
        return filters.get(filterName);
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        checkTainted();
        return unmodifiableMap(filters);
    }

    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return enumeration(initParameters.keySet());
    }

    @Override
    public List<ServletContainerInitializer> getInitializers() {
        return initializers;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        checkTainted();
        return manager.getJspManager() != null 
                ? manager.getJspManager().getJspConfigDescriptor()
                : null;
    }
    
    /**
     * {@return the major version}
     */
    @Override
    public int getMajorVersion() {
        return 5;
    }

    @Override
    public Collection<String> getMappings(String servletName) {
        return webApplicationRequestMapper.getServletMappings(servletName);
    }

    @Override
    public String getMimeType(String filename) {
        return manager.getMimeTypeManager() != null 
                ? manager.getMimeTypeManager().getMimeType(filename)
                : null;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }
    
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
            LOGGER.log(WARNING, () -> "Unable to get real path: " + path, exception);
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
     * {@return the default request character encoding}
     */
    @Override
    public String getRequestCharacterEncoding() {
        return requestCharacterEncoding;
    }

    /**
     * {@return the default response character encoding}
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
        if (!location.startsWith("/")) {
            throw new MalformedURLException("Location " + location + " must start with a /");
        }

        return getManager().getResourceManager().getResource(location);
    }

    /**
     * Get the resource as a stream.
     *
     * @param location the resource location
     * @return the input stream, or null if not found.
     */
    @Override
    public InputStream getResourceAsStream(String location) {
        return getManager().getResourceManager().getResourceAsStream(location);
    }

    /**
     * Returns the file path or the first nested folder
     *
     * @apiNote
     * <p>
     * <b>Examples.</b>
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
    private String getFileOrFirstFolder(String path, String resource) {
        String normalizedPath = path.endsWith("/") ? path : path + "/";
        String[] split = resource.replace(normalizedPath, "/").split("/");

        // It's a directory
        if (split.length > 2) {
            return normalizedPath + split[1] + "/";
        }

        // It's a file
        return normalizedPath + split[1];
    }

    /**
     * Returns a directory-like listing of all the paths to resources within the
     * web application whose longest sub-path matches the supplied path
     * argument.
     *
     * @param path the partial path used to match the resources
     * @return a Set containing the directory listing, or null if there are no
     * resources in the web application whose path begins with the supplied
     * path.
     */
    private Set<String> getResourcePathsImpl(String path) {
        Set<String> collect
                = getManager().getResourceManager().getAllLocations()
                        .filter(resource -> resource.startsWith(path))
                        .filter(not(isEqual(path)))
                        .map(resource -> getFileOrFirstFolder(path, resource))
                        .collect(toSet());

        if (collect.isEmpty()) {
            return null;
        }

        return collect;
    }

    /**
     * {@return the resource paths}
     * @param path the path.
     */
    @Override
    public Set<String> getResourcePaths(String path) {
        if (path == null) {
            return null;
        }

        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path must start with /");
        }

        return getResourcePathsImpl(path);
    }

    /**
     * {@return the response}
     * @param request the request.
     */
    @Override
    public ServletResponse getResponse(ServletRequest request) {
        return (ServletResponse) request.getAttribute(PIRANHA_RESPONSE);
    }

    /**
     * {@return the server info}
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
    @Deprecated
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
     * {@return the servlet names}
     * @deprecated
     */
    @Deprecated
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
        checkTainted();
        return servletEnvironments.get(servletName);
    }

    /**
     * {@return the servlet registrations}
     */
    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        checkTainted();
        return unmodifiableMap(servletEnvironments);
    }

    @Deprecated
    @Override
    public Enumeration<Servlet> getServlets() {
        throw new UnsupportedOperationException("ServletContext.getServlets() is no longer supported");
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        checkTainted();
        return getManager().getHttpSessionManager().getSessionCookieConfig();
    }

    @Override
    public int getSessionTimeout() {
        return getManager().getHttpSessionManager().getSessionTimeout();
    }

    @Override
    public String getVirtualServerName() {
        return virtualServerName;
    }

    @Override
    public void initialize() {
        LOGGER.log(DEBUG, "Initializing web application at {0}", contextPath);
        verifyState(SETUP, "Unable to initialize web application");
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(classLoader);
            initializeInitializers();
            initializeFilters();
            initializeServlets();
            initializeFinish();
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    @Override
    public void initializeDeclaredFinish() {
        if (status == SETUP) {
            status = INITIALIZED_DECLARED;
            LOGGER.log(DEBUG, "Initialized declared items for web application at {0}", contextPath);
        }
        if (status == ERROR) {
            LOGGER.log(WARNING, "An error occurred initializing webapplication at {0}", contextPath);
        }
    }

    /**
     * Finish the initialization.
     */
    @Override
    public void initializeFinish() {
        if (status == SETUP || status == INITIALIZED_DECLARED) {
            status = INITIALIZED;
            LOGGER.log(DEBUG, "Initialized web application at {0}", contextPath);
        }
        if (status == ERROR) {
            LOGGER.log(WARNING, () -> "An error occurred initializing webapplication at " + contextPath);
        }
    }

    /**
     * Initialize the filters.
     */
    @Override
    public void initializeFilters() {
        if (status == SETUP || status == INITIALIZED_DECLARED) {
            List<String> filterNames = new ArrayList<>(filters.keySet());
            filterNames.stream().map(filters::get).forEach(environment -> {
                try {
                    environment.initialize();
                    environment.getFilter().init(environment);
                } catch (Throwable t) {
                    LOGGER.log(WARNING, () -> "Unable to initialize filter: " + environment.getFilterName(), t);
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
                HandlesTypes annotation = initializer.getClass().getAnnotation(HandlesTypes.class);
                Set<Class<?>> classes = Collections.emptySet();
                if (annotation != null) {
                    Class<?>[] value = annotation.value();
                    // Get instances
                    Stream<Class<?>> instances = manager.getAnnotationManager()
                            .getInstances(value).stream();

                    // Get classes by target type
                    List<AnnotationInfo> annotations = manager.getAnnotationManager()
                            .getAnnotations(value);
                    
                    Stream<Class<?>> classStream = annotations.stream().map(AnnotationInfo::getTargetType);

                    classes = Stream.concat(instances, classStream).collect(Collectors.toUnmodifiableSet());
                }
                try {
                    source = initializer;
                    initializer.onStartup(classes, this);
                } finally {
                    source = null;
                }
            } catch (Throwable t) {
                LOGGER.log(WARNING, () -> "Initializer " + initializer.getClass().getName() + " failing onStartup", t);
                error = true;
            }
        }

        if (!error) {
            List<ServletContextListener> listeners = new ArrayList<>(declaredContextListeners);
            listeners.stream().forEach(listener -> {
                try {
                    source = listener;
                    listener.contextInitialized(new ServletContextEvent(this));
                } finally {
                    source = null;
                }
            });

            try {
                tainted = true;
                listeners = new ArrayList<>(contextListeners);
                listeners.stream().forEach(listener -> {
                    source = listener;
                    listener.contextInitialized(new ServletContextEvent(this));
                });
            } finally {
                tainted = false;
                source = null;
            }
        } else {
            status = ERROR;
        }
    }

    /**
     * Initialize the servlets.
     */
    @Override
    public void initializeServlets() {
        if (status == SETUP || status == INITIALIZED_DECLARED) {
            List<String> servletsToBeRemoved = new ArrayList<>();
            List<String> servletNames = new ArrayList<>(servletEnvironments.keySet());

            servletNames.stream().map(servletEnvironments::get).forEach(environment -> {
                initializeServlet(environment);
                if (isPermanentlyUnavailable(environment)) {
                    servletsToBeRemoved.add(environment.getServletName());
                }
            });

            for (String servletName : servletsToBeRemoved) {
                // Servlet:SPEC:11 - If a permanent unavailability is indicated by the UnavailableException, the servlet container must
                // remove the servlet from service ... and release the servlet instance.
                servletEnvironments.remove(servletName);
            }

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

    @Override
    public boolean isInitialized() {
        return status >= INITIALIZED && status < ERROR;
    }
    
    @Override
    public boolean isMetadataComplete() {
        return metadataComplete;
    }

    /**
     * Initialize the servlet.
     *
     * @param environment the default servlet environment.
     */
    @SuppressWarnings("unchecked")
    private void initializeServlet(DefaultServletEnvironment environment) {
        try {
            LOGGER.log(DEBUG, "Initializing servlet: {0}", environment.servletName);
            if (environment.getServlet() == null) {
                Class<? extends Servlet> clazz = environment.getServletClass();
                if (clazz == null) {
                    ClassLoader loader = getClassLoader();
                    if (loader == null) {
                        loader = getClass().getClassLoader();
                    }
                    if (loader == null) {
                        loader = ClassLoader.getSystemClassLoader();
                    }
                    clazz = (Class<? extends Servlet>) loader.loadClass(environment.getClassName());
                }
                environment.setServlet(createServlet(clazz));
            }
            environment.getServlet().init(environment);
            LOGGER.log(DEBUG, "Initialized servlet: {0}", environment.servletName);
        } catch (Throwable t) {
            LOGGER.log(WARNING, () -> "Unable to initialize servlet: " + environment.className, t);

            environment.setStatus(ServletEnvironment.UNAVAILABLE);
            environment.setUnavailableException(t);

            // Servlet:SPEC:11 - If a permanent unavailability is indicated by the UnavailableException, the servlet container must ... call its destroy method
            if (isPermanentlyUnavailable(environment) && environment.getServlet() != null) {
                try {
                    environment.getServlet().destroy();
                } catch (Throwable t2) {
                    t.addSuppressed(t2);
                }
            }

            environment.setServlet(null);
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
        request.setAttribute(PIRANHA_RESPONSE, response);
        responses.put(response, request);
    }

    @Deprecated
    @Override
    public void log(Exception exception, String message) {
        if (manager.getLoggingManager() != null) {
            manager.getLoggingManager().log(message, exception);
        }
    }

    @Override
    public void log(String message, Throwable throwable) {
        if (manager.getLoggingManager() != null) {
            manager.getLoggingManager().log(message, throwable);
        }
    }

    @Override
    public void log(String message) {
        if (manager.getLoggingManager() != null) {
            manager.getLoggingManager().log(message);
        }
    }

    /**
     * Remove the attribute with the given name.
     *
     * @param name the name.
     */
    @Override
    public void removeAttribute(String name) {
        attributeRemoved(name, attributes.remove(name));
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        verifyState(SERVICING, "Unable to service request");
        verifyRequestResponseTypes(request, response);

        linkRequestAndResponse(request, response);
        requestInitialized(request);

        DefaultWebApplicationRequest webAppRequest = (DefaultWebApplicationRequest) request;
        DefaultWebApplicationResponse webAppResponse = (DefaultWebApplicationResponse) response;

        // Obtain a reference to the target servlet invocation, which includes the Servlet itself and/or Filters, as well as mapping data
        DefaultServletInvocation servletInvocation = invocationFinder.findServletInvocationByPath(webAppRequest.getServletPath(), webAppRequest.getPathInfo());

        // Dispatch using the REQUEST dispatch type. This will invoke the Servlet and/or Filters if present and available.
        getInvocationDispatcher(servletInvocation).request(webAppRequest, webAppResponse);

        if (webAppRequest.getSession(false) instanceof DefaultHttpSession session) {
            session.setLastAccessedTime(System.currentTimeMillis());
        }

        requestDestroyed(request);
        unlinkRequestAndResponse(request, response);

        if (webAppRequest.isUpgraded()) {
            WebConnection connection = new DefaultWebConnection(webAppRequest, webAppResponse);
            webAppRequest.getUpgradeHandler().init(connection);
        }
    }
    
    @Override
    public void setAttribute(String name, Object value) {
        Objects.requireNonNull(name);
        if (value != null) {
            boolean added = true;
            if (attributes.containsKey(name)) {
                added = false;
            }
            Object previousValue = attributes.put(name, value);
            if (added) {
                attributeAdded(name, value);
            } else {
                attributeReplaced(name, previousValue);
            }
        } else {
            removeAttribute(name);
        }
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setContextPath(String contextPath) {
        LOGGER.log(DEBUG, "Setting context path to: {0}", contextPath);
        this.contextPath = contextPath;
    }

    @Override
    public void setDefaultServlet(Servlet defaultServlet) {
        this.defaultServlet = defaultServlet;
    }

    @Override
    public void setDistributable(boolean distributable) {
        this.distributable = distributable;
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        requireNonNull(name);
        checkTainted();
        if (status != SETUP && status != INITIALIZED_DECLARED) {
            throw new IllegalStateException("Cannot set init parameter once web application is initialized");
        }
        boolean result = true;
        if (initParameters.containsKey(name)) {
            result = false;
        } else {
            initParameters.put(name, value);
        }
        return result;
    }

    @Override
    public void setRequestCharacterEncoding(String requestCharacterEncoding) {
        this.requestCharacterEncoding = requestCharacterEncoding;
    }

    @Override
    public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        this.responseCharacterEncoding = responseCharacterEncoding;
    }

    @Override
    public void setServletContextName(String servletContextName) {
        this.servletContextName = servletContextName;
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        checkTainted();
        checkServicing();
        getManager().getHttpSessionManager().setSessionTrackingModes(sessionTrackingModes);
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {
        checkTainted();
        if (status != SETUP && status != INITIALIZED_DECLARED) {
            throw new IllegalStateException("Illegal to set session timeout because state is not SETUP");
        }
        getManager().getHttpSessionManager().setSessionTimeout(sessionTimeout);
    }

    /**
     * Set the virtual server name.
     *
     * @param virtualServerName the virtual server name.
     */
    public void setVirtualServerName(String virtualServerName) {
        this.virtualServerName = virtualServerName;
    }

    @Override
    public void setWebApplicationRequestMapper(WebApplicationRequestMapper webApplicationRequestMapper) {
        this.webApplicationRequestMapper = webApplicationRequestMapper;
    }

    @Override
    public void start() {
        LOGGER.log(DEBUG, "Starting web application at {0}", contextPath);
        verifyState(INITIALIZED, "Unable to start servicing");
        status = SERVICING;
        LOGGER.log(DEBUG, "Started web application at {0}", contextPath);
    }

    @Override
    public void stop() {
        LOGGER.log(DEBUG, "Stopping web application at {0}", contextPath);
        verifyState(SERVICING, "Unable to stop servicing");
        status = INITIALIZED;
        LOGGER.log(DEBUG, "Stopped web application at {0}", contextPath);
    }

    @Override
    public void unlinkRequestAndResponse(ServletRequest request, ServletResponse response) {
        request.removeAttribute(PIRANHA_RESPONSE);
        responses.remove(response);
    }

    @Override
    public DefaultServletRequestDispatcher getRequestDispatcher(String path) {
        try {
            DefaultServletInvocation servletInvocation = invocationFinder.findServletInvocationByPath(null, path, null);
            if (servletInvocation == null) {
                return null;
            }

            return getInvocationDispatcher(servletInvocation);
        } catch (IOException | ServletException e) {
            LOGGER.log(WARNING, "Error occurred while getting request dispatcher", e);
            return null;
        }
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        DefaultServletInvocation servletInvocation = invocationFinder.findServletInvocationByName(name);
        if (servletInvocation == null) {
            return null;
        }

        return getInvocationDispatcher(servletInvocation);
    }

    /**
     * Attribute added.
     *
     * @param name the name.
     * @param value the value.
     */
    private void attributeAdded(String name, Object value) {
        contextAttributeListeners.stream().forEach(listener -> listener.attributeAdded(new ServletContextAttributeEvent(this, name, value)));
    }

    /**
     * Attributed removed.
     *
     * @param name the name.
     * @param previousValue the previous value.
     */
    private void attributeRemoved(String name, Object previousValue) {
        contextAttributeListeners.stream().forEach(listener -> listener.attributeRemoved(new ServletContextAttributeEvent(this, name, previousValue)));
    }

    /**
     * Attribute removed.
     *
     * @param name the name.
     * @param value the value.
     */
    private void attributeReplaced(String name, Object value) {
        contextAttributeListeners.stream().forEach(listener -> listener.attributeReplaced(new ServletContextAttributeEvent(this, name, value)));
    }

    /**
     * Make sure the application is not servicing when this method is called.
     */
    private void checkServicing() {
        if (status == SERVICING) {
            throw new IllegalStateException("Cannot call this after web application has initialized");
        }
    }

    /**
     * Make sure the application is not tainted when this method is called.
     */
    private void checkTainted() {
        if (tainted) {
            throw new UnsupportedOperationException(
                    "ServletContext is in tainted mode (as required by spec).");
        }
    }

    /**
     * Get the name request dispatcher.
     *
     * @param servletInvocation the servlet invocation.
     * @return the request dispatcher.
     */
    private DefaultServletRequestDispatcher getInvocationDispatcher(DefaultServletInvocation servletInvocation) {
        return new DefaultServletRequestDispatcher(servletInvocation, this);
    }

    /**
     * Is the string null or empty.
     *
     * @param string the string
     * @return true if it is, false otherwise.
     */
    private boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Is the servlet permanently unavailable.
     *
     * @param environment the Servlet environment.
     * @return true if it is, false otherwise.
     */
    private boolean isPermanentlyUnavailable(DefaultServletEnvironment environment) {
        boolean permanent = false;
        if (environment.getUnavailableException() instanceof UnavailableException ue) {
            permanent = ue.isPermanent();
        }
        return permanent;
    }

    /**
     * Fire the request destroyed event.
     *
     * @param request the request.
     */
    private void requestDestroyed(ServletRequest request) {
        if (!requestListeners.isEmpty()) {
            requestListeners.stream().forEach(servletRequestListener -> servletRequestListener.requestDestroyed(new ServletRequestEvent(this, request)));
        }
    }

    /**
     * Fire the request initialized event.
     *
     * @param request the request.
     */
    private void requestInitialized(ServletRequest request) {
        if (!requestListeners.isEmpty()) {
            requestListeners.stream().forEach(servletRequestListener -> servletRequestListener.requestInitialized(new ServletRequestEvent(this, request)));
        }
    }

    @Override
    public void setMetadataComplete(boolean metadataComplete) {
        this.metadataComplete = metadataComplete;
    }

    /**
     * Verify the request/response types.
     * 
     * @param request the request.
     * @param response the response.
     * @throws ServletException when request or response is invalid.
     */
    protected void verifyRequestResponseTypes(ServletRequest request, ServletResponse response) throws ServletException {
        if (!(request instanceof DefaultWebApplicationRequest) || !(response instanceof DefaultWebApplicationResponse)) {
            throw new ServletException("Invalid request or response");
        }
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
    
    @Override
    public WebApplicationManager getManager() {
        return manager;
    }
}
