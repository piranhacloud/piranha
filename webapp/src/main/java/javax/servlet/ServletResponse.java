/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * The ServletResponse API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ServletResponse {

    /**
     * Flush the buffer.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void flushBuffer() throws IOException;

    /**
     * Get the buffer size.
     *
     * @return the buffer size.
     */
    public int getBufferSize();

    /**
     * Get the character encoding.
     *
     * @return the character encoding.
     */
    public String getCharacterEncoding();

    /**
     * Get the content type.
     *
     * @return the content type.
     */
    public String getContentType();

    /**
     * Get the locale.
     *
     * @return the locale.
     */
    public Locale getLocale();

    /**
     * Get the output stream.
     *
     * @return the output stream.
     * @throws IOException when an I/O error occurs.
     */
    public ServletOutputStream getOutputStream() throws IOException;

    /**
     * Get the writer.
     *
     * @return the writer.
     * @throws IOException when an I/O error occurs.
     */
    public PrintWriter getWriter() throws IOException;

    /**
     * Is committed.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isCommitted();

    /**
     * Reset.
     */
    public void reset();

    /**
     * Reset the buffer.
     */
    public void resetBuffer();

    /**
     * Set the buffer size.
     *
     * @param bufferSize the buffer size.
     */
    public void setBufferSize(int bufferSize);

    /**
     * Set the character encoding.
     *
     * @param characterEncoding the character encoding.
     */
    public void setCharacterEncoding(String characterEncoding);

    /**
     * Set the content length.
     *
     * @param contentLength the content length.
     */
    public void setContentLength(int contentLength);

    /**
     * Set the content length.
     *
     * @param contentLength the content length.
     */
    public void setContentLengthLong(long contentLength);

    /**
     * Set the content type.
     *
     * @param contentType the content type.
     */
    public void setContentType(String contentType);

    /**
     * Set the locale.
     *
     * @param locale the locale.
     */
    public void setLocale(Locale locale);
}
