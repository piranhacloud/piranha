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
package cloud.piranha.servlet4.impl;

import static java.util.Collections.reverse;
import static java.util.stream.Collectors.toList;
import static javax.servlet.DispatcherType.REQUEST;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import cloud.piranha.servlet4.webapp.FilterEnvironment;
import cloud.piranha.servlet4.webapp.FilterPriority;
import cloud.piranha.servlet4.webapp.ServletEnvironment;
import cloud.piranha.servlet4.webapp.WebApplicationRequestMapping;

/**
 * The invocation finder tries to find a servlet invocation matching a request for a path based or name
 * based dispatch.
 *
 * <p>
 * Invocations returned by this finder take into account the various mappings, filters, welcome files
 * and the default servlet.
 *
 *
 * @author Arjan Tijms
 *
 */
public class DefaultInvocationFinder {

    /**
     * Stores the web application.
     */
    private final DefaultWebApplication webApplication;

    /**
     * Constructor.
     * 
     * @param webApplication the web application.
     */
    public DefaultInvocationFinder(DefaultWebApplication webApplication) {
        this.webApplication = webApplication;
    }

    /**
     * Find the servlet invocation by path.
     * 
     * @param servletPath the servlet path.
     * @param pathInfo the path info.
     * @return the servlet invocation.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    public DefaultServletInvocation findServletInvocationByPath(String servletPath, String pathInfo) throws IOException, ServletException {
        return findServletInvocationByPath(REQUEST, servletPath, pathInfo);
    }

    /**
     * Find the servlet invocation by path.
     * 
     * @param dispatcherType the dispatcher type.
     * @param servletPath the servlet path.
     * @param pathInfo the path info.
     * @return the servlet invocation.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    public DefaultServletInvocation findServletInvocationByPath(DispatcherType dispatcherType, String servletPath, String pathInfo) throws IOException, ServletException {
        DefaultServletInvocation servletInvocation = getDirectServletInvocationByPath(servletPath, pathInfo);

        if (servletInvocation == null) {
            if (dispatcherType == REQUEST) {
                servletInvocation = getWelcomeFileServletInvocation(servletPath, pathInfo != null ? pathInfo : "/");

                if (servletInvocation == null) { // TODO: access rules for WEB-INF
                    servletInvocation = getDefaultServletInvocation(servletPath, pathInfo);
                }
            } else {
                // Note: no WEB-INF checks needed here
                servletInvocation = getDefaultServletInvocation(servletPath, pathInfo);
            }
        }

        // Seed the chain with the servlet, if any. REQUEST dispatches can be done to only a filter so a servlet is not hard requirement
        servletInvocation.seedFilterChain();

        return addFilters(dispatcherType, servletInvocation, servletPath, pathInfo);
    }

    /**
     * Add the filters.
     * 
     * @param dispatcherType the dispatcher type.
     * @param servletInvocation the servlet invocation.
     * @param servletPath the servlet path.
     * @param pathInfo the path info.
     * @return the servlet invocation.
     */
    public DefaultServletInvocation addFilters(DispatcherType dispatcherType, DefaultServletInvocation servletInvocation, String servletPath, String pathInfo) {
        if (dispatcherType == null) {
            // If there's no dispatcher type, don't add filters. This can happen when the dispatch is not yet known
            // so as with the request dispatcher, which first gets the resource, and only after that gets to be used for a forward or include.
            return servletInvocation;
        }

        List<FilterEnvironment> filterEnvironments = findFilterEnvironments(dispatcherType, servletPath, pathInfo, servletInvocation == null? null : servletInvocation.getServletName());
        if (filterEnvironments != null) {
            if (servletInvocation == null) {
                servletInvocation = new DefaultServletInvocation();
                servletInvocation.setServletPath(servletPath);
                servletInvocation.setPathInfo(pathInfo);
            }

            servletInvocation.setFilterEnvironments(filterEnvironments);
            servletInvocation.setFilterChain(findFilterChain(filterEnvironments, servletInvocation.getFilterChain()));
        }

        return servletInvocation;
    }

    /**
     * Find the servlet invocation by servlet name.
     * 
     * @param servletName the servlet name.
     * @return the servlet invocation, or null if not found.
     */
    public DefaultServletInvocation findServletInvocationByName(String servletName) {
        ServletEnvironment servletEnvironment = webApplication.servletEnvironments.get(servletName);
        if (servletEnvironment == null) {
            return null;
        }

        DefaultServletInvocation servletInvocation = new DefaultServletInvocation();

        servletInvocation.setServletName(servletName);
        servletInvocation.setServletEnvironment(servletEnvironment);
        servletInvocation.seedFilterChain();

        return servletInvocation;
    }

    private DefaultServletInvocation getDirectServletInvocationByPath(String servletPath, String pathInfo) {
        String path = servletPath + (pathInfo == null ? "" : pathInfo);

        WebApplicationRequestMapping mapping = webApplication.webApplicationRequestMapper.findServletMapping(path);
        if (mapping == null) {
            return null;
        }

        String servletName = webApplication.webApplicationRequestMapper.getServletName(mapping.getPath());
        if (servletName == null) {
            return null;
        }

        ServletEnvironment servletEnvironment = webApplication.servletEnvironments.get(servletName);
        if (servletEnvironment == null) {
            return null;
        }

        DefaultServletInvocation servletInvocation = new DefaultServletInvocation();

        servletInvocation.setInvocationPath(path);
        servletInvocation.setApplicationRequestMapping(mapping);
        servletInvocation.setServletName(servletName);
        servletInvocation.setServletEnvironment(servletEnvironment);

        if (mapping.isExact()) {
            servletInvocation.setServletPath(path);
            servletInvocation.setPathInfo(null);
        } else if (!mapping.isExtension()) {
            servletInvocation.setServletPath(mapping.getPath().substring(0, mapping.getPath().length() - 2));
            servletInvocation.setPathInfo(path.substring(mapping.getPath().length() - 2));
        } else {
            servletInvocation.setServletPath(servletPath);
            servletInvocation.setPathInfo(pathInfo);
        }

        return servletInvocation;
    }

