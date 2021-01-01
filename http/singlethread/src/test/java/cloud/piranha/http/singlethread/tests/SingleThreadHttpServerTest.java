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
package cloud.piranha.http.singlethread.tests;

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.api.HttpServerProcessor;
import cloud.piranha.http.singlethread.SingleThreadHttpServer;
import cloud.piranha.http.tests.HttpServerTest;
import cloud.piranha.http.tests.TestHttpServerProcessor;

/**
 * The JUnit tests for the SingleThreadHttpServer class.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SingleThreadHttpServerTest extends HttpServerTest {

    /**
     * Create the Single-thread HTTP server.
     *
     * @param portNumber the port number.
     * @return the Single-thread HTTP server.
     */
    @Override
    protected HttpServer createServer(int portNumber) {
        SingleThreadHttpServer server = new SingleThreadHttpServer(portNumber);
        server.setHttpServerProcessor(new TestHttpServerProcessor());
        return server;
    }

    /**
     * Create the Single-thread HTTP server.
     *
     * @param portNumber the port number.
     * @param processor the HTTP server processor.
     * @return the Single-thread HTTP server.
     */
    @Override
    protected HttpServer createServer(int portNumber, HttpServerProcessor processor) {
        return new SingleThreadHttpServer(portNumber, processor);
    }
}
