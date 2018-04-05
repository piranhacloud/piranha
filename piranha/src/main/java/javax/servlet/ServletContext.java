/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
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
package javax.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import javax.servlet.descriptor.JspConfigDescriptor;

/**
 * The servlet context API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletContext {

    /**
     * Defines the ORDERED_LIBS constant.
     */
    public static final String ORDERED_LIBS = "javax.servlet.context.orderedLibs";

    /**
     * Defines the TEMPDIR constant.
     */
    public static final String TEMPDIR = "javax.servlet.context.tempdir";

    /**
     * Add the filter.
     *
     * @param filterName the filter name.
     * @param className the class name.
     * @return the dynamic filter registration.
     */
    FilterRegistration.Dynamic addFilter(String filterName, String className);

    /**
     * Add the filter.
     *
     * @param filterName the filter name.
     * @param filter the filter.
     * @return the dynamic filter registration.
     */
    FilterRegistration.Dynamic addFilter(String filterName, Filter filter);

    /**
     * Add the filter.
     *
     * @param filterName the filter name.
     * @param filterClass the filter class.
     * @return the dynamic filter registration.
     */
    FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass);
    
    /**
     * Add a JSP file.
     * 
     * @param servletName the name of the servlet to be used.
     * @param jspFile the path of the JSP file.
     * @return the dynamic servlet registration.
     */
    ServletRegistration.Dynamic addJspFile(String servletName, String jspFile);

    /**
     * Add the listener.
     *
     * @param className the class name.
     */
    void addListener(String className);

    /**
     * Add the listener.
     *
     * @param <T> the type.
     * @param listener the listener.
     */
    <T extends EventListener> void addListener(T listener);

    /**
     * Add the listener.
     *
     * @param listenerClass the listener class.
     */
    void addListener(Class<? extends EventListener> listenerClass);

    /**
     * Add the servlet.
     *
     * @param servletName the servlet name.
     * @param className the class name.
     * @return the servlet dynamic registration.
     */
    ServletRegistration.Dynamic addServlet(String servletName, String className);

    /**
     * Add the servlet.
     *
     * @param servletName the servlet name.
     * @param servlet the servlet.
     * @return the dynamic servlet registration.
     */
    ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet);

    /**
     * Add the servlet.
     *
     * @param servletName the servlet name.
     * @param servletClass the servlet class.
     * @return the dynamic servlet registration.
     */
    ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass);

    /**
     * Create the filter.
     *
     * @param <T> the filter type.
     * @param clazz the class.
     * @return the filter.
     * @throws ServletException when a servlet error occurs.
     */
    <T extends Filter> T createFilter(Class<T> clazz) throws ServletException;

    /**
     * Create the listener.
     *
     * @param <T> the listener type.
     * @param clazz the class.
     * @return the listener.
     * @throws ServletException when a servlet error occurs.
     */
    <T extends EventListener> T createListener(Class<T> clazz) throws ServletException;

    /**
     * Create the servlet.
     *
     * @param <T> the servlet type.
     * @param clazz the class.
     * @return the created servlet.
     * @throws ServletException when a servlet error occurs.
     */
    <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException;

    /**
     * Declare the roles.
     *
     * @param roles the roles.
     */
    void declareRoles(String... roles);

    /**
     * Get the attribute.
     *
     * @param name the name.
     * @return the value, or null.
     */
    Object getAttribute(String name);

    /**
     * Get the attribute names.
     *
     * @return the attribute names.
     */
    Enumeration<String> getAttributeNames();

    /**
     * Get the class loader.
     *
     * @return the class loader.
     */
    ClassLoader getClassLoader();

    /**
     * Get the servlet context for the given path.
     *
     * @param path the path.
     * @return the servlet context, or null if not found (or not supported).
     */
    ServletContext getContext(String path);

    /**
     * Get the context path.
     *
     * @return the context path.
     */
    String getContextPath();

    /**
     * Get the default session tracking modes.
     *
     * @return the default session tracking modes.
     */
    Set<SessionTrackingMode> getDefaultSessionTrackingModes();

    /**
     * Get the effective major version.
     *
     * @return the effective major version.
     */
    int getEffectiveMajorVersion();

    /**
     * Get the effective minor version.
     *
     * @return the effective minor version.
     */
    int getEffectiveMinorVersion();

    /**
     * Get the effective session tracking modes.
     *
     * @return the effective session tracking modes.
     */
    Set<SessionTrackingMode> getEffectiveSessionTrackingModes();

    /**
     * Get the filter registration.
     *
     * @param filterName the filter name.
     * @return the
     */
    FilterRegistration getFilterRegistration(String filterName);

    /**
     * Get the filter registrations.
     *
     * @return the filter registrations.
     *
     */
    Map<String, ? extends FilterRegistration> getFilterRegistrations();

    /**
     * Get the init parameter.
     *
     * @param name the name.
     * @return the value, or null if not found.
     */
    String getInitParameter(String name);

    /**
     * Get the init parameter names.
     *
     * @return the init parameter names.
     */
    Enumeration<String> getInitParameterNames();

    /**
     * Get the JSP config descriptor.
     *
     * @return the JSP config descriptor.
     */
    JspConfigDescriptor getJspConfigDescriptor();

    /**
     * Get the major version.
     *
     * @return the major version.
     */
    int getMajorVersion();

    /**
     * Get the mime type.
     *
     * @param filename the filename.
     * @return the mime type, or null.
     */
    String getMimeType(String filename);

    /**
     * Get the minor version.
     *
     * @return the minor version.
     */
    int getMinorVersion();

    /**
     * Get the named dispatcher.
     *
     * @param name the name.
     * @return the request dispatcher, or null if not found.
     */
    RequestDispatcher getNamedDispatcher(String name);

    /**
     * Get the real path.
     *
     * @param path the path.
     * @return the real path.
     */
    String getRealPath(String path);
    
    /**
     * Get the default request character encoding.
     * 
     * @return the default request character encoding.
     */
    String getRequestCharacterEncoding();

    /**
     * Get the request dispatcher.
     *
     * @param path the path.
     * @return the request dispatcher, or null.
     */
    RequestDispatcher getRequestDispatcher(String path);
    
    /**
     * Get the default response character encoding.
     * 
     * @return the default response character encoding.
     */
    String getResponseCharacterEncoding();
    
    /**
     * Get the resource.
     *
     * @param path the path.
     * @return the URL, or null if not found.
     * @throws MalformedURLException when the path is malformed.
     */
    URL getResource(String path) throws MalformedURLException;

    /**
     * Get the resource as a stream.
     *
     * @param path the path.
     * @return the input stream, or null.
     */
    InputStream getResourceAsStream(String path);

    /**
     * Get the resource paths for the given path.
     *
     * @param path the path.
     * @return the resource paths.
     */
    Set<String> getResourcePaths(String path);

    /**
     * Get the server info.
     *
     * @return the server info.
     */
    String getServerInfo();

    /**
     * Get the servlet.
     *
     * @param name the name.
     * @return null
     * @throws ServletException when a servlet error occurs.
     * @deprecated
     */
    Servlet getServlet(String name) throws ServletException;

    /**
     * Get the servlet context name.
     *
     * @return the servlet context name.
     */
    String getServletContextName();

    /**
     * Get the servlet names.
     *
     * @return an empty enumeration.
     * @deprecated
     */
    Enumeration<String> getServletNames();

    /**
     * Get the servlet registration.
     *
     * @param servletName the servlet name.
     * @return the servlet registration, or null if not found.
     */
    ServletRegistration getServletRegistration(String servletName);

    /**
     * Get the servlet registrations.
     *
     * @return the servlet registrations.
     */
    Map<String, ? extends ServletRegistration> getServletRegistrations();

    /**
     * Get the servlets.
     *
     * @return an empty enumeration.
     * @deprecated
     */
    Enumeration<Servlet> getServlets();

    /**
     * Get the session cookie config.
     *
     * @return the session cookie config.
     */
    SessionCookieConfig getSessionCookieConfig();
    
    /**
     * Get the default session timeout.
     * 
     * @return the default session timeout.
     */
    int getSessionTimeout();

    /**
     * Get the virtual server name.
     *
     * @return the virtual server name.
     */
    String getVirtualServerName();

    /**
     * Log the specified message.
     *
     * @param message the message.
     */
    void log(String message);

    /**
     * Log the exception and message.
     *
     * @param exception the exception.
     * @param message the message.
     * @deprecated
     */
    void log(Exception exception, String message);

    /**
     * Log the message and throwable.
     *
     * @param message the message.
     * @param throwable the throwable.
     */
    void log(String message, Throwable throwable);

    /**
     * Remove the attribute.
     *
     * @param name the name.
     */
    void removeAttribute(String name);

    /**
     * Set the attribute.
     *
     * @param name the name.
     * @param object the object value.
     */
    void setAttribute(String name, Object object);

    /**
     * Set the init parameter.
     *
     * @param name the name.
     * @param value the value.
     * @return true if it was set, false otherwise.
     */
    boolean setInitParameter(String name, String value);
    
    /**
     * Set the default request character encoding.
     * 
     * @param requestCharacterEncoding the default request character encoding.
     */
    void setRequestCharacterEncoding(String requestCharacterEncoding);
    
    /**
     * Set the default response character encoding.
     * 
     * @param responseCharacterEncoding the default response character encoding.
     */
    void setResponseCharacterEncoding(String responseCharacterEncoding);
    
    /**
     * Set the default session timeout.
     * 
     * @param sessionTimeout the default session timeout.
     */
    void setSessionTimeout(int sessionTimeout);

    /**
     * Set the session tracking modes.
     *
     * @param sessionTrackingModes the session tracking modes.
     */
    void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes);
}
