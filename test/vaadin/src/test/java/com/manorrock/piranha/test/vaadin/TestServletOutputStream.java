/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha.test.vaadin;

import com.manorrock.piranha.DefaultHttpServletRequest;
import com.manorrock.piranha.DefaultHttpServletResponse;
import com.manorrock.piranha.DefaultServletOutputStream;
import com.manorrock.piranha.DefaultWebApplication;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.WriteListener;

/**
 * A test servlet output-stream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestServletOutputStream extends DefaultServletOutputStream {

    /**
     * Stores the listener.
     */
    private WriteListener listener;

    /**
     * Stores the byte array output stream.
     */
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    /**
     * Stores the response.
     */
    private DefaultHttpServletResponse response;

    /**
     * Constructor.
     */
    public TestServletOutputStream() {
    }

    /**
     * Flush the output stream.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        outputStream.flush();
        response.setCommitted(true);
    }

    /**
     * Get the content.
     *
     * @return the content.
     */
    public byte[] getBytes() {
        return this.outputStream.toByteArray();
    }

    /**
     * Is the output stream ready?
     *
     * @return true
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * Reset the output stream.
     */
    public void reset() {
        this.outputStream.reset();
    }

    /**
     * Write the byte.
     *
     * @param b the byte.
     * @throws IOException when a serious I/O error occurs.
     */
    @Override
    public void write(int b) throws IOException {
        this.outputStream.write((char) b);
    }

    /**
     * Set the response.
     *
     * @param response the response.
     */
    @Override
    public void setResponse(DefaultHttpServletResponse response) {
        this.response = response;
    }

    /**
     * Set the write listener.
     *
     * @param listener the listener.
     */
    @Override
    public void setWriteListener(WriteListener listener) {
        if (listener == null) {
            throw new NullPointerException("Read listener cannot be null");
        }

        if (this.listener != null) {
            throw new IllegalStateException("Read listener can only be set once");
        }

        DefaultWebApplication webApp = (DefaultWebApplication) response.getWebApplication();
        DefaultHttpServletRequest request = (DefaultHttpServletRequest) webApp.getRequest(response);

        if (!request.isAsyncStarted() && !request.isUpgraded()) {
            throw new IllegalStateException("Read listener cannot be set as the request is not upgraded nor the async is started");
        }

        this.listener = listener;
    }
}
