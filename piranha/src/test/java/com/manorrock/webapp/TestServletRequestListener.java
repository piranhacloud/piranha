/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * A test servlet request listener.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestServletRequestListener implements ServletRequestListener {

    /**
     * Handle request destroyed event.
     * 
     * @param event the event. 
     */
    @Override
    public void requestDestroyed(ServletRequestEvent event) {
        event.getServletContext().setAttribute("requestDestroyed", true);
    }

    /**
     * Handle request initialized event.
     * 
     * @param event the event. 
     */
    @Override
    public void requestInitialized(ServletRequestEvent event) {
        event.getServletContext().setAttribute("requestInitialized", true);
    }
}
