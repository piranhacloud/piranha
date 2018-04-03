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

            if (location != null) {
                File file = new File(rootDirectory, location);
                if (file.exists()) {
                    try {
                        result = file.toURI().toURL();
                    } catch (MalformedURLException exception) {
                    }
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
