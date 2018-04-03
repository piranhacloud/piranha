/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.Enumeration;

/**
 * The ServletConfig API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletConfig {

    /**
     * Get the init parameter.
     *
     * @param name the name.
     * @return the value, or null if not found.
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

    /**
     * Get the servlet name.
     *
     * @return the servlet name.
     */
    public String getServletName();
}
