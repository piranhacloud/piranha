/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp.test.wicket;

import java.io.IOException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * A test servlet input stream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestServletInputStream extends ServletInputStream {

    /**
     * Stores the buffer.
     */
    private final byte[] buffer;

    /**
     * Stores the index.
     */
    private int index;

    /**
     * Stores the read listener.
     */
    private ReadListener listener;

    /**
     * Stores the HTTP servlet request we work for.
     */
    private final TestHttpServletRequest request;

    /**
     * Constructor.
     *
     * @param buffer the buffer.
     * @param request the request.
     */
    public TestServletInputStream(byte[] buffer, TestHttpServletRequest request) {
        this.buffer = buffer;
        this.index = 0;
        this.request = request;
    }

    /**
     * Read from the byte array.
     *
     * @return the int read, or -1.
     * @throws IOException when an I/O error occurs.
     * @see ServletInputStream#read()
     */
    @Override
    public int read() throws IOException {
        int result;

        if (index == buffer.length) {
            result = -1;
        } else {
            result = buffer[index];
            index++;
        }

        return result;
    }

    /**
     * Is finished.
     *
     * @return true if finished, false otherwise.
     */
    @Override
    public boolean isFinished() {
        return (index >= buffer.length);
    }

    /**
     * Is ready.
     *
     * @return true as we have the entire buffer immediately available.
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * Set the read listener.
     *
     * @param listener the read listener.
     */
    @Override
    public void setReadListener(ReadListener listener) {
        if (listener == null) {
            throw new NullPointerException("Read listener cannot be null");
        }

        if (this.listener != null) {
            throw new IllegalStateException("Read listener can only be set once");
        }

        if (!request.isAsyncStarted() || !request.isUpgraded()) {
            throw new IllegalStateException("Read listener cannot be set as the request is not upgraded nor the async is started");
        }

        this.listener = listener;
    }
}
