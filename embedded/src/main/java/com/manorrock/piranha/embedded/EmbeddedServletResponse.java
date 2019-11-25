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
package com.manorrock.piranha.embedded;

import cloud.piranha.DefaultWebApplicationResponse;

/**
 * The embedded version of a ServletResponse.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class EmbeddedServletResponse extends DefaultWebApplicationResponse {
    
    /**
     * Constructor.
     */
    public EmbeddedServletResponse() {
        EmbeddedServletOutputStream embeddedOutputStream = new EmbeddedServletOutputStream();
        embeddedOutputStream.setResponse(this);
        outputStream = embeddedOutputStream;
    }

    /**
     * Get the buffer size.
     * 
     * @return the buffer size.
     */
    @Override
    public int getBufferSize() {
        return getEmbeddedOutputStream().getBufferSize();
    }
    
    /**
     * Get the embedded output stream.
     * 
     * @return the embedded output stream.
     */
    public EmbeddedServletOutputStream getEmbeddedOutputStream() {
        return (EmbeddedServletOutputStream) outputStream;
    }

    /**
     * Get the response as a byte-array.
     * 
     * @return the response as a byte-array.
     */
    public byte[] getResponseAsButes() {
        return getEmbeddedOutputStream().getBytes();
    }

    /**
     * Get the response as a string.
     * 
     * @return the response as a string.
     */
    public String getResponseAsString() {
        return new String(getEmbeddedOutputStream().getBytes());
    }

    /**
     * Reset the buffer.
     */
    @Override
    public void resetBuffer() {
        getEmbeddedOutputStream().reset();
    }

    /**
     * Set the buffer size.
     * 
     * @param bufferSize the buffer size. 
     */
    @Override
    public void setBufferSize(int bufferSize) {
    }
}
