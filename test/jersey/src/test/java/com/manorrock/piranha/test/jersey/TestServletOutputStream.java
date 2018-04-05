/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha.test.jersey;

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
