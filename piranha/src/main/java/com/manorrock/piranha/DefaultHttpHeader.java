/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.piranha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * The default HttpHeader.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpHeader implements HttpHeader {

    /**
     * Stores the name.
     */
    private final String name;
    /**
     * Stores the values.
     */
    private final ArrayList<String> values = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param name the name.
     * @param value the value.
     */
    public DefaultHttpHeader(String name, String value) {
        this.name = name;
        values.add(value);
    }

    /**
     * Add the value.
     *
     * @param value the value to add.
     */
    @Override
    public void addValue(String value) {
        values.add(value);
    }

    /**
     * Get the name.
     *
     * @return the name.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Get the value.
     *
     * @return the value.
     */
    @Override
    public String getValue() {
        return values.get(0);
    }

    /**
     * Get the values.
     *
     * @return the values.
     */
    @Override
    public Enumeration<String> getValues() {
        return Collections.enumeration(values);
    }
}
