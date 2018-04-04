/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;

/**
 * A test HTTP session id listener.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpSessionIdListener implements HttpSessionIdListener {

    /**
     * Handle the session id changed event.
     *
     * @param event the event.
     * @param oldSessionId the old session id.
     */
    @Override
    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
        event.getSession().getServletContext().setAttribute("sessionIdChanged", true);
    }
}
