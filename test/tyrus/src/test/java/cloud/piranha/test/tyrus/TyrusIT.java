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
package cloud.piranha.test.tyrus;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * An integration test to verify running a exploded web application coming from
 * a supplied WAR.
 *
 * @author Manfred Riem (mriem@manorrock.com)
 */
class TyrusIT {

    /**
     * Test configure method.
     *
     * @throws Exception when an error occurs.
     */
    @Test
    @Disabled
    void testConfigure() throws Exception {
        /*
        final MicroPiranha piranha = new MicroPiranha();
        WebApplication webApplication = piranha.configure(new String[]{
            "--webapp", "target/tyrus-exploded", "--war", "target/tyrus.war"});
        DefaultWebApplicationExtensionContext context = new DefaultWebApplicationExtensionContext();
        context.add(ServletExtension.class);
        context.configure(webApplication);
        Thread thread = new Thread(piranha);
        thread.start();

//        HttpClient client = HttpClient.newHttpClient();
//        WebSocket webSocket = client.newWebSocketBuilder()
//                .buildAsync(URI.create("ws://localhost:8080/endpoint"), new Listener() {
//                    @Override
//                    CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
//                        return webSocket.sendText(data, true);
//                    }
//                }).join();
//        Object result = webSocket.sendText("message", true).get();
//        assertEquals("message", result);

        piranha.stop();
        Thread.sleep(3000);
        */
    }
}
