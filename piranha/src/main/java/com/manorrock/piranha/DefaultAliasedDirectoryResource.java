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
package com.manorrock.piranha;

import com.manorrock.piranha.api.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The default AliasedDirectoryResource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAliasedDirectoryResource implements Resource {

    /**
     * Stores the root directory.
     */
    private File rootDirectory;

    /**
     * Stores the alias.
     */
    private String alias;

    /**
     * Constructor.
     */
    public DefaultAliasedDirectoryResource() {
        this.rootDirectory = null;
        this.alias = null;
    }

    /**
     * Constructor.
     *
     * @param rootDirectory the root directory.
     * @param alias the alias.
     */
    public DefaultAliasedDirectoryResource(File rootDirectory, String alias) {
        this.rootDirectory = rootDirectory;
        this.alias = alias;
    }

    /*
     * Get the resource.
     *
     * @return the resource.
     */
    @Override
    public URL getResource(String location) {
        URL result = null;
        if (location.startsWith(alias)) {
            location = location.substring(alias.length());
            File file = new File(rootDirectory, location);
            if (file.exists()) {
                try {
                    result = file.toURI().toURL();
                } catch (MalformedURLException exception) {
                }
            }
        }
        return result;
    }

    /**
     * @param location the resource location.
     * @return the input stream, or null if not found.
     * @see Resource#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String location) {
        InputStream result = null;
        if (location.startsWith(alias)) {
            location = location.substring(alias.length() + 1);
            File file = new File(rootDirectory, location);
            try {
                result = new FileInputStream(file);
            } catch (FileNotFoundException exception) {
            }
        }
        return result;
    }

    /**
     * Get the alias.
     *
     * @return the alias.
     */
    public String getAlias() {
        return this.alias;
    }

    /**
     * Get the root directory.
     *
     * @return the root directory.
     */
    public File getRootDirectory() {
        return this.rootDirectory;
    }

    /**
     * Set the alias.
     *
     * @param alias the alias.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Set the root directory.
     *
     * @param rootDirectory the root directory.
     */
    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }
}
