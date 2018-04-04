/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * A test HTTP session attribute listener.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpSessionAttributeListener implements HttpSessionAttributeListener {

    /**
     * Handle attribute added event.
     *
     * @param event the event.
     */
    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        event.getSession().getServletContext().setAttribute("attributeAdded", true);
    }

    /**
     * Handle attribute removed event.
     *
     * @param event the event.
     */
    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
    }

    /**
     * Handle attribute replaced event.
     *
     * @param event the event.
     */
    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        event.getSession().getServletContext().setAttribute("attributeAdded", event.getValue());
    }
}
