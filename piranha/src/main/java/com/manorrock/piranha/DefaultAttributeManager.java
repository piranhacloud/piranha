/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default AttributeManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultAttributeManager implements AttributeManager {

    /**
     * Stores the attributes.
     */
    protected Map<String, Object> attributes;

    /**
     * Constructor.
     */
    public DefaultAttributeManager() {
        attributes = new ConcurrentHashMap<>();
    }

    /**
     * Get the attribute.
     *
     * @param name the name.
     * @return the value.
     */
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * Get the attribute names.
     *
     * @return the attribute names.
     */
    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    /**
     * Remove the attribute.
     *
     * @param name the name.
     */
    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * Set the attribute.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setAttribute(String name, Object value) {
        if (value != null) {
            attributes.put(name, value);
        } else {
            attributes.remove(name);
        }
    }
}
