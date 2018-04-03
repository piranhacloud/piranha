/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.util.EventListener;

/**
 * The HttpSessionBindingListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpSessionBindingListener extends EventListener {

    /**
     * Handle the value bound event.
     *
     * @param event the HTTP session binding event.
     */
    public void valueBound(HttpSessionBindingEvent event);

    /**
     * Handle the value unbound event.
     *
     * @param event the HTTP session binding event.
     */
    public void valueUnbound(HttpSessionBindingEvent event);
}
