/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

/**
 * The AsyncListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface AsyncListener extends EventListener {

    /**
     * Handle the on complete event.
     *
     * @param event the event.
     * @throws IOException when an I/O error occurs.
     */
    public void onComplete(AsyncEvent event) throws IOException;

    /**
     * Handle the on error event.
     *
     * @param event the event.
     * @throws IOException when an I/O error occurs.
     */
    public void onError(AsyncEvent event) throws IOException;

    /**
     * Handle the on start async event.
     *
     * @param event the event.
     * @throws IOException when an I/O error occurs.
     */
    public void onStartAsync(AsyncEvent event) throws IOException;

    /**
     * Handle the on timeout event.
     *
     * @param event the event.
     * @throws IOException when an I/O error occurs.
     */
    public void onTimeout(AsyncEvent event) throws IOException;
}
