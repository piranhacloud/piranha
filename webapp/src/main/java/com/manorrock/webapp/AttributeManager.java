/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.util.Enumeration;

/**
 * The AttributeManager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface AttributeManager {

    /**
     * Get the attribute.
     *
     * @param name the name.
     * @return the value.
     */
    Object getAttribute(String name);

    /**
     * Get the attribute names.
     *
     * @return the attribute names.
     */
    Enumeration<String> getAttributeNames();

    /**
     * Remove the attribute.
     *
     * @param name the name.
     */
    void removeAttribute(String name);

    /**
     * Set the attribute.
     *
     * @param name the name.
     * @param value the value.
     */
    void setAttribute(String name, Object value);
}