    private DefaultServletInvocation getWelcomeFileServletInvocation(String servletPath, String pathInfo) throws IOException {

        // Try if we have a welcome file that we can load via the default servlet

        if (webApplication.defaultServlet != null) {
            for (String welcomeFile : webApplication.getWelcomeFileManager().getWelcomeFileList()) {
                if (!isStaticResource(servletPath, pathInfo + welcomeFile))
                    continue;
                DefaultServletInvocation servletInvocation = getDefaultServletInvocation(servletPath, pathInfo + welcomeFile);
                return Objects.requireNonNullElseGet(servletInvocation, () -> getDefaultServletInvocation(servletPath, pathInfo + welcomeFile));
            }
        }

        // Next try if we have a welcome servlet

        for (String welcomeFile : webApplication.getWelcomeFileManager().getWelcomeFileList()) {
            if (!isStaticResource(servletPath, pathInfo + welcomeFile))
                continue;
            DefaultServletInvocation servletInvocation = getDirectServletInvocationByPath(servletPath, pathInfo + welcomeFile);
            return Objects.requireNonNullElseGet(servletInvocation, () -> getDefaultServletInvocation(servletPath, pathInfo + welcomeFile));
        }

        // No welcome file or servlet
        return null;
    }

    private boolean isStaticResource(String servletPath, String pathInfo) throws MalformedURLException {
        return webApplication.getResource(addSlashIfNeeded(servletPath + (pathInfo == null? "" : pathInfo))) != null;
    }

    private String addSlashIfNeeded(String string) {
        if (string.startsWith("/")) {
            return string;
        }

        return "/" + string;
    }

    private DefaultServletInvocation getDefaultServletInvocation(String servletPath, String pathInfo) {
        Servlet defaultServlet = webApplication.defaultServlet;
        if (defaultServlet == null) {
            defaultServlet = new DefaultServlet();
        }

        DefaultServletInvocation servletInvocation = new DefaultServletInvocation();

        servletInvocation.setServletName("default");
        servletInvocation.setServletEnvironment(new DefaultServletEnvironment(webApplication, "default", defaultServlet));
        servletInvocation.setServletPath(servletPath);
        servletInvocation.setPathInfo(pathInfo);
        servletInvocation.setInvocationPath(servletPath); // look at whether its really needed to have path and invocation path

        return servletInvocation;
    }

    /**
     * Find the filter environments.
     *
     * @param dispatcherType the dispatcher type.
     * @param servletPath the servlet path to which filters should apply.
     * @param pathInfo the path info to which filters should apply.
     * @param servletName name of the servlet to be filtered, if any. Can be null.
     *
     * @return the filter environments.
     */
    protected List<FilterEnvironment> findFilterEnvironments(DispatcherType dispatcherType, String servletPath, String pathInfo, String servletName) {
        List<FilterEnvironment> filterEnvironments = null;

        String path = servletPath + (pathInfo == null ? "" : pathInfo);
        Collection<String> filterNames = webApplication.webApplicationRequestMapper.findFilterMappings(dispatcherType, path);

        if (servletName != null) {
            String servletNamePath = "servlet:// " + servletName;
            filterNames.addAll(webApplication.webApplicationRequestMapper.findFilterMappings(dispatcherType, servletNamePath));
        }

        if (!filterNames.isEmpty()) {
            filterEnvironments = new ArrayList<>();
            for (String filterName : filterNames) {
                if (webApplication.filters.get(filterName) != null) {
                    filterEnvironments.add(webApplication.filters.get(filterName));
                }
            }
        }

        return filterEnvironments;
    }

    private FilterChain findFilterChain(List<FilterEnvironment> filterEnvironments, FilterChain initialFilterChain) {
        List<FilterEnvironment> prioritisedFilters = filterEnvironments.stream()
                .filter(e -> e.getFilter() instanceof FilterPriority)
                .sorted(this::sortOnPriority)
                .collect(toList());

        List<FilterEnvironment> notPrioritisedFilters = filterEnvironments.stream()
                .filter(e -> e.getFilter() instanceof FilterPriority == false)
                .collect(toList());

        List<FilterEnvironment> currentEnvironments = new ArrayList<>();
        currentEnvironments.addAll(prioritisedFilters);
        currentEnvironments.addAll(notPrioritisedFilters);

        reverse(currentEnvironments);

        FilterChain downFilterChain = initialFilterChain;
        FilterChain upFilterChain;
        for (FilterEnvironment filterEnvironment : currentEnvironments) {
            upFilterChain = new DefaultFilterChain(filterEnvironment.getFilter(), downFilterChain);
            downFilterChain = upFilterChain;
        }

        return downFilterChain;
    }

    private int sortOnPriority(FilterEnvironment x, FilterEnvironment y) {
        FilterPriority filterX = (FilterPriority) x.getFilter();
        FilterPriority filterY = (FilterPriority) y.getFilter();

        return Integer.compare(filterX.getPriority(), filterY.getPriority());
    }

}
