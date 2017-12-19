/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.util.EventListener;

/**
 * The HttpSessionActivationListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpSessionActivationListener extends EventListener {

    /**
     * Handle the session will passivate event.
     *
     * @param event the HTTP session event.
     */
    public void sessionWillPassivate(HttpSessionEvent event);

    /**
     * Handle the session did activate event.
     *
     * @param event the HTTP session event.
     */
    public void sessionDidActivate(HttpSessionEvent event);
}
