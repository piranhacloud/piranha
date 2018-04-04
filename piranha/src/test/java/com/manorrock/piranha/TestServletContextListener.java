/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * A test servlet context listener.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestServletContextListener implements ServletContextListener {

    /**
     * Context initialized event.
     *
     * @param event the event.
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute("contextInitialized", true);
    }

    /**
     * Context destroyed event.
     * 
     * @param event the event.
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().setAttribute("contextDestroyed", true);
    }
}
