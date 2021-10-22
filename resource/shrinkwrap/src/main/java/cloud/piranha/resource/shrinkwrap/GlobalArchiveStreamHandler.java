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
package cloud.piranha.resource.shrinkwrap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import cloud.piranha.core.api.WebApplication;

/**
 * Stream handler for the <code>shrinkwrap</code> protocol (urls starting with
 * <code>shrinkwrap://</code>).
 *
 * <p>
 * This is for URLs that don't have the embedded stream handler, which is for
 * instance the case when resource URLs obtained from Piranha Micro are
 * converted to external string form and used to create a new URL.
 *
 * @author Arjan Tijms
 *
 */
public class GlobalArchiveStreamHandler extends URLStreamHandler {

    /**
     * Stores the web application.
     */
    private WebApplication webApplication;

    /**
     * Constructror.
     *
     * @param webApplication the web application.
     */
    public GlobalArchiveStreamHandler(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    /**
     * Connect to the URL.
     *
     * @param requestedUrl the requested URL.
     * @return the URL connection.
     */
    public URLConnection connect(URL requestedUrl) {
        try {
            return openConnection(requestedUrl);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public URLConnection openConnection(URL requestedUrl) throws IOException {
        return new StreamConnection(requestedUrl) {
            @Override
            public InputStream getInputStream() throws IOException {
                InputStream inputStream = webApplication.getResourceAsStream(requestedUrl.toString());
                if (inputStream == null) {
                    inputStream= Thread.currentThread().getContextClassLoader().getResourceAsStream(requestedUrl.toString());
                }
                
                return inputStream;
            }
        };
    }
}
