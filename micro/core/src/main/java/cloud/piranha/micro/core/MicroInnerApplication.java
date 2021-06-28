/*
 * Copyright (c) 2002-2021 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.micro.core;

import cloud.piranha.naming.thread.ThreadInitialContextFactory;
import cloud.piranha.webapp.api.WebApplication;
import cloud.piranha.webapp.impl.CookieParser;
import cloud.piranha.webapp.impl.DefaultWebApplicationRequest;
import cloud.piranha.webapp.impl.DefaultWebApplicationResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * The inner Piranha Micro application.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class MicroInnerApplication implements Consumer<Map<String, Object>> {
    
    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(MicroInnerApplication.class.getName());

    /**
     * Stores the web application.
     */
    private final WebApplication webApplication;

    /**
     * Constructor.
     * 
     * @param webApplication the web application.
     */
    public MicroInnerApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    @Override
    public void accept(Map<String, Object> requestMap) {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(webApplication.getClassLoader());
            ThreadInitialContextFactory.setInitialContext(webApplication.getNamingManager().getContext());
            webApplication.service(copyMapToApplicationRequest(requestMap), copyMapToApplicationResponse(requestMap));

        } catch (ServletException | IOException e) {
            LOGGER.log(Level.WARNING, "An error occurred servicing request", e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
            ThreadInitialContextFactory.removeInitialContext();
        }
    }

    @SuppressWarnings("unchecked")
    private DefaultWebApplicationRequest copyMapToApplicationRequest(Map<String, Object> requestMap) {
        DefaultWebApplicationRequest applicationRequest = new DefaultWebApplicationRequest();

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

        applicationRequest.setWebApplication(webApplication);
        return applicationRequest;
    }

    private static Cookie[] processCookies(DefaultWebApplicationRequest result, String cookiesValue) {
        Cookie[] cookies = CookieParser.parse(cookiesValue);

        Stream.of(cookies)
            .filter(x -> "JSESSIONID".equals(x.getName()))
            .findAny()
            .ifPresent(cookie -> {
                result.setRequestedSessionIdFromCookie(true);
                result.setRequestedSessionId(cookie.getValue());
            });

        return cookies;
    }

    private DefaultWebApplicationResponse copyMapToApplicationResponse(Map<String, Object> requestMap) {
        DefaultWebApplicationResponse response = new DefaultWebApplicationResponse();
        response.setUnderlyingOutputStream((OutputStream) requestMap.get("UnderlyingOutputStream"));
        response.setResponseCloser((Runnable) requestMap.get("ResponseCloser"));
        return response;
    }
}
