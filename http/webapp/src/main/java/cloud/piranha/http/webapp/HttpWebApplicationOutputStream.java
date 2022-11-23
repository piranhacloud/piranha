/*
 * Copyright (c) 2002-2022 Manorrock.com. All Rights Reserved.
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
package cloud.piranha.http.webapp;

import cloud.piranha.core.api.WebApplication;
import cloud.piranha.core.api.WebApplicationResponse;
import cloud.piranha.core.impl.DefaultWebApplicationOutputStream;
import cloud.piranha.http.api.HttpServerResponse;
import jakarta.servlet.ServletRequest;
import static jakarta.servlet.http.HttpServletResponse.SC_SWITCHING_PROTOCOLS;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * The HttpWebApplication variant of WebApplicationOutputStream.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class HttpWebApplicationOutputStream extends DefaultWebApplicationOutputStream {

    /**
     * Stores the HttpServerResponse.
     */
    private final HttpServerResponse httpServerResponse;

    /**
     * Constructor.
     *
     * @param response the WebApplicationResponse.
     * @param httpServerResponse the HttpServerResponse.
     */
    public HttpWebApplicationOutputStream(
            WebApplicationResponse response,
            HttpServerResponse httpServerResponse) {
        this.response = response;
        this.httpServerResponse = httpServerResponse;
        setOutputStream(httpServerResponse.getOutputStream());
    }

    @Override
    public void close() throws IOException {
        super.close();
        /*
         * If we were upgraded and we are now closing the output stream it means
         * we are done so we should unlink the request and response.
         */
        if (response.getStatus() == SC_SWITCHING_PROTOCOLS) {
            WebApplication webApplication = response.getWebApplication();
            ServletRequest request = webApplication.getRequest(response);
            webApplication.unlinkRequestAndResponse(request, response);
        }
    }

    /**
     * Format the timestamp to a GMT string.
     *
     * @param timestamp the timestamp.
     * @return the GMT string.
     */
    private String formatDateToGMT(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.of("GMT"))
                .format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }

    @Override
    public void writeStatusLine() throws IOException {
        httpServerResponse.setStatus(response.getStatus());
        httpServerResponse.writeStatusLine();
    }

    @Override
    public void writeHeaders() throws IOException {

        /*
         * Add Content-Type.
         */
        if (response.getContentType() != null) {
        StringBuilder contentType = new StringBuilder();
            contentType.append(response.getContentType());
            httpServerResponse.addHeader("Content-Type", contentType.toString());
        }

        /**
         * Add Content-Language.
         */
        if (response.getContentLanguage() != null) {
            httpServerResponse.addHeader("Content-Language", response.getContentLanguage());
        }

        /**
         * Add cookies.
         */
        response.getCookies().forEach(cookie -> {
            StringBuilder value = new StringBuilder();
            value.append(cookie.getName())
                    .append("=");
            if (cookie.getValue() != null) {
                value.append(cookie.getValue());
            }
            if (cookie.getMaxAge() > -1) {
                value.append("; Max-Age=").append(cookie.getMaxAge());
                String expireDate = formatDateToGMT(Instant.now().plusSeconds(cookie.getMaxAge()).toEpochMilli());
                value.append("; Expires=").append(expireDate);
            }
            if (cookie.getSecure()) {
                value.append("; Secure");
            }
            if (cookie.isHttpOnly()) {
                value.append("; HttpOnly");
            }
            if (cookie.getPath() != null) {
                value.append("; Path=").append(cookie.getPath());
            }
            if (cookie.getVersion() > 0) {
                value.append("; Version=").append(cookie.getVersion());
            }
            httpServerResponse.addHeader("Set-Cookie", value.toString());
        });

        /**
         * Add remaining headers.
         */
        response.getHeaderNames().forEach(
                name -> httpServerResponse.addHeader(name, response.getHeader(name)));

        /**
         * Write the headers to the HttpServerResponse.
         */
        httpServerResponse.writeHeaders();
    }
}
