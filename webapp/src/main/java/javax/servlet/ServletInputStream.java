/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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
