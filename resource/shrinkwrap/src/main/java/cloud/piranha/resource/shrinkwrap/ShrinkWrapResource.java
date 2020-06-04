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
package cloud.piranha.resource.shrinkwrap;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Collection;
import java.util.stream.Stream;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import cloud.piranha.resource.api.Resource;

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
    public URL getResource(String url) {
        String location = getLocationFromUrl(url);
        if (location == null) {
            return null;
        }
        
        Node node = getNode(archive, location);
        if (node == null) {
            return null;
        }
        
        URLStreamHandler streamHandler = archiveStreamHandler;
        Asset asset = node.getAsset();
        if (asset == null) {
            // Node was a directory
            streamHandler = new NodeURLStreamHandler(getContentFromNode(node));
        }
        
        try {
            return new URL(null, 
                "shrinkwrap://" + archive.getName() + (location.startsWith("/")? "" : archive.getName().endsWith("/")? "" : "/") + location, 
                streamHandler);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public InputStream getResourceAsStream(String url) {
        return getResourceAsStreamByLocation(getLocationFromUrl(url));
    }
    
    public InputStream getResourceAsStreamByLocation(String location) {
        if (location == null) {
            return null;
        }
        
        Node node = getNode(archive, location);
        if (node == null) {
            return null;
        }
        
        Asset asset = node.getAsset();
        if (asset == null) {
            // Node was a directory
            return new ShrinkWrapDirectoryInputStream(getContentFromNode(node));
        }
        
        // Node was an asset
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
    
    private Collection<Node> getContentFromNode(Node node) {
        return 
            archive.getContent(
                        e -> e.get().startsWith(node.getPath().get()))
                   .values();
    }
    
    private String getLocationFromUrl(String url) {
        if (url == null) {
            return null;
        }
        
        if (!url.contains("shrinkwrap://")) {
            // Already a relative URL, so should be the location
            return url;
        }
        
        // Relative URL: [shrinkwrap://][jar name][location]
        try {
            URL archiveURL = new URL(url.substring(url.indexOf("shrinkwrap://")));
            
            String archiveName = archiveURL.getHost();
            if (!archive.getName().equals(archiveName)) {
                return null;
            }
            
            return archiveURL.getPath().replace("//", "/");
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
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
