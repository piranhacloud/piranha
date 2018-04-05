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

import java.io.InputStream;
import java.util.Iterator;

/**
 * The HttpServerRequest API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpServerRequest {

    /**
     * Get the header.
     *
     * @param name.
     * @return the value, or null.
     */
    String getHeader(String name);

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    Iterator<String> getHeaderNames();

    /**
     * Get the input stream.
     *
     * @return the input stream.
     */
    InputStream getInputStream();

    /**
     * Get the local address.
     *
     * @return the local address.
     */
    String getLocalAddress();

    /**
     * Get the local hostname.
     *
     * @return the local hostname.
     */
    String getLocalHostname();

    /**
     * Get the local port.
     *
     * @return the local port.
     */
    int getLocalPort();

    /**
     * Get the method.
     *
     * @return the method.
     */
    String getMethod();
    
    /**
     * Get the query parameter.
     * 
     * @param name the name.
     * @return the value, or null if not found.
     */
    String getQueryParameter(String name);
    
    /**
     * Get the query string.
     * 
     * @return the query string.
     */
    String getQueryString();

    /**
     * Get the remote address.
     *
     * @return the remote address.
     */
    String getRemoteAddress();
    
    /**
     * Get the remote hostname.
     * 
     * @return the remote hostname.
     */
    String getRemoteHostname();

    /**
     * Get the remote port.
     *
     * @return the remote port.
     */
    int getRemotePort();

    /**
     * Get the request target.
     *
     * @return the request target.
     */
    String getRequestTarget();
}
