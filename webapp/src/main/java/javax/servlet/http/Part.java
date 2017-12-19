/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

/**
 * The Part API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface Part {

    /**
     * Delete the part.
     *
     * @throws IOException when an I/O error occurs.
     */
    public void delete() throws IOException;

    /**
     * Get the content type.
     *
     * @return the content type.
     */
    public String getContentType();

    /**
     * Get the header.
     *
     * @param name the name.
     * @return the header, or null.
     */
    public String getHeader(String name);

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    public Collection<String> getHeaderNames();

    /**
     * Get the headers.
     *
     * @param name the name.
     * @return the headers.
     */
    public Collection<String> getHeaders(String name);

    /**
     * Get the input stream.
     *
     * @return the input stream.
     * @throws IOException when an I/O error occurs.
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Get the name.
     *
     * @return the name.
     */
    public String getName();

    /**
     * Get the size.
     *
     * @return the size.
     */
    public long getSize();

    /**
     * Get the submitted filename.
     *
     * @return the submitted filename.
     */
    public String getSubmittedFileName();

    /**
     * Write to the given filename.
     *
     * @param filename the filename.
     * @throws IOException when an I/O error occurs.
     */
    public void write(String filename) throws IOException;
}
