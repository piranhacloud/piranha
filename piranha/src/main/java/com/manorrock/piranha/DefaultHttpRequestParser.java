/*
 *  Copyright (c) 2002-2019, Manorrock.com. All Rights Reserved.
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

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;

/**
 * The default HttpRequestParser.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpRequestParser {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultHttpRequestParser.class.getName());

    /**
     * Parse the request.
     *
     * @param request the request.
     */
    public void parse(DefaultWebApplicationRequest request) {
        try {
            DataInputStream reader = new DataInputStream(request.getInputStream());
            String line = reader.readLine();
            if (line != null) {
                parseRequestLine(request, line);

                line = reader.readLine();
                while (line != null && !line.equals("")) {
                    if (line.toUpperCase().contains("COOKIE")) {
                        String value = line.substring(line.indexOf(":") + 1).trim();
                        ArrayList<Cookie> cookies = new ArrayList<>();
                        String[] cookieCandidates = value.split(";");
                        if (cookieCandidates.length > 0) {
                            for (int i = 0; i < cookieCandidates.length; i++) {
                                String[] cookieString = cookieCandidates[i].split("=");
                                Cookie cookie = new Cookie(cookieString[0].trim(), cookieString[1].trim());
                                if (cookie.getName().equals("JSESSIONID")) {
                                    request.setRequestedSessionIdFromCookie(true);
                                    request.setRequestedSessionId(cookie.getValue());
                                } else {
                                    cookies.add(cookie);
                                }
                            }
                        }
                        request.setCookies(cookies.toArray(new Cookie[0]));
                    } else {
                        parseHeader(request, line);
                    }
                    line = reader.readLine();
                }
            }

            if (request.getContentType() != null
                    && request.getContentType().equals("application/x-www-form-urlencoded")) {

                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                InputStream inputStream = request.getInputStream();

                int read = inputStream.read();
                int index = 0;
                while (read != -1) {
                    byteOutput.write(read);
                    read = inputStream.read();
                    index++;
                    if (index + 1 == request.getContentLength()) {
                        break;
                    }
                }
                byteOutput.write(read);
                String parameterString = new String(byteOutput.toByteArray());
                String[] pairs = parameterString.trim().split("&");
                if (pairs != null) {
                    for (int i = 0; i < pairs.length; i++) {
                        String[] pair = pairs[i].trim().split("=");
                        if (pair.length == 2) {
                            pair[0] = URLDecoder.decode(pair[0], "UTF-8");
                            pair[1] = URLDecoder.decode(pair[1], "UTF-8");
                            request.setParameter(pair[0], new String[]{pair[1]});
                        } else {
                            pair[0] = URLDecoder.decode(pair[0], "UTF-8");
                            request.setParameter(pair[0], new String[]{""});
                        }
                    }
                }
            }

        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "Unable to parse HTTP request", ioe);
        }
    }

    /**
     * Parse the header.
     *
     * @param request the request.
     * @param line the header line.
     */
    protected void parseHeader(DefaultWebApplicationRequest request, String line) {
        String name = line.substring(0, line.indexOf(":")).trim();
        String value = line.substring(line.indexOf(":") + 1).trim();
        request.setHeader(name, value);
        if (name.equals("Content-Type")) {
            request.setContentType(value);
        }
        if (name.equals("Content-Length")) {
            request.setContentLength(Integer.parseInt(value));
        }
    }

    /**
     * Parse the request line.
     *
     * @param request the request.
     * @param line the request line.
     */
    protected void parseRequestLine(DefaultWebApplicationRequest request, String line) {
        int index = line.indexOf(" ");
        String method = line.substring(0, index);
        request.setMethod(method);
        line = line.substring(index + 1);
        index = line.indexOf(" ");
        String requestUri = line.substring(0, index);
        request.setContextPath(requestUri);
    }
}
