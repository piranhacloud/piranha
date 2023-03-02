/*
 * Copyright (c) 2002-2023 Manorrock.com. All Rights Reserved.
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
import java.util.Collection;
import java.util.Optional;

import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.Asset;

/**
 * Stream handler used for URLs that represent directories.
 *
 * @author Arjan Tijms
 */
public class NodeURLStreamHandler extends URLStreamHandler {

    /**
     * Stores the nodes.
     */
    private Collection<Node> nodes;

    /**
     * Constructor.
     *
     * @param nodes the collection of nodes.
     */
    public NodeURLStreamHandler(Collection<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    protected URLConnection openConnection(URL requestedUrl) throws IOException {
        return new StreamConnection(requestedUrl) {
            @Override
            public InputStream getInputStream() throws IOException {
                if (requestedUrl == null) {
                    return null;
                }

                // Relative URL: [shrinkwrap://][jar name][location]
                String location = requestedUrl.getPath();

                if ("/".equals(location) || "".equals(location)) {
                    return new ShrinkWrapDirectoryInputStream(nodes);
                }

                Optional<Node> optionalNode = nodes.stream()
                     .filter(node -> node.getPath().get().equals(location))
                     .findAny();

                if (!optionalNode.isPresent()) {
                    return null;
                }

                Asset asset  = optionalNode.get().getAsset();
                if (asset != null) {
                    return asset.openStream();
                }

                return new ShrinkWrapDirectoryInputStream(getDirectoryContent(location));
            }
        };
    }

    private Collection<Node> getDirectoryContent(String location) {
        return
            nodes.stream()
                 .filter(node -> node.getPath().get().startsWith(location))
                 .toList();
    }

}
