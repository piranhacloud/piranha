/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.EventObject;

/**
 * The ServletContextEvent API.
 */
public class ServletContextEvent extends EventObject {

    /**
     * Constructor.
     *
     * @param servletContext the servlet context.
     */
    public ServletContextEvent(ServletContext servletContext) {
        super(servletContext);
    }

    /**
     * Get the servlet context.
     *
     * @return the servlet context.
     */
    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }
}
