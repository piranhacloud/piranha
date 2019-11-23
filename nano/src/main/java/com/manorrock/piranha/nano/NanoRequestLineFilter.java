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
package com.manorrock.piranha.nano;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The filter that parses out the HTTP request line.
 *
 * <p>
 * This filter will parse the method, servlet path and query string from the
 * input stream so the rest of the filter chain has access to it.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NanoRequestLineFilter implements Filter {

    /**
     * Filter the request.
     *
     * @param servletRequest the request.
     * @param servletResponse the response.
     * @param chain the filter chain.
     * @throws IOException when an I/O error occurs.
     * @throws ServletException when a Servlet error occurs.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain chain) throws IOException, ServletException {
        NanoHttpServletRequest request = (NanoHttpServletRequest) servletRequest;
        InputStream inputStream = request.getUnderlyingInputStream();
        StringBuilder line = new StringBuilder();
        int read = inputStream.read();
        while (read != -1 && inputStream.available() > 0) {
            if ('\r' != (char) read) {
                line.append((char) read);
            }
            read = inputStream.read();
            if ('\n' == (char) read) {
                if (line.length() > 0) {
                    String requestLine = line.toString();
                    int index = requestLine.indexOf(' ');
                    request.setMethod(requestLine.substring(0, index));
                    requestLine = requestLine.substring(index + 1);
                    index = requestLine.indexOf(' ');
                    String servletPath = requestLine.substring(0, index);
                    if (servletPath.contains("?")) {
                        String queryString = servletPath.substring(servletPath.indexOf("?") + 1);
                        servletPath = servletPath.substring(0, servletPath.indexOf("?"));
                        request.setQueryString(queryString);
                    }
                    request.setServletPath(servletPath);
                    break;
                }
            }
        }
        chain.doFilter(servletRequest, servletResponse);
    }
}
