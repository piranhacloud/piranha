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
package cloud.piranha.security.exousia;

import static cloud.piranha.security.exousia.AuthorizationPreFilter.localServletRequest;
import static jakarta.servlet.annotation.ServletSecurity.TransportGuarantee.CONFIDENTIAL;
import static jakarta.servlet.annotation.ServletSecurity.TransportGuarantee.NONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.concat;

import java.security.Permission;
import java.security.Policy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import jakarta.security.jacc.PolicyConfiguration;
import jakarta.security.jacc.PolicyContextException;

import org.omnifaces.exousia.AuthorizationService;
import org.omnifaces.exousia.constraints.SecurityConstraint;
import org.omnifaces.exousia.constraints.WebResourceCollection;
import org.omnifaces.exousia.constraints.transformer.ElementsToConstraintsTransformer;

import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.impl.DefaultAuthenticatedIdentity;
import cloud.piranha.webapp.impl.WebXml;
import cloud.piranha.webapp.impl.WebXmlManager;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletSecurityElement;
import jakarta.servlet.annotation.ServletSecurity;

/**
 * The Exousia initializer.
 *
 * @author Arjan Tijms
 */
public class AuthorizationPreInitializer implements ServletContainerInitializer {

    /**
     * Defines the constant for the auth service attribute.
     */
    public static final String AUTHZ_SERVICE = AuthorizationPreInitializer.class.getName() + ".authz.service";

    /**
     * Defines the constant for the auth factory class attribute.
     */
    public static final String AUTHZ_FACTORY_CLASS = AuthorizationPreInitializer.class.getName() + ".authz.factory.class";
    
    /**
     * Defines the constant fore the auth policy class attribute.
     */
    public static final String AUTHZ_POLICY_CLASS = AuthorizationPreInitializer.class.getName() + ".authz.module.class";

    /**
     * Defines the constant for the unchecked permissions attribute.
     */
    public static final String UNCHECKED_PERMISSIONS = AuthorizationPreInitializer.class.getName() + ".unchecked.permissions";
    
    /**
     * Defines the constants for the per-role permissions attribute.
     */
    public static final String PERROLE_PERMISSIONS = AuthorizationPreInitializer.class.getName() + ".perrole.permissions";
    
    /**
     * Defines the constant for the security constraints attribute.
     */
    public static final String CONSTRAINTS = AuthorizationPreInitializer.class.getName() + ".constraints";
    
    /**
     * Defines the constant for the security elements attribute.
     */
    public static final String SECURITY_ELEMENTS = AuthorizationPreInitializer.class.getName() + ".security.elements";
    
    /**
     * Defines the constant for the security annotations attribute.
     */
    public static final String SECURITY_ANNOTATIONS = "cloud.piranha.authorization.exousia.AuthorizationPreInitializer.security.annotations";

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

    /**
     * Do we deny uncovered HTTP methods.
     * 
     * @param webApp the web application
     * @return true if we do, false otherwise.
     * @throws ServletException when a Servlet error occurs.
     */
    private boolean isDenyUncoveredHttpMethods(WebApplication webApp) throws ServletException {
        return webApp.getDenyUncoveredHttpMethods();
    }

    /**
     * Add to the role.
     * 
     * @param policyConfiguration the policy configuration.
     * @param role the role.
     * @param permission the permission.
     */
    public static void addToRole(PolicyConfiguration policyConfiguration, String role, Permission permission) {
        try {
            policyConfiguration.addToRole(role, permission);
        } catch (PolicyContextException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Get the attribute.
     * 
     * @param <T> the type.
     * @param servletContext the Servlet context.
     * @param name the name.
     * @return the value.
     * @throws ServletException when a Servlet error occurs.
     */
    public <T> T getAttribute(ServletContext servletContext, String name) throws ServletException {
        T t = getOptionalAttribute(servletContext, name);
        if (t == null) {
            throw new ServletException("Attribute " + name + " not specified");
        }

        return t;
    }

    /**
     * Get the optional attribute.
     * 
     * @param <T> the type.
     * @param servletContext the Servlet context.
     * @param name the name.
     * @return the value.
     * @throws ServletException when a  Servlet error occurs.
     */
    public <T> T getOptionalAttribute(ServletContext servletContext, String name) throws ServletException {
        @SuppressWarnings("unchecked")
        T t = (T) servletContext.getAttribute(name);

        return t;
    }

    /**
     * Get the security constraints from security elements.
     * 
     * @param servletContext the Servlet context.
     * @param authorizationService the authorization service.
     * @return the security constraints.
     * @throws ServletException when a Servlet error occurs.
     */
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

    /**
     * Get the security constraints from security annotations.
     * 
     * @param servletContext the Servlet context.
     * @param authorizationService the authorization service.
     * @return the security constraints.
     * @throws ServletException when a Servlet error occurs.
     */
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

    /**
     * Get the security constraints from web.xml.
     * 
     * @param context the web application.
     * @return the security constraints.
     * @throws ServletException when a Servlet error occurs.
     */
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

    /**
     * Join security constraints.
     * 
     * @param constraintsA constraints A.
     * @param constraintsB constraints B.
     * @param constraintsC constraints C.
     * @param constraintsD constraints D.
     * @return the security constraints.
     */
    public List<SecurityConstraint> join(List<SecurityConstraint> constraintsA, List<SecurityConstraint> constraintsB, List<SecurityConstraint> constraintsC, List<SecurityConstraint> constraintsD) {
        return join(join(constraintsA, constraintsB, constraintsC), constraintsD);
    }

    /**
     * Join security constraints.
     * 
     * @param constraintsA constraints A.
     * @param constraintsB constraints B.
     * @param constraintsC constraints C.
     * @return the security constraints.
     */   
    public List<SecurityConstraint> join(List<SecurityConstraint> constraintsA, List<SecurityConstraint> constraintsB, List<SecurityConstraint> constraintsC) {
        return join(join(constraintsA, constraintsB), constraintsC);
    }

    /**
     * Join security constraints.
     * 
     * @param constraintsA constraints A.
     * @param constraintsB constraints B.
     * @return the security constraints.
     */
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

    /**
     * Has the permission set.
     * 
     * @param servletContext the Servlet context.
     * @return true if the permission is set, false otherwise.
     * @throws ServletException when a Servlet error occurs.
     */
    public boolean hasPermissionsSet(ServletContext servletContext) throws ServletException {
        return getOptionalAttribute(servletContext, UNCHECKED_PERMISSIONS) != null
                || getOptionalAttribute(servletContext, PERROLE_PERMISSIONS) != null;
    }

    /**
     * Set the permissions.
     * 
     * @param servletContext the Servlet context.
     * @param authorizationService the authorization service.
     * @throws ServletException when a Servlet error occurs.
     */
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
