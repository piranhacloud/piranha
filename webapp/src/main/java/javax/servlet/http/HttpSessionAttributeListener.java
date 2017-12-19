/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.util.EventListener;

/**
 * The HttpSessionAttributeListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpSessionAttributeListener extends EventListener {

    /**
     * Handle the attribute added event.
     *
     * @param event the HTTP session binding event.
     */
    public void attributeAdded(HttpSessionBindingEvent event);

    /**
     * Handle the attribute removed event.
     *
     * @param event the HTTP session binding event.
     */
    public void attributeRemoved(HttpSessionBindingEvent event);

    /**
     * Handle the attribute replaced event.
     *
     * @param event the HTTP session binding event.
     */
    public void attributeReplaced(HttpSessionBindingEvent event);
}
