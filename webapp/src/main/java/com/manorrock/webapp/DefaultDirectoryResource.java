/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The default DirectoryResource.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultDirectoryResource implements Resource {

    /**
     * Stores the root directory.
     */
    private File rootDirectory;

    /**
     * Constructor.
     */
    public DefaultDirectoryResource() {
    }

    /**
     * Constructor.
     *
     * @param rootDirectory the root directory.
     */
    public DefaultDirectoryResource(File rootDirectory) {
        this.rootDirectory = rootDirectory;
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
            File file = new File(rootDirectory, location);
            if (file.exists()) {
                try {
                    result = new URL(file.toURI().toString());
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
        File file = new File(rootDirectory, location);

        try {
            result = new FileInputStream(file);
        } catch (FileNotFoundException exception) {
        }

        return result;
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
     * Set the root directory.
     *
     * @param rootDirectory the root directory.
     */
    public void setRootDirectory(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }
}
