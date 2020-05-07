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
package cloud.piranha.http.impl;

import cloud.piranha.http.api.HttpServerRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.logging.Level.WARNING;
import java.util.logging.Logger;

/**
 * The default implementation of HTTP Server Request.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServerRequest implements HttpServerRequest {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(
            DefaultHttpServerRequest.class.getPackageName());

    /**
     * Stores the headers.
     */
    private final Map<String, String> headers;

    /**
     * Stores the input stream.
     */
    private InputStream inputStream;

    /**
     * Stores the method.
     */
    private String method;

    /**
     * Stores the query parameters.
     */
    private Map<String, List<String>> queryParameters;

    /**
     * Stores the query string.
     */
    private String queryString;

    /**
     * Stores the request target.
     */
    private String requestTarget;

    /**
     * Stores the socket.
     */
    private final Socket socket;

    /**
     * Constructor.
     *
     * @param socket the socket.
     */
    public DefaultHttpServerRequest(Socket socket) {
        this.headers = new ConcurrentHashMap<>(1);
        this.socket = socket;
        parse();
    }

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     */
    public void addHeader(String name, String value) {
        if (!headers.containsKey(name)) {
            headers.put(name, value);
        } else {
            headers.put(name, headers.get(name) + "," + value);
        }
    }

    /**
     * @see HttpServerRequest#getHeader(java.lang.String) 
     */
    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    /**
     * @see HttpServerRequest#getHeaderNames() 
     */
    @Override
    public Iterator<String> getHeaderNames() {
        return headers.keySet().iterator();
    }

    /**
     * @see HttpServerRequest#getInputStream() 
     */
    @Override
    public InputStream getInputStream() {
        InputStream result = inputStream;

        if (inputStream == null) {
            try {
                inputStream = socket.getInputStream();
                result = inputStream;
            } catch (IOException exception) {
                if (LOGGER.isLoggable(WARNING)) {
                    LOGGER.log(WARNING, "An I/O error occurred while acquiring input stream", exception);
                }
            }
        }

        return result;
    }

    /**
     * @see HttpServerRequest#getLocalAddress() 
     */
    @Override
    public String getLocalAddress() {
        return socket.getLocalAddress().getHostAddress();
    }

    /**
     * @see HttpServerRequest#getLocalHostname() 
     */
    @Override
    public String getLocalHostname() {
        return socket.getLocalAddress().getHostName();
    }

    /**
     * @see HttpServerRequest#getLocalPort() 
     */
    @Override
    public int getLocalPort() {
        return socket.getLocalPort();
    }

    /**
     * @see HttpServerRequest#getMethod() 
     */
    @Override
    public String getMethod() {
        return method;
    }

    /**
     * @see HttpServerRequest#getQueryParameter(java.lang.String) 
     */
    @Override
    public String getQueryParameter(String name) {
        String result = null;
        synchronized (this) {
            if (queryParameters == null && queryString != null) {
                queryParameters = new HashMap<>();
                String[] params = queryString.split("&");
                for (String param : params) {
                    try {
                        String parameterName = URLDecoder.decode(param.split("=")[0], "UTF-8");
                        String parameterValue = URLDecoder.decode(param.split("=")[1], "UTF-8");
                        if (queryParameters.containsKey(parameterName)) {
                            List<String> values = queryParameters.get(parameterName);
                            values.add(parameterValue);
                        } else {
                            List<String> values = new ArrayList<>();
                            values.add(parameterValue);
                            queryParameters.put(parameterName, values);
                        }
                    } catch (UnsupportedEncodingException uee) {
                        throw new RuntimeException(uee);
                    }
                }
            }
        }
        if (queryParameters != null) {
            result = queryParameters.get(name) != null
                    ? queryParameters.get(name).get(0) : null;
        }
        return result;
    }

    /**
     * @see HttpServerRequest#getQueryString() 
     */
    @Override
    public String getQueryString() {
        return queryString;
    }

    /**
     * Get the remote address.
     *
     * @return the remote address.
     */
    @Override
    public String getRemoteAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    /**
     * @see HttpServerRequest#getRemoteHostname() 
     */
    @Override
    public String getRemoteHostname() {
        return socket.getInetAddress().getHostName();
    }

    /**
     * @see HttpServerRequest#getRemotePort() 
     */
    @Override
    public int getRemotePort() {
        return socket.getPort();
    }

    /**
     * @see HttpServerRequest#getRequestTarget() 
     */
    @Override
    public String getRequestTarget() {
        return requestTarget;
    }

    /**
     * Set the method.
     *
     * @param method the method.
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Set the request target.
     *
     * @param requestTarget the request target.
     */
    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }

    /**
     * Parse the request.
     *
     * @param request the request.
     */
    private void parse() {
        try {
            InputStream parseStream = socket.getInputStream();
            StringBuilder line = new StringBuilder();
            int read = parseStream.read();
            boolean requestLineParsed = false;
            if (read != -1) {
                while (read != -1 && parseStream.available() > 0) {
                    if ('\r' != (char) read) {
                        line.append((char) read);
                    }
                    read = parseStream.read();
                    if ('\n' == (char) read) {
                        if (line.length() > 0) {
                            if (!requestLineParsed) {
                                parseRequestLine(line.toString());
                                requestLineParsed = true;
                            } else {
                                parseHeader(line.toString());
                            }
                            line = new StringBuilder();
                            read = parseStream.read();
                        } else {
                            read = -1;
                        }
                    }
                }
            }
        } catch (IOException exception) {
            if (LOGGER.isLoggable(WARNING)) {
                LOGGER.log(WARNING, "An I/O error occurred while parsing the request", exception);
            }
        }
    }

    /**
     * Parse the header.
     *
     * @param request the request.
     * @param line the header line.
     */
    private void parseHeader(String line) {
        String name = line.substring(0, line.indexOf(':')).trim();
        String value = line.substring(line.indexOf(':') + 1).trim();
        addHeader(name, value);
    }

    /**
     * Parse the request line.
     *
     * @param request the request.
     * @param line the request line.
     */
    private void parseRequestLine(String line) {
        int index = line.indexOf(' ');
        setMethod(line.substring(0, index));
        line = line.substring(index + 1);
        index = line.indexOf(' ');
        requestTarget = line.substring(0, index);
        if (requestTarget.contains("?")) {
            queryString = requestTarget.substring(requestTarget.indexOf("?") + 1);
            requestTarget = requestTarget.substring(0, requestTarget.indexOf("?"));
        }
    }
}
