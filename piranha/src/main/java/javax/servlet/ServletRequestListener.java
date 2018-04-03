/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.EventListener;

/**
 * The ServletRequestListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletRequestListener extends EventListener {

    /**
     * Handle the request destroyed event.
     *
     * @param event the event.
     */
    public void requestDestroyed(ServletRequestEvent event);

    /**
     * Handle the request initialized event.
     *
     * @param event the event.
     */
    public void requestInitialized(ServletRequestEvent event);
}
