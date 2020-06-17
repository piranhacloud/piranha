/*
 * Copyright (c) 2002-2020 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.webapp.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import cloud.piranha.webapp.api.HttpHeader;

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
