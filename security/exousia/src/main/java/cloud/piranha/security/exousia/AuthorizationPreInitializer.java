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
package cloud.piranha.security.exousia;

import static cloud.piranha.security.exousia.AuthorizationPreFilter.localServletRequest;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;
import static javax.servlet.annotation.ServletSecurity.TransportGuarantee.CONFIDENTIAL;
import static javax.servlet.annotation.ServletSecurity.TransportGuarantee.NONE;

import java.security.Permission;
import java.security.Policy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyContextException;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletSecurityElement;
import javax.servlet.annotation.ServletSecurity;

import org.omnifaces.exousia.AuthorizationService;
import org.omnifaces.exousia.constraints.SecurityConstraint;
import org.omnifaces.exousia.constraints.WebResourceCollection;
import org.omnifaces.exousia.constraints.transformer.ElementsToConstraintsTransformer;

import cloud.piranha.webapp.impl.DefaultAuthenticatedIdentity;
import cloud.piranha.servlet.webxml.WebXml;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.servlet.webxml.WebXmlManager;

/**
 * The Exousia initializer.
 *
 * @author Arjan Tijms
 */
public class AuthorizationPreInitializer implements ServletContainerInitializer {

    public static final String AUTHZ_SERVICE = AuthorizationPreInitializer.class.getName() + ".authz.service";

    public static final String AUTHZ_FACTORY_CLASS = AuthorizationPreInitializer.class.getName() + ".authz.factory.class";
    public static final String AUTHZ_POLICY_CLASS = AuthorizationPreInitializer.class.getName() + ".authz.module.class";

    public static final String UNCHECKED_PERMISSIONS = AuthorizationPreInitializer.class.getName() + ".unchecked.permissions";
    public static final String PERROLE_PERMISSIONS = AuthorizationPreInitializer.class.getName() + ".perrole.permissions";
    public static final String CONSTRAINTS = AuthorizationPreInitializer.class.getName() + ".constraints";
    public static final String SECURITY_ELEMENTS = AuthorizationPreInitializer.class.getName() + ".security.elements";
    public static final String SECURITY_ANNOTATIONS = AuthorizationPreInitializer.class.getName() + ".security.annotations";

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

        // Gets the authorization module classes that were configured externally
        Class<?> factoryClass = getAttribute(servletContext, AUTHZ_FACTORY_CLASS);
        Class<? extends Policy> policyClass = getAttribute(servletContext, AUTHZ_POLICY_CLASS);

        // Create the main Exousia authorization service, which implements the various entry points (an SPI)
        // for a runtime to make use of Jakarta Authorization
        AuthorizationService authorizationService = new AuthorizationService(
                factoryClass,
                policyClass, context.getServletContextId(),
                () -> localServletRequest.get(),
                () -> DefaultAuthenticatedIdentity.getCurrentSubject(),
                new PiranhaPrincipalMapper());

        // Join together in one list the constraints set by the servlet security elements, and the
        // piranha specific security constraint
        List<SecurityConstraint> securityConstraints = join(
                getConstraintsFromSecurityElements(servletContext, authorizationService),
                getConstraintsFromSecurityAnnotations(servletContext, authorizationService),
                getOptionalAttribute(servletContext, CONSTRAINTS),
                getConstraintsFromWebXMl(context));

        if (hasPermissionsSet(context)) {
            setPermissions(context, authorizationService);
        } else {
            authorizationService.addConstraintsToPolicy(
                    securityConstraints != null ? securityConstraints : emptyList(),
                    emptySet(),
                    isDenyUncoveredHttpMethods(context),
                    context.getServletRegistrations().keySet());
        }

        servletContext.setAttribute(AUTHZ_SERVICE, authorizationService);
        servletContext.addFilter(AuthorizationPreFilter.class.getSimpleName(), AuthorizationPreFilter.class);

