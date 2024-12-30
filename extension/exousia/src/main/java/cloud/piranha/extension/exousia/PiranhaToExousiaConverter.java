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
package cloud.piranha.extension.exousia;

import cloud.piranha.core.api.SecurityManager;
import cloud.piranha.core.api.SecurityWebResourceCollection;
import cloud.piranha.core.api.WebApplication;
import jakarta.servlet.ServletSecurityElement;
import jakarta.servlet.annotation.ServletSecurity;
import static jakarta.servlet.annotation.ServletSecurity.TransportGuarantee.CONFIDENTIAL;
import static jakarta.servlet.annotation.ServletSecurity.TransportGuarantee.NONE;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.glassfish.exousia.constraints.SecurityConstraint;
import org.glassfish.exousia.constraints.WebResourceCollection;
import org.glassfish.exousia.constraints.transformer.ElementsToConstraintsTransformer;
import org.glassfish.exousia.mapping.SecurityRoleRef;

/**
 * This class converts from Piranha security types to Exousia security types.
 *
 * @author arjan
 *
 */
public class PiranhaToExousiaConverter {

    /**
     * Get the security constraints from security elements.
     *
     * @param elements the security elements.
     * @return the security constraints.
     */
    public List<SecurityConstraint> getConstraintsFromSecurityElements(List<Entry<List<String>, ServletSecurityElement>> elements) {
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
     * Get the security constraints from annotations.
     *
     * @param elements the elements.
     * @return the security constraints.
     */
    public List<SecurityConstraint> getConstraintsFromSecurityAnnotations(List<Entry<List<String>, ServletSecurity>> elements) {
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
     * Get the security constraints from the security manager.
     *
     * @param securityManager the security manager.
     * @return the security constraints.
     */
    public List<SecurityConstraint> getConstraintsFromSecurityManager(SecurityManager securityManager) {

        List<SecurityConstraint> constraints = new ArrayList<>();

        for (cloud.piranha.core.api.SecurityConstraint xmlConstraint
                : securityManager.getSecurityConstraints()) {

            List<WebResourceCollection> webResourceCollections = new ArrayList<>();
            for (SecurityWebResourceCollection xmlCollection : xmlConstraint.getSecurityWebResourceCollections()) {
                webResourceCollections.add(new WebResourceCollection(
                        xmlCollection.getUrlPatterns(),
                        xmlCollection.getHttpMethods(),
                        xmlCollection.getHttpMethodOmissions()));
            }

            constraints.add(new SecurityConstraint(
                    webResourceCollections,
                    new HashSet<>(xmlConstraint.getRoleNames()),
                    "confidential".equalsIgnoreCase(xmlConstraint.getTransportGuarantee())
                    ? CONFIDENTIAL : NONE));

        }

        return constraints;
    }

    /**
     * Get the security role refs from the web application.
     *
     * @param servletNames the servlet names.
     * @param webApplication the web application.
     * @return the security role refs.
     */
    public Map<String, List<SecurityRoleRef>> getSecurityRoleRefsFromSecurityManager(Set<String> servletNames, WebApplication webApplication) {
        Map<String, List<SecurityRoleRef>> securityRoleRefs = new HashMap<>();

        SecurityManager securityManager = webApplication.getManager().getSecurityManager();
        for (String servletName : servletNames) {
            List<SecurityRoleRef> securityRoleRefList = new ArrayList<>();
            if (securityManager.getSecurityRoleReferences().get(servletName) != null) {
                securityManager.getSecurityRoleReferences().get(servletName).forEach(
                        roleReference -> {
                            SecurityRoleRef securityRole = new SecurityRoleRef(
                                    roleReference.getRoleName(), 
                                    roleReference.getRoleLink());
                            securityRoleRefList.add(securityRole);
                        }
                );
            }
            securityRoleRefs.put(servletName, securityRoleRefList);
        }

        return securityRoleRefs;
    }
}
