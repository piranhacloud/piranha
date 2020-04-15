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
package cloud.piranha.upload.apache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItem;

/**
 * The Part implementation for the ApacheMultiPartManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ApacheMultiPart implements Part {

    /**
     * Stores the item.
     */
    private final FileItem item;

    /**
     * Constructor.
     *
     * @param item the file item.
     */
    public ApacheMultiPart(FileItem item) {
        this.item = item;
    }

    /**
     * Delete the part.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void delete() throws IOException {
        item.delete();
    }

    /**
     * Get the content type.
     *
     * @return the content type.
     */
    @Override
    public String getContentType() {
        return item.getContentType();
    }

    /**
     * Get the header.
     *
     * @param name the name of the header.
     * @return the header, or null if not found.
     */
    @Override
    public String getHeader(String name) {
        return item.getHeaders().getHeader(name);
    }

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    @Override
    public Collection<String> getHeaderNames() {
        Collection<String> result = new ArrayList<>();
        item.getHeaders().getHeaderNames().forEachRemaining(result::add);
        return result;
    }

    /**
     * Get the header values.
     *
     * @param name the name of the header.
     * @return the values of the header, or empty collection if none.
     */
    @Override
    public Collection<String> getHeaders(String name) {
        Collection<String> result = new ArrayList<>();
        item.getHeaders().getHeaders(name).forEachRemaining(result::add);
        return result;
    }

    /**
     * Get the input stream.
     *
     * @return the input stream.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return item.getInputStream();
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    @Override
    public String getName() {
        return item.getFieldName();
    }

    /**
     * Get the size.
     *
     * @return the size.
     */
    @Override
    public long getSize() {
        return item.getSize();
    }

    /**
     * Get the submitted filename.
     *
     * @return the submitted filename.
     */
    @Override
    public String getSubmittedFileName() {
        return item.getName();
    }

    /**
     * Write the content to the given filename.
     *
     * @param filename the filename.
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void write(String filename) throws IOException {
        try {
            item.write(new File(filename));
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
