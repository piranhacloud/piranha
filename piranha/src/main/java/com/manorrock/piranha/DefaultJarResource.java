/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The default JarResource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultJarResource implements Resource {

    /**
     * Stores the JAR file.
     */
    private File jarFile;

    /**
     * Constructor.
     */
    public DefaultJarResource() {
    }

    /**
     * Constructor.
     *
     * @param jarFile the JAR file.
     */
    public DefaultJarResource(File jarFile) {
        this.jarFile = jarFile;
    }

    /*
     * Get the resource.
     *
     * @return the resource.
     */
    @Override
    public URL getResource(String location) {
        URL result = null;

        if (location != null) {
            try {
                result = new URL("jar://" + jarFile.toURI() + "!" + location);
            } catch (MalformedURLException mue) {
                result = null;
            }
        }

        return result;
    }

    /**
     * Get the resource as a stream.
     *
     * <p>
     * Note that this method will read the content of a JAR entry into a
     * byte-array to avoid locking the JAR file.
     * </p>
     *
     * @param location the resource location.
     * @return the input stream, or null if not found.
     * @see Resource#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(String location) {
        InputStream result = null;
        JarFile jar = null;

        try {
            jar = new JarFile(jarFile);
            JarEntry entry = jar.getJarEntry(location);
            if (entry != null) {
                InputStream inputStream;
                try (InputStream jarInputStream = jar.getInputStream(entry)) {
                    inputStream = new BufferedInputStream(jarInputStream);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int read = inputStream.read();
                    while (read != -1) {
                        outputStream.write((byte) read);
                        read = inputStream.read();
                    }
                    result = new ByteArrayInputStream(outputStream.toByteArray());
                }
                inputStream.close();
            }
        } catch (IOException exception) {
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException ioe) {
                }
            }
        }

        return result;
    }

    /**
     * Get the JAR file.
     *
     * @return the JAR file.
     */
    public File getJarFile() {
        return this.jarFile;
    }

    /**
     * Set the JAR file.
     *
     * @param jarFile the JAR file.
     */
    public void setJarFile(File jarFile) {
        this.jarFile = jarFile;
    }
}
