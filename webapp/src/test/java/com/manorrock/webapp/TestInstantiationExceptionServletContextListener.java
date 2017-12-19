/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * A test instantiation exception servlet context listener.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestInstantiationExceptionServletContextListener implements ServletContextListener {

    /**
     * Constructor.
     *
     * @throws InstantiationException on purpose.
     */
    public TestInstantiationExceptionServletContextListener() throws InstantiationException {
        throw new InstantiationException();
    }

    /**
     * Handle context destroyed event.
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
