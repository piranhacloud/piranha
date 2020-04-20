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
package cloud.piranha.http.undertow;

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.api.HttpServerTest;

/**
 * The JUnit tests for the UndertowHttpServer class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class UndertowHttpServerTest extends HttpServerTest {

    /**
     * Create the Undertow HTTP server.
     * 
     * @param portNumber the port number.
     * @return the Undertow HTTP server.
     */
    @Override
    protected HttpServer createServer(int portNumber) {
        return new UndertowHttpServer(portNumber);
    }

    /**
     * Create the Underow HTTP server.
     * 
     * @param portNumber the port number.
     * @param processor the HTTP server processor.
     * @return the Undertow HTTP server.
     */
    @Override
    protected HttpServer createServer(int portNumber, HttpServerProcessor processor) {
        return new UndertowHttpServer(portNumber, processor);
    }
}
