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
package cloud.piranha.resource.shrinkwrap;

import static java.lang.System.Logger.Level.WARNING;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLStreamHandler;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.Asset;

/**
 *
 * @author Arjan Tijms
 *
 */
public class ArchiveURLStreamHandler extends URLStreamHandler {

    /**
     * Stores the logger.
     */
    private static final System.Logger LOGGER = System.getLogger(ArchiveURLStreamHandler.class.getName());

    /**
     * Stores the archive.
     */
    private Archive<?> archive;

    /**
     * Constructor.
     *
     * @param archive the archive.
     */
    public ArchiveURLStreamHandler(Archive<?> archive) {
        this.archive = archive;
    }

    @Override
    protected StreamConnection openConnection(URL requestedUrl) throws IOException {
        return new StreamConnection(requestedUrl) {

            String contentType;

            @Override
            public InputStream getInputStream() throws IOException {
                Node node = getNode();
                if (node == null) {
                    throw new IOException("Can't resolve URL " + requestedUrl.toExternalForm());
                }

                Asset asset = node.getAsset();
                if (asset == null) {
                    return null;
                }

                return asset.openStream();
            }

            @Override
            public String getContentType() {
                if (contentType != null) {
                    return contentType;
                }

                try {
                    InputStream stream = getInputStream();
                    if (stream != null) {
                        contentType = guessContentTypeFromStream(stream);
                    }
                } catch (IOException ioe) {
                    LOGGER.log(WARNING, "An I/O error occurred while guessing content type from stream", ioe);
                }

                if (contentType == null) {
                    Node node = getNode();
                    if (node != null) {
                        contentType = guessContentTypeFromName(node.getPath().get());
                    }
                }

                if (contentType == null) {
                    contentType = "content/unknown";
                }

                return contentType;
            }

            /**
             * {@return the node}
             */
            private Node getNode() {
                return archive.get(
                        ArchivePaths.create(
                                requestedUrl
                                        .getPath()
                                        .replace(archive.getName(), "")));
            }
        };

    }

}
