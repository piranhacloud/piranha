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
package cloud.piranha.http.netty;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.lang.System.Logger.Level;
import java.lang.System.Logger;

import cloud.piranha.http.api.HttpServerProcessor;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * The Netty Handler used by the Netty implementation of HTTP Server.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * Stores the logger.
     */
    private static final Logger LOGGER = System.getLogger(NettyHttpServerHandler.class.getName());

    /**
     * Stores the HTTP server processor.
     */
    private final HttpServerProcessor httpServerProcessor;
    
    /**
     * Stores the secure flag.
     */
    private final boolean secure;

    /**
     * Constructor.
     *
     * @param httpServerProcessor the HTTP server processor.
     * @param secure the secure flag.
     */
    public NettyHttpServerHandler(HttpServerProcessor httpServerProcessor, boolean secure) {
        this.httpServerProcessor = httpServerProcessor;
        this.secure = secure;
    }

    /**
     * Complete the channel read.
     *
     * @param context the context.
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    /**
     * Read the channel.
     *
     * @param context the context.
     * @param object the object read.
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, FullHttpRequest object) {
        NettyHttpServerRequest nettyRequest = new NettyHttpServerRequest(context, object, secure);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, true);
        NettyHttpServerResponse nettyResponse = new NettyHttpServerResponse(response);
        httpServerProcessor.process(nettyRequest, nettyResponse);
        ChannelFuture future = context.channel().writeAndFlush(response);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Handle the exception.
     *
     * @param context the context.
     * @param throwable the throwable.
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) {
        LOGGER.log(Level.WARNING, "Exception caught in NettyHttpServerHandler", throwable);
        context.close();
    }
}
