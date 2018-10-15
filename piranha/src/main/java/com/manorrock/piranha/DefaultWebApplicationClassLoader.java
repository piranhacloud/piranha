/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import com.manorrock.piranha.api.WebApplicationClassLoader;
import com.manorrock.piranha.api.ResourceManager;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default WebApplicationClassLoader.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationClassLoader extends ClassLoader implements WebApplicationClassLoader {

    /**
     * Stores the resource manager.
     */
    private ResourceManager resourceManager;

    /**
     * Stores the loaded classes.
     */
    private final ConcurrentHashMap<String, Class> classes = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    public DefaultWebApplicationClassLoader() {
    }

    /**
     * Constructor.
     *
     * @param baseDirectory the base directory.
     */
    public DefaultWebApplicationClassLoader(File baseDirectory) {
        resourceManager = new DefaultResourceManager();
        File classesDirectory = new File(baseDirectory, "WEB-INF/classes");
        if (classesDirectory.exists()) {
            resourceManager.addResource(new DefaultDirectoryResource(classesDirectory));
        }
        File libDirectory = new File(baseDirectory, "WEB-INF/lib");
        if (libDirectory.exists()) {
            File[] jarFiles = libDirectory.listFiles();
            if (jarFiles != null) {
                for (File jarFile : jarFiles) {
                    resourceManager.addResource(new DefaultJarResource(jarFile));
                }
            }
        }
    }

    /**
     * Constructor.
     *
     * @param resourceManager the resource manager.
     */
    public DefaultWebApplicationClassLoader(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * Load the class.
     *
     * @param name the name.
     * @param resolve the resolve flag.
     * @return the class.
     * @throws ClassNotFoundException when the class cannot be found.
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> result;
        try {
            result = getSystemClassLoader().loadClass(name);
        } catch (ClassNotFoundException cnfe) {
            result = null;
        }
        if (result == null) {
            if (classes.containsKey(name)) {
                result = classes.get(name);
            } else {
                try {
                    String normalizedName = name.replaceAll("\\.", "/") + ".class";
                    BufferedInputStream inputStream = new BufferedInputStream(resourceManager.getResourceAsStream(normalizedName));
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int read = inputStream.read();
                    while (read != -1) {
                        outputStream.write((byte) read);
                        read = inputStream.read();
                    }
                    byte[] bytes = outputStream.toByteArray();
                    result = defineClass(name, bytes, 0, bytes.length);
                    if (resolve) {
                        resolveClass(result);
                    }
                    classes.put(name, result);
                } catch (Throwable throwable) {
                    throw new ClassNotFoundException("Unable to load class: " + name, throwable);
                }
            }
        }
        return result;
    }

    /**
     * Set the resource manager.
     *
     * @param resourceManager the resource manager.
     */
    @Override
    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
}
