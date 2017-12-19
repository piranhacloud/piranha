/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * The default WebApplicationServerInputStream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationServerInputStream extends ServletInputStream {

    /**
     * Stores the finished flag.
     */
    private boolean finished;

    /**
     * Stores the input stream.
     */
    private final InputStream inputStream;

    /**
     * Stores the listener.
     */
    private ReadListener listener;

    /**
     * Stores the HTTP servlet request.
     */
    private final DefaultWebApplicationServerRequest request;

    /**
     * Constructor.
     *
     * @param inputStream the input stream.
     * @param request the HTTP servlet request.
     */
    public DefaultWebApplicationServerInputStream(InputStream inputStream, DefaultWebApplicationServerRequest request) {
        this.inputStream = inputStream;
        this.request = request;
        this.finished = false;
    }

    /**
     * Read a byte.
     *
     * @return the read value.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public int read() throws IOException {
        int read = inputStream.read();
        if (read == -1) {
            finished = true;
        }
        return read;
    }

    /**
     * Is the input stream finished.
     *
     * @return the finished flag.
     */
    @Override
    public boolean isFinished() {
        return finished;
    }

    /**
     * Is the input stream ready for non-blocking I/O.
     *
     * @return false.
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * Set the read listener.
     *
     * @param listener the listener.
     */
    @Override
    public void setReadListener(ReadListener listener) {
        if (listener == null) {
            throw new NullPointerException("Read listener cannot be null");
        }

        if (this.listener != null) {
            throw new IllegalStateException("Read listener can only be set once");
        }

        if (!request.isAsyncStarted() && !request.isUpgraded()) {
            throw new IllegalStateException("Read listener cannot be set as the request is not upgraded nor the async is started");
        }

        this.listener = listener;
    }
}
