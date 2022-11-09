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

import jakarta.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The WebApplicationOutputStream API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class WebApplicationOutputStream extends ServletOutputStream {

    /**
     * Flush the buffer.
     * 
     * @throws IOException when an I/O error occurs.
     */
    public abstract void flushBuffer() throws IOException;

    /**
     * Get the buffer size.
     *
     * @return the buffer size.
     */
    public abstract int getBufferSize();

    /**
     * Get the output stream.
     *
     * @return the output stream.
     */
    public abstract OutputStream getOutputStream();
    
    /**
     * Get the web application response.
     * 
     * @return the web application response.
     */
    public abstract WebApplicationResponse getResponse();
    
    /**
     * Reset the buffer.
     */
    public abstract void resetBuffer();

    /**
     * Set the buffer size.
     * 
     * @param bufferSize the buffer size.
     */
    public abstract void setBufferSize(int bufferSize);
    
    /**
     * Set the output stream.
     *
     * @param outputStream the output stream.
     */
    public abstract void setOutputStream(OutputStream outputStream);

    /**
     * Set the web application response.
     * 
     * @param response the web application response.
     */
    public abstract void setResponse(WebApplicationResponse response);
}
