/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.Set;

/**
 * The ServletContainerInitializer API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletContainerInitializer {

    /**
     * Notify the initializer upon startup.
     *
     * @param classes the classes to inspect.
     * @param servletContext the servlet context.
     * @throws ServletException when a servlet error occurs.
     */
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException;
}
