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
package jakarta.servlet;

import jakarta.servlet.descriptor.JspConfigDescriptor;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

/**
 * A Test ServletContext.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestServletContext implements ServletContext {

    public TestServletContext() {
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletRegistration.Dynamic addJspFile(String servletName, String jspFile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addListener(String className) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends EventListener> void addListener(T listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void declareRoles(String... roles) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getAttribute(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ClassLoader getClassLoader() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletContext getContext(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getEffectiveMajorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getEffectiveMinorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getInitParameter(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMajorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMimeType(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMinorVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRequestCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getResponseCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Servlet getServlet(String name) throws ServletException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServletContextName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration<String> getServletNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSessionTimeout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getVirtualServerName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(Exception exception, String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void log(String message, Throwable throwable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeAttribute(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttribute(String name, Object object) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRequestCharacterEncoding(String requestCharacterEncoding) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSessionTimeout(int sessionTimeout) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
