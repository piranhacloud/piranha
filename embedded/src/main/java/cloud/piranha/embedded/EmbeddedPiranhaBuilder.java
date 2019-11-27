/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.embedded;

import cloud.piranha.DefaultAliasedDirectoryResource;
import cloud.piranha.DefaultDirectoryResource;
import cloud.piranha.api.HttpSessionManager;
import java.io.File;

/**
 * The Embedded Piranha builder.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EmbeddedPiranhaBuilder {

    /**
     * Stores the active filter name.
     */
    private String filterName;

    /**
     * Stores the Embedded Piranha.
     */
    private final EmbeddedPiranha piranha;

    /**
     * Stores the active servlet name.
     */
    private String servletName;

    /**
     * Constructor.
     */
    public EmbeddedPiranhaBuilder() {
        piranha = new EmbeddedPiranha();
    }

    /**
     * Add an aliased directory resource.
     *
     * @param path the path.
     * @param alias the alias.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder aliasedDirectoryResource(String path, String alias) {
        piranha.getWebApplication().addResource(
                new DefaultAliasedDirectoryResource(new File(path), alias));
        return this;
    }

    /**
     * Build the Piranha Embedded instance.
     *
     * @return the instance.
     */
    public EmbeddedPiranha build() {
        return piranha;
    }

    /**
     * Add a directory resource.
     *
     * @param path the path.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder directoryResource(String path) {
        piranha.getWebApplication().addResource(new DefaultDirectoryResource(path));
        return this;
    }

    /**
     * Add a filter.
     *
     * @param filterName the filter name.
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder filter(String filterName, String className) {
        piranha.getWebApplication().addFilter(filterName, className);
        this.filterName = filterName;
        this.servletName = null;
        return this;
    }

    /**
     * Add a filter mapping.
     *
     * @param filterName the filter name.
     * @param urlPatterns the URL patterns.
     * @return
     */
    public EmbeddedPiranhaBuilder filterMapping(String filterName, String... urlPatterns) {
        piranha.getWebApplication().addFilterMapping(filterName, urlPatterns);
        return this;
    }

    /**
     * Add an init parameter to the active filter/servlet.
     *
     * @param name the name.
     * @param value the value.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder initParam(String name, String value) {
        if (filterName != null) {
            piranha.getWebApplication().getFilterRegistration(filterName)
                    .setInitParameter(name, value);
        } else if (servletName != null) {
            piranha.getWebApplication().getServletRegistration(servletName)
                    .setInitParameter(name, value);
        }
        return this;
    }

    /**
     * Add an initializer.
     *
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder initializer(String className) {
        piranha.getWebApplication().addInitializer(className);
        return this;
    }

    /**
     * Add a servlet.
     * 
     * @param servletName the servlet name.
     * @param className the class name.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servlet(String servletName, String className) {
        piranha.getWebApplication().addServlet(servletName, className);
        this.filterName = null;
        this.servletName = servletName;
        return this;
    }

    /**
     * Add a servlet mapping.
     *
     * @param servletName the servlet name.
     * @param urlPatterns the URL patterns.
     * @return the builder.
     */
    public EmbeddedPiranhaBuilder servletMapping(String servletName, String... urlPatterns) {
        piranha.getWebApplication().addServletMapping(servletName, urlPatterns);
        return this;
    }

    /**
     * Set the session manager.
     * 
     * @param sessionManager the session manager.
     * @return 
     */
    public EmbeddedPiranhaBuilder sessionManager(HttpSessionManager sessionManager) {
        piranha.getWebApplication().setHttpSessionManager(sessionManager);
        return this;
    }
}
