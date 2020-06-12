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
package cloud.piranha.http.netty;

import cloud.piranha.http.impl.DefaultHttpServerProcessor;
import cloud.piranha.http.api.HttpServer;
import cloud.piranha.http.api.HttpServerProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.logging.Logger;

/**
 * The Netty implementation of HTTP Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NettyHttpServer implements HttpServer {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NettyHttpServer.class.getName());

    /**
     * Stores the boss event loop group.
     */
    private EventLoopGroup bossGroup;

    /**
     * Stores the HTTP server processor.
     */
    private HttpServerProcessor httpServerProcessor;

    /**
     * Stores the server port.
     */
    private int serverPort;

    /**
     * Stores the worker event loop group.
     */
    private EventLoopGroup workerGroup;

    /**
     * Stores the SSL flag
     */
    private boolean ssl;


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
     * @see HttpServer#isRunning() 
     */
    @Override
    public boolean isRunning() {
        return (bossGroup != null && workerGroup != null);
    }

    /**
     * @see HttpServer#start() 
     */
    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyHttpServerInitializer(httpServerProcessor, ssl))
                .bind(serverPort).awaitUninterruptibly();
    }

    /**
     * @see HttpServer#stop() 
     */
    @Override
    public void stop() {
        workerGroup.shutdownGracefully().awaitUninterruptibly();
        bossGroup.shutdownGracefully().awaitUninterruptibly();
        bossGroup = null;
        workerGroup = null;
    }

    @Override
    public int getServerPort() {
        return serverPort;
    }

    @Override
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public void setSSL(boolean ssl) {
        this.ssl = ssl;
    }

    @Override
    public boolean getSSL() {
        return ssl;
    }

    @Override
    public void setHttpServerProcessor(HttpServerProcessor httpServerProcessor) {
        this.httpServerProcessor = httpServerProcessor;
    }

    @Override
    public HttpServerProcessor getHttpServerProcessor() {
        return httpServerProcessor;
    }
}
