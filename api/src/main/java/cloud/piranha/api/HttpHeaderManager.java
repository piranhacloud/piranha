/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.api;

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