        // TMP - should use Dynamic
        context.addFilterMapping(AuthorizationPreFilter.class.getSimpleName(), "/*");
    }

    private boolean isDenyUncoveredHttpMethods(WebApplication webApp) throws ServletException {
        return webApp.getDenyUncoveredHttpMethods();
    }

    public static void addToRole(PolicyConfiguration policyConfiguration, String role, Permission permission) {
        try {
            policyConfiguration.addToRole(role, permission);
        } catch (PolicyContextException e) {
            throw new IllegalStateException(e);
        }
    }

    public <T> T getAttribute(ServletContext servletContext, String name) throws ServletException {
        T t = getOptionalAttribute(servletContext, name);
        if (t == null) {
            throw new ServletException("Attribute " + name + " not specified");
        }

        return t;
    }

    public <T> T getOptionalAttribute(ServletContext servletContext, String name) throws ServletException {
        @SuppressWarnings("unchecked")
        T t = (T) servletContext.getAttribute(name);

        return t;
    }

    public List<SecurityConstraint> getConstraintsFromSecurityElements(ServletContext servletContext, AuthorizationService authorizationService) throws ServletException {
        List<Entry<List<String>, ServletSecurityElement>> elements = getOptionalAttribute(servletContext, SECURITY_ELEMENTS);
        if (elements == null) {
            return null;
        }

        List<SecurityConstraint> constraints = new ArrayList<>();

        for (Entry<List<String>, ServletSecurityElement> elementEntry : elements) {
            constraints.addAll(ElementsToConstraintsTransformer.createConstraints(
                    new HashSet<>(elementEntry.getKey()),
                    elementEntry.getValue()));
        }

        return constraints;
    }

    public List<SecurityConstraint> getConstraintsFromSecurityAnnotations(ServletContext servletContext, AuthorizationService authorizationService) throws ServletException {
        List<Entry<List<String>, ServletSecurity>> elements = getOptionalAttribute(servletContext, SECURITY_ANNOTATIONS);
        if (elements == null) {
            return null;
        }

        List<SecurityConstraint> constraints = new ArrayList<>();

        for (Entry<List<String>, ServletSecurity> elementEntry : elements) {
            constraints.addAll(ElementsToConstraintsTransformer.createConstraints(
                    new HashSet<>(elementEntry.getKey()),
                    elementEntry.getValue()));
        }

        return constraints;
    }

    public List<SecurityConstraint> getConstraintsFromWebXMl(WebApplication context) throws ServletException {
        WebXmlManager manager = (WebXmlManager) context.getAttribute(WebXmlManager.KEY);
        WebXml webXml = (WebXml) manager.getWebXml();
        
        if (webXml == null || webXml.securityConstraints == null) {
            return null;
        }

        List<SecurityConstraint> constraints = new ArrayList<>();

        for (WebXml.SecurityConstraint xmlConstraint : webXml.securityConstraints) {

            List<WebResourceCollection> webResourceCollections = new ArrayList<>();
            for (WebXml.SecurityConstraint.WebResourceCollection xmlCollection : xmlConstraint.webResourceCollections) {
                webResourceCollections.add(new WebResourceCollection(
                        xmlCollection.urlPatterns,
                        xmlCollection.httpMethods,
                        xmlCollection.httpMethodOmissions));
            }

            constraints.add(new SecurityConstraint(
                    webResourceCollections,
                    new HashSet<>(xmlConstraint.roleNames),
                    "confidential".equalsIgnoreCase(xmlConstraint.transportGuarantee)
                    ? CONFIDENTIAL : NONE));

        }

        return constraints;
    }

    public List<SecurityConstraint> join(List<SecurityConstraint> constraintsA, List<SecurityConstraint> constraintsB, List<SecurityConstraint> constraintsC, List<SecurityConstraint> constraintsD) {
        return join(join(constraintsA, constraintsB, constraintsC), constraintsD);
    }

    public List<SecurityConstraint> join(List<SecurityConstraint> constraintsA, List<SecurityConstraint> constraintsB, List<SecurityConstraint> constraintsC) {
        return join(join(constraintsA, constraintsB), constraintsC);
    }

    public List<SecurityConstraint> join(List<SecurityConstraint> constraintsA, List<SecurityConstraint> constraintsB) {
        if (constraintsA == null && constraintsB == null) {
            return null;
        }

        if (constraintsA != null && constraintsB != null) {
            return concat(constraintsA.stream(), constraintsB.stream()).collect(toList());
        }

        if (constraintsA != null) {
            return constraintsA;
        }

        return constraintsB;
    }

    public boolean hasPermissionsSet(ServletContext servletContext) throws ServletException {
        return getOptionalAttribute(servletContext, UNCHECKED_PERMISSIONS) != null
                || getOptionalAttribute(servletContext, PERROLE_PERMISSIONS) != null;
    }

    public void setPermissions(ServletContext servletContext, AuthorizationService authorizationService) throws ServletException {
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

            // TODO: Move commit moment to after all ServletContainerInitializer, Filters and Servlets have initialized
            policyConfiguration.commit();
        } catch (PolicyContextException e) {
            throw new IllegalStateException(e);
        }
    }

}
