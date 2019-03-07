/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package javax.servlet;

import java.util.Collection;
import java.util.EnumSet;

/**
 * The FilterRegistration API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface FilterRegistration extends Registration {

    /**
     * Add a mapping for servlet names.
     *
     * @param dispatcherTypes the dispatcher types.
     * @param isMatchAfter true if it should be matched after declared filters.
     * @param servletNames the servlet names.
     */
    public void addMappingForServletNames(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... servletNames);

    /**
     * Add a mapping for URL patterns.
     *
     * @param dispatcherTypes the dispatcher types.
     * @param isMatchAfter true if it should be matched after declared filters.
     * @param urlPatterns the URL patterns.
     */
    public void addMappingForUrlPatterns(EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter, String... urlPatterns);

    /**
     * Get the servlet name mappings.
     *
     * @return the servlet name mappings.
     */
    public Collection<String> getServletNameMappings();

    /**
     * Get the URL pattern mappings.
     *
     * @return the URL pattern mappings.
     */
    public Collection<String> getUrlPatternMappings();

    /**
     * The FilterRegistration.Dynamic API.
     *
     * @author Manfred Riem (mriem@manorrock.com)
     */
    interface Dynamic extends FilterRegistration, Registration.Dynamic {
    }
}
