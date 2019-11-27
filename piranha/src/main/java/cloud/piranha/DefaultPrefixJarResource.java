/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package cloud.piranha;

import cloud.piranha.api.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * The default PrefixJarResource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultPrefixJarResource implements Resource {

    /**
     * Stores the JAR file.
     */
    private JarFile jarFile;

    /**
     * Stores the prefix.
     */
    private String prefix;

    /**
     * Constructor.
     */
    public DefaultPrefixJarResource() {
        this.jarFile = null;
        this.prefix = null;
    }

    /**
     * Constructor.
     *
     * @param jarFile the JAR file.
     * @param prefix the prefix.
     */
    public DefaultPrefixJarResource(JarFile jarFile, String prefix) {
        this.jarFile = jarFile;
        if (!prefix.endsWith("/")) {
            this.prefix = prefix + "/";
        } else {
            this.prefix = prefix;
        }
    }

    /*
     * Get the resource.
     *
     * @return the resource.
     */
    @Override
    public URL getResource(String location) {
        URL result = null;
        try {
            location = prefix + location;
            JarEntry jarEntry = jarFile.getJarEntry(location);
            if (jarEntry != null) {
                result = new URL("jar://" + jarFile.getName() + "#!/" + location);
            }
        } catch (MalformedURLException use) {
        }
        return result;
    }

    /**
     * Get the resource as a stream.
     *
     * @param location the resource location.
     * @return the input stream, or null if not found.
     * @see Resource#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String location) {
        InputStream result = null;
        location = prefix + location;
        JarEntry jarEntry = jarFile.getJarEntry(location);
        if (jarEntry != null) {
            try {
                result = jarFile.getInputStream(jarEntry);
            } catch (IOException ioe) {
            }
        }
        return result;
    }
    
    public Stream<String> getAllLocations() {
        return Stream.empty();
    }

    /**
     * Get the JAR file.
     *
     * @return the JAR file.
     */
    public JarFile getJarFile() {
        return this.jarFile;
    }

    /**
     * Get the prefix.
     *
     * @return the prefix.
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Set the JAR file.
     *
     * @param jarFile the JAR file.
     */
    public void setJarFile(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    /**
     * Set the prefix.
     *
     * @param prefix the prefix.
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        if (!prefix.endsWith("/")) {
            this.prefix = prefix + "/";
        }
    }
}
