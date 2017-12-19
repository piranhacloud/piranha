/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.util.EventListener;

/**
 * The ServletRequestAttributeListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletRequestAttributeListener extends EventListener {

    /**
     * Handle the attribute added event.
     *
     * @param event the event.
     */
    public void attributeAdded(ServletRequestAttributeEvent event);

    /**
     * Handle the attribute removed event.
     *
     * @param event the event.
     */
    public void attributeRemoved(ServletRequestAttributeEvent event);

    /**
     * Handle the attribute replaced event.
     *
     * @param event the event.
     */
    public void attributeReplaced(ServletRequestAttributeEvent event);
}
