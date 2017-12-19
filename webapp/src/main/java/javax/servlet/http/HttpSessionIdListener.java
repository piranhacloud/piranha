/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.util.EventListener;

/**
 * The HttpSessionIdListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpSessionIdListener extends EventListener {

    /**
     * Handle the session id change event.
     *
     * @param event the HTTP session event.
     * @param oldSessionId the old HTTP session id.
     */
    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId);
}
