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

import static java.util.Collections.enumeration;
import static java.util.Collections.list;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static jakarta.servlet.DispatcherType.ASYNC;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import cloud.piranha.webapp.api.AttributeManager;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.api.WebApplicationRequest;

/**
 * The async HTTP dispatch wrapper.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class AsyncHttpDispatchWrapper extends HttpServletRequestWrapper implements WebApplicationRequest {

    /**
     * Stores the servlet path.
     */
    private String servletPath;
    
    /**
     * Stores the path info.
     */
    private String pathInfo;
    
    /**
     * Stores the request URI.
     */
    private String requestURI;
    
    /**
     * Stores the query string.
     */
    private String queryString;

    /**
     * Stores the async context.
     */
    private AsyncContext asyncContext;
    
    /**
     * Stores the async started flag.
     */
    private boolean asyncStarted; // Note that asyncStarted is per async cycle, and resets when the request is dispatched

    /**
     * Stores the attribute manager.
     */
    private AttributeManager attributeManager = new DefaultAttributeManager();

    /**
     * Stores the wrapper attributes.
     */
    private List<String> wrapperAttributes = new ArrayList<>();
    
    /**
     * Stores the wrapper parameters.
     */
    private Map<String, String[]> wrapperParameters = new HashMap<>();

    /**
     * Constructor.
     * 
     * @param request the HTTP servlet request.
     */
    public AsyncHttpDispatchWrapper(HttpServletRequest request) {
        super(request);
        wrapperAttributes.add("piranha.response");
    }

    @Override
    public HttpServletRequest getRequest() {
        return (HttpServletRequest) super.getRequest();
    }

    @Override
    public DispatcherType getDispatcherType() {
        return ASYNC;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public String getPathInfo() {
        return pathInfo;
    }

    /**
     * Set the path info.
     * 
     * @param pathInfo the path info.
     */
    public void setPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    /**
     * Set the request URI.
     * 
     * @param requestURI the request URI.
     */
    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return startAsync(this, requireNonNull((ServletResponse) getAttribute("piranha.response")));
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        if (asyncContext != null) {
            throw new IllegalStateException("Async cycle has already been started");
        }

        servletRequest.setAttribute("CALLED_FROM_ASYNC_WRAPPER", "true");
        asyncContext = super.startAsync(servletRequest, servletResponse);
        asyncStarted = true;

        return asyncContext;
    }

    @Override
    public boolean isAsyncStarted() {
        return asyncStarted;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        HashSet<String> attributeNames = new HashSet<>();
        attributeNames.addAll(list(super.getAttributeNames()));
        attributeNames.addAll(wrapperAttributes);

        return enumeration(attributeNames);
    }

    @Override
    public Object getAttribute(String name) {
        if (wrapperAttributes.contains(name)) {
            return attributeManager.getAttribute(name);
        }

        return super.getAttribute(name);
    }

    @Override
    public void setAttribute(String name, Object object) {
        if (wrapperAttributes.contains(name)) {
            attributeManager.setAttribute(name, object);
        } else {
            super.setAttribute(name, object);
        }
    }

    @Override
    public void removeAttribute(String name) {
        if (wrapperAttributes.contains(name)) {
            attributeManager.removeAttribute(name);
        } else {
            super.removeAttribute(name);
        }
    }

    /**
     * Get the parameter.
     *
     * @param name the name.
     * @return the value.
     */
    @Override
    public String getParameter(String name) {
        if (getParameterValues(name) == null) {
            return null;
        }

        return getParameterValues(name)[0];
    }

    /**
     * Get the parameter map.
     *
     * @return the parameter map.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.putAll(super.getParameterMap());
        parameterMap.putAll(wrapperParameters);

        return unmodifiableMap(parameterMap);
    }

    /**
     * Get the parameter names.
     *
     * @return the parameter names.
     */
    @Override
    public Enumeration<String> getParameterNames() {
        HashSet<String> parameterNames = new HashSet<>();
        parameterNames.addAll(super.getParameterMap().keySet());
        parameterNames.addAll(wrapperParameters.keySet());

        return enumeration(parameterNames);
    }

    /**
     * Get the parameter values.
     *
     * @param name the parameter name.
     * @return the parameter values.
     */
    @Override
    public String[] getParameterValues(String name) {
        if (wrapperParameters.containsKey(name)) {
            return wrapperParameters.get(name);
        }

        return super.getParameterValues(name);
    }

    /**
     * Set the query string.
     * 
     * @param queryString the query string.
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    /**
     * Set as a wrapper attribute.
     * 
     * @param name the name.
     * @param value the value.
     */
    public void setAsWrapperAttribute(String name, Object value) {
        attributeManager.setAttribute(name, value);
    }

    /**
     * Get the wrapper attributes.
     * 
     * @return the wrapper attributes.
     */
    public List<String> getWrapperAttributes() {
        return wrapperAttributes;
    }

    /**
     * Get the wrapper parameters.
     * 
     * @return the wrapper parameters.
     */
    public Map<String, String[]> getWrapperParameters() {
        return wrapperParameters;
    }

    @Override
    public void setDispatcherType(DispatcherType dispatcherType) {

    }

    @Override
    public String toString() {
        return getRequestURIWithQueryString() + " " + super.toString();
    }

    /**
     * Get the request URI with query string.
     * 
     * @return the request URI with query string.
     */
    public String getRequestURIWithQueryString() {
        String requestURI = getRequestURI();
        String queryString = getQueryString();
        return queryString == null ? requestURI : requestURI + "?" + queryString;
    }

    @Override
    public void setContextPath(String contextPath) {
    }
    
    @Override
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    @Override
    public void setWebApplication(WebApplication webApplication) {
    }
}
