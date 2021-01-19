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
     * @param url the url in string form.
     * @return the URL, or null if not found.
     */
    @Override
    public URL getResource(String url) {
        String location = getLocationFromUrl(url);
        if (location == null) {
            return null;
        }

        if (!location.equals(this.location)) {
            return null;
        }

        try {
            return new URL(null, "bytes://" + "root" + location, new ByteArrayResourceURLStreamHandler(this));
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Get the resource as a stream.
     *
     * @param url the location in URL form
     * @return the input stream.
     */
    @Override
    public InputStream getResourceAsStream(String url) {
        return getResourceAsStreamByLocation(getLocationFromUrl(url));
    }
    
    /**
     * Get the resource as stream by location.
     * 
     * @param location the location.
     * @return the input stream
     */
    public InputStream getResourceAsStreamByLocation(String location) {
        if (location == null) {
            return null;
        }
        
        if (!location.equals(this.location)) {
            return null;
        }
        
        return new ByteArrayInputStream(bytes);
    }
    
    private String getLocationFromUrl(String url) {
        if (url == null) {
            return null;
        }
        
        if (!url.contains("bytes://")) {
            // Already a relative URL, so should be the location
            return url;
        }
        
        // Relative URL: [bytes://][root][location] eg bytes://root/WEB-INF/web.xml
        try {
            URL bytesURL = new URL(url.substring(url.indexOf("bytes://")));
            
            String hostName = bytesURL.getHost();
            if (!"root".equals(hostName)) {
                return null;
            }
            
            return bytesURL.getPath().replace("//", "/");
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
        
    }
    
}
