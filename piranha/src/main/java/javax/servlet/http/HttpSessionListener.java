/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.util.EventListener;

/**
 * The HttpSessionListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpSessionListener extends EventListener {

    /**
     * Handle the HTTP session created event.
     *
     * @param httpSessionEvent the HTTP session event.
     */
    public void sessionCreated(HttpSessionEvent httpSessionEvent);

    /**
     * Handle the HTTP session destroyed event.
     *
     * @param httpSessionEvent the HTTP session event.
     */
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent);
}
