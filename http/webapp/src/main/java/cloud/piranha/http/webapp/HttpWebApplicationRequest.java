/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.http.webapp;

import cloud.piranha.http.api.HttpServerRequest;
import cloud.piranha.core.impl.DefaultWebApplicationRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

/**
 * The HttpServerRequest variant of WebApplicationRequest.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpWebApplicationRequest extends DefaultWebApplicationRequest {

    /**
     * Stores the wrapped HttpServerRequest.
     */
    private final HttpServerRequest wrapped;

    /**
     * Constructor.
     *
     * @param wrapped the wrapped HttpServerRequest.
     */
    public HttpWebApplicationRequest(HttpServerRequest wrapped) {
        this.wrapped = wrapped;
        if (wrapped.getRequestTarget() != null) {
            if (!wrapped.getRequestTarget().contains("?")) {
                setServletPath(wrapped.getRequestTarget());
            } else {
                setServletPath(wrapped.getRequestTarget().substring(0, 
                        wrapped.getRequestTarget().indexOf("?")));
                setQueryString(wrapped.getRequestTarget().substring(
                        wrapped.getRequestTarget().indexOf("?") + 1));
            }
        }
        setInputStream(wrapped.getInputStream());
        if (wrapped.getHeader("Content-Length") != null) {
            setContentLength(Integer.valueOf(wrapped.getHeader("Content-Length")));
        }
    }

    @Override
    public long getDateHeader(String name) {
        long result = -1;
        if (wrapped.getHeader(name) != null) {
            try {
                String value = wrapped.getHeader(name);
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
        return wrapped.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        ArrayList<String> headerNames = new ArrayList<>();
        wrapped.getHeaderNames().forEachRemaining(headerNames::add);
        return Collections.enumeration(headerNames);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        ArrayList<String> headers = new ArrayList<>();
        wrapped.getHeaders(name).forEachRemaining(headers::add);
        return Collections.enumeration(headers);
    }

    @Override
    public int getIntHeader(String name) {
        int result = -1;
        if (wrapped.getHeader(name) != null) {
            try {
                result = Integer.parseInt(wrapped.getHeader(name));
            } catch (NumberFormatException exception) {
                throw new NumberFormatException(
                        "Cannot convert header to an int");
            }
        }
        return result;
    }

    @Override
    public String getMethod() {
        return wrapped.getMethod();
    }

    @Override
    public String getQueryString() {
        if (queryString == null) {
            String requestTarget = wrapped.getRequestTarget();
            if (requestTarget.contains("?")) {
                queryString = requestTarget.substring(requestTarget.indexOf("?") + 1);
            }
        }
        return queryString;
    }
}
