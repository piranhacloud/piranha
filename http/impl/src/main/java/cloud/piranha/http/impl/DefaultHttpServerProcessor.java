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

import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.api.HttpServerProcessorEndState;
import static cloud.piranha.http.api.HttpServerProcessorEndState.COMPLETED;
import cloud.piranha.http.api.HttpServerRequest;
import cloud.piranha.http.api.HttpServerResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.System.Logger;
import static java.lang.System.Logger.Level.ERROR;

/**
 * The default implementation of a HTTP Server Processor.
 *
 * <p>
 * This HTTP Server Processor will either show the user with a directory
 * listing, or stream back contents of the file clicked on in the browser, or it
 * will return a 404 error because neither could be found.
 * </p>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class DefaultHttpServerProcessor implements HttpServerProcessor {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(
            DefaultHttpServerProcessor.class.getName());
    
    /**
     * Stores the error writing response message.
     */
    private static final String IO_ERROR_WRITING_RESPONSE = 
            "An I/O error occurred while writing the response";

    /**
     * Constructor.
     */
    public DefaultHttpServerProcessor() {
    }

    @Override
    public HttpServerProcessorEndState process(HttpServerRequest request, HttpServerResponse response) {
        response.setStatus(200);
        File baseDir = new File(System.getProperty("user.dir"));
        File file = new File(baseDir, request.getRequestTarget());
        if (file.exists() && file.isDirectory() && "GET".equals(request.getMethod())) {
            response.setHeader("Content-Type", "text/html");
            try {
                response.writeStatusLine();
                response.writeHeaders();
                OutputStream outputStream = response.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream);
                writer.println("<html><head></head><body>");
                writer.println(file.getName() + "<br/>");
                writer.print("<a href=\".\">.</a><br/>");
                writer.print("<a href=\"..\">..</a><br/>");
                File[] fileList = file.listFiles();
                if (fileList != null && fileList.length > 0) {
                    for (int i = 0; i < fileList.length; i++) {
                        String uri = fileList[i].getAbsolutePath().substring(baseDir.getAbsolutePath().length());
                        if (uri.startsWith("//")) {
                            uri = uri.substring(1);
                        }
                        writer.println("<a href=\"http://" + request.getLocalHostname() + ":"
                                + request.getLocalPort() + uri + "\">" + fileList[i].getName() + "</a><br/>"
                        );
                    }
                }
                writer.println("</body></html>");
                writer.flush();
            } catch (IOException exception) {
                LOGGER.log(ERROR, IO_ERROR_WRITING_RESPONSE, exception);
            }
        } else if (file.exists() && !file.isDirectory()) {
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Length", Long.toString(file.length()));
            try {
                response.writeStatusLine();
                response.writeHeaders();
                OutputStream outputStream = response.getOutputStream();
                try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                    int read = inputStream.read();
                    while (read != -1) {
                        outputStream.write((char) read);
                        read = inputStream.read();
                    }
                    outputStream.flush();
                }
            } catch (IOException exception) {
                LOGGER.log(ERROR, IO_ERROR_WRITING_RESPONSE, exception);
            }
        } else {
            try {
                response.setStatus(404);
                response.writeStatusLine();
                response.writeHeaders();
            } catch (IOException exception) {
                LOGGER.log(ERROR, IO_ERROR_WRITING_RESPONSE, exception);
            }
        }

        return COMPLETED;
    }
}
