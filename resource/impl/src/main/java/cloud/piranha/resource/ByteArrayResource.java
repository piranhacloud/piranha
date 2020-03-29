/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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

import cloud.piranha.resource.api.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

/**
 * The byte-array resource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class ByteArrayResource implements Resource {

    /**
     * Stores the byte-array.
     */
    private final byte[] bytes;

    /**
     * Stores the location.
     */
    private final String location;
    
    /**
     * Constructor.
     * 
     * @param location the location.
     * @param bytes the byte-array.
     */
    public ByteArrayResource(String location, byte[] bytes) {
        this.location = location;
        this.bytes = bytes;
    }

    /**
     * Get all locations.
     *
     * @return the locations.
     */
    @Override
    public Stream<String> getAllLocations() {
        return Stream.of(location);
    }

    /**
     * Get the byte-array.
     *
     * @return the byte-array.
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Get the resource.
     *
     * @param location the location.
     * @return the URL, or null if not found.
     */
    @Override
    public URL getResource(String location) {
        URL result = null;
        if (location != null && location.equals(this.location)) {
            try {
                result = new URL(null, "bytes://" + location,
                        new ByteArrayResourceURLStreamHandler(this));
            } catch (MalformedURLException mue) {
            }
        }
        return result;
    }

    /**
     * Get the resource as a stream.
     *
     * @param location the location.
     * @return the input stream.
     */
    @Override
    public InputStream getResourceAsStream(String location) {
        InputStream result = null;
        if (location != null && location.equals(this.location)) {
            result = new ByteArrayInputStream(bytes);
        }
        return result;
    }
}
