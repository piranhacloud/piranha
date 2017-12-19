/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.io.InputStream;
import java.net.URL;

/**
 * The Resource API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface Resource {

    /**
     * Get the resource.
     *
     * @param location the location.
     * @return the URL.
     */
    URL getResource(String location);

    /**
     * Get the resource as a stream.
     *
     * @param location the location.
     * @return the resource as a stream, or null if not found.
     */
    InputStream getResourceAsStream(String location);
}
