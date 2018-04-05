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
package javax.servlet;

import java.io.IOException;
import java.io.InputStream;

/**
 * The ServletInputStream API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public abstract class ServletInputStream extends InputStream {

    /**
     * Constructor.
     */
    protected ServletInputStream() {
    }

    /**
     * Is the stream at the end.
     *
     * @return true if it is, false otherwise.
     */
    public abstract boolean isFinished();

    /**
     * Is the stream ready for reading.
     *
     * @return true if it is, false otherwise.
     */
    public abstract boolean isReady();

    /**
     * Read a line.
     *
     * @param buffer the buffer.
     * @param offset the offset.
     * @param length the length.
     * @return the number of bytes read.
     * @throws IOException when an I/O error occurs.
     */
    public int readLine(byte[] buffer, int offset, int length) throws IOException {
        int result = 0;
        if (length > 0) {
            int count = 0;
            int read = read();
            while (read != -1) {
                buffer[offset++] = (byte) read;
                count++;
                if (read == '\n' || count == length) {
                    break;
                }
                read = read();
            }
            result = count > 0 ? count : -1;
        }
        return result;
    }

    /**
     * Set the read listener.
     *
     * @param readListener the read listener.
     */
    public abstract void setReadListener(ReadListener readListener);
}
