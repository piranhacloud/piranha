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
package com.manorrock.piranha.grizzly;

import com.manorrock.piranha.WebApplicationRequest;
import java.util.ArrayList;
import javax.servlet.http.Cookie;
import org.glassfish.grizzly.http.server.Request;

/**
 * The Grizzly WebApplicationRequest.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class GrizzlyWebApplicationRequest extends WebApplicationRequest {

    /**
     * Stores the request URI.
     */
    private String requestUri;

    /**
     * Constructor.
     *
     * @param request the Grizzly Request.
     */
    public GrizzlyWebApplicationRequest(Request request) {
        if (request.getQueryString() != null) {
            queryString = request.getQueryString();
        }
        request.getHeaderNames().forEach(
                (name) -> {
                    String unparsedValue = request.getHeader(name);
                    headerManager.setHeader(name, unparsedValue);
                    if (name.equalsIgnoreCase("COOKIE")) {
                        ArrayList<Cookie> cookieList = new ArrayList<>();
                        String[] cookieCandidates = unparsedValue.split(";");
                        if (cookieCandidates.length > 0) {
                            for (String cookieCandidate : cookieCandidates) {
                                String[] cookieString = cookieCandidate.split("=");
                                Cookie cookie = new Cookie(cookieString[0].trim(), cookieString[1].trim());
                                cookieList.add(cookie);
                            }
                        }
                        cookies = cookieList.toArray(new Cookie[0]);
                    }
                }
        );
        if (request.getContentType() != null) {
            contentType = request.getContentType();
        }
        if (request.getContentLength() != -1) {
            contentLength = request.getContentLength();
        }
        if (request.getRequestedSessionId() != null) {
            if (request.isRequestedSessionIdFromCookie()) {
                requestedSessionIdFromCookie = true;
            }
            requestedSessionId = request.getRequestedSessionId();
        }
    }

    /**
     * Get the request URI.
     *
     * @return the request URI.
     */
    @Override
    public String getRequestURI() {
        return requestUri;
    }
}
