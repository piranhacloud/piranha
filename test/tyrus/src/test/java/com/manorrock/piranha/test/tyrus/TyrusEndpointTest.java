/*
 *  Copyright (c) 2002-2018, Manorrock.com. All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *      1. Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *
 *      2. Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */
package com.manorrock.piranha.test.tyrus;

import com.manorrock.piranha.DefaultAliasedDirectoryResource;
import com.manorrock.piranha.DefaultHttpServer;
import com.manorrock.piranha.DefaultWebApplication;
import com.manorrock.piranha.DefaultWebApplicationServer;
import com.manorrock.piranha.api.HttpServer;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketExtension;
import com.neovisionaries.ws.client.WebSocketFactory;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A JUnit test for TyrusEndpoint.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class TyrusEndpointTest {
    
    /**
     * Test process method.
     * 
     * <p>
     *  The test is set to be ignored as our async support is not ready for
     *  Tyrus yet. So we need to finish it first before we can attempt to do
     *  again.
     * </p>
     *
     * @throws Exception when a serious error occurs.
     */
    @Test
    @Ignore
    public void testProcess() throws Exception {
        DefaultWebApplicationServer server = new DefaultWebApplicationServer();
        HttpServer httpServer = new DefaultHttpServer(7001, server);
        DefaultWebApplication webApp = new DefaultWebApplication();
        webApp.addResource(new DefaultAliasedDirectoryResource(new File("src/main/webapp"), ""));
        webApp.setContextPath("");
        Set<Class<?>> classes = new HashSet<>();
        classes.add(TyrusEndpoint.class);
        webApp.addInitializer("org.glassfish.tyrus.servlet.TyrusServletContainerInitializer", classes);
        server.addWebApplication(webApp);
        server.initialize();
        server.start();
        httpServer.start();
        
        final StringBuilder response = new StringBuilder();
        WebSocketFactory factory = new WebSocketFactory();
        WebSocket webSocket = factory.createSocket("ws://localhost:7001/echo").addListener(new WebSocketAdapter() {
            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {
                response.append(text);
            }
        }).addExtension(WebSocketExtension.PERMESSAGE_DEFLATE).connect();
        webSocket.sendText("Echo");
        webSocket.disconnect();
        
        httpServer.stop();
        server.stop();
        assertEquals("Echo", response.toString());
    }
}
