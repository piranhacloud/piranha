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
package cloud.piranha.test.utils;

import static java.util.regex.Pattern.quote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import cloud.piranha.DefaultHttpHeader;
import cloud.piranha.DefaultWebApplicationRequest;
import cloud.piranha.DefaultWebApplicationResponse;
import cloud.piranha.api.WebApplication;
import java.io.ByteArrayOutputStream;

/**
 * A test web app
 *
 * @author Arjan Tijms
 */
public class TestWebApp {
    
    private List<Cookie> cookies = new ArrayList<>();
    
    /**
     * The underlying web application
     */
    private WebApplication webApp;
    
    public TestWebApp(WebApplication webApp) {
        this.webApp = webApp;
    }
    
    /**
     * Gets a response as string from the server at the given path
     * 
     * @param path the servlet path
     * @return the response as a string
     * @throws IOException when something goes wrong
     */
    public String getFromServerPath(String path, DefaultHttpHeader... headers) throws IOException {
        return fromServerPath("GET", path, headers);
    }
    
    public String postToServerPath(String path, DefaultHttpHeader... headers) throws IOException {
        return fromServerPath("POST", path, headers);
    }
    
    public String fromServerPath(String method, String path, DefaultHttpHeader... headers) throws IOException {
        DefaultWebApplicationResponse response = responseFromServerPath(method, path, headers);
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) response.getUnderlyingOutputStream();
        return new String(outputStream.toByteArray());
    }
    
    public DefaultWebApplicationResponse getResponseFromServerPath(String path, DefaultHttpHeader... headers) throws IOException {
        return responseFromServerPath("GET", path, headers);
    }
    
    public DefaultWebApplicationResponse responseFromServerPath(String method, String path, DefaultHttpHeader... headers) throws IOException {
        
        String servletPath = path;

        if (!servletPath.startsWith("/")) {
            servletPath = "/" + servletPath;
        }
        
        Map<String, String> parameters = Collections.emptyMap();
        if (servletPath.contains("?")) {
            String[] splitPath = servletPath.split(quote("?"));
            servletPath = splitPath[0];
            parameters = toParameterMap(splitPath[1]);
        }
        
        try (DefaultWebApplicationRequest request = new DefaultWebApplicationRequest();
                DefaultWebApplicationResponse response = new DefaultWebApplicationResponse()) {
            
            request.setWebApplication(webApp);
            request.setContextPath("");
            request.setServletPath(servletPath);
            request.setMethod(method);
            
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                request.setParameter(parameter.getKey(), new String[] { parameter.getValue() });
            }
            
            List<Cookie> requestCookies = new ArrayList<>();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    request.setRequestedSessionIdFromCookie(true);
                    request.setRequestedSessionId(cookie.getValue());
                } else {
                    requestCookies.add(cookie);
                }
            }
            
            request.setCookies(requestCookies.toArray(new Cookie[0]));
            
            for (DefaultHttpHeader header : headers) {
                request.setHeader(header.getName(), header.getValue());
            }
            
            response.setUnderlyingOutputStream(new ByteArrayOutputStream());
        
            webApp.service(request, response);
                        
            // Very basic impl. Better implementation should look atleast at expiration
            cookies.addAll(response.getCookies());
            
            return response;
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }
    
    public List<Cookie> getCookies() {
        return cookies;
    }
    
    /**
     * Creates a key value map from a query string
     * @param queryString the query string
     * @return map with keys and values corresponding to the query parameters
     */
    public Map<String, String> toParameterMap(String queryString) {
        String[] parameters = queryString.split(quote("&"));
        Map<String, String> parameterMap = new LinkedHashMap<>(parameters.length);

        for (String parameter : parameters) {
            if (parameter.contains("=")) {
                String[] pair = parameter.split(quote("="));
                String key = pair[0];
                String value = (pair.length > 1 && pair[1] != null) ? pair[1] : "";
                parameterMap.put(key, value);
            } else {
                parameterMap.put(parameter, "");
            }
        }

        return parameterMap;
    }
   

}
