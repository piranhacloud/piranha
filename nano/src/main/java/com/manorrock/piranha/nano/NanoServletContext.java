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
package com.manorrock.piranha.nano;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

/**
 * The Nano version of the Servlet context.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoServletContext implements ServletContext {

    /**
     * Stores the context path.
     */
    private String contextPath;
    
    /**
     * Constructor.
     */
    public NanoServletContext() {
        contextPath = "";
    }
    
    /**
     * Not supported.
     * 
     * @param filterName the filter name.
     * @param className the class name.
     * @return the dynamic.
     */
    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param filterName the filter name.
     * @param filter the filter.
     * @return the dynamic.
     */
    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param filterName the filter name.
     * @param filterClass the filter class.
     * @return the dynamic.
     */
    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param servletName the servlet name.
     * @param jspFile the JSP file.
     * @return the dynamic.
     */
    @Override
    public ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param className the class name.
     */
    @Override
    public void addListener(String className) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param <T> the type.
     * @param listener the listener.
     */
    @Override
    public <T extends EventListener> void addListener(T listener) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param listenerClass the listener class.
     */
    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param servletName the servlet name.
     * @param className the class name.
     * @return the dynamic.
     */
    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param servletName the servlet name.
     * @param servlet the servlet.
     * @return the dynamic.
     */
    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param servletName the servlet name.
     * @param servletClass the servlet class.
     * @return the dynamic.
     */
    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param <T> the type.
     * @param clazz the class.
     * @return the filter.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param <T> the type.
     * @param clazz the class.
     * @return the listener.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param <T> the type.
     * @param clazz the class.
     * @return the servlet.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param roles the roles.
     */
    @Override
    public void declareRoles(String... roles) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param name the name.
     * @return the value.
     */
    @Override
    public Object getAttribute(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @return the attribute names.
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @return the class loader.
     */
    @Override
    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param path the path
     * @return the Servlet context.
     */
    @Override
    public ServletContext getContext(String path) {
        throw new UnsupportedOperationException("Not supported");
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
     * Not supported.
     * 
     * @return the set of default session tracking modes.
     */
    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Get the effective major version.
     * 
     * @return the effective major version.
     */
    @Override
    public int getEffectiveMajorVersion() {
        return 4;
    }

    /**
     * Get the effective minor version.
     * 
     * @return the effective minor version.
     */
    @Override
    public int getEffectiveMinorVersion() {
        return 0;
    }

    /**
     * Not supported.
     * 
     * @return the set of efftive session tracking modes.
     */
    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param filterName the filter name.
     * @return the filter registration.
     */
    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @return the filter registrations.
     */
    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @param name the name.
     * @return the value.
     */
    @Override
    public String getInitParameter(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * Not supported.
     * 
     * @return the init parameter names.
     */
    @Override
    public Enumeration<String> getInitParameterNames() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getMajorVersion() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getMimeType(String filename) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getMinorVersion() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getRequestCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getResponseCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getServerInfo() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getServletContextName() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Enumeration<String> getServletNames() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public int getSessionTimeout() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getVirtualServerName() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void log(String message) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void log(Exception exception, String message) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void log(String message, Throwable throwable) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void removeAttribute(String name) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setAttribute(String name, Object object) {
        throw new UnsupportedOperationException("Not supported");
    }
    
    /**
     * Set the context path.
     * 
     * @param contextPath the context path.
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setRequestCharacterEncoding(String requestCharacterEncoding) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        throw new UnsupportedOperationException("Not supported");
    }
}
