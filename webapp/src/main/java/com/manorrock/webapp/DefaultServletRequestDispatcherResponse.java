/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

/**
 * The default ServletRequestDispatcherResponse.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultServletRequestDispatcherResponse extends DefaultHttpServletResponse {

    /**
     * Constructor.
     */
    public DefaultServletRequestDispatcherResponse() {
        super();
        this.outputStream = new DefaultServletRequestDispatcherOutputStream();
    }

    /**
     * Get the buffer size.
     *
     * @return the buffer size.
     */
    @Override
    public int getBufferSize() {
        return 0;
    }

    /**
     * Get the bytes in the buffer.
     *
     * @return the bytes in the buffer.
     */
    public byte[] getResponseBody() {
        if (this.gotWriter) {
            this.writer.flush();
        }
        DefaultServletRequestDispatcherOutputStream output = (DefaultServletRequestDispatcherOutputStream) this.outputStream;
        return output.getBytes();
    }

    /**
     * Reset the buffer.
     */
    @Override
    public void resetBuffer() {
        verifyNotCommitted("resetBuffer");
        DefaultServletRequestDispatcherOutputStream output = (DefaultServletRequestDispatcherOutputStream) this.outputStream;
        output.reset();
    }

    /**
     * Set the buffer size.
     *
     * @param size the buffer size.
     */
    @Override
    public void setBufferSize(int size) {
    }
}
