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
package cloud.piranha;

import cloud.piranha.api.HttpServerResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The default HttpServerResponse.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServerResponse implements HttpServerResponse {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(DefaultHttpServerResponse.class.getName());

    /**
     * Stores the headers.
     */
    private final Map<String, String> headers;

    /**
     * Stores the output stream.
     */
    private OutputStream outputStream;

    /**
     * Stores the socket.
     */
    private final Socket socket;

    /**
     * Stores the status.
     */
    private int status;

    /**
     * Constructor.
     *
     * @param socket the socket.
     */
    public DefaultHttpServerResponse(Socket socket) {
        this.headers = new ConcurrentHashMap<>(1);
        this.socket = socket;
    }

    /**
     * Get the output stream.
     *
     * @return the output stream.
     */
    @Override
    public OutputStream getOutputStream() {
        OutputStream result = outputStream;

        if (outputStream == null) {
            try {
                outputStream = socket.getOutputStream();
                result = outputStream;
            } catch (IOException exception) {
                LOGGER.log(Level.WARNING, "An I/O error occurred while acquiring the output stream", exception);
            }
        }

        return result;
    }

    /**
     * Add the header.
     *
     * @param name the name.
     * @param value the value.
     */
    @Override
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    /**
     * Set the status.
     *
     * @param status the status.
     */
    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Write the header.
     *
     * @param name the header name.
     * @throws IOException when an I/O error occurs.
     */
    private void writeHeader(String name, boolean last) throws IOException {
        OutputStream output = getOutputStream();
        output.write(name.getBytes());
        output.write(": ".getBytes());
        output.write(headers.get(name).getBytes());
        if (!last) {
            output.write(",".getBytes());
        }
        output.write("\n".getBytes());
    }

    /**
     * Write the headers.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void writeHeaders() throws IOException {
        Iterator<String> names = headers.keySet().iterator();
        while (names.hasNext()) {
            String name = names.next();
            if (names.hasNext()) {
                writeHeader(name, false);
            } else {
                writeHeader(name, true);
            }
        }
        OutputStream output = getOutputStream();
        output.write("\n".getBytes());
    }

    /**
     * Write the status line.
     *
     * @throws IOException when an I/O error occurs.
     */
    @Override
    public void writeStatusLine() throws IOException {
        OutputStream output = getOutputStream();
        output.write("HTTP/1.0".getBytes());
        output.write(" ".getBytes());
        output.write(Integer.toString(status).getBytes());
        output.write("\n".getBytes());
    }
}
