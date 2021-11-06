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

package cloud.piranha.resource.impl;

import cloud.piranha.resource.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A resource wrapper that loads the versioned entries from META-INF/versions if
 * the resource contains a main attribute named "Multi-Release" in the META-INF/MANIFEST.MF
 *
 * <p>A multi-release resource is a resource that contains a set of "base" entries and a set of "versioned"
 * entries contained in subdirectories of "META-INF/versions" directory
 * <p>The versioned entries are partitioned by the major version of the Java release. A versioned entry,
 * with a version {@code n}, {@code 8 < n}, in the "META-INF/versions/{n}" directory overrides the base entry
 * as well as any entry with a version number {@code i} where {@code 8 < i < n}
 */
public final class MultiReleaseResource implements Resource {

    /**
     * Stores the META-INF constant
     */
    private static final String META_INF = "META-INF";

    /**
     * Stores the META-INF/versions/ constant
     */
    private static final String META_INF_VERSIONS = META_INF + "/versions/";

    /**
     * Stores the current version of the runtime
     */
    private static final int CURRENT_VERSION = Runtime.version().feature();

    /**
     * Stores the base release version
     */
    private static final int BASE_RELEASE_VERSION = 8;

    /**
     * Stores the resource
     */
    private final Resource resource;

    /**
     * Stores if the resource if a multi release
     */
    private final boolean isMultiRelease;

    /**
     * Constructor
     *
     * @param resource the resource
     */
    public MultiReleaseResource(Resource resource) {
        this.resource = resource;
        boolean isMultiReleaseTemp = false;
        try (InputStream resourceAsStream = resource.getResourceAsStream("META-INF/MANIFEST.MF")) {
            if (resourceAsStream != null) {
                isMultiReleaseTemp = Boolean.parseBoolean(new Manifest(resourceAsStream).getMainAttributes().getValue(Attributes.Name.MULTI_RELEASE));
            }
        } catch (IOException ignored) {
        }
        isMultiRelease = isMultiReleaseTemp;
    }

    @Override
    public URL getResource(String location) {
        if (!isMultiRelease) {
            return resource.getResource(location);
        }
        return versionedEntry(location);
    }

    /**
     * Searches in the META-INF/versions for a versioned entry of some resource.
     *
     * <p>It performs a search in META-INF/versions from the current Java release until
     * the 9 version (the first version supporting multi-release resources).
     *
     * @param location the location of a resource
     * @return the URL of the versioned entry if present otherwise the base entry
     */
    private URL versionedEntry(String location) {
        if (location.startsWith(META_INF))
            return resource.getResource(location);

        return IntStream.iterate(CURRENT_VERSION, version -> version > BASE_RELEASE_VERSION, version -> --version)
                .mapToObj(version -> resource.getResource(META_INF_VERSIONS + version + "/" + location))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> resource.getResource(location));
    }

    @Override
    public InputStream getResourceAsStream(String location) {
        if (!isMultiRelease) {
            return resource.getResourceAsStream(location);
        }
        try {
            URL url = versionedEntry(location);
            if (url != null)
                return url.openStream();
        } catch (IOException ignored) {
        }
        return null;
    }

    @Override
    public Stream<String> getAllLocations() {
        return resource.getAllLocations();
    }

    @Override
    public String getName() {
        return resource.getName();
    }
}
