/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

/**
 * The ServletRequestEvent API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletRequestEvent extends java.util.EventObject {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = -7467864054698729101L;

    /**
     * Stores the request.
     */
    private final ServletRequest servletRequest;

    /**
     * Constructor.
     *
     * @param servletContext the servlet context.
     * @param servletRequest the servlet request.
     */
    public ServletRequestEvent(ServletContext servletContext, ServletRequest servletRequest) {
        super(servletContext);
        this.servletRequest = servletRequest;
    }

    /**
     * Get the servlet context.
     *
     * @return the servlet context.
     */
    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }

    /**
     * Get the servlet request.
     *
     * @return the servlet request.
     */
    public ServletRequest getServletRequest() {
        return this.servletRequest;
    }
}
