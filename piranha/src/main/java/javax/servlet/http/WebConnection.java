/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package javax.servlet.http;

import java.io.IOException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

/**
 * The WebConnection API.
 */
public interface WebConnection extends AutoCloseable {

    /**
     * Get the input stream.
     *
     * @return the input stream.
     * @throws IOException when an I/O error occurs.
     */
    ServletInputStream getInputStream() throws IOException;

    /**
     * Get the output stream.
     *
     * @return the output stream.
     * @throws IOException when an I/O error occurs.
     */
    ServletOutputStream getOutputStream() throws IOException;
}
