/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * The default ResourceManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultResourceManager implements ResourceManager {

    /**
     * Stores the resources.
     */
    private final ArrayList<Resource> resources = new ArrayList<>();

    /**
     * Add resource.
     *
     * @param resource the resource.
     */
    @Override
    public void addResource(Resource resource) {
        this.resources.add(resource);
    }

    /**
     * Get the resource URL.
     *
     * @param location the location.
     * @return the URL, or null if not found.
     * @throws MalformedURLException when the location URL is malformed.
     */
    @Override
    public URL getResource(String location) throws MalformedURLException {
        URL result = null;

        Iterator<Resource> iterator = resources.iterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            result = resource.getResource(location);
            if (result != null) {
                break;
            }
        }

        if (result == null) {
            if (location != null) {
                result = getClass().getResource(location);
            }
        }

        return result;
    }

    /**
     * Get the resource as a stream.
     *
     * @param location the location.
     * @return the input stream, or null if not found.
     */
    @Override
    public InputStream getResourceAsStream(String location) {
        InputStream result = null;
        Iterator<Resource> iterator = resources.iterator();
        while (iterator.hasNext()) {
            Resource resource = iterator.next();
            result = resource.getResourceAsStream(location);
            if (result != null) {
                break;
            }
        }

        if (result == null) {
            result = getClass().getResourceAsStream(location);
        }

        return result;
    }
}
