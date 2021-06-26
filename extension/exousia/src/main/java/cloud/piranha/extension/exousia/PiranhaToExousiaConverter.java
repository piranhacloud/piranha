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
package cloud.piranha.extension.exousia;

import static java.util.Collections.emptyList;
import static jakarta.servlet.annotation.ServletSecurity.TransportGuarantee.CONFIDENTIAL;
import static jakarta.servlet.annotation.ServletSecurity.TransportGuarantee.NONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.omnifaces.exousia.constraints.SecurityConstraint;
import org.omnifaces.exousia.constraints.WebResourceCollection;
import org.omnifaces.exousia.constraints.transformer.ElementsToConstraintsTransformer;
import org.omnifaces.exousia.mapping.SecurityRoleRef;

import cloud.piranha.webapp.impl.WebXml;
import cloud.piranha.webapp.impl.WebXmlServlet;
import cloud.piranha.webapp.impl.WebXmlServletSecurityRoleRef;
import jakarta.servlet.ServletSecurityElement;
import jakarta.servlet.annotation.ServletSecurity;

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
     * Get the security constraints from web.xml.
     *
     * @param webXml the web.xml
     * @return the security constraints.
     */
    public List<SecurityConstraint> getConstraintsFromWebXml(WebXml webXml) {
        if (webXml == null || webXml.getSecurityConstraints() == null) {
            return null;
        }

        List<SecurityConstraint> constraints = new ArrayList<>();

        for (WebXml.SecurityConstraint xmlConstraint : webXml.getSecurityConstraints()) {

            List<WebResourceCollection> webResourceCollections = new ArrayList<>();
            for (WebXml.SecurityConstraint.WebResourceCollection xmlCollection : xmlConstraint.getWebResourceCollections()) {
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
     * Get the security role refs from web.xml
     *
     * @param servletNames the servlet names.
     * @param webXml the web.xml.
     * @return the security role refs.
     */
    public Map<String, List<SecurityRoleRef>> getSecurityRoleRefsFromWebXml(Set<String> servletNames, WebXml webXml) {
        Map<String, List<SecurityRoleRef>> securityRoleRefs = new HashMap<>();

        for (String servletName : servletNames) {
            securityRoleRefs.put(servletName, webXml == null ? emptyList() : getSecurityRoleRefsByServletName(webXml, servletName));
        }

        return securityRoleRefs;
    }

    private List<SecurityRoleRef> getSecurityRoleRefsByServletName(WebXml webXml, String servletName) {
        List<WebXmlServletSecurityRoleRef> piranhaSecurityRoleRefs = getWebXmlSecurityRoleRefsByServletName(webXml, servletName);
        if (piranhaSecurityRoleRefs.isEmpty()) {
            return Collections.emptyList();
        }

        List<SecurityRoleRef> exousiaSecurityRoleRefs = new ArrayList<>();

        for (WebXmlServletSecurityRoleRef piranhaSecurityRoleRef : piranhaSecurityRoleRefs) {
            exousiaSecurityRoleRefs.add(new SecurityRoleRef(
                    piranhaSecurityRoleRef.getRoleName(),
                    piranhaSecurityRoleRef.getRoleLink()));
        }

        return exousiaSecurityRoleRefs;
    }

    private List<WebXmlServletSecurityRoleRef> getWebXmlSecurityRoleRefsByServletName(WebXml webXml, String servletName) {
        WebXmlServlet servlet = getServletByName(webXml, servletName);
        if (servlet == null) {
            return emptyList();
        }

        return servlet.getSecurityRoleRefs();
    }

    private WebXmlServlet getServletByName(WebXml webXml, String servletName) {
        for (WebXmlServlet servlet : webXml.getServlets()) {
            if (servlet.getServletName().equals(servletName)) {
                return servlet;
            }
        }

        return null;
    }
}