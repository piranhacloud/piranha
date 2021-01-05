/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of the copyright holder nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
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
 *
 */

package cloud.piranha.jpms;

import cloud.piranha.resource.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleReader;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *  Provides an implementation of {@link ModuleReader} to work
 *  with {@link Resource}
 * @author Thiago Henrique Hupner
 */
public class DefaultModuleReader implements ModuleReader {

    /**
     * Stores the resources
     */
    private final Resource resource;

    /**
     * Constructor
     * @param resource the resource
     */
    public DefaultModuleReader(Resource resource) {
        this.resource = resource;
    }

    @Override
    public Optional<URI> find(String name) throws IOException {
        try {
            return Optional.of(resource.getResource(name).toURI());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<InputStream> open(String name) throws IOException {
        // It was needed to override because the default implementation
        // opens the URL from an URI, so it loses its StreamHandler
        // and tries to load any resource from the GlobalArchiveStreamHandler

        URL url = resource.getResource(name);
        if (url != null)
            return Optional.of(url.openStream());
        return Optional.empty();
    }

    @Override
    public Stream<String> list() throws IOException {
        return resource.getAllLocations().map(x -> x.startsWith("/") ? x.substring(1) : x);
    }

    @Override
    public void close() throws IOException {

    }
}
