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
package com.manorrock.piranha.shrinkwrap;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import com.manorrock.piranha.api.Resource;

/**
 * 
 * @author Arjan Tijms
 *
 */
public class ShrinkWrapResource implements Resource {
    
    private Archive<?> archive;
    private ArchiveURLStreamHandler archiveStreamHandler;
    
    public ShrinkWrapResource(String resourcesPath, Archive<?> archive) {
        JavaArchive newArchive = ShrinkWrap.create(JavaArchive.class);
        
        getAllLocations(archive)
            .filter(path -> path.startsWith(resourcesPath))
            .forEach(path -> 
                newArchive.add(
                    archive.get(path).getAsset() , 
                    path.substring(resourcesPath.length())));
        
        this.archive = newArchive;
        
        archiveStreamHandler = new ArchiveURLStreamHandler(this.archive);
    }
    
    public ShrinkWrapResource(Archive<?> archive) {
        this.archive = archive.shallowCopy();
        archiveStreamHandler = new ArchiveURLStreamHandler(this.archive);
    }

    @Override
    public URL getResource(String location) {
        if (getAsset(archive, location) == null) {
            return null;
        }
        
        try {
            return new URL(null, "ShrinkWrap:" + archive.getName() + location, archiveStreamHandler);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public InputStream getResourceAsStream(String location) {
        Asset asset = getAsset(archive, location);
        if (asset == null) {
            return null;
        }
        
        return asset.openStream();
    }
    
    @Override
    public Stream<String> getAllLocations() {
        return getAllLocations(archive);
    }
    
    public Stream<String> getAllLocations(Archive<?> archiveToGetFrom) {
        return 
            archiveToGetFrom.getContent()
                   .keySet()
                   .stream()
                   .map(e -> e.get())
                   .filter(e -> getAsset(archiveToGetFrom, e) != null)                
                   ;
    }
    
    private Asset getAsset(Archive<?> archiveToGetFrom, String location) {
        Node node = getNode(archiveToGetFrom, location);
        if (node == null) {
            return null;
        }

        Asset asset = node.getAsset();
        if (asset == null) {
            return null;
        }
        
        return asset;
    }
    
    private Node getNode(Archive<?> archiveToGetFrom, String location) {
        return archiveToGetFrom.get(
            ArchivePaths.create(
                location));
    }

}
