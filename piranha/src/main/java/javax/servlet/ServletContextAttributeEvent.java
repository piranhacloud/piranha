/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

/**
 * The ServletContextAttributeEvent API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ServletContextAttributeEvent extends ServletContextEvent {

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
     * @param name the name.
     * @param value the value.
     */
    public ServletContextAttributeEvent(ServletContext servletContext, String name, Object value) {
        super(servletContext);
        this.name = name;
        this.value = value;
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName() {
        return this.name;
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
