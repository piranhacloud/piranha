/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.util.Enumeration;

/**
 * The HttpHeader API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpHeader {

    /**
     * Add the value.
     *
     * @param value the value to add.
     */
    void addValue(String value);

    /**
     * Get the name.
     *
     * @return the name.
     */
    String getName();

    /**
     * Get the value.
     *
     * @return the value.
     */
    String getValue();

    /**
     * Get the values.
     *
     * @return the values.
     */
    Enumeration<String> getValues();
}
