/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.resource.impl;

import cloud.piranha.resource.api.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MultiReleaseResourceTest {
    private static final Resource MANIFEST_MULTI_RELEASE = new ByteArrayResource("META-INF/MANIFEST.MF", """
            Manifest-Version: 1.0
            Multi-Release: true
            """.getBytes(StandardCharsets.UTF_8));
    
    private static final byte[] FOO_BYTES = "FOO".getBytes(StandardCharsets.UTF_8);
    
    private static final byte[] BAR_BYTES = "BAR".getBytes(StandardCharsets.UTF_8);

    private static final byte[] BAZ_BYTES = "BAZ".getBytes(StandardCharsets.UTF_8);


    private static Resource compose(Resource... resources) {
        return new Resource() {
            @Override
            public URL getResource(String location) {
                for (Resource resource : resources) {
                    URL resourceURL = resource.getResource(location);
                    if (resourceURL != null)
                        return resourceURL;
                }
                return null;
            }

            @Override
            public InputStream getResourceAsStream(String location) {
                for (Resource resource : resources) {
                    InputStream resourceAsStream = resource.getResourceAsStream(location);
                    if (resourceAsStream != null)
                        return resourceAsStream;
                }
                return null;
            }

            @Override
            public Stream<String> getAllLocations() {
                return Arrays.stream(resources).flatMap(Resource::getAllLocations);
            }
        };
    }

    @Test
    void testNormalResource() throws IOException {
        Resource resource = new MultiReleaseResource(new ByteArrayResource("foo", FOO_BYTES));
        assertNotNull(resource);
        assertArrayEquals(FOO_BYTES, resource.getResource("foo").openStream().readAllBytes());
    }

    @Test
    void testResourceWithoutMultiReleaseManifest() throws IOException {
        Resource resource = new MultiReleaseResource(
                compose(new ByteArrayResource("foo", FOO_BYTES),
                        new ByteArrayResource("META-INF/versions/11/foo", BAR_BYTES),
                        new ByteArrayResource("META-INF/MANIFEST.MF", "Manifest-Version: 1.0".getBytes(StandardCharsets.UTF_8)))
        );
        assertArrayEquals(FOO_BYTES, resource.getResource("foo").openStream().readAllBytes());
    }

    @Test
    void testResourceWithMultiReleaseManifest() throws IOException {
        Resource resource = new MultiReleaseResource(
                compose(new ByteArrayResource("foo", FOO_BYTES),
                        new ByteArrayResource("META-INF/versions/9/foo", BAR_BYTES),
                        MANIFEST_MULTI_RELEASE));
        assertArrayEquals(BAR_BYTES, resource.getResource("foo").openStream().readAllBytes());
    }

    @Test
    void testResourceWithMultiReleaseManifest2() throws IOException {
        Resource resource = new MultiReleaseResource(
                compose(new ByteArrayResource("foo", FOO_BYTES),
                        new ByteArrayResource("META-INF/versions/8/foo", BAR_BYTES),
                        MANIFEST_MULTI_RELEASE));
        assertArrayEquals(FOO_BYTES, resource.getResource("foo").openStream().readAllBytes());
    }

    @Test
    void testResourceWithMultiVersionsManifest() throws IOException {
        Resource resource = new MultiReleaseResource(
                compose(new ByteArrayResource("foo", FOO_BYTES),
                        new ByteArrayResource("META-INF/versions/11/foo", BAR_BYTES),
                        new ByteArrayResource("META-INF/versions/%d/foo".formatted(Runtime.version().feature()), BAZ_BYTES),
                        MANIFEST_MULTI_RELEASE));
        assertArrayEquals(BAZ_BYTES, resource.getResource("foo").openStream().readAllBytes());
    }

    @Test
    void testResourceNotMultiRelease() throws IOException {
        Resource resource = new MultiReleaseResource(
                compose(new ByteArrayResource("foo", FOO_BYTES),
                        new ByteArrayResource("META-INF/versions/11/foo", BAR_BYTES),
                        new ByteArrayResource("baz", BAZ_BYTES),
                        MANIFEST_MULTI_RELEASE));
        assertArrayEquals(BAZ_BYTES, resource.getResource("baz").openStream().readAllBytes());
    }

    @Test
    void testMetaInfResource() throws IOException {
        Resource resource = new MultiReleaseResource(
                compose(new ByteArrayResource("META-INF/foo", FOO_BYTES),
                        MANIFEST_MULTI_RELEASE));
        assertArrayEquals(FOO_BYTES, resource.getResource("META-INF/foo").openStream().readAllBytes());
    }

    @Test
    void testMetaInfResource2() throws IOException {
        Resource resource = new MultiReleaseResource(
                compose(new ByteArrayResource("META-INF/foo", FOO_BYTES),
                        new ByteArrayResource("META-INF/versions/11/META-INF/foo", BAR_BYTES),
                        MANIFEST_MULTI_RELEASE));
        assertArrayEquals(FOO_BYTES, resource.getResource("META-INF/foo").openStream().readAllBytes());
    }

}
