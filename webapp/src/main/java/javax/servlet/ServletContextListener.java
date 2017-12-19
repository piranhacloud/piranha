/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.EventListener;

/**
 * The ServletContextListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletContextListener extends EventListener {

    /**
     * Handle the context destroyed event.
     *
     * @param event the event.
     */
    public void contextDestroyed(ServletContextEvent event);

    /**
     * Handle the context initialized event.
     *
     * @param event the event.
     */
    public void contextInitialized(ServletContextEvent event);
}
