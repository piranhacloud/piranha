/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.io.IOException;
import java.util.EventListener;

/**
 * The ReadListener API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ReadListener extends EventListener {

    /**
     * Called when all data has been read.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void onAllDataRead() throws IOException;

    /**
     * Called when data is available.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void onDataAvailable() throws IOException;

    /**
     * Called when an error occurs.
     *
     * @param throwable the throwable.
     */
    public void onError(Throwable throwable);
}
