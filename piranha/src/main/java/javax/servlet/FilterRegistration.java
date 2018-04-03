/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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
