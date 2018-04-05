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
package com.manorrock.piranha.test.myfaces;

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
