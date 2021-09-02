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
package cloud.piranha.resource;

import cloud.piranha.resource.api.ResourceManager;
import cloud.piranha.resource.api.Resource;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * The default ResourceManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultResourceManager implements ResourceManager {

    /**
     * Stores the resources.
     */
    private final ArrayList<Resource> resources = new ArrayList<>();

    /**
     * Add resource.
     *
     * @param resource the resource.
     */
    @Override
    public void addResource(Resource resource) {
        this.resources.add(resource);
    }

    /**
     * Get the resource URL.
     *
     * @param location the location.
     * @return the URL, or null if not found.
     * @throws MalformedURLException when the location URL is malformed.
     */
    @Override
    public URL getResource(String location) throws MalformedURLException {
        URL result = null;
        for (Resource resource : resources) {
            result = resource.getResource(location);
            if (result != null) {
                break;
            }
        }
        if (result == null) {
            if (location != null) {
                result = getClass().getResource(location);
            }
        }
        return result;
    }

    /**
     * Get the resources.
     * 
     * @param location the location.
     * @return the collection with the resources.
     * @throws MalformedURLException when the location URL is malformed.
     */
    @Override
    public Collection<URL> getResources(String location) throws MalformedURLException {
        ArrayList<URL> result = new ArrayList<>();
        for (Resource resource : resources) {
            URL url = resource.getResource(location);
            if (url != null) {
                result.add(url);
            }
        }
        URL url = getClass().getResource(location);
        if (url != null) {
            result.add(url);
        }
        return result;
    }

    /**
     * Get the resource as a stream.
     *
     * @param location the location.
     * @return the input stream, or null if not found.
     */
    @Override
    public InputStream getResourceAsStream(String location) {
        InputStream result = null;
        for (Resource resource : resources) {
            result = resource.getResourceAsStream(location);
            if (result != null) {
                break;
            }
        }
        if (result == null) {
            result = getClass().getResourceAsStream(location);
        }
        return result;
    }

    @Override
    public Stream<String> getAllLocations() {
        return resources.stream().flatMap(Resource::getAllLocations);
    }

    @Override
    public List<Resource> getResourceList() {
        return resources;
    }
}
