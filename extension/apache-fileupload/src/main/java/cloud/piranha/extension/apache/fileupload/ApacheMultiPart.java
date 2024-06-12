/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.apache.fileupload;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.fileupload2.core.FileItem;

/**
 * The Part for the ApacheMultiPartManager.
 *
 * <p>
 * This class implements the Servlet Part API and delegates to an Apache Commons
 * FileItem for its functionality.
 * </p>
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

    @Override
    public void delete() throws IOException {
        item.delete();
    }

    @Override
    public String getContentType() {
        return item.getContentType();
    }

    @Override
    public String getHeader(String name) {
        return item.getHeaders().getHeader(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        Collection<String> result = new ArrayList<>();
        item.getHeaders().getHeaderNames().forEachRemaining(result::add);
        return result;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        Collection<String> result = new ArrayList<>();
        item.getHeaders().getHeaders(name).forEachRemaining(result::add);
        return result;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return item.getInputStream();
    }

    @Override
    public String getName() {
        return item.getFieldName();
    }

    @Override
    public long getSize() {
        return item.getSize();
    }

    @Override
    public String getSubmittedFileName() {
        return item.getName();
    }

    @Override
    public void write(String filename) throws IOException {
        try {
            item.write(new File(filename).toPath());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
