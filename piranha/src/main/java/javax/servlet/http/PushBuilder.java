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
package javax.servlet.http;

import java.util.Set;

/**
 * The push builder API.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface PushBuilder {

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     * @return the push builder.
     */
    PushBuilder addHeader(String name, String value);

    /**
     * Get the header.
     *
     * @param name the name.
     * @return the value.
     */
    String getHeader(String name);

    /**
     * Get the header names.
     *
     * @return the header names.
     */
    Set<String> getHeaderNames();

    /**
     * Get the HTTP method.
     *
     * @return the HTTP method.
     */
    String getMethod();

    /**
     * Get the path.
     * 
     * @return the path.
     */
    String getPath();

    /**
     * Get the query string.
     *
     * @return the query string.
     */
    String getQueryString();

    /**
     * Get the session id.
     *
     * @return the session id.
     */
    String getSessionId();

    /**
     * Set the HTTP method.
     *
     * @param method the method.
     * @return the push builder.
     */
    PushBuilder method(String method);

    /**
     * Set the path.
     *
     * @param path the path.
     * @return the push builder.
     */
    PushBuilder path(String path);

    /**
     * Push.
     */
    void push();

    /**
     * Set the query string.
     *
     * @param queryString the query string.
     * @return the push builder.
     */
    PushBuilder queryString(String queryString);

    /**
     * Remove the header.
     *
     * @param name the name.
     * @return the push builder.
     */
    PushBuilder removeHeader(String name);

    /**
     * Set the session id.
     *
     * @param sessionId the session id.
     * @return the push builder.
     */
    PushBuilder sessionId(String sessionId);

    /**
     * Set the header.
     *
     * @param name the name.
     * @param value the value.
     * @return the push builder.
     */
    PushBuilder setHeader(String name, String value);
}
