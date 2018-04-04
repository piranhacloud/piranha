/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletSecurityElement;

/**
 * The default ServletEnvironment.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultServletEnvironment implements Dynamic, ServletConfig {

    /**
     * Defines the UNAVAILABLE constant.
     */
    public static final int UNAVAILABLE = -1;

    /**
     * Stores the async supported flag.
     */
    protected boolean asyncSupported;

    /**
     * Stores the class name.
     */
    protected String className;

    /**
     * Stores the init parameters.
     */
    protected final Map<String, String> initParameters;

    /**
     * Stores the load on startup value.
     */
    protected int loadOnStartup;

    /**
     * Stores the multi-part config.
     */
    protected MultipartConfigElement multipartConfig;

    /**
     * Stores the run-as-role.
     */
    protected String runAsRole;

    /**
     * Stores the servlet.
     */
    protected Servlet servlet;

    /**
     * Stores the servlet class.
     */
    protected Class servletClass;

    /**
     * Stores the servlet name.
     */
    protected final String servletName;

    /**
     * Stores the status.
     */
    protected int status;

    /**
     * Stores the web application.
     */
    protected final WebApplication webApp;

    /**
     * Constructor.
     *
     * @param webApp the web application.
     * @param servletName the serlvet name.
     */
    public DefaultServletEnvironment(DefaultWebApplication webApp, String servletName) {
        this.asyncSupported = false;
        this.initParameters = new ConcurrentHashMap<>(1);
        this.loadOnStartup = -1;
        this.servletName = servletName;
        this.webApp = webApp;
    }

    /**
     * Constructor.
     *
     * @param webApp the web application.
     * @param servletName the servlet name.
     * @param servlet the servlet.
     */
    public DefaultServletEnvironment(DefaultWebApplication webApp, String servletName, Servlet servlet) {
        this(webApp, servletName);
        this.className = servlet.getClass().getName();
        this.servlet = servlet;
    }

    /**
     * Add a mapping for the given servlet.
     *
     * @param urlPatterns the url patterns.
     * @return a set of which mappings where set.
     */
    @Override
    public Set<String> addMapping(String... urlPatterns) {
        return webApp.addServletMapping(servletName, urlPatterns);
    }

    /**
     * Get the class name.
     *
     * @return the class name.
     */
    @Override
    public String getClassName() {
        return className;
    }

    /**
     * Get the init parameter.
     *
     * @param name the parameter name.
     * @return the value.
     */
    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    /**
     * Get the init parameter names.
     *
     * @return the init parameter names.
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
    }

    /**
     * Get the init parameters.
     *
     * @return the init parameters.
     */
    @Override
    public Map<String, String> getInitParameters() {
        return initParameters;
    }

    /**
     * Get the load on startup.
     *
     * @return the load on startup.
     */
    public int getLoadOnStartup() {
        return loadOnStartup;
    }

    /**
     * Get the mappings.
     *
     * @return the mappings.
     */
    @Override
    public Collection<String> getMappings() {
        Collection<String> result = new ArrayList<>();
        Collection<String> mappings = webApp.getMappings(servletName);
        if (mappings != null) {
            result.addAll(mappings);
        }
        return result;
    }

    /**
     * Get the multi-part config.
     *
     * @return the multi-part config.
     */
    public MultipartConfigElement getMultipartConfig() {
        return multipartConfig;
    }

    /**
     * Get the servlet name.
     *
     * @return the servlet name.
     */
    @Override
    public String getName() {
        return servletName;
    }

    /**
     * Get the "Run as Role".
     *
     * @return the role.
     */
    @Override
    public String getRunAsRole() {
        return runAsRole;
    }

    /**
     * Get the servlet.
     *
     * @return the servlet.
     */
    public Servlet getServlet() {
        return servlet;
    }

    /**
     * Get the servlet context.
     *
     * @return
     */
    @Override
    public ServletContext getServletContext() {
        return webApp;
    }

    /**
     * Get the servlet class.
     *
     * @return the servlet class.
     */
    public Class getServletClass() {
        return servletClass;
    }

    /**
     * Get the servlet name.
     *
     * @return the servlet name.
     */
    @Override
    public String getServletName() {
        return servletName;
    }

    /**
     * Get the web application.
     *
     * @return the web application.
     */
    public WebApplication getWebApplication() {
        return this.webApp;
    }

    /**
     * Is async supported.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isAsyncSupported() {
        return asyncSupported;
    }

    /**
     * Set the async supported flag.
     *
     * @param asyncSupported the async supported flag.
     */
    @Override
    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    /**
     * Set the class name.
     *
     * @param className the class name.
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Set the init parameter.
     *
     * @param name the parameter name.
     * @param value the parameter value.
     * @return if the init parameter was set.
     */
    @Override
    public boolean setInitParameter(String name, String value) {
        initParameters.put(name, value);
        return true;
    }

    /**
     * Set the init parameters.
     *
     * @param initParameters the init parameters.
     * @return the set of set init parameters that could not be set.
     */
    @Override
    public Set<String> setInitParameters(Map<String, String> initParameters) {
        HashSet<String> conflictingParams = new HashSet<>();
        for (Map.Entry<String, String> entry : initParameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key == null || value == null) {
                throw new IllegalArgumentException("Init parameter key or value cannot be null");
            } else if (!setInitParameter(key, value)) {
                conflictingParams.add(key);
            }
        }
        return conflictingParams;
    }

    /**
     * Set the load on startup.
     *
     * @param loadOnStartup the load on startup.
     */
    @Override
    public void setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    /**
     * Set the multipart config.
     *
     * @param multipartConfig the multipart config.
     */
    @Override
    public void setMultipartConfig(MultipartConfigElement multipartConfig) {
        this.multipartConfig = multipartConfig;
    }

    /**
     * Set the run as role.
     *
     * @param runAsRole the run as role.
     */
    @Override
    public void setRunAsRole(String runAsRole) {
        this.runAsRole = runAsRole;
    }

    /**
     * Set the servlet.
     *
     * @param servlet the servlet.
     */
    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    /**
     * Set the servlet security element.
     *
     * @param servletSecurityElement the servlet security element.
     * @return the servlet security element.
     */
    @Override
    public Set<String> setServletSecurity(ServletSecurityElement servletSecurityElement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Set the status.
     *
     * @param status the status.
     */
    public void setStatus(int status) {
        this.status = status;
    }
}
