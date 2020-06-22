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
package jakarta.servlet.http;

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
