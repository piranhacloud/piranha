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
package cloud.piranha.http.impl;

import static java.lang.System.Logger.Level.WARNING;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.lang.System.Logger;

import cloud.piranha.http.api.HttpServerResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The default implementation of HTTP Server Response.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServerResponse implements HttpServerResponse {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(
            DefaultHttpServerResponse.class.getPackageName());

    /**
     * Stores the HTTP version.
     */
    private String httpVersion = "HTTP/1.0";

    /**
     * Stores the headers.
     */
    private final Map<String, List<String>> headers;

    /**
     * Stores the output stream.
     */
    private OutputStream outputStream;
    
    /**
     * Stores the reason phrase.
     */
    private String reasonPhrase;

    /**
     * Stores the socket.
     */
    private final Socket socket;

    /**
     * Stores the status.
     */
    private int statusCode;

    /**
     * Constructor.
     *
     * @param socket the socket.
     */
    public DefaultHttpServerResponse(Socket socket) {
        this.headers = new HashMap<>(1);
        this.socket = socket;
    }

    @Override
    public void addHeader(String name, String value) {
        List<String> values = headers.get(name);
        if (values == null) {
            synchronized (this) {
                values = new ArrayList<>();
            }
        }
        values.add(value);
    }

    @Override
    public void closeResponse() throws IOException {
        HttpServerResponse.super.closeResponse();
        socket.close();
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name) == null ? null : headers.get(name).get(0);
    }

    @Override
    public OutputStream getOutputStream() {
        if (outputStream == null) {
            try {
                outputStream = socket.getOutputStream();
            } catch (IOException exception) {
                LOGGER.log(WARNING, () -> "An I/O error occurred while acquiring the output stream", exception);
            }
        }

        return outputStream;
    }

    @Override
    public void setHeader(String name, String value) {
        ArrayList<String> values = new ArrayList<>();
        values.add(value);
        headers.put(name, values);
    }

    @Override
    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    @Override
    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Write the header.
     *
     * @param name the header name.
     * @throws IOException when an I/O error occurs.
     */
    private void writeHeader(String name) throws IOException {
        OutputStream output = getOutputStream();
        output.write(name.getBytes());
        output.write(": ".getBytes());
        Iterator<String> values = headers.get(name).iterator();
        while (values.hasNext()) {
            output.write(values.next().getBytes());
            if (values.hasNext()) {
                output.write(",".getBytes());
            }
        }
        output.write("\n".getBytes());
    }

    @Override
    public void writeHeaders() throws IOException {
        Iterator<String> names = headers.keySet().iterator();
        while (names.hasNext()) {
            String name = names.next();
            writeHeader(name);
        }
        OutputStream output = getOutputStream();
        output.write("\n".getBytes());
    }

    @Override
    public void writeStatusLine() throws IOException {
        OutputStream output = getOutputStream();
        output.write(httpVersion.getBytes());
        output.write(" ".getBytes());
        output.write(Integer.toString(statusCode).getBytes());
        if (reasonPhrase != null) {
            output.write(" ".getBytes());
            output.write(reasonPhrase.getBytes());
        }
        output.write("\n".getBytes());
    }
}
