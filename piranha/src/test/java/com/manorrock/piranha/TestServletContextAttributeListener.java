/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

/**
 * A test servlet context attribute listener.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestServletContextAttributeListener implements ServletContextAttributeListener {

    /**
     * Handle attribute added event.
     * 
     * @param event the event. 
     */
    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        event.getServletContext().setAttribute("attributeAdded", true);
    }

    /**
     * Handle attribute removed event.
     * 
     * @param event the event. 
     */
    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
    }

    /**
     * Handle attribute replaced event.
     * 
     * @param event the event. 
     */
    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
    }
}
