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
package cloud.piranha.extension.servletannotations;

import cloud.piranha.core.api.AnnotationInfo;
import cloud.piranha.core.api.AnnotationManager;
import cloud.piranha.core.api.WebApplication;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRegistration.Dynamic;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.WARNING;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.EnumSet.noneOf;
import java.util.EventListener;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import static java.util.stream.Collectors.toCollection;

/**
 * The standard Servlet annotations initializer.
 *
 * @author Arjan Tijms
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletAnnotationsInitializer implements ServletContainerInitializer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(ServletAnnotationsInitializer.class.getName());

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        WebApplication webApp = (WebApplication) servletContext;

        if (!webApp.isMetadataComplete()) {

            AnnotationManager annotationManager = webApp.getManager().getAnnotationManager();

            // Process @WebServlet
            for (AnnotationInfo<WebServlet> annotationInfo : annotationManager.getAnnotations(WebServlet.class)) {

                WebServlet webServlet = annotationInfo.getInstance();

                String servletName = webServlet.name();
                if ("".equals(servletName)) {
                    servletName = annotationInfo.getTargetType().getName(); // WebServlet only has target Type
                }

                // Add the Servlet
                ServletRegistration registration = webApp.addServlet(servletName, annotationInfo.getTargetType().getName());

                if (registration == null) {
                    registration = webApp.getServletRegistration(servletName);
                }

                // Add params
                if (webServlet.initParams().length != 0) {
                    final ServletRegistration finalServletRegistration = registration;
                    stream(webServlet.initParams()).forEach(
                            initParam -> finalServletRegistration.setInitParameter(initParam.name(), initParam.value()));
                }

                if (registration instanceof Dynamic dynamic) {
                    dynamic.setAsyncSupported(webServlet.asyncSupported());
                }

                String[] urlPatterns = webServlet.value();
                if (urlPatterns.length == 0) {
                    urlPatterns = webServlet.urlPatterns();
                }

                // Add mapping
                if (registration != null) {
                    registration.addMapping(urlPatterns);
                }
            }

            // Process @WebFilter
            for (AnnotationInfo<WebFilter> annotationInfo : annotationManager.getAnnotations(WebFilter.class)) {

                WebFilter webFilter = annotationInfo.getInstance();

                String filterName = webFilter.filterName();
                if ("".equals(filterName)) {
                    filterName = annotationInfo.getTargetType().getName(); // WebServlet only has target Type
                }

                // Add the Filter
                FilterRegistration.Dynamic registration = webApp.addFilter(filterName, annotationInfo.getTargetType().getName());

                // Add params
                if (webFilter.initParams().length != 0) {
                    stream(webFilter.initParams()).forEach(initParam
                            -> registration.setInitParameter(initParam.name(), initParam.value())
                    );
                }

                if (registration != null) {
                    registration.setAsyncSupported(webFilter.asyncSupported());
                }

                String[] urlPatterns = webFilter.value();
                if (urlPatterns.length == 0) {
                    urlPatterns = webFilter.urlPatterns();
                }

                // Add mapping for URL patterns, if any
                if (registration != null && urlPatterns.length > 0) {
                    registration.addMappingForUrlPatterns(null, false, urlPatterns);
                }

                // Add mapping for Servlet names, if any
                if (webFilter.servletNames().length > 0) {
                    registration.addMappingForServletNames(
                            stream(webFilter.dispatcherTypes())
                                    .collect(toCollection(() -> noneOf(DispatcherType.class))),
                            true,
                            webFilter.servletNames());
                }
            }

            // Process @MultipartConfig
            // This assumes all applicable Servlets have been registered, either via web.xml, annotations or programmatically prior to this point.
            for (AnnotationInfo<MultipartConfig> annotationInfo : annotationManager.getAnnotations(MultipartConfig.class)) {
                for (ServletRegistration servletRegistration : servletContext.getServletRegistrations().values()) {
                    if (servletRegistration instanceof Dynamic dynamicRegistration) {
                        Class<?> targetType = annotationInfo.getTargetType();
                        if (targetType != null && targetType.getName().equals(servletRegistration.getClassName())) {
                            dynamicRegistration.setMultipartConfig(new MultipartConfigElement(annotationInfo.getInstance()));
                        }
                    }
                }
            }

            // Process @ServletSecurity
            List<Entry<List<String>, ServletSecurity>> securityAnnotations = new ArrayList<>();
            for (AnnotationInfo<ServletSecurity> annotationInfo : annotationManager.getAnnotations(ServletSecurity.class)) {

                Class<?> servlet = getTargetServlet(annotationInfo);

                // Take into account mixed mapped (annotation + web.xml later)
                WebServlet webServlet = servlet.getAnnotation(WebServlet.class);

                if (webServlet != null) {
                    String[] urlPatterns = webServlet.value();
                    if (urlPatterns.length == 0) {
                        urlPatterns = webServlet.urlPatterns();
                    }

                    securityAnnotations.add(new SimpleImmutableEntry<>(
                            asList(urlPatterns),
                            annotationInfo.getInstance()));
                } else {
                    LOGGER.log(WARNING,
                            "@ServletSecurity encountered on Servlet " + servlet
                            + "but no @WebServlet encountered");
                }
            }
            webApp.setAttribute(
                    "cloud.piranha.authorization.exousia.AuthorizationPreInitializer.security.annotations",
                    securityAnnotations
            );

            // Collect the roles from various annotations
            for (AnnotationInfo<RolesAllowed> rolesAllowedInfo : annotationManager.getAnnotations(RolesAllowed.class)) {
                webApp.declareRoles(rolesAllowedInfo.getInstance().value());
            }

            for (AnnotationInfo<DeclareRoles> declareRolesInfo : annotationManager.getAnnotations(DeclareRoles.class)) {
                webApp.declareRoles(declareRolesInfo.getInstance().value());
            }

            // Process @WebListener
            for (AnnotationInfo<WebListener> annotationInfo : annotationManager.getAnnotations(WebListener.class)) {
                webApp.addListener(getTargetListener(annotationInfo));
            }

        }
        webApp.initializeDeclaredFinish();
    }

    @SuppressWarnings("unchecked")
    private Class<? extends EventListener> getTargetListener(AnnotationInfo<WebListener> annotationInfo) {
        return (Class<EventListener>) annotationInfo.getTargetType();
    }

    @SuppressWarnings("unchecked")
    private Class<? extends HttpServlet> getTargetServlet(AnnotationInfo<ServletSecurity> annotationInfo) {
        return (Class<HttpServlet>) annotationInfo.getTargetType();
    }
}
