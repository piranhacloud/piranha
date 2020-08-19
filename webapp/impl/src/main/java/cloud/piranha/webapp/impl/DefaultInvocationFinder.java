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

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import cloud.piranha.webapp.api.FilterEnvironment;
import cloud.piranha.webapp.api.FilterPriority;
import cloud.piranha.webapp.api.ServletEnvironment;
import cloud.piranha.webapp.api.ServletInvocation;
import cloud.piranha.webapp.api.WebApplicationRequestMapping;

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

    DefaultWebApplication webApplication;

    public DefaultInvocationFinder(DefaultWebApplication webApplication) {
        this.webApplication = webApplication;
    }

    public ServletInvocation findServletInvocationByPath(String servletPath, String pathInfo) throws IOException, ServletException {
        DefaultServletInvocation servletInvocation = getDirectServletInvocationByPath(servletPath, pathInfo);

        if (servletInvocation == null) {
            servletInvocation = getWelcomeFileServletInvocation(servletPath, pathInfo != null ? pathInfo : "");
        }

        if (servletInvocation == null) {
            servletInvocation = getDefaultServletInvocation();
        }

        if (servletInvocation != null && servletInvocation.getServletEnvironment().getStatus() == ServletEnvironment.UNAVAILABLE) {
            throw new UnavailableException("Servlet is unavailable");
        }

        List<FilterEnvironment> filterEnvironments = findFilterEnvironments(servletPath, pathInfo, servletInvocation == null? null : servletInvocation.getServletName());
        if (filterEnvironments != null) {
            if (servletInvocation == null) {
                servletInvocation = new DefaultServletInvocation();
            }

            servletInvocation.setFilterEnvironments(filterEnvironments);
            servletInvocation.setFilterChain(findFilterChain(filterEnvironments, servletInvocation.getServletEnvironment()));
        }

        return servletInvocation;
    }

    public ServletInvocation findServletInvocationByName(String servletName) {
        ServletEnvironment servletEnvironment = webApplication.servletEnvironments.get(servletName);
        if (servletEnvironment == null) {
            return null;
        }

        DefaultServletInvocation servletInvocation = new DefaultServletInvocation();

        servletInvocation.setServletName(servletName);
        servletInvocation.setServletEnvironment(servletEnvironment);

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

                if (webApplication.getResource(webApplication.contextPath + servletPath + pathInfo + welcomeFile) != null) {

                    DefaultServletInvocation servletInvocation = getDefaultServletInvocation();
                    if (servletInvocation != null) {
                        servletInvocation.setServletPath(servletPath);
                        servletInvocation.setPathInfo(pathInfo + welcomeFile);
                        return servletInvocation;
                    }
                }
            }
        }

        // Next try if we have a welcome servlet

        for (String welcomeFile : webApplication.getWelcomeFileManager().getWelcomeFileList()) {
            DefaultServletInvocation servletInvocation = getDirectServletInvocationByPath(servletPath, pathInfo + welcomeFile);
            if (servletInvocation != null) {
                return servletInvocation;
            }
        }


        // No welcome file or servlet
        return null;
    }

    private DefaultServletInvocation getDefaultServletInvocation() {
        if (webApplication.defaultServlet == null) {
            return null;
        }

        DefaultServletInvocation servletInvocation = new DefaultServletInvocation();

        servletInvocation.setServletName("default");
        servletInvocation.setServletEnvironment(new DefaultServletEnvironment(webApplication, "default", webApplication.defaultServlet));

        return servletInvocation;
    }

    /**
     * Find the filter environments.
     *
     * @param servletPath the servlet path to which filters should apply.
     * @param pathInfo the path info to which filters should apply.
     * @param servletName name of the servlet to be filtered, if any. Can be null.
     *
     * @return the filter environments.
     */
    protected List<FilterEnvironment> findFilterEnvironments(String servletPath, String pathInfo, String servletName) {
        List<FilterEnvironment> filterEnvironments = null;

        String path = servletPath + (pathInfo == null ? "" : pathInfo);
        Collection<String> filterNames = webApplication.webApplicationRequestMapper.findFilterMappings(path);

        if (servletName != null) {
            String servletNamePath = "servlet:// " + servletName;
            filterNames.addAll(webApplication.webApplicationRequestMapper.findFilterMappings(servletNamePath));
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

    private FilterChain findFilterChain(List<FilterEnvironment> filterEnvironments, ServletEnvironment servletEnvironment) {
        List<FilterEnvironment> prioritisedFilters = filterEnvironments.stream()
                .filter(e -> e.getFilter() instanceof FilterPriority)
                .sorted((x, y) -> sortOnPriority(x, y))
                .collect(toList());

        List<FilterEnvironment> notPrioritisedFilters = filterEnvironments.stream()
                .filter(e -> e.getFilter() instanceof FilterPriority == false)
                .collect(toList());

        List<FilterEnvironment> currentEnvironments = new ArrayList<>();
        currentEnvironments.addAll(prioritisedFilters);
        currentEnvironments.addAll(notPrioritisedFilters);

        Collections.reverse(currentEnvironments);

        DefaultFilterChain downFilterChain = new DefaultFilterChain(servletEnvironment == null? null : servletEnvironment.getServlet());
        DefaultFilterChain upFilterChain;
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
