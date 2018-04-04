/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The ResourceManager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface ResourceManager {

    /**
     * Add the resource.
     *
     * @param resource the resource.
     */
    void addResource(Resource resource);

    /**
     * Get the resource.
     *
     * @param location the location.
     * @return the URL.
     * @throws MalformedURLException when the location is malformed.
     */
    URL getResource(String location) throws MalformedURLException;

    /**
     * Get the resource as a stream.
     *
     * @param location the location.
     * @return the input stream, or null if not found.
     */
    InputStream getResourceAsStream(String location);
}
