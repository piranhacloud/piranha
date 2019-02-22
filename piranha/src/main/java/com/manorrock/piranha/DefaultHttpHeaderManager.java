/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha;

import com.manorrock.piranha.api.HttpHeaderManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * The default HttpHeaderManager.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpHeaderManager implements HttpHeaderManager {

    /**
     * Stores the headers.
     */
    protected final HashMap<String, DefaultHttpHeader> headers;

    /**
     * Constructor.
     */
    public DefaultHttpHeaderManager() {
        headers = new HashMap<>();
    }

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void addHeader(String name, String value) {
        if (headers.containsKey(name.toUpperCase())) {
            headers.get(name.toUpperCase()).addValue(value);
        } else {
            DefaultHttpHeader header = new DefaultHttpHeader(name, value);
            headers.put(name.toUpperCase(), header);
        }
    }

    /**
     * Contains the given header.
     *
     * @param name the header name.
     * @return true if there, false otherwise.
     */
    @Override
    public boolean containsHeader(String name) {
        return headers.containsKey(name.toUpperCase());
    }

    /**
     * Get the date header.
     *
     * @param name the header name.
     * @return the date header.
     */
    @Override
    public long getDateHeader(String name) throws IllegalArgumentException {
        long result = -1;
        if (headers.containsKey(name.toUpperCase())) {
            DefaultHttpHeader header = headers.get(name.toUpperCase());
            try {
                String value = header.getValue();
                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                result = format.parse(value).getTime();
            } catch (ParseException exception) {
                throw new IllegalArgumentException(
                        "Cannot convert header to a date", exception);
            }
        }
        return result;
    }

    /**
     * Get the header.
     *
     * @param name the header name.
     * @return the header value.
     */
    @Override
    public String getHeader(String name) {
        String result = null;
        if (headers.containsKey(name.toUpperCase())) {
            result = headers.get(name.toUpperCase()).getValue();
        }
        return result;
    }

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = new ArrayList<>();
        Iterator<DefaultHttpHeader> iterator = headers.values().iterator();

        while (iterator.hasNext()) {
            DefaultHttpHeader header = iterator.next();
            names.add(header.getName());
        }

        return Collections.enumeration(names);
    }

    /**
     * Get the headers.
     *
     * @param name the header name.
     * @return the header values.
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        Enumeration<String> result = null;

        if (headers.containsKey(name.toUpperCase())) {
            result = headers.get(name.toUpperCase()).getValues();
        }

        return result;
    }

    /**
     * Get the int header.
     *
     * @param name the header name.
     * @return the int header.
     */
    @Override
    public int getIntHeader(String name) throws NumberFormatException {
        int result = -1;
        if (headers.containsKey(name.toUpperCase())) {
            DefaultHttpHeader header = headers.get(name.toUpperCase());
            try {
                result = Integer.parseInt(header.getValue());
            } catch (NumberFormatException exception) {
                throw new NumberFormatException(
                        "Cannot convert header to an int");
            }
        }
        return result;
    }

    /**
     * Set the header.
     *
     * @param name the name.
     * @param value the value (string).
     */
    @Override
    public void setHeader(String name, String value) {
        DefaultHttpHeader header = new DefaultHttpHeader(name, value);
        headers.put(name.toUpperCase(), header);
    }
}
