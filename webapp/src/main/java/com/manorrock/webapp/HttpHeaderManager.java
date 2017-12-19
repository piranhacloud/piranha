/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
 */
package com.manorrock.webapp;

import java.util.Enumeration;

/**
 * The HttpHeaderManager API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpHeaderManager {

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     */
    public void addHeader(String name, String value);

    /**
     * Contains the given header.
     *
     * @param name the header name.
     * @return true if there, false otherwise.
     */
    public boolean containsHeader(String name);

    /**
     * Get the date header.
     *
     * @param name the header name.
     * @return the date header.
     * @throws IllegalArgumentException when the header could not be converted
     * to a date.
     */
    public long getDateHeader(String name) throws IllegalArgumentException;

    /**
     * Get the header.
     *
     * @param name the header name.
     * @return the header value.
     */
    public String getHeader(String name);

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    public Enumeration<String> getHeaderNames();

    /**
     * Get the headers.
     *
     * @param name the header name.
     * @return the header values.
     */
    public Enumeration<String> getHeaders(String name);

    /**
     * Get the int header.
     *
     * @param name the header name.
     * @return the int value.
     * @throws NumberFormatException when the value could not be converted to an
     * int.
     */
    public int getIntHeader(String name) throws NumberFormatException;

    /**
     * Set the header.
     *
     * @param name the name.
     * @param value the value (string).
     */
    public void setHeader(String name, String value);
}
