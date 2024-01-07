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
package cloud.piranha.http.crac;

import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.api.HttpServerProcessor;
import java.lang.System.Logger;
import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;

/**
 * The CRaC integration for the HttpServer API.
 */
public class CracHttpServer implements HttpServer, Resource {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(CracHttpServer.class.getName());

    /**
     * Stores the delegate.
     */
    private HttpServer delegate;
    
    /**
     * Stores the checkpoint flag.
     */
    private boolean checkpointing;

    /**
     * Constructor.
     *
     * @param delegate the delegate.
     */
    public CracHttpServer(HttpServer delegate) {
        this.delegate = delegate;
        Core.getGlobalContext().register(this);
    }

    @Override
    public void afterRestore(Context<? extends Resource> context) throws Exception {
        delegate.start();
        checkpointing = false;
    }

    @Override
    public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
        checkpointing = true;
        LOGGER.log(Logger.Level.INFO, "Stopping HTTP server");
        delegate.stop();
        LOGGER.log(Logger.Level.INFO, "Stopped HTTP server");
        LOGGER.log(Logger.Level.INFO, "Giving sockets time to close");
        try {
            Thread.sleep(30000);
        } catch(InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        LOGGER.log(Logger.Level.INFO, "Sockets should be closed");
    }

    @Override
    public HttpServerProcessor getHttpServerProcessor() {
        return delegate.getHttpServerProcessor();
    }

    @Override
    public boolean getSSL() {
        return delegate.getSSL();
    }

    @Override
    public int getServerPort() {
        return delegate.getServerPort();
    }

    @Override
    public boolean isRunning() {
        boolean running = true;
        if (!checkpointing) {
            running = delegate.isRunning();
        }
        return running;
    }

    @Override
    public void setHttpServerProcessor(HttpServerProcessor httpServerProcessor) {
        delegate.setHttpServerProcessor(httpServerProcessor);
    }

    @Override
    public void setSSL(boolean ssl) {
        delegate.setSSL(ssl);
    }

    @Override
    public void setServerPort(int serverPort) {
        delegate.setServerPort(serverPort);
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void stop() {
        delegate.stop();
    }
}
