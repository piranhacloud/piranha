/*
 * Copyright (c) 2002-2024 Manorrock.com. All Rights Reserved.
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
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import static java.lang.System.Logger.Level.WARNING;
import java.lang.System.Logger;
import javax.net.ssl.SSLSocket;

/**
 * The default implementation of HttpServerRequest.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServerRequest implements HttpServerRequest {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(
            DefaultHttpServerRequest.class.getName());

    /**
     * Stores the headers.
     */
    private final Map<String, List<String>> headers;

    /**
     * Stores the input stream.
     */
    private InputStream inputStream;

    /**
     * Stores the method.
     */
    private String method;

    /**
     * Stores the request target.
     */
    private String requestTarget;

    /**
     * Stores the socket.
     */
    private final Socket socket;

    /**
     * Stores the protocol
     */
    private String protocol;

    /**
     * Constructor.
     *
     * @param socket the socket.
     */
    public DefaultHttpServerRequest(Socket socket) {
        this.headers = new HashMap<>(1);
        this.socket = socket;
        protocol = "HTTP/1.1";
        parse();
    }

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     */
    public void addHeader(String name, String value) {
        if (!headers.containsKey(name.toUpperCase())) {
            ArrayList<String> values = new ArrayList<>();
            values.add(value);
            headers.put(name.toUpperCase(), values);
        } else {
            headers.get(name.toUpperCase()).add(value);
        }
    }

    @Override
    public String getHeader(String name) {
        String header = null;
        if (headers.get(name.toUpperCase()) != null) {
            header = headers.get(name.toUpperCase()).isEmpty() ? null : headers.get(name.toUpperCase()).get(0);
        }
        return header;
    }

    @Override
    public Iterator<String> getHeaderNames() {
        return headers.keySet().iterator();
    }

    @Override
    public Iterator<String> getHeaders(String name) {
        return headers.get(name.toUpperCase()) == null
                ? new ArrayList<String>().iterator()
                : headers.get(name.toUpperCase()).iterator();
    }

    @Override
    public InputStream getInputStream() {
        InputStream result = inputStream;

        if (inputStream == null) {
            try {
                inputStream = socket.getInputStream();
                result = inputStream;
            } catch (IOException exception) {
                LOGGER.log(WARNING, "An I/O error occurred while acquiring input stream", exception);
            }
        }

        return result;
    }

    @Override
    public String getLocalAddress() {
        return socket.getLocalAddress().getHostAddress();
    }

    @Override
    public String getLocalHostname() {
        return socket.getLocalAddress().getHostName();
    }

    @Override
    public int getLocalPort() {
        return socket.getLocalPort();
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getRemoteAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    @Override
    public String getRemoteHostname() {
        return socket.getInetAddress().getHostName();
    }

    @Override
    public int getRemotePort() {
        return socket.getPort();
    }

    @Override
    public String getRequestTarget() {
        return requestTarget;
    }

    @Override
    public boolean isSecure() {
        return socket instanceof SSLSocket;
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
     */
    private void parse() {
        if (socket != null) {
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
                LOGGER.log(WARNING, "An I/O error occurred while parsing the request", exception);
            }
        }
    }

    /**
     * Parse the header.
     *
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
     * @param line the request line.
     */
    private void parseRequestLine(String line) {
        int index = line.indexOf(' ');
        setMethod(line.substring(0, index));
        line = line.substring(index + 1);
        index = line.indexOf(' ');
        requestTarget = line.substring(0, index);
        String protocolRequestLine = line.substring(index + 1);
        if (!protocol.isEmpty()) {
            protocol = protocolRequestLine;
        }
    }
}
