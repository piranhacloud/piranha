/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * A test illegal access exception servlet context listener.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestIllegalAccessExceptionServletContextListener implements ServletContextListener {

    /**
     * Constructor.
     *
     * @throws IllegalAccessException on purpose.
     */
    public TestIllegalAccessExceptionServletContextListener() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * Handle the context destroyed event.
     *
     * @param event the event.
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    /**
     * Handle context initialized event.
     *
     * @param event the event.
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    }
}
