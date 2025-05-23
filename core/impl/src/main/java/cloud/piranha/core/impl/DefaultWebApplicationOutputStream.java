/*
 * Copyright (c) 2002-2025 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.core.impl;

import cloud.piranha.core.api.WebApplicationOutputStream;
import cloud.piranha.core.api.WebApplicationResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.Cookie;
import static jakarta.servlet.http.HttpServletResponse.SC_SWITCHING_PROTOCOLS;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.TRACE;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The default WebApplicationOutputStream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationOutputStream extends WebApplicationOutputStream implements Runnable {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER
            = System.getLogger(DefaultWebApplicationOutputStream.class.getName());

    /**
     * Stores the buffer.
     */
    protected byte[] buffer;

    /**
     * Stores the closed flag.
     */
    protected boolean closed;

    /**
     * Stores the index.
     */
    protected int index;

    /**
     * Stores the web application response.
     */
    protected WebApplicationResponse response;

    /**
     * Stores the output stream.
     */
    protected OutputStream outputStream;

    /**
     * Stores the write listener.
     */
    protected WriteListener writeListener;

    /**
     * Stores the write listener lock.
     */
    protected Lock writeListenerLock = new ReentrantLock();

    /**
     * Constructor.
     */
    public DefaultWebApplicationOutputStream() {
        this.buffer = new byte[8192];
        this.outputStream = new ByteArrayOutputStream();
    }

    @Override
    public void close() throws IOException {
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Closing input stream");
        }
        if (!response.isCommitted()) {
            response.flushBuffer();
            outputStream.flush();
        }
        closed = true;
    }

    @Override
    public void flush() throws IOException {
        if (response.isBufferResetting()) {
            if (LOGGER.isLoggable(TRACE)) {
                LOGGER.log(TRACE, "Buffer resetting, ignoring flush");
            }
            return;
        }
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Flushing the input stream");
        }
        if (!response.isCommitted()) {
            response.flushBuffer();
        }
        outputStream.flush();
    }

    /**
     * Flush the buffer.
     *
     * <p>
     * This will cause the following to be written out in order:
     * </p>
     * <ol>
     * <li>the status line</li>
     * <li>the response headers</li>
     * <li>the buffer (aka the response body)</li>
     * </ol>
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void flushBuffer() throws IOException {
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Flushing the buffer");
        }
        if (!response.isBodyOnly()) {
            writeStatusLine();
            writeHeaders();
        }
        if (!response.isCommitted()) {
            outputStream.write(buffer, 0, index);
            index = buffer.length;
            response.setCommitted(true);
        }
    }

    /**
     * Format the timestamp to a GMT string.
     *
     * @param timestamp the timestamp.
     * @return the GMT string.
     */
    private String formatDateToGMT(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("GMT"))
                .format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    @Override
    public int getBufferSize() {
        return buffer.length;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public WriteListener getWriteListener() {
        return writeListener;
    }

    @Override
    public WebApplicationResponse getResponse() {
        return response;
    }

    @Override
    public boolean isReady() {
        try {
            writeListenerLock.lock();
            return !closed;
        } finally {
            writeListenerLock.unlock();
        }
    }

    @Override
    public void resetBuffer() {
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Reset the buffer");
        }
        this.buffer = new byte[buffer.length];
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (isReady()) {
                    try {
                        writeListenerLock.lock();
                        writeListener.onWritePossible();
                    } finally {
                        writeListenerLock.unlock();
                    }
                }
                if (closed) {
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();;
                }
            } catch (IOException ioe) {
                writeListener.onError(ioe);
                break;
            }
        }
    }

    @Override
    public void setBufferSize(int bufferSize) {
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Setting buffer size: {0}", bufferSize);
        }
        this.buffer = new byte[bufferSize];
    }

    @Override
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void setResponse(WebApplicationResponse response) {
        this.response = response;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Setting WriteListener: {0}", writeListener);
        }
        if (writeListener == null) {
            throw new NullPointerException("Write listener cannot be null");
        }
        if (this.writeListener != null) {
            throw new IllegalStateException("Write listener can only be set once");
        }
        if (!response.getWebApplication().getRequest(response).isAsyncStarted()
                && response.getStatus() != SC_SWITCHING_PROTOCOLS) {
            throw new IllegalStateException("Write listener cannot be set as the request is not upgraded nor async is started");
        }
        this.writeListener = writeListener;
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void write(int integer) throws IOException {
        if (writeListener == null) {
            /*
             * If the response is in buffer resetting mode, we are going to
             * ignore any writes to the underlying output stream until the
             * response is no longer in buffer resetting mode.
             */
            if (response.isBufferResetting()) {
                if (LOGGER.isLoggable(TRACE)) {
                    LOGGER.log(TRACE, "Buffer is resetting, ignoring write");
                }
                return;
            }

            /*
             * Servlet:SPEC:192.2
             *
             * If the integer we are looking at will cause the buffer to
             * overflow, write out the buffer and then write the integer
             * directly to the underlying output stream.
             */
            if (index == buffer.length - 1) {
                if (LOGGER.isLoggable(TRACE)) {
                    LOGGER.log(TRACE, "Buffer is full, flushing");
                }
                flushBuffer();
                outputStream.write(integer);
            } else if (index == buffer.length) {
                /*
                 * Write the integer directly to the underlying output stream as
                 * the buffer was previously flushed.
                 */
                outputStream.write(integer);
            } else if (index > buffer.length) {
                /*
                 * The buffer is full, so we need to write the integer directly
                 * to the underlying output stream.
                 */
                outputStream.write(integer);
            } else {
                /*
                 * Add the integer to the buffer.
                 */
                this.buffer[index] = (byte) integer;
                this.index++;
            }
        } else {
            /*
             * WriteListener is set so write directly to output stream.
             */
            outputStream.write(integer);
        }
    }

    /**
     * Write out the content language.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeContentLanguage() throws IOException {
        if (response.getContentLanguage() == null) {
            return;
        }
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Writing Content-Language: {0}", response.getContentLanguage());
        }
        outputStream.write("Content-Language: ".getBytes());
        outputStream.write(response.getContentLanguage().getBytes());
        outputStream.write("\n".getBytes());
    }

    /**
     * Write out the content type.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeContentType() throws IOException {
        if (response.getContentType() != null) {
            if (LOGGER.isLoggable(TRACE)) {
                LOGGER.log(TRACE, "Writing Content-Type: {0}", response.getContentType());
            }
            outputStream.write("Content-Type: ".getBytes());
            outputStream.write(response.getContentType().getBytes());
            /*
             * Add the character encoding if it is not already there.
             */
            if (response.getCharacterEncoding() != null
                    && !response.getContentType().contains("charset")) {
                if (LOGGER.isLoggable(TRACE)) {
                    LOGGER.log(TRACE, "Writing Content-Type Charset: {0}", response.getCharacterEncoding());
                }
                outputStream.write(";charset=".getBytes());
                outputStream.write(response.getCharacterEncoding().getBytes());
            }
            outputStream.write("\n".getBytes());
        }
    }

    /**
     * Write out a cookie.
     *
     * @param cookie the cookie.
     * @throws IOException when an I/O error occurs.
     */
    @SuppressWarnings({"removal"})
    private void writeCookie(Cookie cookie) throws IOException {
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Writing Cookie Name: {0}", cookie.getName());
        }
        outputStream.write("Set-Cookie: ".getBytes());
        outputStream.write(cookie.getName().getBytes());
        outputStream.write("=".getBytes());
        if (cookie.getValue() != null) {
            outputStream.write(cookie.getValue().getBytes());
            if (LOGGER.isLoggable(TRACE)) {
                LOGGER.log(TRACE, "Writing Cookie value: {0}", cookie.getValue());
            }
        }
        if (cookie.getMaxAge() > -1) {
            outputStream.write(("; Max-Age=" + cookie.getMaxAge()).getBytes());
            String expireDate = formatDateToGMT(Instant.now().plusSeconds(cookie.getMaxAge()).toEpochMilli());
            outputStream.write(("; Expires=" + expireDate).getBytes());
            if (LOGGER.isLoggable(TRACE)) {
                LOGGER.log(TRACE, "Writing Cookie Max-Age: {0}", cookie.getMaxAge());
                LOGGER.log(TRACE, "Writing Cookie Expires: {0}", expireDate);
            }
        }
        if (cookie.getSecure()) {
            outputStream.write("; Secure".getBytes());
            if (LOGGER.isLoggable(TRACE)) {
                LOGGER.log(TRACE, "Writing Cookie Secure: {0}", cookie.getSecure());
            }
        }
        if (cookie.isHttpOnly()) {
            outputStream.write("; HttpOnly".getBytes());
            if (LOGGER.isLoggable(TRACE)) {
                LOGGER.log(TRACE, "Writing Cookie HttpOnly: {0}", cookie.isHttpOnly());
            }
        }
        if (cookie.getPath() != null) {
            outputStream.write(("; Path=" + cookie.getPath()).getBytes());
            if (LOGGER.isLoggable(TRACE)) {
                LOGGER.log(TRACE, "Writing Cookie Path: {0}", cookie.isHttpOnly());
            }
        }
        if (cookie.getVersion() > 0) {
            outputStream.write(("; Version=" + cookie.getVersion()).getBytes());
            if (LOGGER.isLoggable(TRACE)) {
                LOGGER.log(TRACE, "Writing Cookie Version: {0}", cookie.getVersion());
            }
        }
        outputStream.write("\n".getBytes());
    }

    /**
     * Write out the cookies.
     *
     * @throws IOException when an I/O error occurs.
     */
    private void writeCookies() throws IOException {
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Writing cookies");
        }
        for (Cookie cookie : response.getCookies()) {
            writeCookie(cookie);
        }
    }

    /**
     * Write out a header.
     *
     * @param name the name of the header.
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
                if (LOGGER.isLoggable(TRACE)) {
                    LOGGER.log(TRACE, "Writing header: {0} with value: {1}", name, value);
                }
            }
        }
        outputStream.write("\n".getBytes());
    }

    /**
     * Write out the headers.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void writeHeaders() throws IOException {
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Writing headers");
        }
        writeContentType();
        writeContentLanguage();
        writeCookies();
        for (String name : response.getHeaderNames()) {
            writeHeader(name);
        }
        outputStream.write("\n".getBytes());
    }

    /**
     * Write out the status line.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void writeStatusLine() throws IOException {
        if (LOGGER.isLoggable(TRACE)) {
            LOGGER.log(TRACE, "Writing status line: {0}",
                    "HTTP/1.1 " + Integer.toString(response.getStatus()));
        }

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
