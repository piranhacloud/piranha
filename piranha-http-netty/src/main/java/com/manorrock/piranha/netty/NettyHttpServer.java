/*
 * Copyright (c) 2002-2019 Manorrock.com. All Rights Reserved.
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
package com.manorrock.piranha.netty;

import com.manorrock.piranha.DefaultHttpServerProcessor;
import com.manorrock.piranha.api.HttpServer;
import com.manorrock.piranha.api.HttpServerProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * The Netty HTTP server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NettyHttpServer implements HttpServer {

    /**
     * Stores the boss event loop group.
     */
    private EventLoopGroup bossGroup;
    
    /**
     * Stores the HTTP server processor.
     */
    private final HttpServerProcessor httpServerProcessor;

    /**
     * Stores the server port.
     */
    private final int serverPort;

    /**
     * Stores the worker event loop group.
     */
    private EventLoopGroup workerGroup;

    /**
     * Constructor.
     */
    public NettyHttpServer() {
        httpServerProcessor = new DefaultHttpServerProcessor();
        serverPort = 8080;
    }

    /**
     * Constructor.
     *
     * @param serverPort the server port.
     */
    public NettyHttpServer(int serverPort) {
        this.httpServerProcessor = new DefaultHttpServerProcessor();
        this.serverPort = serverPort;
    }

    /**
     * Constructor.
     *
     * @param serverPort the server port.
     * @param httpServerProcessor the HTTP server processor.
     */
    public NettyHttpServer(int serverPort, HttpServerProcessor httpServerProcessor) {
        this.httpServerProcessor = httpServerProcessor;
        this.serverPort = serverPort;
    }

    /**
     * Is the server running?
     *
     * @return true if running, false otherwise.
     */
    @Override
    public boolean isRunning() {
        return (bossGroup != null && workerGroup != null);
    }

    /**
     * Start the server.
     */
    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyHttpServerInitializer(httpServerProcessor))
                .bind(serverPort);
    }

    /**
     * Stops the server.
     */
    @Override
    public void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        bossGroup = null;
        workerGroup = null;
    }
}
