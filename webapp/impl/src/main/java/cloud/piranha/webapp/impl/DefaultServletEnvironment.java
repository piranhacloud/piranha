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
package cloud.piranha.webapp.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletSecurityElement;

import cloud.piranha.webapp.api.ServletEnvironment;
import cloud.piranha.webapp.api.WebApplication;

/**
 * The default ServletEnvironment.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultServletEnvironment implements ServletEnvironment {

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
    protected Class<? extends Servlet> servletClass;

    /**
     * Stores the servlet name.
     */
    protected final String servletName;

    /**
     * Stores the status.
     */
    protected int status;

    /**
     * Stores the unavailableException.
     */
    protected Throwable unavailableException;

    /**
     * Stores the web application.
     */
    protected final WebApplication webApp;

    /**
     * Constructor.
     *
     * @param webApp the web application.
     * @param servletName the servlet name.
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

    @Override
    public Set<String> addMapping(String... urlPatterns) {
        return webApp.addServletMapping(servletName, urlPatterns);
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
    }

    @Override
    public Map<String, String> getInitParameters() {
        return initParameters;
    }

    @Override
    public int getLoadOnStartup() {
        return loadOnStartup;
    }

    @Override
    public Collection<String> getMappings() {
        Collection<String> result = new ArrayList<>();
        Collection<String> mappings = webApp.getMappings(servletName);
        if (mappings != null) {
            result.addAll(mappings);
        }
        return result;
    }

    @Override
    public MultipartConfigElement getMultipartConfig() {
        return multipartConfig;
    }

    @Override
    public String getName() {
        return servletName;
    }

    @Override
    public String getRunAsRole() {
        return runAsRole;
    }

    @Override
    public Servlet getServlet() {
        return servlet;
    }

    @Override
    public ServletContext getServletContext() {
        return webApp;
    }

    @Override
    public Class<? extends Servlet> getServletClass() {
        return servletClass;
    }

    @Override
    public String getServletName() {
        return servletName;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public WebApplication getWebApplication() {
        return this.webApp;
    }

    @Override
    public boolean isAsyncSupported() {
        return asyncSupported;
    }

    @Override
    public void setAsyncSupported(boolean asyncSupported) {
        this.asyncSupported = asyncSupported;
    }

    @Override
    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        boolean result = false;
        if (!initParameters.containsKey(name)) {
            initParameters.put(name, value);
            result = true;
        }
        return result;
    }

    @Override
    public Set<String> setInitParameters(Map<String, String> initParameters) {
        HashSet<String> conflicting = new HashSet<>();
        if (initParameters != null) {
            initParameters.entrySet().forEach(entry -> {
                String name = entry.getKey();
                String value = entry.getValue();
                if (name == null) {
                    throw new IllegalArgumentException("A null name is not allowed");
                }
                if (value == null) {
                    throw new IllegalArgumentException("A null value is not allowed");
                }
                if (!setInitParameter(name, value)) {
                    conflicting.add(name);
                }
            });
        }
        return conflicting;
    }

    @Override
    public void setLoadOnStartup(int loadOnStartup) {
        this.loadOnStartup = loadOnStartup;
    }

    @Override
    public void setMultipartConfig(MultipartConfigElement multipartConfig) {
        this.multipartConfig = multipartConfig;
    }

    @Override
    public void setRunAsRole(String runAsRole) {
        this.runAsRole = runAsRole;
    }

    @Override
    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    @Override
    public Set<String> setServletSecurity(ServletSecurityElement servletSecurityElement) {
        return new HashSet<>();
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public Throwable getUnavailableException() {
        return unavailableException;
    }

    @Override
    public void setUnavailableException(Throwable unavailableException) {
        this.unavailableException = unavailableException;
    }

}
