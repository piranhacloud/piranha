/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.exousia;

import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.CONSTRAINTS;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.PERROLE_PERMISSIONS;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.SECURITY_ANNOTATIONS;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.SECURITY_ELEMENTS;
import static cloud.piranha.extension.exousia.AuthorizationPreInitializer.UNCHECKED_PERMISSIONS;
import static java.util.Collections.disjoint;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;
import static org.glassfish.exousia.constraints.SecurityConstraint.join;

import java.security.Permission;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.glassfish.exousia.AuthorizationService;
import org.glassfish.exousia.constraints.SecurityConstraint;
import org.glassfish.exousia.constraints.WebResourceCollection;
import org.glassfish.exousia.mapping.SecurityRoleRef;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebXmlManager;
import jakarta.security.jacc.PolicyConfiguration;
import jakarta.security.jacc.PolicyContextException;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * The Exousia initializer.
 *
 * @author Arjan Tijms
 */
public class AuthorizationPostInitializer implements ServletContainerInitializer {

    /**
     * Stores the Piranha to Exousia converter.
     */
    PiranhaToExousiaConverter piranhaToExousiaConverter = new PiranhaToExousiaConverter();

    /**
     * Initialize Exousia
     *
     * @param classes the classes.
     * @param servletContext the Servlet context.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        WebApplication context = (WebApplication) servletContext;

        // Get the main (Exousia) authorization service, which implements the various entry points (an SPI)
        // for a runtime to make use of Jakarta Authorization
        AuthorizationService authorizationService = getAuthorizationService(context);

        // Join together in one list the constraints set by the servlet security elements, and the
        // piranha specific security constraint
        List<SecurityConstraint> securityConstraints = getAllScurityConstraints(context);

        for (SecurityConstraint securityConstraint : securityConstraints) {
            context.getManager().getSecurityManager().declareRoles(securityConstraint.getRolesAllowed());
        }

        if (hasPermissionsSet(context)) {
            setPermissions(context, authorizationService);
        } else {
            setConstraints(context, authorizationService, securityConstraints);
        }

        authorizationService.commitPolicy();

        addAuthorizationPreFilter(context);
    }


    // ### Private methods


    private AuthorizationService getAuthorizationService(WebApplication context) throws ServletException {
        return getAttribute(context, AuthorizationPreInitializer.AUTHZ_SERVICE);
    }

    private List<SecurityConstraint> getAllScurityConstraints(WebApplication context) throws ServletException {
        List<SecurityConstraint> webXmlConstraints = getConstraintsFromWebXml(context);
        List<SecurityConstraint> annotationConstraints = filterAnnotatedConstraints(
                webXmlConstraints,
                getConstraintsFromSecurityAnnotations(context));

        List<SecurityConstraint> allScurityConstraints = join(
            getConstraintsFromSecurityElements(context),
            annotationConstraints,
            getOptionalAttribute(context, CONSTRAINTS),
            webXmlConstraints);

        if (allScurityConstraints == null) {
            return emptyList();
        }

        return allScurityConstraints;
    }

    private void addAuthorizationPreFilter(WebApplication context) {
        FilterRegistration.Dynamic dynamic = context.addFilter(AuthorizationPreFilter.class.getSimpleName(), AuthorizationPreFilter.class);
        dynamic.setAsyncSupported(true);
        context.addFilterMapping(AuthorizationPreFilter.class.getSimpleName(), "/*");
    }

    private List<SecurityConstraint> filterAnnotatedConstraints(List<SecurityConstraint> webXmlConstraints, List<SecurityConstraint> annotationConstraints) {
        if (isAnyNull(webXmlConstraints, annotationConstraints)) {
            return annotationConstraints;
        }

        // Servlet Spec 13.4.1
        //
        // When a security-constraint in the portable deployment descriptor includes a url-pattern
        // that is an exact match for a pattern mapped to a class annotated with @ServletSecurity,
        // the annotation must have no effect on the constraints enforced by the Servlet container
        // on the pattern.

        // Index all URL patterns in web.xml
        Set<String> webXmlUrlPatterns = webXmlConstraints
            .stream()
            .flatMap(e -> e.getWebResourceCollections().stream())
            .flatMap(e -> e.getUrlPatterns().stream())
            .collect(toSet())
            ;

        List<SecurityConstraint> filteredAnnotationConstraints = new ArrayList<>();
        for (SecurityConstraint annotationConstraint : annotationConstraints) {
            List<WebResourceCollection> webResourceCollections = new ArrayList<>();
            for (WebResourceCollection webResourceCollection : annotationConstraint.getWebResourceCollections()) {
                WebResourceCollection newWebResourceCollection = webResourceCollection;
                if (!disjoint(webXmlUrlPatterns, webResourceCollection.getUrlPatterns())) {
                    Set<String> complementPatterns = new HashSet<>(webResourceCollection.getUrlPatterns());
                    complementPatterns.removeAll(webXmlUrlPatterns);

                    if (complementPatterns.isEmpty()) {
                        newWebResourceCollection = null;
                    } else {
                        newWebResourceCollection = new WebResourceCollection(
                            complementPatterns,
                            webResourceCollection.getHttpMethods(),
                            webResourceCollection.getHttpMethodOmissions());
                    }
                }

                if (newWebResourceCollection != null) {
                    webResourceCollections.add(newWebResourceCollection);
                }
            }

            if (!webResourceCollections.isEmpty()) {
                filteredAnnotationConstraints.add(new SecurityConstraint(
                        webResourceCollections,
                        annotationConstraint.getRolesAllowed(),
                        annotationConstraint.getTransportGuarantee()));
            }
        }

        return filteredAnnotationConstraints;
    }

    /**
     * Get the security constraints from security elements.
     *
     * @param servletContext the Servlet context.
     * @return the list of security constraints.
     */
    private List<SecurityConstraint> getConstraintsFromSecurityElements(ServletContext servletContext) {
        return piranhaToExousiaConverter.getConstraintsFromSecurityElements(getOptionalAttribute(servletContext, SECURITY_ELEMENTS));
    }

