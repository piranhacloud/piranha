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
package cloud.piranha.http.api;

/**
 * The HTTP Server API.
 *
 * <p>
 *  This API makes it possible to interact with an HTTP server implementation on
 * a very basic level. It supports the following:
 * </p>
 * <ol>
 *  <li>Starting the server.</li>
 *  <li>Stopping the server.</li>
 *  <li>Checking if the server is running.</li>
 *  <li>Set/get the port of the server.</li>
 *  <li>Set/get the SSL flag.</li>
 *  <li>Set/get the HttpServerProcessor.</li>
 * </ol>
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public interface HttpServer {

    /**
     * Check if the server is running.
     *
     * @return true if it is, false otherwise.
     */
    boolean isRunning();

    /**
     * Start the server.
     */
    void start();

    /**
     * Stop the server.
     */
    void stop();

    /***
     * Get the server port
     * @return the server port
     */
    int getServerPort();

    /***
     * Set the server port
     * @param serverPort the port
     */
    void setServerPort(int serverPort);

    /***
     * Get the SSL flag
     * @return the server port
     */

    boolean getSSL();

    /***
     * Set the SSL flag
     * @param ssl the SSL flag
     */
    void setSSL(boolean ssl);

    /***
     * Get the http server processor
     * @return the http server processor
     */
    HttpServerProcessor getHttpServerProcessor();

    /***
     * Set the http server processor
     * @param httpServerProcessor the http server processor
     */
    void setHttpServerProcessor(HttpServerProcessor httpServerProcessor);
}
