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
package com.manorrock.piranha.test.utils;

import static java.util.regex.Pattern.quote;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;

import com.manorrock.piranha.api.WebApplication;

/**
 * A test web app
 *
 * @author Arjan Tijms
 */
public class TestWebApp {
    
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
    public String getFromServerPath(String path) throws IOException {
        
        String servletPath = path;
        Map<String, String> parameters = Collections.emptyMap();
        if (path.contains("?")) {
            String[] splitPath = path.split(quote("?"));
            servletPath = splitPath[0];
            parameters = toParameterMap(splitPath[1]);
        }
        
        try {
            TestHttpServletRequest request = new TestHttpServletRequest(webApp, "", servletPath);
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                request.setParameter(parameter.getKey(), new String[] { parameter.getValue() });
            }
            TestHttpServletResponse response = new TestHttpServletResponse();
        
            webApp.service(request, response);
            
            return response.getResponseBodyAsString();
        } catch (ServletException e) {
            throw new IOException(e);
        }
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
