/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * A test HTTP session listener.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpSessionListener implements HttpSessionListener {

    /**
     * Handle the session created event.
     *
     * @param event the event.
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().getServletContext().setAttribute("sessionCreated", true);
    }

    /**
     * Handle the session destroyed event.
     *
     * @param event the event.
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    }
}
