/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   3. Neither the name of the copyright holder nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package cloud.piranha.core.impl;

import cloud.piranha.core.api.HttpHeaderManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
     * Stores the Locale.
     */
    private Locale locale;

    /**
     * Constructor.
     */
    public DefaultHttpHeaderManager() {
        headers = new HashMap<>();
        locale = new Locale("en", "US", "ISO-8859-1");
    }

    @Override
    public void addHeader(String name, String value) {
        if (headers.containsKey(name.toUpperCase(locale))) {
            headers.get(name.toUpperCase(locale)).addValue(value);
        } else {
            DefaultHttpHeader header = new DefaultHttpHeader(name, value);
            headers.put(name.toUpperCase(locale), header);
        }
    }

    @Override
    public boolean containsHeader(String name) {
        return headers.containsKey(name.toUpperCase(locale));
    }

    @Override
    public long getDateHeader(String name) throws IllegalArgumentException {
        long result = -1;
        if (headers.containsKey(name.toUpperCase(locale))) {
            DefaultHttpHeader header = headers.get(name.toUpperCase(locale));
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

    @Override
    public String getHeader(String name) {
        String result = null;
        if (headers.containsKey(name.toUpperCase(locale))) {
            result = headers.get(name.toUpperCase(locale)).getValue();
        }
        return result;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = new ArrayList<>();
        for (DefaultHttpHeader header : headers.values()) {
            names.add(header.getName());
        }
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        Enumeration<String> result = Collections.enumeration(Collections.emptyList());
        if (headers.containsKey(name.toUpperCase(locale))) {
            result = headers.get(name.toUpperCase(locale)).getValues();
        }
        return result;
    }

    @Override
    public int getIntHeader(String name) throws NumberFormatException {
        int result = -1;
        if (headers.containsKey(name.toUpperCase(locale))) {
            DefaultHttpHeader header = headers.get(name.toUpperCase(locale));
            try {
                result = Integer.parseInt(header.getValue());
            } catch (NumberFormatException exception) {
                throw new NumberFormatException(
                        "Cannot convert header to an int");
            }
        }
        return result;
    }

    @Override
    public void removeHeader(String name) {
        headers.remove(name.toUpperCase());
    }

    @Override
    public void setHeader(String name, String value) {
        DefaultHttpHeader header = new DefaultHttpHeader(name, value);
        headers.put(name.toUpperCase(locale), header);
    }
}
