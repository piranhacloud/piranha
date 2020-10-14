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
package cloud.piranha.appserver.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import cloud.piranha.appserver.api.WebApplicationServerRequest;
import cloud.piranha.webapp.impl.DefaultWebApplicationRequest;

/**
 * The default WebApplicationServerRequest.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultWebApplicationServerRequest extends DefaultWebApplicationRequest implements WebApplicationServerRequest {

    /**
     * Constructor a request from the request map.
     * 
     * @param requestMap the request map.
     * @return the request.
     */
    @SuppressWarnings("unchecked")
    public static DefaultWebApplicationServerRequest fromMap(Map<String, Object> requestMap) {
        DefaultWebApplicationServerRequest applicationRequest = new DefaultWebApplicationServerRequest();

        applicationRequest.setLocalAddr((String) requestMap.get("Address"));
        applicationRequest.setLocalName((String) requestMap.get("LocalName"));
        applicationRequest.setLocalPort((Integer) requestMap.get("LocalPort"));
        applicationRequest.setRemoteAddr((String) requestMap.get("RemoteAddr"));
        applicationRequest.setRemoteHost((String) requestMap.get("RemoteHost"));
        applicationRequest.setRemotePort((int) requestMap.get("RemotePort"));
        applicationRequest.setServerName((String) requestMap.get("ServerName"));
        applicationRequest.setServerPort((Integer) requestMap.get("ServerPort"));
        applicationRequest.setMethod((String) requestMap.get("Method"));
        applicationRequest.setContextPath((String) requestMap.get("ContextPath"));
        applicationRequest.setServletPath((String) requestMap.get("ServletPath"));
        applicationRequest.setQueryString((String) requestMap.get("QueryString"));
        applicationRequest.setInputStream((InputStream) requestMap.get("InputStream"));

        for (Map.Entry<String, List<String>> headerEntry : ((Map<String, List<String>>) requestMap.get("Headers")).entrySet()) {
            String name = headerEntry.getKey();
            List<String> values = headerEntry.getValue();
            for (String value : values) {
                applicationRequest.setHeader(name, value);

                if (name.equalsIgnoreCase("Content-Type")) {
                    applicationRequest.setContentType(value);
                }
                if (name.equalsIgnoreCase("Content-Length")) {
                    applicationRequest.setContentLength(Integer.parseInt(value));
                }
                if (name.equalsIgnoreCase("COOKIE")) {
                    applicationRequest.setCookies(processCookies(applicationRequest, value));
                }
            }
        }

        return applicationRequest;
    }

    private static Cookie[] processCookies(DefaultWebApplicationServerRequest result, String cookiesValue) {
        ArrayList<Cookie> cookieList = new ArrayList<>();
        String[] cookieCandidates = cookiesValue.split(";");
        for (String cookieCandidate : cookieCandidates) {
            String[] cookieString = cookieCandidate.split("=");
            String cookieName = cookieString[0].trim();
            String cookieValue = null;

            if (cookieString.length == 2) {
                cookieValue = cookieString[1].trim();
            }

            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookie.getName().equals("JSESSIONID")) {
                result.setRequestedSessionIdFromCookie(true);
                result.setRequestedSessionId(cookie.getValue());
            } else {
                cookieList.add(cookie);
            }
        }

        return cookieList.toArray(new Cookie[0]);
    }


}
