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
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;

/**
 * The default WebApplicationServerOutputStream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationServerOutputStream extends ServletOutputStream {

    /**
     * Stores the buffer.
     */
    protected byte[] buffer;

    /**
     * Stores the index.
     */
    protected int index;

    /**
     * Stores the output stream.
     */
    protected OutputStream outputStream;

    /**
     * Stores the response.
     */
    protected DefaultWebApplicationServerResponse response;

    /**
     * Constructor.
     */
    public DefaultWebApplicationServerOutputStream() {
        buffer = new byte[8192];
    }

    /**
     * Flush the output stream.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        if (!response.isCommitted()) {
            writeOut();
        }
    }

    /**
     * Is the output stream ready?
     *
     * @return true if it is, false otherwise.
     */
    @Override
    public boolean isReady() {
        return true;
    }

    /**
     * Set the output stream.
     *
     * @param outputStream the output stream.
     */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Set the response.
     *
     * @param response the response.
     */
    public void setResponse(DefaultWebApplicationServerResponse response) {
        this.response = response;
    }

    /**
     * Set the write listener.
     *
     * @param listener the write listener.
     */
    @Override
    public void setWriteListener(WriteListener listener) {
    }

    /**
     * Write the integer.
     *
     * @param integer the integer.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void write(int integer) throws IOException {
        if (index == buffer.length - 1) {
            writeOut();
            outputStream.write(integer);
        } else if (index == buffer.length) {
            outputStream.write(integer);
        } else {
            this.buffer[index] = (byte) integer;
            this.index++;
        }
    }

    /**
     * Write out the cookies.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeCookies() throws IOException {
        List<Cookie> cookies = response.cookies;
        for (Cookie cookie : cookies) {
            writeCookie(cookie);
        }
    }

    /**
     * Write out the Content-Type header.
     */
    private void writeContentType() throws IOException {
        String contentType = response.getContentType();
        if (contentType == null) {
            return;
        }
        outputStream.write("Content-Type: ".getBytes());
        outputStream.write(contentType.getBytes());
        outputStream.write("\n".getBytes());
    }

    /**
     * Write out a cookie.
     *
     * @param cookie the cookie.
     * @throws IOException when an I/O error occurs.
     */
    private void writeCookie(Cookie cookie) throws IOException {
        outputStream.write("Set-Cookie: ".getBytes());
        outputStream.write(cookie.getName().getBytes());
        outputStream.write("=".getBytes());
        outputStream.write(cookie.getValue().getBytes());
        
        if (cookie.getSecure()) {
            outputStream.write("; Secure".getBytes());
        }
        
        if (cookie.isHttpOnly()) {
            outputStream.write("; HttpOnly".getBytes());
        }
        
        outputStream.write("\n".getBytes());
    }

    /**
     * Write out the entity header.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeHeader(String name) throws IOException {
        Iterator<String> values = response.getHeaders(name).iterator();
        outputStream.write(name.getBytes());
        outputStream.write(": ".getBytes());
        while (values.hasNext()) {
            String value = values.next();
            if (value != null) {
                outputStream.write(value.getBytes());
                if (values.hasNext()) {
                    outputStream.write(",".getBytes());
                }
            }
        }
        outputStream.write("\n".getBytes());
    }

    /**
     * Write out the entity headers.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeHeaders() throws IOException {
        Iterator<String> names = response.getHeaderNames().iterator();
        while (names.hasNext()) {
            String name = names.next();
            writeHeader(name);
        }
        outputStream.write("\n".getBytes());
    }

    /**
     * Write out the status-line, headers and the buffer.
     */
    private void writeOut() throws IOException {
        writeStatusLine();
        writeContentType();
        writeCookies();
        writeHeaders();
        outputStream.write(buffer, 0, index);
        index = buffer.length;
        response.setCommitted(true);
    }

    /**
     * Write the status line.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeStatusLine() throws IOException {
        outputStream.write("HTTP/1.1".getBytes());
        outputStream.write(" ".getBytes());
        outputStream.write(Integer.toString(response.getStatus()).getBytes());
        if (response.getStatusMessage() != null) {
            outputStream.write(" ".getBytes());
            outputStream.write(response.getStatusMessage().getBytes());
        }
        outputStream.write("\n".getBytes());
    }
}
