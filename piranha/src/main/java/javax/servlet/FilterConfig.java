/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.Enumeration;

/**
 * The FilterConfig API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface FilterConfig {

    /**
     * Get the filter name.
     *
     * @return the filter name.
     */
    public String getFilterName();

    /**
     * Get the init parameter.
     *
     * @param name the name.
     * @return the value.
     */
    public String getInitParameter(String name);

    /**
     * Get the init parameter names.
     *
     * @return the init parameter names.
     */
    public Enumeration<String> getInitParameterNames();

    /**
     * Get the servlet context.
     *
     * @return the servlet context.
     */
    public ServletContext getServletContext();
}
