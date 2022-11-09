/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.api;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The web application input stream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class WebApplicationInputStream extends ServletInputStream {

    /**
     * Stores the finished flag.
     */
    protected boolean finished;
    
    /**
     * Stores the read index.
     */
    protected int index;

    /**
     * Stores the input stream.
     */
    protected InputStream inputStream;
    
    /**
     * Stores the ready flag.
     */
    protected boolean ready;
    
    /**
     * Stores the read listener.
     */
    protected ReadListener readListener;
    
    /**
     * Stores the web application request.
     */
    protected WebApplicationRequest webApplicationRequest;
    
    /**
     * Constructor.
     */
    public WebApplicationInputStream() {
        inputStream = new ByteArrayInputStream(new byte[0]);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public boolean isReady() {
        return ready;
    }
    
    @Override
    public int read() throws IOException {
        if (finished || webApplicationRequest.getContentLength() == 0) {
            return -1;
        }
        int read = inputStream.read();
        index++;
        if (index == webApplicationRequest.getContentLength() || read == -1) {
            finished = true;
        }
        return read;
    }
    
    /**
     * Set the input stream.
     * 
     * @param inputStream the input stream.
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    @Override
    public void setReadListener(ReadListener readListener) {
        if (readListener == null) {
            throw new NullPointerException("Read listener cannot be null");
        }
        if (this.readListener != null) {
            throw new IllegalStateException("Read listener can only be set once");
        }
        if (!webApplicationRequest.isAsyncStarted() && !webApplicationRequest.isUpgraded()) {
            throw new IllegalStateException("Read listener cannot be set as the request is not upgraded nor the async is started");
        }
        this.readListener = readListener;
    }

    /**
     * Set the web application request.
     * 
     * @param webApplicationRequest the web application request.
     */
    public void setWebApplicationRequest(WebApplicationRequest webApplicationRequest) {
        this.webApplicationRequest = webApplicationRequest;
    }
}
