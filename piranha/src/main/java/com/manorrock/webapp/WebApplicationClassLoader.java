/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

/**
 * The WebApplicationClassLoader API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface WebApplicationClassLoader {

    /**
     * Set the resource manager.
     *
     * @param resourceManager the resource manager.
     */
    public void setResourceManager(ResourceManager resourceManager);
}
