/*
 * Copyright (c) 2002-2017, Manorrock.com. All Rights Reserved.
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
    public void parse(DefaultHttpServletRequest request) {
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
    protected void parseHeader(DefaultHttpServletRequest request, String line) {
        String name = line.substring(0, line.indexOf(":")).trim();
        String value = line.substring(line.indexOf(":") + 1).trim();
        request.setHeader(name, value);

        if (name.equals("Content-Type")) {
            request.setContentType(value);
        }

        if (name.equals("Content-Length")) {
            request.setContentLength(Integer.valueOf(value));
        }
    }

    /**
     * Parse the request line.
     *
     * @param request the request.
     * @param line the request line.
     */
    protected void parseRequestLine(DefaultHttpServletRequest request, String line) {
        int index = line.indexOf(" ");
        String method = line.substring(0, index);
        request.setMethod(method);
        line = line.substring(index + 1);
        index = line.indexOf(" ");
        String requestUri = line.substring(0, index);
        request.setContextPath(requestUri);
    }
}
