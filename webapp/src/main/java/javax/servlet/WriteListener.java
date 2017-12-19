/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

/**
 * The WriteListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WriteListener extends EventListener {

    /**
     * Called when an error is detected.
     *
     * @param throwable the throwable.
     */
    public void onError(final Throwable throwable);

    /**
     * Called when a write is possible.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void onWritePossible() throws IOException;
}
