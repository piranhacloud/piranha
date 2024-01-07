/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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

import static jakarta.servlet.DispatcherType.REQUEST;
import static jakarta.servlet.http.MappingMatch.DEFAULT;
import static jakarta.servlet.http.MappingMatch.EXACT;
import static jakarta.servlet.http.MappingMatch.EXTENSION;
import static jakarta.servlet.http.MappingMatch.PATH;
import static java.util.Collections.reverse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cloud.piranha.core.api.FilterEnvironment;
import cloud.piranha.core.api.FilterPriority;
import cloud.piranha.core.api.ServletEnvironment;
import cloud.piranha.core.api.WebApplicationRequestMapping;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;

/**
 * The invocation finder tries to find a servlet invocation matching a request
 * for a path based or name based dispatch.
 *
 * <p>
 * Invocations returned by this finder take into account the various mappings,
 * filters, welcome files and the default servlet.
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

                if (servletInvocation == null) {
                    String servletPathLower = servletPath != null ? servletPath.toLowerCase() : "";
                    if (servletPathLower.startsWith("/web-inf") || servletPathLower.startsWith("/meta-inf")) {
                        servletInvocation = getDefaultServletInvocation(null, null);
                    } else {
                        servletInvocation = getDefaultServletInvocation(servletPath, pathInfo);
                    }
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

        List<FilterEnvironment> filterEnvironments =
            findFilterEnvironments(
                dispatcherType,
                // Look at the servletInvocation if there is one, as a welcome file can be set
                // that differs from the request path
                servletInvocation == null ? servletPath : servletInvocation.getServletPath(),
                servletInvocation == null ? pathInfo : servletInvocation.getPathInfo(),
                servletInvocation == null ? null : servletInvocation.getServletName());

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
        servletInvocation.setFromNamed(true);

        return servletInvocation;
    }

    private DefaultServletInvocation getDirectServletInvocationByPath(String servletPath, String pathInfo) {
        String path = addOrRemoveSlashIfNeeded(servletPath + (pathInfo == null ? "" : pathInfo));

        WebApplicationRequestMapping mapping = webApplication.webApplicationRequestMapper.findServletMapping(path);
        if (mapping == null) {
            return null;
        }

        String servletName = webApplication.webApplicationRequestMapper.getServletName(mapping.getPattern());
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

        servletInvocation.getHttpServletMapping().setMappingMatch(
                mapping.isExact() ? EXACT
                : mapping.isExtension() ? EXTENSION
                : PATH);

        servletInvocation.getHttpServletMapping().setPattern(mapping.getPattern());
        servletInvocation.getHttpServletMapping().setServletName(servletName);
        servletInvocation.getHttpServletMapping().setMatchValue(mapping.getMatchValue());

        if (mapping.isExact()) {
            servletInvocation.setServletPath(path);
            servletInvocation.setPathInfo(null);
        } else if (!mapping.isExtension()) {
            servletInvocation.setServletPath(mapping.getPattern().substring(0, mapping.getPattern().length() - 2));
            String newPathInfo = path.substring(mapping.getPattern().length() - 2);
            servletInvocation.setPathInfo(newPathInfo.isEmpty() ? null : newPathInfo);
        } else {
            servletInvocation.setServletPath(servletPath);
            servletInvocation.setPathInfo(pathInfo);
        }

        return servletInvocation;
    }

    private DefaultServletInvocation getWelcomeFileServletInvocation(String servletPath, String pathInfo) throws IOException {

        if (webApplication.getManager().getWelcomeFileManager() != null) {

            for (String welcomeFile : webApplication.getManager().getWelcomeFileManager().getWelcomeFileList()) {

                DefaultServletInvocation servletInvocation = null;

                if (isJsp(welcomeFile)) {
                    // .jsp files are special in the system, as they are mapped to a servlet, but also
                    // have to be present at exactly that path as static resource. Additionally we have
                    // the required index.jsp welcome file, that may not actually be there.
                    // So, .jsp files are treated as a servlet invocation but only if the static resource exists
                    if (isStaticResource(servletPath, pathInfo + welcomeFile)) {
                        servletInvocation = getDirectServletInvocationByPath(servletPath, pathInfo + welcomeFile);
                    }
                } else if (isStaticResource(servletPath, pathInfo + welcomeFile)) {
                    // If we have a welcome file, we can load it via the default servlet.
                    return getDefaultServletInvocation(servletPath, pathInfo + welcomeFile);
                } else {
                    // Try if we have a welcome servlet
                    servletInvocation = getDirectServletInvocationByPath(servletPath, pathInfo + welcomeFile);
                }

                if (servletInvocation != null) {
                    servletInvocation.setOriginalServletPath(servletPath);
                    return servletInvocation;
                }

            }

            // No static file, JSP or servlet
        }

        return null;
    }

    private boolean isStaticResource(String servletPath, String pathInfo) throws MalformedURLException {
        return webApplication.getResource(addOrRemoveSlashIfNeeded(servletPath + (pathInfo == null ? "" : pathInfo))) != null;
    }

    private static boolean isJsp(String welcomeFile) {
        return welcomeFile.endsWith(".jsp");
    }

    private String addOrRemoveSlashIfNeeded(String string) {
        if (string.startsWith("/")) {
            if (string.startsWith("//")) {
                return string.substring(1);
            }

            return string;
        }

        return "/" + string;
    }

    private DefaultServletInvocation getDefaultServletInvocation(String servletPath, String pathInfo) {
        ServletEnvironment servletEnvironment = null;

        String servletName = webApplication.webApplicationRequestMapper.getDefaultServlet();
        if (servletName != null) {
            servletEnvironment = webApplication.servletEnvironments.get(servletName);
        }

        if (servletEnvironment == null) {
            Servlet defaultServlet = webApplication.defaultServlet;
            if (defaultServlet == null) {
                defaultServlet = new DefaultServlet();
            }
            servletName = "default";
            servletEnvironment = new DefaultServletEnvironment(webApplication, servletName, defaultServlet);
        }

        DefaultServletInvocation servletInvocation = new DefaultServletInvocation();

        servletInvocation.setServletName(servletName);
        servletInvocation.setServletEnvironment(servletEnvironment);

        // 12.2
        // A string containing only the "/" character indicates the "default" servlet of the application.
        // In this case the servlet path is the request URI minus the context path and the path info is null.
        servletInvocation.setServletPath(servletPath == null ? null : addOrRemoveSlashIfNeeded(servletPath + (pathInfo == null ? "" : pathInfo)));
        servletInvocation.setInvocationPath(servletPath); // look at whether its really needed to have path and invocation path
        servletInvocation.setPathInfo(null);
        servletInvocation.getHttpServletMapping().setMappingMatch(DEFAULT);
        servletInvocation.getHttpServletMapping().setMatchValue("");
        servletInvocation.getHttpServletMapping().setPattern("/");
        servletInvocation.getHttpServletMapping().setServletName(servletName);

        return servletInvocation;
    }

    /**
     * Find the filter environments.
     *
     * @param dispatcherType the dispatcher type.
     * @param servletPath the servlet path to which filters should apply.
     * @param pathInfo the path info to which filters should apply.
     * @param servletName name of the servlet to be filtered, if any. Can be
     * null.
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
                .toList();

        List<FilterEnvironment> notPrioritisedFilters = filterEnvironments.stream()
                .filter(e -> !(e.getFilter() instanceof FilterPriority))
                .toList();

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