    /**
     * Get the security constraints from annotations.
     *
     * @param servletContext the Servlet context.
     * @return the list of security constraints.
     */
    private List<SecurityConstraint> getConstraintsFromSecurityAnnotations(ServletContext servletContext) {
        return piranhaToExousiaConverter.getConstraintsFromSecurityAnnotations(getOptionalAttribute(servletContext, SECURITY_ANNOTATIONS));
    }

    /**
     * Get security constraints from web.xml.
     *
     * @param webApplication the web application.
     * @return the list of security constraints.
     * @throws ServletException when a Servlet error occurs.
     */
    private List<SecurityConstraint> getConstraintsFromWebXml(WebApplication webApplication) throws ServletException {
        WebXmlManager manager = webApplication.getManager().getWebXmlManager();
        return piranhaToExousiaConverter.getConstraintsFromWebXml(manager.getWebXml());
    }

    /**
     * Get the security role refs from web.xml.
     *
     * @param webApplication the web application.
     * @return the map of security role refs.
     * @throws ServletException when a Servlet error occurs.
     */
    public Map<String, List<SecurityRoleRef>> getSecurityRoleRefsFromWebXml(WebApplication webApplication) throws ServletException {
        WebXmlManager manager = webApplication.getManager().getWebXmlManager();
        return piranhaToExousiaConverter.getSecurityRoleRefsFromWebXml(webApplication.getServletRegistrations().keySet(), manager.getWebXml());
    }




    private boolean hasPermissionsSet(ServletContext servletContext) {
        return getOptionalAttribute(servletContext, UNCHECKED_PERMISSIONS) != null
                || getOptionalAttribute(servletContext, PERROLE_PERMISSIONS) != null;
    }

    private void setPermissions(ServletContext servletContext, AuthorizationService authorizationService) {
        // Add permissions to the policy configuration, which is the repository that the policy (authorization module)
        // uses
        PolicyConfiguration policyConfiguration = authorizationService.getPolicyConfiguration();

        try {
            List<Permission> unchecked = getOptionalAttribute(servletContext, UNCHECKED_PERMISSIONS);
            if (unchecked != null) {
                for (Permission permission : unchecked) {
                    policyConfiguration.addToUncheckedPolicy(permission);
                }
            }

            List<Entry<String, Permission>> perRole = getOptionalAttribute(servletContext, PERROLE_PERMISSIONS);
            if (perRole != null) {
                for (Entry<String, Permission> perRoleEntry : perRole) {
                    policyConfiguration.addToRole(perRoleEntry.getKey(), perRoleEntry.getValue());
                }
            }
        } catch (PolicyContextException e) {
            throw new IllegalStateException(e);
        }
    }

    private void setConstraints(WebApplication context, AuthorizationService authorizationService, List<SecurityConstraint> securityConstraints) throws ServletException {
        authorizationService.addConstraintsToPolicy(
            securityConstraints,
            context.getManager().getSecurityManager().getRoles(),
            context.getManager().getSecurityManager().getDenyUncoveredHttpMethods(),
            getSecurityRoleRefsFromWebXml(context));
    }



    // ### Utility methods

    private static <T> T getAttribute(ServletContext servletContext, String name) throws ServletException {
        T t = getOptionalAttribute(servletContext, name);
        if (t == null) {
            throw new ServletException("Attribute " + name + " not specified");
        }

        return t;
    }

    private static  <T> T getOptionalAttribute(ServletContext servletContext, String name) {
        @SuppressWarnings("unchecked")
        T t = (T) servletContext.getAttribute(name);

        return t;
    }

    private static boolean isAnyNull(Object... values) {
        for (Object value : values) {
            if (value == null) {
                return true;
            }
        }

        return false;
    }

}