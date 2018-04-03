/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.Collection;
import java.util.Set;

/**
 * The ServletRegistration API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletRegistration extends Registration {

    /**
     * Add a mapping.
     *
     * @param urlPatterns the URL patterns.
     * @return the patterns that were already mapped before.
     */
    public Set<String> addMapping(String... urlPatterns);

    /**
     * Get the mappings.
     *
     * @return the mappings.
     */
    public Collection<String> getMappings();

    /**
     * Get the run as role.
     *
     * @return the run as role.
     */
    public String getRunAsRole();

    /**
     * The ServletRegistration.Dynamic API.
     */
    interface Dynamic extends ServletRegistration, Registration.Dynamic {

        /**
         * Set the load on startup.
         *
         * @param loadOnStartup the load on startup.
         */
        public void setLoadOnStartup(int loadOnStartup);

        /**
         * Set the servlet security.
         *
         * @param constraint the constraint.
         * @return the already mapped URL patterns.
         */
        public Set<String> setServletSecurity(ServletSecurityElement constraint);

        /**
         * Set the multipart config element.
         *
         * @param multipartConfig the multipart config element.
         */
        public void setMultipartConfig(MultipartConfigElement multipartConfig);

        /**
         * Set the run as role.
         *
         * @param role the role.
         */
        public void setRunAsRole(String role);
    }
}
