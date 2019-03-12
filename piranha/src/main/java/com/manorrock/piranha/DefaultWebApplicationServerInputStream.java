/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice, 
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its 
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

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
