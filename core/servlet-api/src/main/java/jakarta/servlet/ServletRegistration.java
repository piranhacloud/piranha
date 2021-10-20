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
package jakarta.servlet;

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
     * {@return the mappings}
     */
    public Collection<String> getMappings();

    /**
     * {@return the run as role}
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
