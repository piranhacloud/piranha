/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.extension.mimetype;

import java.util.HashMap;
import java.util.Map;
import cloud.piranha.webapp.api.MimeTypeManager;

/**
 * The MimeTypeManager that delivers mime-type handling.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultMimeTypeManager implements MimeTypeManager {

    /**
     * Stores the mime types.
     */
    private final Map<String, String> mimeTypes = new HashMap<>();

    /**
     * Constructor.
     */
    public DefaultMimeTypeManager() {
        mimeTypes.put("css", "text/css");
        mimeTypes.put("js", "text/javascript");
        mimeTypes.put("ico", "image/x-icon");
        mimeTypes.put("svg", "image/svg+xml");
        mimeTypes.put("png", "image/png");
        mimeTypes.put("ttf", "font/ttf");
        mimeTypes.put("html", "text/html");
        mimeTypes.put("htm", "text/html");
        mimeTypes.put("text", "text/plain");
        mimeTypes.put("txt", "text/plain");
    }

    /**
     * Add the mime type.
     *
     * @param extension the extension (without the dot).
     * @param mimeType the mime type to return.
     */
    @Override
    public void addMimeType(String extension, String mimeType) {
        mimeTypes.put(extension.toLowerCase(), mimeType);
    }

    /**
     * Get the mime type.
     *
     * @param filename the filename.
     * @return the mime type, or null if not found.
     */
    @Override
    public String getMimeType(String filename) {
        if (!filename.contains(".")) {
            return null;
        }
        return mimeTypes.get(filename.substring(filename.lastIndexOf(".") + 1).toLowerCase());
    }
}
