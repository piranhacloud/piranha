/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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

package cloud.piranha.resource.transformer.eclipse;

import cloud.piranha.resource.api.Resource;
import cloud.piranha.resource.api.ResourceManager;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Resource manager wrapper that wraps resources with {@link EclipseTransformerResourceWrapper}
 *
 * @author Thiago Henrique Hupner
 */
public class EclipseTransformerResourceManagerWrapper implements ResourceManager {

    /**
     * Stores the wrapped resource manager
     */
    private final ResourceManager resourceManager;

    /**
     * Constructor
     * @param resourceManager the wrapped resource manager
     */
    public EclipseTransformerResourceManagerWrapper(ResourceManager resourceManager) {
        this.resourceManager = Objects.requireNonNull(resourceManager);
    }

    @Override
    public void addResource(Resource resource) {
        resourceManager.addResource(new EclipseTransformerResourceWrapper(resource));
    }

    @Override
    public URL getResource(String location) throws MalformedURLException {
        return resourceManager.getResource(location);
    }

    @Override
    public Collection<URL> getResources(String location) throws MalformedURLException {
        return resourceManager.getResources(location);
    }

    @Override
    public InputStream getResourceAsStream(String location) {
        return resourceManager.getResourceAsStream(location);
    }

    @Override
    public Stream<String> getAllLocations() {
        return resourceManager.getAllLocations();
    }
}
