/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;

/**
 * A test servlet request attribute listener.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestServletRequestAttributeListener implements ServletRequestAttributeListener {

    /**
     * Handle attribute added event.
     * 
     * @param event the event. 
     */
    @Override
    public void attributeAdded(ServletRequestAttributeEvent event) {
        event.getServletContext().setAttribute("attributeAdded", true);
    }

    /**
     * Handle attribute removed event.
     * 
     * @param event the event. 
     */
    @Override
    public void attributeRemoved(ServletRequestAttributeEvent event) {
    }

    /**
     * Handle attribute replaced event.
     * 
     * @param event the event. 
     */
    @Override
    public void attributeReplaced(ServletRequestAttributeEvent event) {
    }
}
