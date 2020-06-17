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

import cloud.piranha.http.api.HttpServerProcessor;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

/**
 * The Netty Initializer used by the Netty implementation of HTTP Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
@Sharable
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = Logger.getLogger(NettyHttpServerInitializer.class.getName());

    /**
     * Stores the HTTP server processor.
     */
    private final HttpServerProcessor httpServerProcessor;

    /**
     * Stores the ssl flag
     */
    private final boolean ssl;

    /**
     * Constructor.
     * 
     * @param httpServerProcessor the HTTP server processor.
     * @param ssl the ssl flag
     */
    public NettyHttpServerInitializer(HttpServerProcessor httpServerProcessor, boolean ssl) {
        this.httpServerProcessor = httpServerProcessor;
        this.ssl = ssl;
    }

    /**
     * Initialize the channel.
     *
     * @param channel the channel.
     */
    @Override
    public void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        if (ssl) {
            try {
                SSLContext sslContext = SSLContext.getDefault();
                SSLEngine sslEngine = sslContext.createSSLEngine();
                sslEngine.setUseClientMode(false);
                pipeline.addLast(new SslHandler(sslEngine));
            } catch (NoSuchAlgorithmException e) {
                if (LOGGER.isLoggable(SEVERE)) {
                    LOGGER.log(WARNING, "Unable to match SSL algorithm", e);
                }
            }
        }
        pipeline.addLast(new HttpRequestDecoder());
        pipeline.addLast(new HttpResponseEncoder());
        pipeline.addLast(new HttpObjectAggregator(10*1024*1024));
        pipeline.addLast(new NettyHttpServerHandler(httpServerProcessor));
    }
}
