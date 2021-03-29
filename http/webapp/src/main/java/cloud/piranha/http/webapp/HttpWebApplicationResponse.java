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
package cloud.piranha.http.webapp;

import cloud.piranha.http.api.HttpServerResponse;
import cloud.piranha.webapp.impl.DefaultWebApplicationResponse;
import java.io.IOException;
import static java.lang.System.Logger.Level.WARNING;

/**
 * The HttpServerResponse variant of WebApplicationResponse.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpWebApplicationResponse extends DefaultWebApplicationResponse {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(
            HttpWebApplicationResponse.class.getPackageName());

    /**
     * Stores the wrapped HttpServerResponse.
     */
    private final HttpServerResponse wrapped;

    /**
     * Constructor.
     *
     * @param wrapped the wrapped HttpServerResponse.
     */
    public HttpWebApplicationResponse(HttpServerResponse wrapped) {
        this.outputStream = wrapped.getOutputStream();
        this.responseCloser = () -> {
            try {
                wrapped.closeResponse();
            } catch (IOException ioe) {
                LOGGER.log(WARNING, "I/O error occurred while closing async output stream", ioe);
            }
        };
        this.wrapped = wrapped;
    }

    @Override
    public void writeStatusLine() throws IOException {
        wrapped.setStatusCode(status);
        wrapped.writeStatusLine();
    }

    @Override
    public void writeHeaders() throws IOException {
        if (contentType != null) {
            StringBuilder contentTypeBuilder = new StringBuilder();
            contentTypeBuilder.append(contentType);
            if (characterEncoding != null) {
                contentTypeBuilder
                        .append(";charset=")
                        .append(characterEncoding);
            }
            setHeader("Content-Type", contentTypeBuilder.toString());
        }
        if (contentLanguage != null) {
            setHeader("Content-Language", contentLanguage);
        }
        wrapped.writeHeaders();
    }
}
