/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha;

import cloud.piranha.http.api.HttpServerResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * The HttpServerResponse used for testing.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TestHttpServerResponse implements HttpServerResponse {

    /**
     * Stores the output stream.
     */
    private final ByteArrayOutputStream outputStream;

    /**
     * Constructor.
     */
    public TestHttpServerResponse() {
        this.outputStream = new ByteArrayOutputStream();
    }

    /**
     * Get the byte-array output stream.
     * 
     * @return the byte-array output stream.
     */
    public ByteArrayOutputStream getByteArrayOutputStream() {
        return outputStream;
    }

    /**
     * Get the header.
     * 
     * @param name the name.
     * @return the value, or null if not found.
     */
    @Override
    public String getHeader(String name) {
        return null;
    }

    /**
     * Get the output stream.
     *
     * @return the output stream.
     */
    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Set the header.
     * 
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setHeader(String name, String value) {
    }

    /**
     * Set the status.
     *
     * @param status the status.
     */
    @Override
    public void setStatus(int status) {
    }

    /**
     * Write the headers.
     * 
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void writeHeaders() throws IOException {
    }

    /**
     * Write the status line.
     * 
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void writeStatusLine() throws IOException {
    }
}
