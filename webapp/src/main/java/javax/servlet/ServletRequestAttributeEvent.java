/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

/**
 * The ServletRequestAttributeEvent API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletRequestAttributeEvent extends ServletRequestEvent {

    /**
     * Stores the serial version UID.
     */
    private static final long serialVersionUID = -1466635426192317793L;

    /**
     * Stores the name.
     */
    private final String name;

    /**
     * Stores the value.
     */
    private final Object value;

    /**
     * Constructor.
     *
     * @param servletContext the servlet context.
     * @param request the request.
     * @param name the name.
     * @param value the value.
     */
    public ServletRequestAttributeEvent(ServletContext servletContext, ServletRequest request, String name, Object value) {
        super(servletContext, request);
        this.name = name;
        this.value = value;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value.
     *
     * @return the value.
     */
    public Object getValue() {
        return this.value;
    }
}
