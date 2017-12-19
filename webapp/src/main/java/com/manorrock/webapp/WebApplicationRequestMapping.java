/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

/**
 * The WebApplicationRequestMapping API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationRequestMapping {

    /**
     * Get the path.
     *
     * @return the path.
     */
    public String getPath();

    /**
     * Is this an exact match.
     *
     * @return true it it is, false otherwise.
     */
    public boolean isExact();

    /**
     * Is this an extension match.
     *
     * @return true if it is, false otherwise.
     */
    public boolean isExtension();
}
